package com.teenet.customer;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.teenet.common.ActionCode;
import com.teenet.common.GlobalParam;
import com.teenet.customer.entity.GuiJing1Entity;
import com.teenet.customer.entity.GuiJing2Entity;
import com.teenet.customer.entity.GuiJingEntity;
import com.teenet.threadpool.MyThreadPool;
import com.teenet.util.BigDecimalUtil;
import com.teenet.util.DateUtil;
import com.teenet.util.OkHttpUtil;
import com.teenet.util.WechatUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description 桂箐
 * @Author threedong
 * @Date 2022/7/14 10:22
 */
@Slf4j
@Service
public class GuiJing implements CustomerInterface {

    @Value("${server.port}")
    private String serverPort;

    private static Map<String, String> head = new HashMap<>();

    private String login() {
        String token = null;
        Map<String, Object> data = new HashMap<>(4);
        Map<String, Object> data1 = new HashMap<>(8);
        data1.put("ip", GlobalParam.cusLocalIp);
        data1.put("port", serverPort);
        data1.put("username", GlobalParam.cusUsername);
        data1.put("password", GlobalParam.cusPassword);
        data.put("data", data1);
        data.put("version", GlobalParam.cusVersion);
        String str = new Gson().toJson(data);
        String returnData = OkHttpUtil.method(OkHttpUtil.POST, GlobalParam.cusLoginUrl, null, str);
        if (null != returnData) {
            Map returnMap = new Gson().fromJson(returnData, Map.class);
            Integer error_code = (Integer) returnMap.get("error_code");
            if (error_code.equals(0)) {
                Map<String, String> data2 = (Map<String, String>) returnMap.get("data");
                token = data2.get("token");
                head.put("token", token);
            } else {
                Object errorMsg = returnMap.get("error_msg");
                MyThreadPool.EXECUTOR_SERVICE.execute(() -> WechatUtil.sendMsg("login " + errorMsg, ActionCode.DATA_PULL_ERROR.getDesc()));
                log.info("login error : " + errorMsg);
            }
        }
        return token;
    }

    private void logout() {
        Map<String, Object> data = new HashMap<>(4);
        data.put("data", "");
        data.put("version", GlobalParam.cusVersion);
        String logoutData = new Gson().toJson(data);
        OkHttpUtil.method(OkHttpUtil.POST, GlobalParam.cusLogoutUrl, null, logoutData);
    }


    @Override
    public Map<String, Double> getData() {
        Map<String, Double> fromCus = new HashMap<>(GlobalParam.realDevicesMapVal.size());
        //登入
        String token = login();
        if (null != token) {
            Map<String, Object> data = new HashMap<>(4);
            Map<String, Object> data1 = new HashMap<>(4);
            data1.put("device_ids", Lists.newArrayList());
            List<String> resourceIds = Lists.newArrayList(GlobalParam.realDevicesMapVal.keySet());
            data1.put("resource_ids", resourceIds);
            data.put("data", data1);
            data.put("version", GlobalParam.cusVersion);
            String str = new Gson().toJson(data);
            String returnData = OkHttpUtil.method(OkHttpUtil.POST, GlobalParam.cusRealTimeUrl, head, str);
            if (null != returnData) {
                Map returnMap = new Gson().fromJson(returnData, Map.class);
                Integer error_code2 = (Integer) returnMap.get("error_code");
                if (error_code2.equals(0)) {
                    Map<String, Object> data2 = (Map<String, Object>) returnMap.get("data");
                    String points = new Gson().toJson(data2.get("points"));
                    List<GuiJingEntity> list = new Gson().fromJson(points, new TypeToken<List<GuiJingEntity>>() {
                    }.getType());
                    list.forEach(item -> {
                        String resource_id = item.getResource_id();
                        String val = item.getReal_value();
                        Double coe = GlobalParam.realDevicesMapVal.get(resource_id);
                        Double last = BigDecimalUtil.mulWithDouAndStr(val, coe);
                        fromCus.put(resource_id, last);
                    });
                } else {
                    Object errorMsg = returnMap.get("error_msg");
                    MyThreadPool.EXECUTOR_SERVICE.execute(() -> WechatUtil.sendMsg("getData " + errorMsg, ActionCode.DATA_PULL_ERROR.getDesc()));
                    log.info("realData error : " + errorMsg);
                }
            }
        }
        //登出
        logout();
        return fromCus;
    }

    @Override
    public Map<String, Double> dataWithTime(String time) {
        Map<String, Double> fromCus = new HashMap<>(GlobalParam.realDevicesMapVal.size());
        long paramTime = DateUtil.stringToLocalDateTime(time, DateUtil.FORMAT1).toEpochSecond(ZoneOffset.of("+8"));
        String token = login();
        if (null != token) {
            Map<String, Object> data = new HashMap<>(4);
            Map<String, Object> data1 = new HashMap<>(8);
            data1.put("start", paramTime);
            data1.put("end", paramTime);
            data1.put("interval", "quarter");
            List<String> resourceIds = Lists.newArrayList(GlobalParam.realDevicesMapVal.keySet());
            data1.put("resource_ids", resourceIds);
            data.put("data", data1);
            data.put("version", GlobalParam.cusVersion);
            String str = new Gson().toJson(data);
            String returnData = OkHttpUtil.method(OkHttpUtil.POST, GlobalParam.cusHisTimeUrl, head, str);
            if (null != returnData) {
                Map returnMap = new Gson().fromJson(returnData, Map.class);
                Integer error_code2 = (Integer) returnMap.get("error_code");
                if (error_code2.equals(0)) {
                    String returnData2 = new Gson().toJson(returnMap.get("data"));
                    List<GuiJing1Entity> list = new Gson().fromJson(returnData2, new TypeToken<List<GuiJing1Entity>>() {
                    }.getType());
                    list.forEach(item -> {
                        String resource_id = item.getResource_id();
                        List<GuiJing2Entity> dataList = item.getData_list();
                        String val = dataList.get(0).getValue();
                        Double coe = GlobalParam.realDevicesMapVal.get(resource_id);
                        Double last = BigDecimalUtil.mulWithDouAndStr(val, coe);
                        fromCus.put(resource_id, last);
                    });
                } else {
                    Object errorMsg = returnMap.get("error_msg");
                    MyThreadPool.EXECUTOR_SERVICE.execute(() -> WechatUtil.sendMsg("dataWithTime " + errorMsg, ActionCode.DATA_PULL_ERROR.getDesc()));
                    log.info("realData error : " + errorMsg);
                }
            }
        }
        logout();
        return fromCus;
    }
}

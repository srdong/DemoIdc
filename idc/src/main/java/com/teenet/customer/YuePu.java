package com.teenet.customer;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.teenet.common.ActionCode;
import com.teenet.common.GlobalParam;
import com.teenet.customer.entity.YuePuEntity;
import com.teenet.threadpool.MyThreadPool;
import com.teenet.util.BigDecimalUtil;
import com.teenet.util.OkHttpUtil;
import com.teenet.util.WechatUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Description 月浦
 * @Author threedong
 * @Date 2022/7/14 10:22
 */
@Slf4j
@Service
public class YuePu implements CustomerInterface {

    @Override
    public Map<String, Double> getData() {
        Map<String, Double> fromCus = new HashMap<>(GlobalParam.realDevicesMapVal.size());
        login();
        if (null != session) {
            Map<String, Object> data = new HashMap<>(2);
            List<Map<String, String>> resourceIds = GlobalParam.realDevicesMapVal.keySet().stream().map(item -> {
                Map<String, String> map = new HashMap<>(2);
                map.put("resource_id", item);
                return map;
            }).collect(Collectors.toList());
            data.put("resources", resourceIds);
            String str = new Gson().toJson(data);
            String returnData = OkHttpUtil.method(OkHttpUtil.POST, GlobalParam.cusRealTimeUrl, head, str);
            if (null != returnData) {
                Map returnMap = new Gson().fromJson(returnData, Map.class);
                String error_code2 = (String) returnMap.get("error_code");
                if ("00".equals(error_code2)) {
                    Map<String, Object> data2 = (Map<String, Object>) returnMap.get("data");
                    String resources = new Gson().toJson(data2.get("resources"));
                    List<YuePuEntity> list = new Gson().fromJson(resources, new TypeToken<List<YuePuEntity>>() {
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
        // logout();
        return fromCus;
    }

    @Override
    public Map<String, Double> dataWithTime(String time) {
        return null;
    }

    private static Map<String, String> head = new HashMap<>();
    private static String session = null;

    private void login() {
        Map<String, Object> data = new HashMap<>(4);
        data.put("account", GlobalParam.cusUsername);
        data.put("password", GlobalParam.cusPassword);
        String str = new Gson().toJson(data);
        String returnData = OkHttpUtil.method(OkHttpUtil.POST, GlobalParam.cusLoginUrl, null, str);
        if (null != returnData) {
            Map returnMap = new Gson().fromJson(returnData, Map.class);
            String error_code = (String) returnMap.get("error_code");
            if ("00".equals(error_code)) {
                Map<String, Object> data2 = (Map<String, Object>) returnMap.get("data");
                session = (String) data2.get("session");
                Double id = (Double) data2.get("id");
                String name = (String) data2.get("name");
                String base64Name = Base64.encodeBase64String(name.getBytes(StandardCharsets.UTF_8));
                String account = (String) data2.get("account");
                String base64Account = Base64.encodeBase64String(account.getBytes(StandardCharsets.UTF_8));
                StringBuilder sb = new StringBuilder("X_GU_SID=")
                        .append(session)
                        .append(";")
                        .append("USER_ID=")
                        .append(id)
                        .append(";")
                        .append("ACCOUNT=")
                        .append(base64Account)
                        .append(";")
                        .append("USER_NAME=")
                        .append(base64Name);
                head.put("Cookie", sb.toString());
            } else {
                Object errorMsg = returnMap.get("error_msg");
                MyThreadPool.EXECUTOR_SERVICE.execute(() -> WechatUtil.sendMsg("login " + errorMsg, ActionCode.DATA_PULL_ERROR.getDesc()));
                log.info("login error : " + errorMsg);
            }
        }

    }

    private void logout() {
        Map<String, Object> data = new HashMap<>(4);
        data.put("session ", session);
        String logoutData = new Gson().toJson(data);
        OkHttpUtil.method(OkHttpUtil.POST, GlobalParam.cusLogoutUrl, null, logoutData);
    }
}

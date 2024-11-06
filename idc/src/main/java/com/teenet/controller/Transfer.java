package com.teenet.controller;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.gson.Gson;
import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.Expression;
import com.teenet.common.*;
import com.teenet.entity.TransferData;
import com.teenet.entity.database.Device;
import com.teenet.entity.database.PullTime;
import com.teenet.entity.database.SysLogs;
import com.teenet.entity.devices.*;
import com.teenet.service.DeviceService;
import com.teenet.service.DicService;
import com.teenet.service.PullTimeService;
import com.teenet.service.SysLogsService;
import com.teenet.threadpool.MyThreadPool;
import com.teenet.util.*;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Description
 * @Author threedong
 * @Date 2022/6/14 9:37
 */
@Slf4j
@Component
public class Transfer {

    @Autowired
    private DicService dicService;

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private PullTimeService pullTimeService;

    @Autowired
    private EncryptionUtil encryptionUtil;

    @Autowired
    private SysLogsService sysLogsService;

    /**
     * 数据上传
     *
     * @return
     */
    public Result<String> syncData(Object object, String url) {
        LocalDateTime now = LocalDateTime.now();
        String timeNow = DateUtil.localDateTimeToString(now, DateUtil.FORMAT1);
        TransferData transferData = TransferData.builder().data(object).time(timeNow).build();
        String str = new Gson().toJson(transferData);
        boolean println = null != GlobalParam.println && GlobalParam.println;
        if (println) {
            log.info(str);
        }
        String sec = encryptionUtil.rsa(str);
        if (ObjectUtils.isNull(sec)) {
            log.info("rsa error,time = " + timeNow);
            return Result.createByErrorMessage("rsa error,see in logs");
        }
        return uploadData(sec, url, true);
    }

    /**
     * 上传数据到互联网数据中心
     *
     * @param writeInFile 是否需要写入文件
     */
    private Result<String> uploadData(String sec, String url, boolean writeInFile) {
        String sign = encryptionUtil.signMd5(sec, GlobalParam.idcId);
        if (ObjectUtils.isNull(sign)) {
            log.info("md5 sign error");
            return Result.createByErrorMessage("md5 sign error,see in logs");
        }
        Map<String, String> headers = new HashMap<>(8);
        headers.put("req_sign", sign);
        headers.put("idc_id", GlobalParam.idcId);
        headers.put("Content-Type", "text/plain; charset=UTF-8");
        String result = OkHttpUtil.method(OkHttpUtil.POST, url, headers, sec);
        if (ObjectUtils.isNull(result)) {
            logs(url, "upload error , no result");
            //无法请求就将数据写入文件
            log.info("OkHttpUtil.post error，url = " + url);
            if (GlobalParam.highFrequencyUrl.equals(url) && writeInFile) {
                writeFile(sec);
            }
            MyThreadPool.EXECUTOR_SERVICE.execute(() -> WechatUtil.sendMsg("sendDataCenter(上送请求失败1)", ActionCode.DATA_SEND_ERROR.getDesc()));
            return Result.createByErrorCodeMessage(ResponseCode.WRITE_IN_FILE.getCode(), "message upload error,see in logs");
        } else {
            ReturnObject returnObject = new Gson().fromJson(result, ReturnObject.class);
            String code = returnObject.getCode();
            String logResult = returnObject.getCode() + "|" + returnObject.getMessage();
            if ("000".equals(code)) {
                logs(url, logResult);
                return Result.createBySuccess();
            } else {
                logs(url, logResult);
                log.info(" return error : " + logResult);
                MyThreadPool.EXECUTOR_SERVICE.execute(() -> WechatUtil.sendMsg("sendDataCenter(上送请求失败2)", ActionCode.DATA_SEND_ERROR.getDesc()));
                return Result.createByErrorMessage(returnObject.getMessage());
            }
        }
    }

    private void logs(String url, String result) {
        SysLogs sysLogs = new SysLogs();
        if (GlobalParam.idcInfoUrl.equals(url)) {
            sysLogs.setType(1);
            sysLogs.setName("基础信息");
        } else if (GlobalParam.deviceInfoUrl.equals(url)) {
            sysLogs.setType(2);
            sysLogs.setName("设备信息");
        } else if (GlobalParam.lowFrequencyUrl.equals(url)) {
            sysLogs.setType(3);
            sysLogs.setName("低频数据");
        } else if (GlobalParam.highFrequencyUrl.equals(url)) {
            sysLogs.setType(4);
            sysLogs.setName("高频数据");
        }
        sysLogs.setCreateTime(LocalDateTime.now());
        sysLogs.setContent(result);
        sysLogsService.save(sysLogs);
    }

    /**
     * 高频数据断点续传
     */
    public Integer breakPointUploadHighFrequency(@NotNull File file, String url) {
        Integer code = ResponseCode.ERROR.getCode();
        String path = file.getAbsolutePath();
        try {
            List<String> data = Files.readAllLines(Paths.get(path), StandardCharsets.UTF_8);
            if (CollectionUtils.isNotEmpty(data)) {
                String sec = String.join("", data);
                Result<String> result = uploadData(sec, url, false);
                code = result.getCode();
                boolean needToDel = code.equals(ResponseCode.SUCCESS.getCode());
                if (needToDel) {
                    //文件删除，并且更新上传数据
                    file.delete();
                    String[] split = file.getName().split("\\.");
                    String time = split[0];
                    LocalDateTime localDateTime = DateUtil.stringToLocalDateTime(time, DateUtil.FORMAT3);
                    pullTimeService.update(Wrappers.<PullTime>lambdaUpdate()
                            .set(PullTime::getUploadAgainTime, LocalDateTime.now())
                            .set(PullTime::getSuccessIs, true)
                            .eq(PullTime::getInTime, localDateTime)
                            .eq(PullTime::getType, 2));
                }
            }
        } catch (Exception exception) {
            MyThreadPool.EXECUTOR_SERVICE.execute(() -> WechatUtil.sendMsg("Transfer.breakPointUploadHighFrequency", ActionCode.UNKNOWN_EXCEPTION.getDesc()));
            log.info("breakPointUploadHighFrequency error");
            exception.printStackTrace();
        }
        return code;
    }

    /**
     * 对数据进行包装
     * 高频和仪表的数据
     *
     * @param list
     * @param fromCustomer 空 说明上传的是仪表数据， 不为空，说明上传的高频数据
     * @return
     */
    public Map<String, Object> packageData(List<Device> list, Map<Integer, Double> fromCustomer, String nowTime) {
        Map<String, Object> mapsObject = new HashMap<>(8);
        //本键为接入IDC 的全部电能，值不能为空
        IDC_EC idc_ec = IDC_EC.builder().build();
        mapsObject.put("IDC_EC", idc_ec);
        //全部的 IT 设备的耗电，不能为空，
        IDC_IT_EC idc_it_ec = IDC_IT_EC.builder().build();
        mapsObject.put("IDC_IT_EC", idc_it_ec);
        //制冷散热设备的耗电，不能为空
        IDC_Cooling_EC idc_cooling_ec = IDC_Cooling_EC.builder().build();
        mapsObject.put("IDC_Cooling_EC", idc_cooling_ec);
        //本键名必须有，数据中心除了 IT 设备和制冷系统之外的其他耗电
        IDC_Other_EC idc_other_ec = IDC_Other_EC.builder().build();
        mapsObject.put("IDC_Other_EC", idc_other_ec);
        //本键名必须有，指的是数据中心的水耗，本键的值可以为空。
        IDC_Consumption_Water idc_consumption_water = IDC_Consumption_Water.builder().build();
        mapsObject.put("IDC_Consumption_Water", idc_consumption_water);
        Map<String, Object> maps = new HashMap<>(8);
        //已经过滤了在Dic表中type = 1的数据
        Map<String, List<String>> mapTypes = dicService.typeIs1();
        //根据大类先分类
        Map<String, List<Device>> dataByCategory = list.stream().collect(Collectors.groupingBy(Device::getDeviceCategory));
        mapTypes.forEach((key, value) -> {
            if (CollectionUtils.isNotEmpty(value)) {
                Object objectVal = mapsObject.get(key);
                value.forEach(item2 -> {
                    Map<String, Data1> values = getChild(dataByCategory, key, item2, fromCustomer, nowTime);
                    setFieldValueByFieldName(item2, objectVal, values);
                });
                maps.put(key, objectVal);
            } else {
                //本键名必须有，指的是走数据中心的总电表，被计入了总用电，但是又不是用于数据中心的电能。其性质类似办公耗电，因此允许值为空。
                Map<String, Data1> nonIdcEc = getChild(dataByCategory, key, null, fromCustomer, nowTime);
                maps.put("Non_IDC_EC", nonIdcEc);
            }
        });
        return maps;
    }


    private void setFieldValueByFieldName(String fieldName, Object object, Map<String, Data1> value) {
        try {
            // 获取obj类的字节文件对象
            Class<?> c = object.getClass();
            // 获取该类的成员变量
            Field f = c.getDeclaredField(fieldName);
            // 取消语言访问检查
            f.setAccessible(true);
            // 给变量赋值
            f.set(object, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private Map<String, Data1> getChild(Map<String, List<Device>> data,
                                        String parentCode,
                                        String childCode,
                                        Map<Integer, Double> fromCustomer,
                                        String nowTime) {
        List<Device> lists = data.get(parentCode);
        if (CollectionUtils.isNotEmpty(lists)) {
            Map<String, List<Device>> child = lists.stream()
                    .filter(item -> ObjectUtils.isNotEmpty(item.getDeviceSmallClass()))
                    .collect(Collectors.groupingBy(Device::getDeviceSmallClass));
            if (!child.isEmpty()) {
                lists = child.get(childCode);
            }
        } else {
            return new HashMap<>(2);
        }
        if (CollectionUtils.isEmpty(fromCustomer)) {
            return deviceMapData(lists);
        } else {
            return highMapData(lists, fromCustomer, nowTime);
        }
    }

    private Map<String, Data1> deviceMapData(List<Device> list) {
        if (CollectionUtils.isNotEmpty(list)) {
            Map<String, Data1> map = new HashMap<>(list.size() * 2);
            list.forEach(item -> {
                Data1 data1 = Data1.builder().Position(item.getPosition())
                        .Meter_Unit(item.getMeterUnit())
                        .Meter_Code(item.getMeterCode())
                        .Meter_Category(item.getMeterCategory())
                        .build();
                map.put(item.getName(), data1);
            });
            return map;
        }
        return new HashMap<>(2);
    }

    private Map<String, Data1> highMapData(List<Device> list, Map<Integer, Double> fromCustomer, String nowTime) {
        if (CollectionUtils.isNotEmpty(list)) {
            Map<String, Data1> map = new HashMap<>(list.size() * 2);
            list.forEach(item -> {
                Integer id = item.getId();
                Double val = fromCustomer.get(id);
                if (null != val) {
                    String val2 = BigDecimal.valueOf(val).toPlainString();
                    Data1 data1 = Data1.builder()
                            .Meter_Unit(item.getMeterUnit())
                            .Meter_Code(item.getMeterCode())
                            .Meter_Value(val2)
                            .Meter_Time(nowTime)
                            .build();
                    map.put(item.getName(), data1);
                }
            });
            return map;
        }
        return new HashMap<>(2);
    }

    private void writeFile(String data) {
        StringBuilder filePathAndName = new StringBuilder().append(GlobalParam.highFrequencyFilePath);
        File file = new File(GlobalParam.highFrequencyFilePath);
        boolean existFile = true;
        if (!file.exists()) {
            existFile = file.mkdir();
        }
        if (existFile) {
            LocalDateTime now = DateUtil.now00();
            String string = DateUtil.localDateTimeToString(now, DateUtil.FORMAT3);
            filePathAndName.append(string).append(".data");
            file = new File(filePathAndName.toString());
            boolean readyToWrite = true;
            if (!file.exists()) {
                try {
                    readyToWrite = file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                }
            }
            if (readyToWrite) {
                Path path = Paths.get(filePathAndName.toString());
                byte[] strToBytes = data.getBytes();
                try {
                    Files.write(path, strToBytes);
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
            }

        }

    }

    public static Map<String, Expression> compiledExp = new HashMap<>();

    /**
     * 来自客户的数据
     *
     * @param fromCus 来自顾客的数据
     */
    public Map<Integer, Double> deviceValFromCustomer(Map<String, Double> fromCus) {
        //获取所有真实的仪表
        List<Device> list = deviceService.list(Wrappers.<Device>lambdaQuery().eq(Device::getRealIs, true));
        //真实仪表的值
        Map<Integer, Double> data = new HashMap<>(list.size() * 2);
        list.forEach(item -> {
            String key = item.getMapping();
            Double val = fromCus.get(key);
            if (null != val) {
                data.put(item.getId(), val);
            }
        });
        Map<Integer, String> virtualMap = GlobalParam.virtualMap;
        if (!virtualMap.isEmpty()) {
            //计算虚拟仪表的值
            virtualMap.forEach((key, value) -> {
                Double val = calVal(value, data);
                if (null != val) {
                    data.put(key, val);
                }
            });
        }
        return data;
    }


    /**
     * @param fromCustomer key = device.id
     * @param rePull
     * @return
     */
    public Double idcPueVal(Map<Integer, Double> fromCustomer, boolean rePull) {
        Double molecularVal = molecularOrDenominator(fromCustomer, GlobalParam.molecularMap, GlobalParam.molecularFormula, rePull, GlobalParam.MOLECULAR);
        Double denominatorVal = molecularOrDenominator(fromCustomer, GlobalParam.denominatorMap, GlobalParam.denominatorFormula, rePull, GlobalParam.DENOMINATOR);

        BigDecimal bigDecimal = BigDecimalUtil.divideObject(molecularVal, denominatorVal, 2);

        return bigDecimal.doubleValue();
    }

    private Double molecularOrDenominator(Map<Integer, Double> fromCustomer,
                                          Map<String, List<Integer>> mapData,
                                          String formula,
                                          boolean rePull,
                                          Integer keyType) {
        String lastVal;
        //pue的值要减去前一次的值
        if (rePull) {
            lastVal = GlobalParam.idcPueValHis.get(keyType);
        } else {
            lastVal = GlobalParam.idcPueValNow.get(keyType);
        }
        Map<String, String> mapLastVal = new HashMap<>(2);
        if (StringUtils.isNotBlank(lastVal)) {
            mapLastVal = new Gson().fromJson(lastVal, Map.class);
        }
        //本次计算需要用到的值
        Map<String, Object> val4Cal = new HashMap<>(32);
        //下次需要计算的数据
        Map<String, Object> data4Put = new HashMap<>(32);
        Map<String, String> finalDenominatorMapLastVal = mapLastVal;
        mapData.forEach((k, v) -> {
            Double valNow = 0D;
            if (ObjectUtils.isNotNull(v)) {
                valNow = v.stream().map(item -> fromCustomer.get(item))
                        .filter(ObjectUtils::isNotNull)
                        .mapToDouble(Double::doubleValue)
                        .sum();
            }
            data4Put.put(k, valNow);
            Object lastVal1 = finalDenominatorMapLastVal.get(k);
            Double realVal = BigDecimalUtil.subtractObject(valNow, lastVal1).doubleValue();
            val4Cal.put(k, realVal);
        });
        if (StringUtils.isBlank(formula)) {
            return 0D;
        }
        //放置数据用于下次计算
        String dataJson = new Gson().toJson(data4Put);
        if (rePull) {
            GlobalParam.idcPueValHis.put(keyType, dataJson);
        } else {
            GlobalParam.idcPueValNow.put(keyType, dataJson);
        }
        //计算
        Expression compiledExp1 = compiledExp.get(formula);
        if (null == compiledExp1) {
            compiledExp1 = AviatorEvaluator.compile(formula);
            compiledExp.put(formula, compiledExp1);
        }
        Object execute = compiledExp1.execute(val4Cal);
        Double returnVal = Double.parseDouble(execute.toString());

        return returnVal;
    }

    private Double calVal(String value, Map<Integer, Double> data) {
        String[] split = value.split("\\|");
        String formula = split[0];
        Expression compiledExp1 = compiledExp.get(formula);
        if (null == compiledExp1) {
            compiledExp1 = AviatorEvaluator.compile(formula);
            compiledExp.put(formula, compiledExp1);
        }
        Map<String, Object> expMap = new HashMap<>(split.length * 2);
        for (int i = 1; i < split.length; i++) {
            String str = split[i];
            String[] str2 = str.split("_");
            Double aDouble = data.get(Integer.valueOf(str2[1]));
            if (null != aDouble) {
                expMap.put(str2[0], aDouble);
            }
        }
        boolean next = expMap.size() == (split.length - 1);
        if (next) {
            Object execute = compiledExp1.execute(expMap);
            return Double.parseDouble(execute.toString());
        }
        log.info("calVal size is not equals ，" + value);
        return null;
    }


}

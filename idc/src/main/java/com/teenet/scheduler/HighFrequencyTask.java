package com.teenet.scheduler;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.common.collect.Lists;
import com.teenet.common.ActionCode;
import com.teenet.common.GlobalParam;
import com.teenet.common.ResponseCode;
import com.teenet.common.Result;
import com.teenet.customer.CustomerInterface;
import com.teenet.entity.database.*;
import com.teenet.mapper.IdcPueMapper;
import com.teenet.service.DeviceService;
import com.teenet.service.IdcPueRecordService;
import com.teenet.service.PullTimeService;
import com.teenet.service.SchedulerCronService;
import com.teenet.threadpool.MyThreadPool;
import com.teenet.util.ApplicationContextProvider;
import com.teenet.util.DateUtil;
import com.teenet.util.WechatUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Description
 * @Author threedong
 * @Date 2022/6/15 17:45
 */

@Slf4j
@Configuration
public class HighFrequencyTask extends AbstractConfigurerScheduling {

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private SchedulerCronService schedulerCronService;

    @Autowired
    private PullTimeService pullTimeService;


    @Autowired
    private IdcPueMapper idcPueMapper;

    @Autowired
    private IdcPueRecordService idcPueRecordService;

    @Override
    public void processTask() {
        CustomerInterface clazz = (CustomerInterface) ApplicationContextProvider.getBean(GlobalParam.customer);
        Map<String, Double> fromCus = clazz.getData();
        String nowTime = DateUtil.now00Str();
        GlobalParam.code = dealData(fromCus, nowTime, false);
    }

    /**
     * 从客户那没拉取到数据才需要补
     *
     * @param time
     * @return
     */
    public Integer processTaskWithTime(String time) {
        CustomerInterface clazz = (CustomerInterface) ApplicationContextProvider.getBean(GlobalParam.customer);
        Map<String, Double> fromCus = clazz.dataWithTime(time);
        if (null == fromCus || fromCus.isEmpty()) {
            return ResponseCode.ERROR.getCode();
        } else {
            return dealData(fromCus, time, true);
        }
    }

    /**
     * @param fromCus key=device.mapping
     * @param nowTime
     * @param rePull  补数据
     * @return
     */
    private Integer dealData(Map<String, Double> fromCus, String nowTime, boolean rePull) {
        Result<String> result = Result.createByError();
        Double idcPueVal = 0D;
        try {
            if (fromCus.isEmpty()) {
                return result.getCode();
            }
            //需要上传的数据
            List<Device> list = deviceService.list(Wrappers.<Device>lambdaQuery().eq(Device::getNeedUpload, true));
            if (CollectionUtils.isNotEmpty(list)) {
                //数据转换 key = device.id
                Map<Integer, Double> fromCustomer = deviceValFromCustomer(fromCus);
                //包装需要上传的数据
                Map<String, Object> maps = packageData(list, fromCustomer, nowTime);
                //计算idcPue
                idcPueVal = idcPueVal(fromCustomer, rePull);
                Map<String, Object> map1 = new HashMap<>(8);
                map1.put("PUE_Value", idcPueVal);
                map1.put("PUE_Time", nowTime);
                //数据中心实时功率 PUE
                maps.put("IDC_PUE", map1);
                //温湿度， 不支持多组温湿度
                Map<String, Device> idcMp = list.stream().filter(item -> "IDC_MP".equals(item.getDeviceCategory())).collect(Collectors.toMap(Device::getDeviceSmallClass, item -> item, (k1, k2) -> k1));
                Map<String, Object> idc_mp = new HashMap<>(8);
                setMpData(mp_Temperature, idcMp, fromCustomer, nowTime, idc_mp, true);
                setMpData(mp_Humidity, idcMp, fromCustomer, nowTime, idc_mp, false);
                maps.put("IDC_MP", idc_mp);
                result = syncData(maps, GlobalParam.highFrequencyUrl);
            }
        } catch (Exception e) {
            MyThreadPool.EXECUTOR_SERVICE.execute(() -> WechatUtil.sendMsg("HighFrequencyTask.dealData", ActionCode.UNKNOWN_EXCEPTION.getDesc()));
            e.printStackTrace();
            log.info("processTask error");
        } finally {
            LocalDateTime localDateTime = DateUtil.stringToLocalDateTime(nowTime, DateUtil.FORMAT1);
            Integer code = result.getCode();
            //PullTime.type 1代表拉取客户数据，2代表上送数据，只有上送成功或者文件已保存，才算拉取客户数据成功
            boolean toInsert = code.equals(ResponseCode.SUCCESS.getCode()) || code.equals(ResponseCode.WRITE_IN_FILE.getCode());
            if (!rePull) {
                //记录正常情况下客户数据拉取
                PullTime pullTime = new PullTime();
                pullTime.setSuccessIs(toInsert);
                pullTime.setInTime(localDateTime);
                pullTime.setType(1);
                pullTime.setPueD(GlobalParam.idcPueValNow.get(GlobalParam.DENOMINATOR));
                pullTime.setPueM(GlobalParam.idcPueValNow.get(GlobalParam.MOLECULAR));
                pullTimeService.save(pullTime);
                IdcPue idcPue = idcPueMapper.selectOne(null);
                if (ObjectUtils.isNotNull(idcPue)) {
                    String val1 = GlobalParam.idcPueValNow.get(GlobalParam.DENOMINATOR);
                    String val2 = GlobalParam.idcPueValNow.get(GlobalParam.MOLECULAR);
                    idcPue.setDenominatorVal(val1);
                    idcPue.setMolecularVal(val2);
                    idcPue.setCurrentVal(idcPueVal);
                    idcPueMapper.updateById(idcPue);
                    //idcpue存记录
                    IdcPueRecord build = IdcPueRecord.builder().time(localDateTime).val(idcPueVal).build();
                    idcPueRecordService.save(build);
                }
                //更新设备高频val值,存缓存，未存数据库
                List<Device> list = deviceService.list();
                if (CollectionUtils.isNotEmpty(list)) {
                    list.forEach(item -> {
                        String mapping = item.getMapping();
                        Double aDouble = fromCus.get(mapping);
                        GlobalParam.currentVal.put(item.getId(), aDouble);
                    });
                }
            }
            if (toInsert) {
                PullTime pullTime = new PullTime();
                //高频数据上传情况
                boolean equalsCode = code.equals(ResponseCode.SUCCESS.getCode());
                pullTime.setInTime(localDateTime);
                pullTime.setSuccessIs(equalsCode);
                pullTime.setType(2);
                pullTime.setContent(result.getMessage());
                pullTimeService.save(pullTime);
            }

        }
        return result.getCode();
    }

    private static List<String> mp_Temperature = Lists.newArrayList("IDC_MP_Temperature_Indoor", "IDC_MP_Temperature_Outdoor");
    private static List<String> mp_Humidity = Lists.newArrayList("IDC_MP_Humidity_Indoor", "IDC_MP_Humidity_Outdoor");

    private void setMpData(List<String> mp, Map<String, Device> idcMp, Map<Integer, Double> fromCustomer,
                           String time, Map<String, Object> result, boolean isTemperature) {
        String unit1 = isTemperature ? "摄氏度" : "%";
        mp.forEach(item -> {
            Map<String, Object> mpChild = new HashMap<>(2);
            String val2 = "0";
            Device device = idcMp.get(item);
            String unit = unit1;
            if (ObjectUtils.isNotNull(device)) {
                unit = device.getMeterUnit();
                Integer key = device.getId();
                Double val = fromCustomer.get(key);
                if (ObjectUtils.isNotNull(val)) {
                    val2 = BigDecimal.valueOf(val).toPlainString();
                } else {
                    mpChild.put("Meter_Failure", 1);
                }
            } else {
                mpChild.put("Meter_Failure", 1);
            }
            mpChild.put("Meter_Value", val2);
            mpChild.put("Meter_Unit", unit);
            mpChild.put("Meter_Time", time);
            result.put(item, mpChild);
        });
    }

    @Override
    protected String getCron() {
        SchedulerCron one = schedulerCronService.getById(1);
        return one.getCron();
    }
}

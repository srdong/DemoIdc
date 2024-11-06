package com.teenet.controller;

import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.teenet.common.GlobalParam;
import com.teenet.common.Result;
import com.teenet.entity.database.LowFrequency;
import com.teenet.preprocessor.InitializationCommandLineRunner;
import com.teenet.service.LowFrequencyService;
import com.teenet.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Description
 * @Author threedong
 * @Date 2022/6/14 17:20
 */
@RestController
@RequestMapping("/low_frequency")
public class LowFrequencyController extends Transfer {

    @Autowired
    private LowFrequencyService lowFrequencyService;

    @Autowired
    private InitializationCommandLineRunner initializationCommandLineRunner;


    @GetMapping("detail")
    public Result<Map<String, LowFrequency>> detail() {
        List<LowFrequency> list = lowFrequencyService.list();
        Map<String, LowFrequency> collect = list.stream().collect(Collectors.toMap(LowFrequency::getType, item -> item));
        return Result.createBySuccess(collect);
    }

    @PostMapping("update")
    public Result<String> update(@RequestBody List<LowFrequency> list) {
        list.forEach(item -> lowFrequencyService.update(item, Wrappers.<LowFrequency>lambdaUpdate()
                .set(ObjectUtils.isNull(item.getStartTime()), LowFrequency::getStartTime, null)
                .set(ObjectUtils.isNull(item.getEndTime()), LowFrequency::getEndTime, null)
                .set(ObjectUtils.isNull(item.getValue()), LowFrequency::getValue, null)
                .eq(LowFrequency::getId, item.getId())));
        initializationCommandLineRunner.refreshIDC_ITEquipment_Load();
        return Result.createBySuccess();
    }


    @GetMapping("sync")
    public Result<String> sync() {
        List<LowFrequency> list = lowFrequencyService.list();
        Map<String, Map<String, LowFrequency>> maps = list.stream().collect(Collectors.groupingBy(LowFrequency::getType2,
                Collectors.collectingAndThen(Collectors.toList(), item -> item.stream().collect(Collectors.groupingBy(LowFrequency::getType
                        , Collectors.collectingAndThen(Collectors.toList(), item2 -> item2.get(0)))))));
        Map<String, Object> returnData = new HashMap<>(4);
        maps.forEach((key, lowFrequency) -> {
            Map<String, Object> valMaps = new HashMap<>(16);
            LowFrequency idc_itEquipment_load = lowFrequency.get(GlobalParam.IDC_IT_EQUIPMENT_LOAD);
            if (ObjectUtils.isNotEmpty(idc_itEquipment_load)) {
                valMaps.put(GlobalParam.IDC_IT_EQUIPMENT_LOAD, idc_itEquipment_load.getValue());
            }
            lowFrequency.entrySet().stream().filter(self -> !GlobalParam.IDC_IT_EQUIPMENT_LOAD.equals(self.getKey())).forEach(item2 -> {
                String key2 = item2.getValue().getType();
                Map<String, Object> map = new HashMap<>(8);
                LowFrequency one = item2.getValue();
                Double value = one.getValue();
                if (ObjectUtils.isNotNull(value)) {
                    String val = BigDecimal.valueOf(value).toPlainString();
                    map.put("Value", val);
                    String unit = one.getUnit();
                    if (StringUtils.isNotEmpty(unit)) {
                        map.put("Unit", unit);
                    }
                    LocalDateTime startTime = one.getStartTime();
                    if (ObjectUtils.isNotEmpty(startTime)) {
                        String startTime1 = DateUtil.localDateTimeToString(startTime, DateUtil.FORMAT1);
                        map.put("Start_Time", startTime1);
                    }
                    LocalDateTime endTime = one.getEndTime();
                    if (ObjectUtils.isNotEmpty(endTime)) {
                        String endTime2 = DateUtil.localDateTimeToString(endTime, DateUtil.FORMAT1);
                        map.put("End_Time", endTime2);
                    }
                    valMaps.put(key2, map);
                }
            });
            returnData.put(key, valMaps);
        });

        return syncData(returnData, GlobalParam.lowFrequencyUrl);
    }
}

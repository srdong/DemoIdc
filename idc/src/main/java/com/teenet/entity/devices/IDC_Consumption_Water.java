package com.teenet.entity.devices;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

/**
 * 本键名必须有，指的是数据中心的水耗，本键的值可以为空。
 *
 * @Description
 * @Author threedong
 * @Date 2022/6/13 15:42
 */
@Data
@Builder
public class IDC_Consumption_Water {

    /**
     * 冷冻站用水，键名非必须，值可以为空
     */
    private Map<String, Data1> Refrigeration_Station;

    /**
     * 冷却塔用水，键名非必须，值可以为空
     */
    private Map<String, Data1> Cooling_Tower;
}

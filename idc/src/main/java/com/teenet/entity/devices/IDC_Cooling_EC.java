package com.teenet.entity.devices;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

/**
 * 制冷散热设备的耗电，不能为空
 * @Description
 * @Author threedong
 * @Date 2022/6/13 16:09
 */
@Data
@Builder
public class IDC_Cooling_EC {

    /**
     * 对整个系统进行制冷的耗电
     */
    private Map<String,Data1> IDC_Cooling_System;
    /**
     * 仅用于对 Ups 不间断电源进行制冷的耗电
     */
    private Map<String,Data1>  IDC_Ups_Cooling;
    /**
     * 仅用于对机柜进行制冷的耗电
     */
    private Map<String,Data1>  IDC_Cabinet_Cooling;
}

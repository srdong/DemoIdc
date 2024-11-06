package com.teenet.entity.devices;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

/**
 * 全部的 IT 设备的耗电，不能为空，
 *
 * @Description
 * @Author threedong
 * @Date 2022/6/13 16:05
 */
@Data
@Builder
public class IDC_IT_EC {

    /**
     * 在 Ups 不间断电源的进口位置，计量到的 IT 设备耗电，可以为空。
     */
    private Map<String,Data1> IDC_ITEC_Ups_In;
    /**
     * 在 Ups 不间断电源的出口位置，计量到的 IT 设备耗电
     */
    private Map<String,Data1>  IDC_ITEC_Ups_Out;
    /**
     * 在一列机柜的第一个机柜上（列头柜）计量到的整列机柜的 IT设备耗电
     *
     */
    private Map<String,Data1>  IDC_ITEC_Cabinet;
    /**
     * 可以认为是直接安装在刀片机电源上的电表计量到的服务器耗电，可以为空
     *
     */
    private Map<String,Data1>  IDC_ITEC_ITEquipment;
}

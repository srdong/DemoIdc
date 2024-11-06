package com.teenet.entity.devices;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

/**
 * 本键为接入IDC 的全部电能，值不能为空,总耗电
 *
 * @Description
 * @Author threedong
 * @Date 2022/6/13 15:58
 */
@Data
@Builder
public class IDC_EC {
    /**
     * 市电,本键为市电入口，值不能为空，也就是说至少应有一块电表。
     */
    private Map<String,Data1> IDC_EC_Supply;
    /**
     * 柴油机发电
     */
    private Map<String,Data1> IDC_EC_DieselGenerator;
    /**
     * 柴油发电，值可以为空。
     */
    private Map<String,Data1> IDC_EC_Renewable;
    /**
     * 可再生能源发电，值可以为空。
     */
    private Map<String,Data1> IDC_EC_Gas;
    /**
     * 蓄电站送出电能，值可以为空
     */
    private Map<String,Data1> IDC_EC_Storage;
}

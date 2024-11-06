package com.teenet.entity.devices;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

/**
 * 本键名必须有，数据中心除了 IT 设备和制冷系统之外的其他耗电
 * @Description
 * @Author threedong
 * @Date 2022/6/13 16:13
 */
@Data
@Builder
public class IDC_Other_EC {
    /**
     * 办公耗电，允许为空
     */
    private Map<String,Data1> IDC_SubMetering_Office;
}

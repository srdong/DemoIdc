package com.teenet.entity.devices;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @Description
 * @Author threedong
 * @Date 2022/6/14 13:35
 */
@Data
@Builder
public class Data1 {
    private String Position;
    private String Meter_Code;
    private String Meter_Category;
    private String Meter_Unit;
    private String Meter_Value;
    private String Meter_Time;
}

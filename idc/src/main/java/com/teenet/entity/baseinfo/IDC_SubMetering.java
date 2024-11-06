package com.teenet.entity.baseinfo;

import lombok.Builder;
import lombok.Data;

/**
 * 能耗计量情况
 * @Description
 * @Author threedong
 * @Date 2022/6/13 16:38
 */

@Data
@Builder
public class IDC_SubMetering {
    /**
     * 市电入口测量点是否计量
     */
    private Boolean IDC_SubMetering_Supply;
    /**
     * 柴油发电测量点是否计量
     */
    private Boolean IDC_SubMetering_DieselGenerator;
    /**
     * IT 设备电耗测量点是否计量
     */
    private Boolean IDC_SubMetering_ITEC;
    /**
     * 列头柜配电测量点是否计量
     */
    private Boolean IDC_SubMetering_Cabinet;
    /**
     * 其他办公设备用电测量点是否计量
     */
    private Boolean IDC_SubMetering_Office;
    /**
     * 低污染的化石能源发电测量点是否计量
     */
    private Boolean IDC_SubMetering_FossilGenerator;
    /**
     * 可再生能源发电测量点是否计量
     */
    private Boolean IDC_SubMetering_Renewable;
    /**
     * 蓄电站测量点是否计量
     */
    private Boolean IDC_SubMetering_Storage;
    /**
     * 冷站内测量点是否计量
     */
    private Boolean IDC_SubMetering_ColdSite;
    /**
     * 机房内温度场测量是否计量
     */
    private Boolean IDC_SubMetering_TemperatureField;
    /**
     * 空调用电测量点是否计量
     */
    private Boolean IDC_SubMetering_AirConditioning;
    /**
     * 照明用电测量点是否计量
     */
    private Boolean IDC_SubMetering_Lighting;
}

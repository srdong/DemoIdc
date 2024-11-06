package com.teenet.entity.baseinfo;

import lombok.Builder;
import lombok.Data;

/**
 * 设施配备情况
 * @Description
 * @Author threedong
 * @Date 2022/6/13 16:35
 */
@Data
@Builder
public class IDC_Facility {

    /**
     * 是否具备运行中的柴油发电机
     */
    private Boolean IDC_Facility_DieselGenerator;
    /**
     * 是否具备列头柜配电
     */
    private Boolean IDC_Facility_Cabinet;
    /**
     * 是否具备其他办公设备
     */
    private Boolean IDC_Facility_Office;
    /**
     * 是否具备运行中的低污染的化石能源发电机
     */
    private Boolean IDC_Facility_FossilGenerator;
    /**
     * 是否具备运行中的可再生能源发电机
     */
    private Boolean IDC_Facility_Renewable;
    /**
     * 是否具备运行中的蓄电站
     */
    private Boolean IDC_Facility_Storage;
    /**
     * 是否具备运行中的冷站
     */
    private Boolean IDC_Facility_ColdSite;
}

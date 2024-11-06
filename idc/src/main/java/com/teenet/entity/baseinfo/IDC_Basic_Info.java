package com.teenet.entity.baseinfo;

import lombok.Builder;
import lombok.Data;

/**
 * IDC 基本信息
 *
 * @Description
 * @Author threedong
 * @Date 2022/6/13 16:29
 */
@Data
@Builder
public class IDC_Basic_Info {

    /**
     * 数据中心名称
     */
    private String IDC_Name;
    /**
     * 数据中心名称
     */
    private String IDC_Address;
    /**
     * 数据中心业主
     */
    private String IDC_Owner;
    /**
     * 业主联系人
     */
    private String IDC_Contact;
    /**
     * 业主联系人电话
     */
    private String IDC_Owner_Phone;
    /**
     * 数据中心实际运营方
     */
    private String IDC_Operator;
    /**
     * 运营方联系人
     */
    private String IDC_Operator_Contact;
    /**
     * 运营方联系电话
     */
    private String IDC_Operator_Contact_Phone;
    /**
     * 数据中心面积（平方米）
     */
    private Double IDC_Area_m2;
    /**
     * 投入使用时间
     */
    private String IDC_Service_Start;
    /**
     * 机房所在楼层
     */
    private String IDC_Storey;
    /**
     * 楼层总数
     */
    private String IDC_Total_Floors;
    /**
     * 设计机柜总数（台）
     */
    private Integer IDC_Design_Cabinets_Number;
    /**
     * 实际安装机柜数（台）
     */
    private Integer IDC_Actual_Cabinets_Number;
    /**
     * 设计总功率（kW）
     */
    private Double IDC_Design_Total_Power_kW;
}

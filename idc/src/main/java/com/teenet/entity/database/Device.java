package com.teenet.entity.database;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author threedong
 * @since 2022-06-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class Device implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String name;

    private String nameDesc;

    private String position;

    private String meterCode;

    private String meterCategory;

    private String meterUnit;

    /**
     * 设备大类
     */
    private String deviceCategory;
    @TableField(exist = false)
    private String deviceCategoryDesc;

    /**
     * 设备小类
     */
    private String deviceSmallClass;
    @TableField(exist = false)
    private String deviceSmallClassDesc;

    private Boolean realIs;
    private Boolean needUpload;
    private String mapping;
    private String formula;
    @TableField(exist = false)
    private List<RealDeviceVo> list;
    private Double coe;
    @TableField(exist = false)
    private Double currentVal;


}

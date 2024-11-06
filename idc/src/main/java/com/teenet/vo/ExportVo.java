package com.teenet.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Description
 * @Author threedong
 * @Date 2022/11/3 17:10
 */

@Data
@EqualsAndHashCode(callSuper = false)
public class ExportVo {
    private static final long serialVersionUID = 1L;
    private Integer id;
    private String name;
    private String nameDesc;
    private String position;
    private String deviceCategory;
    private String deviceSmallClass;
    private String mapping;
    private Double coe;
    private String needUpload;
}

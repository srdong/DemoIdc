package com.teenet.entity.database;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @Description
 * @Author threedong
 * @Date: 2022/7/12 9:40
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class IdcPue implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String denominatorVal;
    private String molecularVal;
    private Double currentVal;

    private Integer dicPueId;
}

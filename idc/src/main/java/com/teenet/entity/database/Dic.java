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
public class Dic implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer parentId;

    private String name;

    private String description;

    private String code;

    private String category;

    private String unit;

    /**
     * 1代表设备的分类
     */
    private Integer type;

    @TableField(exist = false)
    private List<Dic> child;


}

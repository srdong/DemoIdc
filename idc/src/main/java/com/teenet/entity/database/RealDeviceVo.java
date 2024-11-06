package com.teenet.entity.database;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

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
public class RealDeviceVo implements Serializable {

    private Integer id;

    private String name;

    private String letter;

    private String nameDesc;

    private String mapping;

}

package com.teenet.entity.database;

import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author threedong
 * @since 2022-11-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class LimitRanges implements Serializable {

    private static final long serialVersionUID=1L;

      private Long id;

    /**
     * 下限
     */
    private String lowerLimit;

    /**
     * 上限
     */
    private String upperLimit;

    private String val1;

    private String val2;

    private String val3;


}

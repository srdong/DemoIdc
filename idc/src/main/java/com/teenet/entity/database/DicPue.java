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
 * @since 2022-11-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class DicPue implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    /**
     * 计量修正表示
     */
    private String name;

    /**
     * 分子
     */
    private String molecular;

    /**
     * 分母
     */
    private String denominator;

    /**
     * 分母计算公式
     */
    private String denominatorFormula;

    private String molecularFormula;


}

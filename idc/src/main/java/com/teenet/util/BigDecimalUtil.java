package com.teenet.util;

import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * @Description
 * @Author threedong
 * @Date 2022/7/28 15:47
 */
public class BigDecimalUtil {

    /**
     * 乘法
     *
     * @param bigDecimal1
     * @param bigDecimal2
     * @return
     */
    public static double mulWithDouAndStr(Object bigDecimal1, Object bigDecimal2) {
        BigDecimal bigDecimal11 = convertBigDecimal(bigDecimal1);
        BigDecimal bigDecimal22 = convertBigDecimal(bigDecimal2);
        BigDecimal bigDecimal = bigDecimal11.multiply(bigDecimal22);
        return bigDecimal.doubleValue();
    }

    public static double mulWithDouble(Double bigDecimal1, Double bigDecimal2) {
        BigDecimal bigDecimal11 = ObjectUtils.isEmpty(bigDecimal1) ? BigDecimal.ZERO : BigDecimal.valueOf(bigDecimal1);
        BigDecimal bigDecimal22 = ObjectUtils.isEmpty(bigDecimal2) ? BigDecimal.ZERO : BigDecimal.valueOf(bigDecimal2);
        BigDecimal bigDecimal = bigDecimal11.multiply(bigDecimal22);
        return bigDecimal.doubleValue();
    }

    /**
     * 除法
     *
     * @param val1
     * @param val2
     * @param scale
     * @return
     */
    public static BigDecimal divideObject(Object val1, Object val2, int scale) {
        BigDecimal bigDecimal11 = convertBigDecimal(val1);
        BigDecimal bigDecimal22 = convertBigDecimal(val2);
        boolean isZero = bigDecimal11.compareTo(BigDecimal.ZERO) == 0 || bigDecimal22.compareTo(BigDecimal.ZERO) == 0;
        if (isZero) {
            return BigDecimal.ZERO;
        }
        BigDecimal bigDecimal = bigDecimal11.divide(bigDecimal22, RoundingMode.HALF_UP).setScale(scale, BigDecimal.ROUND_HALF_UP);
        return bigDecimal;
    }

    public static BigDecimal convertBigDecimal(Object val) {
        BigDecimal bigDecimal = BigDecimal.ZERO;
        try {
            if (ObjectUtils.isEmpty(val)) {
                return bigDecimal;
            }
            if (val instanceof String) {
                bigDecimal = new BigDecimal((String) val);
            } else if (val instanceof Double) {
                bigDecimal = BigDecimal.valueOf((Double) val);
            } else {
                bigDecimal = new BigDecimal(String.valueOf(val));
            }
        } catch (Exception e) {
            System.out.println("convertBigDecimal error : " + val);
        }
        return bigDecimal;
    }


    public static BigDecimal subtractObject(Object val1, Object val2) {
        BigDecimal bigDecimal11 = convertBigDecimal(val1);
        BigDecimal bigDecimal22 = convertBigDecimal(val2);
        return bigDecimal11.subtract(bigDecimal22);
    }

}

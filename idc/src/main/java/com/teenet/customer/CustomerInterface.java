package com.teenet.customer;

import java.util.Map;

/**
 * @Description
 * @Author threedong
 * @Date 2022/7/14 10:30
 */
public interface CustomerInterface {

    Map<String, Double> getData();

    Map<String, Double> dataWithTime(String time);
}

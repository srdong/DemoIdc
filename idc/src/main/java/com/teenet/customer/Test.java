package com.teenet.customer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description 桂箐
 * @Author threedong
 * @Date 2022/7/14 10:22
 */
@Slf4j
@Service
public class Test implements CustomerInterface {

    @Override
    public Map<String, Double> getData() {
        Map<String, Double> fromCus = new HashMap<>(6);

        DecimalFormat df = new DecimalFormat("0.00");


        fromCus.put("5_0_237_1_34_0", Double.valueOf(df.format(Math.random() * 100)));
        fromCus.put("6_0_481_1_34_0", Double.valueOf(df.format(Math.random() * 100)));
        fromCus.put("6_0_484_1_34_0", 37958600.00);
        fromCus.put("5_0_233_1_34_0", Double.valueOf(df.format(Math.random() * 100)));
        fromCus.put("5_0_391_1_42_0", Double.valueOf(df.format(Math.random() * 100)));
        fromCus.put("5_0_393_1_42_0", Double.valueOf(df.format(Math.random() * 100)));
        fromCus.put("5_0_375_1_26_0", Double.valueOf(df.format(Math.random() * 100)));
        fromCus.put("5_0_374_1_26_0", Double.valueOf(df.format(Math.random() * 100)));
        fromCus.put("91_0_127_1_627_0", Double.valueOf(df.format(Math.random() * 100)));
        fromCus.put("91_0_128_1_627_0", Double.valueOf(df.format(Math.random() * 100)));
        fromCus.put("5_0_84_1_189_0", Double.valueOf(df.format(Math.random() * 100)));
        fromCus.put("5_0_84_1_190_0", Double.valueOf(df.format(Math.random() * 100)));
        fromCus.put("a_a_a_2", Double.valueOf(df.format(Math.random() * 100)));
        fromCus.put("a_a_a_3", Double.valueOf(df.format(Math.random() * 100)));

        fromCus.put("5_0_57_1_87_0", Double.valueOf(df.format(Math.random() * 100)));
        fromCus.put("5_0_55_1_87_0", Double.valueOf(df.format(Math.random() * 100)));
        fromCus.put("5_0_56_1_87_0", Double.valueOf(df.format(Math.random() * 100)));

        fromCus.put("c_c_c_c", Double.valueOf(df.format(Math.random() * 100)));
        fromCus.put("b_b_b_1", Double.valueOf(df.format(Math.random() * 100)));

        fromCus.put("5_0_55_1_3_0", Double.valueOf(df.format(Math.random() * 100)));
        fromCus.put("5_0_56_1_3_0", Double.valueOf(df.format(Math.random() * 100)));
        fromCus.put("5_0_57_1_3_0", Double.valueOf(df.format(Math.random() * 100)));

        fromCus.put("t_t_t_t_1", Double.valueOf(df.format(Math.random() * 100)));


        System.out.println("test");
        return fromCus;
    }


    @Override
    public Map<String, Double> dataWithTime(String time) {
        Map<String, Double> fromCus = new HashMap<>(6);
        fromCus.put("1_2_0", 1D);
        fromCus.put("1_2_1", 2D);
        fromCus.put("62", 3D);
        System.out.println("tet");
        return fromCus;
    }


}

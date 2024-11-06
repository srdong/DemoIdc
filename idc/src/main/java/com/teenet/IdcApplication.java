package com.teenet;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @Description
 * @Author threedong
 * @Date: 2022/6/13 9:02
 */
@EnableScheduling
@SpringBootApplication
@MapperScan("com.teenet.mapper")
public class IdcApplication {
    public static void main(String[] args) {
        SpringApplication.run(IdcApplication.class, args);
    }

}

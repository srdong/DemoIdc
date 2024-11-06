package com.teenet.customer.entity;

import lombok.Data;

/**
 * @Description
 * @Author threedong
 * @Date 2022/7/29 10:53
 */
@Data
public class YuePuEntity {
    private Integer status;
    private Long server_time;
    private String resource_id;
    private String real_value;
    private String event_count;
    private String alias;
    private String save_time;
}

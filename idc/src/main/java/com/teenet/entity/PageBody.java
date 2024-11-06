package com.teenet.entity;

import lombok.Data;

/**
 * @Description
 * @Author threedong
 * @Date 2023/1/16 15:50
 */
@Data
public class PageBody {
    private Integer pageNo;
    private Integer pageSize;
    private String type;
    private String startTime;
    private String endTime;
}

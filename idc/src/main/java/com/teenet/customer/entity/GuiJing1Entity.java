package com.teenet.customer.entity;

import lombok.Data;

import java.util.List;

/**
 * @Description
 * @Author threedong
 * @Date 2022/7/28 14:02
 */
@Data
public class GuiJing1Entity {
    private String resource_id;
    private List<GuiJing2Entity> data_list;

}

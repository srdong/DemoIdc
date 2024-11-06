package com.teenet.entity;

import lombok.Builder;
import lombok.Data;

/**
 * @Description
 * @Author threedong
 * @Date 2022/6/13 13:57
 */
@Data
@Builder
public class TransferData {
    private Object data;
    private String time;
}

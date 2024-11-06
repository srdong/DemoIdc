package com.teenet.customer.entity;

import com.teenet.util.Crc16Util;
import com.teenet.util.NumberUtil;
import lombok.Data;

import java.math.BigInteger;

/**
 * @Description
 * @Author threedong
 * @Date 2022/7/27 14:38
 */
@Data
public class CmccEntity {

    private byte[] head = {0X7E, 0X7C, 0X6B, 0X5A};
    private int length = 0;
    private byte[] serialsNo = new byte[4];
    private byte[] pkType = new byte[4];
    private byte[] content;
    private byte[] crc16 = new byte[2];
    private byte[] lengthByte = new byte[4];
    private byte[] data;
    private int pkTypeDesc = 0;

    public CmccEntity() {
    }

    public CmccEntity(byte[] serialsNo, byte[] pkType, byte[] content) {
        this.serialsNo = serialsNo;
        this.pkType = pkType;
        this.pkTypeDesc = new BigInteger(pkType).intValue();
        this.content = content;
        this.length = head.length + 4 + serialsNo.length + pkType.length + content.length + 2;
        this.lengthByte = NumberUtil.intToBytesHighAhead(length);
        byte[] bytes = NumberUtil.mergeBytes(head, lengthByte, serialsNo, pkType, content);
        data = Crc16Util.getData(bytes);
    }

    public void setLengthByte(byte[] lengthByte) {
        this.lengthByte = lengthByte;
        this.length = new BigInteger(lengthByte).intValue();
    }

    public void setPkType(byte[] pkType) {
        this.pkType = pkType;
        this.pkTypeDesc = new BigInteger(pkType).intValue();
    }
}

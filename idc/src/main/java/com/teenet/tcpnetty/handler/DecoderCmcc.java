package com.teenet.tcpnetty.handler;


import com.teenet.customer.entity.CmccEntity;
import com.teenet.util.NumberUtil;

/**
 * @author threedong
 */
public class DecoderCmcc {

    /**
     * 将bytes 转换成指定的数据结构
     *
     * @param bytes
     * @return
     */
    public static CmccEntity encoder(byte[] bytes) {
        CmccEntity cmccEntity = new CmccEntity();
        int index = 4;
        byte[] length = NumberUtil.getByte(bytes, index, 4);
        cmccEntity.setLengthByte(length);
        index += 4;
        byte[] serialsNo = NumberUtil.getByte(bytes, index, 4);
        cmccEntity.setSerialsNo(serialsNo);
        index += 4;
        byte[] pkType = NumberUtil.getByte(bytes, index, 4);
        cmccEntity.setPkType(pkType);
        index += 4;
        int contentLength = cmccEntity.getLength() - 18;
        byte[] content = NumberUtil.getByte(bytes, index, contentLength);
        cmccEntity.setContent(content);
        index += contentLength;
        byte[] crc16 = NumberUtil.getByte(bytes, index, 2);
        cmccEntity.setCrc16(crc16);
        return cmccEntity;
    }


}

package com.teenet.tcpnetty.handler;


import com.teenet.customer.entity.CmccEntity;
import com.teenet.util.NumberUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.extern.slf4j.Slf4j;

/**
 * 编码器
 *
 * @author sun
 */
@Slf4j
public class DataEncoder extends MessageToByteEncoder<CmccEntity> {

    @Override
    protected void encode(ChannelHandlerContext ctx, CmccEntity cmccEntity, ByteBuf out) throws Exception {
        byte[] data = cmccEntity.getData();
        System.out.println("发送数据。。。。" + NumberUtil.bytesToHex(data));
        out.writeBytes(data);
    }

}

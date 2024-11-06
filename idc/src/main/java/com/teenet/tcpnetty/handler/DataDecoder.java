package com.teenet.tcpnetty.handler;

import com.teenet.customer.entity.CmccEntity;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;


/**
 * 解码器
 *
 * @author sun
 */
public class DataDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        byte[] data = new byte[in.readableBytes()];
        in.readBytes(data);
        CmccEntity entity = DecoderCmcc.encoder(data);
        out.add(entity);
    }


}

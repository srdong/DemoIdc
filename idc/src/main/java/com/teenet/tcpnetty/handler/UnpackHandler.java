package com.teenet.tcpnetty.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.math.BigInteger;
import java.util.List;

/**
 * @author sun
 * @ClassName: Unpack104Util
 * @Description: 解决TCP 拆包和沾包的问题
 */
public class UnpackHandler extends ByteToMessageDecoder {


    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf buffer, List<Object> out) throws Exception {
        mdecode(ctx, buffer, out);
    }


    private void mdecode(ChannelHandlerContext ctx, ByteBuf buffer, List<Object> out) {
        // 记录包头开始的index
        int beginReader;
        int newDataLength = 0;
        while (true) {
            // 获取包头开始的index
            beginReader = buffer.readerIndex();
            // 记录一个标志用于重置
            buffer.markReaderIndex();
            // 读到了协议的开始标志，结束while循环
            //if (buffer.readByte() == Iec104Constant.HEAD_DATA) {
            if (buffer.readByte() == 0x7E) {
                if (buffer.readByte() == 0X7C) {
                    if (buffer.readByte() == 0X6B) {
                        if (buffer.readByte() == 0X5A) {
                            // 标记当前包为新包
                            //读取包长度
                            int i = 0;
                            byte[] length = new byte[4];
                            while (i < 4) {
                                length[i] = buffer.readByte();
                                i++;
                            }
                            newDataLength = new BigInteger(length).intValue() - 8;
                            int restLength = buffer.readableBytes();
                            while (restLength < newDataLength) {
                                break;
                            }
                            break;
                        }
                    }
                }
            }
            continue;
        }
        newDataLength = newDataLength + 8;
        //恢复指针
        buffer.readerIndex(beginReader);
        ByteBuf data = buffer.readBytes(newDataLength);
        out.add(data);
    }

}

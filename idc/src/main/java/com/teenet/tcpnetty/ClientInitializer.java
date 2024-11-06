package com.teenet.tcpnetty;


import com.teenet.tcpnetty.handler.*;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import lombok.Setter;
import lombok.experimental.Accessors;


@Setter
@Accessors(chain = true)
public class ClientInitializer extends ChannelInitializer<SocketChannel> {

    private DataHandler dataHandler;

    /**
     * 初始化处理链
     */
    @Override
    public void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        // 沾包拆包工具
        pipeline.addLast("unpack", new UnpackHandler());
        // 数组编码器
        pipeline.addLast("byteencoder", new BytesEncoder());
        // 编码器
        pipeline.addLast("encoder", new DataEncoder());
        // 解码器
        pipeline.addLast("decoder", new DataDecoder());
        pipeline.addLast("handler", new ClientHandler(dataHandler));
    }
}

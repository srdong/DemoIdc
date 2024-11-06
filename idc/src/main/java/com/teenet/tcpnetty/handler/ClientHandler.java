package com.teenet.tcpnetty.handler;



import com.teenet.tcpnetty.TcpClientMaster;
import com.teenet.threadpool.MyThreadPool;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class ClientHandler extends SimpleChannelInboundHandler<Object> {

    private DataHandler dataHandler;

    public ClientHandler(DataHandler dataHandler) {
        this.dataHandler = dataHandler;
    }


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("aaaaa");
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, Object object) throws IOException {
        System.out.println("bbbbbb");
        if (dataHandler != null) {
            MyThreadPool.EXECUTOR_SERVICE.execute(()->{
                try {
                    dataHandler.channelRead2(new ChannelHandlerImpl(ctx), object);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.info("channelInactive exception");
        TcpClientMaster.channel= null;
    }
}

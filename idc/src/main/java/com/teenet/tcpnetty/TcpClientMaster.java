package com.teenet.tcpnetty;


import com.teenet.common.ActionCode;
import com.teenet.customer.entity.CmccEntity;
import com.teenet.tcpnetty.handler.DataHandler;
import com.teenet.util.WechatUtil;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;


@Slf4j
public class TcpClientMaster implements ClientMaster {

    private DataHandler dataHandler;

    private static Bootstrap bootstrap = null;

    public static EventLoopGroup eventLoopGroup = new NioEventLoopGroup(10);

    public static Channel channel = null;

    /**
     * 初始化Bootstrap
     */
    public void getBootstrap() {
        if (null == bootstrap) {
            bootstrap = new Bootstrap();
            bootstrap.group(eventLoopGroup)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .option(ChannelOption.RCVBUF_ALLOCATOR, new FixedRecvByteBufAllocator(2048))
                    .handler(new ClientInitializer().setDataHandler(dataHandler));
        }
    }


    public static int connectAgain = 0;

    /**
     * 连接
     *
     * @param host
     * @param port
     */
    public static void doConnect(String host, int port) {
        bootstrap.remoteAddress(host, port);
        //异步连接tcp服务端
        bootstrap.connect().addListener((ChannelFuture futureListener) -> {
            if (futureListener.isSuccess()) {
                log.info("与 " + host + ":" + port + " 连接成功");
                channel = futureListener.channel();
            } else {
                if (connectAgain < 3) {
                    log.info("与 " + host + ":" + port + " 连接失败!将重新请求连接!");
                    futureListener.channel().eventLoop().schedule(() -> doConnect(host, port), 1, TimeUnit.SECONDS);
                } else {
                    log.info("3次连接失败，不在重连");
                    WechatUtil.sendMsg("3次连接失败，不在重连", ActionCode.TCP_CONNECT_ERROR.getDesc());
                }
                connectAgain += 1;
            }
        });
    }

    /**
     * 发消息
     *
     * @param channel
     * @throws Exception
     */
    public static void sendMsg(Channel channel, CmccEntity cmccEntity) throws Exception {
        if (channel != null && channel.isActive()) {
            log.info("向设备发送数据");
            channel.writeAndFlush(cmccEntity).sync();
        }
    }


    @Override
    public void run() {
        getBootstrap();
        doConnect("192.168.0.134", 2406);
        //doConnect(GlobalParam.tcpIp, GlobalParam.tcpPort);
    }

    @Override
    public void close() {
        channel.close();
        channel = null;
        connectAgain = 0;
        log.info("tcp close");
    }


    @Override
    public ClientMaster setDataHandler(DataHandler dataHandler) {
        this.dataHandler = dataHandler;
        return this;
    }

}

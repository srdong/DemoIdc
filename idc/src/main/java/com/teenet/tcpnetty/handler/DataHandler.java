package com.teenet.tcpnetty.handler;

/**
 * @ClassName: DataHandler
 * @Description: 数据处理
 * @author: sun
 */
public interface DataHandler {

    /**
     * @param ctx
     * @throws Exception
     * @Title: handlerAdded
     * @Description: 建立连接
     */
    void handlerAdded(ChannelHandler ctx) throws Exception;

    /**
     * @Description
     * @Author threedong
     * @Date: 2022/7/27 15:11
     */
    void channelRead(ChannelHandler ctx, Object object) throws Exception;

    void channelRead2(ChannelHandler ctx, Object object) throws Exception;
}

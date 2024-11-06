package com.teenet.tcpnetty.handler;

import com.teenet.customer.entity.CmccEntity;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class SysDataHandler implements DataHandler {


    @Override
    public void handlerAdded(ChannelHandler ctx) throws Exception {
    }


    @Override
    public void channelRead(ChannelHandler ctx, Object ruleDetail104) throws Exception {

    }

    @Override
    public void channelRead2(ChannelHandler ctx, Object object) throws Exception {
        CmccEntity entity = (CmccEntity) object;
        System.out.println("收到数据");
    }


}

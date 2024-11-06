package com.teenet.tcpnetty;

import com.teenet.customer.entity.CmccEntity;
import com.teenet.tcpnetty.handler.SysDataHandler;
import com.teenet.util.Crc16Util;
import com.teenet.util.NumberUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;

/**
 * @Description
 * @Author threedong
 * @Date 2022/7/25 16:03
 */
public class test {


    public static void main(String[] args) throws Exception {
//        MasterFactory.createTcpClientMaster().setDataHandler(new SysDataHandler()).run();
//        System.out.println("ddddddddddddd");
//        Thread.sleep(10000);
//        MasterFactory.createTcpClientMaster().close();
//        System.out.println("1111111");
//        Thread.sleep(3000);
//        MasterFactory.createTcpClientMaster().setDataHandler(new SysDataHandler()).run();


        byte[] serialsNo = NumberUtil.intToBytesHighAhead(1);
        byte[] pkType = NumberUtil.intToBytesHighAhead(101);
        byte[] userName = NumberUtil.paddingSpaces("上级SC", 20);
        byte[] passWord = NumberUtil.paddingSpaces("pass@1234", 20);

        //所有数据
        MasterFactory.createTcpClientMaster().setDataHandler(new SysDataHandler()).run();
        Thread.sleep(2000);
        Channel future = TcpClientMaster.channel;

        byte[] content = NumberUtil.mergeBytes(userName, passWord);
        CmccEntity entity = new CmccEntity(serialsNo, pkType, content);

        int i = 0;
        while (i<4){
            if (null != future) {
                System.out.println("start to send data");
                TcpClientMaster.sendMsg(future, entity);
            }
            i++;
            Thread.sleep(1000);
            System.out.println(i);
        }

//        pkType = NumberUtil.intToBytesHighAhead(401);
//        byte[] terminalId = NumberUtil.paddingSpaces("1234", 7);
//        byte[] groupId = NumberUtil.intToBytesHighAhead(1);
//        //一问一答方式
//        byte[] enumAcce = {0, 0};
//        byte[] pollingTime = NumberUtil.intToBytesHighAhead(10);
//        //模拟输入量，遥测  一组有多个
//        byte[] tidEnumType = {0, 3};
//        byte[] tidSiteId = NumberUtil.paddingSpaces("1234", 20);
//        byte[] tidDeviceId = NumberUtil.paddingSpaces("1234", 26);
//        byte[] tidSignalId = NumberUtil.paddingSpaces("1234", 20);
//        byte[] tidSignalNumber = NumberUtil.paddingSpaces("123", 3);


        System.out.println("111");

    }


}

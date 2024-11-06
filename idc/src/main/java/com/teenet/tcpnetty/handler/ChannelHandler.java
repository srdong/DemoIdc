package com.teenet.tcpnetty.handler;



public interface ChannelHandler {

	void writeAndFlush(Object object);
}

package com.teenet.tcpnetty;


/**
 * 主站 工厂类
 * @ClassName:  Iec104MasterFactory   
 * @Description: IEC104规约主站
 * @author: sun
 */
public class MasterFactory {

 

	/**
	* @Title: createTcpClientMaster
	* @Description: 创建一个TCM客户端的104主站
	* @return
	 */
	public static  ClientMaster createTcpClientMaster() {
		return new TcpClientMaster();
	}
}

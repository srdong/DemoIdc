package com.teenet.tcpnetty;


import com.teenet.tcpnetty.handler.DataHandler;

/**
 * 主站抽象类
 */
public interface ClientMaster {

	/**
	 * 服务启动方法
	 * @throws Exception
	 */
	void run() throws Exception;

	void close() throws Exception;
	
	/**
	 * 
	* @Title: setDataHandler
	* @Description: 设置数据处理类
	* @param dataHandler
	 */
	ClientMaster setDataHandler(DataHandler dataHandler);


}

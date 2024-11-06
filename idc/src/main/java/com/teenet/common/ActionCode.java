package com.teenet.common;


/**
 * @author threedong
 */

public enum ActionCode {

    DATA_SEND_ERROR(100, "数据上行发送失败"),
    DATA_PULL_ERROR(101, "数据拉取失败"),
    UNKNOWN_EXCEPTION(102, "未知异常"),
    SYSTEM_STARTUP(103, "系统启动"),
    TCP_CONNECT_ERROR(105, "TCP启动失败"),
    URL_CONNECT_ERROR(106, "url请求失败"),
    SUPPLEMENT_ERROR(107, "补录失败"),
    SYSTEM_SHUT_DOWN(104, "系统关闭");


    private final Integer code;
    private final String desc;

    ActionCode(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public Integer getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

}


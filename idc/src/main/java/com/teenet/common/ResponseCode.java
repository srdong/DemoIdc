package com.teenet.common;


public enum ResponseCode {

    SUCCESS(200, "SUCCESS"),
    WRITE_IN_FILE(210, "已写入文件"),
    QUERY_NOT_DATA(204, "未查询到数据"),
    NAME_PASS_ERROR(400, "用户名或密码错误"),
    ERROR(500, "ERROR");



    private final Integer code;
    private final String desc;


    ResponseCode(Integer code, String desc) {
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


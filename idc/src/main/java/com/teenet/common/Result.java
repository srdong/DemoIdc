package com.teenet.common;

import lombok.Data;

import java.io.Serializable;


/**
 * 接口返回数据格式
 * @author threedong
 */
@Data
public class Result<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 成功标志
     */
    private boolean success = true;

    /**
     * 返回处理消息
     */
    private String message = "操作成功！";

    /**
     * 返回代码
     */
    private Integer code = 0;

    /**
     * 返回数据对象 data
     */
    private T result;

    /**
     * 时间戳
     */
    private long timestamp = System.currentTimeMillis();

    public Result() {
    }

    private Result(Integer code) {
        this.code = code;
    }

    private Result(Integer code, T result) {
        this.code = code;
        this.result = result;
    }

    private Result(Integer code, String message, T result) {
        this.code = code;
        this.message = message;
        this.result = result;
    }

    private Result(Integer code, String message) {
        this.code = code;
        this.message = message;
    }


    public static <T> Result<T> createBySuccess() {
        return new Result<T>(ResponseCode.SUCCESS.getCode());
    }

    public static <T> Result<T> createBySuccessMessage(String msg) {
        return new Result<T>(ResponseCode.SUCCESS.getCode(), msg);
    }

    public static <T> Result<T> createBySuccess(T data) {
        return new Result<T>(ResponseCode.SUCCESS.getCode(), data);
    }

    public static <T> Result<T> createBySuccess(String msg, T data) {
        return new Result<T>(ResponseCode.SUCCESS.getCode(), msg, data);
    }

    public static <T> Result<T> createByError() {
        return new Result<T>(ResponseCode.ERROR.getCode(), ResponseCode.ERROR.getDesc());
    }

    public static <T> Result<T> createByErrorMessage(String errorMessage) {
        return new Result<T>(ResponseCode.ERROR.getCode(), errorMessage);
    }

    public static <T> Result<T> createByErrorMessage(T errorMessage) {
        return new Result<T>(ResponseCode.ERROR.getCode(), errorMessage);
    }

    public static <T> Result<T> createByErrorCodeMessage(int errorCode, String errorMessage) {
        return new Result<T>(errorCode, errorMessage);
    }

    public static <T> Result<T> createByNoData() {
        return new Result<T>(ResponseCode.QUERY_NOT_DATA.getCode(), ResponseCode.QUERY_NOT_DATA.getDesc());
    }


}
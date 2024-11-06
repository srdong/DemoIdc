package com.teenet.execptions;

import com.teenet.common.ActionCode;
import com.teenet.threadpool.MyThreadPool;
import com.teenet.util.WechatUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * 异常发送数据
 *
 * @Description
 * @Author threedong
 * @Date 2022/7/26 11:01
 */
@Slf4j
@ControllerAdvice
public class MyExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    public void exceptionHandler(Exception e) {
        log.info("MyExceptionHandler.exceptionHandler");
        MyThreadPool.EXECUTOR_SERVICE.execute(() -> WechatUtil.sendMsg(e.getMessage(), ActionCode.UNKNOWN_EXCEPTION.getDesc()));
        e.printStackTrace();
    }
}

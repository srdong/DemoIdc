package com.teenet.listener;

import com.teenet.common.ActionCode;
import com.teenet.util.WechatUtil;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.stereotype.Component;

/**
 * @Description
 * @Author threedong
 * @Date 2022/7/13 16:54
 */
@Component
@Slf4j
public class ShutdownHookListener implements ApplicationListener<ContextClosedEvent> {

    @Override
    public void onApplicationEvent(@NotNull ContextClosedEvent event) {
        WechatUtil.sendMsg("系统关闭中", ActionCode.SYSTEM_SHUT_DOWN.getDesc());
        log.info("shutdown hook, ContextClosedEvent");
    }
}

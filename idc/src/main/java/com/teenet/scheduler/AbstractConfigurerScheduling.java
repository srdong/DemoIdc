package com.teenet.scheduler;


import com.teenet.controller.Transfer;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.util.StringUtils;

/**
 * @Description
 * @Author threedong
 * @Date: 2022/6/15 17:43
 */
@Configuration
public abstract class AbstractConfigurerScheduling extends Transfer implements SchedulingConfigurer {

    /**
     * 定时任务周期表达式
     */
    private String cron;

    @Override
    public void configureTasks(ScheduledTaskRegistrar scheduledTaskRegistrar) {
        scheduledTaskRegistrar.addTriggerTask(
                //执行定时任务
                this::processTask,
                //设置触发器
                triggerContext -> {
                    // 初始化定时任务周期
                    if (StringUtils.isEmpty(cron)) {
                        cron = getCron();
                    }
                    CronTrigger trigger = new CronTrigger(cron);
                    return trigger.nextExecutionTime(triggerContext);
                }
        );
    }


    /**
     * 任务的处理函数
     * 本函数需要由派生类根据业务逻辑来实现
     */
    protected abstract void processTask();


    /**
     * 获取定时任务周期表达式
     * 本函数由派生类实现，从配置文件，数据库等方式获取参数值
     */
    protected abstract String getCron();
}



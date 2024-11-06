package com.teenet.scheduler;

import com.teenet.common.GlobalParam;
import com.teenet.common.ResponseCode;
import com.teenet.entity.database.SchedulerCron;
import com.teenet.service.SchedulerCronService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.io.File;

/**
 * @Description 断点续传高频数据
 * @Author threedong
 * @Date 2022/6/15 18:01
 */
@Slf4j
@Configuration
public class BreakPointTask extends AbstractConfigurerScheduling {

    @Autowired
    private SchedulerCronService schedulerCronService;


    @Override
    public void processTask() {
        boolean toRun = GlobalParam.code.equals(ResponseCode.SUCCESS.getCode()) && GlobalParam.notRun;
        boolean println = null != GlobalParam.println && GlobalParam.println;
        if (println) {
            log.info("BreakPointTask.processTask : " + toRun + "_" + GlobalParam.notRun + "_" + GlobalParam.code);
        }
        if (toRun) {
            GlobalParam.notRun = false;
            File file = new File(GlobalParam.highFrequencyFilePath);
            if (file.exists()) {
                File[] files = file.listFiles();
                boolean next = null != files && files.length > 0;
                if (next) {
                    log.info(" BreakPointTask start ");
                    for (File oneFile : files) {
                        Integer code = breakPointUploadHighFrequency(oneFile, GlobalParam.highFrequencyUrl);
                        boolean toContinue = code.equals(ResponseCode.SUCCESS.getCode());
                        if (!toContinue) {
                            break;
                        }
                    }
                }
            }
            GlobalParam.notRun = true;
        }
    }

    @Override
    protected String getCron() {
        SchedulerCron one = schedulerCronService.getById(2);
        return one.getCron();
    }
}

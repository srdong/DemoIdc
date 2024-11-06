package com.teenet.scheduler;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.teenet.common.ActionCode;
import com.teenet.common.GlobalParam;
import com.teenet.common.ResponseCode;
import com.teenet.entity.database.PullTime;
import com.teenet.entity.database.SchedulerCron;
import com.teenet.service.PullTimeService;
import com.teenet.service.SchedulerCronService;
import com.teenet.threadpool.MyThreadPool;
import com.teenet.util.DateUtil;
import com.teenet.util.WechatUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.util.List;


/**
 * 拉取客户数据失败，重跑
 * 历史数据
 *
 * @Description
 * @Author threedong
 * @Date 2022/7/14 11:07
 */
@Slf4j
@Configuration
public class UnsuccessfulDataTask extends AbstractConfigurerScheduling {


    @Autowired
    private SchedulerCronService schedulerCronService;

    @Autowired
    private HighFrequencyTask highFrequencyTask;

    @Autowired
    private PullTimeService pullTimeService;

    @Override
    public void processTask() {
        if (GlobalParam.unsuccessfulDataRun) {
            boolean toRun = GlobalParam.code.equals(ResponseCode.SUCCESS.getCode()) && GlobalParam.notRun;
            if (toRun) {
                GlobalParam.notRun = false;
                //根据顺序排序需要补录的数据
                List<PullTime> list = pullTimeService.list(Wrappers.<PullTime>lambdaQuery().eq(PullTime::getSuccessIs, false).eq(PullTime::getType, 1).orderByAsc(PullTime::getInTime));
                if (CollectionUtils.isNotEmpty(list)) {
                    log.info(" UnsuccessfulDataTask start ");
                    //需要时间段的数据
                    for (PullTime item : list) {
                        LocalDateTime inTime = item.getInTime();
                        //获取前15分钟pue的分子分母，肯定是成功的数据，有值
                        LocalDateTime localDateTime = inTime.minusMinutes(15);
                        PullTime one = pullTimeService.getOne(Wrappers.<PullTime>lambdaQuery().eq(PullTime::getInTime, localDateTime).eq(PullTime::getType, 1));
                        if (ObjectUtils.isNotNull(one)) {
                            GlobalParam.idcPueValHis.put(GlobalParam.DENOMINATOR, one.getPueM());
                            GlobalParam.idcPueValHis.put(GlobalParam.MOLECULAR, one.getPueD());
                        }
                        String time = DateUtil.localDateTimeToString(inTime, DateUtil.FORMAT1);
                        Integer code = highFrequencyTask.processTaskWithTime(time);
                        //只要是成功，或者是到了写入文件那一步都算是从customer拉取数据成功
                        boolean successIs = code.equals(ResponseCode.SUCCESS.getCode()) || code.equals(ResponseCode.WRITE_IN_FILE.getCode());
                        item.setSuccessIs(successIs);
                        //idcPue的值
                        item.setPueD(GlobalParam.idcPueValHis.get(GlobalParam.DENOMINATOR));
                        item.setPueM(GlobalParam.idcPueValHis.get(GlobalParam.MOLECULAR));
                        pullTimeService.updateById(item);
                        if (!successIs) {
                            MyThreadPool.EXECUTOR_SERVICE.execute(
                                    () -> WechatUtil.sendMsg("补录时间点 ：" + time, ActionCode.SUPPLEMENT_ERROR.getDesc()));
                            break;
                        }
                    }
                }
                GlobalParam.notRun = true;
            }
        }
    }

    @Override
    protected String getCron() {
        SchedulerCron one = schedulerCronService.getById(3);
        return one.getCron();
    }
}

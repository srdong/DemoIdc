package com.teenet.scheduler;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.teenet.entity.database.PullTime;
import com.teenet.entity.database.SchedulerCron;
import com.teenet.entity.database.SysLogs;
import com.teenet.service.PullTimeService;
import com.teenet.service.SchedulerCronService;
import com.teenet.service.SysLogsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


/**
 * @Description 删除一个月前的数据
 * @Author threedong
 * @Date 2022/7/14 11:07
 */
@Slf4j
@Configuration
public class PullTimeDelTask extends AbstractConfigurerScheduling {


    @Autowired
    private SchedulerCronService schedulerCronService;


    @Autowired
    private PullTimeService pullTimeService;

    @Autowired
    private SysLogsService sysLogsService;

    @Override
    protected void processTask() {
        LocalDateTime lastTime = LocalDateTime.now().minusDays(30);
        List<PullTime> list = pullTimeService.list();
        if (CollectionUtils.isNotEmpty(list)) {
            List<Integer> collect = list.stream().filter(item -> item.getInTime().isBefore(lastTime)).map(PullTime::getId).collect(Collectors.toList());
            pullTimeService.removeBatchByIds(collect);
        }
        List<SysLogs> list2 = sysLogsService.list();
        if (CollectionUtils.isNotEmpty(list2)) {
            List<Integer> collect = list2.stream().filter(item -> item.getCreateTime().isBefore(lastTime)).map(SysLogs::getId).collect(Collectors.toList());
            sysLogsService.removeBatchByIds(collect);
        }
    }

    @Override
    protected String getCron() {
        SchedulerCron one = schedulerCronService.getById(4);
        return one.getCron();
    }
}

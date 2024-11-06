package com.teenet.controller;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.teenet.common.Result;
import com.teenet.entity.PageBody;
import com.teenet.entity.database.SysLogs;
import com.teenet.service.SysLogsService;
import com.teenet.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description
 * @Author threedong
 * @Date 2022/7/22 15:06
 */
@RestController
@RequestMapping("/sys_logs")
public class SysLogsController {

    @Autowired
    private SysLogsService logsService;

    @PostMapping("pageList")
    public Result<Page<SysLogs>> pageList(@RequestBody PageBody pageBody) {
        Integer pageNo = pageBody.getPageNo();
        Integer pageSize = pageBody.getPageSize();
        String type = pageBody.getType();
        String startTime = pageBody.getStartTime();
        String endTime = pageBody.getEndTime();
        Page<SysLogs> page = new Page<>(pageNo, pageSize);

        if (StringUtils.isNotBlank(startTime)) {
            endTime = DateUtil.addDay(endTime, DateUtil.FORMAT4, 1);
        }

        logsService.page(page, Wrappers.<SysLogs>lambdaQuery().eq(StringUtils.isNotBlank(type), SysLogs::getType, type)
                .ge(StringUtils.isNotBlank(startTime), SysLogs::getCreateTime, startTime)
                .lt(StringUtils.isNotBlank(endTime), SysLogs::getCreateTime, endTime)
                .orderByDesc(SysLogs::getCreateTime));
        return Result.createBySuccess(page);
    }


}

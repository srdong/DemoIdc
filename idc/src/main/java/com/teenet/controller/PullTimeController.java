package com.teenet.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.teenet.common.GlobalParam;
import com.teenet.common.Result;
import com.teenet.entity.database.PullTime;
import com.teenet.service.PullTimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description
 * @Author threedong
 * @Date 2022/7/22 15:06
 */
@RestController
@RequestMapping("/pull_time")
public class PullTimeController {

    @Autowired
    private PullTimeService pullTimeService;

    @GetMapping("pageList")
    public Result<Page<PullTime>> pageList(@RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                           @RequestParam(name = "pageSize", defaultValue = "-1") Integer pageSize) {
        Page<PullTime> page = new Page<>(pageNo, pageSize);
        pullTimeService.page(page, Wrappers.<PullTime>lambdaQuery().eq(PullTime::getType, 2).orderByDesc(PullTime::getInTime));
        return Result.createBySuccess(page);
    }

    @GetMapping("println")
    public Result<Boolean> println() {
        Boolean println = GlobalParam.println;
        return Result.createBySuccess(println);
    }

    @GetMapping("printlnChange")
    public Result<Boolean> printlnChange(@RequestParam Boolean state) {
        GlobalParam.println = state;
        return Result.createBySuccess();
    }

}

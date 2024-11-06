package com.teenet.controller;


import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.teenet.common.Result;
import com.teenet.entity.database.IdcPueRecord;
import com.teenet.service.IdcPueRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author threedong
 * @since 2022-11-29
 */
@RestController
@RequestMapping("/idc-pue-record")
public class IdcPueRecordController {

    @Autowired
    private IdcPueRecordService idcPueRecordService;

    @GetMapping("list")
    public Result<List<IdcPueRecord>> list(@RequestParam(value = "startTime", required = false) String startTime,
                                           @RequestParam(value = "endTime", required = false) String endTime) {
        if(StringUtils.isNotEmpty(startTime)){
            String temp = startTime;
            startTime = temp + " 00:00:00";
            if(StringUtils.isNotEmpty(endTime)){
                endTime = endTime + " 23:59:59";
            }else{
                endTime = temp + " 23:59:59";
            }
        }
        List<IdcPueRecord> list = idcPueRecordService.list(Wrappers.<IdcPueRecord>lambdaQuery()
                .between(StringUtils.isNotBlank(startTime), IdcPueRecord::getTime, startTime, endTime));
        return Result.createBySuccess(list);
    }

}


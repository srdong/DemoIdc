package com.teenet.controller;

import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.teenet.common.Result;
import com.teenet.entity.database.IdcPue;
import com.teenet.mapper.IdcPueMapper;
import com.teenet.preprocessor.InitializationCommandLineRunner;
import com.teenet.scheduler.BreakPointTask;
import com.teenet.scheduler.HighFrequencyTask;
import com.teenet.scheduler.UnsuccessfulDataTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Description
 * @Author threedong
 * @Date 2022/6/14 17:20
 */
@RestController
@RequestMapping("/high_frequency")
public class HighFrequencyController extends Transfer {

    @Autowired
    private InitializationCommandLineRunner initializationCommandLineRunner;

    @Autowired
    private IdcPueMapper idcPueMapper;

    @Autowired
    private HighFrequencyTask highFrequencyTask;

    @Autowired
    private UnsuccessfulDataTask unsuccessfulDataTask;

    @Autowired
    private BreakPointTask breakPointTask;

    @GetMapping("idcPue")
    public Result<IdcPue> idcPue() {
        IdcPue idcPue = idcPueMapper.selectOne(null);
        if (ObjectUtils.isNotNull(idcPue)) {
            idcPue.setMolecularVal(null);
            idcPue.setDenominatorVal(null);
        } else {
            return Result.createBySuccess(new IdcPue());
        }
        return Result.createBySuccess(idcPue);
    }

    @PostMapping("idcPueSaveOrUpdate")
    public Result<String> idcPue(@RequestBody IdcPue idcPue) {
        Integer id = idcPue.getId();
        if (null == id) {
            idcPueMapper.insert(idcPue);
        } else {
            idcPueMapper.updateById(idcPue);
        }
        initializationCommandLineRunner.refreshIdcPue();
        return Result.createBySuccess();
    }


    @GetMapping("sync")
    public Result<String> sync() {
        highFrequencyTask.processTask();
        return Result.createBySuccess();
    }

    @GetMapping("rePull")
    public Result<String> rePull() {
        unsuccessfulDataTask.processTask();
        return Result.createBySuccess();
    }

    @GetMapping("reBreakPointTask")
    public Result<String> reBreakPointTask() {
        breakPointTask.processTask();
        return Result.createBySuccess();
    }


}

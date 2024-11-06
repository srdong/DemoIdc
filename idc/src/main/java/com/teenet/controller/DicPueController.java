package com.teenet.controller;


import com.teenet.common.Result;
import com.teenet.entity.database.DicPue;
import com.teenet.service.DicPueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author threedong
 * @since 2022-11-28
 */
@RestController
@RequestMapping("/dic-pue")
public class DicPueController {

    @Autowired
    private DicPueService dicPueService;

    @GetMapping("list")
    public Result<List<DicPue>> list() {
        List<DicPue> list = dicPueService.list();
        return Result.createBySuccess(list);
    }
}


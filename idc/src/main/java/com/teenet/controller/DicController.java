package com.teenet.controller;


import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.teenet.common.Result;
import com.teenet.entity.database.Dic;
import com.teenet.service.DicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author threedong
 * @since 2022-06-14
 */
@RestController
@RequestMapping("/dic")
public class DicController {


    @Autowired
    private DicService dicService;

    @GetMapping("classification")
    public Result<List<Dic>> getCategory(@RequestParam(value = "categoryIs", defaultValue = "true") Boolean categoryIs,
                                         @RequestParam(value = "parentId", required = false) String parentId,
                                         @RequestParam(value = "type", required = false) Integer type) {
        List<Dic> list = null;
        if (categoryIs) {
            list = dicService.list(Wrappers.<Dic>lambdaQuery().isNull(Dic::getParentId).eq(ObjectUtils.isNotEmpty(type), Dic::getType, type));
        } else {
            Dic one = dicService.getOne(Wrappers.<Dic>lambdaQuery().eq(Dic::getName, parentId).eq(ObjectUtils.isNotEmpty(type), Dic::getType, type));
            if (ObjectUtils.isNotEmpty(one)) {
                Integer id = one.getId();
                list = dicService.list(Wrappers.<Dic>lambdaQuery().eq(Dic::getParentId, id).eq(ObjectUtils.isNotEmpty(type), Dic::getType, type));
            }
        }
        return Result.createBySuccess(list);
    }

    @GetMapping("clearType1")
    public Result<String> clearMap() {
        dicService.clearType1();
        return Result.createBySuccess();
    }

    /**
     *
     * @return
     */
    @GetMapping("category")
    public Result<List<String>> getCategory() {
        List<String> list = dicService.list(Wrappers.<Dic>lambdaQuery().isNotNull(Dic::getCategory)).stream().map(Dic::getCategory).distinct().collect(Collectors.toList());
        return Result.createBySuccess(list);
    }
}


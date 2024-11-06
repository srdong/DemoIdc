package com.teenet.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.teenet.entity.database.PullTime;

import java.time.LocalDateTime;

/**
 * @Description
 * @Author threedong
 * @Date: 2022/7/14 9:37
 */
public interface PullTimeService extends IService<PullTime> {

    LocalDateTime maxTime();
}

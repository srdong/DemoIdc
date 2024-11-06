package com.teenet.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.teenet.entity.database.PullTime;
import com.teenet.mapper.PullTimeMapper;
import com.teenet.service.PullTimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * @Description
 * @Author threedong
 * @Date: 2022/7/14 9:37
 */
@Service
public class PullTimeServiceImpl extends ServiceImpl<PullTimeMapper, PullTime> implements PullTimeService {

    @Autowired
    private PullTimeMapper pullTimeMapper;

    @Override
    public LocalDateTime maxTime() {
        return pullTimeMapper.maxTime();
    }
}

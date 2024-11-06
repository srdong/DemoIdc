package com.teenet.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.teenet.entity.database.PullTime;
import com.teenet.entity.database.SysLogs;
import com.teenet.mapper.PullTimeMapper;
import com.teenet.mapper.SysLogsMapper;
import com.teenet.service.PullTimeService;
import com.teenet.service.SysLogsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * @Description
 * @Author threedong
 * @Date: 2023/1/9 15:47
 */
@Service
public class SysLogsServiceImpl extends ServiceImpl<SysLogsMapper, SysLogs> implements SysLogsService {

}

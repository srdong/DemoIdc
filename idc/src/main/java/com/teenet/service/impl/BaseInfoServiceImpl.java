package com.teenet.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.teenet.entity.database.BaseInfo;
import com.teenet.mapper.BaseInfoMapper;
import com.teenet.service.BaseInfoService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author threedong
 * @since 2022-06-14
 */
@Service
public class BaseInfoServiceImpl extends ServiceImpl<BaseInfoMapper, BaseInfo> implements BaseInfoService {

}

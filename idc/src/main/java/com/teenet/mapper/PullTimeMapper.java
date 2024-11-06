package com.teenet.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.teenet.entity.database.PullTime;

import java.time.LocalDateTime;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author threedong
 * @since 2022-06-14
 */
public interface PullTimeMapper extends BaseMapper<PullTime> {

    LocalDateTime maxTime();

}

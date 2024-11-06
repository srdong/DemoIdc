package com.teenet.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.teenet.entity.database.Dic;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author threedong
 * @since 2022-06-14
 */
public interface DicService extends IService<Dic> {

    Map<String, List<String>> typeIs1();

    void clearType1();
}

package com.teenet.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.teenet.entity.database.Device;
import com.teenet.vo.ExportVo;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author threedong
 * @since 2022-06-14
 */
public interface DeviceService extends IService<Device> {

    int batchSave(List<ExportVo> data);
}

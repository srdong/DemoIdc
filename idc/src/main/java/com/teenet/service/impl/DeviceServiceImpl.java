package com.teenet.service.impl;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.teenet.entity.database.Device;
import com.teenet.entity.database.Dic;
import com.teenet.mapper.DeviceMapper;
import com.teenet.preprocessor.InitializationCommandLineRunner;
import com.teenet.service.DeviceService;
import com.teenet.service.DicService;
import com.teenet.vo.ExportVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author threedong
 * @since 2022-06-14
 */
@Service
public class DeviceServiceImpl extends ServiceImpl<DeviceMapper, Device> implements DeviceService {

    @Autowired
    private DicService dicService;

    @Autowired
    private InitializationCommandLineRunner initializationCommandLineRunner;

    @Override
    public int batchSave(List<ExportVo> data) {
        Map<String, Dic> dicMap = dicService.list().stream().collect(Collectors.toMap(Dic::getName, Function.identity()));
        List<Device> list = Lists.newArrayList();
        data.forEach(item -> {
            String mapping = item.getMapping();
            //空不添加
            boolean next = StringUtils.isNotBlank(mapping);
            if (next) {
                Device device = new Device();
                BeanUtils.copyProperties(item, device);
                //设置大类
                String deviceCategory = item.getDeviceCategory();
                String[] split = deviceCategory.split("\\.");
                String val1 = split[1];
                device.setDeviceCategory(val1);
                Dic dic = dicMap.get(val1);
                device.setMeterCategory(dic.getCategory());
                device.setMeterUnit(dic.getUnit());
                device.setMeterCode(dic.getCode());
                //设置小类
                String deviceSmallClass = item.getDeviceSmallClass();
                if (StringUtils.isNotBlank(deviceSmallClass)) {
                    String[] split2 = deviceSmallClass.split("\\.");
                    String val2 = split2[1];
                    device.setDeviceSmallClass(val2);
                    Dic dic2 = dicMap.get(val2);
                    device.setMeterCategory(dic2.getCategory());
                    device.setMeterUnit(dic2.getUnit());
                    device.setMeterCode(dic2.getCode());
                }
                device.setRealIs(true);
                boolean needUpload = "是".equals(item.getNeedUpload());
                device.setNeedUpload(needUpload);
                Double coe = item.getCoe();
                if(ObjectUtils.isNull(coe)){
                    device.setCoe(1D);
                }
                list.add(device);
            }
        });
        if (CollectionUtils.isNotEmpty(list)) {
            this.saveOrUpdateBatch(list);
            initializationCommandLineRunner.refreshCache();
        }
        return list.size();
    }
}

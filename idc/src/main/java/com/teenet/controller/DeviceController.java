package com.teenet.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.teenet.common.GlobalParam;
import com.teenet.common.Result;
import com.teenet.entity.database.Device;
import com.teenet.entity.database.Dic;
import com.teenet.entity.database.RealDeviceVo;
import com.teenet.preprocessor.InitializationCommandLineRunner;
import com.teenet.service.DeviceService;
import com.teenet.service.DicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author threedong
 * @since 2022-06-14
 */
@RestController
@RequestMapping("/device")
public class DeviceController extends Transfer {

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private DicService dicService;

    @Autowired
    private InitializationCommandLineRunner initializationCommandLineRunner;


    @PostMapping("saveOrUpdate")
    public Result<String> saveOrUpdate(@RequestBody Device device) {
        String meterCode = device.getMeterCode();
        meterCode = StringUtils.isBlank(meterCode) ? "" : meterCode;
        String meterCategory = device.getMeterCategory();
        meterCategory = StringUtils.isBlank(meterCategory) ? "" : meterCategory;
        String meterUnit = device.getMeterUnit();
        meterUnit = StringUtils.isBlank(meterUnit) ? "" : meterUnit;
        device.setMeterCode(meterCode);
        device.setMeterCategory(meterCategory);
        device.setMeterUnit(meterUnit);
        Double coe = device.getCoe();
        if (device.getRealIs()) {
            if (ObjectUtils.isNull(coe)) {
                device.setCoe(1D);
            }
            device.setFormula("");
        } else {
            List<RealDeviceVo> list = device.getList();
            if (CollectionUtils.isNotEmpty(list)) {
                String str = new Gson().toJson(list);
                device.setMapping(str);
                list.forEach(item -> GlobalParam.virtualBind.put(item.getId(), item.getId()));
            }
            Integer id = device.getId();
            if (ObjectUtils.isNotEmpty(id)) {
                long count = list.stream().map(RealDeviceVo::getId).filter(item -> item.equals(id)).count();
                if (count > 0) {
                    return Result.createByErrorMessage("虚拟器具无法选择自身进行映射");
                }
            }
        }
        deviceService.saveOrUpdate(device);
        initializationCommandLineRunner.refreshCache();
        return Result.createBySuccess();
    }

    /**
     * 删除前，先判断
     *
     * @param id
     * @return
     */
    @DeleteMapping("delById")
    public Result<String> delById(@RequestParam("id") String id) {
        Device one = deviceService.getById(id);
        Boolean realIs = one.getRealIs();
        if (realIs) {
            //如果是真实器具，
            Integer exist = GlobalParam.virtualBind.get(one.getId());
            if (ObjectUtils.isNotNull(exist)) {
                return Result.createByErrorMessage("已绑定虚拟器具，无法删除");
            }
            deviceService.removeById(id);
        } else {
            //如果是虚拟器具，先移除，在重新赋值
            deviceService.removeById(id);
            GlobalParam.virtualBind = deviceService.list(Wrappers.<Device>lambdaQuery().eq(Device::getRealIs, false))
                    .stream().map(item -> {
                        String context = item.getMapping();
                        if (StringUtils.isNotBlank(context)) {
                            List<RealDeviceVo> lists = new Gson().fromJson(context, new TypeToken<List<RealDeviceVo>>() {
                            }.getType());
                            return lists.stream().map(RealDeviceVo::getId).collect(Collectors.toList());
                        }
                        return null;
                    }).filter(ObjectUtils::isNotNull)
                    .flatMap(item -> item.stream())
                    .distinct()
                    .collect(Collectors.toMap(Function.identity(), Function.identity(), (k1, k2) -> k1));
        }
        initializationCommandLineRunner.refreshCache();
        return Result.createBySuccess();
    }

    @GetMapping("pageList")
    public Result<Page<Device>> pageList(@RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                         @RequestParam(name = "pageSize", defaultValue = "-1") Integer pageSize,
                                         @RequestParam(name = "category", required = false) String category,
                                         @RequestParam(name = "smallClass", required = false) String smallClass,
                                         @RequestParam(name = "name", required = false) String name,
                                         @RequestParam(name = "realIs", required = false) Boolean realIs,
                                         @RequestParam(name = "meterCategory", required = false) String meterCategory) {
        Page<Device> page = new Page<>(pageNo, pageSize);
        deviceService.page(page, new QueryWrapper<Device>().eq(StringUtils.isNotBlank(category), "device_category", category)
                .eq(StringUtils.isNotBlank(smallClass), "device_small_class", smallClass)
                .eq(StringUtils.isNotBlank(meterCategory), "meter_category", meterCategory)
                .eq(ObjectUtils.isNotNull(realIs), "real_is", realIs)
                .like(StringUtils.isNotBlank(name), "name_desc", name)
                .orderBy(true, true, "meter_code = ''||ISNULL(meter_code)", "meter_code"));
        List<Device> records = page.getRecords();
        if (CollectionUtils.isNotEmpty(records)) {
            Map<String, String> collect = dicService.list().stream().collect(Collectors.toMap(Dic::getName, Dic::getDescription));
            records.forEach(item -> {
                if (!item.getRealIs()) {
                    String mapping = item.getMapping();
                    if (StringUtils.isNotBlank(mapping)) {
                        List<RealDeviceVo> list = new Gson().fromJson(mapping, new TypeToken<List<RealDeviceVo>>() {
                        }.getType());
                        item.setList(list);
                        item.setMapping(null);
                    }
                }
                String val1 = collect.get(item.getDeviceCategory());
                item.setDeviceCategoryDesc(val1);
                String val2 = collect.get(item.getDeviceSmallClass());
                item.setDeviceSmallClassDesc(val2);
                Integer id = item.getId();
                Double aDouble = GlobalParam.currentVal.get(id);
                aDouble = null == aDouble ? 0 : aDouble;
                item.setCurrentVal(aDouble);
            });
        }
        return Result.createBySuccess(page);
    }

    @GetMapping("refreshCache")
    public Result<String> refreshCache() {
        initializationCommandLineRunner.refreshCache();
        return Result.createBySuccess();
    }

    /**
     * 需要上传的仪表
     *
     * @return
     */
    @GetMapping("sync")
    public Result<String> sync() {
        List<Device> list = deviceService.list(Wrappers.<Device>lambdaQuery().eq(Device::getRealIs, true).eq(Device::getNeedUpload, true));
        if (CollectionUtils.isEmpty(list)) {
            return Result.createByErrorMessage("数据不存在无法同步");
        }
        Map<String, Object> maps = packageData(list, null, null);
        return syncData(maps, GlobalParam.deviceInfoUrl);
    }


}


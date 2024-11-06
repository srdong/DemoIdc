package com.teenet.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.read.metadata.ReadSheet;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.common.collect.Lists;
import com.teenet.common.Result;
import com.teenet.entity.database.Device;
import com.teenet.entity.database.Dic;
import com.teenet.service.DeviceService;
import com.teenet.service.DicService;
import com.teenet.util.ExcelUtil;
import com.teenet.vo.ExportVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;


/**
 * @Description
 * @Author threedong
 * @Date: 2022/11/3 17:44
 */
@RequestMapping("excel")
@RestController
public class ExcelController {

    @Autowired
    private DicService dicService;

    @Autowired
    private DeviceService deviceService;


    @GetMapping(value = "exportTemplate")
    public void exportTemplate(HttpServletResponse response) {
        export(response, Lists.newArrayList(), 50, "idcTemplate");
    }

    @GetMapping(value = "exportData")
    public void exportData(HttpServletResponse response) {
        List<Device> list = deviceService.list(Wrappers.<Device>lambdaQuery().eq(Device::getRealIs, true).orderByAsc(Device::getMeterCode));
        Map<String, String> collect = dicService.list()
                .stream().collect(Collectors.toMap(Dic::getName, item -> {
                    String description = item.getDescription();
                    String category = item.getCategory();
                    String desc = StringUtils.isBlank(category) ? description : category;
                    return desc + "." + item.getName();
                }));
        List<ExportVo> exportData = Lists.newArrayList();
        list.forEach(item -> {
            ExportVo vo = new ExportVo();
            BeanUtils.copyProperties(item, vo);
            Boolean needUpload = item.getNeedUpload();
            String upload = needUpload ? "是" : "否";
            vo.setNeedUpload(upload);
            String val1 = collect.get(vo.getDeviceCategory());
            vo.setDeviceCategory(val1);
            String val2 = collect.get(vo.getDeviceSmallClass());
            vo.setDeviceSmallClass(val2);
            exportData.add(vo);
        });
        export(response, exportData, exportData.size(), "idcData");
    }

    private void export(HttpServletResponse response, Collection<?> data, int size, String fileName) {
        List<Dic> list = dicService.list();
        Map<String, List<String>> dropDownMap2 = new HashMap<>(64);
        Map<Integer, List<String>> collect1 = list.stream().filter(item -> ObjectUtils.isNotNull(item.getParentId()))
                .collect(Collectors.groupingBy(Dic::getParentId,
                        Collectors.collectingAndThen(Collectors.toList(),
                                item -> item.stream().map(item2 -> {
                                    String description = item2.getDescription();
                                    String category = item2.getCategory();
                                    String desc = StringUtils.isBlank(category) ? description : category;
                                    return desc + "." + item2.getName();
                                })
                                        .collect(Collectors.toList()))));
        List<Dic> collect = list.stream().filter(item -> ObjectUtils.isNull(item.getParentId())).collect(Collectors.toList());
        collect.forEach(item -> {
            Integer id = item.getId();
            List<String> stringList = collect1.get(id);
            dropDownMap2.put(item.getDescription() + "." + item.getName(), stringList);
        });
        String template = fileName + ".xlsx";
        InputStream inputStream = ExcelController.class.getClassLoader().getResourceAsStream(template);
        Map<Integer, String[]> dropDownMap = new HashMap<>(4);
        String[] str = {"是", "否"};
        dropDownMap.put(8, str);
        ExcelUtil.templateWithPullDown(response, fileName, dropDownMap, dropDownMap2, inputStream, data, size);
    }


    @PostMapping("/upload")
    public Result<String> upload(@RequestParam("file") MultipartFile file) {
        AnalysisEventListener<ExportVo> analysisEventListener = ExcelUtil.getListen(this.batchInsert(), 100);
        ExcelReader excelReader = null;
        try (InputStream in = file.getInputStream()) {
            excelReader = EasyExcel.read(in, ExportVo.class, analysisEventListener).build();
            ReadSheet readSheet = EasyExcel.readSheet(0).build();
            excelReader.read(readSheet);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (excelReader != null) {
                excelReader.finish();
            }
        }
        return Result.createBySuccess();
    }

    private Consumer<List<ExportVo>> batchInsert() {
        return data -> deviceService.batchSave(data);
    }


}

package com.teenet.controller;


import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.teenet.common.GlobalParam;
import com.teenet.common.Result;
import com.teenet.entity.baseinfo.IDC_Basic_Info;
import com.teenet.entity.baseinfo.IDC_Facility;
import com.teenet.entity.baseinfo.IDC_SubMetering;
import com.teenet.entity.database.BaseInfo;
import com.teenet.service.BaseInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author threedong
 * @since 2022-06-14
 */
@RestController
@RequestMapping("/base-info")
public class BaseInfoController extends Transfer {

    @Autowired
    private BaseInfoService baseInfoService;

    @PostMapping("saveOrUpdate")
    public Result<String> saveOrUpdate(@RequestBody BaseInfo baseInfo) {
        String idcName = baseInfo.getIdcName();
        if (StringUtils.isNotBlank(idcName)) {
            GlobalParam.customerName = idcName;
        }
        baseInfoService.saveOrUpdate(baseInfo);
        return Result.createBySuccess();
    }

    @GetMapping("detail")
    public Result<BaseInfo> detail() {
        List<BaseInfo> baseInfo = baseInfoService.list();
        if (CollectionUtils.isNotEmpty(baseInfo)) {
            return Result.createBySuccess(baseInfo.get(0));
        }
        return Result.createBySuccess(new BaseInfo());
    }

    @GetMapping("sync")
    public Result<String> sync() {
        List<BaseInfo> list = baseInfoService.list();
        if (CollectionUtils.isEmpty(list)) {
            return Result.createByErrorMessage("数据不存在无法同步");
        }
        BaseInfo one = list.get(0);
        IDC_Basic_Info idc_basic_info = IDC_Basic_Info.builder().IDC_Name(one.getIdcName())
                .IDC_Address(one.getIdcAddress()).IDC_Owner(one.getIdcOwner()).IDC_Contact(one.getIdcContact())
                .IDC_Owner_Phone(one.getIdcOwnerPhone()).IDC_Operator(one.getIdcOperator()).IDC_Operator_Contact(one.getIdcOperatorContact())
                .IDC_Operator_Contact_Phone(one.getIdcOperatorContactPhone()).IDC_Area_m2(one.getIdcAreaM2()).IDC_Service_Start(one.getIdcServiceStart())
                .IDC_Storey(one.getIdcStorey()).IDC_Total_Floors(one.getIdcTotalFloors()).IDC_Design_Cabinets_Number(one.getIdcDesignCabinetsNumber())
                .IDC_Actual_Cabinets_Number(one.getIdcActualCabinetsNumber()).IDC_Design_Total_Power_kW(one.getIdcDesignTotalPowerKw())
                .build();
        IDC_Facility idc_facility = IDC_Facility.builder().IDC_Facility_DieselGenerator(one.getIdcFacilityDieselgenerator())
                .IDC_Facility_Cabinet(one.getIdcFacilityCabinet()).IDC_Facility_Office(one.getIdcFacilityOffice())
                .IDC_Facility_FossilGenerator(one.getIdcFacilityFossilgenerator()).IDC_Facility_Renewable(one.getIdcFacilityRenewable())
                .IDC_Facility_Storage(one.getIdcFacilityStorage()).IDC_Facility_ColdSite(one.getIdcFacilityColdsite())
                .build();
        IDC_SubMetering idc_subMetering = IDC_SubMetering.builder().IDC_SubMetering_Supply(one.getIdcSubmeteringSupply())
                .IDC_SubMetering_DieselGenerator(one.getIdcSubmeteringDieselgenerator())
                .IDC_SubMetering_ITEC(one.getIdcSubmeteringItec()).IDC_SubMetering_Cabinet(one.getIdcSubmeteringCabinet())
                .IDC_SubMetering_Office(one.getIdcSubmeteringOffice()).IDC_SubMetering_FossilGenerator(one.getIdcSubmeteringFossilgenerator())
                .IDC_SubMetering_Renewable(one.getIdcSubmeteringRenewable()).IDC_SubMetering_Storage(one.getIdcSubmeteringStorage())
                .IDC_SubMetering_ColdSite(one.getIdcSubmeteringColdsite()).IDC_SubMetering_TemperatureField(one.getIdcSubmeteringTemperaturefield())
                .IDC_SubMetering_AirConditioning(one.getIdcSubmeteringAirconditioning()).IDC_SubMetering_Lighting(one.getIdcSubmeteringLighting())
                .build();

        Map<String, Object> map = new HashMap<>(8);
        map.put("IDC_Basic_Info", idc_basic_info);
        map.put("IDC_Facility", idc_facility);
        map.put("IDC_SubMetering", idc_subMetering);
        return syncData(map, GlobalParam.idcInfoUrl);
    }
}


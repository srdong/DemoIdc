package com.teenet.entity.database;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author threedong
 * @since 2022-06-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class BaseInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String idcName;

    private String idcAddress;

    private String idcOwner;

    private String idcContact;

    private String idcOwnerPhone;

    private String idcOperator;

    private String idcOperatorContact;

    private String idcOperatorContactPhone;

    private Double idcAreaM2;

    private String idcServiceStart;

    private String idcStorey;

    private String idcTotalFloors;

    private Integer idcDesignCabinetsNumber;

    private Integer idcActualCabinetsNumber;

    private Double idcDesignTotalPowerKw;

    private Boolean idcFacilityDieselgenerator;

    private Boolean idcFacilityCabinet;

    private Boolean idcFacilityOffice;

    private Boolean idcFacilityFossilgenerator;

    private Boolean idcFacilityRenewable;

    private Boolean idcFacilityStorage;

    private Boolean idcFacilityColdsite;

    private Boolean idcSubmeteringSupply;

    private Boolean idcSubmeteringDieselgenerator;

    private Boolean idcSubmeteringItec;

    private Boolean idcSubmeteringCabinet;

    private Boolean idcSubmeteringOffice;

    private Boolean idcSubmeteringFossilgenerator;

    private Boolean idcSubmeteringRenewable;

    private Boolean idcSubmeteringStorage;

    private Boolean idcSubmeteringColdsite;

    private Boolean idcSubmeteringTemperaturefield;

    private Boolean idcSubmeteringAirconditioning;

    private Boolean idcSubmeteringLighting;


}

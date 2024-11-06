package com.teenet.common;

import com.serotonin.modbus4j.ModbusMaster;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description
 * @Author threedong
 * @Date 2022/7/7 8:52
 */
@Component
public class GlobalParam {

    /**
     * 客户的真实器具
     * 拉取客户数据时使用
     * key = 客户的ID val = 系数值
     */
    public static Map<String, Double> realDevicesMapVal = new HashMap<>();

    /**
     * key = 56 , val = A+B|A_1|B_1
     * 虚拟器具
     */
    public static Map<Integer, String> virtualMap = new HashMap<>();

    public static Map<Integer, String> idcPueValHis = new HashMap<>();
    public static Map<Integer, String> idcPueValNow = new HashMap<>();

    public static Integer DENOMINATOR = 1;
    public static Integer MOLECULAR = 2;

    /**
     * key = 代数 ，val = 对应的deviceId
     */
    public static Map<String, List<Integer>> denominatorMap = new HashMap<>();
    public static Map<String, List<Integer>> molecularMap = new HashMap<>();
    public static String denominatorFormula;
    public static String molecularFormula;

    /**
     * 所有虚拟器具绑定的真实仪表的ID
     */
    public static Map<Integer, Integer> virtualBind = new HashMap<>();

    /**
     * 公用
     * BreakPointTask 和 UnsuccessfulDataTask 能用上
     */
    public static Integer code = ResponseCode.ERROR.getCode();

    /**
     * 当前值
     */
    public static Map<Integer, Double> currentVal = new HashMap<>();

    /**
     * 公用
     * BreakPointTask 和 UnsuccessfulDataTask 不同时跑
     */
    public static boolean notRun = true;


    public static String IDC_IT_EQUIPMENT_LOAD = "IDC_ITEquipment_Load";

    /**
     * 修正测量中的a1，a2，a3的值
     */
    public static String a1 = "0";
    public static String a2 = "0";
    public static String a3 = "0";

    public static String A1 = "a1";
    public static String A2 = "a2";
    public static String A3 = "a3";


    public static String idcId;
    public static String idcInfoUrl;
    public static String deviceInfoUrl;
    public static String lowFrequencyUrl;
    public static String highFrequencyUrl;
    public static String highFrequencyFilePath;
    public static String apiPem;
    public static String rsaPem;
    public static Boolean unsuccessfulDataRun;
    public static String customer;
    public static String cusTcpIp;
    public static Integer cusTcpPort;
    public static String cusLoginUrl;
    public static String cusVersion;
    public static String cusUsername;
    public static String cusPassword;
    public static String cusLocalIp;
    public static String cusLogoutUrl;
    public static String cusRealTimeUrl;
    public static String cusHisTimeUrl;
    public static String customerName;

    public static String sqlServerDriverClassName;
    public static String sqlServerUrl;
    public static String sqlServerUsername;
    public static String sqlServerPassword;

    public static Boolean println;

    public static Boolean smsMessage;
    public static String smsUser;

    public static ModbusMaster modbusMaster;
    public static String modbusHost;
    public static Integer modbusPort;
    public static Boolean modbusEnable;


}

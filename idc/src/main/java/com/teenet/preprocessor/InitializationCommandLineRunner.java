package com.teenet.preprocessor;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.serotonin.modbus4j.ModbusFactory;
import com.serotonin.modbus4j.ModbusMaster;
import com.serotonin.modbus4j.exception.ModbusInitException;
import com.serotonin.modbus4j.ip.IpParameters;
import com.teenet.common.ActionCode;
import com.teenet.common.GlobalParam;
import com.teenet.config.IdcConfig;
import com.teenet.entity.database.*;
import com.teenet.mapper.IdcPueMapper;
import com.teenet.service.*;
import com.teenet.util.WechatUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Description
 * @Author threedong
 * @Date: 2022/7/7 8:42
 */
@Slf4j
@Component
@Order(1)
public class InitializationCommandLineRunner implements CommandLineRunner {

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private IdcPueMapper idcPueMapper;

    @Autowired
    private PullTimeService pullTimeService;

    @Autowired
    private IdcConfig idcConfig;

    @Autowired
    private BaseInfoService baseInfoService;

    @Autowired
    private LowFrequencyService lowFrequencyService;

    @Autowired
    private LimitRangesService limitRangesService;


    @Autowired
    private DicPueService dicPueService;

    @Override
    public void run(String... args) throws Exception {
        init0();
        WechatUtil.sendMsg("系统启动中", ActionCode.SYSTEM_STARTUP.getDesc());
        init1();
        init2();
        init3();
        init4();
        initModbusTcpMaster();
    }

    public void init0() {
        GlobalParam.idcId = idcConfig.getId();
        GlobalParam.idcInfoUrl = idcConfig.getIdcInfoUrl();
        GlobalParam.deviceInfoUrl = idcConfig.getDeviceInfoUrl();
        GlobalParam.lowFrequencyUrl = idcConfig.getLowFrequencyUrl();
        GlobalParam.highFrequencyUrl = idcConfig.getHighFrequencyUrl();
        GlobalParam.highFrequencyFilePath = idcConfig.getHighFrequencyFilePath();
        GlobalParam.apiPem = idcConfig.getApiPem();
        GlobalParam.rsaPem = idcConfig.getRsaPem();
        GlobalParam.customer = idcConfig.getCustomer();
        GlobalParam.cusTcpIp = idcConfig.getCusTcpIp();
        GlobalParam.cusTcpPort = idcConfig.getCusTcpPort();
        GlobalParam.unsuccessfulDataRun = idcConfig.getUnsuccessfulDataRun();
        GlobalParam.cusLoginUrl = idcConfig.getCusLoginUrl();
        GlobalParam.cusLogoutUrl = idcConfig.getCusLogoutUrl();
        GlobalParam.cusVersion = idcConfig.getCusVersion();
        GlobalParam.cusPassword = idcConfig.getCusPassword();
        GlobalParam.cusUsername = idcConfig.getCusUsername();
        GlobalParam.cusRealTimeUrl = idcConfig.getCusRealTimeUrl();
        GlobalParam.cusHisTimeUrl = idcConfig.getCusHisTimeUrl();
        GlobalParam.customerName = idcConfig.getCustomer();

        GlobalParam.sqlServerDriverClassName = idcConfig.getSqlServerDriverClassName();
        GlobalParam.sqlServerPassword = idcConfig.getSqlServerPassword();
        GlobalParam.sqlServerUrl = idcConfig.getSqlServerUrl();
        GlobalParam.sqlServerUsername = idcConfig.getSqlServerUsername();
        GlobalParam.println = idcConfig.getPrintln();
        GlobalParam.smsMessage = idcConfig.getSmsMessage();
        GlobalParam.smsUser = idcConfig.getSmsUser();
        GlobalParam.modbusHost = idcConfig.getModbusHost();
        GlobalParam.modbusPort = idcConfig.getModbusPort();
        GlobalParam.modbusEnable = idcConfig.getModbusEnable();
    }

    public void refreshCache() {
        init1();
    }

    public void refreshIdcPue() {
        init2();
    }

    public void refreshIDC_ITEquipment_Load() {
        init5();
    }

    private void init1() {
        List<Device> total = deviceService.list();
        if (CollectionUtils.isNotEmpty(total)) {
            //获取所有真实器具,拉取客户数据的时候用
            Map<String, Double> collect = total.stream()
                    .filter(Device::getRealIs)
                    .filter(item -> StringUtils.isNotEmpty(item.getMapping()))
                    .collect(Collectors.toMap(Device::getMapping, Device::getCoe, (k1, k2) -> k1));
            GlobalParam.realDevicesMapVal.putAll(collect);
            //获取所有虚拟器具
            List<Device> list = total.stream().filter(item -> !item.getRealIs()).collect(Collectors.toList());
            list.forEach(item -> {
                String mapping = item.getMapping();
                String formula = item.getFormula();
                String val = packageData(mapping, formula);
                if (null != val) {
                    GlobalParam.virtualMap.put(item.getId(), val);
                }
            });
        }
    }

    /**
     * 1是分母，2是分子
     */
    private void init2() {
        GlobalParam.denominatorMap.clear();
        GlobalParam.molecularMap.clear();

        init5();

        List<Device> list = deviceService.list();
        //根据meterCode分组
        Map<String, List<Integer>> meterCodeMap = list.stream()
                .filter(item2 -> StringUtils.isNotBlank(item2.getMeterCode()))
                .collect(Collectors.groupingBy(Device::getMeterCode,
                        Collectors.collectingAndThen(Collectors.toList(),
                                item -> item.stream().map(Device::getId).filter(ObjectUtils::isNotEmpty).collect(Collectors.toList()))));
        List<IdcPue> idcPue = idcPueMapper.selectList(null);
        if (CollectionUtils.isNotEmpty(idcPue)) {
            IdcPue idcPue1 = idcPue.get(0);
            String denominatorVal = idcPue1.getDenominatorVal();
            String molecularVal = idcPue1.getMolecularVal();
            GlobalParam.idcPueValNow.put(GlobalParam.DENOMINATOR, denominatorVal);
            GlobalParam.idcPueValNow.put(GlobalParam.MOLECULAR, molecularVal);
            //对应的计算公式
            Integer dicPueId = idcPue1.getDicPueId();
            DicPue dicPue = dicPueService.getById(String.valueOf(dicPueId));
            //分子
            String molecular = dicPue.getMolecular();
            Arrays.stream(molecular.split("\\|")).forEach(item -> {
                boolean nota1a2a3 = GlobalParam.A1.equals(item) || GlobalParam.A2.equals(item) || GlobalParam.A3.equals(item);
                if (!nota1a2a3) {
                    List<Integer> stringList = meterCodeMap.get(item);
                    GlobalParam.molecularMap.put(item, stringList);
                }
            });
            //分母
            String denominator = dicPue.getDenominator();
            Arrays.stream(denominator.split("\\|")).forEach(item -> {
                boolean nota1a2a3 = GlobalParam.A1.equals(item) || GlobalParam.A2.equals(item) || GlobalParam.A3.equals(item);
                if (!nota1a2a3) {
                    List<Integer> stringList = meterCodeMap.get(item);
                    GlobalParam.denominatorMap.put(item, stringList);
                }
            });
            //分母计算公式
            String denominatorFormula = dicPue.getDenominatorFormula();
            GlobalParam.denominatorFormula = denominatorFormula.replaceAll("a1", GlobalParam.a1).replaceAll("a2", GlobalParam.a2).replaceAll("a3", GlobalParam.a3);
            //分子计算公式
            String molecularFormula = dicPue.getMolecularFormula();
            GlobalParam.molecularFormula = molecularFormula;

        }
    }


    private String packageData(String context, String formula) {
        boolean next = StringUtils.isNotBlank(context) && StringUtils.isNotBlank(formula);
        if (next) {
            List<RealDeviceVo> lists = new Gson().fromJson(context, new TypeToken<List<RealDeviceVo>>() {
            }.getType());
            lists.forEach(item -> GlobalParam.virtualBind.put(item.getId(), item.getId()));
            String collect = lists.stream()
                    .map(item2 -> item2.getLetter() + "_" + item2.getId())
                    .collect(Collectors.joining("|"));
            return formula + "|" + collect;
        }
        return null;
    }

    /**
     * 如果系统重启，查看丢失的时间
     */
    private void init3() {
        //maxTime的分钟数只可能是00，15，30，45
        LocalDateTime maxTime = pullTimeService.maxTime();
        if (null != maxTime) {
            LocalDateTime now = LocalDateTime.now();
            Duration duration = Duration.between(maxTime, now);
            boolean to = duration.toMinutes() >= 15;
            LocalDateTime plusTime = maxTime;
            List<PullTime> list = Lists.newArrayList();
            if (to) {
                //大于15分钟的数据
                while (true) {
                    plusTime = plusTime.plusMinutes(15);
                    if (plusTime.isAfter(now)) {
                        break;
                    }
                    PullTime pullTime = new PullTime();
                    pullTime.setSuccessIs(false);
                    pullTime.setInTime(plusTime);
                    pullTime.setType(1);
                    list.add(pullTime);
                }
            }
            if (CollectionUtils.isNotEmpty(list)) {
                pullTimeService.saveBatch(list);
            }
        }
    }

    private void init4() {
        BaseInfo one = baseInfoService.getOne(null);
        if (ObjectUtils.isNotNull(one)) {
            String idcName = one.getIdcName();
            if (StringUtils.isNotBlank(idcName)) {
                GlobalParam.customerName = idcName;
            }
        }
    }

    /**
     * 修正计算中的取值
     */
    public void init5() {
        Double value = 0D;
        LowFrequency one = lowFrequencyService.getOne(Wrappers.<LowFrequency>lambdaQuery().eq(LowFrequency::getType, GlobalParam.IDC_IT_EQUIPMENT_LOAD));
        if (ObjectUtils.isNotNull(one)) {
            value = ObjectUtils.isNull(one.getValue()) ? value : one.getValue();
        }
        List<LimitRanges> list = limitRangesService.list();
        int size = list.size();
        for (int i = 0; i < size; i++) {
            LimitRanges getOne = list.get(i);
            boolean b = compareVal(value, getOne);
            if (b) {
                break;
            }
        }
    }

    private boolean compareVal(Double value, LimitRanges one) {
        boolean lowerBoolean;
        boolean upperBoolean;
        String lowerLimit = one.getLowerLimit();
        String upperLimit = one.getUpperLimit();
        if (StringUtils.isNotBlank(lowerLimit)) {
            double v1 = Double.parseDouble(lowerLimit);
            lowerBoolean = value.compareTo(v1) > 0;
        } else {
            lowerBoolean = true;
        }

        if (StringUtils.isNotBlank(upperLimit)) {
            double v1 = Double.parseDouble(upperLimit);
            upperBoolean = value.compareTo(v1) <= 0;
        } else {
            upperBoolean = true;
        }

        if (lowerBoolean && upperBoolean) {
            GlobalParam.a1 = one.getVal1();
            GlobalParam.a2 = one.getVal2();
            GlobalParam.a3 = one.getVal3();
        }
        return lowerBoolean && upperBoolean;
    }


    private void initModbusTcpMaster() {
        if (Boolean.TRUE.equals(GlobalParam.modbusEnable)) {
            IpParameters parameters = new IpParameters();
            parameters.setHost(GlobalParam.modbusHost);
            parameters.setPort(GlobalParam.modbusPort);
            parameters.setEncapsulated(false);
            ModbusFactory modbusFactory = new ModbusFactory();
            ModbusMaster master = modbusFactory.createTcpMaster(parameters, false);
            master.setTimeout(3000);
            master.setRetries(1);
            try {
                master.init();
                GlobalParam.modbusMaster = master;
            } catch (ModbusInitException e) {
                e.printStackTrace();
            }
        }
    }
}

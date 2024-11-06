package com.teenet.config;

/**
 * @Description
 * @Author threedong
 * @Date 2022/7/26 11:17
 */

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
@ConfigurationProperties(prefix = "idc", ignoreInvalidFields = true)
public class IdcConfig {

    private String id;
    private String idcInfoUrl;
    private String deviceInfoUrl;
    private String lowFrequencyUrl;
    private String highFrequencyUrl;
    private String highFrequencyFilePath;
    private String apiPem;
    private String rsaPem;
    /**
     * 客户如果不支持按时间查询，需关闭
     */
    private Boolean unsuccessfulDataRun;
    /**
     * 客户的名字
     * customer包下@servie的默认名字一样，
     * （默认是class类首字母小写的全名）
     */
    private String customer;
    private String cusTcpIp;
    private Integer cusTcpPort;
    private String cusLoginUrl;
    private String cusVersion;
    private String cusUsername;
    private String cusPassword;
    private String cusLocalIp;
    private String cusLogoutUrl;
    private String cusRealTimeUrl;
    private String cusHisTimeUrl;

    private String sqlServerDriverClassName;
    private String sqlServerUrl;
    private String sqlServerUsername;
    private String sqlServerPassword;

    private Boolean println;

    private Boolean smsMessage;
    private String smsUser;

    private String modbusHost;
    private Integer modbusPort;
    private Boolean modbusEnable;

}

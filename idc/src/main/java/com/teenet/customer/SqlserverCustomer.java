package com.teenet.customer;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.teenet.common.GlobalParam;
import com.teenet.customer.entity.SqlServerEntity;
import com.teenet.util.BigDecimalUtil;
import com.teenet.util.JdbcUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description sqlserver
 * @Author threedong
 * @Date 2022/7/14 10:22
 */
@Slf4j
@Service
public class SqlserverCustomer implements CustomerInterface {

    @Override
    public Map<String, Double> getData() {
        Map<String, Double> fromCus = new HashMap<>(GlobalParam.realDevicesMapVal.size());
        String sql = "select PName,PValue from [View_High-frequency] ";
        List<SqlServerEntity> values = (List<SqlServerEntity>) JdbcUtil.findDataByPropertiesFile(sql, SqlServerEntity.class, "PName", "PValue");
        values.forEach(item -> {
            String val = item.getPValue();
            if(StringUtils.isNotEmpty(val)){
                Double coe = GlobalParam.realDevicesMapVal.get(item.getPName());
                Double last = BigDecimalUtil.mulWithDouAndStr(val, coe);
                fromCus.put(item.getPName(), last);

            }
        });
        return fromCus;
    }

    @Override
    public Map<String, Double> dataWithTime(String time) {
        return new HashMap<>(2);
    }


}

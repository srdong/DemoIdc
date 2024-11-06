package com.teenet.customer;

import com.serotonin.modbus4j.BatchRead;
import com.serotonin.modbus4j.BatchResults;
import com.serotonin.modbus4j.locator.BaseLocator;
import com.teenet.common.ActionCode;
import com.teenet.common.GlobalParam;
import com.teenet.threadpool.MyThreadPool;
import com.teenet.util.BigDecimalUtil;
import com.teenet.util.WechatUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description
 * @Author threedong
 * @Date: 2023/4/11 14:02
 */
@Slf4j
@Service
public class ModBusCustomer implements CustomerInterface {

    @Override
    public Map<String, Double> getData() {
        boolean println = null != GlobalParam.println && GlobalParam.println;
        Map<String, Double> fromCus = new HashMap<>(GlobalParam.realDevicesMapVal.size());
        try {
            List<String> data = new ArrayList<>(GlobalParam.realDevicesMapVal.keySet());
            BatchRead<Integer> batch = new BatchRead<>();
            int size = data.size();
            for (int i = 0; i < size; i++) {
                String address = data.get(i);
                String[] split = address.split("_");
                Integer subAddress = Integer.valueOf(split[0]);
                Integer messageTarget = Integer.valueOf(split[1]);
                Integer dataType = Integer.valueOf(split[2]);
                //System.out.println(i + " : " + subAddress + " : " + messageTarget);
                batch.addLocator(i, BaseLocator.holdingRegister(subAddress, messageTarget, dataType));
            }
            batch.setContiguousRequests(false);
            BatchResults<Integer> results = GlobalParam.modbusMaster.send(batch);
            for (int i = 0; i < size; i++) {
                String address = data.get(i);
                Object value = results.getValue(i);
                if (null != value) {
                    if (println) {
                        log.info(String.valueOf(value));
                    }
                    Double val = Double.parseDouble(value.toString());
                    Double coe = GlobalParam.realDevicesMapVal.get(address);
                    Double last = BigDecimalUtil.mulWithDouAndStr(val, coe);
                    fromCus.put(address, last);
                } else {
                    fromCus.put(address, 0D);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            MyThreadPool.EXECUTOR_SERVICE.execute(() -> WechatUtil.sendMsg("modbus get data error ", ActionCode.DATA_PULL_ERROR.getDesc()));
        }
        return fromCus;
    }

    @Override
    public Map<String, Double> dataWithTime(String time) {
        return new HashMap<>(2);
    }


}

package com.teenet.customer;

import com.teenet.entity.database.PullTime;
import com.teenet.service.PullTimeService;

import com.teenet.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description 中国移动
 * @Author threedong
 * @Date 2022/7/14 10:22
 */
@Service
public class Cmcc implements CustomerInterface {


    @Autowired
    private PullTimeService pullTimeService;

    @Override
    public Map<String, Double> getData() {

        try {
            //MasterFactory.createTcpClientMaster().setDataHandler(new SysDataHandler()).run();
        } catch (Exception e) {

            e.printStackTrace();
        }


        LocalDateTime now = DateUtil.now00();
        //todo test  要加上倍率
        Map<String, Double> fromCus = new HashMap<>(6);
        fromCus.put("1", 1D);
        fromCus.put("2", 2D);
        fromCus.put("3", 3D);
        System.out.println("GuiJing");
        //不管成功不成功都需要插入数据，方便到时补数据
        PullTime pullTime = new PullTime();
        pullTime.setSuccessIs(true);
        pullTime.setInTime(now);
        pullTime.setType(1);
        pullTimeService.save(pullTime);

        return fromCus;
    }

    @Override
    public Map<String, Double> dataWithTime(String time) {
        Map<String, Double> fromCus = new HashMap<>(6);
        fromCus.put("1", 1D);
        fromCus.put("2", 2D);
        fromCus.put("3", 3D);
        return fromCus;
    }
}

package com.teenet.util;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.teenet.common.ActionCode;
import com.teenet.common.GlobalParam;
import com.teenet.threadpool.MyThreadPool;

import javax.sql.DataSource;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description
 * @Author threedong
 * @Date: 2022/3/25 10:17
 */
public class JdbcUtil {

    private static DataSource realDataSource = null;

    public static synchronized DataSource initializeDataSource() {
        if (realDataSource != null) {
            return realDataSource;
        }
        try {
            /** 创建连接池对象*/
            realDataSource = createdDataSource();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return realDataSource;
    }

    public static DataSource createdDataSource() throws Exception {
        Map map = new HashMap<>(32);
        map.put(DruidDataSourceFactory.PROP_DRIVERCLASSNAME, GlobalParam.sqlServerDriverClassName);
        map.put(DruidDataSourceFactory.PROP_URL, GlobalParam.sqlServerUrl);
        map.put(DruidDataSourceFactory.PROP_USERNAME, GlobalParam.sqlServerUsername);
        map.put(DruidDataSourceFactory.PROP_PASSWORD, GlobalParam.sqlServerPassword);
        // 初始化时建立物理连接的个数
        map.put(DruidDataSourceFactory.PROP_INITIALSIZE, "10");
        // 最小连接池数量
        map.put(DruidDataSourceFactory.PROP_MINIDLE, "10");
        // 最大连接池数量
        map.put(DruidDataSourceFactory.PROP_MAXACTIVE, "50");
        // 获取连接时最大等待时间，单位毫秒
        map.put(DruidDataSourceFactory.PROP_MAXWAIT, "60000");
        // 检测连接的间隔时间，单位毫秒
        map.put(DruidDataSourceFactory.PROP_TIMEBETWEENEVICTIONRUNSMILLIS, "60000");
        return DruidDataSourceFactory.createDataSource(map);
    }

    public static Object findDataByPropertiesFile(String sql, Class clazz, String... val) {
        DataSource dataSource = JdbcUtil.initializeDataSource();
        try (Connection connect = dataSource.getConnection();
             Statement stmt = connect.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            String[] values = val.clone();
            List<Object> vList = new ArrayList<>();
            Map<String, Method> set = Arrays.stream(clazz.newInstance().getClass().getMethods())
                    .filter(item -> item.getName().startsWith("set")).collect(Collectors.toMap(item -> item.getName().substring(3).toLowerCase(), item -> item));
            Map<String, Method> get = Arrays.stream(clazz.newInstance().getClass().getMethods())
                    .filter(item -> item.getName().startsWith("get")).collect(Collectors.toMap(item -> item.getName().substring(3).toLowerCase(), item -> item));
            while (rs.next()) {
                Object instance = clazz.newInstance();
                for (String s : values) {
                    Object realVal = rs.getObject(s);
                    Method method = set.get(s.toLowerCase());
                    try {
                        if(null != realVal){
                            Class<?> type = get.get(s.toLowerCase()).getReturnType();
                            if (type.equals(LocalDateTime.class)) {
                                LocalDateTime val1 = DateUtil.stringToLocalDateTime(String.valueOf(realVal), DateUtil.FORMAT1);
                                method.invoke(instance, val1);
                            } else {
                                method.invoke(instance, realVal);
                            }
                        }
                    } catch (Exception eee) {
                        eee.printStackTrace();
                    }
                }
                vList.add(instance);
            }
            return vList;
        } catch (Exception e) {
            MyThreadPool.EXECUTOR_SERVICE.execute(() -> WechatUtil.sendMsg(e.getMessage(), ActionCode.DATA_PULL_ERROR.getDesc()));
            System.out.println("findDataByPropertiesFile error");
            e.printStackTrace();
        }
        return null;
    }
}

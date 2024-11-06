package com.teenet.util;

import com.teenet.common.ActionCode;
import com.teenet.common.GlobalParam;
import com.teenet.threadpool.MyThreadPool;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @Description
 * @Author threedong
 * @Date: 2022/6/13 9:15
 * <p>
 * https://www.jianshu.com/p/13ab4c0c60f0
 */
public class OkHttpUtil {
    public final static Logger logger = LoggerFactory.getLogger(OkHttpUtil.class);
    /**
     * 最大连接时间
     */
    public final static int CONNECTION_TIMEOUT = 10;
    /**
     * JSON格式
     */
    public static final MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json; charset=utf-8");
    /**
     * OkHTTP线程池最大空闲线程数
     */
    public final static int MAX_IDLE_CONNECTIONS = 50;
    /**
     * OkHTTP线程池空闲线程存活时间
     */
    public final static long KEEP_ALIVE_DURATION = 30L;

    public final static String POST = "POST";
    public final static String GET = "GET";

    /**
     * client
     * 配置重试
     */
    private final static OkHttpClient HTTP_CLIENT = new OkHttpClient.Builder()
            .connectTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS)
            .connectionPool(new ConnectionPool(MAX_IDLE_CONNECTIONS, KEEP_ALIVE_DURATION, TimeUnit.MINUTES))
            .build();

    /**
     * @param method
     * @param url
     * @param headers
     * @param json
     * @return
     */
    public static String method(String method, String url, Map<String, String> headers, String json) {
        try {
            RequestBody body;
            if (null == json) {
                body = null;
            } else {
                body = RequestBody.create(json, MEDIA_TYPE_JSON);
            }
            Request.Builder builder = new Request.Builder();
            buildHeader(builder, headers);
            Request request = builder.url(url).method(method, body).build();
            Response response = HTTP_CLIENT.newCall(request).execute();
            if (response.isSuccessful() && Objects.nonNull(response.body())) {
                String result = response.body().string();
                boolean println = null != GlobalParam.println && GlobalParam.println;
                if(println){
                    logger.info("result : "+result +" , 执行" + method + "请求，url: {}, success !", url);
                }
                return result;
            }
        } catch (Exception e) {
            MyThreadPool.EXECUTOR_SERVICE.execute(() -> WechatUtil.sendMsg(url, ActionCode.URL_CONNECT_ERROR.getDesc()));
            e.printStackTrace();
            logger.error("执行" + method + "请求，url: {},失败!", url);
        }
        return null;
    }


    public static void smsSend(String url, String json) {
        try {
            RequestBody body = RequestBody.create(json, MEDIA_TYPE_JSON);;
            Request.Builder builder = new Request.Builder();
            Request request = builder.url(url).method(GET, body).build();
            HTTP_CLIENT.newCall(request).execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 设置请求头
     *
     * @param builder .
     * @param headers 请求头
     */
    private static void buildHeader(Request.Builder builder, Map<String, String> headers) {
        if (Objects.nonNull(headers) && headers.size() > 0) {
            headers.forEach((k, v) -> {
                if (Objects.nonNull(k) && Objects.nonNull(v)) {
                    builder.addHeader(k, v);
                }
            });
        }
    }
}

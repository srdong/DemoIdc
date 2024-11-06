package com.teenet.util;

import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.google.gson.Gson;
import com.teenet.common.GlobalParam;
import lombok.extern.slf4j.Slf4j;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * 通知
 *
 * @Description
 * @Author threedong
 * @Date 2022/7/26 9:14
 */
@Slf4j
public class WechatUtil {
    private static String accessToken = null;
    private static final String smsUrl = "http://utf8.api.smschinese.cn/?Uid=xxx&Key=xxx";
    private static final Boolean SEND_SMS;

    static {
        SEND_SMS = ObjectUtils.isNotNull(GlobalParam.smsMessage) && GlobalParam.smsMessage && StringUtils.isNotBlank(GlobalParam.smsUser);
    }

    public static void sendMsg(String errorDesc, String action) {
        String content = "IDC:" + GlobalParam.customerName + "\r\n" + action + "\r\n" + errorDesc;
        log.info(content);
        sendSms(content);
    }

    private static void sendSms(String content) {
        boolean toSend = ObjectUtils.isNotNull(GlobalParam.smsMessage) && GlobalParam.smsMessage && StringUtils.isNotBlank(GlobalParam.smsUser);
        if (toSend) {
            try {
                String val = URLEncoder.encode(content, "UTF-8");
                String message = smsUrl + "&smsMob=" + GlobalParam.smsUser + "&smsText=" + val;
                OkHttpUtil.smsSend(OkHttpUtil.GET, message);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

        }
    }

    public static void accessToken() {
        String tokenUrl = "https://qyapi.weixin.qq.com/cgi-bin/gettoken?corpid=ww68f2d774196b7760&corpsecret=KAV8_s1fNCzaUuqU9xa1N5r0t1v_9WQGxm6jhxoR048";
        String var = OkHttpUtil.method(OkHttpUtil.GET, tokenUrl, null, null);
        Map<String, String> map = new Gson().fromJson(var, Map.class);
        accessToken = map.get("access_token");
    }

    public static void sendWechat(String content) {
        if (null == accessToken) {
            accessToken();
        }
        Map<String, Object> params = new HashMap<>(8);
        params.put("touser", "@all");
        params.put("agentid", "1000011");
        params.put("msgtype", "text");
        Map<String, String> text = new HashMap<>(2);
        text.put("content", content);
        params.put("text", text);
        String json = new Gson().toJson(params);
        String messageUrl = "https://qyapi.weixin.qq.com/cgi-bin/message/send?access_token=";
        String string = OkHttpUtil.method(OkHttpUtil.POST, messageUrl + accessToken, null, json);
        Map<String, String> map = new Gson().fromJson(string, Map.class);
        String errorMsg = map.get("errmsg");
        boolean isOk = "ok".equalsIgnoreCase(errorMsg);
        if (!isOk) {
            log.info("weChat send error : " + string);
            boolean needToken = errorMsg.contains("expired") || errorMsg.contains("invalid");
            if (needToken) {
                //失效了重新获取在发送
                accessToken();
                OkHttpUtil.method(OkHttpUtil.POST, messageUrl + accessToken, null, json);
            }
        }
    }

}

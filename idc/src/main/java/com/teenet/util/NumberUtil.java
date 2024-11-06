package com.teenet.util;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * @Description
 * @Author threedong
 * @Date 2022/7/27 10:03
 */
public class NumberUtil {


    /**
     * 将int数值转换为占四个字节的byte数组，本方法适用于(低位在前，高位在后)的顺序。
     * 和bytesToInt()配套使用
     *
     * @param value 要转换的int值
     * @return byte数组
     */
    public static byte[] intToBytesLowAhead(int value) {
        byte[] src = new byte[4];
        src[3] = (byte) ((value >> 24) & 0xFF);
        src[2] = (byte) ((value >> 16) & 0xFF);
        src[1] = (byte) ((value >> 8) & 0xFF);
        src[0] = (byte) (value & 0xFF);
        return src;
    }

    /**
     * 将int数值转换为占四个字节的byte数组，本方法适用于(高位在前，低位在后)的顺序。
     * 和bytesToInt2（）配套使用
     */
    public static byte[] intToBytesHighAhead(int value) {
        byte[] src = new byte[4];
        src[0] = (byte) ((value >> 24) & 0xFF);
        src[1] = (byte) ((value >> 16) & 0xFF);
        src[2] = (byte) ((value >> 8) & 0xFF);
        src[3] = (byte) (value & 0xFF);
        return src;
    }


    /**
     * 将int转换成byte数组，低位在前，高位在后
     * 改变高低位顺序只需调换数组序号
     */
    public static byte[] intToBytes(int value) {
        byte[] src = new byte[2];
        src[1] = (byte) ((value >> 8) & 0xFF);
        src[0] = (byte) (value & 0xFF);
        return src;
    }


    /**
     * 字节数组转16进制
     *
     * @param bytes 需要转换的byte数组
     * @return 转换后的Hex字符串
     */
    public static String bytesToHex(byte[] bytes) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(bytes[i] & 0xFF);
            if (hex.length() < 2) {
                sb.append(0);
            }
            sb.append(hex);
        }
        return sb.toString();
    }

    public static byte[] paddingSpaces(String string, int size) {
        byte[] item = string.getBytes(StandardCharsets.UTF_8);
        byte[] result = new byte[size];
        Arrays.fill(result, (byte) 32);
        System.arraycopy(item, 0, result, 0, item.length);
        return result;
    }

    /**
     * 合并多个字节数组到一个字节数组
     *
     * @param values 动态字节数字参数
     * @return byte[] 合并后的字节数字
     */
    public static byte[] mergeBytes(byte[]... values) {
        int lengthByte = 0;
        for (byte[] value : values) {
            lengthByte += value.length;
        }
        byte[] allBytes = new byte[lengthByte];
        int countLength = 0;
        for (byte[] b : values) {
            System.arraycopy(b, 0, allBytes, countLength, b.length);
            countLength += b.length;
        }
        return allBytes;
    }


    /**
     * 返回指定位置的数组
     *
     * @param bytes
     * @param start  开始位置
     * @param length 截取长度
     * @return
     */
    public static byte[] getByte(byte[] bytes, int start, int length) {
        byte[] ruleByte = new byte[length];
        int index = 0;
        while (index < length) {
            ruleByte[index++] = bytes[start++];
        }
        return ruleByte;
    }


}

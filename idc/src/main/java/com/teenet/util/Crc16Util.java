package com.teenet.util;

/**
 * 基于Modbus CRC16的校验算法工具类
 *
 * @Description
 * @Author threedong
 * @Date 2022/7/27 9:37
 * <p>
 * https://blog.csdn.net/qq_34356024/article/details/78205530
 */
public class Crc16Util {

    /**
     *  --------------------------https://www.jianshu.com/p/924b5d9d46c2--------start--------
     */
    /**
     * 一个字节占 8位
     */
    private static final int BITS_OF_BYTE = 8;

    /**
     * 多项式
     */
    private static final int POLYNOMIAL = 0XA001;

    /**
     * CRC寄存器默认初始值
     */
    private static final int INITIAL_VALUE = 0XFFFF;

    /**
     * CRC16 编码
     *
     * @param bytes 编码内容
     * @return 编码结果
     */
    public static int crc16(byte[] bytes) {
        int res = INITIAL_VALUE;
        for (byte data : bytes) {
            // 把byte转换成int后再计算
            res = res ^ (data & 0XFF);
            for (int i = 0; i < BITS_OF_BYTE; i++) {
                res = (res & 0X0001) == 1 ? (res >> 1) ^ POLYNOMIAL : res >> 1;
            }
        }
        return res;
    }
    /**
     *  -------------------------- end --------
     */

    /**
     * 获取源数据和验证码的组合byte数组
     *
     * @param aa 字节数组
     * @return
     */
    public static byte[] getData(byte[] aa) {
        byte[] bb = getCrc16(aa);
        byte[] cc = new byte[aa.length + bb.length];
        System.arraycopy(aa, 0, cc, 0, aa.length);
        System.arraycopy(bb, 0, cc, aa.length, bb.length);
        return cc;
    }

    /**
     * 获取验证码byte数组，基于Modbus CRC16的校验算法
     */
    public static byte[] getCrc16(byte[] arrBuff) {
        int len = arrBuff.length;
        // 预置 1 个 16 位的寄存器为十六进制FFFF, 称此寄存器为 CRC寄存器。
        int crc = 0xFFFF;
        int i, j;
        for (i = 0; i < len; i++) {
            // 把第一个 8 位二进制数据 与 16 位的 CRC寄存器的低 8 位相异或, 把结果放于 CRC寄存器
            crc = ((crc & 0xFF00) | (crc & 0x00FF) ^ (arrBuff[i] & 0xFF));
            for (j = 0; j < 8; j++) {
                // 把 CRC 寄存器的内容右移一位( 朝低位)用 0 填补最高位, 并检查右移后的移出位
                if ((crc & 0x0001) > 0) {
                    // 如果移出位为 1, CRC寄存器与多项式A001进行异或
                    crc = crc >> 1;
                    crc = crc ^ 0xA001;
                } else {
                    // 如果移出位为 0,再次右移一位
                    crc = crc >> 1;
                }
            }
        }
        return NumberUtil.intToBytes(crc);
    }

}

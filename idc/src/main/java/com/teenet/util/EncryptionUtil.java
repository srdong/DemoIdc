package com.teenet.util;

import com.teenet.common.GlobalParam;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.interfaces.RSAKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description
 * @Author threedong
 * @Date 2022/6/13 9:25
 */
@Component
public class EncryptionUtil {

    /**
     * RSA最大加密明文大小
     */
    private static final String ALGORITHM_NAME = "RSA";
    private static PublicKey pubKey = null;
    private static String apiKey = null;

    /**
     * RSA加密
     *
     * @param data 待加密数据
     */
    public String rsa(String data) {
        if (null == pubKey) {
            try {
                List<String> rsa = Files.readAllLines(Paths.get(GlobalParam.rsaPem), StandardCharsets.UTF_8);
                //pem文件包含start和end字符的数据
                String publicKey = rsa.stream().filter(item -> !item.startsWith("--")).collect(Collectors.joining());
                byte[] decoded = Base64.decodeBase64(publicKey.getBytes());
                pubKey = KeyFactory.getInstance(ALGORITHM_NAME).generatePublic(new X509EncodedKeySpec(decoded));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM_NAME);
            cipher.init(Cipher.ENCRYPT_MODE, pubKey);

            byte[] allBytes = data.getBytes(StandardCharsets.UTF_8);
            int len = allBytes.length;
            int size = ((RSAKey) pubKey).getModulus().bitLength() / 8 - 11;

            int start = 0;
            int end = Math.min(len, start + size);

            while (end <= len) {
                byte[] part = Arrays.copyOfRange(allBytes, start, end);
                byte[] encryptedData = cipher.doFinal(part);
                stream.write(encryptedData);

                if (end == len) {
                    break;
                }
                start += size;
                end += size;
                if (end > len) {
                    end = len;
                }
            }
            byte[] encryptedBytes = stream.toByteArray();
            return Base64.encodeBase64String(encryptedBytes);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                stream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public String signMd5(String sec, String idcId) {
        if (null == apiKey) {
            try {
                List<String> api = Files.readAllLines(Paths.get(GlobalParam.apiPem), StandardCharsets.UTF_8);
                apiKey = String.join("", api);
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
        String content = sec + idcId + apiKey;
        return DigestUtils.md5Hex(content.getBytes(StandardCharsets.UTF_8));
    }


}

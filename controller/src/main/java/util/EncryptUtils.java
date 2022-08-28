package util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class EncryptUtils {
    /**
     * 进行MD5加密
     *
     * @param info 要加密的信息
     * @return String 加密后的字符串
     */
    public static String encryptToMD5(String inStr) {
        byte[] hash;
        try {
            hash = MessageDigest.getInstance("MD5").digest(inStr.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException var7) {
            throw new RuntimeException("MD5 should be supported?", var7);
        } catch (Exception var8) {
            throw new RuntimeException("UTF-8 should be supported?", var8);
        }

        StringBuilder hex = new StringBuilder(hash.length * 2);
        byte[] var6 = hash;
        int var5 = hash.length;

        for (int var4 = 0; var4 < var5; ++var4) {
            byte b = var6[var4];
            if ((b & 255) < 16) {
                hex.append("0");
            }
            hex.append(Integer.toHexString(b & 255));
        }
        return hex.toString();
    }

}

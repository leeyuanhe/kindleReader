package kindle.utils;

import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.tomcat.util.codec.binary.Base64;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Random;

public class PasswordUtils {

    /**
     * @description md5加密
     * @author hely
     * @date 2017-08-24
     * @param
     */
    public static String getMD5(String str) {
        try {
            // 生成一个MD5加密计算摘要
            MessageDigest md = MessageDigest.getInstance("MD5");
            // 计算md5函数
            md.update(str.getBytes());
            // digest()最后确定返回md5 hash值，返回值为8为字符串。因为md5 hash值是16位的hex值，实际上就是8位的字符
            // BigInteger函数则将8位的字符串转换成16位hex值，用字符串来表示；得到字符串形式的hash值
            return new BigInteger(1, md.digest()).toString(16);
        } catch (Exception e) {
            throw new RuntimeException("MD5加密出现错误");
        }
    }


    /**
     * @description 生成随机盐
     * @author hely
     * @date 2017-08-24
     * @param
     */
    public static String generateRandomSalt(){
        Random r = new SecureRandom();
        byte[] salt = new byte[32];
        r.nextBytes(salt);
        return Base64.encodeBase64String(salt);
    }

    /**
     * @description 生成密码
     * @author hely
     * @date 2017-09-13
     * @param
     */
    public static String codePassword(String username,String password,String salt){
        String algorithmName = "MD5";
        String salt1 = username;
//        String salt2 = new SecureRandomNumberGenerator().nextBytes().toHex();
        int hashIterations = 2;//加密次数
        SimpleHash hash = new SimpleHash(algorithmName, password, username + salt, hashIterations);
        return hash.toHex();
    }

}

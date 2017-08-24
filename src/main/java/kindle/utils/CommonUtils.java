package kindle.utils;

import java.util.Collection;
import java.util.Map;

/**
 * @description 常用工具类
 * @author hely
 * @date 2017-08-24
 * @param
 */
public class CommonUtils {

    /**
     * @description 判断是否为空 true-空
     * @author hely
     * @date 2017-08-24
     * @param
     */
    public static boolean isEmpty(Object pObj) {
        if (pObj == null)
            return true;
        if (pObj instanceof String) {
            if (((String) pObj).trim().length() == 0) {
                return true;
            }
        } else if (pObj instanceof Collection) {
            if (((Collection) pObj).size() == 0) {
                return true;
            }
        } else if (pObj instanceof Map) {
            if (((Map) pObj).size() == 0) {
                return true;
            }
        }
        return false;
    }

}

package i.am.lucky.utils.filter;

/**
 * @author Cazaea
 * @time 2018/3/6 16:13
 * @mail wistorm@sina.com
 */

public class TextValidateUtil {

    /**
     * 判断是否是金额(可转Bouble类型)
     *
     * @param s 类似金额的字符串
     * @return 是否能转金额类型
     */
    public static boolean isDouble(String s) {
        try {
            double amount = Double.parseDouble(s);
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}

package i.am.lucky.utils.filter;

import android.annotation.SuppressLint;

/**
 * @author Cazaea
 * @time 2017/11/7 16:14
 * @mail wistorm@sina.com
 */

public class AmountUtil {

    public static String formatString(String info) {
        String result = String.format("%.2f", Float.parseFloat(info));
        return result;
    }

    public static String formatFloat(float info) {
        String result = String.format("%.2f", info);
        return result;
    }
}

package i.am.lucky.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * @author Cazaea
 * @time 2017/11/28 10:42
 * @mail wistorm@sina.com
 */
public class FormatUtil {

    /**
     * 获取当前时间
     *
     * @return
     */
    public static String currentTime() {
        SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss", Locale.getDefault());
        Date date = new Date();
        return format.format(date);
    }

}

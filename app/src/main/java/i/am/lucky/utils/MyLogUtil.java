package i.am.lucky.utils;

import com.orhanobut.logger.Logger;

/**
 * Created by Cazaea on 2016/12/13.
 */

public class MyLogUtil {

    // 是否需要打印bug
    public static boolean isDebug = true;

    /*MyLogUtil.d("hello");
    MyLogUtil.e("hello");
    MyLogUtil.w("hello");
    MyLogUtil.v("hello");
    MyLogUtil.wtf("hello");
    MyLogUtil.json(JSON_CONTENT);
    MyLogUtil.xml(XML_CONTENT);
    MyLogUtil.log(DEBUG, "tag", "message", throwable);*/

    public static void d(String msg) {
        if (isDebug)
            Logger.d(msg);
    }

    public static void e(String msg) {
        if (isDebug)
            Logger.e(msg);
    }

    public static void w(String msg) {
        if (isDebug)
            Logger.w(msg);
    }

    public static void v(String msg) {
        if (isDebug)
            Logger.v(msg);
    }


    //红色突出显示
    public static void wtf(String msg) {
        if (isDebug)
            Logger.wtf(msg);
    }

    public static void json(String msg) {
        if (isDebug)
            Logger.json(msg);
    }

    public static void xml(String msg) {
        if (isDebug)
            Logger.xml(msg);
    }
}

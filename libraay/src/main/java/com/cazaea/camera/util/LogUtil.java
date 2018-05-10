package com.cazaea.camera.util;

import android.util.Log;

import static com.cazaea.camera.BuildConfig.DEBUG;

/**
 * =====================================
 * 作者: Cazaea
 * 日期: 2017/6/6
 * 邮箱: wistorm@sina.com
 * 描述:
 * =====================================
 */
public class LogUtil {

    private static final String DEFAULT_TAG = "Cazaea";

    private static void i(String tag, String msg) {
        if (DEBUG)
            Log.i(tag, msg);
    }

    private static void v(String tag, String msg) {
        if (DEBUG)
            Log.v(tag, msg);
    }

    private static void d(String tag, String msg) {
        if (DEBUG)
            Log.d(tag, msg);
    }

    private static void e(String tag, String msg) {
        if (DEBUG)
            Log.e(tag, msg);
    }

    public static void i(String msg) {
        i(DEFAULT_TAG, msg);
    }

    public static void v(String msg) {
        v(DEFAULT_TAG, msg);
    }

    public static void d(String msg) {
        d(DEFAULT_TAG, msg);
    }

    public static void e(String msg) {
        e(DEFAULT_TAG, msg);
    }
}

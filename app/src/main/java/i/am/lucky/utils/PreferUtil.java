package i.am.lucky.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import i.am.lucky.app.MainApp;

/**
 * @author Cazaea
 * @time 2017/5/9 8:45
 * @mail wistorm@sina.com
 */

public class PreferUtil {

    private static SharedPreferences pref;
    private static SharedPreferences.Editor editor;

    // SharedPreferences 文件名
    private static final String PREF_NAME = "intro_slider";

    private static final String IS_FIRST_LAUNCH = "IsFirstTimeLaunch";

    public PreferUtil(Context context) {
        pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = pref.edit();
    }

    /**
     * 设置已启动
     *
     * @param isFirstTime
     */
    public void setFirstLaunch(boolean isFirstTime) {
        editor.putBoolean(IS_FIRST_LAUNCH, isFirstTime);
        editor.commit();
    }

    /**
     * @return 是否第一次启动
     */
    public boolean isFirstLaunch() {
        return pref.getBoolean(IS_FIRST_LAUNCH, true);
    }

    // 以下方法为老方法

    /**
     * 向sharePreferences文件中放入String类型的数据
     *
     * @param key   数据的key值
     * @param value 数据
     */
    public static void putString(String key, String value) {
        editor.putString(key, value).commit();
    }

    /**
     * 获取String消息内容
     *
     * @return key所对应value值
     */
    public static String getString(String key) {
        return pref.getString(key, null);
    }

    /**
     * 向sharePreferences文件中放入int类型的数据
     *
     * @param key   数据的key值
     * @param value 数据
     */
    public static void putInt(String key, int value) {
        editor.putInt(key, value).commit();
    }

    /**
     * 获取int类型消息内容
     *
     * @return key所对应value值
     */
    public static int getInt(String key) {
        return pref.getInt(key, -1);
    }

    /**
     * 向sharePreferences文件中放入boolean类型的数据
     *
     * @param key   数据的key值
     * @param value 数据
     */
    public static void putBoolean(String key, boolean value) {
        editor.putBoolean(key, value).commit();
    }

    /**
     * 获取boolean类型消息内容
     *
     * @return key所对应value值
     */
    public static boolean getBoolean(String key) {
        return pref.getBoolean(key, false);
    }

    /**
     * 获取存入的versionCode
     *
     * @return
     */
    public int getSavedVersionCode() {
        return getInt("least_version_code");
    }

    /**
     * 存入最新的versionCode
     */
    public void putLeastVersionCode() {
        putInt("least_version_code", MainApp.versionCode());
    }

}

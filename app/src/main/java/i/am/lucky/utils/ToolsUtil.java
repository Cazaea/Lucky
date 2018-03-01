package i.am.lucky.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedHashMap;

import i.am.lucky.data.User;
import xiaofei.library.datastorage.DataStorageFactory;
import xiaofei.library.datastorage.IDataStorage;

/**
 * Created by Cazaea on 2017/7/28.
 */

public class ToolsUtil {

    /**
     * 获取APP包名
     *
     * @param context
     * @return 包名
     */
    public static String getAppId(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            String version = info.packageName;
            return version;
        } catch (Exception e) {
            return "Cazaea";
        }
    }

    /**
     * 获取APP版本名
     *
     * @param context
     * @return
     */
    public static String getVersionName(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            String version = info.versionName;
            return version;
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 获取APP版本号
     *
     * @param context
     * @return
     */
    public static String getVersionCode(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            String version = String.valueOf(info.versionCode);
            return version;
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 为链接添加user_id和token
     *
     * @param applicationContext
     * @param oldUrl
     * @return
     */
    public static String urlAddUserInfo(Context applicationContext, String oldUrl) {
        String currentUrl = oldUrl;
        String routerPart = UrlAnalysis.UrlPage(currentUrl);//url中的链接部分
        LinkedHashMap<String, String> valuePart = UrlAnalysis.URLRequest(currentUrl);//url中的参数部分
        IDataStorage dataStorage = DataStorageFactory.getInstance(
                applicationContext, DataStorageFactory.TYPE_DATABASE);
        User user = dataStorage.load(User.class, "User");
        try {
            JSONObject jo = new JSONObject(user.userInfo);
            valuePart.put("user_id", jo.getString("id"));
            valuePart.put("token", jo.getString("token"));
        } catch (JSONException e) {
            valuePart.put("user_id", "-1");
            valuePart.put("token", "-1");
        }
        String reloadUrl = UrlAnalysis.UrlMosaic(routerPart, valuePart);
        return reloadUrl;
    }

    /**
     * 为链接添加user_id,token和另一参数
     *
     * @param applicationContext
     * @param oldUrl
     * @return
     */
    public static String urlAddInfo(Context applicationContext, String oldUrl) {
        String currentUrl = oldUrl;
        String routerPart = UrlAnalysis.UrlPage(currentUrl);//url中的链接部分
        LinkedHashMap<String, String> valuePart = UrlAnalysis.URLRequest(currentUrl);//url中的参数部分
        IDataStorage dataStorage = DataStorageFactory.getInstance(
                applicationContext, DataStorageFactory.TYPE_DATABASE);
        User user = dataStorage.load(User.class, "User");
        try {
            JSONObject jo = new JSONObject(user.userInfo);
            valuePart.put("user_id", jo.getString("id"));
            valuePart.put("token", jo.getString("token"));
            valuePart.put("column_id", "-1");
        } catch (JSONException e) {
            valuePart.put("user_id", "-1");
            valuePart.put("token", "-1");
            valuePart.put("column_id", "-1");
        }
        String reloadUrl = UrlAnalysis.UrlMosaic(routerPart, valuePart);
        return reloadUrl;
    }

}

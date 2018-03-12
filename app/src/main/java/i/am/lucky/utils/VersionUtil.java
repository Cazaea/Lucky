package i.am.lucky.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

/**
 * Created by Cazaea 2016/12/6.
 */

public class VersionUtil {

    /**
     * 获取应用当前版本号
     *
     * @param context
     * @return
     */
    public static int getApkVersionCode(Context context) {
        int versionCode = -1;
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            versionCode = info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }

}

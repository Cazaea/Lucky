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
    public static int getApkVersion(Context context) {
        int v = -1;
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            v = info.versionCode;
        } catch (NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return v;
    }

}

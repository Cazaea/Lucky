package i.am.lucky.utils;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;

import java.io.File;

import i.am.lucky.BuildConfig;

/**
 * @author Cazaea
 * // * @time 2018/3/30 17:13
 * @mail wistorm@sina.com
 */
public class InstallUtil extends AppCompatActivity {

    private static final int INSTALL_PACKAGES_REQUEST_CODE = 100;

    private Context context;

    /**
     * 初始化一些必要的参数
     */
    public void getInstance(){
        this.context = context;
    }

    /**
     * 判断是否是8.0系统,是的话需要获取此权限,判断开没开,没开的话处理未知应用来源权限问题,否则直接安装
     */
    private void checkIsAndroidOrea(Context context, String apkPath) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            boolean b = getPackageManager().canRequestPackageInstalls();
            if (b) {
                // 安装应用的逻辑(写自己的就可以)
                installApk(context, apkPath);
            } else {
                // 请求安装未知应用来源的权限
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.REQUEST_INSTALL_PACKAGES}, INSTALL_PACKAGES_REQUEST_CODE);
            }
        } else {
            installApk(context, apkPath);
        }

    }

    /**
     * 安装APK
     *
     * @param context
     * @param apkPath
     */
    public static void installApk(Context context, String apkPath) {



        if (context == null || TextUtils.isEmpty(apkPath)) {
            return;
        }
        File file = new File(apkPath);
        Intent intent = new Intent(Intent.ACTION_VIEW);

        // 判读版本是否在7.0以上
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            // 当前的内容提供者
            Uri apkUri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID.concat(".fileProvider"), file);
            // 授予临时权限
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
        } else {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        }
        context.startActivity(intent);
    }

}

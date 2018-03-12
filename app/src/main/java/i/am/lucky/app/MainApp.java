package i.am.lucky.app;

import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.ImageView;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheEntity;
import com.lzy.okgo.cache.CacheMode;
import com.mikepenz.materialdrawer.util.AbstractDrawerImageLoader;
import com.mikepenz.materialdrawer.util.DrawerImageLoader;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.squareup.picasso.Picasso;
import com.tencent.android.tpush.XGIOperateCallback;
import com.tencent.android.tpush.XGPushConfig;
import com.tencent.android.tpush.XGPushManager;
import com.thejoyrun.router.Router;

import org.greenrobot.eventbus.EventBus;

import java.util.logging.Level;

import i.am.lucky.config.AppConfig;
import i.am.lucky.utils.PreferUtil;
import xiaofei.library.datastorage.DataStorageFactory;
import xiaofei.library.datastorage.IDataStorage;

/*
 * @author Cazaea
 * @time 2017/11/14 14:41
 * @mail wistorm@sina.com
 *
 *                        ___====-_  _-====___
 *                  _--^^^#####//      \\#####^^^--_
 *               _-^##########// (    ) \\##########^-_
 *              -############//  |\^^/|  \\############-
 *            _/############//   (@::@)   \\############\_
 *           /#############((     \\//     ))#############\
 *          -###############\\    (oo)    //###############-
 *         -#################\\  / VV \  //#################-
 *        -###################\\/      \//###################-
 *       _#/|##########/\######(   /\   )######/\##########|\#_
 *       |/ |#/\#/\#/\/  \#/\##\  |  |  /##/\#/  \/\#/\#/\#| \|
 *       `  |/  V  V  `   V  \#\| |  | |/#/  V   '  V  V  \|  '
 *          `   `  `      `   / | |  | | \   '      '  '   '
 *                           (  | |  | |  )
 *                          __\ | |  | | /__
 *                         (vvv(VVV)(VVV)vvv)
 *
 *                           HERE BE DRAGONS
 *
 */

public class MainApp extends Application {

    private static MainApp app;

    private static PreferUtil mPreferUtil;
    private static IDataStorage mData;

    @Override
    public void onCreate() {
        super.onCreate();
        // 给app赋值
        app = this;
        // 注册信鸽推送
        registerXGPush();
        // 注册路由
        initRouter();
        // 初始化Google抽屉
        initDrawer();
        // 初始化OkGo
        okGoInit();
        // 初始化Logger打印
        initLogger();

    }

    /**
     * 静态MainApp对象
     */
    public static MainApp getApp() {
        return app;
    }

    /**
     * 初始化AndroidDataStorage
     */
    public static IDataStorage getData() {
        if (mData == null)
            mData = DataStorageFactory.getInstance(getApp(), DataStorageFactory.TYPE_DATABASE);
        return mData;
    }

    /**
     * 获取SharedPreferences
     */
    public static PreferUtil getPreferUtil() {
        if (mPreferUtil == null)
            mPreferUtil = new PreferUtil(getApp());
        return mPreferUtil;
    }

    /**
     * 获取应用当前版本号
     */
    public static int versionCode() {
        int versionCode;
        try {
            PackageManager manager = getApp().getPackageManager();
            PackageInfo info = manager.getPackageInfo(getApp().getPackageName(), 0);
            versionCode = info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            versionCode = -1;
            e.printStackTrace();
        }
        return versionCode;
    }

    /**
     * 初始化Logger打印
     */
    private void initLogger() {
        Logger.t("Cazaea");
        Logger.addLogAdapter(new AndroidLogAdapter());
    }

    /**
     * 初始化OkGo
     */
    private void okGoInit() {
        OkGo.init(this);
        try {
            //以下都不是必须的，根据需要自行选择,一般来说只需要 debug,缓存相关,cookie相关的 就可以了
            OkGo.getInstance()
                    // 打开该调试开关,打印级别INFO,并不是异常,是为了显眼,不需要就不要加入该行
                    // 最后的true表示是否打印okgo的内部异常，一般打开方便调试错误
                    .debug("OkGo", Level.INFO, true)
                    //如果使用默认的 60秒,以下三行也不需要传
                    .setConnectTimeout(OkGo.DEFAULT_MILLISECONDS)  //全局的连接超时时间
                    .setReadTimeOut(OkGo.DEFAULT_MILLISECONDS)     //全局的读取超时时间
                    .setWriteTimeOut(OkGo.DEFAULT_MILLISECONDS)    //全局的写入超时时间
                    //可以全局统一设置缓存模式,默认是不使用缓存,可以不传,具体其他模式看 github 介绍 https://github.com/jeasonlzy/
                    .setCacheMode(CacheMode.REQUEST_FAILED_READ_CACHE)

                    //可以全局统一设置缓存时间,默认永不过期,具体使用方法看 github 介绍
                    .setCacheTime(CacheEntity.CACHE_NEVER_EXPIRE)

                    //可以全局统一设置超时重连次数,默认为三次,那么最差的情况会请求4次(一次原始请求,三次重连请求),不需要可以设置为0
                    .setRetryCount(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 初始化Google抽屉菜单
     */
    private void initDrawer() {
        // 初始化和创建图像加载程序逻辑
        DrawerImageLoader.init(new AbstractDrawerImageLoader() {
            @Override
            public void set(ImageView imageView, Uri uri, Drawable placeholder) {
                Picasso.with(imageView.getContext()).load(uri).placeholder(placeholder).into(imageView);
            }

            @Override
            public void cancel(ImageView imageView) {
                Picasso.with(imageView.getContext()).cancelRequest(imageView);
            }

            /*
            @Override
            public Drawable placeholder(Context ctx) {
                return super.placeholder(ctx);
            }

            @Override
            public Drawable placeholder(Context ctx, String tag) {
                return super.placeholder(ctx, tag);
            }
            */
        });
    }

    /**
     * 初始化路由框架
     */
    private void initRouter() {
        Router.init(AppConfig.ROUTER_HEAD);
        Router.setHttpHost(AppConfig.ROUTER_WEBSITE);
    }

    /**
     * 注册信鸽推送
     */
    private void registerXGPush() {

        // 开启logcat输出，方便debug，发布时请关闭
        XGPushConfig.enableDebug(this, true);
        // 输出Device_Token
        Log.d("TPush", XGPushConfig.getToken(this));
        // 注册是否成功
        XGPushManager.registerPush(this, XGPushConfig.getToken(this), new XGIOperateCallback() {
            @Override
            public void onSuccess(Object data, int i) {
                Log.d("TPush", "注册成功，设备token为：" + data);
            }

            @Override
            public void onFail(Object data, int errCode, String msg) {
                Log.d("TPush", "注册失败，错误码：" + errCode + ",错误信息：" + msg);
            }
        });

    }

    /**
     * 注册极光推送
     */
    private void registerTPush() {

    }

}

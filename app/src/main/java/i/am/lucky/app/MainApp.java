package i.am.lucky.app;

import android.app.Application;
import android.util.DisplayMetrics;

import com.facebook.drawee.backends.pipeline.Fresco;
import i.am.lucky.config.AppConfig;
import i.am.lucky.data.User;
import i.am.lucky.utils.LogUtil;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.tencent.android.tpush.XGIOperateCallback;
import com.tencent.android.tpush.XGPushConfig;
import com.tencent.android.tpush.XGPushManager;
import com.tencent.smtt.sdk.QbSdk;
import com.thejoyrun.router.Router;
import com.uuch.adlibrary.utils.DisplayUtil;

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

    @Override
    public void onCreate() {
        super.onCreate();
        // 给app赋值
        app = this;
        // 初始化Logger打印
        initLogger();
        // 注册路由工具
        initRouter();
        // 初始化广告弹框功能
        initAdDialog();
        // 注册信鸽推送
        registerXGPush();
        // 初始化x5内核,X5的预加载
        initTBSX5();
        // 初始化User数据
        initUser();

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
        return DataStorageFactory.getInstance(getApp(), DataStorageFactory.TYPE_DATABASE);
    }

    /**
     * 初始化广告弹框
     */
    private void initAdDialog() {
        initDisplayOpinion();
        Fresco.initialize(getApplicationContext());
    }

    private void initDisplayOpinion() {
        DisplayMetrics dm = getResources().getDisplayMetrics();
        DisplayUtil.density = dm.density;
        DisplayUtil.densityDPI = dm.densityDpi;
        DisplayUtil.screenWidthPx = dm.widthPixels;
        DisplayUtil.screenhightPx = dm.heightPixels;
        DisplayUtil.screenWidthDip = DisplayUtil.px2dip(getApplicationContext(), dm.widthPixels);
        DisplayUtil.screenHightDip = DisplayUtil.px2dip(getApplicationContext(), dm.heightPixels);
    }

    /**
     * 初始化Logger打印
     */
    private void initLogger() {
        Logger.t(AppConfig.ROUTER_HEAD);
        Logger.addLogAdapter(new AndroidLogAdapter());
    }

    /**
     * 注册信鸽推送
     */
    private void registerXGPush() {

        // 开启logcat输出，方便debug，发布时请关闭
        XGPushConfig.enableDebug(this, AppConfig.DEBUG_MODE);
        // 输出Device_Token
        LogUtil.d("设备Device_Token:-->" + XGPushConfig.getToken(this));
        // 注册是否成功
        XGPushManager.registerPush(this, XGPushConfig.getToken(this), new XGIOperateCallback() {
            @Override
            public void onSuccess(Object data, int i) {
                LogUtil.d("TPush注册成功，设备token为：-->" + data);
            }

            @Override
            public void onFail(Object data, int errCode, String msg) {
                LogUtil.d("TPush注册失败，错误码：-->" + errCode + ",错误信息：-->" + msg);
            }
        });

//        XGPushManager.registerPush(this, "*");

    }

    /**
     * 初始化路由框架
     */
    private void initRouter() {
        Router.init(AppConfig.ROUTER_HEAD);
        Router.setHttpHost(AppConfig.ROUTER_WEBSITE);
    }

    /**
     * 初始化TBS浏览服务X5内核
     */
    private void initTBSX5() {
        // 非wifi条件下允许下载X5内核
        QbSdk.setDownloadWithoutWifi(true);
        // 搜集本地tbs内核信息并上报服务器，服务器返回结果决定是用哪个内核。
        QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {
            @Override
            public void onViewInitFinished(boolean status) {
                // x5内核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败会自动切换到系统内核。
                LogUtil.d("TBS浏览服务X5内核是否加载成功-->" + status);
            }

            @Override
            public void onCoreInitFinished() {
                // TODO Auto-generated method stub
            }
        };
        // X5内核初始化接口
        QbSdk.initX5Environment(getApp(), cb);
    }

    /**
     * 给User传入 默认数据
     */
    private void initUser() {
        IDataStorage storage = MainApp.getData();
        User user = storage.load(User.class, "User");
        // 如果没有存入数据，先初始化User数据为未登录状态
        if (user == null) {
            user = new User();
            user.userInfo = User.defaultInfo;
            user.hasLogin = false;
            user.fromAccount = true;
            storage.storeOrUpdate(user, "User");
        }
    }

}

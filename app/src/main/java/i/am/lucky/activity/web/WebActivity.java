//package i.am.lucky.activity.web;
//
//import android.Manifest;
//import android.app.Activity;
//import android.app.AlertDialog;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.content.pm.PackageManager;
//import android.graphics.PixelFormat;
//import android.net.Uri;
//import android.os.Build;
//import android.os.Bundle;
//import android.os.Environment;
//import android.os.Handler;
//import android.os.Message;
//import android.provider.MediaStore;
//import android.support.annotation.NonNull;
//import android.support.v4.app.ActivityCompat;
//import android.support.v4.content.ContextCompat;
//import android.support.v4.content.FileProvider;
//import android.support.v7.app.AppCompatActivity;
//import android.util.Log;
//import android.view.KeyEvent;
//import android.view.MotionEvent;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.WindowManager;
//import android.webkit.JavascriptInterface;
//import android.widget.FrameLayout;
//import android.widget.ImageView;
//import android.widget.TextView;
//import android.widget.Toast;
//
////import com.hxd.yqczb.utils.CustomDialog;
////import com.hxd.yqczb.utils.ToolsUtil;
////import com.hxd.yqczb.utils.UrlAnalysis;
////import com.hxd.yqczb.utils.configUtils.EventConfig;
////import com.hxd.yqczb.utils.configUtils.UrlConfig;
////import com.hxd.yqczb.utils.reWeb.ReWebChromeClient;
////import com.hxd.yqczb.utils.reWeb.ReWebViewClient;
//import com.tencent.smtt.export.external.interfaces.IX5WebChromeClient;
//import com.tencent.smtt.sdk.CacheManager;
//import com.tencent.smtt.sdk.ValueCallback;
//import com.tencent.smtt.sdk.WebChromeClient;
//import com.tencent.smtt.sdk.WebSettings;
//import com.tencent.smtt.sdk.WebView;
//import com.thejoyrun.router.Router;
//import com.thejoyrun.router.RouterActivity;
//import com.thejoyrun.router.RouterField;
//import com.umeng.socialize.ShareAction;
//import com.umeng.socialize.UMShareAPI;
//import com.umeng.socialize.UMShareListener;
//import com.umeng.socialize.bean.SHARE_MEDIA;
//import com.umeng.socialize.media.UMImage;
//import com.umeng.socialize.media.UMWeb;
//
//import org.greenrobot.eventbus.EventBus;
//import org.greenrobot.eventbus.Subscribe;
//import org.greenrobot.eventbus.ThreadMode;
//
//import java.io.File;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//
//import butterknife.BindView;
//import butterknife.ButterKnife;
//import butterknife.OnClick;
//import i.am.lucky.R;
//import i.am.lucky.config.EventConfig;
//import i.am.lucky.config.UrlConfig;
//import i.am.lucky.data.ShareInfo;
//import i.am.lucky.utils.ToolsUtil;
//import i.am.lucky.utils.UrlAnalysis;
//import i.am.lucky.view.LoadingDialog;
//
//@RouterActivity("web")
//public class WebActivity extends AppCompatActivity implements ReWebChromeClient.OpenFileChooserCallBack {
//
//    @BindView(R.id.web_wv)
//    WebView webView;//webview
//    @BindView(R.id.web_tv_title)
//    TextView tv_title;//标题
//    @BindView(R.id.web_iv_share)
//    ImageView iv_share;
//    @BindView(R.id.web_tv_ctrl)
//    TextView tv_ctrl;
//
//    @RouterField("url")
//    String url;//第一次路由传入的链接
//
//
//    private String should_close_after_login_fail; //标记位 登录失败后是否需要关闭本页面
//    private LoadingDialog customDialog; //加载loading框
//    private UMShareListener umShareListener; //友盟社会化分享
//
//    // 用于 WebView 中选择图片
//    private static final int REQUEST_CODE_PICK_IMAGE = 0;
//    private static final int REQUEST_CODE_IMAGE_CAPTURE = 1;
//    private ValueCallback<Uri> mUploadMsg;
//    public ValueCallback<Uri[]> mUploadMessageForAndroid5;
//
//    private OptBridge optBridge;
//
//    private ShareInfo shareInfo;
//    private WebBtn webBtn;
//
//    /** 视频全屏参数 */
//    protected static final FrameLayout.LayoutParams COVER_SCREEN_PARAMS = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//    private View customView;
//    private FrameLayout fullscreenContainer;
//    private IX5WebChromeClient.CustomViewCallback customViewCallback;
//
//    /**
//     * 用于接收EventBus传递的事件总线，登录成功后reload所有web重置用户数据
//     * 用法：在需要刷新web的地方调用  EventBus.getDefault().post(EventConfig.EVENT_RELOADWEB);
//     *
//     * @param message EventConfig中的选项
//     */
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void reloadWeb(String message) {
//        if (message.equals(EventConfig.EVENT_EXIT_WITHOUT_LOGIN)) {
//            if (should_close_after_login_fail.equals("1")) {
//                WebActivity.this.finish();
//            }
//        }
//        if (message.equals(EventConfig.EVENT_RELOAD_WEB)) {
//            String currentUrl = webView.getUrl();
//            String reloadUrl = ToolsUtil.urlAddUserInfo(getApplicationContext(),currentUrl);
//            webView.loadUrl(reloadUrl);
//        }
//    }
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_web);
//
//        permissionRequest(); // 权限申请
//
//        Router.inject(this);
//        ButterKnife.bind(this);
//        EventBus.getDefault().register(this);
//
//        iv_share.setVisibility(View.GONE);
//
//        getWindow().setFormat(PixelFormat.TRANSLUCENT);//（这个对宿主没什么影响，建议声明）
//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
//
//        initWebView();
//
//        umShareListener = new UMShareListener() {
//            @Override
//            public void onStart(SHARE_MEDIA share_media) {
//            }
//
//            @Override
//            public void onResult(SHARE_MEDIA share_media) {
//            }
//
//            @Override
//            public void onError(SHARE_MEDIA share_media, Throwable throwable) {
//            }
//
//            @Override
//            public void onCancel(SHARE_MEDIA share_media) {
//            }
//        };
//
//        customDialog = new LoadingDialog(this, R.style.CustomDialog);
//
//        should_close_after_login_fail = "0";
//
//        loadUrl(ToolsUtil.urlAddUserInfo(getApplicationContext(),url));
//        //loadUrl(url);
//    }
//
//    /**
//     * 权限申请
//     */
//    private void permissionRequest() {
//        if (Build.VERSION.SDK_INT >= 23) {
//            List<String> permissionsList = new ArrayList<>();
//            if (ContextCompat.checkSelfPermission(WebActivity.this,
//                    Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                    != PackageManager.PERMISSION_GRANTED) {
//                permissionsList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
//            }
//            if (ContextCompat.checkSelfPermission(WebActivity.this,
//                    Manifest.permission.ACCESS_FINE_LOCATION)
//                    != PackageManager.PERMISSION_GRANTED) {
//                permissionsList.add(Manifest.permission.ACCESS_FINE_LOCATION);
//            }
//            if (ContextCompat.checkSelfPermission(WebActivity.this,
//                    Manifest.permission.CALL_PHONE)
//                    != PackageManager.PERMISSION_GRANTED) {
//                permissionsList.add(Manifest.permission.CALL_PHONE);
//            }
//            if (ContextCompat.checkSelfPermission(WebActivity.this,
//                    Manifest.permission.READ_LOGS)
//                    != PackageManager.PERMISSION_GRANTED) {
//                permissionsList.add(Manifest.permission.READ_LOGS);
//            }
//            if (ContextCompat.checkSelfPermission(WebActivity.this,
//                    Manifest.permission.READ_PHONE_STATE)
//                    != PackageManager.PERMISSION_GRANTED) {
//                permissionsList.add(Manifest.permission.READ_PHONE_STATE);
//            }
//            if (ContextCompat.checkSelfPermission(WebActivity.this,
//                    Manifest.permission.SET_DEBUG_APP)
//                    != PackageManager.PERMISSION_GRANTED) {
//                permissionsList.add(Manifest.permission.SET_DEBUG_APP);
//            }
//            if (ContextCompat.checkSelfPermission(WebActivity.this,
//                    Manifest.permission.SYSTEM_ALERT_WINDOW)
//                    != PackageManager.PERMISSION_GRANTED) {
//                permissionsList.add(Manifest.permission.SYSTEM_ALERT_WINDOW);
//            }
//            if (ContextCompat.checkSelfPermission(WebActivity.this,
//                    Manifest.permission.WRITE_APN_SETTINGS)
//                    != PackageManager.PERMISSION_GRANTED) {
//                permissionsList.add(Manifest.permission.WRITE_APN_SETTINGS);
//            }
//            ActivityCompat.requestPermissions(this,
//                    permissionsList.toArray(new String[permissionsList.size()]), 123);
//        }
//    }
//
//
//    /**
//     * 初始化web内核设置
//     */
//    private void initWebView() {
//        WebSettings webSettings = webView.getSettings();
//        webSettings.setJavaScriptEnabled(true);
//        webSettings.setMixedContentMode(android.webkit.WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
//        webSettings.setUseWideViewPort(true);
//        webSettings.setAllowFileAccess(true);
//        webSettings.setSupportZoom(true);
//        webSettings.setLoadWithOverviewMode(true);
//        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
//        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
//        optBridge = new OptBridge();
//        webView.addJavascriptInterface(optBridge, "OptBridge");
//        webView.setWebViewClient(new ReWebViewClient() {
//
//            @Override
//            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                String schemePart = url.substring(0, url.indexOf(":"));//截取url中的scheme部分 如"http","https","nbw"
//                String routerPart = UrlAnalysis.UrlPage(url);//url中的链接部分 如"http://www.rubulls.com"
//                Map<String, String> valuePart = UrlAnalysis.URLRequest(url);//url中的参数部分
//                if (schemePart.equals(UrlConfig.routerHead)) {
//                    //登录 type1 转到登录页 登录完成后更新所有web用户信息
//                    if (routerPart.contains(UrlConfig.routerHead+"://login")) {
//                        should_close_after_login_fail = valuePart.get("close");
//                        Router.startActivity(WebActivity.this, url);
//                    }else{
//                        Router.startActivity(WebActivity.this, url);
//                    }
//
//                    return true;
//                }
//                if(valuePart.get("_target") != null && valuePart.get("_target").equals("self")){
//                    url = url.replace("&", "%26");
//                    Router.startActivity(WebActivity.this, UrlConfig.routerHead+"://web?url=" + url);
//                    WebActivity.this.finish();
//                    return true;
//                }
//
//                WebView.HitTestResult hit = webView.getHitTestResult();
//                int hitType = hit.getType();
//                if (hitType != WebView.HitTestResult.UNKNOWN_TYPE) {
//                    // 这里执行自定义的操作
//                    url = url.replace("&", "%26");
//                    Router.startActivity(WebActivity.this, UrlConfig.routerHead+"://web?url=" + url);
//                    return true;
//                } else {
//                    // 重定向时hitType为0 ,执行默认的操作
//                    return false;
//                }
//            }
//
//            @Override
//            public void onReceivedError(WebView var1, int var2, String var3, String var4) {
//                Toast.makeText(WebActivity.this, "页面加载失败", Toast.LENGTH_SHORT).show();
//                customDialog.dismiss();
//                Log.e("打印日志", "网页加载失败");
//            }
//        });
//
//        // 进度条
//        webView.setWebChromeClient(new ReWebChromeClient(this) {
//            @Override
//            public void onReceivedTitle(WebView view, String title) {
//                super.onReceivedTitle(view, title);
//                tv_title.setText(title);
//            }
//
//            @Override
//            public void onProgressChanged(WebView view, int newProgress) {
//                if (newProgress == 100) {
//                    customDialog.dismiss();
//                    Log.i("打印日志", "加载完成");
//                }
//            }
//
//            /*** 视频播放相关的方法 **/
//
//            @Override
//            public View getVideoLoadingProgressView() {
//                FrameLayout frameLayout = new FrameLayout(WebActivity.this);
//                frameLayout.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
//                return frameLayout;
//            }
//
//            @Override
//            public void onShowCustomView(View view, IX5WebChromeClient.CustomViewCallback callback) {
//                showCustomView(view, callback);
//            }
//
//            @Override
//            public void onHideCustomView() {
//                hideCustomView();
//            }
//        });
//    }
//    /** 视频播放全屏 **/
//    private void showCustomView(View view, IX5WebChromeClient.CustomViewCallback callback) {
//        // if a view already exists then immediately terminate the new one
//        if (customView != null) {
//            callback.onCustomViewHidden();
//            return;
//        }
//
//        WebActivity.this.getWindow().getDecorView();
//
//        FrameLayout decor = (FrameLayout) getWindow().getDecorView();
//        fullscreenContainer = new FullscreenHolder(WebActivity.this);
//        fullscreenContainer.addView(view, COVER_SCREEN_PARAMS);
//        decor.addView(fullscreenContainer, COVER_SCREEN_PARAMS);
//        customView = view;
//        setStatusBarVisibility(false);
//        customViewCallback = callback;
//    }
//
//    /** 隐藏视频全屏 */
//    private void hideCustomView() {
//        if (customView == null) {
//            return;
//        }
//
//        setStatusBarVisibility(true);
//        FrameLayout decor = (FrameLayout) getWindow().getDecorView();
//        decor.removeView(fullscreenContainer);
//        fullscreenContainer = null;
//        customView = null;
//        customViewCallback.onCustomViewHidden();
//        webView.setVisibility(View.VISIBLE);
//    }
//
//    /** 全屏容器界面 */
//    static class FullscreenHolder extends FrameLayout {
//
//        public FullscreenHolder(Context ctx) {
//            super(ctx);
//            setBackgroundColor(ctx.getResources().getColor(android.R.color.black));
//        }
//
//        @Override
//        public boolean onTouchEvent(MotionEvent evt) {
//            return true;
//        }
//    }
//
//    private void setStatusBarVisibility(boolean visible) {
//        int flag = visible ? 0 : WindowManager.LayoutParams.FLAG_FULLSCREEN;
//        getWindow().setFlags(flag, WindowManager.LayoutParams.FLAG_FULLSCREEN);
//    }
//
//    public final class OptBridge {
//        @JavascriptInterface
//        public void OptShare(String title, String description, String link, String piclink) {
//            if (shareInfo == null)
//                shareInfo = new ShareInfo();
//            shareInfo.mtitle = title;
//            shareInfo.mdescription = description;
//            shareInfo.mlink = link;
//            shareInfo.mpic = piclink;
//
//            ShareWeb();
//            //webview中按钮点击打开分享
//            //Toast.makeText(WebActivity.this,"doShare "+title+" "+description+" "+link,Toast.LENGTH_SHORT).show();
//        }
//
//        @JavascriptInterface
//        public void OptEnableShareBtn(String title, String description, String link, String piclink) {
//            if (shareInfo == null)
//                shareInfo = new ShareInfo();
//            shareInfo.mtitle = title;
//            shareInfo.mdescription = description;
//            shareInfo.mlink = link;
//            shareInfo.mpic = piclink;
//
//            Message msg = new Message();
//            msg.what = 1000;
//            mHandler.sendMessage(msg);
//            //webview控制打开原生右上角的分享按钮
//            //Toast.makeText(WebActivity.this,"needShare "+title+" "+description+" "+link,Toast.LENGTH_SHORT).show();
//        }
//
//        @JavascriptInterface
//        public void OptEnableWebBtn(String title, String url){
//            if(webBtn==null)
//                webBtn=new WebBtn();
//            webBtn.mtitle=title;
//            webBtn.mUrl=url;
//
//            Message msg = new Message();
//            msg.what = 2000;
//            mHandler.sendMessage(msg);
//        }
//
//        @JavascriptInterface
//        public void OptClose(){
//            Message msg = new Message();
//            msg.what = 3000;
//            mHandler.sendMessage(msg);
//        }
//
//        @JavascriptInterface
//        public void OptSendNotification(String msg){
//            if(msg.equals("webReload")){
//                EventBus.getDefault().post(EventConfig.EVENT_RELOAD_WEB);
//                return;
//            }
//            if(msg.equals("userInfoRefresh")){
//                EventBus.getDefault().post(EventConfig.EVENT_MINE);
//                return;
//            }
//            if(msg.equals("planRefresh")){
//                EventBus.getDefault().post(EventConfig.EVENT_REFRESH_MY_TARGET);
//                return;
//            }
//
//        }
//    }
//    private Handler mHandler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            // 显示分享按钮
//            if (msg.what == 1000) {
//                iv_share.setVisibility(View.VISIBLE);
//            }
//            //显示文字按钮
//            if (msg.what == 2000) {
//                tv_ctrl.setText(webBtn.mtitle);
//                tv_ctrl.setVisibility(View.VISIBLE);
//            }
//            //关闭本页面
//            if(msg.what==3000){
//                if (customView != null) {
//                    hideCustomView();
//                } else if (webView != null && webView.canGoBack()) {
//                    webView.goBack();
//                } else {
//                    WebActivity.this.finish();
//                }
//            }
//        }
//    };
//
//    /**
//     * 加载链接
//     *
//     * @param url
//     */
//    private void loadUrl(String url) {
//        customDialog.show();
//        webView.loadUrl(url);
//    }
//
//    /**
//     * 点击右上角的分享
//     */
//    @OnClick(R.id.web_iv_share)
//    public void onShare() {
//        ShareWeb();
//    }
//
//    @OnClick(R.id.web_tv_ctrl)
//    public void onCtrl(){
//        Router.startActivity(WebActivity.this,UrlConfig.routerHead+"://web?url="+webBtn.mUrl.replace("&","%26"));
//    }
//
//
//    private void ShareWeb() {
//        if (shareInfo == null) {
//            return;
//        }
//        UMImage thumb = new UMImage(WebActivity.this, shareInfo.mpic);
//        UMWeb web = new UMWeb(shareInfo.mlink);//链接
//        web.setThumb(thumb);//缩略图
//        web.setDescription(shareInfo.mdescription);//描述
//        web.setTitle(shareInfo.mtitle);//标题
//        new ShareAction(WebActivity.this).withMedia(web).setDisplayList(/*SHARE_MEDIA.SINA,*/ SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE, SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE).setCallback(umShareListener).open();
//    }
//
//    @Override
//    protected void onDestroy() {
//        // clear
//        File file = CacheManager.getCacheFileBaseDir();
//        if (file != null && file.exists() && file.isDirectory()) {
//            for (File item : file.listFiles()) {
//                item.delete();
//            }
//            file.delete();
//        }
//        deleteDatabase("webview.db");
//        deleteDatabase("webviewCache.db");
//        // destroy webView
//        if (webView != null) {
//            webView.clearCache(true);
//            webView.clearFormData();
//            webView.clearHistory();
//            webView.clearMatches();
//            webView.clearSslPreferences();
//            webView.clearFocus();
//            webView.clearDisappearingChildren();
//            webView.clearAnimation();
//            webView.removeAllViews();
//            webView.destroy();
//        }
//        EventBus.getDefault().unregister(this);
//
//        super.onDestroy();
//    }
//
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            if (customView != null) {
//                hideCustomView();
//            } else if (webView != null && webView.canGoBack()) {
//                webView.goBack();
//                return true;
//            }
//            return super.onKeyDown(keyCode, event);
//        }
//        return super.onKeyDown(keyCode, event);
//    }
//
//    /**
//     * Activity 结果的回调
//     */
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//
//        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
//
//        if (resultCode != Activity.RESULT_OK) {
//
//            if (mUploadMsg != null) {
//                mUploadMsg.onReceiveValue(null);
//                mUploadMsg = null;
//            }
//
//            if (mUploadMessageForAndroid5 != null) {
//                mUploadMessageForAndroid5.onReceiveValue(null);
//                mUploadMessageForAndroid5 = null;
//            }
//            return;
//        }
//
//        File temp = new File(Environment.getExternalStorageDirectory() + "/temp.jpg");
//        if (mUploadMessageForAndroid5 != null) {
//
//            switch (requestCode) {
//
//                case REQUEST_CODE_IMAGE_CAPTURE:
//                    if (temp.exists()) {
//                        mUploadMessageForAndroid5.onReceiveValue(new Uri[]{Uri.fromFile(temp)});
//                    } else {
//                        mUploadMessageForAndroid5.onReceiveValue(new Uri[]{});
//                    }
//
//                case REQUEST_CODE_PICK_IMAGE:
//                    try {
//                        final Uri result = (data == null) ? null : data.getData();
//                        if (null != mUploadMessageForAndroid5) {
//                            if (result != null) {
//                                mUploadMessageForAndroid5.onReceiveValue(new Uri[]{result});
//                            } else {
//                                mUploadMessageForAndroid5.onReceiveValue(new Uri[]{});
//                            }
//                        }
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                    break;
//            }
//        }
//
//
//        if (mUploadMsg != null) {
//
//            switch (requestCode) {
//                case REQUEST_CODE_IMAGE_CAPTURE:
//                    if (temp.exists()) {
//                        mUploadMsg.onReceiveValue(Uri.fromFile(temp));
//                    } else {
//                        mUploadMsg.onReceiveValue(null);
//                    }
//                    Uri uri = Uri.fromFile(temp);
//                    mUploadMsg.onReceiveValue(uri);
//
//                case REQUEST_CODE_PICK_IMAGE:
//
//                    final Uri result = (data == null) ? null : data.getData();
//                    try {
//                        mUploadMsg.onReceiveValue(result);
//                        // PicAsyncTask(result);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                    break;
//            }
//        }
//    }
//
//    /**
//     * 权限申请的回调
//     */
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        switch (requestCode) {
//            case 300:
//                /*
//                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                intent.putExtra(MediaStore.EXTRA_OUTPUT,
//                        Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "temp.jpg")));
//                startActivityForResult(intent, REQUEST_CODE_IMAGE_CAPTURE);
//                */
//                imageCapture();
//                break;
//        }
//    }
//
//    /**
//     * 判断系统及拍照
//     */
//    private void imageCapture() {
//        Intent intent;
//        Uri pictureUri;
//        File pictureFile = new File(Environment.getExternalStorageDirectory(), "temp.jpg");
//        // 判断当前系统
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//            pictureUri = FileProvider.getUriForFile(this,
//                    "com.hxd.yqczb.fileProvider", pictureFile);
//        } else {
//            intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//            pictureUri = Uri.fromFile(pictureFile);
//        }
//        // 去拍照
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, pictureUri);
//        startActivityForResult(intent, REQUEST_CODE_IMAGE_CAPTURE);
//    }
//
//
//    @Override
//    public void openFileChooserCallBack(ValueCallback<Uri> uploadMsg, String acceptType) {
//        mUploadMsg = uploadMsg;
//        showOptions();
//    }
//
//    @Override
//    public void openFileChooserCallBack(ValueCallback<Uri[]> uploadMsg, WebChromeClient.FileChooserParams fileChooserParams) {
//        mUploadMessageForAndroid5 = uploadMsg;
//        showOptions();
//    }
//
//    /**
//     * 显示 "拍照/相册" 选项
//     */
//    public void showOptions() {
//
//        CharSequence[] sequences = {"相册", "拍照"};
//        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
//        alertDialog.setOnCancelListener(new ReOnCancelListener());
//        alertDialog.setTitle("选择图片");
//        alertDialog.setItems(sequences, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                if (which == 0) {
//                    Intent showImgIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                    startActivityForResult(showImgIntent, REQUEST_CODE_PICK_IMAGE);
//                } else {
//                    // 权限申请
//                    if (ContextCompat.checkSelfPermission(WebActivity.this,
//                            Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
//                            || ContextCompat.checkSelfPermission(WebActivity.this,
//                            Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                            != PackageManager.PERMISSION_GRANTED) {
//                        // 权限还没有授予，需要在这里写申请权限的代码
//                        ActivityCompat.requestPermissions(WebActivity.this,
//                                new String[]{Manifest.permission.CAMERA,
//                                        Manifest.permission.WRITE_EXTERNAL_STORAGE}, 300);
//                    } else {
//                        // 权限已经申请，直接拍照
//                        imageCapture();
//                    }
//                }
//            }
//        });
//        alertDialog.show();
//    }
//
//
//    /**
//     * 对话框取消后的回调
//     */
//    private class ReOnCancelListener implements DialogInterface.OnCancelListener {
//
//        @Override
//        public void onCancel(DialogInterface dialogInterface) {
//            if (mUploadMsg != null) {
//                mUploadMsg.onReceiveValue(null);
//                mUploadMsg = null;
//            }
//            if (mUploadMessageForAndroid5 != null) {
//                mUploadMessageForAndroid5.onReceiveValue(null);
//                mUploadMessageForAndroid5 = null;
//            }
//        }
//    }
//
//}

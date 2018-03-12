package i.am.lucky.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.thejoyrun.router.Router;
import com.thejoyrun.router.RouterActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bingoogolapple.bgabanner.BGABanner;
import cn.bingoogolapple.bgabanner.BGALocalImageSize;
import i.am.lucky.R;
import i.am.lucky.app.MainApp;
import i.am.lucky.config.AppConfig;
import i.am.lucky.utils.PreferUtil;

@RouterActivity("guide")
public class GuideActivity extends AppCompatActivity {

    @BindView(R.id.banner_guide_background)
    BGABanner bannerGuideBackground;
    @BindView(R.id.banner_guide_foreground)
    BGABanner bannerGuideForeground;
    @BindView(R.id.tv_guide_skip)
    TextView tvGuideSkip;
    @BindView(R.id.tv_guide_enter)
    TextView tvGuideEnter;

    private PreferUtil preferUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 在setContentView()前检查是否第一次运行
        preferUtil = MainApp.getPreferUtil();
        if (preferUtil.getSavedVersionCode()!=-1) {
            launchSplashScreen();
            return;
        }

        initViews();
        setListener();
        initData();
    }

    private void launchSplashScreen() {
        preferUtil.putLeastVersionCode();
        Router.startActivity(this, AppConfig.ROUTER_HEAD + "://splash");
        finish();
    }

    private void launchMainScreen() {
        preferUtil.putLeastVersionCode();
        Router.startActivity(this, AppConfig.ROUTER_HEAD + "://main");
        finish();
    }

    private void initViews() {

        setContentView(R.layout.activity_guide);
        ButterKnife.bind(this);

    }

    private void initData() {

        // Bitmap 的宽高在 maxWidth maxHeight 和 minWidth minHeight 之间
        BGALocalImageSize localImageSize = new BGALocalImageSize(720, 1280, 320, 640);
        // 前背景数据源
        bannerGuideForeground.setData(localImageSize, ImageView.ScaleType.CENTER_CROP,
                R.drawable.fore_guide1,
                R.drawable.fore_guide2,
                R.drawable.fore_guide3,
                R.drawable.fore_guide4);
        // 后背景数据源
        bannerGuideBackground.setData(localImageSize, ImageView.ScaleType.CENTER_CROP,
                R.drawable.back_guide1,
                R.drawable.back_guide2,
                R.drawable.back_guide3,
                R.drawable.back_guide4);

    }

    private void setListener() {

        bannerGuideForeground.setEnterSkipViewIdAndDelegate(R.id.tv_guide_enter, R.id.tv_guide_skip, new BGABanner.GuideDelegate() {
            @Override
            public void onClickEnterOrSkip() {
                // 设置最新版本号
                launchMainScreen();
                finish();
            }
        });

    }

}

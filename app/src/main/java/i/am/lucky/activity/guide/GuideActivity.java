package i.am.lucky.activity.guide;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import i.am.lucky.R;
import i.am.lucky.activity.MainActivity;
import i.am.lucky.utils.PreferUtil;

import cn.bingoogolapple.bgabanner.BGABanner;
import cn.bingoogolapple.bgabanner.BGALocalImageSize;

public class GuideActivity extends AppCompatActivity {

    private BGABanner bannerGuideBackground;
    private BGABanner bannerGuideForeground;
    private TextView tvGuideSkip;
    private TextView tvGuideEnter;

    private PreferUtil preferUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 在setContentView()前检查是否第一次运行
        preferUtil = PreferUtil.getInstance(this);
        if (preferUtil.getSavedVersionCode() != -1) {
            launchSplashScreen();
            return;
        }
        // 初始化控件
        initViews();
        // 引导页数据显示
        initGuideData();
        // 跳过或者立即体验
        setEnterOrSkip();
    }

    private void launchSplashScreen() {
        preferUtil.putLeastVersionCode();
        startActivity(new Intent(GuideActivity.this, SplashActivity.class));
        finish();
    }

    private void launchMainScreen() {
        preferUtil.putLeastVersionCode();
        startActivity(new Intent(GuideActivity.this, MainActivity.class));
        finish();
    }

    private void initViews() {

        setContentView(R.layout.activity_guide);

        bannerGuideBackground = findViewById(R.id.banner_guide_background);
        bannerGuideForeground = findViewById(R.id.banner_guide_foreground);
        tvGuideSkip = findViewById(R.id.tv_guide_skip);
        tvGuideEnter = findViewById(R.id.tv_guide_enter);
    }

    private void initGuideData() {
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

    private void setEnterOrSkip() {
        bannerGuideForeground.setEnterSkipViewIdAndDelegate(R.id.tv_guide_enter, R.id.tv_guide_skip, new BGABanner.GuideDelegate() {
            @Override
            public void onClickEnterOrSkip() {
                // 设置最新版本号
                launchMainScreen();
            }
        });
    }

}

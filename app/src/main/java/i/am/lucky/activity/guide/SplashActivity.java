package i.am.lucky.activity.guide;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import i.am.lucky.R;
import i.am.lucky.activity.MainActivity;
import i.am.lucky.view.SplashTimerView;

/**
 * 启动页
 *
 * @author Cazaea
 * @time 2017/5/9 9:01
 * @mail wistorm@sina.com
 */
public class SplashActivity extends AppCompatActivity {

    @BindView(R.id.iv_splash)
    ImageView ivSplash;
    @BindView(R.id.timer_view)
    SplashTimerView splashTimerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        // 延时跳转主页
        delayEnterMain();
    }

    /**
     * 延时跳转
     */
    private void delayEnterMain() {

        // 设置倒计时时间为2S
        splashTimerView.setTimeMillis(2000);
        // 设置正向进度
        splashTimerView.setProgressType(SplashTimerView.ProgressType.COUNT);
        // 监听进度条
        splashTimerView.setProgressListener(new SplashTimerView.OnProgressListener() {
            @Override
            public void onProgress(int progress) {
                // 进度走完，进入主页
                if (progress == 100) {
                    enterMain();
                }
            }
        });
        // 开始转动
        splashTimerView.start();
    }

    /**
     * 进入主界面
     */
    private void enterMain() {

        /*// 如果API大于21设置状态栏为透明色
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            this.getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary));
        }
        // 温和的去除全屏切换非全屏屏幕闪动
        this.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);*/

        // 设置
        startActivity(new Intent(SplashActivity.this, MainActivity.class));
        finish();
    }

    @OnClick(R.id.timer_view)
    public void onViewClicked() {
        splashTimerView.stop();
        enterMain();
    }
}

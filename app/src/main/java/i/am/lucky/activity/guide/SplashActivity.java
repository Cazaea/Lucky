package i.am.lucky.activity.guide;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import i.am.lucky.R;
import i.am.lucky.activity.MainActivity;

/**
 * 启动页
 *
 * @author Cazaea
 * @time 2017/5/9 9:01
 * @mail wistorm@sina.com
 */
public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        // 延时跳转主界面
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                enterMain();
            }
        }, 1500);

    }


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

}

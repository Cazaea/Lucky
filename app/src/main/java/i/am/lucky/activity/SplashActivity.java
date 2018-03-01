package i.am.lucky.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.thejoyrun.router.Router;
import com.thejoyrun.router.RouterActivity;

import i.am.lucky.R;
import i.am.lucky.config.AppConfig;

/**
 * 启动页
 *
 * @author Cazaea
 * @time 2017/5/9 9:01
 * @mail wistorm@sina.com
 */
@RouterActivity("splash")
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
        Router.startActivity(this, AppConfig.ROUTER_HEAD + "://main");
        finish();
    }

}

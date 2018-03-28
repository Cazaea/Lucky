package i.am.lucky.activity.main;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import i.am.lucky.R;
import i.am.lucky.app.MainApp;
import i.am.lucky.config.AppConfig;

import com.cazaea.sweetalert.SweetAlertDialog;
import com.orhanobut.logger.Logger;
import com.thejoyrun.router.Router;
import com.thejoyrun.router.RouterActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import i.am.lucky.data.User;
import i.am.lucky.utils.LogUtil;

/*
 * @author Cazaea
 * @time 2017/11/19 14:41
 * @mail wistorm@sina.com
 *
 *             ,o%%%%%%%。
 *           ,%%/\%%%%/\%%。
 *          ,%%%\c "" J/%%%。
 * %.       %%%%/ o  o \%%%%
 * '%%.     %%%%    _  |%%%%
 *  '%%     '%%%%(__Y__)%%%'
 *  //       ;%%%%`\-/%%%%'
 * ((       /  '%%%%%%%%'
 *  \\    .'          )
 *   \\  /     .--,   |
 *    \\/          )| |
 *     \          /_| |__
 *     (____________))))))
 *
 *          攻城狮
 */

@RouterActivity("home")
public class HomeActivity extends AppCompatActivity {

    @BindView(R.id.hello_world)
    TextView helloWorld;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        try {
            User user = MainApp.getData().load(User.class, "User");
            helloWorld.setText(user.userInfo);
            LogUtil.d("数据已拿到。");
        } catch (Exception e) {
            LogUtil.e("暂时没数据。");
        }
        // 保存数据
        saved();

    }

    @OnClick(R.id.hello_world)
    public void onViewClicked() {
//        Router.startActivity(this, AppConfig.ROUTER_HEAD + "://web?url=" + "http://www.cazaea.com/love.mp4");
//        Router.startActivity(this, AppConfig.ROUTER_TOTAL_HEAD + "web?url=" + "http://a.app.qq.com/o/simple.jsp?pkgname=com.hxd.zjsmk");
//        Router.startActivity(this, AppConfig.ROUTER_TOTAL_HEAD + "login?username=" + "15010267528");

        final SweetAlertDialog dialog = new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText("你点了我了！");
        dialog.show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                    Router.startActivity(HomeActivity.this, AppConfig.ROUTER_HEAD + "://player");
                }
            }
        }, 2000);

    }

    /**
     * 测试保存数据
     */
    public void saved() {
        User user = new User();
        user.userInfo = "你好！";
        MainApp.getData().storeOrUpdate(user, "User");
    }

}

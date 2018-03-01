package i.am.lucky.activity.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import com.thejoyrun.router.Router;
import com.thejoyrun.router.RouterActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import i.am.lucky.R;
import i.am.lucky.config.AppConfig;

/**
 * @author Cazaea
 * @time 2017/11/19 15:36
 * @mail wistorm@sina.com
 */
@RouterActivity("code")
public class CodeActivity extends AppCompatActivity {

    @BindView(R.id.default_scan)
    Button defaultScan;
    @BindView(R.id.we_chat_scan)
    Button weChatScan;
    @BindView(R.id.ali_scan)
    Button aliScan;
    @BindView(R.id.create_code)
    Button createCode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.default_scan)
    public void onDefaultScanClicked() {
        Router.startActivity(this, AppConfig.ROUTER_HEAD + "://default");
    }

    @OnClick(R.id.we_chat_scan)
    public void onWeChatScanClicked() {
        Router.startActivity(this, AppConfig.ROUTER_HEAD + "://wechat");
    }

    @OnClick(R.id.ali_scan)
    public void onAliScanClicked() {
        Router.startActivity(this, AppConfig.ROUTER_HEAD + "://alipay");
    }

    @OnClick(R.id.create_code)
    public void onViewClicked() {
        Router.startActivity(this, AppConfig.ROUTER_HEAD + "://share");
//        startActivity(new Intent(ScanActivity.this, ShareActivity.class));
    }

}

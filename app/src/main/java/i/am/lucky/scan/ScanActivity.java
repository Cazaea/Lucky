package i.am.lucky.scan;

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

@RouterActivity("scan")
public class ScanActivity extends AppCompatActivity {

    @BindView(R.id.default_start)
    Button defaultStart;
    @BindView(R.id.wechat_start)
    Button wechatStart;
    @BindView(R.id.alipay_start)
    Button alipayStart;
    @BindView(R.id.create_code)
    Button createCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.default_start)
    public void onDefaultClicked() {
        Router.startActivity(this, AppConfig.ROUTER_HEAD + "://default");
    }

    @OnClick(R.id.wechat_start)
    public void onWechatClicked() {
        Router.startActivity(this, AppConfig.ROUTER_HEAD + "://wechat");
    }

    @OnClick(R.id.alipay_start)
    public void onAlipayClicked() {
        Router.startActivity(this, AppConfig.ROUTER_HEAD + "://alipay");
    }

    @OnClick(R.id.create_code)
    public void onViewClicked() {
        Router.startActivity(this, AppConfig.ROUTER_HEAD + "://share");
    }
}

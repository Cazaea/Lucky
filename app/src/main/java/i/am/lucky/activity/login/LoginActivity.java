package i.am.lucky.activity.login;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cazaea.sweetalert.SweetAlertDialog;
import i.am.lucky.R;
import i.am.lucky.app.MainApp;
import i.am.lucky.config.ApiConfig;
import i.am.lucky.config.AppConfig;
import i.am.lucky.config.EventConfig;
import i.am.lucky.data.User;
import i.am.lucky.http.HttpUtil;
import com.tencent.android.tpush.XGPushConfig;
import com.tencent.android.tpush.XGPushManager;
import com.thejoyrun.router.Router;
import com.thejoyrun.router.RouterActivity;
import com.thejoyrun.router.RouterField;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import xiaofei.library.datastorage.DataStorageFactory;
import xiaofei.library.datastorage.IDataStorage;

@RouterActivity("login")
public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.login_toolbar)
    Toolbar loginToolbar;
    @BindView(R.id.et_account)
    EditText etAccount;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.tv_register)
    TextView tvRegister;
    @BindView(R.id.tv_find_password)
    TextView tvFindPassword;

    @RouterField("page")
    String page;
    @RouterField("username")
    String username;

    private loginTask task;
    private SweetAlertDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Router.inject(this);
        ButterKnife.bind(this);

        loginToolbar.setTitle("");
        setSupportActionBar(loginToolbar);

        if (!username.isEmpty()) {
            etAccount.setText(username);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    /**
     * 登录账户
     */
    private void login() {
        String account = etAccount.getText().toString();
        String password = etPassword.getText().toString();
        if (account.equals("")) {
            etAccount.setHintTextColor(Color.RED);
            return;
        }
        if (password.equals("")) {
            etPassword.setHintTextColor(Color.RED);
            return;
        }

        HashMap<String, String> params = new HashMap<>();
        params.put("account", account);
        params.put("password", password);
        task = new loginTask();
        task.execute(params);
    }

    /**
     * 注册账户
     */
    private void register() {
        Router.startActivity(LoginActivity.this, AppConfig.ROUTER_TOTAL_HEAD + "register");
        LoginActivity.this.finish();
    }

    /**
     * 恢复密码
     */
    private void recoverPassword() {
        Router.startActivity(LoginActivity.this, AppConfig.ROUTER_TOTAL_HEAD + "recover");
    }

    /**
     * 监听返回按钮
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            exitWithoutLogin();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 返回键重载
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exitWithoutLogin();
            finish();
        }
        return true;
    }

    /**
     * 未登录即退出
     */
    private void exitWithoutLogin() {
        IDataStorage dataStorage = MainApp.getData();
        User user = dataStorage.load(User.class, "User");
        user.fromAccount = true;
        dataStorage.storeOrUpdate(user, "User");
        EventBus.getDefault().post(EventConfig.EVENT_EXIT_WITHOUT_LOGIN);
    }

    /**
     * 初始化信鸽推送
     * 注意本app可能要绑定账号
     */
    private void initXGPUSH() {
        IDataStorage dataStorage = DataStorageFactory.getInstance(
                getApplicationContext(), DataStorageFactory.TYPE_DATABASE);
        User user = dataStorage.load(User.class, "User");
        String pushId = "-1";
        try {
            JSONObject jsonObject = new JSONObject(user.userInfo);
            pushId = jsonObject.getString("id");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // 开启logcat输出，方便debug，发布时请关闭
        XGPushConfig.enableDebug(this, AppConfig.DEBUG_MODE);
        // 如果需要知道注册是否成功，请使用registerPush(getApplicationContext(), XGIOperateCallback)带callback版本
        // 如果需要绑定账号，请使用registerPush(getApplicationContext(),account)版本
        // 具体可参考详细的开发指南
        // 传递的参数为ApplicationContext
        Context context = getApplicationContext();
        if (AppConfig.XG_PUSH_MODE) {
            XGPushManager.registerPush(context, pushId);
        }

        // 其它常用的API：
        // 绑定账号（别名）注册：registerPush(context,account)或registerPush(context,account, XGIOperateCallback)，其中account为APP账号，可以为任意字符串（qq、openid或任意第三方），业务方一定要注意终端与后台保持一致。
        // 取消绑定账号（别名）：registerPush(context,"*")，即account="*"为取消绑定，解绑后，该针对该账号的推送将失效
        // 反注册（不再接收消息，用户没有业务要求，尽量不要调用此接口）：unregisterPush(context)
        // 设置标签：setTag(context, tagName)
        // 删除标签：deleteTag(context, tagName)
    }

    @OnClick({R.id.btn_login, R.id.tv_register, R.id.tv_find_password})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                login();
                break;
            case R.id.tv_register:
                register();
                break;
            case R.id.tv_find_password:
                recoverPassword();
                break;
        }
    }

    private class loginTask extends AsyncTask<HashMap<String, String>, Integer, Object[]> {
        @Override
        protected void onPreExecute() {
            if (progressDialog == null) {
                progressDialog = new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.PROGRESS_TYPE)
                        .setCancelText("取消")
                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                if (task != null) {
                                    task.cancel(true);
                                }
                            }
                        });
                progressDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                progressDialog.setTitleText("登录中");
            } else {
                progressDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                progressDialog.setTitleText("登录中");
            }
            progressDialog.show();
        }

        @Override
        protected Object[] doInBackground(HashMap<String, String>... params) {
            try {
                return HttpUtil.postHttp(LoginActivity.this, ApiConfig.LoginApi, params[0],
                        HttpUtil.TYPE_FORCE_NETWORK, 0);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object[] result) {
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            if ((boolean) result[0]) {
                try {
                    JSONObject data = (JSONObject) result[2];
                    // 保存数据
                    User user = new User();
                    user.userInfo = data.toString();
                    user.fromAccount = false;
                    IDataStorage dataStorage = MainApp.getData();
                    dataStorage.storeOrUpdate(user, "User");
                    initXGPUSH();
                    LoginActivity.this.finish();
                } catch (Exception ex) {
                    Toast.makeText(LoginActivity.this, "数据解析失败", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(LoginActivity.this, result[1].toString(), Toast.LENGTH_SHORT).show();
            }

        }

        @Override
        protected void onCancelled() {
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        }
    }

}


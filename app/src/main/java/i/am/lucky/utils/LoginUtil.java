package i.am.lucky.utils;

import i.am.lucky.app.MainApp;
import i.am.lucky.data.User;

import org.json.JSONObject;

/**
 * 登录判断工具类
 *
 * @author Cazaea
 * @time 2017/11/1 16:59
 * @mail wistorm@sina.com
 */

public class LoginUtil {

    /**
     * 判断是否已登录
     */
    public static boolean isLogin() {

        // 用户信息
        User user = MainApp.getData().load(User.class, "User");

        boolean isLogin = false;
        try {
            JSONObject jo = new JSONObject(user.userInfo);
            String user_id = jo.getString("id").trim();
            if (!user_id.equals("-1")) {
                isLogin = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return isLogin;

    }

}

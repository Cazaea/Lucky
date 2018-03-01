package i.am.lucky.utils;

import android.content.Context;

import org.json.JSONObject;

import i.am.lucky.data.User;
import xiaofei.library.datastorage.DataStorageFactory;
import xiaofei.library.datastorage.IDataStorage;

/**
 * @author Cazaea
 * @time 2017/11/1 16:59
 * @mail wistorm@sina.com
 */

public class LoginUtil {

    /**
     * 判断是否已登录
     */
    public static boolean isLogin(Context context) {

        IDataStorage dataStorage = DataStorageFactory.getInstance(
                context.getApplicationContext(), DataStorageFactory.TYPE_DATABASE);
        User user = dataStorage.load(User.class, "User");

        boolean isLogin = true;
        try {
            JSONObject jo = new JSONObject(user.userInfo);
            String user_id = jo.getString("id").toString().trim();
            if (user_id.equals("-1")) {
                isLogin = false;
            }
        } catch (Exception e) {
            isLogin = false;
        }

        return isLogin;

    }

}

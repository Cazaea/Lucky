package i.am.lucky.http;

import android.content.Context;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import i.am.lucky.config.AppConfig;
import i.am.lucky.config.EventConfig;
import i.am.lucky.data.User;
import i.am.lucky.utils.DeviceUtil;
import i.am.lucky.utils.ToolsUtil;
import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import xiaofei.library.datastorage.DataStorageFactory;
import xiaofei.library.datastorage.IDataStorage;

/**
 * Created by Cazaea on 2017/6/9.
 * 此类为http请求基础类
 */

public class HttpUtil {

    private static final CacheControl FORCE_NETWORK = new CacheControl.Builder().noCache().build();
    private static final CacheControl FORCE_CACHE = new CacheControl.Builder()
            .onlyIfCached()
            .maxStale(Integer.MAX_VALUE, TimeUnit.SECONDS)
            .build();

    public static final String TYPE_FORCE_CACHE = "TYPE_FORCE_CACHE";
    public static final String TYPE_FORCE_NETWORK = "TYPE_FORCE_NETWORK";
    public static final String TYPE_CACHE_CONTROL = "TYPE_CACHE_CONTROL";

    /**
     * 此静态方法为一般post方法，因在AsyncTask中调用
     *
     * @param context       调用场景context
     * @param url           请求地址
     * @param params        请求参数
     * @param cache_type    缓存类型(TYPE_FORCE_CACHE,TYPE_FORCE_NETWORK,TYPE_CACHE_CONTROL),如果为TYPE_CACHE_CONTROL 需要填写缓存时间
     * @param cache_seconds 缓存时间，单位秒
     * @return 返回对象数组 obj[0]:bool,成功、失败；obj[1]:String,提示信息；obj[2]：JSONObject/null，返回数据
     */
    public static Object[] postHttp(Context context, String url, HashMap<String, String> params, String cache_type, int cache_seconds) {
        //参数中添加平台信息、版本号、设备编号
        params = addInfo(context, params);
        try {
            params.put("sign", getSignature(params, "1qaz2wsx"));
        } catch (IOException e) {

        }
        try {
            //缓存文件夹
            File cacheFile = new File(context.getExternalCacheDir().toString(), "cache");
            //缓存大小为50M
            int cacheSize = 50 * 1024 * 1024;
            //创建缓存对象
            final Cache cache = new Cache(cacheFile, cacheSize);

            OkHttpClient mOkHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(20, TimeUnit.SECONDS)
                    .cache(cache)
                    .build();

            FormBody.Builder formBodyBuilder = new FormBody.Builder();
            for (Map.Entry<String, String> entry : params.entrySet())
                formBodyBuilder.add(entry.getKey(), entry.getValue());
            RequestBody formBody = formBodyBuilder.build();

            CacheControl cacheControl = null;
            if (cache_type.equals(TYPE_CACHE_CONTROL)) {
                cacheControl = new CacheControl.Builder()
                        .maxAge(cache_seconds, TimeUnit.SECONDS)
                        .build();
            }
            if (cache_type.equals(TYPE_FORCE_CACHE)) {
                cacheControl = FORCE_CACHE;
            }
            if (cache_type.equals(TYPE_FORCE_NETWORK)) {
                cacheControl = FORCE_NETWORK;
            }

            Request request = new Request.Builder()
                    .cacheControl(cacheControl)
                    .url(url)
                    .post(formBody)
                    .build();
            Response response = mOkHttpClient.newCall(request).execute();
            String result = response.body().string();
            JSONObject jo = new JSONObject(result);
            if (jo.getInt("code") == 1000) {
                return new Object[]{true, jo.getString("msg"), jo.isNull("data") ? new JSONObject() : jo.get("data")};
            } else {
                if (jo.getInt("code") == 1102) {
                    IDataStorage dataStorage = DataStorageFactory.getInstance(
                            context.getApplicationContext(), DataStorageFactory.TYPE_DATABASE);
                    User user = dataStorage.load(User.class, "User");
                    if (AppConfig.XG_PUSH_MODE) {
//                        XGPushManager.registerPush(context.getApplicationContext(), "*");
                    }
                    // 将用户数据设置为默认数
                    user.userInfo = User.defaultInfo;
                    user.fromAccount = "true";
                    user.hasLogin = "false";
                    dataStorage.storeOrUpdate(user, "User");
                    EventBus.getDefault().post(EventConfig.EVENT_LOGOUT);
                    return new Object[]{false, "用户不存在，请重新登录", null};
                }
                return new Object[]{false, jo.getString("msg"), null, jo.getInt("code")};
            }
        } catch (Exception e) {
            if (e instanceof JSONException) {
                return new Object[]{false, "数据解析失败", null};
            } else if (NetWorkUtil.isNetworkConnected(context)) {
                return new Object[]{false, "无法访问服务器", null};
            } else {
                return new Object[]{false, "当前无法访问网络", null};
            }
        }
    }

    /**
     * @param context 调用场景context
     * @param url     请求地址
     * @param params  请求参数
     * @param image   要上传的png图片
     * @return 返回对象数组 obj[0]:bool,成功、失败；obj[1]:String,提示信息；obj[2]：JSONObject/null，返回数据
     */
    public static Object[] postHttpJPG(Context context, String url, HashMap<String, String> params, HashMap<String, File> image) {
        //参数中添加平台信息、版本号、设备编号
        params = addInfo(context, params);
        try {
            params.put("sign", getSignature(params, "1qaz2wsx"));
        } catch (IOException e) {

        }

        try {
            //params.put("sign", getSignature(params, "1qaz2wsx"));
            OkHttpClient mOkHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(20, TimeUnit.SECONDS)
                    .build();
            MultipartBody.Builder multipartBody = new MultipartBody.Builder();
            multipartBody.setType(MultipartBody.FORM);
            for (Map.Entry<String, String> entry : params.entrySet())
                multipartBody.addFormDataPart(entry.getKey(), entry.getValue());
            for (Map.Entry<String, File> entry : image.entrySet()) {
                RequestBody fileBody = RequestBody.create(MediaType.parse("image/jpg"), entry.getValue());
                multipartBody.addFormDataPart(entry.getKey(), entry.getKey() + ".jpg", fileBody);
            }
            RequestBody requestBody = multipartBody.build();

            Request request = new Request.Builder()
                    .url(url)
                    .post(requestBody)
                    .build();
            Response response = mOkHttpClient.newCall(request).execute();
            String result = response.body().string();
            JSONObject jo = new JSONObject(result);
            if (jo.getInt("code") == 1000) {
                return new Object[]{true, jo.getString("msg"), jo.get("data")};
            } else {
                return new Object[]{false, jo.getString("msg"), jo.get("data")};
            }
        } catch (Exception e) {
            if (e instanceof JSONException) {
                return new Object[]{false, "JSON解析失败", null};
            } else if (NetWorkUtil.isNetworkConnected(context)) {
                return new Object[]{false, "无法访问服务器", null};
            } else {
                return new Object[]{false, "当前无法访问网络", null};
            }
        }
    }

    /**
     * 参数中添加平台信息、版本号、设备编号
     *
     * @param context
     * @param params
     * @return
     */
    public static HashMap<String, String> addInfo(Context context, HashMap<String, String> params) {
        // 添加平台信息
        params.put("platform", "Android");
        // 添加版本号
        params.put("version", ToolsUtil.getVersionCode(context));
        // 添加设备编号
        params.put("device_uid", DeviceUtil.getUniqueId(context));
        return params;
    }

    /**
     * 数字签名工具
     * 1qaz2wsx
     *
     * @param postPairs
     * @param secret
     * @return
     * @throws IOException
     */
    public static String getSignature(HashMap<String, String> postPairs,
                                      String secret) throws IOException {
        StringBuilder baseString = new StringBuilder();
        List<Map.Entry<String, String>> list = new ArrayList<Map.Entry<String, String>>(postPairs.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<String, String>>() {
            //升序排序
            public int compare(Map.Entry<String, String> o1,
                               Map.Entry<String, String> o2) {
                return o1.getKey().compareTo(o2.getKey());
            }

        });

        for (Map.Entry<String, String> mapping : list) {
            System.out.println(mapping.getKey() + ":" + mapping.getValue());

            baseString.append(mapping.getKey()).append("=")
                    .append(mapping.getValue());
        }
        baseString.append(secret);

        byte[] bytes = null;
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            bytes = md5.digest(baseString.toString().getBytes("UTF-8"));
        } catch (GeneralSecurityException ex) {
            throw new IOException(ex);
        }

        StringBuilder sign = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(bytes[i] & 0xFF);
            if (hex.length() == 1) {
                sign.append("0");
            }
            sign.append(hex);
        }
        return sign.toString();
    }

}

package i.am.lucky.data;

/**
 * Created by Cazaea on 2017/6/28.
 * User 信息保存
 * 默认 user_id 为 -1   token 为 -1
 */

public class User {

    public static final String defaultInfo = "{\n" +
            "        \"id\": \"-1\",\n" +
            "        \"account\": \"cazaea\",\n" +
            "        \"nickname\": \"\",\n" +
            "        \"phone\": \"\",\n" +
            "        \"create_time\": \"\",\n" +
            "        \"status\": \"0\",\n" +
            "        \"weixin_flag\": \"0\",\n" +
            "        \"remark\": null,\n" +
            "        \"delete_flag\": \"0\",\n" +
            "        \"delete_time\": null,\n" +
            "        \"delete_user_id\": null,\n" +
            "        \"token\": \"-1\"\n" +
            "    }";

    public String userInfo;

    public String hasLogin;

    public String fromAccount;

}

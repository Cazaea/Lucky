package i.am.lucky.config;

/**
 * @author Cazaea
 * @time 2017/11/3 13:26
 * @mail wistorm@sina.com
 */

public class ApiConfig {

    // 接口根目录
    private static String ROOT = "http://192.168.1.7:801/app";

    //=========================文件路径=========================//
    public static final String PATH_NAME = "Lucky";

    //=========================接口数据=========================//

    // 登录
    public static String LoginApi = ROOT + " ";
    // 注册
    public static String RegisterApi = ROOT + " ";
    // 找回密码
    public static String FindPwdApi = ROOT + " ";
    // 修改密码
    public static String ChangePwdApi = ROOT + " ";
    // 获取验证码
    public static String AuthCodeApi = ROOT + " ";
    // 首页数据
    public static String HomeDataApi = ROOT + " ";
    // 众筹数据
    public static String FundDataApi = ROOT + " ";
    // 市场数据
    public static String MarketDataApi = ROOT + " ";
    // 租赁数据
    public static String LeaseDataApi = ROOT + " ";
    // 个人数据
    public static String MineDataApi = ROOT + " ";
    // 版本检测
    public static String CheckUpdateApi = ROOT + " ";
    // 获取配置信息
    public static String GetConfigApi = ROOT + " ";
    // 订单详情
    public static String OrderDetailApi = ROOT + " ";

}

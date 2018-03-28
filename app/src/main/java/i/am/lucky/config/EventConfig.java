package i.am.lucky.config;

/**
 * 用于配置EventBus的事件
 *
 * @author Cazaea
 * @time 2017/11/3 14:48
 * @mail wistorm@sina.com
 */

public class EventConfig {

    // 退出登录
    public static String EVENT_LOGOUT = "EVENT_LOGOUT";
    // 未登录及退出
    public static String EVENT_EXIT_WITHOUT_LOGIN = "EVENT_EXIT_WITHOUT_LOGIN";
    // 弹窗广告消息
    public static String EVENT_BOX_INFO = "EVENT_BOX_INFO";
    // 返回键处理
    public static String EVENT_PRESS_BACK = "EVENT_PRESS_BACK";
    // 向上滑动
    public static String EVENT_SCROLL_TO_UP = "EVENT_SCROLL_TO_UP";
    // 向下滑动
    public static String EVENT_SCROLL_TO_DOWN = "EVENT_SCROLL_TO_DOWN";
    // 加载Web页面
    public static String EVENT_RELOAD_WEB = "EVENT_RELOAD_WEB";
    // 刷新个人信息
    public static String EVENT_REFRESH_MINE = "EVENT_REFRESH_MINE";

}

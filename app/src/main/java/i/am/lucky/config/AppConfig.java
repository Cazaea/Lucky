package i.am.lucky.config;

/**
 * 用于配置App的常量及开关
 *
 * @author Cazaea
 * @time 2017/11/17 14:48
 * @mail wistorm@sina.com
 */

public class AppConfig {

    /**
     * Open or Close
     */
    public static final boolean DEBUG_MODE = true;
    public static final boolean XG_PUSH_MODE = true;

    /**
     * Constants
     * 应用所需常量
     */
    public static final String ROUTER_HEAD = "cazaea";
    public static final String ROUTER_TOTAL_HEAD = "cazaea://";
    public static final String ROUTER_WEBSITE = "www.cazaea.com";

    public static final String PROVIDER_FILE_NAME = "${applicationId}.fileProvider";

    // 图库选择
    public static final int CODE_SELECT_IMAGE = 2000;
    // 相机拍照
    public static final int CODE_TAKE_PHOTO = 2001;
    // 裁剪图片
    public static final int CODE_CUT_IMAGE = 2002;
    // 默认图片名称
    public static final String IMAGE_FILE_NAME = "icon.jpg";


}

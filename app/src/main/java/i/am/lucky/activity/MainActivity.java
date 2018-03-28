package i.am.lucky.activity;

import android.app.LocalActivityManager;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import i.am.lucky.R;
import i.am.lucky.activity.main.CodeActivity;
import i.am.lucky.activity.main.HomeActivity;
import i.am.lucky.activity.main.SiteActivity;
import i.am.lucky.activity.main.UnityActivity;
import i.am.lucky.activity.main.VideoActivity;
import i.am.lucky.adapters.PagerAdapter;
import i.am.lucky.config.EventConfig;
import i.am.lucky.utils.helper.BottomNavigationViewHelper;
import com.thejoyrun.router.RouterActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * █████▒█      ██  ▄████▄   ██ ▄█▀       ██████╗ ██╗   ██╗ ██████╗
 * ▓██   ▒ ██  ▓██▒▒██▀ ▀█   ██▄█▒        ██╔══██╗██║   ██║██╔════╝
 * ▒████ ░▓██  ▒██░▒▓█    ▄ ▓███▄░        ██████╔╝██║   ██║██║  ███╗
 * ░▓█▒  ░▓▓█  ░██░▒▓▓▄ ▄██▒▓██ █▄        ██╔══██╗██║   ██║██║   ██║
 * ░▒█░   ▒▒█████▓ ▒ ▓███▀ ░▒██▒ █▄       ██████╔╝╚██████╔╝╚██████╔╝
 * ▒ ░   ░▒▓▒ ▒ ▒ ░ ░▒ ▒  ░▒ ▒▒ ▓▒       ╚═════╝  ╚═════╝  ╚═════╝
 * ░     ░░▒░ ░ ░   ░  ▒   ░ ░▒ ▒░
 * ░ ░    ░░░ ░ ░ ░        ░ ░░ ░
 * ░     ░ ░      ░  ░
 */
@RouterActivity("main")
public class MainActivity extends AppCompatActivity {

    @BindView(R.id.viewpager)
    ViewPager viewPager;
    @BindView(R.id.bottom_navigation)
    BottomNavigationView bottomNavigationView;

    // 控制页面切换
    private LocalActivityManager manager;
    // 底部导航菜单
    private MenuItem menuItem;

    // 第一次点击时间
    private long firstTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 因为全局设置Activity转场动画，无法全局设定状态栏透明主题
        // 故在此申请占位状态栏，以解决Android5.0及以下版本全屏切换非全屏屏幕闪动问题
        // initStatusBar(R.color.colorPrimary);

        ButterKnife.bind(this);
        // 注册EventBus
        EventBus.getDefault().register(this);

        // 初始化控件, 及ViewPager绑定
        initViewPager(savedInstanceState);
        // 初始化底部导航栏
        initNavigation();
        // 添加页面
        addActivities();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 解绑EventBus
        EventBus.getDefault().unregister(this);
    }

    /**
     * 状态栏处理：解决全屏切换非全屏页面被压缩问题
     */
    public void initStatusBar(int barColor) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
            // 获取状态栏高度
            int statusBarHeight = getResources().getDimensionPixelSize(resourceId);
            View rectView = new View(this);
            // 绘制一个和状态栏一样高的矩形，并添加到视图中
            LinearLayout.LayoutParams params
                    = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, statusBarHeight);
            rectView.setLayoutParams(params);
            // 设置状态栏颜色
            rectView.setBackgroundColor(getResources().getColor(barColor));
            // 添加矩形View到布局中
            ViewGroup decorView = (ViewGroup) getWindow().getDecorView();
            decorView.addView(rectView);
            ViewGroup rootView = (ViewGroup) ((ViewGroup) this.findViewById(android.R.id.content)).getChildAt(0);
            rootView.setFitsSystemWindows(true);
            rootView.setClipToPadding(true);
        }
    }

    /**
     * 通过滑动RecyclerView设置Tab显示隐藏
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(String message) {

        if (message.equals(EventConfig.EVENT_SCROLL_TO_UP)) {
            scrollUp();
        } else if (message.equals(EventConfig.EVENT_SCROLL_TO_DOWN)) {
            scrollDown();
        } else if (message.equals(EventConfig.EVENT_PRESS_BACK)) {
            if (System.currentTimeMillis() - firstTime > 2000) {
                Toast.makeText(MainActivity.this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                firstTime = System.currentTimeMillis();
            } else {
                finish();
                System.exit(0);
            }
        }
    }

    /**
     * RecyclerView向上滑动
     */
    private void scrollUp() {
        // 隐藏底部导航栏
        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) bottomNavigationView.getLayoutParams();
        int fabBottomMargin = lp.bottomMargin;
        bottomNavigationView.animate().translationY(bottomNavigationView.getHeight() + fabBottomMargin).setInterpolator(new AccelerateInterpolator(2)).start();
    }

    /**
     * RecyclerView向下滑动
     */
    private void scrollDown() {
        // 显示底部导航栏
        bottomNavigationView.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2)).start();
    }

    /**
     * 初始化主页面
     */
    private void initViewPager(Bundle savedInstanceState) {

        manager = new LocalActivityManager(this, true);
        manager.dispatchCreate(savedInstanceState);

        // ViewPager添加切换监听
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (menuItem != null) {
                    menuItem.setChecked(false);
                } else {
                    bottomNavigationView.getMenu().getItem(0).setChecked(false);
                }
                menuItem = bottomNavigationView.getMenu().getItem(position);
                menuItem.setChecked(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        //禁止ViewPager滑动
        /*viewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });*/

    }

    /**
     * ViewPager绑定，底部导航栏实现
     */
    private void initNavigation() {
        // 默认>3时选中效果会影响ViewPager的滑动切换效果，故利用反射去掉
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);

        // 底部导航栏绑定ViewPager
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.menu_home:
                                viewPager.setCurrentItem(0);
                                break;
                            case R.id.menu_code:
                                viewPager.setCurrentItem(1);
                                break;
                            case R.id.menu_unity:
                                viewPager.setCurrentItem(2);
                                break;
                            case R.id.menu_site:
                                viewPager.setCurrentItem(3);
                                break;
                            case R.id.menu_video:
                                viewPager.setCurrentItem(4);
                                break;
                        }
                        return false;
                    }
                });

    }

    /**
     * 将Activity添加进ViewPager
     */
    private void addActivities() {

        List<View> mViews = new ArrayList<>();
        Intent intent = new Intent();

        intent.setClass(this, HomeActivity.class);
        intent.putExtra("id", 1);
        mViews.add(getView("activity_home", intent));

        intent.setClass(this, CodeActivity.class);
        intent.putExtra("id", 2);
        mViews.add(getView("activity_code", intent));

        intent.setClass(this, UnityActivity.class);
        intent.putExtra("id", 3);
        mViews.add(getView("activity_unity", intent));

        intent.setClass(this, SiteActivity.class);
        intent.putExtra("id", 4);
        mViews.add(getView("activity_site", intent));

        intent.setClass(this, VideoActivity.class);
        intent.putExtra("id", 5);
        mViews.add(getView("activity_video", intent));

        PagerAdapter adapter = new PagerAdapter(mViews);
        viewPager.setAdapter(adapter);

    }

    /**
     * 通过activity获取视图
     *
     * @param id
     * @param intent
     * @return
     */
    private View getView(String id, Intent intent) {
        return manager.startActivity(id, intent).getDecorView();
    }

    /**
     * 再按一次退出程序
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            EventBus.getDefault().post(EventConfig.EVENT_PRESS_BACK);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
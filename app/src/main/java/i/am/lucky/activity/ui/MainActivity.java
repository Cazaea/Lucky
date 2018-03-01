package i.am.lucky.activity.ui;

import android.app.LocalActivityManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;

import com.thejoyrun.router.RouterActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import i.am.lucky.R;
import i.am.lucky.adapters.MyPagerAdapter;
import i.am.lucky.config.EventConfig;
import i.am.lucky.utils.BottomNavigationViewHelper;

/**
 * █████▒█    ██  ▄████▄   ██ ▄█▀       ██████╗ ██╗   ██╗ ██████╗
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
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    // 控制页面切换
    private LocalActivityManager manager;
    // 底部导航菜单
    private MenuItem menuItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        // 注册EventBus
        EventBus.getDefault().register(this);

        // 初始化ToolBar
        initToolBar();
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
     * 通过滑动RecyclerView设置Tab显示隐藏
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(String message) {

        if (message.equals(EventConfig.EVENT_SCROLL_UP)) {
            scrollUp();
        } else if (message.equals(EventConfig.EVENT_SCROLL_DOWN)) {
            scrollDown();
        } else if (message.equals(EventConfig.EVENT_PRESS_BACK)) {

        }
    }

    /**
     * RecyclerView向上滑动
     */
    private void scrollUp() {
        // 显示ToolBar
        toolbar.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2));
        // 隐藏底部导航栏
        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) bottomNavigationView.getLayoutParams();
        int fabBottomMargin = lp.bottomMargin;
        bottomNavigationView.animate().translationY(bottomNavigationView.getHeight() + fabBottomMargin).setInterpolator(new AccelerateInterpolator(2)).start();
    }

    /**
     * RecyclerView向下滑动
     */
    private void scrollDown() {
        // 隐藏ToolBar
        toolbar.animate().translationY(-toolbar.getHeight()).setInterpolator(new AccelerateInterpolator(2));
        // 显示底部导航栏
        bottomNavigationView.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2)).start();
    }

    /**
     * 初始化ToolBar
     */
    private void initToolBar() {
        toolbar.setTitle("个人技术展示");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_TITLE);
            //actionBar.setTitle(R.string.app_name);
        }
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
//        viewPager.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                return true;
//            }
//        });

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
                            case R.id.menu_scan:
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

        MyPagerAdapter adapter = new MyPagerAdapter(mViews);
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

}

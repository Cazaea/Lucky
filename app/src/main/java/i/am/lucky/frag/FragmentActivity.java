package i.am.lucky.frag;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.thejoyrun.router.RouterActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import i.am.lucky.R;
import i.am.lucky.frag.BaseFragment;
import i.am.lucky.utils.BottomNavigationViewHelper;

/**
 * 原本为MainActivity
 * 实现ViewPager+Fragment实现切换
 * 后改为ViewPager+Activity切换
 *
 * @author Cazaea
 */
@RouterActivity("fragment")
public class FragmentActivity extends AppCompatActivity {

    @BindView(R.id.viewpager)
    ViewPager viewPager;
    @BindView(R.id.bottom_navigation)
    BottomNavigationView bottomNavigationView;

    private MenuItem menuItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        // 初始化控件, 及ViewPager绑定
        initView();
    }

    /**
     * ViewPager绑定，底部导航栏实现
     */
    private void initView() {
        // 默认>3时选中效果会影响ViewPager的滑动切换效果，故利用反射去掉
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
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

        setupViewPager(viewPager);
    }

    private List<Fragment> mFragments = new ArrayList<>();
    ;

    private void setupViewPager(ViewPager viewPager) {

        if (mFragments.size() != 0) {
            mFragments.clear();
        } else {
            mFragments.add(BaseFragment.newInstance("首页"));
            mFragments.add(BaseFragment.newInstance("扫码"));
            mFragments.add(BaseFragment.newInstance("Unity3D"));
            mFragments.add(BaseFragment.newInstance("网站"));
            mFragments.add(BaseFragment.newInstance("视频"));
        }

        ViewPagerAdapter pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), mFragments);

        viewPager.setAdapter(pagerAdapter);
    }


    public class ViewPagerAdapter extends FragmentPagerAdapter {
        private List<Fragment> mFragments;

        ViewPagerAdapter(FragmentManager fm, List<Fragment> mFragments) {
            super(fm);
            this.mFragments = mFragments;
        }

        @Override
        public Fragment getItem(int position) {//必须实现
            return mFragments.get(position);
        }

        @Override
        public int getCount() {//必须实现
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {//选择性实现
            return mFragments.get(position).getClass().getSimpleName();
        }
    }

}

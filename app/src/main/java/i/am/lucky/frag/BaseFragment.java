package i.am.lucky.frag;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import i.am.lucky.R;

/**
 * @author Cazaea
 * @time 2017/9/25 15:29
 * @mail wistorm@sina.com
 */
public class BaseFragment extends Fragment {

    @BindView(R.id.frag_text)
    TextView fragText;
    @BindView(R.id.frag_swipe)
    SwipeRefreshLayout fragmentSwipe;

    Unbinder unbinder;

    String type_code;

    public static BaseFragment newInstance(String tag) {
        BaseFragment myFragment = new BaseFragment();
        //传递参数
        Bundle bundle = new Bundle();
        bundle.putString("type_code", tag);
        myFragment.setArguments(bundle);
        return myFragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        @SuppressLint("InflateParams") View view = LayoutInflater.from(this.getActivity()).inflate(R.layout.fragment_base, null);
        //初始化控件
        unbinder = ButterKnife.bind(this, view);

        initRefresh();

        // 模拟加载数据
        getInfo();

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //获取参数
        Bundle arguments = getArguments();
        assert arguments != null;
        type_code = arguments.getString("type_code");

        fragText.setText(type_code);

    }

    /**
     * 初始化刷新
     */
    private void initRefresh() {

        fragmentSwipe.setColorSchemeResources(android.R.color.holo_blue_light,
                android.R.color.holo_red_light, android.R.color.holo_orange_light,
                android.R.color.holo_green_light);
        fragmentSwipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // 刷新数据
                getInfo();
            }
        });
    }

    public void getInfo() {
//        fragmentSwipe.setRefreshing(true);
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                if (fragmentSwipe != null && fragmentSwipe.isRefreshing())
//                    fragmentSwipe.setRefreshing(false);
//            }
//        }, 1500);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

}

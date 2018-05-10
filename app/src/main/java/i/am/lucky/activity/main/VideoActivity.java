package i.am.lucky.activity.main;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnScrollListener;

import com.cazaea.recycler.GridRecyclerView;
import com.cazaea.recycler.OnLoadMoreListener;
import com.cazaea.recycler.SuperRecyclerView;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.request.BaseRequest;
import com.thejoyrun.router.RouterActivity;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import i.am.lucky.R;
import i.am.lucky.adapters.HotMoviesAdapter;
import i.am.lucky.api.DouBanApi;
import i.am.lucky.api.NetEaseApi;
import i.am.lucky.config.EventConfig;
import i.am.lucky.data.Bean.HotMoviesBean;
import i.am.lucky.utils.JsonTools;
import i.am.lucky.utils.LogUtil;
import i.am.lucky.utils.UrlAnalysis;
import i.am.lucky.utils.video.GridMarginDecoration;
import okhttp3.Call;
import okhttp3.Response;

/**
 * @author Cazaea
 * @time 2017/11/19 15:36
 * @mail wistorm@sina.com
 */

@RouterActivity("video")
public class VideoActivity extends AppCompatActivity {

    @BindView(R.id.video_recycler)
    GridRecyclerView videoRecycler;
    @BindView(R.id.video_refresh)
    SwipeRefreshLayout videoRefresh;

    GridLayoutManager gridLayoutManager;
    HotMoviesAdapter moviesAdapter;

    private final int REFRESH = 0;
    private final int LOADING = 1;

    HotMoviesBean hotMoviesBean;

    private List<HotMoviesBean.SubjectsBean> mList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        ButterKnife.bind(this);
        // 初始化刷新
        initRefresh();
        // 初始化RecyclerView样式
        initRecycler();
        // 监听RecyclerView滑动
        viewListener(videoRecycler);

    }

    /**
     * 监听RecyclerView滑动状态
     */
    private void viewListener(RecyclerView view) {

        videoRecycler.setOnScrollListener(new MyScrollListener() {
            @Override
            public void scrollUp() {
                EventBus.getDefault().post(EventConfig.EVENT_SCROLL_TO_UP);
            }

            @Override
            public void scrollDown() {
                EventBus.getDefault().post(EventConfig.EVENT_SCROLL_TO_DOWN);
            }
        });

    }

    /**
     * 监听RecyclerView滑动状态
     */
    abstract class MyScrollListener extends OnScrollListener {

        private static final int HIDE_THRESHOLD = 20;
        private int scrolledDistance = 0;
        private boolean controlsVisible = true;

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            if (scrolledDistance > HIDE_THRESHOLD && controlsVisible) {
                scrollUp();
                controlsVisible = false;
                scrolledDistance = 0;
            } else if (scrolledDistance < -HIDE_THRESHOLD && !controlsVisible) {
                scrollDown();
                controlsVisible = true;
                scrolledDistance = 0;
            }
            if ((controlsVisible && dy > 0) || (!controlsVisible && dy < 0)) {
                scrolledDistance += dy;
            }
        }

        abstract void scrollUp();

        abstract void scrollDown();
    }

    /**
     * 初始化刷新
     */
    private void initRefresh() {

        videoRefresh.setColorSchemeResources(android.R.color.holo_blue_light,
                android.R.color.holo_red_light, android.R.color.holo_orange_light,
                android.R.color.holo_green_light);
        videoRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // 刷新数据
                getInfo(REFRESH, 0);
            }
        });
    }

    /**
     * RecyclerView排布样式更改
     */
    private void initRecycler() {

        gridLayoutManager = new GridLayoutManager(this, 3);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                switch (position % 6) {
                    case 5:
                        return 3;
                    case 3:
                        return 2;
                    default:
                        return 1;
                }
            }
        });

        videoRecycler.setLayoutManager(gridLayoutManager);
        videoRecycler.addItemDecoration(new GridMarginDecoration(
                getResources().getDimensionPixelSize(R.dimen.grid_item_spacing)));
        videoRecycler.setHasFixedSize(true);

        // 上拉加载更多
        videoRecycler.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(SuperRecyclerView recyclerView) {
                recyclerView.showFootProgress();
                // 获取数据
                if (hotMoviesBean != null) {
                    if (hotMoviesBean.isHasNext()) {
                        recyclerView.showFootProgress();
                        getInfo(LOADING, hotMoviesBean.getNextIndex());
                    } else {
                        recyclerView.showFootProgressEnd();
                    }
                }
            }
        });

        // 先进行一次刷新
        videoRefresh.post(new Runnable() {
            @Override
            public void run() {
                getInfo(REFRESH, 0);
            }
        });
    }

    /**
     * 获取数据
     */
    public void getInfo(final int type, final int pageIndex) {

        String userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.59 Safari/537.36";
        String accept = "application/vnd.yourapi.v1.full+json";

        // 获取网易视频内容
        OkGo.get(NetEaseApi.Url_Real_Movies)
                .headers("Accept", accept)
                .headers("User-Agent", userAgent)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        // 网易短视频Json
                        LogUtil.json(s);
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        LogUtil.e("添加Header后调用接口失败！！！");
                    }
                });

        OkGo.get(DouBanApi.Url_HotMovie)
                .params("city", "北京")
                .params("start", pageIndex)
                .params("count", 12)
                .execute(new StringCallback() {

                    @Override
                    public void onBefore(BaseRequest request) {
                        super.onBefore(request);
                        if (type == REFRESH)
                            videoRefresh.setRefreshing(true);
                    }

                    @Override
                    public void onSuccess(String s, Call call, Response response) {

                        LogUtil.json(s);

                        hotMoviesBean = new Gson().fromJson(s, HotMoviesBean.class);
                        List<HotMoviesBean.SubjectsBean> hotMovies = hotMoviesBean.getSubjects();

//                        if (pageIndex != 0) {
//                            if (hotMovies != null) {
//                                hotMovies = JsonTools.joinJSONArray(info, titles);
//                            }
//
//                        }

                        if (moviesAdapter == null) {
                            moviesAdapter = new HotMoviesAdapter(hotMovies, VideoActivity.this);
                            videoRecycler.setAdapter(moviesAdapter);
                        } else {

                            moviesAdapter.update(hotMovies);
                        }

                    }

                    @Override
                    public void onCacheSuccess(String s, Call call) {
                        super.onCacheSuccess(s, call);

                        HotMoviesBean data = new Gson().fromJson(s, HotMoviesBean.class);
                        List<HotMoviesBean.SubjectsBean> hotMovies = data.getSubjects();

                        if (moviesAdapter == null) {
                            moviesAdapter = new HotMoviesAdapter(hotMovies, VideoActivity.this);
                            videoRecycler.setAdapter(moviesAdapter);
                        } else {
                            moviesAdapter.update(hotMovies);
                        }

                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                    }

                    @Override
                    public void onAfter(String s, Exception e) {
                        super.onAfter(s, e);
                        if (type == REFRESH)
                            videoRefresh.setRefreshing(false);
                    }
                });

    }

}

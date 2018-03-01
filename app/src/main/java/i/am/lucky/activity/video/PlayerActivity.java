package i.am.lucky.activity.video;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.cazaea.recycler.CommonViewHolder;
import com.cazaea.recycler.RecyclerViewCommonAdapter;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.request.BaseRequest;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.utils.GSYVideoType;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;
import com.shuyu.gsyvideoplayer.video.base.GSYVideoPlayer;
import com.squareup.picasso.Picasso;
import com.thejoyrun.router.RouterActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import i.am.lucky.R;
import i.am.lucky.api.DouBanApi;
import i.am.lucky.data.Bean.MovieDetail;
import i.am.lucky.utils.video.GridMarginDecoration;
import okhttp3.Call;
import okhttp3.Response;

@RouterActivity("player")
public class PlayerActivity extends AppCompatActivity {

    private static final String INTENT_KEY_MOVIE_ID = "id";

    @BindView(R.id.video_player)
    StandardGSYVideoPlayer videoPlayer;
    @BindView(R.id.movie_name)
    TextView movieName;
    @BindView(R.id.rb_movie_level)
    RatingBar rbMovieLevel;
    @BindView(R.id.tv_movie_level_info)
    TextView tvMovieLevelInfo;
    @BindView(R.id.tv_movie_info)
    TextView tvMovieInfo;
    @BindView(R.id.tv_summary)
    TextView tvSummary;
    @BindView(R.id.rv_horizontal_view)
    RecyclerView rvHorizontalView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    //    final String VIDEO_URL = "http://baobab.wdjcdn.com/14564977406580.mp4";
//    final String VIDEO_URL = "http://cdn.tiaobatiaoba.com/Upload/square/2017-11-02/1509585140_1279.mp4";
//    final String VIDEO_URL = "http://cazaea.com/love.mp4";
//    final String VIDEO_URL = "https://Cazaea.github.io/love.mp4";
    final String VIDEO_URL = "https://Cazaea.coding.me/love.mp4";
//    final String VIDEO_URI = "android.resource://" + this.getPackageName() + "/" + R.raw.love;


    public static Intent getCallingIntent(Context context, String id) {
        Intent intent = new Intent(context, PlayerActivity.class);
        intent.putExtra(INTENT_KEY_MOVIE_ID, id);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        ButterKnife.bind(this);

        // 初始化ActionBar
        initActionBar();
        // 初始化横向RecyclerView
        initRecycler();
        // 获取影视信息
        getMovieDetail(getIntent());
        // 初始化视频播放
        initPlayVideo();

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        GSYVideoManager.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        GSYVideoManager.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        GSYVideoPlayer.releaseAllVideos();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        if (StandardGSYVideoPlayer.backFromWindowFull(this)) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        getMovieDetail(intent);
    }

    /**
     * 初始化ActionBar
     */
    private void initActionBar() {
        toolbar.setTitle("电影详情");
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    /**
     * 初始化横向滑动的RecyclerView
     */
    private void initRecycler() {
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rvHorizontalView.setLayoutManager(layoutManager);
        rvHorizontalView.addItemDecoration(new GridMarginDecoration(
                getResources().getDimensionPixelSize(R.dimen.grid_item_spacing)));
    }

    /**
     * 获取电影详细信息
     */
    private void getMovieDetail(Intent intent) {

        String movieId = intent.getStringExtra(INTENT_KEY_MOVIE_ID);

        if (!TextUtils.isEmpty(movieId)) {
            OkGo.get(DouBanApi.Url_MovieDetail + movieId)
                    .execute(new StringCallback() {

                        @Override
                        public void onBefore(BaseRequest request) {
                            super.onBefore(request);
                        }

                        @Override
                        public void onSuccess(String s, Call call, Response response) {
                            MovieDetail movieDetailBean = new Gson().fromJson(s, MovieDetail.class);
                            if (movieDetailBean != null) {
//                                Picasso.with(PlayerActivity.this).load(movieDetailBean.getImages().getLarge()).into(backdrop);
                                MovieDetail.RatingBean ratingBean = movieDetailBean.getRating();
                                if (ratingBean != null) {
                                    float averageLevel = (float) movieDetailBean.getRating().getAverage();
                                    rbMovieLevel.setRating(averageLevel / 2);
                                    tvMovieLevelInfo.setText(String.format(" %s 分", String.valueOf(averageLevel)));
                                }
                            }

                            // 设置电影名称
                            movieName.setText(movieDetailBean.getTitle());
                            // 加载影片信息
                            loadBaseInfo(movieDetailBean);

                            // 设置电影简介
                            tvSummary.setText(movieDetailBean.getSummary());

                            // 设置演员
                            ArtAdapter artAdapter = new ArtAdapter(PlayerActivity.this, movieDetailBean.getCasts());
                            rvHorizontalView.setAdapter(artAdapter);

                        }

                        @Override
                        public void onCacheSuccess(String s, Call call) {
                            super.onCacheSuccess(s, call);

                            MovieDetail movieDetailBean = new Gson().fromJson(s, MovieDetail.class);
                            MovieDetail.RatingBean ratingBean = movieDetailBean.getRating();
                            if (ratingBean != null) {
                                float averageLevel = (float) movieDetailBean.getRating().getAverage();
                                rbMovieLevel.setRating(averageLevel / 2);
                                tvMovieLevelInfo.setText(String.format(" %s 分", String.valueOf(averageLevel)));
                            }

                            // 设置电影简介
                            tvSummary.setText(movieDetailBean.getSummary());

                            // 设置电影名称
                            movieName.setText(movieDetailBean.getTitle());
                            // 加载影片信息
                            loadBaseInfo(movieDetailBean);

                            // 设置演员
                            ArtAdapter artAdapter = new ArtAdapter(PlayerActivity.this, movieDetailBean.getCasts());
                            rvHorizontalView.setAdapter(artAdapter);

                        }

                        @Override
                        public void onError(Call call, Response response, Exception e) {
                            super.onError(call, response, e);
                        }

                        @Override
                        public void onAfter(String s, Exception e) {
                            super.onAfter(s, e);
                        }
                    });

        }

    }

    /**
     * 加载影片信息
     *
     * @param movieDetailBean
     */
    private void loadBaseInfo(MovieDetail movieDetailBean) {
        StringBuilder stringBuilder = new StringBuilder();
        List<MovieDetail.DirectorsBean> directorsBeanList = movieDetailBean.getDirectors();

        if (directorsBeanList != null && directorsBeanList.size() > 0) {
            stringBuilder.append("导演:");
            boolean isFirst = true;
            for (MovieDetail.DirectorsBean directorsBean : directorsBeanList) {
                if (!isFirst) {
                    stringBuilder.append("/");
                }
                stringBuilder.append(directorsBean.getName());
                isFirst = false;
            }
            stringBuilder.append("\n");
        }

        List<String> genres = movieDetailBean.getGenres();
        stringBuilder = listToString("类型:", genres, stringBuilder);

        stringBuilder.append("上映日期:");
        stringBuilder.append(movieDetailBean.getYear());
        stringBuilder.append("\n");

        List<String> countries = movieDetailBean.getCountries();
        stringBuilder = listToString("制片国家/地区:", countries, stringBuilder);

        List<String> akrs = movieDetailBean.getAka();
        if (akrs != null && akrs.size() > 3) {
            akrs = akrs.subList(0, 2);
        }
        stringBuilder = listToString("又名:", akrs, stringBuilder);

        tvMovieInfo.setText(stringBuilder.toString());
    }

    private StringBuilder listToString(String title, List<String> list, StringBuilder stringBuilder) {
        if (list != null && list.size() > 0) {
            stringBuilder.append(title);
            boolean isFirst = true;
            for (String string : list) {
                if (!isFirst) {
                    stringBuilder.append("/");
                }
                stringBuilder.append(string);
                isFirst = false;
            }
            stringBuilder.append("\n");
        }
        return stringBuilder;
    }

    /**
     * 演员列表适配器
     */
    private class ArtAdapter extends RecyclerViewCommonAdapter<MovieDetail.CastsBean> {

        public ArtAdapter(Context context, List<MovieDetail.CastsBean> data) {
            super(context, data, R.layout.item_cast);
        }

        @Override
        public void onListBindViewHolder(CommonViewHolder holder, int position) {
            final MovieDetail.CastsBean castsBean = getItem(position);
            if (castsBean != null) {
                if (castsBean.getAvatars() != null) {
                    ImageView castImageView = holder.getView(R.id.iv_cast_image);
                    Picasso.with(mContext).load(castsBean.getAvatars().getLarge()).placeholder(android.R.color.white).into(castImageView);
                    //holder.loadImageUrl(mContext, R.id.iv_cast_image, castsBean.getAvatars().getLarge());
                } else {
                    holder.setImageRes(R.id.iv_cast_image, R.drawable.default_large);
                }

                holder.setText(R.id.tv_cast_name, castsBean.getName());
//                holder.itemView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        CelebrityActivity.start(mContext, castsBean.getId());
//                    }
//                });
            }
        }
    }

    /**
     * 初始化视频播放
     */
    public void initPlayVideo() {

        //设置播放url，第一个url，第二个开始缓存，第三个使用默认缓存路径，第四个设置title
        GSYVideoType.setShowType(GSYVideoType.SCREEN_TYPE_FULL);
        videoPlayer.setUp(VIDEO_URL, false, null, "一生所爱-卢冠中");
        //        GSYVideoManager.instance().setTimeOut(4000, true);
        //GSYVideoManager.instance().setVideoType(MainActivity.this, GSYVideoType.SCREEN_TYPE_FULL);
        //非全屏下，不显示title
        videoPlayer.getTitleTextView().setVisibility(View.GONE);
        //非全屏下不显示返回键
        videoPlayer.getBackButton().setVisibility(View.GONE);
        //打开非全屏下触摸效果
        videoPlayer.setIsTouchWiget(false);
        //立即播放
        //videoPlayer.startPlayLogic();
        //开启自动旋转
        videoPlayer.setRotateViewAuto(false);
        //全屏首先横屏
        videoPlayer.setLockLand(true);
        //是否需要全屏动画效果
        videoPlayer.setShowFullAnimation(false);

        //增加封面
        ImageView imageView = new ImageView(this);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setImageResource(R.drawable.img_ask);
        videoPlayer.setThumbImageView(imageView);
        //外部辅助的旋转，帮助全屏
        //orientationUtils = new OrientationUtils(this, videoPlayer);

        videoPlayer.getFullscreenButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //第一个true是否需要隐藏actionbar，第二个true是否需要隐藏statusbar
                videoPlayer.startWindowFullscreen(PlayerActivity.this, true, true);
            }
        });

    }


}

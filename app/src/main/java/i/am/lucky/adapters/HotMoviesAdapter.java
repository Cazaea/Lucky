package i.am.lucky.adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.cazaea.recycler.CommonViewHolder;
import com.cazaea.recycler.RecyclerViewCommonAdapter;
import com.squareup.picasso.Picasso;
import com.tencent.smtt.sdk.TbsVideo;
import com.tencent.smtt.sdk.WebView;

import java.util.ArrayList;
import java.util.List;

import i.am.lucky.R;
import i.am.lucky.activity.video.PlayerActivity;
import i.am.lucky.data.Bean.HotMoviesBean;


/**
 * Created  on 16-6-6.
 * <p/>
 * 正在上映电影 - 数据适配器
 */
public class HotMoviesAdapter extends RecyclerViewCommonAdapter<HotMoviesBean.SubjectsBean> {

    public HotMoviesAdapter(List<HotMoviesBean.SubjectsBean> list, Context context) {
        super(context, list, R.layout.item_hot_movies);
    }

    public void update(List<HotMoviesBean.SubjectsBean> list) {
        addList((ArrayList<HotMoviesBean.SubjectsBean>) list);
    }

    @Override
    public void onListBindViewHolder(CommonViewHolder holder, int position) {
        final HotMoviesBean.SubjectsBean entity = mList.get(position);

        Picasso.with(mContext)
                .load(mList.get(position).getImages().getLarge())
                .placeholder(android.R.color.white)
                .into((ImageView) holder.getView(R.id.photo));

        holder.setText(R.id.tv_movie_name, entity.getTitle());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 跳转具体播放页面
                mContext.startActivity(PlayerActivity.getCallingIntent(mContext, entity.getId()));

                // TBS直接播放页面
                // 判断当前Tbs播放器是否已经可以使用。
//                if (TbsVideo.canUseTbsPlayer(mContext)){
//                    Bundle bundle = new Bundle();
//                    bundle.putString("title","大吉大利，今晚吃鸡。");
                // extraData对象是根据定制需要传入约定的信息，没有需要可以传如null
//                    TbsVideo.openVideo(mContext, "http://flv3.bn.netease.com/tvmrepo/2018/3/H/3/EDCQNA3H3/SD/EDCQNA3H3-mobile.mp4",bundle);
//                }

            }
        });

    }


//    @Override
//    public PhotoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_list_item_home, parent, false);
//        PhotoViewHolder holder = new PhotoViewHolder(view);
//        return holder;
//    }
//
//   @Override
//    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
//        PhotoViewHolder photoViewHolder = (PhotoViewHolder) holder;
//        final TheatersMoive.SubjectsEntity entity = mList.get(position);
//        Glide.with(mContext)
//                .load(mList.get(position).getImages().getLarge())
//                .placeholder(android.R.color.white)
//                .into(photoViewHolder.photo);
//        photoViewHolder.name.setText(entity.getTitle());
//        photoViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mContext.startActivity(MovieDetailActivity.getCallingIntent(mContext, entity.getId()));
//            }
//        });
//    }

//    @Override
//    public int getItemCount() {
//        return mList.size();
//    }

//    final class PhotoViewHolder extends RecyclerView.ViewHolder {
//
//        ImageView photo;
//        TextView name;
//
//        public PhotoViewHolder(View itemView) {
//            super(itemView);
//            photo = (ImageView) itemView.findViewById(R.id.photo);
//            name = (TextView) itemView.findViewById(R.id.tv_movie_name);
//        }
//    }

}

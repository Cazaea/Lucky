package i.am.lucky.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;

/**
 * 作 者： Cazaea
 * 日 期： 2018/4/19
 * 邮 箱： wistorm@sina.com
 */
public class CustomWindow implements PopupWindow.OnDismissListener {

    private PopupWindow mPopupWindow;
    private View mContentView;
    private Context mContext;
    private Activity mActivity;

    public CustomWindow(Builder builder) {
        mContext = builder.context;
        mContentView = LayoutInflater.from(mContext).inflate(builder.layoutResID, null);
        if (builder.width == 0 || builder.height == 0) {
            builder.width = ViewGroup.LayoutParams.WRAP_CONTENT;
            builder.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        }
        mPopupWindow = new PopupWindow(mContentView, builder.width, builder.height, builder.focusable);
        // 需要跟 setBackGroundDrawable 结合
        mPopupWindow.setOutsideTouchable(builder.outsideCancelable);
        mPopupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mPopupWindow.setAnimationStyle(builder.animStyle);

        // 设置背景色
        if (builder.alpha > 0 && builder.alpha < 1) {
            mActivity = builder.activity;
            WindowManager.LayoutParams params = builder.activity.getWindow().getAttributes();
            params.alpha = builder.alpha;
            builder.activity.getWindow().setAttributes(params);
        }

        mPopupWindow.setOnDismissListener(this);
    }

    /**
     * popup 消失
     */
    public void dismiss() {
        if (mPopupWindow != null) {
            mPopupWindow.dismiss();
            if (mActivity != null) {
                WindowManager.LayoutParams params = mActivity.getWindow().getAttributes();
                params.alpha = 1.0f;
                mActivity.getWindow().setAttributes(params); //回复背景色
            }
        }
    }

    /**
     * 根据id获取view
     *
     * @param viewId
     * @return
     */
    public View getItemView(int viewId) {
        if (mPopupWindow != null) {
            return this.mContentView.findViewById(viewId);
        }
        return null;
    }

    /**
     * 根据父布局，显示位置
     *
     * @param rootViewId
     * @param gravity
     * @param x
     * @param y
     * @return
     */
    public CustomWindow showAtLocation(int rootViewId, int gravity, int x, int y) {
        if (mPopupWindow != null) {
            View rootView = LayoutInflater.from(mContext).inflate(rootViewId, null);
            mPopupWindow.showAtLocation(rootView, gravity, x, y);
        }
        return this;
    }

    /**
     * 根据id获取view ，并显示在该view的位置
     *
     * @param targetViewId
     * @param gravity
     * @param offX
     * @param offY
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public CustomWindow showAsLaction(int targetViewId, int gravity, int offX, int offY) {
        if (mPopupWindow != null) {
            View targetView = LayoutInflater.from(mContext).inflate(targetViewId, null);
            mPopupWindow.showAsDropDown(targetView, offX, offY, gravity);
        }
        return this;
    }

    /**
     * 显示在 targetView 的不同位置
     *
     * @param targetView
     * @param gravity
     * @param offX
     * @param offY
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public CustomWindow showAsLaction(View targetView, int gravity, int offX, int offY) {
        if (mPopupWindow != null) {
            mPopupWindow.showAsDropDown(targetView, offX, offY, gravity);
        }
        return this;
    }

    /**
     * 根据id设置焦点监听
     *
     * @param viewId
     * @param listener
     */
    public void setOnFocusListener(int viewId, View.OnFocusChangeListener listener) {
        View view = getItemView(viewId);
        view.setOnFocusChangeListener(listener);
    }

    /**
     * 根据id设置点击事件监听
     *
     * @param viewId
     * @param listener
     */
    public void setOnClickListener(int viewId, View.OnClickListener listener) {
        getItemView(viewId).setOnClickListener(listener);
    }

    /**
     * 监听 dismiss，还原背景色
     */
    @Override
    public void onDismiss() {
        if (mActivity != null) {
            WindowManager.LayoutParams params = mActivity.getWindow().getAttributes();
            params.alpha = 1.0f;
            mActivity.getWindow().setAttributes(params);
        }
    }

    /**
     * builder 类
     */
    public static class Builder {
        private int layoutResID;
        private int width;
        private int height;
        private boolean focusable;
        private boolean outsideCancelable;
        private int animStyle;
        private Context context;
        private Activity activity;
        private float alpha;

        public Builder setContext(Context context) {
            this.context = context;
            return this;
        }

        public Builder setContentView(int layoutResID) {
            this.layoutResID = layoutResID;
            return this;
        }

        public Builder setWidth(int width) {
            this.width = width;
            return this;
        }

        public Builder setHeight(int height) {
            this.height = height;
            return this;
        }

        public Builder setFocusable(boolean focusable) {
            this.focusable = focusable;
            return this;
        }

        public Builder setOutsideCancelable(boolean outsideCancelable) {
            this.outsideCancelable = outsideCancelable;
            return this;
        }

        public Builder setAnim(int animStyle) {
            this.animStyle = animStyle;
            return this;
        }

        public Builder setBackGroudAlpha(Activity activity, float alpha) {
            this.activity = activity;
            this.alpha = alpha;
            return this;
        }

        public CustomWindow builder() {
            return new CustomWindow(this);
        }
    }
}

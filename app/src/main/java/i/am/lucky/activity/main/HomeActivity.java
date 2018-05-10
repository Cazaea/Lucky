package i.am.lucky.activity.main;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.thejoyrun.router.Router;
import com.thejoyrun.router.RouterActivity;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import i.am.lucky.BuildConfig;
import i.am.lucky.R;
import i.am.lucky.app.MainApp;
import i.am.lucky.config.AppConfig;
import i.am.lucky.data.User;
import i.am.lucky.utils.LogUtil;
import i.am.lucky.view.PhotoWindow;

/*
 * @author Cazaea
 * @time 2017/11/19 14:41
 * @mail wistorm@sina.com
 *
 *             ,o%%%%%%%。
 *           ,%%/\%%%%/\%%。
 *          ,%%%\c "" J/%%%。
 * %.       %%%%/ o  o \%%%%
 * '%%.     %%%%\   _  /%%%%
 *  '%%     '%%%%(__Y__)%%%'
 *  //       ;%%%%`\-/%%%%'
 * ((       /  '%%%%%%%%'
 *  \\    .'          )
 *   \\  /     .--,   |
 *    \\/          )| |
 *     \          /_| |__
 *     (____________))))))
 *
 *          攻城狮
 */

@RouterActivity("home")
public class HomeActivity extends AppCompatActivity {

    @BindView(R.id.hello_world)
    TextView helloWorld;
    @BindView(R.id.iv_photo)
    AppCompatImageView ivPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        // 获取数据
        try {
            User user = MainApp.getData().load(User.class, "User");
            helloWorld.setText(user.userInfo + "\n" + BuildConfig.APPLICATION_ID);
            LogUtil.d("数据已拿到。");
        } catch (Exception e) {
            LogUtil.e("暂时没数据。");
        }
        // 保存数据
        savedInfo();

    }

    /**
     * 上传头像
     */
    @OnClick(R.id.iv_photo)
    public void onPhotoClicked() {
        PhotoWindow.show(this);
    }

    /**
     * 处理回调结果
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // 回调成功
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                // 相册选取
                case AppConfig.CODE_SELECT_IMAGE:
//                    try {
//                        cropImage(data.getData(), tag);
//                    } catch (NullPointerException e) {
//                        e.printStackTrace();
//                    }

//                    Bitmap bitmap;
//                    // 外界的程序访问ContentProvider所提供数据 可以通过ContentResolver接口
//                    ContentResolver resolver = getContentResolver();
//
//                    try {
//                        // 获得图片的uri
//                        Uri originalUri = data.getData();
//                        // 显得到bitmap图片
//                        bitmap = MediaStore.Images.Media.getBitmap(resolver, originalUri);
//                        // 这里开始的第二部分，获取图片的路径：
//                        String[] pic_info = {MediaStore.Images.Media.DATA};
//                        // 好像是android多媒体数据库的封装接口，具体的看Android文档
//                        @SuppressWarnings("deprecation")
//                        Cursor cursor = managedQuery(originalUri, pic_info, null, null, null);
//                        // 按我个人理解 这个是获得用户选择的图片的索引值
//                        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//                        // 将光标移至开头 ，这个很重要，不小心很容易引起越界
//                        cursor.moveToFirst();
//                        // 最后根据索引值获取图片路径
//                        String path = cursor.getString(column_index);
////                        iv_photo.setImageURI(originalUri);
////                        File temp = new File(path);
//                        upLoadFile(originalUri, tag);
//
//                    } catch (IOException | NullPointerException e) {
//                        e.printStackTrace();
//                    }

                    ivPhoto.setImageURI(PhotoWindow.imageUri);

                    break;
                // 拍照
                case AppConfig.CODE_TAKE_PHOTO:
//                    File temp = new File(Environment.getExternalStorageDirectory() + "/" + AppConfig.IMAGE_FILE_NAME);
//                    cropImage(temp, tag);

//                    File temp = new File(PhotoWindow.imageFilePath);
                    Bitmap bmp = BitmapFactory.decodeFile(PhotoWindow.imageFilePath);
                    ivPhoto.setImageBitmap(bmp);

//                    upLoadFile(temp, tag);

                    break;
                // 上传文件
                case AppConfig.CODE_CUT_IMAGE:
//                    upLoadFile(imageUri, tag);
                    break;
            }
        }
    }

    /**
     * 跳转页面实验
     */

    @OnClick(R.id.hello_world)
    public void onViewClicked() {
        /*
         * 跳转到网页播放视频
         */
        Router.startActivity(this, AppConfig.ROUTER_HEAD + "://web?url=" + "http://flv3.bn.netease.com/tvmrepo/2018/3/H/3/EDCQNA3H3/SD/EDCQNA3H3-mobile.mp4");
//        Router.startActivity(this, AppConfig.ROUTER_HEAD + "://web?url=" + "http://player.youku.com/embed/XMzk2ODc4NTUy");
        /*
         * 跳转到网页下载
         */
//        Router.startActivity(this, AppConfig.ROUTER_TOTAL_HEAD + "web?url=" + "http://a.app.qq.com/o/simple.jsp?pkgname=com.hxd.zjsmk");
        /*
         * 跳转到登陆页面
         */
//        Router.startActivity(this, AppConfig.ROUTER_TOTAL_HEAD + "login?username=" + "15010267528");

        /*
         * 成功后跳转
         */
        /*final SweetAlertDialog dialog = new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText("你点了我了！");
        dialog.show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                    Router.startActivity(HomeActivity.this, AppConfig.ROUTER_HEAD + "://player");
                }
            }
        }, 2000);*/

    }

    /**
     * 测试保存数据
     */
    public void savedInfo() {
        User user = new User();
        user.userInfo = "你好！";
        MainApp.getData().storeOrUpdate(user, "User");
    }

}

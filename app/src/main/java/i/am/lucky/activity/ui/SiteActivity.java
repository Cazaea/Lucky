package i.am.lucky.activity.ui;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.afollestad.materialcamera.MaterialCamera;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.loader.ImageLoader;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.lzy.imagepicker.view.CropImageView;
import com.squareup.picasso.Picasso;
import com.thejoyrun.router.Router;
import com.thejoyrun.router.RouterActivity;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import i.am.lucky.R;
import i.am.lucky.config.AppConfig;

/**
 * @author Cazaea
 * @time 2017/11/19 15:36
 * @mail wistorm@sina.com
 */
@RouterActivity("site")
public class SiteActivity extends AppCompatActivity {

    @BindView(R.id.btn_image)
    Button btnImage;
    @BindView(R.id.btn_record)
    Button btnRecord;
    @BindView(R.id.btn_permission)
    Button btnPermission;
    @BindView(R.id.btn_unity)
    Button btnUnity;
    @BindView(R.id.btn_guide)
    Button btnGuide;
    @BindView(R.id.btn_ali_pay)
    Button btnAliPay;
    @BindView(R.id.btn_wechat_pay)
    Button btnWechatPay;

    private MaterialCamera materialCamera;
    private final int IMAGE_PICKER = 100;
    private final int CAMERA_RECORD = 6969;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_site);
        ButterKnife.bind(this);

        initImagePicker();
        initMaterialCamera();

        // startAsyncTask();

    }

    @OnClick({R.id.btn_image, R.id.btn_record, R.id.btn_permission, R.id.btn_unity, R.id.btn_guide, R.id.btn_ali_pay, R.id.btn_wechat_pay})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_image:
                Intent intent = new Intent(this, ImageGridActivity.class);
                startActivityForResult(intent, IMAGE_PICKER);
                break;
            case R.id.btn_record:
                materialCamera.start(CAMERA_RECORD);
                break;
            case R.id.btn_permission:
                Toast.makeText(this, "未开发", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_unity:
//                Intent intent2 = new Intent(this, UnityPlayerActivity.class);
//                startActivity(intent2);
                break;
            case R.id.btn_guide:
                Toast.makeText(this, "未开发", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_ali_pay:
                Router.startActivity(this, AppConfig.ROUTER_HEAD + "://scan");
                break;
            case R.id.btn_wechat_pay:
                Router.startActivity(this, AppConfig.ROUTER_HEAD + "://scan");
                break;
        }
    }

    // 初始化ImagePicker
    private void initImagePicker() {
        ImagePicker imagePicker = ImagePicker.getInstance();
        imagePicker.setImageLoader(new PicassoImageLoader());   //设置图片加载器
        imagePicker.setMultiMode(false);
        imagePicker.setShowCamera(true);  //显示拍照按钮
        imagePicker.setCrop(true);        //允许裁剪（单选才有效）
        imagePicker.setSaveRectangle(true); //是否按矩形区域保存
        //imagePicker.setSelectLimit(9);    //选中数量限制
        imagePicker.setStyle(CropImageView.Style.RECTANGLE);  //裁剪框的形状
        imagePicker.setFocusWidth(800);   //裁剪框的宽度。单位像素（圆形自动取宽高最小值）
        imagePicker.setFocusHeight(800);  //裁剪框的高度。单位像素（圆形自动取宽高最小值）
        imagePicker.setOutPutX(1000);//保存文件的宽度。单位像素
        imagePicker.setOutPutY(1000);//保存文件的高度。单位像素
    }


    //测试内存泄漏
    private void startAsyncTask() {
        // 这个AsyncTask是一个匿名内部类，因此他隐式的持有一个外部类
        // 的对象，也就是MainActivity。如果MainActivity在AsyncTask
        // 执行完成前就销毁了，这个activity实例就发生了泄露。
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                SystemClock.sleep(20000); // 休眠20秒
                return null;
            }
        }.execute();
    }

    /*@NeedsPermission({Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void openCamera() {
    }

    *//*@Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        MainActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }*//*


    void cameraOnShow(final PermissionRequest request) {
    }

    @OnPermissionDenied({Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void cameraDenied() {
    }

    @OnNeverAskAgain({Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void cameraNeverAsk() {
    }*/


    public class PicassoImageLoader implements ImageLoader {

        @Override
        public void displayImage(Activity activity, String path, ImageView imageView, int width, int height) {
            Picasso.with(activity)
                    .load(Uri.fromFile(new File(path)))
                    .error(R.mipmap.grid_camera)
                    .placeholder(R.mipmap.grid_camera)
                    .into(imageView);

        }

        @Override
        public void displayImagePreview(Activity activity, String path, ImageView imageView, int width, int height) {
            Picasso.with(activity)
                    .load(Uri.fromFile(new File(path)))
                    .error(R.mipmap.grid_camera)
                    .placeholder(R.mipmap.grid_camera)
//                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imageView);
        }

        @Override
        public void clearMemoryCache() {
            //这里是清除缓存的方法,根据需要自己实现
        }
    }

    private void initMaterialCamera() {
        File saveDir = null;
        saveDir = new File(Environment.getExternalStorageDirectory(), "MaterialCamera");
        saveDir.mkdirs();
        materialCamera = new MaterialCamera(this);
        materialCamera
                .allowRetry(false)                        //是否显示重拍按钮
                .autoSubmit(false)                        //是否允许用户在录制后播放视频
                .saveDir(saveDir)                    //拍摄的内容的储存地址
                .primaryColorAttr(R.attr.colorPrimary)  //相机配色，建议遵从app style
                .showPortraitWarning(true)               //如果用户按纵向方向的记录，是否显示警告
                .defaultToFrontFacing(false)            //是否默认为前置摄像头
                .retryExits(true)                         //如果为true，播放屏幕中的“重试”按钮将退出相机，而不是返回录像，并且播放界面按返回也会直接退出相机
                .restartTimerOnRetry(false)                //如果为true，则当用户在播放中轻按“重试”时，倒计时器将重置为0
                .continueTimerInPlayback(false)         //如果为true，倒计时器将在播放过程中继续向下，而不是暂停。
                .videoEncodingBitRate(1024000)          //设置视频大小
                .audioEncodingBitRate(50000)            // 设置音频大小
                .videoFrameRate(24)                     //设置视频fps值
                .qualityProfile(MaterialCamera.QUALITY_HIGH)//设置质量配置文件，手动设置比特率或帧速率与其他设置将覆盖单个质量配置文件设置
                .videoPreferredHeight(720)                //设置录制视频的首选高度
                .videoPreferredAspect(4f / 3f)            //为录制的视频输出设置首选宽高比
                .maxAllowedFileSize(1024 * 1024 * 20)    //将最大文件大小设置为5MB，如果文件达到此限制，录制将停止。请记住，FAT文件系统的文件大小限制为4GB。
                .iconRecord(R.drawable.mcam_action_capture)//设置用于开始录制的按钮的自定义图标
                .iconStop(R.drawable.mcam_action_stop)  //为用于停止录制的按钮设置自定义图标
                .iconFrontCamera(R.drawable.mcam_camera_front)//设置用于切换到前置摄像头的按钮的自定义图标
                .iconRearCamera(R.drawable.mcam_camera_rear)//设置用于切换到后置摄像头的按钮的自定义图标
                .iconPlay(R.drawable.evp_action_play)   //设置用于开始播放的自定义图标
                .iconPause(R.drawable.evp_action_pause) // 设置用于暂停播放的自定义图标
                .iconRestart(R.drawable.evp_action_restart)//设置用于重新开始播放的自定义图标
                .labelRetry(R.string.video_retry)        //为用于重试记录的按钮设置自定义按钮标签（如果可用）
                .labelConfirm(R.string.video_ok)  //为用于确认/提交录音的按钮设置自定义按钮标签
                .autoRecordWithDelaySec(3)                 //摄像机将在5秒倒计时后自动开始录制。这将最初禁用前后摄像头之间的切换。
                //.autoRecordWithDelayMs(5000)            //与上面相同，用毫秒而不是秒表示。
                .audioDisabled(false);                   //设置为true可记录没有任何音频的视频
    }

    private void testPermisson() {
        Toast.makeText(this, "权限成功", Toast.LENGTH_SHORT).show();
    }


}

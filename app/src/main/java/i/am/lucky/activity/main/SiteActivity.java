package i.am.lucky.activity.main;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

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
import i.am.lucky.activity.guide.GuideActivity;
import i.am.lucky.activity.wechat.ShootingActivity;
import i.am.lucky.config.AppConfig;
import i.am.lucky.utils.LogUtil;

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
    @BindView(R.id.iv_photo)
    ImageView ivPhoto;

    // 权限申请自定义码
    private final int PERMISSION_REQUEST_CODE = 100;
    // 图片选择自定义码
    private final int IMAGE_PICKER_CODE = 1001;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_site);
        ButterKnife.bind(this);

        initImagePicker();

    }

    @OnClick({R.id.btn_image, R.id.btn_record, R.id.btn_permission, R.id.btn_unity, R.id.btn_guide, R.id.btn_ali_pay, R.id.btn_wechat_pay})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_image:
                Intent intent = new Intent(this, ImageGridActivity.class);
                startActivityForResult(intent, IMAGE_PICKER_CODE);
                break;
            case R.id.btn_record:
//                Router.startActivity(this, AppConfig.ROUTER_TOTAL_HEAD + "shoot");
                getPermissions();
                break;
            case R.id.btn_permission:
                Toast.makeText(this, "未开发", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_unity:
//                Intent intent2 = new Intent(this, UnityPlayerActivity.class);
//                startActivity(intent2);
                break;
            case R.id.btn_guide:
//                Router.startActivity(this, AppConfig.ROUTER_TOTAL_HEAD + "guide");
                startActivity(new Intent(SiteActivity.this, GuideActivity.class));
                break;
            case R.id.btn_ali_pay:
                Router.startActivity(this, AppConfig.ROUTER_TOTAL_HEAD + "shoot");
                break;
            case R.id.btn_wechat_pay:
                Router.startActivity(this, AppConfig.ROUTER_TOTAL_HEAD + "shoot");
                break;
        }
    }

    // 初始化ImagePicker
    private void initImagePicker() {
        ImagePicker imagePicker = ImagePicker.getInstance();
        imagePicker.setImageLoader(new PicassoImageLoader());   //设置图片加载器
        imagePicker.setMultiMode(false);
        imagePicker.setShowCamera(true);                        //显示拍照按钮
        imagePicker.setCrop(true);                              //允许裁剪（单选才有效）
        imagePicker.setSaveRectangle(true);                     //是否按矩形区域保存
//        imagePicker.setSelectLimit(9);                        //选中数量限制
        imagePicker.setStyle(CropImageView.Style.RECTANGLE);    //裁剪框的形状
        imagePicker.setFocusWidth(800);                         //裁剪框的宽度。单位像素（圆形自动取宽高最小值）
        imagePicker.setFocusHeight(800);                        //裁剪框的高度。单位像素（圆形自动取宽高最小值）
        imagePicker.setOutPutX(1000);                           //保存文件的宽度。单位像素
        imagePicker.setOutPutY(1000);                           //保存文件的高度。单位像素
    }

    public class PicassoImageLoader implements ImageLoader {

        @Override
        public void displayImage(Activity activity, String path, ImageView imageView, int width, int height) {
            Picasso.with(activity)
                    .load(Uri.fromFile(new File(path)))
                    .error(R.mipmap.grid_camera)
                    .into(imageView);

        }

        @Override
        public void displayImagePreview(Activity activity, String path, ImageView imageView, int width, int height) {
            Picasso.with(activity)
                    .load(Uri.fromFile(new File(path)))
                    .error(R.mipmap.grid_camera)
                    .into(imageView);
        }

        @Override
        public void clearMemoryCache() {
            //这里是清除缓存的方法,根据需要自己实现
        }
    }

    /**
     * 获取权限
     */
    private void getPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager
                    .PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager
                            .PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager
                            .PERMISSION_GRANTED) {
                startActivityForResult(new Intent(SiteActivity.this, ShootingActivity.class), IMAGE_PICKER_CODE);
            } else {
                //不具有获取权限，需要进行权限申请
                ActivityCompat.requestPermissions(SiteActivity.this, new String[]{
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.RECORD_AUDIO,
                        Manifest.permission.CAMERA}, PERMISSION_REQUEST_CODE);
            }
        } else {
            startActivityForResult(new Intent(SiteActivity.this, ShootingActivity.class), IMAGE_PICKER_CODE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_OK) {
            switch (requestCode) {
                case 101:
                    LogUtil.d("picture");
                    String path = data.getStringExtra("path");
                    ivPhoto.setImageBitmap(BitmapFactory.decodeFile(path));
                    break;
                case 102:
                    LogUtil.d("video");
                    String path1 = data.getStringExtra("path");
                    break;
                case 103:
                    Toast.makeText(this, "请检查相机权限~", Toast.LENGTH_SHORT).show();
                case 1001:
                    Toast.makeText(this, "选择图片成功！", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }

    @TargetApi(23)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            int size = 0;
            if (grantResults.length >= 1) {
                int writeResult = grantResults[0];
                // 读写内存权限
                boolean writeGranted = writeResult == PackageManager.PERMISSION_GRANTED;//读写内存权限
                if (!writeGranted) {
                    size++;
                }
                // 录音权限
                int recordPermissionResult = grantResults[1];
                boolean recordPermissionGranted = recordPermissionResult == PackageManager.PERMISSION_GRANTED;
                if (!recordPermissionGranted) {
                    size++;
                }
                // 相机权限
                int cameraPermissionResult = grantResults[2];
                boolean cameraPermissionGranted = cameraPermissionResult == PackageManager.PERMISSION_GRANTED;
                if (!cameraPermissionGranted) {
                    size++;
                }
                if (size == 0) {
                    startActivityForResult(new Intent(SiteActivity.this, ShootingActivity.class), IMAGE_PICKER_CODE);
                } else {
                    Toast.makeText(this, "请到设置-权限管理中开启", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }


}

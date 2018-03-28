package i.am.lucky.activity.wechat;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.cazaea.camera.CameraView;
import com.cazaea.camera.listener.ClickListener;
import com.cazaea.camera.listener.ErrorListener;
import com.cazaea.camera.listener.CameraListener;
import com.cazaea.camera.util.DeviceUtil;
import com.cazaea.camera.util.FileUtil;
import com.thejoyrun.router.RouterActivity;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;

import i.am.lucky.utils.LogUtil;

import i.am.lucky.R;

/**
 * 仿微信拍摄页面
 */
@RouterActivity("shoot")
public class ShootingActivity extends AppCompatActivity {

    @BindView(R.id.camera_view)
    CameraView cameraView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shooting);
        ButterKnife.bind(this);
        // 初始化录屏View
        initShootView();
    }

    @Override
    protected void onStart() {
        super.onStart();
        // 屏幕全屏处理
//        initFullScreen();
    }

    @Override
    protected void onResume() {
        super.onResume();
        cameraView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        cameraView.onPause();
    }

    /**
     * 全屏显示
     */
    private void initFullScreen() {
        if (Build.VERSION.SDK_INT >= 19) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        } else {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(option);
        }
    }

    /**
     * 初始化
     */
    private void initShootView() {
        //设置视频保存路径
        cameraView.setSaveVideoPath(Environment.getExternalStorageDirectory().getPath() + File.separator + "JCamera");
        cameraView.setFeatures(CameraView.BUTTON_STATE_BOTH);
        cameraView.setTip("轻触拍照，长按摄像");
        cameraView.setMediaQuality(CameraView.MEDIA_QUALITY_MIDDLE);
        cameraView.setErrorListener(new ErrorListener() {
            @Override
            public void onError() {
                //错误监听
                LogUtil.e("camera error");
                Intent intent = new Intent();
                setResult(103, intent);
                finish();
            }

            @Override
            public void AudioPermissionError() {
                Toast.makeText(ShootingActivity.this, "给点录音权限可以?", Toast.LENGTH_SHORT).show();
            }
        });

        //JCameraView监听
        cameraView.setCameraListener(new CameraListener() {
            @Override
            public void captureSuccess(Bitmap bitmap) {
                //获取图片bitmap
//                Log.i("JBaseCamera", "bitmap = " + bitmap.getWidth());
                String path = FileUtil.saveBitmap("JCamera", bitmap);
                Intent intent = new Intent();
                intent.putExtra("path", path);
                setResult(101, intent);
                finish();
            }

            @Override
            public void recordSuccess(String url, Bitmap firstFrame) {
                //获取视频路径
                String path = FileUtil.saveBitmap("JCamera", firstFrame);
                LogUtil.d("url = " + url + ", Bitmap = " + path);
                Intent intent = new Intent();
                intent.putExtra("path", path);
                setResult(101, intent);
                finish();
            }
        });

        cameraView.setLeftClickListener(new ClickListener() {
            @Override
            public void onClick() {
                ShootingActivity.this.finish();
            }
        });
        cameraView.setRightClickListener(new ClickListener() {
            @Override
            public void onClick() {
                Toast.makeText(ShootingActivity.this, "Right", Toast.LENGTH_SHORT).show();
            }
        });

        LogUtil.d(DeviceUtil.getDeviceModel());
    }

}

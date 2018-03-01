package i.am.lucky.scan;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;
import android.widget.Toast;

import com.google.zxing.Result;
import com.google.zxing.client.android.AnimViewCallback;
import com.google.zxing.client.android.BaseCaptureActivity;
import com.google.zxing.client.android.ViewfinderView;
import com.thejoyrun.router.RouterActivity;

import i.am.lucky.R;

/**
 * 默认的扫描界面
 */
@RouterActivity("default")
public class DefaultCaptureActivity extends BaseCaptureActivity {

    private static final String TAG = DefaultCaptureActivity.class.getSimpleName();

    private SurfaceView surfaceView;
    private ViewfinderView viewfinderView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capture);
        surfaceView = (SurfaceView) findViewById(R.id.preview_view);
        viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);
    }

    @Override
    protected void onResume() {
        super.onResume();
        viewfinderView.setCameraManager(cameraManager);
    }

    @Override
    public SurfaceView getSurfaceView() {
        return (surfaceView == null) ? (SurfaceView) findViewById(R.id.preview_view) : surfaceView;
    }

    @Override
    public AnimViewCallback getViewfinderHolder() {
        return (viewfinderView == null) ? (ViewfinderView) findViewById(R.id.viewfinder_view) : viewfinderView;
    }

    @Override
    public void dealDecode(Result rawResult, Bitmap barcode, float scaleFactor) {
        Log.i(TAG, "dealDecode ~~~~~ " + rawResult.getText() + " " + barcode + " " + scaleFactor);
        playBeepSoundAndVibrate();
        Toast.makeText(this, rawResult.getText(), Toast.LENGTH_SHORT).show();
//        对此次扫描结果不满意可以调用
//        reScan();
    }
}

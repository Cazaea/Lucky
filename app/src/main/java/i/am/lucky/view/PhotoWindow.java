package i.am.lucky.view;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.Toast;

import java.io.File;

import i.am.lucky.R;
import i.am.lucky.activity.main.HomeActivity;
import i.am.lucky.config.AppConfig;

/**
 * Created by Lister on 2017-06-12.
 * 自定义 popupWindow 类
 */

public class PhotoWindow extends PopupWindow {

    // PopupWindow 菜单布局
    private View mMenuView;
    // 上下文参数
    private Activity activity;
    // 拍照回调
    private View.OnClickListener takeOnClick;
    // 选择图片回调
    private View.OnClickListener chooseOnClick;

    public static String imageFilePath;
    // 图片Uri
    public static Uri imageUri;

    /**
     * 构造器
     *
     * @param activity 上下文对象
     */
    public PhotoWindow(Activity activity) {
        super(activity);
        this.activity = activity;
        // 导入布局
        initView();
    }

    /**
     * PopupWindow 布局引入
     */
    private void initView() {

        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.layout_photo_popup_window, null);
        Button btn_camera = (Button) mMenuView.findViewById(R.id.popup_btn_camera);
        Button btn_photo = (Button) mMenuView.findViewById(R.id.popup_btn_select);
        Button btn_cancel = (Button) mMenuView.findViewById(R.id.popup_btn_cancel);

        btn_camera.setOnClickListener(getTakeOnClick());
        btn_photo.setOnClickListener(getChooseOnClick());
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        // 导入布局
        this.setContentView(mMenuView);

        // 设置动画效果
        this.setAnimationStyle(R.style.PopupAnimStyle);
        this.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        this.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);

        // 设置可触
        this.setFocusable(true);
        ColorDrawable dw = new ColorDrawable(0x0000000);
        this.setBackgroundDrawable(dw);

        // 单击 popupWindow 以外即关闭
        mMenuView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int height = mMenuView.findViewById(R.id.ll_pop).getTop();
                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < height) {
                        dismiss();
                    }
                }
                return true;
            }
        });
    }

    /**
     * 拍照点击事件
     *
     * @return
     */
    private View.OnClickListener getTakeOnClick() {
        takeOnClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 权限申请
                if (ContextCompat.checkSelfPermission(activity,
                        Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                        || ContextCompat.checkSelfPermission(activity,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    // 权限还没有授予，需要在这里写申请权限的代码
                    ActivityCompat.requestPermissions(activity,
                            new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 222);
                } else {
                    // 权限已经申请，直接拍照
                    dismiss();
                    takePhoto();
                }
                Toast.makeText(activity, "拍照", Toast.LENGTH_SHORT).show();
            }
        };
        return takeOnClick;
    }

    /**
     * 选择图片点击事件
     *
     * @return
     */
    private View.OnClickListener getChooseOnClick() {
        chooseOnClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 权限申请
                if (ContextCompat.checkSelfPermission(activity,
                        Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                        || ContextCompat.checkSelfPermission(activity,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    // 权限还没有授予，需要在这里写申请权限的代码
                    ActivityCompat.requestPermissions(activity,
                            new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 333);
                } else {
                    // 权限已经申请，直接拍照
                    dismiss();
                    selectPhoto();
                }
                Toast.makeText(activity, "选择图片", Toast.LENGTH_SHORT).show();
            }
        };
        return chooseOnClick;
    }

    /**
     * 拍照,判断系统
     */
    private void takePhoto() {

        imageFilePath = Environment.getExternalStorageDirectory() + "/" + AppConfig.IMAGE_FILE_NAME;
        File pictureFile = new File(Environment.getExternalStorageDirectory(), AppConfig.IMAGE_FILE_NAME);

        Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        Uri pictureUri;
        // 判断当前系统,如果是7.0及以上系统,从provider中拿数据
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            captureIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            pictureUri = FileProvider.getUriForFile(activity, AppConfig.PROVIDER_FILE_NAME, pictureFile);
        } else {
            pictureUri = Uri.fromFile(pictureFile);
        }
        // 去拍照
        captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, pictureUri);
        activity.startActivityForResult(captureIntent, AppConfig.CODE_TAKE_PHOTO);
    }

    /**
     * 图库选择
     */
    private void selectPhoto() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");

//        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");

        // 判断系统中是否有处理该 Intent 的 Activity
        if (intent.resolveActivity(activity.getPackageManager()) != null) {
            activity.startActivityForResult(intent, AppConfig.CODE_SELECT_IMAGE);
        } else {
            Toast.makeText(activity, "未找到图片查看器", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 裁剪图片
     */
    public void cropImage(File inputFile) {
        // 内存已挂载
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            String storage = Environment.getExternalStorageDirectory().getPath();
            File dirFile = new File(storage + "/bigIcon");
            File file = new File(dirFile, System.currentTimeMillis() + ".jpg");
            // 将 uri 传出，方便设置到视图中
            imageUri = Uri.fromFile(file);

            // 开始切割
            Intent intent = new Intent("com.android.camera.action.CROP");
            intent.setDataAndType(getImageContentUri(activity, inputFile), "image/*");
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            // 使图片处于可裁剪状态
            intent.putExtra("crop", "true");
            // 裁剪框支持缩放
            intent.putExtra("scale", true);

            // 裁剪框比例
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
            // 输出图片大小,同上
            intent.putExtra("outputX", 600);
            intent.putExtra("outputY", 600);
            // 是否需要人脸识别
            intent.putExtra("noFaceDetection", true);

            // 不直接返回数据
            intent.putExtra("return-data", false);
            // 设置裁剪区域的形状，默认为矩形，也可设置为原形
            intent.putExtra("circleCrop", false);
            // 传递原图路径
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
            // intent.putExtra("noFaceDetection", true); // no face detection
            activity.startActivityForResult(intent, AppConfig.CODE_CUT_IMAGE);

        }

    }

    /**
     * 裁剪图片
     */
    public void cropImage(Uri uri) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            String storage = Environment.getExternalStorageDirectory().getPath();
            File dirFile = new File(storage + "/bigIcon");
            File file = new File(dirFile, System.currentTimeMillis() + ".jpg");
            // 将 uri 传出，方便设置到视图中
            imageUri = Uri.fromFile(file);

            // 开始切割
            Intent intent = new Intent("com.android.camera.action.CROP");
            intent.setDataAndType(uri, "image/*");
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            // 使图片处于可裁剪状态
            intent.putExtra("crop", "true");
            // 裁剪框支持缩放
            intent.putExtra("scale", true);

            // 裁剪框比例
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
            // 输出图片大小,同上
            intent.putExtra("outputX", 600);
            intent.putExtra("outputY", 600);
            // 是否需要人脸识别
            intent.putExtra("noFaceDetection", true);

            // 不直接返回数据
            intent.putExtra("return-data", false);
            // 设置裁剪区域的形状，默认为矩形，也可设置为原形
            intent.putExtra("circleCrop", false);
            // 传递原图路径
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());

            activity.startActivityForResult(intent, AppConfig.CODE_CUT_IMAGE);
//
        }

    }

    /**
     * 获取图片ContentUri
     */
    private Uri getImageContentUri(Context context, File imageFile) {
        String filePath = imageFile.getAbsolutePath();
        Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new String[]{MediaStore.Images.Media._ID}, MediaStore.Images.Media.DATA + "=? ", new String[]{filePath}, null);

        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
            Uri baseUri = Uri.parse("content://media/external/images/media");
            return Uri.withAppendedPath(baseUri, "" + id);
        } else {
            if (imageFile.exists()) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, filePath);
                return context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            } else {
                return null;
            }
        }
    }

    /**
     * 弹出拍照选择框
     */
    public static void show(Activity activity){
        View rootView = LayoutInflater.from(activity).inflate(R.layout.activity_main, null);
        new PhotoWindow(activity).showAtLocation(rootView, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }


}

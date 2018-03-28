package com.cazaea.camera.listener;

import android.graphics.Bitmap;

/**
 * =====================================
 * 作者: Cazaea
 * 日期: 2017/6/6
 * 邮箱: wistorm@sina.com
 * 描述:
 * =====================================
 */
public interface CameraListener {

    void captureSuccess(Bitmap bitmap);

    void recordSuccess(String url, Bitmap firstFrame);

}

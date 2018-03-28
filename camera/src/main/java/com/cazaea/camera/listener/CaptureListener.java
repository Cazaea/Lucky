package com.cazaea.camera.listener;

/**
 * =====================================
 * 作者: Cazaea
 * 日期: 2017/6/6
 * 邮箱: wistorm@sina.com
 * 描述:
 * =====================================
 */
public interface CaptureListener {
    void takePictures();

    void recordShort(long time);

    void recordStart();

    void recordEnd(long time);

    void recordZoom(float zoom);

    void recordError();
}

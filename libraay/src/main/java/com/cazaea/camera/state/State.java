package com.cazaea.camera.state;

import android.view.Surface;
import android.view.SurfaceHolder;

import com.cazaea.camera.CameraInterface;

/**
 * =====================================
 * 作者: Cazaea
 * 日期: 2017/6/6
 * 邮箱: wistorm@sina.com
 * 描述:
 * =====================================
 */
public interface State {

    void start(SurfaceHolder holder, float screenProp);

    void stop();

    void focus(float x, float y, CameraInterface.FocusCallback callback);

    void switchCamera(SurfaceHolder holder, float screenProp);

    void restart();

    void capture();

    void record(Surface surface, float screenProp);

    void stopRecord(boolean isShort, long time);

    void cancel(SurfaceHolder holder, float screenProp);

    void confirm();

    void zoom(float zoom, int type);

    void flash(String mode);
}

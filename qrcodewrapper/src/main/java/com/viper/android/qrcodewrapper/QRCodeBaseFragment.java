package com.viper.android.qrcodewrapper;

import android.app.Activity;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import cn.bingoogolapple.qrcode.core.QRCodeView;

import static android.content.Context.VIBRATOR_SERVICE;

public abstract class QRCodeBaseFragment extends BaseDialogFragment implements QRCodeView.Delegate {

    private static final String TAG = "QRCodeBaseFragment";

    private QRCodeView mQRCodeView;
    protected OnScanListener mOnScanListener;

    @LayoutRes
    public abstract int getLayoutId();

    @IdRes
    public abstract int getViewId();

    @NonNull
    private QRCodeView getQRCodeView() {
        if (mQRCodeView == null) {
            throw new RuntimeException("qrcode view can not be null");
        }
        return mQRCodeView;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(getLayoutId(), container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mQRCodeView = view.findViewById(getViewId());
        mQRCodeView.setDelegate(this);
    }

    @Override
    public void onStart() {
        super.onStart();

        getQRCodeView().startCamera(); // 打开后置摄像头开始预览，但是并未开始识别
//        getQRCodeView().startCamera(Camera.CameraInfo.CAMERA_FACING_FRONT); // 打开前置摄像头开始预览，但是并未开始识别

        getQRCodeView().startSpotAndShowRect(); // 显示扫描框，并开始识别
    }

    @Override
    public void onStop() {
        getQRCodeView().stopCamera(); // 关闭摄像头预览，并且隐藏扫描框
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        getQRCodeView().onDestroy(); // 销毁二维码扫描控件
        super.onDestroyView();
    }

    private void vibrate() {
        Activity activity = getActivity();
        if (activity != null && !activity.isFinishing() && !activity.isDestroyed()) {
            Vibrator vibrator = (Vibrator) getActivity().getSystemService(VIBRATOR_SERVICE);
            vibrator.vibrate(200);
        }
    }

    @Override
    public void onScanQRCodeSuccess(String result) {
        vibrate();
        boolean processed = false;
        if (mOnScanListener != null) {
            processed = mOnScanListener.onCapture(result);
        }
        if (!processed) {
            getQRCodeView().startSpot(); // 继续识别
        } else {
            //Dismiss
            dismissAllowingStateLoss();
        }
    }

    @Override
    public void onCameraAmbientBrightnessChanged(boolean isDark) {
        // 这里是通过修改提示文案来展示环境是否过暗的状态，接入方也可以根据 isDark 的值来实现其他交互效果
        String tipText = getQRCodeView().getScanBoxView().getTipText();
        String ambientBrightnessTip = "\n环境过暗，请打开闪光灯";
        if (isDark) {
            if (!tipText.contains(ambientBrightnessTip)) {
                getQRCodeView().getScanBoxView().setTipText(tipText + ambientBrightnessTip);
            }
        } else {
            if (tipText.contains(ambientBrightnessTip)) {
                tipText = tipText.substring(0, tipText.indexOf(ambientBrightnessTip));
                getQRCodeView().getScanBoxView().setTipText(tipText);
            }
        }
    }

    @Override
    public void onScanQRCodeOpenCameraError() {
        Log.e(TAG, "打开相机出错");
        if (mOnScanListener != null) {
            mOnScanListener.onError();
        }
    }

    public void setOnScanListener(OnScanListener onScanListener) {
        mOnScanListener = onScanListener;
    }
}

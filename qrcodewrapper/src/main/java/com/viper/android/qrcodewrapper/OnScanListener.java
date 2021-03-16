package com.viper.android.qrcodewrapper;

public interface OnScanListener {

    boolean onCapture(String result);

    void onError();
}

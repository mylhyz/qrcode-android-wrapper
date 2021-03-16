package com.viper.android.qrcodewrapper;

public class ZXingFragment extends QRCodeBaseFragment {

    @Override
    public int getLayoutId() {
        return R.layout.fragment_zxing;
    }

    @Override
    public int getViewId() {
        return R.id.zxing_view;
    }
}

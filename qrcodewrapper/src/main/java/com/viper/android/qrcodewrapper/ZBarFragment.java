package com.viper.android.qrcodewrapper;

public class ZBarFragment extends QRCodeBaseFragment {

    @Override
    public int getLayoutId() {
        return R.layout.fragment_zbar;
    }

    @Override
    public int getViewId() {
        return R.id.zbar_view;
    }
}

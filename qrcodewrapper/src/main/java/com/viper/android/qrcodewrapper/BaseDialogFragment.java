package com.viper.android.qrcodewrapper;

import android.app.DialogFragment;
import android.app.FragmentManager;


public abstract class BaseDialogFragment extends DialogFragment {

    public void showSafe(FragmentManager manager) {
        try {
            String tagName = this.getClass().getSimpleName();
            super.show(manager, tagName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

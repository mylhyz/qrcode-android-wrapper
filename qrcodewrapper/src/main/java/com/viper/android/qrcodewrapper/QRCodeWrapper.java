package com.viper.android.qrcodewrapper;

import android.app.Activity;
import android.app.FragmentManager;

import java.lang.ref.WeakReference;

public final class QRCodeWrapper {

    public static class Wrapper {
        WeakReference<Activity> mActivityWeakRef;
        private boolean mUseZBar = false;
        private OnScanListener mOnScanListener = null;

        private Wrapper(Activity activity) {
            mActivityWeakRef = new WeakReference<>(activity);
        }

        public Wrapper chooseImpl(boolean useZBar) {
            mUseZBar = useZBar;
            return this;
        }

        public Wrapper withListener(final OnScanListener listener) {
            mOnScanListener = listener;
            return this;
        }

        public void start() {
            Activity activity = mActivityWeakRef.get();
            if (activity != null && !activity.isFinishing() && !activity.isDestroyed()) {
                FragmentManager fm = activity.getFragmentManager();
                if (fm != null) {
                    if (mUseZBar) {
                        ZBarFragment fragment = new ZBarFragment();
                        fragment.setOnScanListener(mOnScanListener);
                        fragment.showSafe(fm);
                    } else {
                        ZXingFragment fragment = new ZXingFragment();
                        fragment.setOnScanListener(mOnScanListener);
                        fragment.showSafe(fm);
                    }
                }
            }
        }
    }

    public static Wrapper withActivity(Activity activity) {
        return new Wrapper(activity);
    }
}

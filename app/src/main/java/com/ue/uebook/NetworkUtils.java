package com.ue.uebook;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import static android.content.Context.CONNECTIVITY_SERVICE;

public class NetworkUtils {

    @SuppressLint("StaticFieldLeak")
    private static NetworkUtils networkUtil = null;
    private Context mCtx;

    private NetworkUtils(Context mCtx) {
        this.mCtx = mCtx;
    }

    public static NetworkUtils getInstance(Context mCtx) {
        if (networkUtil == null)
            networkUtil = new NetworkUtils(mCtx);

        return networkUtil;
    }
    public boolean isConnectingToInternet() {
        if (mCtx != null) {
            ConnectivityManager connectivityManager = (ConnectivityManager) mCtx
                    .getSystemService(CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            return networkInfo != null && networkInfo.isConnected();
        }

        return false;
    }
}

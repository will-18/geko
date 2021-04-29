package com.digitalgeko.login.Internet;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Internet {
/////esta actividad es para verificar si hay internet Documentacion:https://developer.android.com/reference/android/net/ConnectivityManager
    public static boolean isOnline(Context context)
    {
        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(
                Context.CONNECTIVITY_SERVICE);
            if (connectivityManager != null) {
                NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
                    if (networkInfo != null)
                    {
                    return networkInfo.isConnected();
                    }
                }
            return false;

    }
}

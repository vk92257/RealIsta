package com.example.myapplication.utils;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.myapplication.NoInternetConnection_activity;


public class NetworkChangeReceiver extends BroadcastReceiver {

    private static final String LOG_TAG = "NetworkChangeReceiver";
    private boolean isConnected = false;

    //    private NoInternetConnectionActivity  activity = new NoInternetConnectionActivity();
    public static boolean open = false;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.v(LOG_TAG, "Receieved notification about network status");
        Log.e("netcheck", " onReceive onStart: register");
        isNetworkAvailable(context);
    }

    private boolean isNetworkAvailable(Context context) {
        Log.e("netcheck", " NetworkChange onStart: register");
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        if (!isConnected) {
                            Log.e(LOG_TAG, "Now you are connected to Internet!");
//                            Toast.makeText(context, "Internet availablle via Broadcast receiver", Toast.LENGTH_SHORT).show();
                            isConnected = true;

                            Log.e(LOG_TAG, "isNetworkAvailable:open-> " + open);

                            if (open) {
                                Log.e("NetworkChangeReceiver", "isNetworkAvailable:t true " + open);
                                Intent intent = new Intent("Nointernetclass");
                                LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                            }

                        }
                        return true;
                    }
                }
            }
        }
        Log.v(LOG_TAG, "You are not connected to Internet!");
//        Toast.makeText(context, "Internet NOT availablle via Broadcast receiver", Toast.LENGTH_SHORT).show();
        opennointernetactivity(context);

        isConnected = false;
        return false;
    }

    private void opennointernetactivity(Context context) {


//        Intent intent = new Intent(context, activity.getClass());
//        context.startActivity(intent);

//        NoInternetConnectionActivity.startActivity((Activity) context);
        NoInternetConnection_activity.startActivity((Activity) context);


    }


}
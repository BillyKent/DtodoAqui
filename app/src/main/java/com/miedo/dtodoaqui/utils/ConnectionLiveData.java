package com.miedo.dtodoaqui.utils;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.NetworkRequest;
import android.os.Build;

import androidx.lifecycle.LiveData;

import static android.content.Context.CONNECTIVITY_SERVICE;

public class ConnectionLiveData extends LiveData<Boolean> {

    private final Context context;

    private final ConnectivityManager connectivityManager;

    private ConnectivityManager.NetworkCallback connectivityManagerCallback;

    public ConnectionLiveData(Context context) {
        this.context = context;
        connectivityManager = (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);

    }


    @Override
    protected void onActive() {
        super.onActive();
        updateConnection();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            connectivityManager.registerDefaultNetworkCallback(getConnectivityManagerCallback());
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            lollipopNetworkAvailableRequest();
        } else {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                context.registerReceiver(networkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
            }
        }
    }

    @Override
    protected void onInactive() {
        super.onInactive();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            connectivityManager.unregisterNetworkCallback(connectivityManagerCallback);
        } else {
            context.unregisterReceiver(networkReceiver);
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void lollipopNetworkAvailableRequest() {
        NetworkRequest.Builder builder = new NetworkRequest.Builder();
        builder
                .addTransportType(android.net.NetworkCapabilities.TRANSPORT_CELLULAR)
                .addTransportType(android.net.NetworkCapabilities.TRANSPORT_WIFI);
        connectivityManager.registerNetworkCallback(builder.build(), getConnectivityManagerCallback());
    }


    private ConnectivityManager.NetworkCallback getConnectivityManagerCallback() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            connectivityManagerCallback = new ConnectivityManager.NetworkCallback() {
                @Override
                public void onAvailable(Network network) {
                    super.onAvailable(network);
                    postValue(true);
                }

                @Override
                public void onLost(Network network) {
                    super.onLost(network);
                    postValue(false);
                }
            };

            return connectivityManagerCallback;
        } else {
            throw new IllegalAccessError("Should not happened");
        }
    }

    private BroadcastReceiver networkReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateConnection();
        }
    };

    private void updateConnection() {

        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        postValue((activeNetwork != null && activeNetwork.isConnected()));
    }

}
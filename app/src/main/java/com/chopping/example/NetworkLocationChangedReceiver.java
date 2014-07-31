package com.chopping.example;

import com.chopping.bus.BusProvider;
import com.chopping.example.bus.WifiEvent;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.text.TextUtils;

/**
 * Receiver to detect status of Wifi.
 */
public final class NetworkLocationChangedReceiver extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {
		NetworkInfo info = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
		if(info!=null && TextUtils.equals(info.getTypeName(), "WIFI")) {
			BusProvider.getBus().post(new WifiEvent(info.isConnected()));
		}
	}
}

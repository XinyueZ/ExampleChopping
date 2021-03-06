package com.chopping.example;

import com.chopping.example.bus.WifiEvent;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.text.TextUtils;
import de.greenrobot.event.EventBus;

/**
 * Receiver to detect status of Wifi.
 */
public final class WifiNetworkChangedReceiver extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {
		NetworkInfo info = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
		if(info!=null && TextUtils.equals(info.getTypeName(), "WIFI")) {
			EventBus.getDefault().post(new WifiEvent(info.isConnected()));
		}
	}
}

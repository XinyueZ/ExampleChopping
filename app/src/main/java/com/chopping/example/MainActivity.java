package com.chopping.example;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.chopping.activities.BaseActivity;
import com.chopping.application.BasicPrefs;
import com.chopping.example.bus.WifiEvent;
import com.chopping.example.data.DOUser;
import com.chopping.example.data.DOUsers;
import com.chopping.net.GsonRequestTask;

public class MainActivity extends BaseActivity implements
		android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener {
	private static final int LAYOUT = R.layout.activity_main;
	private ListView mListView;
	private Button mLoadUsersV;
	private ArrayAdapter<String> mAdapter;
	private SwipeRefreshLayout mReloadSRL;
	private WifiManager mWifiManager;
	private MenuItem mWifiMenuItem;


	// ------------------------------------------------
	// Subscribes, event-handlers
	// ------------------------------------------------


	public void onEvent(DOUsers e) {
		List<DOUser> users = e.getUserList();
		mAdapter.clear();
		for (DOUser user : users) {
			mAdapter.add(user.getName());
		}
		mLoadUsersV.setEnabled(false);
		mLoadUsersV.setText(R.string.lbl_users_loaded);
		mReloadSRL.setRefreshing(false);

		setHasShownDataOnUI(true);
	}


	public void onEvent(WifiEvent _e) {
		ActivityCompat.invalidateOptionsMenu(this);
		mWifiMenuItem.setEnabled(true);
		mWifiMenuItem.setTitle(
				_e.isEnable() ? R.string.menu_wifi_is_on : R.string.menu_wifi_is_off);
	}

	// ------------------------------------------------

	public void loadUser(View v) {
		new GsonRequestTask<DOUsers>(
				getApplicationContext(),
				Request.Method.GET,
				Prefs.getInstance().getApiUsers(),
				DOUsers.class).execute();
	}

	public void openOther(View v) {
		OtherActivity.showInstance(this);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(LAYOUT);
		mWifiManager = (WifiManager) getSystemService(
				Context.WIFI_SERVICE);
		mListView = (ListView) findViewById(R.id.users_lv);
		mLoadUsersV = (Button) findViewById(R.id.load_users_btn);
		mReloadSRL = (SwipeRefreshLayout) findViewById(R.id.reload_srl);
		mReloadSRL.setColorSchemeResources(R.color.color1, R.color.color2, R.color.color3, R.color.color4);
		mReloadSRL.setOnRefreshListener(this);
		mListView.setAdapter(mAdapter = new ArrayAdapter<String>(
				this,
				android.R.layout.simple_expandable_list_item_1,
				new ArrayList<String>()));
	}


	@Override
	public void onRefresh() {
		loadUser(null);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		mWifiMenuItem = menu.findItem(R.id.action_wifi_settings);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.action_wifi_settings:
				boolean isEnable = mWifiManager.isWifiEnabled();
				mWifiManager.setWifiEnabled(!isEnable);
				item.setTitle(
						isEnable ? R.string.menu_wifi_is_off : R.string.menu_wifi_is_on);
				item.setEnabled(false);
				break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected BasicPrefs getPrefs() {
		return Prefs.getInstance();
	}

	@Override
	protected void onAppConfigLoaded() {
		StringBuilder stringBuilder = new StringBuilder();
		TextView textView = (TextView) findViewById(R.id.output_tv);
		String newLine = System.getProperty("line.separator");
		Prefs prefs = Prefs.getInstance();
		stringBuilder.
				append("property: ").append(prefs.getOneProperty()).append(newLine).
				append("float: ").append(prefs.getFloatValue()).append(newLine).
				append("int: ").append(prefs.getIntValue()).append(newLine).
				append("long: ").append(prefs.getLongValue()).append(newLine).
				append("bool: ").append(prefs.getBooleanValue()).append(newLine);
		textView.setText(stringBuilder.toString());
		mLoadUsersV.setEnabled(true);
	}

	@Override
	protected void onAppConfigIgnored() {
		onAppConfigLoaded();
	}

	@Override
	protected void onNetworkError() {
		mReloadSRL.setRefreshing(false);
	}

	@Override
	protected void onReload() {
		mReloadSRL.setRefreshing(true);
		loadUser(null);
	}
}

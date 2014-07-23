package com.chopping.example;

import android.app.Application;

import com.chopping.net.TaskHelper;


public final class App extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		init();

	}

	private void init() {
		Prefs.createInstance(this);
		TaskHelper.init(getApplicationContext());
	}
}

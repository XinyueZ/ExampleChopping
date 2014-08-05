package com.chopping.example;

import com.chopping.application.BasicPrefs;

import android.content.Context;

/**
 * Store app and device information.
 *
 * @author Chris.Xinyue Zhao
 */
public final class Prefs extends BasicPrefs {

	/**
	 * The Instance.
	 */
	private static Prefs sInstance;

	private Prefs() {
		super(null);
	}

	/**
	 * Created a DeviceData storage.
	 *
	 * @param context
	 * 		A context object.
	 */
	private Prefs(Context context) {
		super(context);
	}

	/**
	 * Singleton method.
	 *
	 * @param context
	 * 		A context object.
	 *
	 * @return single instance of DeviceData
	 */
	public static Prefs createInstance(Context context) {
		if (sInstance == null) {
			synchronized (Prefs.class) {
				if (sInstance == null) {
					sInstance = new Prefs(context);
				}
			}
		}
		return sInstance;
	}

	/**
	 * Singleton getInstance().
	 *
	 * @return The instance of Prefs.
	 */
	public static Prefs getInstance() {
		return sInstance;
	}

	// ----------------------------------------------------------
	// Description: Application's preference.
	//
	// Below defines set/get methods for preference of the whole
	// App, inc. data that was stored in app's config or local.
	// ----------------------------------------------------------

	public String getOneProperty() {
		return getString("name", null);
	}

	public String getApiUsers() {
		return getString("api_url", null);
	}

	public float getFloatValue() {
		return getFloat("float_number", -1f);
	}

	public long getLongValue() {
		return getLong("long_number", -1l);
	}

	public int getIntValue() {
		return getInt("int_number", -1);
	}

	public boolean getBooleanValue() {
		return getBoolean("bool_value", true);
	}
}

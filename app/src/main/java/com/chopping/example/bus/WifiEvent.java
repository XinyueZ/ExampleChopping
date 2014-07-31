package com.chopping.example.bus;

/**
 * Event when Wifi status has been changed.
 */
public final class WifiEvent {
	/**True, if current wifi-status is ON.*/
	private boolean mEnable;

	/**
	 * Constructor of WifiEvent.
	 * @param _isEnable Current wifi-status. True is ON, false is OFF.
	 */
	public WifiEvent(boolean _isEnable) {
		mEnable = _isEnable;
	}

	/**Get current wifi-status. True is ON, false is OFF.*/
	public boolean isEnable() {
		return mEnable;
	}
}

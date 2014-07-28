package com.chopping.example.data;

import com.google.gson.annotations.SerializedName;

/**
 * Example data-set for a User.
 */
public final class DOUser {
	@SerializedName("name")
	private String name;

	public DOUser(String _name) {
		name = _name;
	}

	public String getName() {
		return name;
	}
}

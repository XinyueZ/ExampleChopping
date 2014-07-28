package com.chopping.example.data;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Example data-set for Users
 */
public final class DOUsers {
	@SerializedName("users")
	private List<DOUser> mUserList;

	public DOUsers(List<DOUser> _userList) {
		mUserList = _userList;
	}

	public List<DOUser> getUserList() {
		return mUserList;
	}
}

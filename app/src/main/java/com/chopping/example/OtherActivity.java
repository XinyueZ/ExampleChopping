package com.chopping.example;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.chopping.activities.BaseActivity;
import com.chopping.application.BasicPrefs;
import com.chopping.bus.ApplicationConfigurationDownloadedEvent;
import com.chopping.example.data.DOUser;
import com.chopping.example.data.DOUsers;
import com.chopping.fragments.BaseFragment;
import com.chopping.net.GsonRequestTask;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;


public class OtherActivity extends BaseActivity {
	private static final int LAYOUT = R.layout.activity_other;
	private static final int ERR_HANDLER_CONTAINER = R.id.container;

	// ------------------------------------------------
	// Subscribes, event-handlers
	// ------------------------------------------------
	@Subscribe
	public void onApplicationConfigurationDownloaded(ApplicationConfigurationDownloadedEvent e) {

	}
	// ------------------------------------------------

	/**
	 * Show instance of {@link com.chopping.example.OtherActivity}.
	 *
	 * @param cxt
	 * 		{@link android.content.Context}.
	 */
	public static void showInstance(Context cxt) {
		Intent i = new Intent(cxt, OtherActivity.class);
		cxt.startActivity(i);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(LAYOUT);
		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(ERR_HANDLER_CONTAINER, ErrorPlayerFragment.newInstance(this, ERR_HANDLER_CONTAINER))
					.commit();
		}
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		setErrorHandlerAvailable(false);
	}

	@Override
	protected BasicPrefs getPrefs() {
		return Prefs.getInstance();
	}

	/**
	 * A {@link com.chopping.example.OtherActivity.ErrorPlayerFragment} demonstrates error-handling.
	 */
	public static class ErrorPlayerFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {
		private static final int LAYOUT = R.layout.fragment_other;
		private ListView mListView;
		private ArrayAdapter<String> mAdapter;
		private SwipeRefreshLayout mReloadSRL;

		//------------------------------------------------
		//Subscribes, event-handlers
		//------------------------------------------------

		@Subscribe
		public void onDOUsersLoaded(DOUsers e) {
			List<DOUser> users = e.getUserList();
			mAdapter.clear();
			for (DOUser user : users) {
				mAdapter.add(user.getName());
			}
			mReloadSRL.setRefreshing(false);
		}


		@Subscribe
		public void onVolleyError(VolleyError e) {
			mReloadSRL.setRefreshing(false);
		}

		//------------------------------------------------

		/**
		 * Initialize an instance of {@link com.chopping.example.OtherActivity.ErrorPlayerFragment} to demonstrate
		 * error-handling.
		 *
		 * @param cxt
		 * 		{@link android.content.Context}.
		 * @param errLayoutId
		 * 		{@link int} id of a container where an {@link com.chopping.fragments.ErrorHandlerFragment} will be
		 * 		inflated.
		 *
		 * @return
		 */
		public static ErrorPlayerFragment newInstance(Context cxt, @IdRes int errLayoutId) {
			Bundle args = new Bundle();
			args.putInt(EXTRAS_ERR_LAYOUT_CONTAINER, errLayoutId);
			return (ErrorPlayerFragment) ErrorPlayerFragment.instantiate(cxt, ErrorPlayerFragment.class.getName(),
					args);
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
		                         Bundle savedInstanceState) {
			return super.onCreateView(inflater,
					(ViewGroup)inflater.inflate(LAYOUT, null), savedInstanceState);
		}

		@Override
		public void onViewCreated(View view, Bundle savedInstanceState) {
			super.onViewCreated(view, savedInstanceState);
			setErrorHandlerAvailable(true);
			mListView = (ListView) view.findViewById(R.id.users_lv);
			mReloadSRL = (SwipeRefreshLayout) view.findViewById(R.id.reload_srl);
			mReloadSRL.setColorSchemeResources(R.color.color1, R.color.color2, R.color.color3, R.color.color4);
			mReloadSRL.setOnRefreshListener(this);
			mListView.setAdapter(mAdapter = new ArrayAdapter<String>(
					getActivity(),
					android.R.layout.simple_expandable_list_item_1,
					new ArrayList<String>()));
		}

		@Override
		protected BasicPrefs getPrefs() {
			return Prefs.getInstance();
		}

		@Override
		public void onRefresh() {
			loadUser(null);
		}

		public void loadUser(View v) {
			new GsonRequestTask<DOUsers>(
					getActivity().getApplicationContext(),
					Request.Method.GET,
					Prefs.getInstance().getApiUsers(),
					DOUsers.class).execute();
		}

	}
}

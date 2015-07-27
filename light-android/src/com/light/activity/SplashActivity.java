package com.light.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.lidroid.xutils.db.sqlite.WhereBuilder;
import com.lidroid.xutils.exception.DbException;
import com.light.R;
import com.light.app.LightApplication;
import com.light.bean.FriendBean;
import com.light.bean.UserBean;
import com.light.util.ACache;
import com.light.util.LibraryUtils;
import com.light.util.SessionUtils;
import com.light.util.TDevice;

public class SplashActivity extends BaseActivity {
	private static String TAG = "SplashActivity";
	boolean isFirstIn = false;
	private static final long SPLASH_DELAY_MILLIS = 1000;
	private static final String SHAREDPREFERENCES_NAME = "first_pref";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LayoutInflater layoutinflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
		View view = layoutinflater.inflate(R.layout.activity_splash, null);
		setContentView(R.layout.activity_splash);
		super.onCreate(savedInstanceState, R.layout.activity_splash, TAG);
		Log.d(TAG, "startActivity------>setContentView");
		SharedPreferences preferences = getSharedPreferences(
				SHAREDPREFERENCES_NAME, MODE_PRIVATE);
		isFirstIn = preferences.getBoolean("isFirstIn", true);

		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				Intent intent = new Intent();
				if (!isFirstIn) {
					intent.setClass(SplashActivity.this, LoginActivity.class);
					UserBean userBean = (UserBean) ACache.get(
							LightApplication.getInstance()).getAsObject(
							"userBean");
					if (userBean != null) {
						intent.setClass(SplashActivity.this, HomeActivity.class);
						SessionUtils.getInstance().setUserBean(userBean);
						LibraryUtils.setAliasAndTags(context, userBean.getId());
						if (TDevice.getNetworkType() != 0) {
							try {
								dbUtils.delete(
										FriendBean.class,
										WhereBuilder.b("user_id", "=",
												userBean.getId()));
							} catch (DbException e) {
								LibraryUtils.onError(context, e.getMessage());
							}
						}
					}

				} else {
					intent.setClass(SplashActivity.this, WelcomeActivity.class);
				}
				startActivity(intent);
				Log.d(TAG, "startActivity");
				overridePendingTransition(R.anim.push_left_in,
						R.anim.push_left_out);
				finish();
			}

		}, SPLASH_DELAY_MILLIS);
	}
}

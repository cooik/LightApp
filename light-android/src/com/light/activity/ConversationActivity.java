package com.light.activity;

import io.rong.imkit.RongIM;
import io.rong.imkit.fragment.ConversationFragment;
import io.rong.imlib.RongIMClient.ConnectCallback;
import io.rong.imlib.RongIMClient.ErrorCode;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.light.R;
import com.light.constant.SysConst;
import com.light.util.PreferencesUtils;
import com.light.util.RongCloudEvent;
import com.light.util.SessionUtils;

public class ConversationActivity extends BaseFragmentActivity {

	public static final String TAG = "ConversationActivity";
	
	private TextView mTextView;
	
	@ViewInject(R.id.back)
	private ImageView mBack;
	
	private InputMethodManager imm ;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState, R.layout.conversation);
		
		imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		
		mTextView = (TextView) findViewById(R.id.title);

		if (PreferencesUtils.getBoolean(context, SysConst.EXIT_APP)) {

			String token = PreferencesUtils.getString(context,
					SysConst.RONG_TOKEN);
			RongIM.connect(token, new ConnectCallback() {

				@Override
				public void onError(ErrorCode arg0) {
					Log.d(TAG, arg0.getMessage() + "");
				}

				@Override
				public void onSuccess(String arg0) {
					Log.d(TAG, "success");
					ConversationFragment fragment = (ConversationFragment) getSupportFragmentManager()
							.findFragmentByTag("ConversationFragment");
					if (fragment == null) {
						fragment = new ConversationFragment();
					}
					FragmentTransaction ft = getSupportFragmentManager()
							.beginTransaction();
					ft.replace(R.id.ll_conversation, fragment,
							"ConversationFragment").commit();

					RongCloudEvent.getInstance().setOtherListener();
					
					
					mTextView.setText(getIntent().getData().getQueryParameter("title"));
				}

				@Override
				public void onTokenIncorrect() {
					Log.d(TAG, "onTokenIncorrect");
				}
			});
		} else {
			ConversationFragment fragment = (ConversationFragment) getSupportFragmentManager()
					.findFragmentByTag("ConversationFragment");
			if (fragment == null) {
				fragment = new ConversationFragment();
			}
			FragmentTransaction ft = getSupportFragmentManager()
					.beginTransaction();
			ft.replace(R.id.ll_conversation, fragment, "ConversationFragment")
					.commit();

			RongCloudEvent.getInstance().setOtherListener();
			
			mTextView.setText(getIntent().getData().getQueryParameter("title"));

		}
	}
	
	@Override
	public void onBackPressed() {
		// 隐藏输入框
		if (imm.isActive()) {
			imm.hideSoftInputFromWindow(getCurrentFocus()
					.getWindowToken(), 0);
		}
		SessionUtils.getInstance().setNeedRefresh(true);

		super.onBackPressed();
	}

	@OnClick(R.id.back)
	private void backAction(View v){
		onBackPressed();
	}
}

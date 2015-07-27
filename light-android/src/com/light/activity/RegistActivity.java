package com.light.activity;

import java.util.Random;

import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import cn.smssdk.SMSSDK;

import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.light.R;
import com.light.bean.ResponseBean;
import com.light.fragment.RegistAddInfoFragment;
import com.light.fragment.RegistValidFragment;
import com.light.network.Constant;
import com.light.presenter.IUserRegistPresenter;
import com.light.presenter.impl.UserRegistPresenterImpl;
import com.light.util.RegexUtil;
import com.light.util.SMSUtils;
import com.light.util.StringUtils;
import com.light.util.ToastUtils;

public class RegistActivity extends BaseFragmentActivity implements Callback {

	private static final String TAG = "RegistActivity";

	@ViewInject(R.id.btn_phone_regist)
	private Button phoneRegist;

	@ViewInject(R.id.btn_email_regist)
	private Button emailRegist;

	@ViewInject(R.id.et_phone_num)
	private EditText phoneEditText;
	

	Handler handler;

	public IUserRegistPresenter registPresenter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState, R.layout.activity_regist);
		handler = new Handler(this);
		SMSUtils.getInstance().initSdk(context, handler);

		registPresenter = new UserRegistPresenterImpl(context);
	}

	private String code;

	/**
	 * 手机注册
	 * 
	 * @param view
	 */
	@OnClick(R.id.btn_phone_regist)
	private void phoneRegist(View view) {
		// RegisterPage registerPage = new RegisterPage();
		// registerPage.setRegisterCallback(new EventHandler() {
		// public void afterEvent(int event, int result, Object data) {
		// // 解析注册结果
		// if (result == SMSSDK.RESULT_COMPLETE) {
		// Log.d(TAG, "SMSSDK.RESULT_COMPLETE");
		// @SuppressWarnings("unchecked")
		// HashMap<String, Object> phoneMap = (HashMap<String, Object>) data;
		// String country = (String) phoneMap.get("country");
		// String phone = (String) phoneMap.get("phone");
		//
		// code = phone;
		// registPresenter.registCode(phone,handler);
		// // 提交用户信息
		// registerUser(country, phone);
		//
		// }
		// }
		// });
		// registerPage.show(context);

		// 自定义UI
		String phone = phoneEditText.getText().toString();
		if (!StringUtils.isBlank(phone)) {
			if(RegexUtil.isMobileNo(phone)){
				SMSSDK.getVerificationCode("86", phone);
				code = phone;
				registPresenter.registCode(code,handler);
			}else{
				ToastUtils.show(this, "请输入正确的手机号码");
			}
		} else {
			ToastUtils.show(this, "手机号不能为空");
		}
	}

	// 提交用户信息
	private void registerUser(String country, String phone) {
		Random rnd = new Random();
		int id = Math.abs(rnd.nextInt());
		String uid = String.valueOf(id);
		String nickName = "SmsSDK_User_" + uid;
		String avatar = SMSUtils.AVATARS[id % 12];
		SMSSDK.submitUserInfo(uid, nickName, avatar, country, phone);
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}

//	@OnClick(R.id.btn_email_regist)
//	private void emailRegist(View view) {
//		EmailFragment fragment = new EmailFragment(handler);
//		getSupportFragmentManager().beginTransaction()
//				.replace(R.id.ll_regist_main, fragment, "email_fragment")
//				.addToBackStack(null).commit();
//	}

	private int userId;


	@Override
	public boolean handleMessage(Message msg) {
		switch (msg.what) {
		case Constant.WHAT_VALID_EMAIL_SUCCESS:
			userId = msg.arg1;
			changeFragment(R.id.regit_phone_fragment, new RegistAddInfoFragment(R.layout.regist_addinfo_layout,userId));
			break;
		case Constant.WHAT_VALID_PHONE_SUCCESS:
			int event = msg.arg1;
			int result = msg.arg2;
			Object data = msg.obj;
			Log.d(TAG, "event:"+event+" result:" + result);
			if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
				// 短信注册成功后，返回MainActivity,然后提示新好友
				if (result == SMSSDK.RESULT_COMPLETE) {
					changeFragment(R.id.regit_phone_fragment, new RegistAddInfoFragment(R.layout.regist_addinfo_layout,userId));
				} else {
					((Throwable) data).printStackTrace();
				}
			}
			break;
		case Constant.CODE_SUCCESS:
			ResponseBean response = (ResponseBean) msg.obj;
			if (Constant.REGIST_URL.equals(response.getRequestUrl())) {
				Log.d(TAG, "user_id:" + response.getUser_id());
				userId = response.getUser_id();
				if(response.getResult_code()==Constant.CODE_SUCCESS){
//				registPresenter.validCode(userId, "00000000");// 手机校验
				changeFragment(R.id.regit_phone_fragment, new RegistValidFragment(userId,code,true));
				}else{
					ToastUtils.show(context, response.getResult_msg());
				}
			} else if (Constant.ADDINFO_URL.equals(response.getRequestUrl())) {
				if (response.getResult_code() == Constant.CODE_SUCCESS) {
					ToastUtils.show(context, "注册成功");
					this.finish();
				}
			}
			break;
		}

		return false;
	}


	@Override
	protected void onDestroy() {
		SMSUtils.getInstance().unregisterAllEventHandler();
		super.onDestroy();
	}

	public void changeFragment(int resId, Fragment fragment) {
		if(fragment instanceof RegistValidFragment){
			((RegistValidFragment) fragment).setHandler(handler);
		}
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		ft.setCustomAnimations(R.anim.push_left_in, R.anim.push_left_out);
		ft.replace(resId, fragment, fragment.getClass().getSimpleName())
				.addToBackStack(null).commit();
	}
}

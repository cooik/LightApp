package com.light.activity;

import java.util.HashMap;
import java.util.Map;

import android.os.Handler.Callback;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.Preference;
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
import com.light.R.layout;
import com.light.bean.ResponseBean;
import com.light.fragment.ForgetNextFragment;
import com.light.fragment.RegistValidFragment;
import com.light.network.CallBack;
import com.light.network.Constant;
import com.light.presenter.IUserForgetPswPresenter;
import com.light.presenter.IUserRegistPresenter;
import com.light.presenter.impl.UserForgetPresenterImpl;
import com.light.presenter.impl.UserRegistPresenterImpl;
import com.light.util.LibraryUtils;
import com.light.util.PreferencesUtils;
import com.light.util.RegexUtil;
import com.light.util.SMSUtils;
import com.light.util.StringUtils;
import com.light.util.ToastUtils;

public class ForgetPswActivity extends BaseFragmentActivity implements
		Callback, CallBack {
	public static String TAG = "ForgetPswActivity";
	@ViewInject(R.id.next)
	Button btn_next;
	@ViewInject(R.id.cancle)
	Button btn_cancle;
	@ViewInject(R.id.et_input)
	EditText et_input;
	String requestUrl;

	Handler handler;

	public IUserForgetPswPresenter forgetPswPresenter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState, R.layout.activity_forgetpsw);

		handler = new Handler(this);
		SMSUtils.getInstance().initSdk(context, handler);
		forgetPswPresenter = new UserForgetPresenterImpl(context, null);
	}

	private String phone;

	@OnClick(R.id.next)
	private void next(View view) {
		String inputstr = et_input.getText().toString();
		if (!StringUtils.isBlank(inputstr)) {
			if (RegexUtil.isMobileNo(inputstr)) {
				phone = inputstr;
				verifyUser(phone);
			} else if (RegexUtil.isEmail(inputstr)) {

			} else {
				ToastUtils.show(this, "请输入正确的手机号码");
			}
		} else {
			ToastUtils.show(this, "手机号不能为空");
		}
	}

	@Override
	public boolean handleMessage(Message msg) {
		return false;
	}

	public void verifyUser(String userId) {
		this.requestUrl = Constant.VERIFY_USERID_URL;
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("code", userId);
		String inputJson = LibraryUtils.formatJson(map);
		LibraryUtils.httpPost(Constant.VERIFY_USERID_URL, inputJson, this);
	}

	@Override
	public void onNetworkFinished(boolean isSuccess, String reuslt) {
		if (isSuccess) {
			ResponseBean response = LibraryUtils.formatJsonToTargetClass(
					reuslt, ResponseBean.class);
			int resultCode = response.getResult_code();
			if (resultCode == Constant.USER_INEXISTENCE) {
				ToastUtils.show(this, "账号不存在");
			} else {
				PreferencesUtils.putInt(this, "user_id", response.getUser_id());
				Log.d("Phone", phone + "");
				changeFragment(R.id.forget_fragment, new ForgetNextFragment(
						phone, response.getUser_id()));
			}

		}
	}

	public void changeFragment(int resId, Fragment fragment) {
		if (fragment instanceof RegistValidFragment) {
			((RegistValidFragment) fragment).setHandler(handler);
		}
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		ft.setCustomAnimations(R.anim.push_left_in, R.anim.push_left_out);
		ft.replace(resId, fragment, fragment.getClass().getSimpleName())
				.addToBackStack(null).commit();
	}

}

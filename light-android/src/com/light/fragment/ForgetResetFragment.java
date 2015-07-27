package com.light.fragment;

import java.util.HashMap;
import java.util.Map;

import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.light.R;
import com.light.activity.LoginActivity;
import com.light.bean.ResponseBean;
import com.light.network.CallBack;
import com.light.network.Constant;
import com.light.util.LibraryUtils;
import com.light.util.PreferencesUtils;
import com.light.util.ToastUtils;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class ForgetResetFragment extends BaseFragment implements CallBack {
	int UserId;
	@ViewInject(R.id.btn_back)
	Button back_to_login;
	@ViewInject(R.id.btn_cancle)
	Button cancle;
	@ViewInject(R.id.new_psw)
	EditText new_psw;
	@ViewInject(R.id.re_psw)
	EditText re_psw;
	String str_new_psw;
	EditText str_re_pse;
	boolean issame = false;
	String phone;

	public ForgetResetFragment(int userId, String phone) {
		UserId = userId;
		this.phone = phone;

	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		Log.d("Phone", "ForgetResetFragment" + phone + "");
		return super.onCreateView(inflater, container, savedInstanceState,
				R.layout.fragment_forget_resetpsw, "ForgetResetFragment");
	}

	@OnClick(R.id.btn_cancel)
	private void cancle(View view) {
		getActivity().finish();
	}

	@OnClick(R.id.btn_back)
	private void toLogin(View view) {
		if (new_psw.getText().toString().equals(re_psw.getText().toString())) {
			resetPsw();
		} else {
			ToastUtils.show(getActivity(), "两次密码输入不同");
		}
	}

	private void resetPsw() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("user_id", UserId);
		map.put("pwd", new_psw.getText().toString());
		String inputJson = LibraryUtils.formatJson(map);
		LibraryUtils.httpPost(Constant.RESETPSW_URL, inputJson, this);
	}

	@Override
	public void onNetworkFinished(boolean isSuccess, String reuslt) {
		if (isSuccess) {
			ResponseBean response = LibraryUtils.formatJsonToTargetClass(
					reuslt, ResponseBean.class);
			int resultCode = response.getResult_code();
			Log.d("ForgetResetFragment", Integer.toString(resultCode));
			if (resultCode == Constant.CODE_SUCCESS) {
				ToastUtils.show(getActivity(), "修改密码成功");
				PreferencesUtils.putString(context, "user_code", phone);
				gotoLogin();
			}
		} else {
			ToastUtils.show(getActivity(), "修改密码失败");
		}
	}

	private void gotoLogin() {
		Intent intent = new Intent(getActivity(), LoginActivity.class);
		startActivity(intent);
	}
}

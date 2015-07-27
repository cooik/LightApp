package com.light.model.impl;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.os.Handler;
import android.widget.Toast;

import com.light.bean.ResponseBean;
import com.light.model.IUserModel;
import com.light.network.CallBack;
import com.light.network.Constant;
import com.light.util.LibraryUtils;
import com.light.util.PreferencesUtils;

public class UserModelImpl implements IUserModel, CallBack {

	private Context context;
	private String code;
	private String passwd;
	private String requestUrl;

	private Handler handler;

	public UserModelImpl(Context context) {
		LibraryUtils.initHttpClient();
		this.context = context;
	}

	@Override
	public void login(String name, String passwd) {
		this.requestUrl = Constant.LOGIN_URL;
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("code", name);
		map.put("pwd", passwd);
		this.code = name;
		this.passwd = passwd;
		String inputJson = LibraryUtils.formatJson(map);
		LibraryUtils.httpPost(Constant.LOGIN_URL, inputJson, this);
	}

	@Override
	public void onNetworkFinished(boolean isSuccess, String reuslt) {

		if (isSuccess) {
			ResponseBean response = LibraryUtils.formatJsonToTargetClass(
					reuslt, ResponseBean.class);
			response.setRequestUrl(requestUrl);
			int resultCode = response.getResult_code();
			if (resultCode == Constant.CODE_SUCCESS) {
				parseSuccessResult(response);
			}
			if (handler != null) {
				handler.obtainMessage(Constant.CODE_SUCCESS, response)
						.sendToTarget();
			} else {
				LibraryUtils.sendEvent(response);
			}
			response = null;
		} else {
			ResponseBean response = new ResponseBean();
			response.setResult_code(Constant.CODE_ERROR);
			response.setResult_msg(reuslt);
			LibraryUtils.sendEvent(response);
			response = null;
		}
	}

	@Override
	public String[] queryUserCodeAndPwdFromLocal() {
		String[] result = new String[2];
		result[0] = PreferencesUtils.getString(context, "user_code", "");
		result[1] = PreferencesUtils.getString(context, "user_pwd", "");
		return result;
	}

	@Override
	public void registCode(String code, Handler... handler) {
		if (handler != null && handler.length > 0)
			this.handler = handler[0];
		this.requestUrl = Constant.REGIST_URL;
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("code", code);
		this.code = code;
		String inputJson = LibraryUtils.formatJson(map);
		LibraryUtils.httpPost(Constant.REGIST_URL, inputJson, this);
	}

	private void parseSuccessResult(ResponseBean response) {
		if (Constant.LOGIN_URL.equals(this.requestUrl)) {
			PreferencesUtils.putString(context, "user_code", code);
			PreferencesUtils.putString(context, "user_pwd", passwd);
		} else if (Constant.REGIST_URL.equals(this.requestUrl)) {
		} else if (Constant.ADDINFO_URL.equals(this.requestUrl)) {
			PreferencesUtils.putString(context, "user_code", code);
		} else if (Constant.VERIFY_USERID_URL.equals(this.requestUrl)) {
			Toast.makeText(context, "忘记密码", Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void validCode(int userId, String authCode) {
		this.requestUrl = Constant.VALID_URL;
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("user_id", userId);
		map.put("val_code", authCode);
		String inputJson = LibraryUtils.formatJson(map);
		LibraryUtils.httpPost(Constant.VALID_URL, inputJson, this);
	}

	@Override
	public void addInfo(Map<String, Object> infoMap) {
		this.requestUrl = Constant.ADDINFO_URL;
		String inputJson = LibraryUtils.formatJson(infoMap);
		LibraryUtils.httpPost(Constant.ADDINFO_URL, inputJson, this);
	}

	@Override
	public void otherLogin(Map<String, Object> params) {
		this.requestUrl = Constant.OTHER_LOGIN_URL;
		String inputJson = LibraryUtils.formatJson(params);
		LibraryUtils.httpPost(Constant.OTHER_LOGIN_URL, inputJson, this);
	}

	@Override
	public void resetPsw(int userId, String psw) {

	}

}

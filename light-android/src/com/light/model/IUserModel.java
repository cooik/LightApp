package com.light.model;

import java.util.Map;

import android.os.Handler;

public interface IUserModel {

	public void login(String name, String passwd);

	public String[] queryUserCodeAndPwdFromLocal();

	public void registCode(String code, Handler... handler);

	public void validCode(int userId, String authCode);

	public void addInfo(Map<String, Object> infoMap);

	public void otherLogin(Map<String, Object> params);

	public void resetPsw(int userId, String psw);

}

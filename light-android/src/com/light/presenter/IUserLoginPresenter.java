package com.light.presenter;

import java.util.Map;

public interface IUserLoginPresenter {
	
	public void login();
	
	public void queryUserCodeAndPwdFromLocal();
	
	public void otherLogin(Map<String,Object> params);

}

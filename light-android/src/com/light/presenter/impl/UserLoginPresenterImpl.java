package com.light.presenter.impl;

import java.util.Map;

import android.content.Context;

import com.light.model.IUserModel;
import com.light.model.impl.UserModelImpl;
import com.light.presenter.IUserLoginPresenter;
import com.light.util.StringUtils;
import com.light.view.IUserLoginView;

public class UserLoginPresenterImpl implements IUserLoginPresenter{
	
	private IUserModel userModel;
	private IUserLoginView userLoginView;
	
	public UserLoginPresenterImpl(IUserLoginView userLoginView,Context context){
		this.userLoginView = userLoginView;
		userModel = new UserModelImpl(context);
	}

	@Override
	public void login() {
		 userModel.login(userLoginView.getUserName(), userLoginView.getUserPwd());
	}

	@Override
	public void queryUserCodeAndPwdFromLocal() {
		String[] results = userModel.queryUserCodeAndPwdFromLocal();
		if(!StringUtils.isBlank(results[0]))
		userLoginView.queryFromLocal(results[0], results[1]);
	}

	@Override
	public void otherLogin(Map<String, Object> params) {
		userModel.otherLogin(params);
	}

}

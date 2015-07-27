package com.light.presenter.impl;

import java.util.Map;

import android.content.Context;
import android.os.Handler;

import com.light.model.IUserModel;
import com.light.model.impl.UserModelImpl;
import com.light.presenter.IUserRegistPresenter;
import com.light.view.IUserRegistView;

public class UserRegistPresenterImpl implements IUserRegistPresenter{
	
	private IUserModel userModel;
	
	private IUserRegistView registView;
	
	public UserRegistPresenterImpl(Context context){
		this(context,null);
	}
	
	public UserRegistPresenterImpl(Context context, IUserRegistView registView){
		this.registView = registView;
		userModel = new UserModelImpl(context);
	}


	@Override
	public void validCode(int userId, String authCode) {
		userModel.validCode(userId, authCode);
	}

	@Override
	public void addInfo(Map<String, Object> infoMap) {
		userModel.addInfo(infoMap);
	}

	@Override
	public void registCode(String code, Handler... handler) {
		userModel.registCode(code,handler);
	}

	

}

package com.light.presenter.impl;

import android.content.Context;
import android.os.Handler;

import com.light.model.IUserModel;
import com.light.model.impl.UserModelImpl;
import com.light.presenter.IUserForgetPswPresenter;
import com.light.view.IUserForgetView;
import com.light.view.IUserRegistView;

public class UserForgetPresenterImpl implements IUserForgetPswPresenter {
	private IUserModel userModel;

	private IUserForgetView forgetView;

	public UserForgetPresenterImpl(Context context, IUserForgetView forgetView) {
		this.forgetView = forgetView;
		userModel = new UserModelImpl(context);
	}

	@Override
	public void validCode(int userId, String authCode) {
		userModel.validCode(userId, authCode);
	}

	@Override
	public void resetPsw(int userId, String psw) {
		userModel.resetPsw(userId, psw);
	}

}

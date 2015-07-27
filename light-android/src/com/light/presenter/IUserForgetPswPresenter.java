package com.light.presenter;

import android.os.Handler;

public interface IUserForgetPswPresenter {
	public void validCode(int userId, String authCode);

	public void resetPsw(int userId, String psw);

}

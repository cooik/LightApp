package com.light.presenter;

import java.util.Map;

import android.os.Handler;

public interface IUserRegistPresenter {
	
	public void registCode(String code , Handler... handler);
	
	public void validCode(int userId,String authCode);
	
	public void addInfo(Map<String,Object> infoMap);

}

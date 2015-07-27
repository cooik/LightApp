package com.light.view;

public interface IUserLoginView {
	
	public String getUserName();
	
	public String getUserPwd();
	
	public void queryFromLocal(String code,String passwd);
}

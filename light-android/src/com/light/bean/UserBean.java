package com.light.bean;

import java.io.Serializable;

public class UserBean implements Serializable {

	private int id;

	private String userCode;

	private String name;

	private String passwd;

	private int societyGender; // 社会性别

	private int physiologyGender; // 生理性别

	private String avatar; // 头像

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPasswd() {
		return passwd;
	}

	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}

	public int getSocietyGender() {
		return societyGender;
	}

	public void setSocietyGender(int societyGender) {
		this.societyGender = societyGender;
	}

	public int getPhysiologyGender() {
		return physiologyGender;
	}

	public void setPhysiologyGender(int physiologyGender) {
		this.physiologyGender = physiologyGender;
	}

	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

}

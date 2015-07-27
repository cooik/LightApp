package com.light.bean;

import java.util.List;

public class ResponseBean {

	private int id;

	private int sys_code;

	private String sys_msg;

	private String result_msg;

	private int physiology_gender;

	private int result_code;

	private int society_gender;

	private String name;

	private int user_id;

	private String val_code;

	private String requestUrl;

	private int validate_result;

	private List<Article> articles;

	private String avatar;

	private String token;

	private List<FriendBean> friends;

	private List<FriendBean> npcs;

	private List<CertificateBean> certificates;
	
	private List<ExpertBean> experts;

	public List<CertificateBean> getCertificates() {
		return certificates;
	}

	public void setCertificates(List<CertificateBean> certificates) {
		this.certificates = certificates;
	}

	public List<Article> getArticles() {
		return articles;
	}

	public void setArticles(List<Article> articles) {
		this.articles = articles;
	}

	public int getSys_code() {
		return sys_code;
	}

	public void setSys_code(int sys_code) {
		this.sys_code = sys_code;
	}

	public String getSys_msg() {
		return sys_msg;
	}

	public void setSys_msg(String sys_msg) {
		this.sys_msg = sys_msg;
	}

	public String getResult_msg() {
		return result_msg;
	}

	public void setResult_msg(String result_msg) {
		this.result_msg = result_msg;
	}

	public int getPhysiology_gender() {
		return physiology_gender;
	}

	public void setPhysiology_gender(int physiology_gender) {
		this.physiology_gender = physiology_gender;
	}

	public int getResult_code() {
		return result_code;
	}

	public void setResult_code(int result_code) {
		this.result_code = result_code;
	}

	public int getSociety_gender() {
		return society_gender;
	}

	public void setSociety_gender(int society_gender) {
		this.society_gender = society_gender;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getUser_id() {
		return user_id;
	}

	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}

	public String getVal_code() {
		return val_code;
	}

	public void setVal_code(String val_code) {
		this.val_code = val_code;
	}

	public String getRequestUrl() {
		return requestUrl;
	}

	public void setRequestUrl(String requestUrl) {
		this.requestUrl = requestUrl;
	}

	public int getValidate_result() {
		return validate_result;
	}

	public void setValidate_result(int validate_result) {
		this.validate_result = validate_result;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public List<FriendBean> getFriends() {
		return friends;
	}

	public void setFriends(List<FriendBean> friends) {
		this.friends = friends;
	}

	public List<FriendBean> getNpcs() {
		return npcs;
	}

	public void setNpcs(List<FriendBean> npcs) {
		this.npcs = npcs;
	}
	
	

	@Override
	public String toString() {
		return Integer.toString(getSys_code());
	}

	public List<ExpertBean> getExperts() {
		return experts;
	}

	public void setExperts(List<ExpertBean> experts) {
		this.experts = experts;
	}
}

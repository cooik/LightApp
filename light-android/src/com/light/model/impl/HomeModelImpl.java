package com.light.model.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.light.app.LightApplication;
import com.light.bean.FriendBean;
import com.light.bean.ResponseBean;
import com.light.constant.SysConst;
import com.light.model.IHomeModel;
import com.light.network.CallBack;
import com.light.network.Constant;
import com.light.util.LibraryUtils;

public class HomeModelImpl implements IHomeModel, CallBack {

	private String requestUrl;
	
	private DbUtils dbUtils;
	
	public HomeModelImpl(){
		dbUtils = DbUtils.create(LightApplication.getInstance(), SysConst.DB_NAME);
	}

	@Override
	public void onNetworkFinished(boolean isSuccess, String reuslt) {

		if (isSuccess) {
			ResponseBean response = LibraryUtils.formatJsonToTargetClass(
					reuslt, ResponseBean.class);
			response.setRequestUrl(requestUrl);

			LibraryUtils.sendEvent(response);
			response = null;
		} else {
			ResponseBean response = new ResponseBean();
			response.setResult_code(Constant.CODE_ERROR);
			response.setResult_msg(reuslt);
			LibraryUtils.sendEvent(response);
			response = null;
		}
	}

	@Override
	public void getRongToken(int userId) {
		this.requestUrl = Constant.GET_RONG_TOKEN;
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("user_id", userId);
		String inputJson = LibraryUtils.formatJson(map);
		LibraryUtils.httpPost(Constant.GET_RONG_TOKEN, inputJson, this);
		map = null;
	}

	@Override
	public List<FriendBean> queryFriendsById(int userId) {
		this.requestUrl = Constant.QUERY_FRIENDS;
		List<FriendBean> friends = null;
		try {
			friends = dbUtils.findAll(Selector.from(FriendBean.class).where("user_id", "=", userId).and("status","=","-1"));
		} catch (DbException e) {
			LibraryUtils.onError(LightApplication.getInstance(), e.getMessage());
		}
		if(friends == null || friends.size() == 0){
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("user_id", userId);
			String inputJson = LibraryUtils.formatJson(map);
			LibraryUtils.httpPost(Constant.QUERY_FRIENDS, inputJson, this);
			map = null;
			friends = null;
		}
		
		return friends;
	}

	@Override
	public void queryFriends(Map<String, Object> params) {
		this.requestUrl = Constant.SEARCH_FRIENDS;
		String inputJson = LibraryUtils.formatJson(params);
		LibraryUtils.httpPost(Constant.SEARCH_FRIENDS, inputJson, this);
	}

	@Override
	public void addFriend(int userId, int friendId) {
		this.requestUrl = Constant.ADD_FRIEND;
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("user_id", userId);
		params.put("to_user_id", friendId);
		String inputJson = LibraryUtils.formatJson(params);
		LibraryUtils.httpPost(Constant.ADD_FRIEND, inputJson, this);
	}

	@Override
	public void acceptFriend(int reqId, int userId) {
		this.requestUrl = Constant.ACCEPT_FRIEND;
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("req_id", reqId);
		params.put("user_id", userId);
		String inputJson = LibraryUtils.formatJson(params);
		LibraryUtils.httpPost(Constant.ACCEPT_FRIEND, inputJson, this);
	}

	@Override
	public List<FriendBean> queryNewFriendsById(int userId) {
		this.requestUrl = Constant.NEW_FRIEND;
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("user_id", userId);
		String inputJson = LibraryUtils.formatJson(params);
		LibraryUtils.httpPost(Constant.NEW_FRIEND, inputJson, this);
		return null;
	}

	@Override
	public List<FriendBean> queryNpcs(int userId) {
		this.requestUrl = 	Constant.QUERY_NPCS;
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("user_id", userId);
		String inputJson = LibraryUtils.formatJson(params);
		LibraryUtils.httpPost(Constant.QUERY_NPCS, inputJson, this);
		return null;
	}

	@Override
	public void queryExperts(int field) {
		this.requestUrl = Constant.QUERY_EXPERT;
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("field", field);
		String inputJson = LibraryUtils.formatJson(params);
		LibraryUtils.httpPost(Constant.QUERY_EXPERT, inputJson, this);
		params = null;
	}

}

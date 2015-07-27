package com.light.model;

import java.util.List;
import java.util.Map;

import com.light.bean.FriendBean;

public interface IHomeModel {
	
	
	public void getRongToken(int userId);
	
	public List<FriendBean> queryFriendsById(int userId);
	
	public void queryFriends(Map<String,Object> params);
	
	public void addFriend(int userId,int friendId);
	
	public void acceptFriend(int reqId,int userId);
	
	public List<FriendBean> queryNewFriendsById(int userId);
	
	public List<FriendBean> queryNpcs(int userId);
	
	public void queryExperts(int field);

}

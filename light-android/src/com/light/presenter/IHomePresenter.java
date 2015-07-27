package com.light.presenter;

public interface IHomePresenter {
	
	public void  getRongToken();
	
	public void queryFriends();
	
	public void queryFriends(String key,int pageNo,int pageSize);
	
	public void addFriend(int friendId);
	
	public void acceptFriend(int reqId,int userId);
	
	public void queryNewFriends();
	
	public void queryNpcs();
	
	public void queryExperts(int field);

}

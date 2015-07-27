package com.light.presenter.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.light.bean.FriendBean;
import com.light.model.IHomeModel;
import com.light.model.impl.HomeModelImpl;
import com.light.presenter.IHomePresenter;
import com.light.util.SessionUtils;
import com.light.view.IMessageView;

public class HomePresenterImpl implements IHomePresenter{
	
	private IHomeModel homeModel;
	private IMessageView messageView;
	
	public HomePresenterImpl(){
		homeModel = new HomeModelImpl();
	}
	
	public HomePresenterImpl(IMessageView messageView){
		this.messageView = messageView;
		homeModel = new HomeModelImpl();
	}

	@Override
	public void getRongToken() {
		if(SessionUtils.getInstance().getUserBean()==null)return;
		homeModel.getRongToken(SessionUtils.getInstance().getUserBean().getId());
	}

	@Override
	public void queryFriends() {
		if(SessionUtils.getInstance().getUserBean()==null)return;
		List<FriendBean> friends = homeModel.queryFriendsById(SessionUtils.getInstance().getUserBean().getId());
		if(friends!=null){
			messageView.showFriendsListFromLocal(friends);
		}
	}

	@Override
	public void queryFriends(String key, int pageNo, int pageSize) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("key", key);
		params.put("page_no", pageNo);
		params.put("page_size", pageSize);
		homeModel.queryFriends(params);
	}

	@Override
	public void addFriend(int friendId) {
		if(SessionUtils.getInstance().getUserBean()==null)return;
		homeModel.addFriend(SessionUtils.getInstance().getUserBean().getId(), friendId);
	}

	@Override
	public void acceptFriend(int reqId, int userId) {
		homeModel.acceptFriend(reqId, userId);
	}

	@Override
	public void queryNewFriends() {
		if(SessionUtils.getInstance().getUserBean()==null)return;
		homeModel.queryNewFriendsById(SessionUtils.getInstance().getUserBean().getId());
	}

	@Override
	public void queryNpcs() {
		if(SessionUtils.getInstance().getUserBean()==null)return;
		homeModel.queryNpcs(SessionUtils.getInstance().getUserBean().getId());
	}

	@Override
	public void queryExperts(int field) {
		homeModel.queryExperts(field);
	}

}

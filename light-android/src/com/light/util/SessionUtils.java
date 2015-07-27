package com.light.util;

import io.rong.imkit.RongIM;

import java.util.List;

import android.content.Context;
import android.content.Intent;

import com.light.activity.AMapLocationActivity;
import com.light.bean.FriendBean;
import com.light.bean.UserBean;

public class SessionUtils {
	
	private static SessionUtils instance = null;
	
	private SessionUtils(){
		RongIM.setLocationProvider(new LocationProvider());
	}
	
	
	public static SessionUtils getInstance(){
		if(null == instance){
			synchronized (SessionUtils.class) {
				if(null == instance){
					instance = new SessionUtils();
				}
			}
		}
		return instance;
	}
	
	
	private UserBean userBean;
	
	private String rongToken;
	
	private List<FriendBean> friends;
	
	private FriendBean newFriend;
	
	private int pushType;
	
	private boolean isNeedRefresh;
	
	 private RongIM.LocationProvider.LocationCallback mLastLocationCallback;

	public UserBean getUserBean() {
		return userBean;
	}


	public void setUserBean(UserBean userBean) {
		this.userBean = userBean;
	}


	public String getRongToken() {
		return rongToken;
	}


	public void setRongToken(String rongToken) {
		this.rongToken = rongToken;
	}


	public List<FriendBean> getFriends() {
		return friends;
	}


	public void setFriends(List<FriendBean> friends) {
		this.friends = friends;
	}


	public FriendBean getNewFriend() {
		return newFriend;
	}


	public void setNewFriend(FriendBean newFriend) {
		this.newFriend = newFriend;
	}


	public int getPushType() {
		return pushType;
	}


	public void setPushType(int pushType) {
		this.pushType = pushType;
	}


	public boolean isNeedRefresh() {
		return isNeedRefresh;
	}


	public void setNeedRefresh(boolean isNeedRefresh) {
		this.isNeedRefresh = isNeedRefresh;
	}


	public RongIM.LocationProvider.LocationCallback getLastLocationCallback() {
		return mLastLocationCallback;
	}


	public void setLastLocationCallback(
			RongIM.LocationProvider.LocationCallback mLastLocationCallback) {
		this.mLastLocationCallback = mLastLocationCallback;
	}

	
	 class LocationProvider implements RongIM.LocationProvider {

	        /**
	         * 位置信息提供者:LocationProvider 的回调方法，打开第三方地图页面。
	         *
	         * @param context  上下文
	         * @param callback 回调
	         */
	        @Override
	        public void onStartLocation(Context context, RongIM.LocationProvider.LocationCallback callback) {
	            SessionUtils.getInstance().setLastLocationCallback(callback);
	            Intent intent = new Intent(context, AMapLocationActivity.class);
	            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	            context.startActivity(intent);//SOSO地图
	        }
	    }
	
}

package com.light.activity;

import android.os.Bundle;

import com.light.R;
import com.light.fragment.FriendDetailFragment;
import com.light.util.SessionUtils;

public class FriendDetailsActivity extends BaseFragmentActivity {

	private static final String PAGE_NAME = "FriendDetailsActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState, R.layout.activity_friend_detail);

		/**
		 * req_id 请求id from_user_id 发起请求的用户id to_user_id 收到请求的用户id
		 * from_user_name 发起请求的用户名称 from_user_avatar 发起请求的用户头像
		 */
		FriendDetailFragment fragment = new FriendDetailFragment(SessionUtils
				.getInstance().getNewFriend(),
				FriendDetailFragment.FRIEND_ACCEPT);
		fragment.show(getSupportFragmentManager(), R.id.ll_friend_detail,"FriendDetailFragment");
	}

}

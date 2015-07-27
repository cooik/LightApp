package com.light.fragment;

import java.util.List;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.joanzapata.android.BaseAdapterHelper;
import com.joanzapata.android.QuickAdapter;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.light.R;
import com.light.activity.HomeActivity;
import com.light.bean.FriendBean;
import com.light.bean.ResponseBean;
import com.light.network.Constant;
import com.light.presenter.IHomePresenter;
import com.light.presenter.impl.HomePresenterImpl;
import com.light.util.SessionUtils;
import com.light.util.ToastUtils;

public class NewFriendsFragment extends BaseFragment {

	private static final String PAGE_NAME = "NewFriendsFragment";

	@ViewInject(R.id.iv_show_leftmenu)
	private ImageView backIv;

	@ViewInject(R.id.tv_title)
	private TextView title;

	@OnClick(R.id.iv_show_leftmenu)
	private void backAction(View view) {
		this.dismiss();
	}

	@ViewInject(R.id.lv_show_friends)
	private ListView friendsListView;

	private IHomePresenter homePresenter;

	private QuickAdapter<FriendBean> mAdapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = super.onCreateView(inflater, container, savedInstanceState,
				R.layout.fragment_new_friends, PAGE_NAME);

		homePresenter = new HomePresenterImpl();

		homePresenter.queryNewFriends();

		return view;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		title.setText("新的朋友");

		backIv.setImageResource(R.drawable.icons_back);
	}

	@Override
	public void onEvent(ResponseBean event) {
		if (Constant.NEW_FRIEND.equals(event.getRequestUrl())) {
			if (event.getResult_code() == Constant.CODE_SUCCESS) {
				List<FriendBean> friendsListData = event.getFriends();
				
				if(friendsListData == null){
					ToastUtils.show(context, "您还没有好友，赶快去添加吧");
					return;
				}

				mAdapter = new QuickAdapter<FriendBean>(context,
						R.layout.friend_list_item, friendsListData) {

					@Override
					protected void convert(BaseAdapterHelper helper,
							FriendBean item) {

						final FriendBean bean = item;
						helper.setText(R.id.tv_friend_name, item.getName());
						helper.setImageUrl(R.id.iv_friend_avatar,
								item.getAvatar());
						helper.setText(R.id.tv_friend_id, item.getFriendId()
								+ "");
						helper.setVisible(R.id.btn_friend_action, true);
						switch(item.getStatus()){
						case 1:
							helper.setBackgroundColor(R.id.btn_friend_action, getResources().getColor(R.color.regist_page_bg));
							helper.setText(R.id.btn_friend_action, "已添加");
							helper.setOnClickListener(R.id.btn_friend_action, null);
							break;
						case 0:
							helper.setBackgroundColor(R.id.btn_friend_action, getResources().getColor(R.color.green));
							helper.setText(R.id.btn_friend_action, "接受");
							helper.setOnClickListener(R.id.btn_friend_action, new OnClickListener(){

								@Override
								public void onClick(View v) {
									friend = bean;
									Log.d(PAGE_NAME, "bean_reqId:" + bean.getReqId());
									homePresenter.acceptFriend(bean.getReqId(), SessionUtils.getInstance().getUserBean().getId());
								}
								
							});
							break;
						}
					}
				};
				// 设置适配器
				friendsListView.setAdapter(mAdapter);

				friendsListView
						.setOnItemClickListener(new OnItemClickListener() {

							@Override
							public void onItemClick(AdapterView<?> parent,
									View view, int position, long id) {
							}

						});
			} else {
				ToastUtils.show(context, event.getResult_msg());
			}
		}else if(Constant.ACCEPT_FRIEND.equals(event.getRequestUrl())){
			if (event.getResult_code() == Constant.CODE_SUCCESS){
				ToastUtils.show(context, "添加好友成功");
				if(((HomeActivity)getActivity()).messageFragment!=null)
					((HomeActivity)getActivity()).messageFragment.addNewFriend(friend);
				this.dismiss();
			} else {
				ToastUtils.show(context, event.getResult_msg());
			}
		}
	}

	
	private FriendBean friend;
}

package com.light.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.light.R;
import com.light.activity.FriendDetailsActivity;
import com.light.activity.HomeActivity;
import com.light.bean.FriendBean;
import com.light.bean.ResponseBean;
import com.light.network.Constant;
import com.light.presenter.IHomePresenter;
import com.light.presenter.impl.HomePresenterImpl;
import com.light.util.ToastUtils;
import com.squareup.picasso.Picasso;

public class FriendDetailFragment extends BaseFragment{
	
	public final static int FRIEND_ADD = 0;
	
	public final static int FRIEND_ACCEPT = 1;
	
	private  int state;
	
	public static final String PAGE_NAME = "FriendDetailFragment";
	
	@ViewInject(R.id.btn_friend_add)
	private Button addBtn;
	
	@ViewInject(R.id.btn_friend_accept)
	private Button acceptBtn;
	
	@ViewInject(R.id.iv_friend_pic)
	private ImageView picIv;
	
	@ViewInject(R.id.tv_friend_name)
	private TextView nameTv;
	
	@ViewInject(R.id.iv_show_leftmenu)
	private ImageView backIv;

	@ViewInject(R.id.tv_title)
	private TextView title;
	
	@OnClick(R.id.iv_show_leftmenu)
	private void backAction(View view) {
		this.dismiss();
	}
	
	private FriendBean friend;
	
	public FriendDetailFragment(FriendBean friend, int state){
		this.friend = friend;
		this.state = state;
	}
	
	private IHomePresenter homePresenter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = super.onCreateView(inflater, container, savedInstanceState,R.layout.fragment_friend_detail,PAGE_NAME);
		
		homePresenter = new HomePresenterImpl();
		
		return view;
	}
	
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		title.setText("详细资料");
		
		backIv.setImageResource(R.drawable.icons_back);
		
		Picasso.with(context).load(friend.getAvatar()).into(picIv);
		
		nameTv.setText(friend.getName());
		
		switch(state){
		case FRIEND_ADD:
			setVisibilityAndGone(addBtn,acceptBtn);
			break;
		case FRIEND_ACCEPT:
			setVisibilityAndGone(acceptBtn,addBtn);
			break;
		}
	}
	
	@OnClick(R.id.btn_friend_add)
	private void addFriend(View view){
		homePresenter.addFriend(friend.getFriendId());
	}
	
	@OnClick(R.id.btn_friend_accept)
	private void acceptFriend(View view){
		homePresenter.acceptFriend(friend.getReqId(), friend.getUserId());
	}
	
	@Override
	public void onEvent(ResponseBean event) {
		if(Constant.ADD_FRIEND.equals(event.getRequestUrl())){
			if (event.getResult_code() == Constant.CODE_SUCCESS){
				ToastUtils.show(context, "好友申请发送成功");
				this.dismiss();
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
	
	private void setVisibilityAndGone(View v,View... views){
		v.setVisibility(View.VISIBLE);
		for(View view : views){
			view.setVisibility(View.GONE);
		}
	}
	
	
	@Override
	public void dismiss() {
		if(getActivity() instanceof FriendDetailsActivity){
			getActivity().finish();
		}else{
			super.dismiss();
		}
	}
}

package com.light.fragment;

import io.rong.imkit.RongIM;

import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.light.R;
import com.light.bean.ExpertBean;
import com.squareup.picasso.Picasso;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class ExpertDetailFragment extends BaseFragment{
	
	private String TAG = "ExpertDetailFragment";
	
	private ExpertBean bean;
	
	public ExpertDetailFragment(ExpertBean bean){
		this.bean = bean;
	}
	
	@ViewInject(R.id.iv_expert_avatar)
	private ImageView  avatarIv;
	
	@ViewInject(R.id.tv_expert_name)
	private TextView nameTv;
	
	@ViewInject(R.id.tv_expert_desc)
	private TextView desTv;
	
	@ViewInject(R.id.tv_expert_goodat)
	private TextView goodatTv;
	
	@ViewInject(R.id.tv_expert_auth)
	private TextView authTv;
	
	@ViewInject(R.id.btn_ask_expert)
	private Button askBtn;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = super.onCreateView(inflater, container, savedInstanceState,R.layout.fragment_expert_detail,TAG);
		return v;
	}
	
	
	@ViewInject(R.id.iv_show_leftmenu)
	private ImageView backIv;

	@ViewInject(R.id.tv_title)
	private TextView title;
	
	@OnClick(R.id.iv_show_leftmenu)
	private void backAction(View view) {
		getActivity().getSupportFragmentManager().popBackStack();
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		title.setText(bean.getName());
		backIv.setImageResource(R.drawable.icons_back);
		initDatas();
		
	}
	
	private void initDatas(){
		nameTv.setText(bean.getName());
		Picasso.with(context).load(bean.getAvatar()).into(avatarIv);
		desTv.setText(bean.getDescription());
		goodatTv.setText(bean.getGood_at());
		authTv.setText(bean.getAuth());
	}

	@OnClick(R.id.btn_ask_expert)
	private void askAction(View v){
		RongIM.getInstance().startPrivateChat(context,
				bean.getUser_id() + "",
				bean.getName());
	}
}

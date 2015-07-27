package com.light.fragment;

import io.rong.imkit.RongIM;

import java.util.List;

import com.joanzapata.android.BaseAdapterHelper;
import com.joanzapata.android.QuickAdapter;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.light.R;
import com.light.bean.FriendBean;
import com.light.bean.ResponseBean;
import com.light.network.Constant;
import com.light.presenter.IHomePresenter;
import com.light.presenter.impl.HomePresenterImpl;
import com.light.util.ToastUtils;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class NpcsFragment extends BaseFragment{
	
	private static final String PAGE_NAME = "NpcsFragment";
	
	private IHomePresenter homePresenter;
	
	private List<FriendBean> npcsData;
	
	@ViewInject(R.id.lv_show_npcs)
	private ListView mListView;
	
	private  QuickAdapter<FriendBean> mAdapter;
	
	@ViewInject(R.id.iv_show_leftmenu)
	private ImageView backIv;

	@ViewInject(R.id.tv_title)
	private TextView title;

	@OnClick(R.id.iv_show_leftmenu)
	private void backAction(View view) {
		this.dismiss();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View view = super.onCreateView(inflater, container, savedInstanceState,R.layout.fragment_npcs, PAGE_NAME); 
		
		homePresenter = new HomePresenterImpl();
		homePresenter.queryNpcs();
		
		return view;
	}
	
	@Override
	public void onEvent(ResponseBean event) {
		if (Constant.QUERY_NPCS.equals(event.getRequestUrl())) {
			
			if(event.getResult_code() == Constant.CODE_SUCCESS){
				
				if (npcsData == null) {
					
					npcsData = event.getNpcs();
					
					mAdapter = new QuickAdapter<FriendBean>(context,
							R.layout.common_chat_layout, npcsData) {
						
						@Override
						protected void convert(BaseAdapterHelper helper,
								FriendBean item) {
							
							helper.setText(R.id.tv_common_name, item.getName());
							helper.setImageUrl(R.id.iv_common_head_pic,
									item.getAvatar());
						}
					};
					
					// 设置适配器
					mListView.setAdapter(mAdapter);
					
					mListView.setOnItemClickListener(new OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> parent, View view,
								int position, long id) {

								RongIM.getInstance().startPrivateChat(context,
										mAdapter.getItem(position).getFriendId() + "",
										mAdapter.getItem(position).getName());
						}

					});
					
				} else {
					ToastUtils.show(context, event.getResult_msg());
				}
			}
		}
	}
	

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		title.setText("LIGHT服务");

		backIv.setImageResource(R.drawable.icons_back);
	}
	
}

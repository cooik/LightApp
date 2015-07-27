package com.light.fragment;

import java.util.List;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
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
import com.light.bean.ExpertBean;

public class ExpertListFragment extends BaseFragment {

	private String PAGENAME = "ExpertListFragment";
	
	private QuickAdapter<ExpertBean> quickAdapter;
	
	private List<ExpertBean> expertList;
	
	@ViewInject(R.id.lv_experts)
	private ListView mListView;
	
	private String _title;
	
	public ExpertListFragment(List<ExpertBean> expertList,String _title){
		this.expertList = expertList;
		this._title = _title;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = super.onCreateView(inflater, container, savedInstanceState,
				R.layout.fragment_list_expert, PAGENAME);
		
		initDatas();
		return v;
	}

	private void initDatas(){
		quickAdapter = new QuickAdapter<ExpertBean>(context,R.layout.expert_list_item,expertList){

			@Override
			protected void convert(BaseAdapterHelper helper, ExpertBean item) {
				helper.setImageUrl(R.id.iv_expert_avatar, item.getAvatar());
				helper.setText(R.id.tv_expert_name, item.getName());
				helper.setText(R.id.tv_expert_desc, item.getDescription());
			}
			
		};
		
		mListView.setAdapter(quickAdapter);
		
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				ExpertDetailFragment fragment = new ExpertDetailFragment(expertList.get(position));
				fragment.showBackStack(getFragmentManager(), "ExpertDetailFragment");
			}
		});
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

		title.setText(_title);
		backIv.setImageResource(R.drawable.icons_back);
		
	}
	
}

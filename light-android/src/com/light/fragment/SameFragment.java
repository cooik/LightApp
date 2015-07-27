package com.light.fragment;

import java.util.List;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.light.R;
import com.light.bean.ExpertBean;
import com.light.bean.ResponseBean;
import com.light.network.Constant;
import com.light.presenter.IHomePresenter;
import com.light.presenter.impl.HomePresenterImpl;
import com.light.util.ToastUtils;

public class SameFragment extends BaseFragment{
	
	private static final String PAGE_NAME = "SameFragment";
	
	@ViewInject(R.id.layout_lawyer)
	private ViewGroup lawyerLayout;
	
	@ViewInject(R.id.layout_companyer)
	private ViewGroup companyerLayout;
	
	@ViewInject(R.id.layout_psychological_counselor)
	private ViewGroup psychologicalCounselorLayout;
	
	@ViewInject(R.id.layout_master)
	private ViewGroup mastarLayout;
	
	private IHomePresenter homePresenter;
	
	String title;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View v = super.onCreateView(inflater, container, savedInstanceState,R.layout.fragment_same,PAGE_NAME);
		
		homePresenter = new HomePresenterImpl();
		
		return v;
	}

	
	@OnClick(R.id.layout_lawyer)
	private void showLawyer(View v){
		title = "Light律师";
		showDialog("正在获取专家信息...");
		homePresenter.queryExperts(30);
	}
	
	@OnClick(R.id.layout_companyer)
	private void showCompanyer(View v){
		title = "Light陪伴师";
		showDialog("正在获取专家信息...");
		homePresenter.queryExperts(10);
	}
	
	@OnClick(R.id.layout_psychological_counselor)
	private void showPsychologicalCounselor(View v){
		title = "Light心理咨询师";
		showDialog("正在获取专家信息...");
		homePresenter.queryExperts(11);
	}
	
	@OnClick(R.id.layout_master)
	private void showMaster(View v){
		title = "Light命理大师";
		showDialog("正在获取专家信息...");
		homePresenter.queryExperts(20);
	}
	
	
	@Override
	public void onEvent(ResponseBean event) {
		
		if(Constant.QUERY_EXPERT.equals(event.getRequestUrl())){
			if(event.getResult_code() == Constant.CODE_SUCCESS){
				List<ExpertBean> expertList = event.getExperts();
				if(expertList == null || expertList.size() == 0){
					ToastUtils.show(context, "未查询到专家信息！");
				}else{
					ExpertListFragment fragment = new ExpertListFragment(expertList,title);
					fragment.showBackStack(getFragmentManager(), "ExpertListFragment");
				}
			}else{
				ToastUtils.show(context, "获取专家信息失败！");
			}
			
			hideDialog();
		}
	}
}

package com.light.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.jpush.android.api.JPushInterface;

import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.ViewUtils;
import com.light.R;
import com.light.bean.ResponseBean;
import com.light.constant.SysConst;
import com.light.dialog.DialogHelper;
import com.light.dialog.WaitDialog;
import com.umeng.analytics.MobclickAgent;

import de.greenrobot.event.EventBus;

public class BaseFragment extends Fragment{
	
	public static final int STATE_NONE = 0;
    public static final int STATE_REFRESH = 1;
    public static final int STATE_LOADMORE = 2;
    public static final int STATE_NOMORE = 3;
    public static final int STATE_PRESSNONE = 4;// 正在下拉但还没有到刷新的状态
    public static int mState = STATE_NONE;
	
	protected Context context;
	
	protected String pageName = "BaseFragment";
	
	protected DbUtils dbUtils;
	
	boolean eventFlag = false;
	
	boolean mobFlag = false;
	
	private WaitDialog _waitDialog;
	
	
	protected void showDialog(String msg) {
		if (_waitDialog == null) {
			_waitDialog = DialogHelper.getWaitDialog(getActivity(), msg);
		}
		_waitDialog.show();
	}

	protected void hideDialog() {
		if (_waitDialog != null) {
			_waitDialog.dismiss();
		}
	}
	
	public BaseFragment(){
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState, int layoutId,String pageName) {
		View view = inflater.inflate(layoutId, container, false);
		
		ViewUtils.inject(this, view);
		
		this.pageName = pageName;
		
		EventBus.getDefault().register(this);
		
		eventFlag =  true;
		
		context = getActivity();
		
		dbUtils = DbUtils.create(context, SysConst.DB_NAME);
		dbUtils.configAllowTransaction(true);
		
		return view;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		
		mobFlag = true;
		MobclickAgent.onPageStart(pageName);
		
		JPushInterface.onFragmentResume(context, pageName);
	}
	
	@Override
	public void onPause() {
		super.onPause();
		
		JPushInterface.onFragmentPause(context, pageName);
	}
	
	@Override
	public void onStop() {
		super.onStop();
		Log.d(pageName, "onStop");
		if(mobFlag)
		MobclickAgent.onPageEnd(pageName); 
	}
	
	
	@Override
	public void onDestroyView() {
		
		Log.d(pageName, "onDestroyView");
		
		if(eventFlag)
		EventBus.getDefault().unregister(this);
		super.onDestroyView();
	}
	
	public void onEvent(ResponseBean event) {}
	
	public void dismiss() {
		getActivity().getSupportFragmentManager().beginTransaction().remove(this).commitAllowingStateLoss();
	}
	
	public void show(FragmentManager fm , String tag){
		fm.beginTransaction().replace(R.id.home_drawerLayout,this, tag).commitAllowingStateLoss();
	}
	
	public void show(FragmentManager fm , int layId,String tag){
		fm.beginTransaction().replace(layId,this, tag).addToBackStack(null).commitAllowingStateLoss();
	}
	
	public void showBackStack(FragmentManager fm , String tag){
		fm.beginTransaction().replace(R.id.home_drawerLayout,this, tag).addToBackStack(null).commitAllowingStateLoss();
	}
	
}

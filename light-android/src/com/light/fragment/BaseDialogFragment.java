package com.light.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.lidroid.xutils.ViewUtils;
import com.light.bean.ResponseBean;
import com.umeng.analytics.MobclickAgent;

import de.greenrobot.event.EventBus;

public class BaseDialogFragment extends DialogFragment {
	public static final int STATE_NONE = 0;
    public static final int STATE_REFRESH = 1;
    public static final int STATE_LOADMORE = 2;
    public static final int STATE_NOMORE = 3;
    public static final int STATE_PRESSNONE = 4;// 正在下拉但还没有到刷新的状态
    public static int mState = STATE_NONE;
	
	protected Context context;

	protected String pageName = "BaseDialogFragment";

	boolean eventFlag = false;

	boolean mobFlag = false;

	public BaseDialogFragment() {
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState, int layoutId, String pageName) {

		getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
		View view = inflater.inflate(layoutId, container, false);

		ViewUtils.inject(this, view);

		this.pageName = pageName;

		EventBus.getDefault().register(this);

		eventFlag = true;

		context = getActivity();

		return view;
	}

	@Override
	public void onResume() {
		super.onResume();

		mobFlag = true;
		MobclickAgent.onPageStart(pageName);
	}

	@Override
	public void onStop() {
		super.onStop();

		if (mobFlag)
			MobclickAgent.onPageEnd(pageName);
	}

	@Override
	public void onDestroyView() {
		if (eventFlag)
			EventBus.getDefault().unregister(this);
		super.onDestroyView();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setStyle(DialogFragment.STYLE_NORMAL,
				android.R.style.Theme_Black_NoTitleBar_Fullscreen);
	}

	public void onEvent(ResponseBean event) {
	}
	
}

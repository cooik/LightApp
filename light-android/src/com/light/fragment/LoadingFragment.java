package com.light.fragment;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.light.R;

public class LoadingFragment extends DialogFragment{
	
	private String tips;
	
	public LoadingFragment(String tips){
		this.tips = tips;
	}
	
	@ViewInject(R.id.tv_loading_tip)
	private TextView tipTv;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
		View view = inflater.inflate(R.layout.loading_fragment, container,
				false);
		ViewUtils.inject(this, view);
		
		tipTv.setText(tips);
		return view;
	}

	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setStyle(DialogFragment.STYLE_NORMAL,
				android.R.style.Theme_Black_NoTitleBar_Fullscreen);
	}
}

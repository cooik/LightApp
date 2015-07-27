package com.light.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.light.R;

public class DemoFragment extends BaseFragment{
	
private int index;
	
	@ViewInject(R.id.tv_tip)
	private TextView tip;
	
	public DemoFragment(int index){
		this.index = index;
	}
	
	public DemoFragment(){
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_demo,container, false);
		ViewUtils.inject(this, v);
		
		tip.setText("这是第"+index+"页面");
		return v;
	}

}

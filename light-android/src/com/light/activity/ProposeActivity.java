package com.light.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.light.R;

public class ProposeActivity extends BaseActivity {
	@ViewInject(R.id.tv_title)
	private TextView title;
	@ViewInject(R.id.iv_show_leftmenu)
	private ImageView backIv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_propose);
		initView();
	}

	private void initView() {
		ViewUtils.inject(this);
		title.setText("求婚");
		backIv.setImageResource(R.drawable.icons_back);
		backIv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}
}

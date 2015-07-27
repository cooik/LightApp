package com.light.activity;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.light.R;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class ProtocolActivity extends BaseActivity {
	@ViewInject(R.id.tv_title)
	private TextView title;
	@ViewInject(R.id.iv_show_leftmenu)
	private ImageView backIv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_protocol);
		initView();
	}

	private void initView() {
		ViewUtils.inject(this);
		title.setText("Light 协议");
		backIv.setImageResource(R.drawable.icons_back);
	}

	@OnClick(R.id.iv_show_leftmenu)
	private void back(View view) {
		finish();
	}
}

package com.light.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.light.R;
import com.light.app.AppManager;
import com.light.util.UpdateManager;

public class SettingsActivity extends BaseActivity {
	@ViewInject(R.id.tv_title)
	private TextView title;
	@ViewInject(R.id.iv_show_leftmenu)
	private ImageView backIv;
	
	@ViewInject(R.id.update)
	private ViewGroup updateLayout;
	
	@ViewInject(R.id.exit)
	private ViewGroup exitLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState,R.layout.activity_settings,"SettingsActivity");
		initView();

	}

	private void initView() {
		ViewUtils.inject(this);
		title.setText("设置");
		backIv.setImageResource(R.drawable.icons_back);
		backIv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	@OnClick(R.id.update)
	private void updateAction(View v){
		new UpdateManager(this, true).checkUpdate();
	}
	
	@OnClick(R.id.exit)
	private void exitAction(View v){
		AppManager.getAppManager().quit(this,false);
		Intent intent = new Intent(this,LoginActivity.class);
        startActivity(intent);
//        this.finish();
	}
}

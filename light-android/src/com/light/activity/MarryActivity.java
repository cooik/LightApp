package com.light.activity;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.light.R;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebSettings.PluginState;
import android.widget.ImageView;
import android.widget.TextView;

public class MarryActivity extends BaseActivity {
	@ViewInject(R.id.tv_title)
	private TextView title;
	@ViewInject(R.id.iv_show_leftmenu)
	private ImageView backIv;
	@ViewInject(R.id.wv01)
	private WebView wv;
	private static String TAG = "MarryActivity";
	
	String url = "http://wedding.lightlgbt.com";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_marry);
		Log.d(TAG, "MarryActivity----->setContentView");
		
		initView();
	}

	private void initView() {
		ViewUtils.inject(this);
		title.setText("海外结婚");
		backIv.setImageResource(R.drawable.icons_back);
		backIv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		
		WebSettings webSettings = wv.getSettings();
        webSettings.setJavaScriptEnabled(true); // 启用支持javascript
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);// 优先使用缓存
        webSettings.setAllowFileAccess(true);// 可以访问文件
        webSettings.setBuiltInZoomControls(true);// 支持缩放
        if (android.os.Build.VERSION.SDK_INT >= 11) {
            webSettings.setPluginState(PluginState.ON);
            webSettings.setDisplayZoomControls(false);// 支持缩放
        }
		
		wv.loadUrl(url);
		wv.setWebViewClient(new WebViewClient() {
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				boolean flag = super.shouldOverrideUrlLoading(view, url);
	            return flag;
			}
		});

	}
}

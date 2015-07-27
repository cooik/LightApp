package com.light.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.light.R;
import com.light.bean.Article;
import com.light.util.BitmapHelp;
import com.light.util.ImageLoader;

public class ArticlesActivity extends BaseActivity {
	private static final String TAG = "ArticleActivity";
	public BitmapUtils bitmaputils;
	Article mArticle;
	@ViewInject(R.id.article_iv)
	private ImageView iv;
	@ViewInject(R.id.article_title)
	private TextView tv_title;
	@ViewInject(R.id.article_content)
	private TextView tv_content;
	@ViewInject(R.id.tv_title)
	private TextView title;
	@ViewInject(R.id.iv_show_leftmenu)
	private ImageView backIv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_article_detail);

		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		mArticle = (Article) bundle.getSerializable("Article");
		bitmaputils = BitmapHelp.getBitmapUtils(this.getApplicationContext());
		initView();
	}

	private void initView() {
		ViewUtils.inject(this);
		title.setText("详情");
		backIv.setImageResource(R.drawable.icons_back);
		ImageLoader.getInstance(context).display(iv, mArticle.getPic());
		tv_title.setText(mArticle.getTitle());
		tv_content.setText(mArticle.getContent());
		backIv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});

	}
}

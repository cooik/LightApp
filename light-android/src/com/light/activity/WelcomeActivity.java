package com.light.activity;

import java.util.ArrayList;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.light.R;

public class WelcomeActivity extends BaseActivity {
	@ViewInject(R.id.vp_welcome_viewpager)
	private ViewPager mViewPager;
	@ViewInject(R.id.page0)
	private ImageView mPage0;
	@ViewInject(R.id.page1)
	private ImageView mPage1;
	@ViewInject(R.id.page2)
	private ImageView mPage2;
	@ViewInject(R.id.page3)
	private ImageView mPage3;
	@ViewInject(R.id.page4)
	private ImageView mPage4;
	private ImageButton btn1;
	// private Button btn2;
	// private Button btn3;
	// private Button btn4;
	// private Button btn5;

	public static String TAG = "WelcomeActivity";
	private int currIndex = 0;
	private static final String SHAREDPREFERENCES_NAME = "first_pref";
	private ArrayList<View> views;
	private boolean misScrolled;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState, R.layout.welcome_viewpager, TAG);
		setPreference();
		Log.d(TAG, "WelcomeActivity----->setPreference");
		initViews();
		Log.d(TAG, "WelcomeActivity----->initViews");

		mViewPager.setOnPageChangeListener(new MyOnPageChangeListener());
		Log.d(TAG, "WelcomeActivity----->start");
		PagerAdapter mPagerAdapter = new PagerAdapter() {

			@Override
			public int getCount() {

				return views.size();
			}

			@Override
			public boolean isViewFromObject(View arg0, Object arg1) {
				Log.d(TAG, "WelcomeActivity----->isViewFromObject");
				return arg0 == arg1;
			}

			@Override
			public void destroyItem(ViewGroup container, int position,
					Object object) {
				container.removeView(views.get(position));
			}

			@Override
			public Object instantiateItem(ViewGroup container, int position) {
				container.addView(views.get(position));
				Log.d(TAG, "WelcomeActivity----->instantiateItem");
				View view = views.get(position);
				btn1 = (ImageButton) view.findViewById(R.id.skip);
				btn1.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						toLogin();
					}
				});
				return view;
			}

		};
		mViewPager.setAdapter(mPagerAdapter);

	}

	private void initViews() {
		LayoutInflater mLi = LayoutInflater.from(this);
		View view1 = mLi.inflate(R.layout.welcome_page_a, null);
		View view2 = mLi.inflate(R.layout.welcome_page_b, null);
		View view3 = mLi.inflate(R.layout.welcome_page_c, null);
		View view4 = mLi.inflate(R.layout.welcome_page_d, null);
		View view5 = mLi.inflate(R.layout.welcome_page_e, null);
		// btn1 = (Button) view1.findViewById(R.id.skip);
		// btn1.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// toLogin();
		// }
		// });
		// btn2 = (Button) view2.findViewById(R.id.skip);
		// btn2.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// toLogin();
		// }
		// });
		// btn3 = (Button) view3.findViewById(R.id.skip);
		// btn3.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// toLogin();
		// }
		// });
		// btn4 = (Button) view4.findViewById(R.id.skip);
		// btn4.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// toLogin();
		// }
		// });
		// btn5 = (Button) view5.findViewById(R.id.skip);
		// btn5.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// toLogin();
		// }
		// });

		views = new ArrayList<View>();
		views.add(view1);
		views.add(view2);
		views.add(view3);
		views.add(view4);
		views.add(view5);
	}

	private void setPreference() {
		// TODO Auto-generated method stub
		SharedPreferences preferences = getSharedPreferences(
				SHAREDPREFERENCES_NAME, this.MODE_PRIVATE);
		Editor editor = preferences.edit();
		// 存入数据
		editor.putBoolean("isFirstIn", false);
		// 提交修改
		editor.commit();
	}

	public class MyOnPageChangeListener implements OnPageChangeListener {

		@Override
		public void onPageScrollStateChanged(int state) {
			switch (state) {
			case ViewPager.SCROLL_STATE_DRAGGING:
				misScrolled = false;
				break;
			case ViewPager.SCROLL_STATE_SETTLING:
				misScrolled = true;
				break;
			case ViewPager.SCROLL_STATE_IDLE:
				if (mViewPager.getCurrentItem() == mViewPager.getAdapter()
						.getCount() - 1 && !misScrolled) {
					toLogin();
				}
				misScrolled = true;
				break;
			}
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}

		@Override
		public void onPageSelected(int arg0) {
			switch (arg0) {
			case 0:
				mPage1.setImageDrawable(getResources().getDrawable(
						R.drawable.page_default));
				mPage0.setImageDrawable(getResources().getDrawable(
						R.drawable.page_current));
				break;
			case 1:
				mPage1.setImageDrawable(getResources().getDrawable(
						R.drawable.page_current));
				mPage0.setImageDrawable(getResources().getDrawable(
						R.drawable.page_default));
				mPage2.setImageDrawable(getResources().getDrawable(
						R.drawable.page_default));
				break;
			case 2:
				mPage2.setImageDrawable(getResources().getDrawable(
						R.drawable.page_current));
				mPage1.setImageDrawable(getResources().getDrawable(
						R.drawable.page_default));
				mPage3.setImageDrawable(getResources().getDrawable(
						R.drawable.page_default));
				break;
			case 3:
				mPage3.setImageDrawable(getResources().getDrawable(
						R.drawable.page_current));

				mPage2.setImageDrawable(getResources().getDrawable(
						R.drawable.page_default));
				mPage4.setImageDrawable(getResources().getDrawable(
						R.drawable.page_default));
				break;
			case 4:
				mPage4.setImageDrawable(getResources().getDrawable(
						R.drawable.page_current));
				mPage3.setImageDrawable(getResources().getDrawable(
						R.drawable.page_default));
				break;

			}
			currIndex = arg0;
		}

	}

	private void toLogin() {
		Log.e(TAG, "WelcomeActivity------->toLogin");
		Intent intent = new Intent();
		intent.setClass(WelcomeActivity.this, LoginActivity.class);
		startActivity(intent);
		overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
		finish();
	}
}

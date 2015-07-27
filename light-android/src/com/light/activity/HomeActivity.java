package com.light.activity;

import io.rong.imkit.RongIM;
import io.rong.imkit.fragment.ConversationListFragment;
import io.rong.imlib.RongIMClient.ConnectCallback;
import io.rong.imlib.RongIMClient.ErrorCode;
import io.rong.imlib.model.Conversation;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.DrawerLayout.DrawerListener;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.lidroid.xutils.view.annotation.event.OnRadioGroupCheckedChange;
import com.light.R;
import com.light.app.AppManager;
import com.light.bean.ResponseBean;
import com.light.constant.PageContants;
import com.light.constant.SysConst;
import com.light.fragment.AddFriendsFragment;
import com.light.fragment.ArticlesFragment;
import com.light.fragment.BaseFragment;
import com.light.fragment.DemoFragment;
import com.light.fragment.ForLoveFragment;
import com.light.fragment.MessageFragment;
import com.light.fragment.SameFragment;
import com.light.network.Constant;
import com.light.presenter.IHomePresenter;
import com.light.presenter.impl.HomePresenterImpl;
import com.light.util.DrawableUtils;
import com.light.util.LibraryUtils;
import com.light.util.PreferencesUtils;
import com.light.util.RongCloudEvent;
import com.light.util.SessionUtils;
import com.light.util.ToastUtils;
import com.light.util.UpdateManager;
import com.nineoldandroids.view.ViewHelper;

public class HomeActivity extends BaseFragmentActivity implements
		Handler.Callback {
	public  String TAG = "HomeActivity";
	public  MessageFragment messageFragment;
	public  ForLoveFragment forloveFragment;

	private  Fragment[] FRAGMENTS;

	@ViewInject(R.id.vp_home_container)
	private ViewPager mViewPager;
	@ViewInject(R.id.rg_home_tabs)
	private RadioGroup mRadioGroup;
	@ViewInject(R.id.home_drawerLayout)
	private DrawerLayout mDrawerLayout;

	@ViewInject(R.id.iv_show_leftmenu)
	private ImageView openMenu;
	@ViewInject(R.id.tv_title)
	private TextView title;
	@ViewInject(R.id.iv_add)
	public ImageView addImage;

	@ViewInject(R.id.rb_home_tab2)
	private RadioButton messageRb;

	static final String TITLES[] = { "首页", "消息", "求同", "求爱", "求知" };

	public IHomePresenter homePresenter;

	private boolean isRongConnected;
	public BitmapUtils bitmaputils;
	private Handler mHandler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState, R.layout.activity_home);
		
		messageFragment = new MessageFragment();
		forloveFragment = new ForLoveFragment();

		FRAGMENTS = new Fragment[]{ new ArticlesFragment(),
				messageFragment, new SameFragment(), forloveFragment,
				new DemoFragment(5) };
		
		title.setText(TITLES[0]);
		mViewPager
				.setAdapter(new ViewPagerAdapter(getSupportFragmentManager()));
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				title.setText(TITLES[arg0]);
				((RadioButton) mRadioGroup.getChildAt(arg0)).setChecked(true);

				switch (arg0) {
				case 0:
					LibraryUtils.onEvent(context,
							PageContants.EVENT_ID_MAINPAGE);
					break;
				case 1:
					LibraryUtils.onEvent(context,
							PageContants.EVENT_ID_MESSAGEPAGE);
					break;
				case 2:
					LibraryUtils.onEvent(context,
							PageContants.EVENT_ID_SAMEPAGE);
					break;
				case 3:
					LibraryUtils.onEvent(context,
							PageContants.EVENT_ID_LOVEPAGE);
					break;
				case 4:
					LibraryUtils.onEvent(context,
							PageContants.EVENT_ID_KNOWLEDGEPAGE);
					break;
				}

				doAddAction(arg0);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});

		mViewPager.setOffscreenPageLimit(TITLES.length - 1);
		initEvents();
		Log.d(TAG, TAG + "------>initEvents");
		mHandler = new Handler(this);
		homePresenter = new HomePresenterImpl();
		homePresenter.getRongToken();
		
		
		checkUpdate();

	}
	
	private void checkUpdate() {
		mHandler.postDelayed(new Runnable() {

			@Override
			public void run() {
				new UpdateManager(HomeActivity.this, false).checkUpdate();
			}
		}, 2000);
	}

	private class ViewPagerAdapter extends FragmentPagerAdapter {

		public ViewPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			return FRAGMENTS[position];
		}

		@Override
		public int getCount() {
			return FRAGMENTS.length;
		}

	}

	@OnRadioGroupCheckedChange(R.id.rg_home_tabs)
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
		case R.id.rb_home_tab1:
			mViewPager.setCurrentItem(0);
			break;

		case R.id.rb_home_tab2:
			mViewPager.setCurrentItem(1);
			break;

		case R.id.rb_home_tab3:
			mViewPager.setCurrentItem(2);
			break;

		case R.id.rb_home_tab4:
			mViewPager.setCurrentItem(3);
			break;
		case R.id.rb_home_tab5:
			mViewPager.setCurrentItem(4);
			break;

		default:
			break;
		}
	}

	private void initEvents() {
		mDrawerLayout.setDrawerListener(new DrawerListener() {
			@Override
			public void onDrawerStateChanged(int newState) {
			}

			@Override
			public void onDrawerSlide(View drawerView, float slideOffset) {
				View mContent = mDrawerLayout.getChildAt(0);
				View mMenu = drawerView;
				float scale = 1 - slideOffset;
				float rightScale = 0.8f + scale * 0.2f;

				if (drawerView.getTag().equals("LEFT")) {

					float leftScale = 1 - 0.3f * scale;

					ViewHelper.setScaleX(mMenu, leftScale);
					ViewHelper.setScaleY(mMenu, leftScale);
					ViewHelper.setAlpha(mMenu, 0.6f + 0.4f * (1 - scale));
					ViewHelper.setTranslationX(mContent,
							mMenu.getMeasuredWidth() * (1 - scale));
					ViewHelper.setPivotX(mContent, 0);
					ViewHelper.setPivotY(mContent,
							mContent.getMeasuredHeight() / 2);
					mContent.invalidate();
					ViewHelper.setScaleX(mContent, rightScale);
					ViewHelper.setScaleY(mContent, rightScale);
				} else {
					ViewHelper.setTranslationX(mContent,
							-mMenu.getMeasuredWidth() * slideOffset);
					ViewHelper.setPivotX(mContent, mContent.getMeasuredWidth());
					ViewHelper.setPivotY(mContent,
							mContent.getMeasuredHeight() / 2);
					mContent.invalidate();
					ViewHelper.setScaleX(mContent, rightScale);
					ViewHelper.setScaleY(mContent, rightScale);
				}

			}

			@Override
			public void onDrawerOpened(View drawerView) {
			}

			@Override
			public void onDrawerClosed(View drawerView) {
				mDrawerLayout.setDrawerLockMode(
						DrawerLayout.LOCK_MODE_LOCKED_CLOSED, Gravity.RIGHT);
			}
		});
	}

	@OnClick(R.id.iv_show_leftmenu)
	private void OpenLeftMenu(View view) {
		mDrawerLayout.openDrawer(Gravity.LEFT);
		mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED,
				Gravity.LEFT);

		LibraryUtils.onEvent(context, PageContants.EVENT_ID_SLIDEMENU);
	}

	@Override
	public void onEvent(ResponseBean event) {
		if (Constant.GET_RONG_TOKEN.equals(event.getRequestUrl())) {
			if (event.getResult_code() == Constant.CODE_SUCCESS) {
				String token = event.getToken();
				SessionUtils.getInstance().setRongToken(token);
				PreferencesUtils.putString(context, SysConst.RONG_TOKEN, token);
				RongIM.connect(token, new ConnectCallback() {

					@Override
					public void onError(ErrorCode arg0) {
						// ToastUtils.show(HomeActivity.this, "connect onError"
						// );
					}

					@Override
					public void onSuccess(String arg0) {
						// ToastUtils.show(HomeActivity.this,
						// "connect onSuccess"
						// );
						isRongConnected = true;
						RongCloudEvent.getInstance().setOtherListener();

						final Conversation.ConversationType[] conversationTypes = {
								Conversation.ConversationType.PRIVATE,
								Conversation.ConversationType.DISCUSSION,
								Conversation.ConversationType.GROUP,
								Conversation.ConversationType.SYSTEM,
								Conversation.ConversationType.APP_PUBLIC_SERVICE,
								Conversation.ConversationType.PUBLIC_SERVICE };

						mHandler.postDelayed(new Runnable() {
							@Override
							public void run() {
								RongIM.getInstance()
										.setOnReceiveUnreadCountChangedListener(
												mCountListener,
												conversationTypes);
							}
						}, 500);

						mHandler.post(new Runnable() {
							@Override
							public void run() {
								Intent intent = getIntent();
								if (intent != null) {
									enterFragment(intent);
								}
							}
						});

					}

					@Override
					public void onTokenIncorrect() {

					}
				});
			} else {
				ToastUtils.show(this, event.getResult_msg());
			}

		}
	}

	public RongIM.OnReceiveUnreadCountChangedListener mCountListener = new RongIM.OnReceiveUnreadCountChangedListener() {
		@Override
		public void onMessageIncreased(int count) {

			reDrawMessageTab(count);

		}
	};

	private void doAddAction(int index) {
		switch (index) {
		case 1:
			addImage.setVisibility(View.VISIBLE);
			break;
		default:
			addImage.setVisibility(View.GONE);
			break;
		}
	}

	@OnClick(R.id.iv_add)
	private void add(View view) {
		AddFriendsFragment fragment = new AddFriendsFragment();
		fragment.show(getSupportFragmentManager(), "AddFriendsFragment");
	}

	long mExitTime;

	@Override
	public void onBackPressed() {
		if (isNeedRemove())
			return;
		long secondTime = System.currentTimeMillis();
		if (secondTime - mExitTime > 2000) {
			ToastUtils.show(context, R.string.close_prompt_message);
			mExitTime = secondTime;

			return;
		} else {
			AppManager.getAppManager().AppExit(context,true);
			super.onBackPressed();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();

		if (isRongConnected) {
			// 如果有消息设置message tag
			// reDrawMessageTab();

			if (SessionUtils.getInstance().isNeedRefresh()) {
				SessionUtils.getInstance().setNeedRefresh(false);
				if (clfragment != null)
					getSupportFragmentManager().beginTransaction()
							.remove(clfragment).commitAllowingStateLoss();
				enterFragment(getIntent());
			}
		}
	}

	@SuppressLint("NewApi")
	private void reDrawMessageTab(int count) {
		// int count =
		// RongIM.getInstance().getRongIMClient().getTotalUnreadCount();
		Log.d("zd", "unreadcount:" + count);

		if (count > 0) {
			Bitmap bitmap = DrawableUtils.drawRedbotInBitmap(context,
					R.drawable.msg_32_default);
			messageRb.setCompoundDrawablesRelativeWithIntrinsicBounds(null,
					DrawableUtils.bitmapToDrawble(bitmap, context), null, null);
		} else {
			messageRb.setCompoundDrawablesRelativeWithIntrinsicBounds(null,
					getResources().getDrawable(R.drawable.tab_msg_selector),
					null, null);
		}
	}

	@Override
	public boolean handleMessage(Message msg) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * 消息分发，选择跳转到哪个fragment
	 * 
	 * @param intent
	 */
	Fragment clfragment = null;

	private void enterFragment(Intent intent) {
		String tag = null;
		if (intent != null) {

			// if (intent.getData() != null) {
			// if
			// (intent.getData().getLastPathSegment().equals("conversationlist"))
			// {
			tag = "conversationlist";
			String fragmentName = ConversationListFragment.class
					.getCanonicalName();
			clfragment = Fragment.instantiate(this, fragmentName);
			// }
			// }

			if (clfragment != null) {
				FragmentTransaction transaction = getSupportFragmentManager()
						.beginTransaction();
				transaction.add(R.id.ll_view1, clfragment, tag);
				transaction.addToBackStack(null).commitAllowingStateLoss();
			}
		}
	}

	String[] fragmentsTag = { "AddFriendsFragment", "FriendDetailFragment",
			"NewFriendsFragment", "NpcsFragment","ExpertDetailFragment","ExpertListFragment" };

	private boolean isNeedRemove() {
		BaseFragment fragment  = null;
		for(String tag : fragmentsTag){
			fragment = (BaseFragment) getSupportFragmentManager()
					.findFragmentByTag(tag);
			if (fragment != null && fragment.isVisible()) {
				getSupportFragmentManager().popBackStack();
				return true;
			}
		}
		return false;
	}
}

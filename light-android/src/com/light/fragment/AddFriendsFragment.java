package com.light.fragment;

import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.joanzapata.android.BaseAdapterHelper;
import com.joanzapata.android.QuickAdapter;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.light.R;
import com.light.activity.HomeActivity;
import com.light.bean.FriendBean;
import com.light.bean.ResponseBean;
import com.light.network.Constant;
import com.light.presenter.IHomePresenter;
import com.light.util.StringUtils;
import com.light.util.ToastUtils;

public class AddFriendsFragment extends BaseFragment implements
		SwipeRefreshLayout.OnRefreshListener, OnItemClickListener {

	protected int mCurrentPage = 0;

	@ViewInject(R.id.et_search_content)
	private EditText contentEt;

	@ViewInject(R.id.swiperefreshlayout)
	protected SwipeRefreshLayout mSwipeRefreshLayout;

	@ViewInject(R.id.lv_show_friends)
	private ListView mListView;

	private IHomePresenter homePresenter;

	private QuickAdapter<FriendBean> mAdapter;

	private View mFooterView;

	private int pageNo = 1;
	private int pageSize = 5;
	
	@ViewInject(R.id.iv_show_leftmenu)
	private ImageView backIv;

	@ViewInject(R.id.tv_title)
	private TextView title;
	
	@OnClick(R.id.iv_show_leftmenu)
	private void backAction(View view) {
		//隐藏输入框
		if(imm.isActive()){
			imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
		}
		this.dismiss();
	}
	
	private boolean isNoData;
	
	private InputMethodManager imm ;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

		View view = super.onCreateView(inflater, container, savedInstanceState,
				R.layout.fragment_add_friends, "AddFriendsFragment");

		homePresenter = ((HomeActivity) getActivity()).homePresenter;
		

		if (contentEt != null) {
			contentEt.setOnEditorActionListener(new OnEditorActionListener() {

				@Override
				public boolean onEditorAction(TextView v, int actionId,
						KeyEvent event) {
					if (actionId == EditorInfo.IME_ACTION_SEARCH) {
						pageNo = 1;
						isNoData = false;
						if (friendsListData != null) {
							friendsListData.clear();
							mAdapter.clear();
							mState = STATE_NONE;
						}
						doSearchAction();
						return true;
					}
					return false;
				}

			});
		}

		return view;
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
	}

	private String key;

	private void doSearchAction() {
		key = contentEt.getText().toString();
		if (!StringUtils.isBlank(key)) {
			setSwipeRefreshLoadingState();
			homePresenter.queryFriends(key, pageNo, pageSize);
		} else {
			ToastUtils.show(context, "请输入要搜索的内容");
		}
	}

	private List<FriendBean> friendsListData;

	@Override
	public void onEvent(ResponseBean event) {

		if (event.getResult_code() == Constant.CODE_SUCCESS) {
			if (Constant.SEARCH_FRIENDS.equals(event.getRequestUrl())) {
				if (friendsListData == null) {

					friendsListData = event.getFriends();

					mAdapter = new QuickAdapter<FriendBean>(context,
							R.layout.friend_list_item, friendsListData) {

						@Override
						protected void convert(BaseAdapterHelper helper,
								FriendBean item) {

							helper.setText(R.id.tv_friend_name, item.getName());
							helper.setImageUrl(R.id.iv_friend_avatar,
									item.getAvatar());
							helper.setText(R.id.tv_friend_id, item.getId() + "");
							helper.setVisible(R.id.tv_index_tip, false);
						}
					};

					if(friendsListData.size()==pageSize){
						mListView.addFooterView(mFooterView);
					}else{
						mListView.removeFooterView(mFooterView);
					}
					// 设置适配器
					mListView.setAdapter(mAdapter);
				} else {
					List<FriendBean> friendsListDataExtr = event.getFriends();
					if (isListEmpty(friendsListData)) {
						friendsListData = friendsListDataExtr;
						if(friendsListData.size()==pageSize){
							mListView.addFooterView(mFooterView);
						}else{
							mListView.removeFooterView(mFooterView);
						}
					}
					footerButton.setText("点击加载更多");
					footerPb.setVisibility(View.GONE);

					if (friendsListDataExtr != null) {
						mAdapter.addAll(friendsListDataExtr);
					}
				}
			}

		} else if (event.getResult_code() == 1) {
			isNoData = true;
			ToastUtils.show(context, "无法查询更多信息");
			mListView.removeFooterView(mFooterView);
		} else {
			ToastUtils.show(context, event.getResult_msg());
		}

		setSwipeRefreshLoadedState();

		if(friendsListData.size() < pageSize){
			mState = STATE_NOMORE;
		}else{
			mState = STATE_LOADMORE;
		}
	}

//	@Override
//	public void onScrollStateChanged(AbsListView view, int scrollState) {
//		// TODO Auto-generated method stub
//
//	}
//
//	@Override
//	public void onScroll(AbsListView view, int firstVisibleItem,
//			int visibleItemCount, int totalItemCount) {
////		int lastItem = firstVisibleItem + visibleItemCount;
////		if (lastItem == totalItemCount && mListView
////				.getChildCount()>0) {
////			View lastItemView = (View) mListView.getChildAt(mListView
////					.getChildCount() - 1);
////			if (lastItemView != null
////					&& (mListView.getBottom()) == lastItemView.getBottom()) {
////				if (mFooterView != null && mListView.getFooterViewsCount() != 0) {
////					mListView.removeFooterView(mFooterView);
////
////				}
////				if (!isNoData) {
////					if (mState == STATE_REFRESH) {
////						return;
////					}
////					if (mFooterView != null) {
////						// 若有需求再add上FooterView
////						mListView.addFooterView(mFooterView);
////						mState = STATE_REFRESH;
////						requestData(true);
////					}
////				}
////			}
////		}
//	}

	private FriendDetailFragment friendDetailFragment;
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {

		FriendBean bean = mAdapter.getItem(position);
		
		friendDetailFragment = new FriendDetailFragment(bean,FriendDetailFragment.FRIEND_ADD);
		friendDetailFragment.show(getFragmentManager(), "FriendDetailFragment");
	}

	@Override
	public void onRefresh() {
		if (mState == STATE_REFRESH || mState == STATE_NOMORE) {
			setSwipeRefreshLoadedState();
			return;
		}

		if (isListEmpty(friendsListData)) {
			setSwipeRefreshLoadedState();
			return;
		}
		// 设置顶部正在刷新
		mListView.setSelection(0);
		setSwipeRefreshLoadingState();
		mCurrentPage = 0;
		mState = STATE_REFRESH;
		requestData(true);
	}

	/** 设置顶部正在加载的状态 */
	private void setSwipeRefreshLoadingState() {
		if (mSwipeRefreshLayout != null) {
			mSwipeRefreshLayout.setRefreshing(true);
			// 防止多次重复刷新
			mSwipeRefreshLayout.setEnabled(false);
		}
	}

	/** 设置顶部加载完毕的状态 */
	private void setSwipeRefreshLoadedState() {
		if (mSwipeRefreshLayout != null) {
			mSwipeRefreshLayout.setRefreshing(false);
			mSwipeRefreshLayout.setEnabled(true);
		}
	}

	/***
	 * 获取列表数据
	 */
	protected void requestData(boolean refresh) {

		pageNo++;
		doSearchAction();
	}
	

	// 第一次点击的时间 long型  
	private long firstClick = 0;  
	// 最后一次点击的时间  
	private long lastClick = 0;  
	private int count;
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		initView(view);

		title.setText("添加好友");
		
		backIv.setImageResource(R.drawable.icons_back);
		
		title.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					// 如果第二次点击 距离第一次点击时间过长 那么将第二次点击看为第一次点击
					if (firstClick != 0
							&& System.currentTimeMillis() - firstClick > 500) {
						count = 0;
					}
					count++;
					if (count == 1) {
						firstClick = System.currentTimeMillis();
					} else if (count == 2) {
						lastClick = System.currentTimeMillis();
						// 两次点击小于500ms 也就是连续点击
						if (lastClick - firstClick < 500) {
							onRefresh();
						}
						clear();
					}
				}
				return false;

			}
		});
	}
	
	 // 清空状态  
    private void clear()  
    {  
        count = 0;  
        firstClick = 0;  
        lastClick = 0;  
    } 

	private TextView footerButton;
	private ProgressBar footerPb;

	public void initView(View view) {

//		mListView.setOnScrollListener(this);
		mListView.setOnItemClickListener(this);
		mSwipeRefreshLayout.setOnRefreshListener(this);
		mSwipeRefreshLayout.setColorSchemeResources(
				R.color.swiperefresh_color1, R.color.swiperefresh_color2,
				R.color.swiperefresh_color3, R.color.swiperefresh_color4);

		LayoutInflater layoutInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mFooterView = layoutInflater.inflate(R.layout.listview_footer, null);

		footerPb = (ProgressBar) mFooterView
				.findViewById(R.id.footer_progressBar);
		footerButton = (TextView) mFooterView.findViewById(R.id.footer_button);
		footerButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mState == STATE_REFRESH) {
					return;
				}
				footerButton.setText("正在加载...");
				footerPb.setVisibility(View.VISIBLE);

				mState = STATE_REFRESH;
				requestData(true);
			}

		});

	}

	private <T> boolean isListEmpty(List<T> list) {
		if (list == null || list.size() == 0)
			return true;
		return false;

	}
}

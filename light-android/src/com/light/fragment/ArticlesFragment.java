package com.light.fragment;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import cn.smssdk.a;

import com.light.R;
import com.light.activity.ArticlesActivity;
import com.light.adapter.ArticlesAdapter;
import com.light.app.LightApplication;
import com.light.bean.Article;
import com.light.bean.ResponseBean;
import com.light.network.CallBack;
import com.light.network.Constant;
import com.light.util.LibraryUtils;
import com.light.util.ScreenUtils;
import com.light.util.ToastUtils;

public class ArticlesFragment extends BaseFragment implements CallBack,
		SwipeRefreshLayout.OnRefreshListener, OnItemClickListener,
		OnScrollListener {

	public static final int STATE_NONE = 0;
	public static final int STATE_REFRESH = 1;
	public static final int STATE_LOADMORE = 2;
	public static final int STATE_NOMORE = 3;
	public static final int STATE_PRESSNONE = 4;// 正在下拉但还没有到刷新的状态
	public static int mState = STATE_NONE;
	private static final String TAG = "ArticlesFragment";
	private String requestUrl;
	ListView articles;
	private View mFooterView;
	SwipeRefreshLayout mSwipeRefreshlayout;
	private List<Article> arraylist;
	private boolean isNoData;
	ArticlesAdapter mAdapter;
	private int page_no;
	private int page_size;

	private TextView footerButton;
	private ProgressBar footerPb;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		int withPx = ScreenUtils.getScreenWidthPx(getActivity());
		LightApplication.getInstance().setWithPx(withPx);
		View v = inflater.inflate(R.layout.fragment_article, container, false);
		mFooterView = inflater.inflate(R.layout.listview_footer, null);
		mSwipeRefreshlayout = (SwipeRefreshLayout) v
				.findViewById(R.id.swiperly);
		articles = (ListView) v.findViewById(R.id.lv_article);
		initView();
		context = getActivity();
		page_no = 1;
		page_size = 10;
		isNoData = false;
		this.requestUrl = Constant.ARTICLE_URL;
		arraylist = null;
		getArticles();
		return v;
	}

	private void initView() {
		// TODO Auto-generated method stub
		articles.setOnScrollListener(this);
		articles.setOnItemClickListener(this);
		mSwipeRefreshlayout.setOnRefreshListener(this);
		mSwipeRefreshlayout.setColorSchemeResources(
				R.color.swiperefresh_color1, R.color.swiperefresh_color2,
				R.color.swiperefresh_color3, R.color.swiperefresh_color4);
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
				getArticles();
			}
		});

	}

	@Override
	public void onNetworkFinished(boolean isSuccess, String reuslt) {
		// TODO Auto-generated method stub
		if (isSuccess) {
			ResponseBean response = LibraryUtils.formatJsonToTargetClass(
					reuslt, ResponseBean.class);
			int resultCode = response.getResult_code();
			if (resultCode == Constant.CODE_SUCCESS) {
				if (arraylist == null) {
					arraylist = response.getArticles();
					Log.d(TAG, "result:" + arraylist.size());
					mAdapter = new ArticlesAdapter(getActivity(), arraylist);
					articles.addFooterView(mFooterView);
					// 设置适配器
					articles.setAdapter(mAdapter);
					page_no++;
				} else {
					List<Article> addList = response.getArticles();
					Log.d(TAG, Integer.toString(addList.size()));
					if (addList.size() != 0) {
						for (int i = 0; i < addList.size(); i++) {
							arraylist.add(addList.get(i));
						}
						mAdapter.notifyDataSetChanged();
						page_no++;
						footerButton.setText("点击刷新");
						footerPb.setVisibility(View.GONE);
					} else {
						isNoData = true;
						Log.d(TAG, "No More");
						footerButton.setText("No More");
						footerPb.setVisibility(View.GONE);
						articles.removeFooterView(mFooterView);
					}
				}
			} else {
				articles.removeFooterView(mFooterView);
			}

		}
		setSwipeRefreshLoadedState();

		mState = STATE_NONE;

	}

	private void setSwipeRefreshLoadedState() {
		// TODO Auto-generated method stub
		if (mSwipeRefreshlayout != null) {
			mSwipeRefreshlayout.setRefreshing(false);
			mSwipeRefreshlayout.setEnabled(true);
		}
	}

	/** 设置顶部正在加载的状态 */
	private void setSwipeRefreshLoadingState() {
		if (mSwipeRefreshlayout != null) {
			mSwipeRefreshlayout.setRefreshing(true);
			// 防止多次重复刷新
			mSwipeRefreshlayout.setEnabled(false);
		}
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		// int lastItem = firstVisibleItem + visibleItemCount;
		// if (lastItem == totalItemCount) {
		// View lastItemView = (View) articles.getChildAt(articles
		// .getChildCount() - 1);
		// if (lastItemView != null
		// && (articles.getBottom()) == lastItemView.getBottom()) {
		// if (mFooterView != null && articles.getFooterViewsCount() != 0) {
		// articles.removeFooterView(mFooterView);
		//
		// }
		// if (!isNoData) {
		// if (mState == STATE_REFRESH) {
		// return;
		// }
		// if (mFooterView != null) {
		// // 若有需求再add上FooterView
		// articles.addFooterView(mFooterView);
		// mState = STATE_REFRESH;
		// getArticles();
		// }
		// }
		// }
		// }

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		Article data = (Article) mAdapter.getItem(position);
		Intent intent = new Intent();
		intent.setClass(getActivity(), ArticlesActivity.class);
		Bundle bundle = new Bundle();
		bundle.putSerializable("Article", data);
		intent.putExtras(bundle);
		getActivity().startActivity(intent);

	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		if (mState == STATE_REFRESH) {
			return;
		}
		if (arraylist.size() == 0 || arraylist == null) {
			setSwipeRefreshLoadedState();
			return;
		}
		articles.setSelection(0);
		setSwipeRefreshLoadingState();
		mState = STATE_REFRESH;
		getArticles();
		ToastUtils.show(getActivity(), "Refresh");
	}

	private void getArticles() {
		// TODO Auto-generated method stub
		Log.d(TAG, Integer.toString(page_no));
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("page_size", page_size);
		map.put("page_no", page_no);
		String inputJson = LibraryUtils.formatJson(map);
		LibraryUtils.httpPost(requestUrl, inputJson, this);
	}

}

package com.light.activity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.light.R;
import com.light.adapter.CertificateAdapter;
import com.light.bean.CertificateBean;
import com.light.bean.ResponseBean;
import com.light.network.CallBack;
import com.light.network.Constant;
import com.light.util.LibraryUtils;

public class CertificateActivity extends BaseActivity implements CallBack {

	// 没有状态
	public static final int LISTVIEW_ACTION_NONE = -1;
	// 初始化时，加载缓存状态
	public static final int LISTVIEW_ACTION_INIT = 1;
	// 刷新状态，显示toast
	public static final int LISTVIEW_ACTION_REFRESH = 2;
	// 下拉到底部时，获取下一页的状态
	public static final int LISTVIEW_ACTION_SCROLL = 3;

	static final int STATE_NONE = -1;
	static final int STATE_LOADING = 0;
	static final int STATE_LOADED = 1;

	private static String TAG = "CertificateActivity";
	@ViewInject(R.id.tv_title)
	private TextView title;
	@ViewInject(R.id.iv_show_leftmenu)
	private ImageView backIv;
	@ViewInject(R.id.iv_add)
	private ImageView addIv;
	@ViewInject(R.layout.listview_footer)
	private View mFooterView;
	@ViewInject(R.id.lv_certificate)
	private ListView mListView;
	@ViewInject(R.id.footer_progressBar)
	private ProgressBar mFooterProgressBar;
	@ViewInject(R.id.footer_button)
	private TextView mFooterTextView;

	private int page_no;
	private int page_size;
	private String requestUrl;
	private CertificateAdapter adapter;
	private List<CertificateBean> certificates;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		page_no = 1;
		page_size = 2;
		requestUrl = Constant.CERTIFICATE_URL;
		setContentView(R.layout.activity_certificate);
		initView();
		requestData();

	}

	private void requestData() {
		// TODO Auto-generated method stub
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("page_size", page_size);
		map.put("page_no", page_no);
		String inputJson = LibraryUtils.formatJson(map);
		Log.d(TAG, inputJson);
		LibraryUtils.httpPost(requestUrl, inputJson, this);
	}

	private void initView() {
		ViewUtils.inject(this);
		title.setText("证书墙");
		backIv.setImageResource(R.drawable.icons_back);
		backIv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});

	}

	void setFooterFullState() {
		if (mFooterView != null) {
			mFooterProgressBar.setVisibility(View.GONE);
			mFooterTextView.setText("已加载全部");
		}
	}

	/**
	 * 设置底部加载中的状态
	 */
	void setFooterLoadingState() {
		if (mFooterView != null) {
			mFooterProgressBar.setVisibility(View.VISIBLE);
			mFooterTextView.setText("加载中");
		}
	}

	@Override
	public void onNetworkFinished(boolean isSuccess, String reuslt) {
		if (isSuccess) {
			getData(reuslt);
		} else {

		}
	}

	private void getData(String result) {
		// TODO Auto-generated method stub
		ResponseBean response = LibraryUtils.formatJsonToTargetClass(result,
				ResponseBean.class);
		int resultCode = response.getResult_code();
		if (resultCode == Constant.CODE_SUCCESS) {
			if (certificates == null) {
				certificates = response.getCertificates();
				adapter = new CertificateAdapter(CertificateActivity.this,
						R.layout.list_item_certificate, certificates);
				// mListView.addFooterView(mFooterView);
				// 设置适配器
				mListView.setAdapter(adapter);
				page_no++;
			} else {
				List<CertificateBean> addList = response.getCertificates();
				if (addList.size() != 0) {
					for (int i = 0; i < addList.size(); i++) {
						certificates.add(addList.get(i));
					}
					adapter.notifyDataSetChanged();
					page_no++;
				}

			}
		}
	}
}

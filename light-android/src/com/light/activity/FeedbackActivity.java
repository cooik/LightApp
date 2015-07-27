package com.light.activity;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.JsonArray;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.light.R;
import com.light.bean.ResponseBean;
import com.light.network.CallBack;
import com.light.network.Constant;
import com.light.util.LibraryUtils;
import com.light.util.SessionUtils;
import com.light.util.TDevice;
import com.light.util.ToastUtils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class FeedbackActivity extends Activity implements CallBack {
	public static String TAG = "FeedbackActivity";
	public static final int CONTENT_NULL = 0;
	public static final int SUCCESS_SEND = 1;
	@ViewInject(R.id.tv_title)
	private TextView title;
	@ViewInject(R.id.iv_show_leftmenu)
	private ImageView backIv;
	@ViewInject(R.id.submit)
	private Button btn;
	@ViewInject(R.id.et_feedback)
	private EditText et;
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			String text = null;
			switch (msg.what) {
			case CONTENT_NULL:
				text = "反馈内容不能为空";
				break;
			case SUCCESS_SEND:
				text = "谢谢您的反馈";
			default:
				break;
			}
			ToastUtils.show(FeedbackActivity.this, text);
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_feedback);
		initView();
	}

	private void initView() {
		ViewUtils.inject(this);
		title.setText("用户反馈");
		backIv.setImageResource(R.drawable.icons_back);
		backIv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				TDevice.hideSoftKeyboard(FeedbackActivity.this
						.getCurrentFocus());
				finish();
			}
		});
		btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (et.getText().toString().equals("")
						|| et.getText().toString().trim().equals("")) {
					Message message = new Message();
					message.what = CONTENT_NULL;
					handler.sendMessage(message);

				} else {
					sendTo(et.getText().toString());
				}
			}

		});

	}

	protected void sendTo(String content) {
		Log.d(TAG, "sendto");
		String url = "http://123.57.221.116:8080/light-server/intf/feedback/add.shtml";
		int user_id = SessionUtils.getInstance().getUserBean().getId();
		Log.d(TAG, "UserId------>" + user_id);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("user_id", user_id);
		map.put("content", content);
		String inputJson = LibraryUtils.formatJson(map);
		Log.d(TAG, inputJson);
		LibraryUtils.httpPost(url, inputJson, this);
	}

	@Override
	public void onNetworkFinished(boolean isSuccess, String reuslt) {
		if (isSuccess) {
			ResponseBean response = LibraryUtils.formatJsonToTargetClass(
					reuslt, ResponseBean.class);
			int resultCode = response.getResult_code();
			Log.d(TAG, response.toString());
			if (resultCode == Constant.CODE_SUCCESS) {
				Message message = new Message();
				message.what = SUCCESS_SEND;
				handler.sendMessage(message);
			}
		} else {
			Toast.makeText(getApplicationContext(), "提交失败", Toast.LENGTH_SHORT)
					.show();
		}
	}
}

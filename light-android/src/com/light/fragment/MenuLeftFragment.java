package com.light.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.light.R;
import com.light.activity.AboutActivity;
import com.light.activity.ArticlesActivity;
import com.light.activity.FeedbackActivity;
import com.light.activity.SettingsActivity;
import com.light.bean.Article;
import com.light.bean.UserBean;
import com.light.util.SessionUtils;
import com.squareup.picasso.Picasso;

public class MenuLeftFragment extends BaseFragment {

	private static final String TAG = "MenuLeftFragment";

	public ImageView userface;
	public TextView username;
	public LinearLayout view_about;
	public RelativeLayout view_head;
	public LinearLayout view_feedback;
	public LinearLayout view_settings;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = super.onCreateView(inflater, container, savedInstanceState,
				R.layout.layout_menu, TAG);
		Log.d(TAG, "getuserface And username");
		userface = (ImageView) view.findViewById(R.id.menu_user_info_userface);
		username = (TextView) view.findViewById(R.id.menu_user_info_username);
		view_about = (LinearLayout) view.findViewById(R.id.menu_about);
		view_head = (RelativeLayout) view.findViewById(R.id.menu_user_layout);
		view_feedback = (LinearLayout) view.findViewById(R.id.menu_back);
		view_settings = (LinearLayout) view
				.findViewById(R.id.menu_item_settings);

		Log.d(TAG, "getuserface And username");
		initEvet();
		initMenu();
		return view;
	}

	private void initEvet() {
		view_about.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), AboutActivity.class);
				startActivity(intent);
			}
		});
		view_head.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Toast.makeText(getActivity(), "change icon", Toast.LENGTH_SHORT)
						.show();
			}
		});
		view_feedback.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				int userId = SessionUtils.getInstance().getUserBean().getId();
				Intent intent = new Intent();
				intent.setClass(getActivity(), FeedbackActivity.class);
				intent.putExtra("UserId", userId);
				getActivity().startActivity(intent);
			}
		});
		view_settings.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(getActivity(), SettingsActivity.class);
				startActivity(intent);
			}
		});
	}

	private void initMenu() {

		if(SessionUtils.getInstance().getUserBean()!=null){
			
			Picasso.with(context)
			.load(SessionUtils.getInstance().getUserBean().getAvatar())
			.into(userface);
			username.setText(SessionUtils.getInstance().getUserBean().getName());
		}
	}
}

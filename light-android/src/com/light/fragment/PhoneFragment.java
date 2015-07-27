package com.light.fragment;

import android.content.Intent;
import android.net.wifi.WifiConfiguration.Protocol;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.light.R;
import com.light.activity.ProtocolActivity;
import com.light.activity.RegistActivity;

public class PhoneFragment extends BaseFragment {

	@ViewInject(R.id.btn_email_regist)
	private Button changeEmailRegist;

	@ViewInject(R.id.tv_regist_tip)
	private TextView tip;

	@ViewInject(R.id.tv_go_login)
	private TextView hasCodeTv;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = super.onCreateView(inflater, container, savedInstanceState,
				R.layout.phone_send_fragment, "PhoneFragment");

		tip.setText(Html
				.fromHtml(getResources().getString(R.string.regist_tip)));
		tip.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				getActivity().startActivity(
						new Intent(getActivity(), ProtocolActivity.class));
			}
		});
		return view;
	}

	@OnClick(R.id.btn_email_regist)
	private void changeRegist(View view) {
		((RegistActivity) getActivity()).changeFragment(
				R.id.regit_phone_fragment, new EmailFragment());
	}

	@OnClick(R.id.tv_go_login)
	private void goLoing(View v) {
		getActivity().finish();
	}
}

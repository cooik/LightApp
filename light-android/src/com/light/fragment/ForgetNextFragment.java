package com.light.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import cn.smssdk.SMSSDK;

import com.light.R;
import com.light.util.SMSUtils;

public class ForgetNextFragment extends BaseFragment {
	TextView tv_user_id;
	String phone;
	int user_id;
	Handler handler;

	public ForgetNextFragment(String phone, int user_id) {
		this.phone = phone;
		this.user_id = user_id;
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = super.onCreateView(inflater, container, savedInstanceState,
				R.layout.fragment_forget_next, "ForgetNextFragment");
		tv_user_id = (TextView) view.findViewById(R.id.user_id);
		tv_user_id.setText(phone);
		handler = new Handler((Callback) getActivity());
		SMSUtils.getInstance().initSdk(context, handler);
		SMSSDK.getVerificationCode("86", phone);
		Button btn = (Button) view.findViewById(R.id.next);
		btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				FragmentTransaction ft = getActivity()
						.getSupportFragmentManager().beginTransaction();
				ft.setCustomAnimations(R.anim.push_left_in,
						R.anim.push_left_out);
				ft.replace(R.id.forget_fragment,
						new ForgetVaildFragment(user_id, phone))
						.addToBackStack(null).commit();

			}
		});
		return view;
	}

}

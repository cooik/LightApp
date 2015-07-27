package com.light.fragment;

import java.security.cert.Certificate;

import com.light.R;
import com.light.activity.CertificateActivity;
import com.light.activity.MarryActivity;
import com.light.activity.ProposeActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class ForLoveFragment extends BaseFragment implements OnClickListener {

	RelativeLayout rl_propose;
	RelativeLayout rl_certificate;
	RelativeLayout rl_marry;
	View view;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_love, container, false);
		initView();
		return view;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	private void initView() {
		// TODO Auto-generated method stub
		rl_propose = (RelativeLayout) view.findViewById(R.id.propose);
		rl_certificate = (RelativeLayout) view.findViewById(R.id.certificate);
		rl_marry = (RelativeLayout) view.findViewById(R.id.marry);
		rl_propose.setOnClickListener(this);
		rl_certificate.setOnClickListener(this);
		rl_marry.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent = new Intent();
		switch (v.getId()) {
		case R.id.propose:
			Log.d("ItemClick", "Click----->Propose");
			intent.setClass(getActivity(), ProposeActivity.class);

			break;
		case R.id.certificate:
			Log.d("ItemClick", "Click----->Certificate");
			intent.setClass(getActivity(), CertificateActivity.class);

			break;
		case R.id.marry:
			Log.d("ItemClick", "Click----->Marry");
			intent.setClass(getActivity(), MarryActivity.class);

			break;
		}
		startActivity(intent);
	}
}

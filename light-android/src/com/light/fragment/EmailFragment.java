package com.light.fragment;

import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.light.R;
import com.light.activity.RegistActivity;
import com.light.bean.ResponseBean;
import com.light.network.Constant;
import com.light.presenter.IUserRegistPresenter;
import com.light.util.RegexUtil;
import com.light.util.StringUtils;
import com.light.util.ToastUtils;

public class EmailFragment extends BaseFragment{
	
	private static final String TAG = "EmailFragment";
	
	
	public EmailFragment(){
		
	}
	
	
	@ViewInject(R.id.btn_send_email)
	private Button sendEmail;

	@ViewInject(R.id.et_email)
	private EditText email;
	
	private IUserRegistPresenter registPresenter;
	
	@ViewInject(R.id.tv_regist_tip)
	private TextView tip;
	
	@ViewInject(R.id.btn_phone_regist)
	private Button changePhoneRegist;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		registPresenter = ((RegistActivity)getActivity()).registPresenter;
		
		View view = super.onCreateView(inflater, container, savedInstanceState, R.layout.email_send_fragment,"EmailFragment");
		tip.setText(Html.fromHtml(getResources().getString(R.string.regist_tip)));

		return view;
	}
	
	private String userCode;
	
	@OnClick(R.id.btn_send_email)
	private void sendEmailAction(View view){
		String emailAddress = email.getEditableText().toString();
		if(!StringUtils.isBlank(emailAddress)&&RegexUtil.isEmail(emailAddress)){
			userCode = emailAddress;
			registPresenter.registCode(emailAddress);
		}else{
			ToastUtils.show(context, "请输入正确的邮箱地址");
		}
		
	}
	
	
	
	private int userId;
	public void onEvent(ResponseBean event) {
		if(event.getResult_code() == Constant.CODE_SUCCESS){
			if(Constant.REGIST_URL.equals(event.getRequestUrl())){
				Log.d(TAG, "user_id:" + event.getUser_id());
				userId = event.getUser_id();
				
				((RegistActivity)getActivity()).changeFragment(R.id.regit_phone_fragment, new RegistValidFragment(userId,userCode,false));
				
			}
//			else if(Constant.VALID_URL.equals(event.getRequestUrl())){
//				int result = event.getValidate_result();
//				if(result == Constant.VALIDATE_SUCCESS){
//					Toast.makeText(getActivity(), "验证成功", Toast.LENGTH_LONG).show();
//					
//					handler.obtainMessage(Constant.WHAT_VALID_EMAIL_SUCCESS,userId,0,userCode).sendToTarget();
//					
//				}else{
//					Toast.makeText(getActivity(), "验证失败", Toast.LENGTH_LONG).show();
//				}
//			}
		}else{
			ToastUtils.show(getActivity(), event.getResult_msg());
		}
	}

	
	
	@OnClick(R.id.btn_phone_regist)
	private void changeRegist(View view){
		((RegistActivity)getActivity()).changeFragment(R.id.regit_phone_fragment, new PhoneFragment());
	}

}

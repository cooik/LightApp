package com.light.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import cn.smssdk.SMSSDK;

import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.light.R;
import com.light.activity.RegistActivity;
import com.light.bean.ResponseBean;
import com.light.network.Constant;
import com.light.presenter.IUserRegistPresenter;
import com.light.presenter.impl.UserRegistPresenterImpl;
import com.light.util.StringUtils;
import com.light.util.ToastUtils;

public class RegistValidFragment extends BaseFragment{
	
	private int userId;
	
	private String code;
	
	private boolean isPhone;
	
	private Handler handler;
	
	public void setHandler(Handler handler){
		this.handler = handler;
	}
	
	
	public RegistValidFragment(int userId,String code,boolean isPhone){
		this.userId = userId;
		this.code = code;
		this.isPhone = isPhone;
	}
	
	@ViewInject(R.id.btn_valid)
	private Button validBtn;
	
	@ViewInject(R.id.et_validcode)
	private EditText etValidCode;
	
	
	private IUserRegistPresenter registPresenter;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		registPresenter = ((RegistActivity)getActivity()).registPresenter;
		
		return super.onCreateView(inflater, container, savedInstanceState,R.layout.valid_fragment,"RegistValidFragment");
	}
	
	
	@OnClick(R.id.btn_valid)
	private void sendValidAction(View view){
		String validCode = etValidCode.getEditableText().toString();
		if(!StringUtils.isBlank(validCode)&&userId!=0){
			if(isPhone){
				registPresenter.validCode(userId, "00000000");
				SMSSDK.submitVerificationCode("86", code, validCode);
			}else{
				registPresenter.validCode(userId, validCode);
			}
		}
	}

	@Override
	public void onEvent(ResponseBean event) {
		if(event.getResult_code() == Constant.CODE_SUCCESS){
			if(Constant.VALID_URL.equals(event.getRequestUrl())){
				int result = event.getValidate_result();
				if(result == Constant.VALIDATE_SUCCESS){
					ToastUtils.show(getActivity(), "验证成功", Toast.LENGTH_LONG);
					if(!isPhone)
					handler.obtainMessage(Constant.WHAT_VALID_EMAIL_SUCCESS,userId,0,code).sendToTarget();
					
				}else{
					ToastUtils.show(getActivity(), "验证失败", Toast.LENGTH_LONG);
				}
			}
		}else{
			ToastUtils.show(getActivity(), event.getResult_msg());
		}
	}
}

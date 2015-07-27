package com.light.fragment;

import java.util.HashMap;
import java.util.Map;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.lidroid.xutils.view.annotation.event.OnRadioGroupCheckedChange;
import com.light.R;
import com.light.activity.RegistActivity;
import com.light.bean.ResponseBean;
import com.light.network.Constant;
import com.light.presenter.IUserRegistPresenter;
import com.light.util.StringUtils;
import com.light.util.ToastUtils;

public class RegistAddInfoFragment extends BaseFragment{
	
	private int layoutId;
	private int userId;
	
	private String nikeName;
	private String pwd;
	
	public RegistAddInfoFragment(int layoutId,int userId){
		this.layoutId = layoutId;
		this.userId = userId;
	}
	
	public RegistAddInfoFragment(int layoutId,int userId,String nikeName,String pwd){
		this.layoutId = layoutId;
		this.userId = userId;
		this.nikeName = nikeName;
		this.pwd = pwd;
	}
	
	
	@ViewInject(R.id.et_user_pwd)
	private EditText pwdEditText;
	
	@ViewInject(R.id.et_user_nickname)
	private EditText nameEditText;
	
	@ViewInject(R.id.rg_user_physiology_gender)
	private RadioGroup physiologyGenderSelector;

	@ViewInject(R.id.rg_user_society_gender)
	private RadioGroup societyGenderSelector;

	@ViewInject(R.id.btn_info_submit)
	private Button submitInfo;
	
	@ViewInject(R.id.btn_next)
	private Button nextBtn;
	
	private IUserRegistPresenter registPresenter;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = super.onCreateView(inflater, container, savedInstanceState,layoutId,"RegistAddInfoFragment");
		
		registPresenter = ((RegistActivity)getActivity()).registPresenter;
		
		if (pwdEditText != null) {
			pwdEditText.setOnEditorActionListener(new OnEditorActionListener() {

				@Override
				public boolean onEditorAction(TextView v, int actionId,
						KeyEvent event) {
					if (actionId == EditorInfo.IME_ACTION_NEXT
							|| (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
						next();
						return true;
					}
					return false;
				}

			});
		}
		return view;
	}
	
	
	private LoadingFragment loading;
	
	@OnClick(R.id.btn_info_submit)
	private void submit(View view) {
		if (StringUtils.isBlank(nikeName)) {
			// 处理逻辑
			return;
		}

		if (StringUtils.isBlank(pwd)) {
			return;
		}

		loading = new LoadingFragment("正在创建账户,\n请稍等");
		loading.show(getFragmentManager(), "loading");

		Map<String, Object> infoMap = new HashMap<String, Object>();
		infoMap.put("user_id", userId);
		infoMap.put("pwd", pwd);
		infoMap.put("name", nikeName);
		infoMap.put("physiology_gender", physiologyGender);
		infoMap.put("society_gender", societyGender);

		registPresenter.addInfo(infoMap);
	}
	
	private int physiologyGender = 0; // 生理性别 默认女

	@OnRadioGroupCheckedChange(R.id.rg_user_physiology_gender)
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
		case R.id.rb_physiology_gender_0:
			physiologyGender = 0;
			break;
		case R.id.rb_physiology_gender_1:
			physiologyGender = 1;
			break;
		case R.id.rb_physiology_gender_2:
			physiologyGender = 2;
			break;

		default:
			physiologyGender = 0;
			break;
		}
	}

	private int societyGender = 0; // 社会性别 默认女

	@OnRadioGroupCheckedChange(R.id.rg_user_society_gender)
	public void onCheckedChanged2(RadioGroup group, int checkedId) {
		switch (checkedId) {
		case R.id.rb_society_gender_0:
			societyGender = 0;
			break;
		case R.id.rb_society_gender_1:
			societyGender = 1;
			break;
		case R.id.rb_society_gender_2:
			societyGender = 2;
			break;
		default:
			societyGender = 0;
			break;
		}
	}
	
	@OnClick(R.id.btn_next)
	private void nextAction(View view){
		next();
	}
	
	private void next(){
		if(!StringUtils.isBlank(pwdEditText.getText().toString())
				&&!StringUtils.isBlank(nameEditText.getText().toString())
				){
			((RegistActivity)getActivity()).changeFragment(R.id.regit_phone_fragment, new RegistAddInfoFragment(R.layout.regist_gender_selector,userId,
					nameEditText.getText().toString(),pwdEditText.getText().toString()));
		}else{
			ToastUtils.show(context, "账户名/密码不能为空");
		}
	}

	@Override
	public void onEvent(ResponseBean event) {
		if (Constant.ADDINFO_URL.equals(event.getRequestUrl())) {
			if (event.getResult_code() == Constant.CODE_SUCCESS) {
				ToastUtils.show(context, "注册成功");
				loading.dismiss();
				getActivity().finish();
			}
		}
	}
}

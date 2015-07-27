package com.light.fragment;

import java.util.HashMap;
import java.util.Map;

import cn.smssdk.SMSSDK;

import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.light.R;
import com.light.bean.ResponseBean;
import com.light.network.CallBack;
import com.light.network.Constant;
import com.light.util.LibraryUtils;
import com.light.util.StringUtils;
import com.light.util.ToastUtils;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ForgetVaildFragment extends BaseFragment implements CallBack {

	private int userId;

	private String code;

	private String phone;

	private boolean isPhone = true;

	private boolean isvaild = false;

	private Handler handler;

	public void setHandler(Handler handler) {
		this.handler = handler;
	}

	public ForgetVaildFragment(int user_id, String phone) {
		this.userId = user_id;
		this.phone = phone;
	}

	@ViewInject(R.id.vaild)
	Button vaild;
	@ViewInject(R.id.vaild_code)
	EditText et_vaild_code;
	@ViewInject(R.id.cancle)
	Button cancle;

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		Log.d("Phone", "ForgetVaildFragment" + phone + "");
		return super.onCreateView(inflater, container, savedInstanceState,
				R.layout.fragment_forget_input, "RegistValidFragment");
	}

	@OnClick(R.id.vaild)
	private void vaild(View v) {
		String validCode = et_vaild_code.getEditableText().toString();
		if (!StringUtils.isBlank(validCode) && userId != 0) {
			if (isPhone) {
				this.validCode(userId, "00000000");
				SMSSDK.submitVerificationCode("86", phone, validCode);

			} else {
				this.validCode(userId, validCode);
			}
		}
		if (isvaild == true) {
			FragmentTransaction ft = getActivity().getSupportFragmentManager()
					.beginTransaction();
			ft.setCustomAnimations(R.anim.push_left_in, R.anim.push_left_out);
			ft.replace(R.id.forget_fragment,
					new ForgetResetFragment(userId, phone))
					.addToBackStack(null).commit();
		}
	}

	private void validCode(int userId2, String validCode) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("user_id", userId);
		map.put("val_code", et_vaild_code.getEditableText().toString());
		String inputJson = LibraryUtils.formatJson(map);
		LibraryUtils.httpPost(Constant.VALID_URL, inputJson, this);
	}

	@Override
	public void onNetworkFinished(boolean isSuccess, String reuslt) {
		if (isSuccess) {
			ResponseBean response = LibraryUtils.formatJsonToTargetClass(
					reuslt, ResponseBean.class);
			response.setRequestUrl(Constant.VALID_URL);
			int resultCode = response.getResult_code();
			if (resultCode == Constant.CODE_SUCCESS) {
			}
			if (handler != null) {
				handler.obtainMessage(Constant.CODE_SUCCESS, response)
						.sendToTarget();
			} else {
				LibraryUtils.sendEvent(response);
			}
			response = null;
		} else {
			ResponseBean response = new ResponseBean();
			response.setResult_code(Constant.CODE_ERROR);
			response.setResult_msg(reuslt);
			LibraryUtils.sendEvent(response);
			response = null;
		}
	}

	@Override
	public void onEvent(ResponseBean event) {
		if (event.getResult_code() == Constant.CODE_SUCCESS) {
			if (Constant.VALID_URL.equals(event.getRequestUrl())) {
				int result = event.getValidate_result();
				if (result == Constant.VALIDATE_SUCCESS) {
					ToastUtils.show(getActivity(), "验证成功", Toast.LENGTH_LONG);
					isvaild = true;
					// if (!isPhone)
					// handler.obtainMessage(
					// Constant.WHAT_VALID_EMAIL_SUCCESS, userId, 0,
					// code).sendToTarget();

				} else {
					ToastUtils.show(getActivity(), "验证失败", Toast.LENGTH_LONG);
				}
			}
		} else {
			ToastUtils.show(getActivity(), event.getResult_msg());
		}
	}

}

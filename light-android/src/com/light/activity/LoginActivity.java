package com.light.activity;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler.Callback;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;

import com.lidroid.xutils.db.sqlite.WhereBuilder;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.light.R;
import com.light.app.LightApplication;
import com.light.bean.FriendBean;
import com.light.bean.ResponseBean;
import com.light.bean.UserBean;
import com.light.constant.PageContants;
import com.light.constant.SysConst;
import com.light.network.Constant;
import com.light.presenter.IUserLoginPresenter;
import com.light.presenter.impl.UserLoginPresenterImpl;
import com.light.util.ACache;
import com.light.util.LibraryUtils;
import com.light.util.PreferencesUtils;
import com.light.util.SessionUtils;
import com.light.util.ToastUtils;
import com.light.view.IUserLoginView;
import com.mob.tools.utils.UIHandler;

public class LoginActivity extends BaseActivity implements IUserLoginView,
		PlatformActionListener, Callback {

	private static final String TAG = "LoginActivity";
	@ViewInject(R.id.btn_login)
	private Button login;

	@ViewInject(R.id.btn_regist)
	private TextView regist;

	@ViewInject(R.id.et_usercode)
	private EditText userCode;

	@ViewInject(R.id.et_userpwd)
	private EditText userPwd;

	@ViewInject(R.id.iv_login_qq)
	private ImageView qqLoginIv;

	@ViewInject(R.id.iv_login_sina)
	private ImageView sinaLoginIv;

	@ViewInject(R.id.iv_login_weixin)
	private ImageView weixinLoginIv;

	@ViewInject(R.id.tv_forget_pwd)
	private TextView tv_forget_pwd;

	private IUserLoginPresenter loginPresenter;
	public UserBean userBean;

	private int type;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState, R.layout.activity_login,
				"LoginActivity");

		loginPresenter = new UserLoginPresenterImpl(this, context);
		loginPresenter.queryUserCodeAndPwdFromLocal();

		userCode.setSelection(userCode.getText().toString().length());

		/**
		 * ShareSdk
		 */
		ShareSDK.initSDK(this);
		ShareSDK.setConnTimeout(20000);
		ShareSDK.setReadTimeout(20000);

	}

	@Override
	public String getUserName() {
		return userCode.getEditableText().toString();
	}

	@Override
	public String getUserPwd() {
		return userPwd.getEditableText().toString();
	}

	@Override
	public void queryFromLocal(String name, String passwd) {
		userCode.setText(name);
		userPwd.setText(passwd);
	}

	@OnClick(R.id.btn_login)
	private void login(View view) {

		showWaitDialog("正在登录...");
		loginPresenter.login();
		LibraryUtils.onEvent(this, PageContants.EVENT_ID_LOGIN);
	}

	@OnClick(R.id.btn_regist)
	private void inRegist(View view) {
		Intent intent = new Intent(this, RegistActivity.class);
		startActivity(intent);

		LibraryUtils.onEvent(this, PageContants.EVENT_ID_REGIST);
	}

	@OnClick(R.id.tv_forget_pwd)
	private void forgetPsw(View view) {
		Intent intent = new Intent(this, ForgetPswActivity.class);
		startActivity(intent);

		LibraryUtils.onEvent(this, PageContants.EVENT_ID_FORGETPSW);
	}

	private void gotoHome() {
		hideWaitDialog();
		PreferencesUtils.putBoolean(context, SysConst.EXIT_APP, false);
		Intent intent = new Intent(this, HomeActivity.class);
		startActivity(intent);
		super.onStop();
		finish();
	}

	@Override
	public void onEvent(ResponseBean event) {
		if (event.getResult_code() == Constant.CODE_SUCCESS) {
			if (event.getRequestUrl().equals(Constant.LOGIN_URL)
					|| event.getRequestUrl().equals(Constant.OTHER_LOGIN_URL)) {
				userBean = new UserBean();
				userBean.setId(event.getId());
				userBean.setName(event.getName());
				userBean.setPhysiologyGender(event.getPhysiology_gender());
				userBean.setSocietyGender(event.getSociety_gender());
				userBean.setAvatar(event.getAvatar());
				SessionUtils.getInstance().setUserBean(userBean);

				// 序列化保存用户信息
				ACache.get(LightApplication.getInstance()).put("userBean",
						userBean);
				try {
					dbUtils.delete(FriendBean.class,
							WhereBuilder.b("user_id", "=", event.getId()));
				} catch (DbException e) {
					LibraryUtils.onError(context, e.getMessage());
				}

				LibraryUtils.setAliasAndTags(context, event.getId());
				// 跳转主页
				gotoHome();
			}
		} else if (event.getRequestUrl().equals(Constant.VERIFY_USERID_URL)) {
		} else {
			hideWaitDialog();
			ToastUtils.show(this, event.getResult_msg());
		}
	}

	@OnClick(R.id.iv_login_qq)
	private void qq_login(View view) {
		type = SysConst.QQ_LOGIN_TYPE;
		Platform pf = ShareSDK.getPlatform(QQ.NAME);
		pf.setPlatformActionListener(this);
		pf.showUser(null);
	}

	@OnClick(R.id.iv_login_sina)
	private void sinaweibo_login(View view) {
		type = SysConst.WEIBO_LOGIN_TYPE;
		Platform pf_sina = ShareSDK.getPlatform(SinaWeibo.NAME);
		pf_sina.setPlatformActionListener(this);
		pf_sina.SSOSetting(true);// true 为不使用
		pf_sina.showUser(null);
	}

	@OnClick(R.id.iv_login_weixin)
	private void weixin_login(View view) {
		ToastUtils.show(this, "稍候功能开放");
	}

	@Override
	public void onCancel(Platform plat, int action) {

		Log.d(TAG, "action:" + action);
		Message msg = new Message();
		msg.arg1 = 3;
		msg.arg2 = action;
		msg.obj = plat;
		UIHandler.sendMessage(msg, this);
	}

	@Override
	public void onComplete(Platform plat, int action,
			HashMap<String, Object> arg2) {

		Log.d(TAG, "action:" + action);
		Message msg = new Message();
		msg.arg1 = 1;
		msg.arg2 = action;
		msg.obj = plat;
		UIHandler.sendMessage(msg, this);
	}

	@Override
	public void onError(Platform plat, int action, Throwable arg2) {

		Log.d(TAG, "action:" + action);
		Message msg = new Message();
		msg.arg1 = 2;
		msg.arg2 = action;
		msg.obj = plat;
		UIHandler.sendMessage(msg, this);
	}

	@Override
	public boolean handleMessage(Message msg) {
		Platform plat = (Platform) msg.obj;
		String text = actionToString(msg.arg2);
		switch (msg.arg1) {
		case 1: {
			// 成功
			showWaitDialog("正在登录...");
			String name = plat.getDb().getUserName();
			String icon = plat.getDb().getUserIcon();
			String userId = plat.getDb().getUserId();
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("type", type);
			params.put("external_id", userId);
			params.put("name", name);
			params.put("avatar", icon);
			loginPresenter.otherLogin(params);
			Log.d(TAG, "info:" + name + "," + icon + "," + userId);
		}
			break;
		case 2: {
			// 失败
			text = plat.getName() + " caught error at " + text;
			return false;
		}
		case 3: {
			// 取消
			text = plat.getName() + " canceled at " + text;
			return false;
		}
		}
		return false;
	}

	/** 将action转换为String */
	public static String actionToString(int action) {
		switch (action) {
		case Platform.ACTION_AUTHORIZING:
			return "ACTION_AUTHORIZING";
		case Platform.ACTION_GETTING_FRIEND_LIST:
			return "ACTION_GETTING_FRIEND_LIST";
		case Platform.ACTION_FOLLOWING_USER:
			return "ACTION_FOLLOWING_USER";
		case Platform.ACTION_SENDING_DIRECT_MESSAGE:
			return "ACTION_SENDING_DIRECT_MESSAGE";
		case Platform.ACTION_TIMELINE:
			return "ACTION_TIMELINE";
		case Platform.ACTION_USER_INFOR:
			return "ACTION_USER_INFOR";
		case Platform.ACTION_SHARE:
			return "ACTION_SHARE";
		default: {
			return "UNKNOWN";
		}
		}
	}
}

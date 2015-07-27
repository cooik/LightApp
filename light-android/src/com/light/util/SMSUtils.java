package com.light.util;

import java.util.Random;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

import com.light.network.Constant;

public class SMSUtils {
	
	private static final String TAG = "SMSUtils";

	private static SMSUtils instance = null;

	private final static String APPKEY = "7cf707d0e57c";
	private final static String APPSECRET = "2453b5842a468b4c90c488e997a1bb71";

	// 短信注册，随机产生头像
	public static final String[] AVATARS = {
			"http://tupian.qqjay.com/u/2011/0729/e755c434c91fed9f6f73152731788cb3.jpg",
			"http://99touxiang.com/public/upload/nvsheng/125/27-011820_433.jpg",
			"http://img1.touxiang.cn/uploads/allimg/111029/2330264224-36.png",
			"http://img1.2345.com/duoteimg/qqTxImg/2012/04/09/13339485237265.jpg",
			"http://diy.qqjay.com/u/files/2012/0523/f466c38e1c6c99ee2d6cd7746207a97a.jpg",
			"http://img1.touxiang.cn/uploads/20121224/24-054837_708.jpg",
			"http://img1.touxiang.cn/uploads/20121212/12-060125_658.jpg",
			"http://img1.touxiang.cn/uploads/20130608/08-054059_703.jpg",
			"http://diy.qqjay.com/u2/2013/0422/fadc08459b1ef5fc1ea6b5b8d22e44b4.jpg",
			"http://img1.2345.com/duoteimg/qqTxImg/2012/04/09/13339510584349.jpg",
			"http://img1.touxiang.cn/uploads/20130515/15-080722_514.jpg",
			"http://diy.qqjay.com/u2/2013/0401/4355c29b30d295b26da6f242a65bcaad.jpg" };

	private SMSUtils() {

	}

	public static SMSUtils getInstance() {
		if (null == instance) {
			synchronized (SMSUtils.class) {
				if (null == instance) {
					instance = new SMSUtils();
				}
			}

		}
		return instance;
	}
	
	private boolean ready;

	public void initSdk(Context context,final Handler handler) {
		// 初始化短信SDK
		SMSSDK.initSDK(context, APPKEY, APPSECRET);
		EventHandler eventHandler = new EventHandler() {
			public void afterEvent(int event, int result, Object data) {
				Message msg = new Message();
				msg.arg1 = event;
				msg.arg2 = result;
				msg.obj = data;
				msg.what = Constant.WHAT_VALID_PHONE_SUCCESS;
				handler.sendMessage(msg);
			}
		};
		// 注册回调监听接口
		SMSSDK.registerEventHandler(eventHandler);
		ready = true;
	}

	// 打开注册页面
	public void openRegistPage(Context context) {
//		RegisterPage registerPage = new RegisterPage();
//		registerPage.setRegisterCallback(new EventHandler() {
//			public void afterEvent(int event, int result, Object data) {
//				// 解析注册结果
//				if (result == SMSSDK.RESULT_COMPLETE) {
//					Log.d(TAG, "SMSSDK.RESULT_COMPLETE");
//					@SuppressWarnings("unchecked")
//					HashMap<String, Object> phoneMap = (HashMap<String, Object>) data;
//					String country = (String) phoneMap.get("country");
//					String phone = (String) phoneMap.get("phone");
//					// 提交用户信息
//					registerUser(country, phone);
//				}
//			}
//		});
//		registerPage.show(context);
	}

	// 提交用户信息
	private void registerUser(String country, String phone) {
		Random rnd = new Random();
		int id = Math.abs(rnd.nextInt());
		String uid = String.valueOf(id);
		String nickName = "SmsSDK_User_" + uid;
		String avatar = AVATARS[id % 12];
		SMSSDK.submitUserInfo(uid, nickName, avatar, country, phone);
	}
	
	public void unregisterAllEventHandler(){
		if (ready) {
			// 销毁回调监听接口
			SMSSDK.unregisterAllEventHandler();
		}
	}
}

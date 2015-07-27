package com.light.util;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.http.entity.StringEntity;
import org.apache.http.protocol.HTTP;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.light.network.CallBack;
import com.light.network.NetworkHandler;
import com.umeng.analytics.MobclickAgent;

import de.greenrobot.event.EventBus;

public class LibraryUtils {

	public static final String TAG = "LibraryUtils";

	public static void activityOnCreate(Activity activity) {
		ViewUtils.inject(activity);
	}

	public static void activityOnStart(Activity activity) {
		EventBus.getDefault().register(activity);
	}

	public static void activityOnStop(Activity activity) {
		EventBus.getDefault().unregister(activity);
	}
	
	public static void activityOnDestroy(Activity activity){
		EventBus.getDefault().unregister(activity);
	}
	
	public static void sendEvent(Object event){
		EventBus.getDefault().post(event);
	}

	private static HttpUtils httpUtils = new HttpUtils();

	public static HttpUtils initHttpClient() {
		if (httpUtils == null)
			httpUtils = new HttpUtils();

		return httpUtils;
	}

	/**
	 * 使用xUtils发送post请求
	 * @param url
	 * @param inputJson
	 * @param callBack
	 */
	public static void httpPost(String url, String inputJson,final CallBack callBack) {
		RequestParams params = new RequestParams();
//		params.setHeader("Content-Type", "application/json; charset=utf-8");
		params.addHeader("Content-Type", "application/json");    
		params.addHeader("charset", HTTP.UTF_8);
		try {
			params.setBodyEntity(new StringEntity(inputJson,HTTP.UTF_8));//解决乱码
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		httpUtils.send(HttpMethod.POST, url, params,
				new RequestCallBack<String>() {

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						Log.d(TAG, "result:" + responseInfo.result);
						// http.sHttpCache.put(targetUrl, "adb");
						callBack.onNetworkFinished(true,responseInfo.result);
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						Log.d(TAG, "访问失败");
						callBack.onNetworkFinished(false,msg);
					}

				});

	}

	/**
	 * 使用HttpUrlConnection发送post请求
	 * @param url
	 * @param inputJson
	 */
	public static void httpPostByUrl(String url, String inputJson) {
		new NetworkHandler(url, inputJson, "POST").execute();
	}

	/**
	 * 生成json
	 * 
	 * @param params
	 * @return
	 */
	private static Gson gson = new Gson();

	public static String formatJson(Map<String, Object> params) {
		String inputJson =  gson.toJson(params);
		Log.d(TAG, "inputJson:" + inputJson);
		return inputJson;
	}
	
	public static Map<String,Object> formatJsonToMap(String json){
		return gson.fromJson(json, HashMap.class);
	}
	
	public static <T>  T formatJsonToTargetClass(String json,Class targetClass){
		return (T) gson.fromJson(json, targetClass);
		
	}
	
	/**
	 * UMeng统计错误
	 * @param context
	 * @param error
	 */
	public static void onError(Context context, String error){
		MobclickAgent.reportError(context, error);
	}
	
	/**
	 * UMeng统计错误
	 * @param context
	 * @param e
	 */
	public static void onError(Context context, Throwable e){
		MobclickAgent.reportError(context, e);
	}
	
	/**
	 * UMeng计数行为
	 * @param context
	 * @param eventId  在PageConstant中定义 并且在UMeng后台添加
	 */
	public static void onEvent(Context context, String eventId){
		MobclickAgent.onEvent(context, eventId);
	}
	
	/**
	 * JPUSh设置别名与标签
	 * @param context
	 * @param id
	 */
	public static void setAliasAndTags(Context context, int id) {
		JPushInterface.setAliasAndTags(context.getApplicationContext(),
				id + "", null, new TagAliasCallback() {

					@Override
					public void gotResult(int code, String arg1,
							Set<String> arg2) {
						String logs;
						switch (code) {
						case 0:
							logs = "Set tag and alias success";
							Log.i(TAG, logs);
							break;

						case 6002:
							logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
							break;

						default:
							logs = "Failed with errorCode = " + code;
							Log.e(TAG, logs);
						}

						// ToastUtils.show(context, logs);
					}
				});
	}
	
}

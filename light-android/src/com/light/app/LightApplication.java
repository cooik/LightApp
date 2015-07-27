package com.light.app;

import io.rong.imkit.RongIM;
import android.annotation.TargetApi;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Debug;
import cn.jpush.android.api.JPushInterface;

import com.light.BuildConfig;
import com.light.util.RongCloudEvent;
import com.umeng.analytics.MobclickAgent;

/**
 * 
 * @author zdnuist
 * @dateTime 2015-6-6上午11:27:16
 */
public class LightApplication extends Application{
	
	private int withPx;
	
	private int heightPx;
	
	
	private float density;
	
	private static LightApplication instance;
	
	 private static String PREF_NAME = "creativelocker.pref";
	
	public static LightApplication getInstance(){
		return instance;
		
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		//添加UMeng错误统计.程序下次启动的时候自动发到UMeng后台
		//使用LibraryUtils.onError方法统计
		MobclickAgent.setCatchUncaughtExceptions(true);
		
		RongIM.init(this);
		
		/**
         * 融云SDK事件监听处理
         */
        RongCloudEvent.init(this);
		
		JPushInterface.setDebugMode(BuildConfig.DEBUG); 	// 设置开启日志,发布时请关闭日志
        JPushInterface.init(this);     		// 初始化 JPush
		
		//设置异常处理实例
 		Thread.setDefaultUncaughtExceptionHandler(new UEHandler());
 		_context = getApplicationContext();
 		instance = this;
	}
	
	static Context _context;
	public static synchronized LightApplication context() {
		return (LightApplication) _context;
	    }

	public int getWithPx() {
		return withPx;
	}

	public void setWithPx(int withPx) {
		this.withPx = withPx;
	}

	public float getDensity() {
		return density;
	}

	public void setDensity(float density) {
		this.density = density;
	}

	public int getHeightPx() {
		return heightPx;
	}

	public void setHeightPx(int heightPx) {
		this.heightPx = heightPx;
	}
	
	
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static SharedPreferences getPreferences() {
	SharedPreferences pre = context().getSharedPreferences(PREF_NAME,
		Context.MODE_MULTI_PROCESS);
	return pre;
    }

}

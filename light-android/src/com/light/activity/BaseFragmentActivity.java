package com.light.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import cn.jpush.android.api.JPushInterface;

import com.light.app.AppManager;
import com.light.bean.ResponseBean;
import com.light.util.LibraryUtils;
import com.umeng.analytics.MobclickAgent;

public class BaseFragmentActivity extends FragmentActivity{

	protected Context context;

    protected void onCreate(Bundle savedInstanceState, int layoutResID) {
        super.onCreate(savedInstanceState);
        setContentView(layoutResID);

        context = getApplicationContext();
        
        LibraryUtils.activityOnCreate(this);
        
        AppManager.getAppManager().addActivity(this);
    }
    
    @Override
    protected void onStart() {
    	super.onStart();
    	LibraryUtils.activityOnStart(this);
    }
    
    @Override
    protected void onStop() {
    	super.onStop();
    	LibraryUtils.activityOnStop(this);
    }

    
    @Override
    protected void onDestroy() {
    	super.onDestroy();
    }
    
    @Override
   	protected void onPause() {
   		super.onPause();
   		MobclickAgent.onPause(this);
   		
   		JPushInterface.onPause(this);
   	}

   	@Override
   	protected void onResume() {
   		super.onResume();
   		MobclickAgent.onResume(this);
   		
   		JPushInterface.onResume(this);
   	}
    
    
    public void onEvent(ResponseBean event) {
	}
}

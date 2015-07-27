package com.light.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;
import cn.jpush.android.api.JPushInterface;

import com.lidroid.xutils.DbUtils;
import com.light.BuildConfig;
import com.light.R;
import com.light.app.AppManager;
import com.light.bean.ResponseBean;
import com.light.constant.SysConst;
import com.light.dialog.DialogControl;
import com.light.dialog.DialogHelper;
import com.light.dialog.WaitDialog;
import com.light.util.LibraryUtils;
import com.umeng.analytics.MobclickAgent;

public class BaseActivity extends Activity implements DialogControl{
    protected Context context;
    
    private String pageName;
    
    protected DbUtils dbUtils;
    
    private boolean _isVisible;

    protected void onCreate(Bundle savedInstanceState, int layoutResID,String pageName) {
        super.onCreate(savedInstanceState);
        
        AppManager.getAppManager().addActivity(this);
        
        setContentView(layoutResID);

        this.pageName = pageName;
        context = getApplicationContext();
        
        LibraryUtils.activityOnCreate(this);
        
        MobclickAgent.setDebugMode(BuildConfig.DEBUG);
//      SDK在统计Fragment时，需要关闭Activity自带的页面统计，
//		然后在每个页面中重新集成页面统计的代码(包括调用了 onResume 和 onPause 的Activity)。
		MobclickAgent.openActivityDurationTrack(false);
//		MobclickAgent.setAutoLocation(true);
//		MobclickAgent.setSessionContinueMillis(1000);
		
		MobclickAgent.updateOnlineConfig(this);
		
		dbUtils = DbUtils.create(context, SysConst.DB_NAME);
		dbUtils.configAllowTransaction(true);
		
		_isVisible = true;
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
    	LibraryUtils.activityOnDestroy(this);
    }
    
    @Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart( pageName );
		MobclickAgent.onResume(context);
		
		JPushInterface.onResume(context);
	}
    
    @Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd( pageName );
		MobclickAgent.onPause(context);
		
		JPushInterface.onPause(context);
	}
    
    

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                onBackPressed();
                return true;
            }
        }
        return false;
    }
    
    public void onEvent(ResponseBean event) {
	}

    
    private WaitDialog _waitDialog;
    
    @Override
    public WaitDialog showWaitDialog() {
        return showWaitDialog(R.string.loading);
    }

    @Override
    public WaitDialog showWaitDialog(int resid) {
        return showWaitDialog(getString(resid));
    }

    @Override
    public WaitDialog showWaitDialog(String message) {
        if (_isVisible) {
            if (_waitDialog == null) {
                _waitDialog = DialogHelper.getWaitDialog(this, message);
            }
            if (_waitDialog != null) {
                _waitDialog.setMessage(message);
                _waitDialog.show();
            }
            return _waitDialog;
        }
        return null;
    }

    @Override
    public void hideWaitDialog() {
        if (_isVisible && _waitDialog != null) {
            try {
                _waitDialog.dismiss();
                _waitDialog = null;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
package com.light.app;

import io.rong.imkit.RongIM;

import java.util.Stack;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Process;
import cn.jpush.android.api.JPushInterface;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;

import com.light.activity.LoginActivity;
import com.light.constant.SysConst;
import com.light.util.ACache;
import com.light.util.PreferencesUtils;
import com.light.util.SessionUtils;

/**
 * activity堆栈式管理
 * 
 * @author zdnuist
 *
 */
public class AppManager {

    private static Stack<Activity> activityStack;
    private static AppManager instance;

    private AppManager() {}

    /**
     * 单一实例
     */
    public static AppManager getAppManager() {
        if (instance == null) {
            instance = new AppManager();
        }
        return instance;
    }

    /**
     * 添加Activity到堆栈
     */
    public void addActivity(Activity activity) {
        if (activityStack == null) {
            activityStack = new Stack<Activity>();
        }
        activityStack.add(activity);
    }

    /**
     * 获取当前Activity（堆栈中最后一个压入的）
     */
    public Activity currentActivity() {
        Activity activity = activityStack.lastElement();
        return activity;
    }

    /**
     * 结束当前Activity（堆栈中最后一个压入的）
     */
    public void finishActivity() {
        Activity activity = activityStack.lastElement();
        finishActivity(activity);
    }

    /**
     * 结束指定的Activity
     */
    public void finishActivity(Activity activity) {
        if (activity != null && !activity.isFinishing()) {
            activityStack.remove(activity);
            activity.finish();
            activity = null;
        }
    }
    
    /**
     * 结束指定的Activity
     */
    public void finishActivity2(Activity activity) {
        if (activity != null && !activity.isFinishing()) {
            activity.finish();
            activity = null;
        }
    }

    /**
     * 结束指定类名的Activity
     */
    public void finishActivity(Class<?> cls) {
        for (Activity activity : activityStack) {
            if (activity.getClass().equals(cls)) {
                finishActivity(activity);
                break;
            }
        }
    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        for (int i = 0, size = activityStack.size(); i < size; i++) {
            if (null != activityStack.get(i)) {
                finishActivity2(activityStack.get(i));
            }
        }
        activityStack.clear();
    }

    /**
     * 获取指定的Activity
     * 
     * @author kymjs
     */
    public static Activity getActivity(Class<?> cls) {
        if (activityStack != null)
            for (Activity activity : activityStack) {
                if (activity.getClass().equals(cls)) {
                    return activity;
                }
            }
        return null;
    }

    /**
     * 退出应用程序
     */
    public void AppExit(Context context,boolean flag) {
        try {
        	PreferencesUtils.putBoolean(context, SysConst.EXIT_APP, true);
            finishAllActivity();
//            SessionUtils.getInstance().setUserBean(null);
            // 杀死该应用进程
            if(flag){
            	android.os.Process.killProcess(android.os.Process.myPid());
            	System.exit(0);
            }
        } catch (Exception e) {
        	e.printStackTrace();
        }
    }
    
    /**
     * 退出登录
     * @param context
     */
    public void quit(Context context,boolean flag){
    	try {
    		ACache.get(LightApplication.getInstance()).remove("userBean");
    		JPushInterface.stopPush(context);
			Platform pf = ShareSDK.getPlatform(SinaWeibo.NAME);
			if(pf!=null)
			pf.removeAccount(true);
			pf = ShareSDK.getPlatform(QQ.NAME);
			if(pf!=null)
				pf.removeAccount(true);
			if (RongIM.getInstance() != null) {
				RongIM.getInstance().disconnect();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
    	
         killThisPackageIfRunning(context, "io.rong.imlib.ipc");
         killThisPackageIfRunning(context, "com.light:ipc");
         killThisPackageIfRunning(context, "io.rong.push");
         AppExit(context,flag);
    }
    
    
    public static void killThisPackageIfRunning(final Context context, String packageName) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        activityManager.killBackgroundProcesses(packageName);
    }
}
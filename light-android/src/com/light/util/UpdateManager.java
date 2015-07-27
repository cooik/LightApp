package com.light.util;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;

import com.light.R;
import com.light.app.LightApplication;
import com.light.bean.UpdateBean;
import com.light.dialog.CommonDialog;
import com.light.dialog.DialogHelper;
import com.light.dialog.WaitDialog;
import com.light.network.CallBack;
import com.light.network.Constant;
import com.light.service.DownloadService;

/**
 * 更新管理类
 * 
 */

public class UpdateManager {

	private UpdateBean mUpdate;

	private Context mContext;
	
	private boolean isShow = false;
	
	private WaitDialog _waitDialog;

	public UpdateManager(Context context, boolean isShow) {
		this.mContext = context;
		this.isShow = isShow;
	}
	
	public boolean haveNew() {
		if (this.mUpdate == null) {
			return false;
		}
		boolean haveNew = false;
		if(mUpdate.getResult_code() == Constant.CODE_SUCCESS){
			haveNew = true;
		}
		return haveNew;
	}

	public void checkUpdate() {
		if (isShow) {
			showCheckDialog();
		}
		Map<String,Object> params = new HashMap<String,Object>();
		int curVersionCode = TDevice.getVersionCode(LightApplication
				.getInstance().getPackageName());
		params.put("version_code", curVersionCode);
		params.put("platform", 0);
		String inputJson = LibraryUtils.formatJson(params);
		LibraryUtils.httpPost(Constant.CHECK_UPDATE, inputJson, new CallBack() {
			
			@Override
			public void onNetworkFinished(boolean isSuccess, String reuslt) {
				if(isSuccess){
					hideCheckDialog();
					mUpdate = LibraryUtils.formatJsonToTargetClass(reuslt, UpdateBean.class);
					onFinshCheck();
				}else{
					hideCheckDialog();
					if (isShow) {
						showFaileDialog();
					}
				}
			}
		});
		
		params = null;
	}
	
	private void onFinshCheck() {
		if (haveNew()) {
			showUpdateInfo();
		} else {
			if (isShow) {
				showLatestDialog();
			}
		}
	}

	private void showCheckDialog() {
		if (_waitDialog == null) {
			_waitDialog = DialogHelper.getWaitDialog((Activity) mContext, "正在获取新版本信息...");
		}
		_waitDialog.show();
	}

	private void hideCheckDialog() {
		if (_waitDialog != null) {
			_waitDialog.dismiss();
		}
	}
	
	private void showUpdateInfo() {
		if (mUpdate == null) {
			return;
		}
		
		if(PreferencesUtils.getBoolean(mContext, "no_prompt",false)&&!isShow){
			
			if(PreferencesUtils.getInt(mContext, "no_prompt_type") == 0) return;
			
			if(PreferencesUtils.getInt(mContext, "no_prompt_type") == 1){
				Uri uri = Uri.parse(mUpdate.getUrl());
				Intent intent = new Intent(Intent.ACTION_VIEW, uri);
				mContext.startActivity(intent);
			}
			return;
		}
		
		CommonDialog dialog = DialogHelper.getPinterestDialogCancelable(mContext);
		dialog.setTitle("发现新版本");
		dialog.setMessage(mUpdate.getUpdate_log());
		dialog.setCanceledOnTouchOutside(false);
		dialog.setNegativeButton(R.string.cancle, new DialogInterface.OnClickListener(){

			@Override
			public void onClick(DialogInterface dialog, int which) {
				PreferencesUtils.putInt(mContext, "no_prompt_type", 0);
				dialog.dismiss();
			}
			
		});
		if(!isShow)
		dialog.setNoPromptLayout(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ImageView iv = (ImageView)v;
				boolean noPrompt = PreferencesUtils.getBoolean(mContext, "no_prompt",false);
				int resId = noPrompt ?  R.drawable.blacklist_check_box :R.drawable.blacklist_check_box_pre;
				if(!noPrompt){
					iv.setImageResource(resId);
					PreferencesUtils.putBoolean(mContext, "no_prompt", true);
				}else{
					iv.setImageResource(resId);
					PreferencesUtils.putBoolean(mContext, "no_prompt", false);
				}
			}
		});
		dialog.setPositiveButton("更新版本", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
//				Intent intent = new Intent(mContext, DownloadService.class);
//		        intent.putExtra(DownloadService.BUNDLE_KEY_DOWNLOAD_URL, mUpdate.getUrl());
//		        intent.putExtra(DownloadService.BUNDLE_KEY_TITLE, "Light_"+mUpdate.getVersion_code()+"");
//		        mContext.startService(intent);
				
				Uri uri = Uri.parse(mUpdate.getUrl());
				Intent intent = new Intent(Intent.ACTION_VIEW, uri);
				mContext.startActivity(intent);
				PreferencesUtils.putInt(mContext, "no_prompt_type", 1);
				dialog.dismiss();
			}
		});
		dialog.show();
	}
	
	private void showLatestDialog() {
		CommonDialog dialog = DialogHelper.getPinterestDialogCancelable(mContext);
		dialog.setMessage("已经是最新版本了");
		dialog.setPositiveButton("", null);
		dialog.show();
	}
	
	private void showFaileDialog() {
		CommonDialog dialog = DialogHelper.getPinterestDialogCancelable(mContext);
		dialog.setMessage("网络异常，无法获取新版本信息");
		dialog.setPositiveButton("", null);
		dialog.show();
	}
}

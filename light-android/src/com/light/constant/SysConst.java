package com.light.constant;

import java.io.File;

import android.os.Environment;

public class SysConst {
	
	public static final String DB_NAME = "LIGHT.db";
	
	public static final String RONG_TOKEN = "rong_token";
	
	public static final String EXIT_APP = "exit_app";
	
	public static final int PUSH_TYPE_ADD_FRIEND = 1;
	
	public static final int PUSH_TYPE_ACCEPT_FRIEND = 2;
	
	public static final String FRIENDS_LIST_CHANGE_ACTION = "FRIENDS_LIST_CHANGE_ACTION";
	
	public static final int QQ_LOGIN_TYPE = 2;
	
	public static final int WECHAT_LOGIN_TYPE = 1;
	
	public static final int WEIBO_LOGIN_TYPE = 3;

	
	public final static String DEFAULT_SAVE_FILE_PATH = Environment
            .getExternalStorageDirectory()
            + File.separator
            + "Lights"
            + File.separator + "download" + File.separator;
}

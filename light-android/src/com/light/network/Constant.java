package com.light.network;

public class Constant {

	private Constant() {
	}

	public static final String LOGIN_URL = "http://123.57.221.116:8080/light-server/intf/user/login.shtml";// 登录地址

	public static final String REGIST_URL = "http://123.57.221.116:8080/light-server/intf/user/register.shtml";

	public static final String RESETPSW_URL = "http://123.57.221.116:8080/light-server/intf/forget/change.shtml";

	public static final String VALID_URL = "http://123.57.221.116:8080/light-server/intf/user/validate.shtml";

	public static final String ADDINFO_URL = "http://123.57.221.116:8080/light-server/intf/user/addInfo.shtml";

	public static final String ARTICLE_URL = "http://123.57.221.116:8080/light-server/intf/article/list.shtml";

	public static final String CERTIFICATE_URL = "http://123.57.221.116:8080/light-server/intf/certificate/list.shtml";

	public static final String GET_RONG_TOKEN = "http://123.57.221.116:8080/light-server/intf/im/getToken.shtml";

	public static final String QUERY_FRIENDS = "http://123.57.221.116:8080/light-server/intf/friend/list.shtml";

	public static final String SEARCH_FRIENDS = "http://123.57.221.116:8080/light-server/intf/friend/search.shtml";

	public static final String ADD_FRIEND = "http://123.57.221.116:8080/light-server/intf/friend/add.shtml";

	public static final String ACCEPT_FRIEND = "http://123.57.221.116:8080/light-server/intf/friend/add/resp.shtml";

	public static final String NEW_FRIEND = "http://123.57.221.116:8080/light-server/intf/friend/new.shtml";

	public static final String QUERY_NPCS = "http://123.57.221.116:8080/light-server/intf/friend/npcs.shtml";

	public static final String OTHER_LOGIN_URL = "http://123.57.221.116:8080/light-server/intf/user/oauthLogin.shtml";

	public static final String CHECK_UPDATE = "http://123.57.221.116:8080/light-server/intf/version/current.shtml";

	public static final String QUERY_EXPERT = "http://123.57.221.116:8080/light-server/intf/expert/list.shtml";

	public static final String VERIFY_USERID_URL = "http://123.57.221.116:8080/light-server/intf/forget/forget.shtml";

	public static final String VERIFY_CODE_URL = "http://123.57.221.116:8080/light-server/intf/forget/validate.shtml";

	public static final int CODE_SUCCESS = 0;

	public static final int CODE_FAIL = -1001;

	public static final int CODE_ERROR = -1;

	public static final int USER_INEXISTENCE = -1002;

	public static final int VALIDATE_SUCCESS = 1;

	public static final int VALIDATE_FAIL = -1;

	public static final int WHAT_VALID_EMAIL_SUCCESS = 0x0001;

	public static final int WHAT_VALID_PHONE_SUCCESS = 0x0002;

}

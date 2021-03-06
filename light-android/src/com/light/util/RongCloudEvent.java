package com.light.util;

import java.util.List;

import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.light.activity.AMapLocationActivity;
import com.light.activity.HomeActivity;
import com.light.activity.PhotoActivity;
import com.light.app.MessageNotifycationManager;
import com.light.bean.FriendBean;
import com.light.constant.SysConst;

import io.rong.imkit.PushNotificationManager;
import io.rong.imkit.RongContext;
import io.rong.imkit.RongIM;
import io.rong.imkit.model.UIConversation;
import io.rong.imkit.widget.provider.CameraInputProvider;
import io.rong.imkit.widget.provider.ImageInputProvider;
import io.rong.imkit.widget.provider.InputProvider;
import io.rong.imkit.widget.provider.LocationInputProvider;
import io.rong.imkit.widget.provider.VoIPInputProvider;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Group;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.MessageContent;
import io.rong.imlib.model.UserInfo;
import io.rong.message.ContactNotificationMessage;
import io.rong.message.ImageMessage;
import io.rong.message.InformationNotificationMessage;
import io.rong.message.LocationMessage;
import io.rong.message.RichContentMessage;
import io.rong.message.TextMessage;
import io.rong.message.VoiceMessage;
import io.rong.notification.PushNotificationMessage;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.util.Log;
import android.view.View;

/**
 * Created by zhjchen on 1/29/15.
 */

/**
 * 融云SDK事件监听处理。 把事件统一处理，开发者可直接复制到自己的项目中去使用。
 * <p/>
 * 该类包含的监听事件有： 1、消息接收器：OnReceiveMessageListener。
 * 2、发出消息接收器：OnSendMessageListener。 3、用户信息提供者：GetUserInfoProvider。
 * 4、好友信息提供者：GetFriendsProvider。 5、群组信息提供者：GetGroupInfoProvider。
 * 6、会话界面操作的监听器：ConversationBehaviorListener。
 * 7、连接状态监听器，以获取连接相关状态：ConnectionStatusListener。 8、地理位置提供者：LocationProvider。
 * 9、自定义 push 通知： OnReceivePushMessageListener。
 * 10、会话列表界面操作的监听器：ConversationListBehaviorListener。
 */
public final class RongCloudEvent implements
		RongIMClient.OnReceiveMessageListener, RongIM.OnSendMessageListener,
		RongIM.UserInfoProvider, RongIM.GroupInfoProvider,
		RongIM.ConversationBehaviorListener,
		RongIMClient.ConnectionStatusListener, RongIM.LocationProvider,
		RongIMClient.OnReceivePushMessageListener,
		RongIM.ConversationListBehaviorListener {

	private static final String TAG = RongCloudEvent.class.getSimpleName();

	private static RongCloudEvent mRongCloudInstance;

	private Context mContext;

	/**
	 * 初始化 RongCloud.
	 * 
	 * @param context
	 *            上下文。
	 */
	public static void init(Context context) {

		if (mRongCloudInstance == null) {

			synchronized (RongCloudEvent.class) {

				if (mRongCloudInstance == null) {
					mRongCloudInstance = new RongCloudEvent(context);
				}
			}
		}
	}

	/**
	 * 构造方法。
	 * 
	 * @param context
	 *            上下文。
	 */
	private RongCloudEvent(Context context) {
		mContext = context;
		initDefaultListener();
	}

	/**
	 * RongIM.init(this) 后直接可注册的Listener。
	 */
	private void initDefaultListener() {
		RongIM.setUserInfoProvider(this, true);// 设置用户信息提供者。
		RongIM.setGroupInfoProvider(this, true);// 设置群组信息提供者。
		RongIM.setConversationBehaviorListener(this);// 设置会话界面操作的监听器。
		RongIM.setLocationProvider(this);// 设置地理位置提供者,不用位置的同学可以注掉此行代码
		// RongIM.setPushMessageBehaviorListener(this);//自定义 push 通知。
	}

	/*
	 * 连接成功注册。 <p/> 在RongIM-connect-onSuccess后调用。
	 */
	public void setOtherListener() {
		RongIM.getInstance().getRongIMClient()
				.setOnReceiveMessageListener(this);// 设置消息接收监听器。
		RongIM.getInstance().setSendMessageListener(this);// 设置发出消息接收监听器.
		RongIM.getInstance().getRongIMClient()
				.setConnectionStatusListener(this);// 设置连接状态监听器。
		// 扩展功能自定义
		InputProvider.ExtendProvider[] provider = {
				new ImageInputProvider(RongContext.getInstance()),
				new CameraInputProvider(RongContext.getInstance()),
				new LocationInputProvider(RongContext.getInstance()),
				new VoIPInputProvider(RongContext.getInstance()),
		// new AddressBookProvider(RongContext.getInstance())
		};
		RongIM.getInstance().resetInputExtensionProvider(
				Conversation.ConversationType.PRIVATE, provider);
	}

	/**
	 * 自定义 push 通知。
	 * 
	 * @param msg
	 * @return
	 */
	@Override
	public boolean onReceivePushMessage(PushNotificationMessage msg) {
		Log.d(TAG, "onReceived-onPushMessageArrive:" + msg.getContent());

		PushNotificationManager.getInstance().onReceivePush(msg);

		// Intent intent = new Intent();
		// Uri uri;
		//
		// intent.setAction(Intent.ACTION_VIEW);
		//
		// Conversation.ConversationType conversationType =
		// msg.getConversationType();
		//
		// uri = Uri.parse("rong://" +
		// RongContext.getInstance().getPackageName()).buildUpon().appendPath("conversationlist").build();
		// intent.setData(uri);
		// Log.d(TAG, "onPushMessageArrive-url:" + uri.toString());
		//
		// Notification notification=null;
		//
		// PendingIntent pendingIntent =
		// PendingIntent.getActivity(RongContext.getInstance(), 0,
		// intent, PendingIntent.FLAG_UPDATE_CURRENT);
		//
		// if (android.os.Build.VERSION.SDK_INT < 11) {
		// notification = new
		// Notification(RongContext.getInstance().getApplicationInfo().icon,
		// "自定义 notification", System.currentTimeMillis());
		//
		// notification.setLatestEventInfo(RongContext.getInstance(),
		// "自定义 title", "这是 Content:"+msg.getObjectName(), pendingIntent);
		// notification.flags = Notification.FLAG_AUTO_CANCEL;
		// notification.defaults = Notification.DEFAULT_SOUND;
		// } else {
		//
		// notification = new Notification.Builder(RongContext.getInstance())
		// .setLargeIcon(getAppIcon())
		// .setSmallIcon(R.drawable.ic_launcher)
		// .setTicker("自定义 notification")
		// .setContentTitle("自定义 title")
		// .setContentText("这是 Content:"+msg.getObjectName())
		// .setContentIntent(pendingIntent)
		// .setAutoCancel(true)
		// .setDefaults(Notification.DEFAULT_ALL).build();
		//
		// }
		//
		// NotificationManager nm = (NotificationManager)
		// RongContext.getInstance().getSystemService(RongContext.getInstance().NOTIFICATION_SERVICE);
		//
		// nm.notify(0, notification);

		return true;
	}

	private Bitmap getAppIcon() {
		BitmapDrawable bitmapDrawable;
		Bitmap appIcon;
		bitmapDrawable = (BitmapDrawable) RongContext.getInstance()
				.getApplicationInfo()
				.loadIcon(RongContext.getInstance().getPackageManager());
		appIcon = bitmapDrawable.getBitmap();
		return appIcon;
	}

	/**
	 * 获取RongCloud 实例。
	 * 
	 * @return RongCloud。
	 */
	public static RongCloudEvent getInstance() {
		return mRongCloudInstance;
	}

	/**
	 * 接收消息的监听器：OnReceiveMessageListener 的回调方法，接收到消息后执行。
	 * 
	 * @param message
	 *            接收到的消息的实体信息。
	 * @param left
	 *            剩余未拉取消息数目。
	 */
	@Override
	public boolean onReceived(Message message, int left) {

		MessageContent messageContent = message.getContent();
//		String sendUserId = message.getSenderUserId();
//		MessageNotifycationManager.getInstance().showNewMessageNotifycation(mContext, sendUserId);
		
		int count = RongIM.getInstance().getRongIMClient().getTotalUnreadCount();
		Log.d(TAG, "unreadcount:"+count);
		
		if (messageContent instanceof TextMessage) {// 文本消息
			TextMessage textMessage = (TextMessage) messageContent;
			Log.d(TAG, "onReceived-TextMessage:" + textMessage.getContent());
		} else if (messageContent instanceof ImageMessage) {// 图片消息
			ImageMessage imageMessage = (ImageMessage) messageContent;
			Log.d(TAG, "onReceived-ImageMessage:" + imageMessage.getRemoteUri());
		} else if (messageContent instanceof VoiceMessage) {// 语音消息
			VoiceMessage voiceMessage = (VoiceMessage) messageContent;
			Log.d(TAG, "onReceived-voiceMessage:"
					+ voiceMessage.getUri().toString());
		} else if (messageContent instanceof RichContentMessage) {// 图文消息
			RichContentMessage richContentMessage = (RichContentMessage) messageContent;
			Log.d(TAG,
					"onReceived-RichContentMessage:"
							+ richContentMessage.getContent());
		} else if (messageContent instanceof InformationNotificationMessage) {// 小灰条消息
			InformationNotificationMessage informationNotificationMessage = (InformationNotificationMessage) messageContent;
			Log.d(TAG, "onReceived-informationNotificationMessage:"
					+ informationNotificationMessage.getMessage());
		}
		// else if (messageContent instanceof DeAgreedFriendRequestMessage) {//
		// 好友添加成功消息
		// DeAgreedFriendRequestMessage deAgreedFriendRequestMessage =
		// (DeAgreedFriendRequestMessage) messageContent;
		// Log.d(TAG, "onReceived-deAgreedFriendRequestMessage:"
		// + deAgreedFriendRequestMessage.getMessage());
		// receiveAgreeSuccess(deAgreedFriendRequestMessage);
		// }
		else if (messageContent instanceof ContactNotificationMessage) {// 好友添加消息
			ContactNotificationMessage contactContentMessage = (ContactNotificationMessage) messageContent;
			Log.d(TAG, "onReceived-ContactNotificationMessage:getExtra;"
					+ contactContentMessage.getExtra());
			Log.d(TAG, "onReceived-ContactNotificationMessage:+getmessage:"
					+ contactContentMessage.getMessage().toString());
			// RongIM.getInstance().getRongIMClient().deleteMessages(new
			// int[]{message.getMessageId()});
			// if(DemoContext.getInstance()!=null) {
			// RongIM.getInstance().getRongIMClient().removeConversation(Conversation.ConversationType.SYSTEM,
			// "10000");
			// String targetname =
			// DemoContext.getInstance().getUserNameByUserId(contactContentMessage.getSourceUserId());
			// RongIM.getInstance().getRongIMClient().insertMessage(Conversation.ConversationType.SYSTEM,
			// "10000", contactContentMessage.getSourceUserId(),
			// contactContentMessage, null);
			//
			// }
			// Intent in = new Intent();
			// in.setAction(MainActivity.ACTION_DMEO_RECEIVE_MESSAGE);
			// in.putExtra("rongCloud", contactContentMessage);
			// in.putExtra("has_message", true);
			// mContext.sendBroadcast(in);
		} else {
			Log.d(TAG, "onReceived-其他消息，自己来判断处理");
		}

		return false;

	}

	/**
	 * @param deAgreedFriendRequestMessage
	 */
	// private void receiveAgreeSuccess(
	// DeAgreedFriendRequestMessage deAgreedFriendRequestMessage) {
	// ArrayList<UserInfo> friendreslist = new ArrayList<UserInfo>();
	// if (DemoContext.getInstance() != null) {
	// friendreslist = DemoContext.getInstance().getFriends();
	// friendreslist.add(deAgreedFriendRequestMessage.getUserInfo());
	// DemoContext.getInstance().setFriends(friendreslist);
	// }
	// Intent in = new Intent();
	// in.setAction(MainActivity.ACTION_DMEO_AGREE_REQUEST);
	// in.putExtra("AGREE_REQUEST", true);
	// mContext.sendBroadcast(in);
	//
	// }

	@Override
	public Message onSend(Message message) {
		return message;
	}

	/**
	 * 消息在UI展示后执行/自己的消息发出后执行,无论成功或失败。
	 * 
	 * @param message
	 *            消息。
	 */
	@Override
	public void onSent(Message message) {

		MessageContent messageContent = message.getContent();

		if (messageContent instanceof TextMessage) {// 文本消息
			TextMessage textMessage = (TextMessage) messageContent;
			Log.d(TAG, "onSent-TextMessage:" + textMessage.getContent());
		} else if (messageContent instanceof ImageMessage) {// 图片消息
			ImageMessage imageMessage = (ImageMessage) messageContent;
			Log.d(TAG, "onSent-ImageMessage:" + imageMessage.getRemoteUri());
		} else if (messageContent instanceof VoiceMessage) {// 语音消息
			VoiceMessage voiceMessage = (VoiceMessage) messageContent;
			Log.d(TAG, "onSent-voiceMessage:"
					+ voiceMessage.getUri().toString());
		} else if (messageContent instanceof RichContentMessage) {// 图文消息
			RichContentMessage richContentMessage = (RichContentMessage) messageContent;
			Log.d(TAG,
					"onSent-RichContentMessage:"
							+ richContentMessage.getContent());
		} else {
			Log.d(TAG, "onSent-其他消息，自己来判断处理");
		}
	}

	/**
	 * 用户信息的提供者：GetUserInfoProvider 的回调方法，获取用户信息。
	 * 
	 * @param userId
	 *            用户 Id。
	 * @return 用户信息，（注：由开发者提供用户信息）。
	 */
	@Override
	public UserInfo getUserInfo(String userId) {
		Log.e(TAG, "0604---------getUserInfo----userId---:" + userId);

			if ((SessionUtils.getInstance().getUserBean().getId() + "")
					.equals(userId)) {
				return new UserInfo(SessionUtils.getInstance().getUserBean()
						.getId()
						+ "" + "", SessionUtils.getInstance().getUserBean()
						.getName(), Uri.parse(SessionUtils.getInstance()
						.getUserBean().getAvatar()));
			} 

			List<FriendBean> friends = SessionUtils.getInstance().getFriends();
			if (friends != null && friends.size() > 0) {
				for (FriendBean friend : friends) {
					Log.e(TAG, "0604---------getUserInfo----has userId---:" + userId);
					// 判断返回的userId
					if ((friend.getFriendId() + "").equals(userId))
						return new UserInfo(friend.getFriendId() + "",
								friend.getName(), Uri.parse(friend.getAvatar()));
				}
			}
			return null;
	}

	/**
	 * 群组信息的提供者：GetGroupInfoProvider 的回调方法， 获取群组信息。
	 * 
	 * @param groupId
	 *            群组 Id.
	 * @return 群组信息，（注：由开发者提供群组信息）。
	 */
	@Override
	public Group getGroupInfo(String groupId) {
		/**
		 * demo 代码 开发者需替换成自己的代码。
		 */
		return null;
	}

	/**
	 * 会话界面操作的监听器：ConversationBehaviorListener 的回调方法，当点击用户头像后执行。
	 * 
	 * @param context
	 *            应用当前上下文。
	 * @param conversationType
	 *            会话类型。
	 * @param user
	 *            被点击的用户的信息。
	 * @return 返回True不执行后续SDK操作，返回False继续执行SDK操作。
	 */
	@Override
	public boolean onUserPortraitClick(Context context,
			Conversation.ConversationType conversationType, UserInfo user) {
		Log.d(TAG, "onUserPortraitClick");

		/**
		 * demo 代码 开发者需替换成自己的代码。
		 */
		// Log.d("Begavior", conversationType.getName() + ":" + user.getName());
		// Intent in = new Intent(context, DePersonalDetailActivity.class);
		// in.putExtra("USER", user);
		// in.putExtra("SEARCH_USERID", user.getUserId());
		// context.startActivity(in);

		return false;
	}

	@Override
	public boolean onUserPortraitLongClick(Context context,
			Conversation.ConversationType conversationType, UserInfo userInfo) {
		return false;
	}

	/**
	 * 会话界面操作的监听器：ConversationBehaviorListener 的回调方法，当点击消息时执行。
	 * 
	 * @param context
	 *            应用当前上下文。
	 * @param message
	 *            被点击的消息的实体信息。
	 * @return 返回True不执行后续SDK操作，返回False继续执行SDK操作。
	 */
	@Override
	public boolean onMessageClick(Context context, View view, Message message) {
		Log.d(TAG, "onMessageClick");

		/**
		 * demo 代码 开发者需替换成自己的代码。
		 */
		
		if (message.getContent() instanceof ImageMessage) {
			ImageMessage imageMessage = (ImageMessage) message.getContent();
			Intent intent = new Intent(context, PhotoActivity.class);

			intent.putExtra(
					"photo",
					imageMessage.getLocalUri() == null ? imageMessage
							.getRemoteUri() : imageMessage.getLocalUri());
			if (imageMessage.getThumUri() != null)
				intent.putExtra("thumbnail", imageMessage.getThumUri());

			context.startActivity(intent);
		}else if (message.getContent() instanceof LocationMessage) {
			Intent intent = new Intent(context, AMapLocationActivity.class);
			intent.putExtra("location", message.getContent());
			context.startActivity(intent);
		} 
//		else if (message.getContent() instanceof RichContentMessage) {
//			RichContentMessage mRichContentMessage = (RichContentMessage) message
//					.getContent();
//			Log.d("Begavior", "extra:" + mRichContentMessage.getExtra());
//
//		} else if (message.getContent() instanceof ImageMessage) {
//			ImageMessage imageMessage = (ImageMessage) message.getContent();
//			Intent intent = new Intent(context, PhotoActivity.class);
//
//			intent.putExtra(
//					"photo",
//					imageMessage.getLocalUri() == null ? imageMessage
//							.getRemoteUri() : imageMessage.getLocalUri());
//			if (imageMessage.getThumUri() != null)
//				intent.putExtra("thumbnail", imageMessage.getThumUri());
//
//			context.startActivity(intent);
//		}

		Log.d("Begavior",
				message.getObjectName() + ":" + message.getMessageId());

		return false;
	}

	@Override
	public boolean onMessageLongClick(Context context, View view,
			Message message) {
		return false;
	}

	/**
	 * 连接状态监听器，以获取连接相关状态:ConnectionStatusListener 的回调方法，网络状态变化时执行。
	 * 
	 * @param status
	 *            网络状态。
	 */
	@Override
	public void onChanged(ConnectionStatus status) {
		Log.d(TAG, "onChanged:" + status);
		if (status.getMessage().equals(
				ConnectionStatus.DISCONNECTED.getMessage())) {
		}
	}

	/**
	 * 位置信息提供者:LocationProvider 的回调方法，打开第三方地图页面。
	 * 
	 * @param context
	 *            上下文
	 * @param callback
	 *            回调
	 */
	@Override
	public void onStartLocation(Context context, LocationCallback callback) {
		 SessionUtils.getInstance().setLastLocationCallback(callback);
		 Intent intent = new Intent(context,
				 AMapLocationActivity.class);
		 intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		 context.startActivity(intent);
	}

	/**
	 * 点击会话列表 item 后执行。
	 * 
	 * @param context
	 *            上下文。
	 * @param view
	 *            触发点击的 View。
	 * @param conversation
	 *            会话条目。
	 * @return 返回 true 不再执行融云 SDK 逻辑，返回 false 先执行融云 SDK 逻辑再执行该方法。
	 */
	@Override
	public boolean onConversationClick(Context context, View view,
			UIConversation conversation) {
		
		Log.d(TAG, "onConversationClick");
		return false;
	}

	/**
	 * 长按会话列表 item 后执行。
	 * 
	 * @param context
	 *            上下文。
	 * @param view
	 *            触发点击的 View。
	 * @param conversation
	 *            长按会话条目。
	 * @return 返回 true 不再执行融云 SDK 逻辑，返回 false 先执行融云 SDK 逻辑再执行该方法。
	 */
	@Override
	public boolean onConversationLongClick(Context context, View view,
			UIConversation conversation) {
		Log.d(TAG, "onConversationLongClick");
		return false;
	}
}

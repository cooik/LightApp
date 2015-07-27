package com.light.app;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.support.v4.app.NotificationCompat;

import com.light.R;

public class MessageNotifycationManager {
	private static MessageNotifycationManager instance = null;
	public static final int NOTIFYCATION_TIP_ID = 0x0001;

	private Notification notification;
	private NotificationManager mNotificationManager;

	private MessageNotifycationManager() {

	}

	public static MessageNotifycationManager getInstance() {
		if (null == instance) {
			synchronized (MessageNotifycationManager.class) {
				if (null == instance) {
					instance = new MessageNotifycationManager();

				}
			}
		}
		return instance;
	}

	/**
	 * 新的消息提示
	 * 
	 * @param mContext
	 * @param tip
	 */
	public void showNewMessageNotifycation(Context mContext, String tip) {
		mNotificationManager = (NotificationManager) mContext
				.getSystemService(Context.NOTIFICATION_SERVICE);

		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
				mContext);
		mBuilder.setSmallIcon(R.drawable.ic_launcher).setTicker("您有新的消息了")
				.setContentIntent(null).setContentInfo(tip);

		notification = mBuilder.build();
		notification.flags = Notification.FLAG_AUTO_CANCEL;
		mNotificationManager.notify(NOTIFYCATION_TIP_ID, notification);
	}

	/**
	 * 取消某个notifycation
	 * 
	 * @param notifycationId
	 */
	public void cancelNotifycation(int notifycationId) {
		if (null != mNotificationManager) {
			mNotificationManager.cancel(notifycationId);
		}
	}

	public Notification getNotification() {
		return notification;
	}

	public NotificationManager getmNotificationManager() {
		return mNotificationManager;
	}

}

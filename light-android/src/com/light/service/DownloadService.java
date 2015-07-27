package com.light.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.light.R;
import com.light.constant.SysConst;
import com.light.util.StringUtils;
import com.light.util.TDevice;

public class DownloadService extends Service{

	public static final String BUNDLE_KEY_DOWNLOAD_URL = "download_url";
	
	public static final String BUNDLE_KEY_TITLE = "title";
	
	private final String TAG = "DownloadService";
	private static final int NOTIFY_ID = 0;
	private NotificationManager mNotificationManager;
	
	private String downloadUrl;
	
	private String mTitle = "正在下载%s";

	private String saveFileName = SysConst.DEFAULT_SAVE_FILE_PATH;


	private Context mContext = this;

	private Thread downLoadThread;

	private Notification mNotification;
	
	private int i;
	
	int progress;
	
	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case -1:
				showNotification(R.drawable.notification_dlerror, progress, mContext);
				break;
			case 0:
				// 下载完毕
				mNotificationManager.cancel(NOTIFY_ID);
				installApk();
				break;
			case 2:
				// 取消通知
				mNotificationManager.cancel(NOTIFY_ID);
				break;
			case 1:
				int rate = msg.arg1;
				
				progress = rate;
				
				 if (i > 9) {
						i = 1;
					}

					int id = 0;
					switch (i) {
					case 1:
						id = R.drawable.notification_dling1;
						break;
					case 2:
						id = R.drawable.notification_dling2;
						break;
					case 3:
						id = R.drawable.notification_dling3;
						break;

					case 7:
						id = R.drawable.notification_dling7;
						break;
					case 8:
						id = R.drawable.notification_dling8;
						break;
					case 9:
						id = R.drawable.notification_dling9;
						break;

					case 4:
						id = R.drawable.notification_dling4;
						break;
					case 5:
						id = R.drawable.notification_dling5;
						break;
					case 6:
						id = R.drawable.notification_dling6;
						break;
					}

					if (rate == 100) {
						id = R.drawable.notification_dled;
						showNotification(id, rate,mContext);
					} else {

						showNotification(id, rate,mContext);

						i++;
					}
				
				break;
			}
		}
	};

	
	private String getSaveFileName(String downloadUrl) {
		if (downloadUrl == null || StringUtils.isEmpty(downloadUrl)) {
			return "";
		}
		return downloadUrl.substring(downloadUrl.lastIndexOf("/")+1);
	}

	@Override
	public void onCreate() {
		super.onCreate();
		mNotificationManager = (NotificationManager) getSystemService(android.content.Context.NOTIFICATION_SERVICE);
		stopForeground(true);// 这个不确定是否有作用
	}

	private void startDownload() {
		downloadApk();
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if(intent != null){
			downloadUrl = intent.getStringExtra(BUNDLE_KEY_DOWNLOAD_URL);
			saveFileName = saveFileName + getSaveFileName(downloadUrl); 
			mTitle = String.format(mTitle, intent.getStringExtra(BUNDLE_KEY_TITLE));
			if(!StringUtils.isBlank(downloadUrl)&&!StringUtils.isBlank(mTitle)){
				startDownload();
			}
		}
		return super.onStartCommand(intent, flags, startId);
	}

	/**
	 * 创建通知
	 */
	NotificationCompat.Builder mBuilder;
	private void showNotification(int drawable, int progress, Context context) {
		if(mBuilder == null){
			mBuilder = new NotificationCompat.Builder(context);
			mBuilder
			.setLargeIcon(
					BitmapFactory.decodeResource(context.getResources(),
							R.drawable.ic_launcher)).setContentIntent(null)
			.setOngoing(true);
		}
		String ticker = mTitle+",请稍候...";
		if (progress == 100) {
			ticker = "下载完成";
		} else if (drawable == R.drawable.notification_dlerror) {
			ticker = getResources().getString(R.string.download_error);
		}
		mBuilder.setSmallIcon(drawable)
				.setProgress(100, progress, false)
				.setTicker(ticker)
				.setContentInfo("已下载" + progress + "%")
				.setContentTitle(ticker);

		if (drawable == R.drawable.notification_dlerror) {
			mBuilder.setAutoCancel(true);
			mNotification.flags = Notification.FLAG_AUTO_CANCEL;
		}

		mNotification = mBuilder.build();
		mNotificationManager.notify(NOTIFY_ID, mNotification);
	}

	private void downloadApk() {
		showNotification(R.drawable.notification_dling1,0,mContext);
		downLoadThread = new Thread(mdownApkRunnable);
		downLoadThread.start();
	}

	/**
	 * 安装apk
	 */
	private void installApk() {
		File apkfile = new File(saveFileName);
		if (!apkfile.exists()) {
			return;
		}
		this.stopSelf();
		TDevice.installAPK(mContext, apkfile);
	}

	private Runnable mdownApkRunnable = new Runnable() {
		@Override
		public void run() {
			try {
				File file = new File(SysConst.DEFAULT_SAVE_FILE_PATH);
				if (!file.exists()) {
					file.mkdirs();
				}
				String apkFile = saveFileName;
				File saveFile = new File(apkFile);
				downloadUpdateFile(downloadUrl, saveFile);
//				new DownloadTask(downloadUrl,saveFile).run();
			} catch (Exception e) {
				e.printStackTrace();
				Log.d(TAG, "error:" + e.getMessage());
				Message msg = mHandler.obtainMessage();
				msg.what = -1;
				mHandler.sendMessage(msg);
			}
		}
	};
	
	
	 class DownloadTask implements Runnable {

		private String webUrl;
		private File saveFile;

		public DownloadTask(String webUrl,File saveFile) {
			this.webUrl = webUrl;
			this.saveFile = saveFile;
		}

		@Override
		public void run() {
			HttpURLConnection urlConnection = null;
			InputStream inputStream = null;
			OutputStream outputStream = null;
			try {
				URL url = new URL(webUrl);
				urlConnection = (HttpURLConnection) url.openConnection();
				urlConnection.setConnectTimeout(50 * 10000);
				urlConnection.setDoInput(true);
				urlConnection.setRequestMethod("GET");
				urlConnection.setReadTimeout(5000);
				int responseCode = urlConnection.getResponseCode();
				if (responseCode == HttpURLConnection.HTTP_OK) {
					int totalLength = urlConnection.getContentLength();

					inputStream = urlConnection.getInputStream();

					byte[] buffer = new byte[4 * 1024];
					int len = 0;

					outputStream = new FileOutputStream(saveFile);
					int progress = 0;
					while ((len = inputStream.read(buffer)) != -1) {
						outputStream.write(buffer, 0, len);
						progress += len;
						int pro = (progress * 100) / totalLength;

						Message msg = mHandler.obtainMessage();
						msg.what = 1;
						msg.arg1 = pro;
						mHandler.sendMessage(msg);
						Thread.sleep(300);
					}
				}else{
					Message msg = mHandler.obtainMessage();
					msg.what = -1;
					mHandler.sendMessage(msg);
					Log.d(TAG, "error:" + responseCode);
				}
			} catch (Exception e) {
				e.printStackTrace();
				Message msg = mHandler.obtainMessage();
				msg.what = -1;
				mHandler.sendMessage(msg);
				Log.d(TAG, "error:" + e.getMessage());
			} finally {
				try {
					if (outputStream != null)
						outputStream.close();
					if (inputStream != null)
						inputStream.close();
					if (urlConnection != null)
						urlConnection.disconnect();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	

	public long downloadUpdateFile(String downloadUrl, File saveFile)
			throws Exception {
		int downloadCount = 0;
		long totalSize = 0;
		int updateTotalSize = 0;

		HttpURLConnection httpConnection = null;
		InputStream is = null;
		FileOutputStream fos = null;

		try {
			URL url = new URL(downloadUrl);
			httpConnection = (HttpURLConnection) url.openConnection();
//			httpConnection
//					.setRequestProperty("User-Agent", "PacificHttpClient");
//			if (currentSize > 0) {
//				httpConnection.setRequestProperty("RANGE", "bytes="
//						+ currentSize + "-");
//			}
			httpConnection.setRequestMethod("GET");
			httpConnection.setDoInput(true);
			httpConnection.setConnectTimeout(10000);
			httpConnection.setReadTimeout(20000);
			updateTotalSize = httpConnection.getContentLength();
			if (httpConnection.getResponseCode() != HttpURLConnection.HTTP_OK) {
				Message msg = mHandler.obtainMessage();
				msg.what = -1;
				mHandler.sendMessage(msg);
			}
			is = httpConnection.getInputStream();
			fos = new FileOutputStream(saveFile, false);
			byte buffer[] = new byte[1024];
			int readsize = 0;
			while ((readsize = is.read(buffer)) > 0) {
				fos.write(buffer, 0, readsize);
				totalSize += readsize;
				// 更新进度
				downloadCount =  new Long((totalSize * 100) / updateTotalSize).intValue();
				Message msg = mHandler.obtainMessage();
				msg.what = 1;
				msg.arg1 = downloadCount;
				mHandler.sendMessage(msg);

				Thread.sleep(300);

			}
			
			// 下载完成通知安装
			mHandler.sendEmptyMessage(0);
			// 下载完了，cancelled也要设置
			
		} finally {
			if (httpConnection != null) {
				httpConnection.disconnect();
			}
			if (is != null) {
				is.close();
			}
			if (fos != null) {
				fos.close();
			}
		}
		return totalSize;
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

}

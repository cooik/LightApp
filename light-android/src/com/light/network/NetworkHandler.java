package com.light.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.util.Log;

import com.lidroid.xutils.task.PriorityAsyncTask;

public class NetworkHandler extends PriorityAsyncTask<Object, Object, Void>{
	
	private String weburl;
	private String requestMethod;
	private String content;
	
	public NetworkHandler(String url,String content, String requestMethod){
		this.weburl = url;
		this.requestMethod = requestMethod;
		this.content = content;
	}

	@Override
	protected Void doInBackground(Object... params) {

		HttpURLConnection urlConnection = null;
		InputStream inputStream = null;
		OutputStream outputStream = null;
		StringBuilder sb = null;
		try {
			URL url = new URL(weburl);
			urlConnection = (HttpURLConnection) url.openConnection();
			urlConnection.setConnectTimeout(5 * 10000);
			urlConnection.setDoInput(true);
			urlConnection.setRequestMethod(requestMethod);
			urlConnection.setReadTimeout(5000);
			urlConnection.setRequestProperty("Content-Type", " text/html;charset=utf-8");
			if("POST".equals(requestMethod)){
				outputStream = urlConnection.getOutputStream();
				outputStream.write(content.getBytes());
				outputStream.flush();
			}
			int responseCode = urlConnection.getResponseCode();
			if (responseCode == HttpURLConnection.HTTP_OK) {
				sb = new StringBuilder();
				inputStream = urlConnection.getInputStream();

				BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
				String line = null;
				while((line = br.readLine())!=null){
					sb.append(line);
				}
				Log.d("zd", sb.toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
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
	
		return null;
	}

}

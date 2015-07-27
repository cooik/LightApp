package com.light.adapter;

import java.util.List;

import com.lidroid.xutils.BitmapUtils;
import com.light.R;
import com.light.bean.CertificateBean;
import com.light.util.BitmapHelp;
import com.light.util.ImageLoader;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CertificateAdapter extends BaseAdapter {
	private List<CertificateBean> data;
	public BitmapUtils bitmaputils;
	private static LayoutInflater inflater;
	private View view;
	private int resouceId;
	private Context context;
	public TextView title;
	public ImageView pic;

	public CertificateAdapter(Context context, int ViewResourceId,
			List<CertificateBean> objects) {
		data = objects;
		resouceId = ViewResourceId;
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		bitmaputils = BitmapHelp
				.getBitmapUtils(context.getApplicationContext());
		this.context = context;
	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		CertificateBean certificate = (CertificateBean) getItem(position);
		View view;
		if (convertView == null) {
			view = inflater.from(context).inflate(resouceId, null);
		} else {
			view = convertView;
		}
		title = (TextView) view.findViewById(R.id.tv_cer);
		title.setText(data.get(position).getTitle());
		ImageView image = (ImageView) view.findViewById(R.id.iv_cer);
		ImageLoader.getInstance(context).display(image,
				data.get(position).getPic());
		return view;
	}
}

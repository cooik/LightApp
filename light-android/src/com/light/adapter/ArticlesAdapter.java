package com.light.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.light.R;
import com.light.bean.Article;
import com.light.util.BitmapHelp;
import com.light.util.ImageLoader;

public class ArticlesAdapter extends BaseAdapter {
	private Activity activity;
	private List<Article> data;
	private static LayoutInflater inflater = null;
	public BitmapUtils bitmaputils;
	private View lv;

	private Context context;

	public ArticlesAdapter(FragmentActivity activity, List<Article> arraylist) {
		// TODO Auto-generated constructor stub
		this.activity = activity;
		this.data = arraylist;
		inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		context = activity.getApplicationContext();
		bitmaputils = BitmapHelp.getBitmapUtils(activity
				.getApplicationContext());

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	public static class ViewHolder {

		public TextView title;
		public ImageView pic;

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View vi = convertView;
		ViewHolder holder;

		if(vi == null){
			holder = new ViewHolder();
			/****** Inflate tabitem.xml file for each row ( Defined below ) *******/
			vi = inflater.inflate(R.layout.article_item, null);
			
			/****** View Holder Object to contain tabitem.xml file elements ******/
			holder.title = (TextView) vi.findViewById(R.id.title);
			
			holder.pic = (ImageView) vi.findViewById(R.id.pic);
			
			/************ Set holder with LayoutInflater ************/
			
			vi.setTag(holder);
		}else{
			holder = (ViewHolder) vi.getTag();
		}

		holder.title.setText(data.get(position).getTitle());

		ImageView image = holder.pic;
		ImageLoader.getInstance(context).display(image,
				data.get(position).getPic());
		/******** Set Item Click Listner for LayoutInflater for each row ***********/
		return vi;
	}
}

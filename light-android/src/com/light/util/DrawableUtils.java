package com.light.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

public class DrawableUtils {

	public static Bitmap drawRedbotInBitmap(Context mContext, int imgSrc) {
		Bitmap bitmap = null;
		int radius = 15;
		try {
			Bitmap srcmap = BitmapFactory.decodeResource(
					mContext.getResources(), imgSrc);
			bitmap = Bitmap.createBitmap(srcmap.getWidth(), srcmap.getHeight(),
					srcmap.getConfig());
			Canvas canvas = new Canvas(bitmap);
			canvas.drawBitmap(srcmap, 0, 0, null);
			Paint mPaint = new Paint();
			mPaint.setStyle(Paint.Style.FILL);
			mPaint.setColor(Color.RED);
			canvas.drawCircle(srcmap.getWidth()-radius, radius, radius, mPaint);
			canvas.save(Canvas.ALL_SAVE_FLAG);
			canvas.restore();

		} catch (Exception e) {
		}
		return bitmap;
	}

	private Bitmap compressImage(Bitmap image) {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.JPEG, 80, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
		int options = 100;
		while (baos.toByteArray().length / 1024 > 100) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
			options -= 10;// 每次都减少10
			baos.reset();// 重置baos即清空baos
			image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
		}
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
		return bitmap;
	}
	
	/** 
	* Drawable转化为Bitmap 
	*/  
	public static Bitmap drawableToBitmap(Drawable drawable) {  
	   int width = drawable.getIntrinsicWidth();  
	   int height = drawable.getIntrinsicHeight();  
	   Bitmap bitmap = Bitmap.createBitmap(width, height, drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);  
	   Canvas canvas = new Canvas(bitmap);  
	   drawable.setBounds(0, 0, width, height);  
	   drawable.draw(canvas);  
	   return bitmap;  
	  
	}  
	/**
	 * Bitmap to Drawable
	 * @param bitmap
	 * @param mcontext
	 * @return
	 */
	public static Drawable bitmapToDrawble(Bitmap bitmap,Context mcontext){
		Drawable drawable = new BitmapDrawable(mcontext.getResources(), bitmap);
		return drawable;
	}
}

package com.light.util;

import android.content.Context;

import com.lidroid.xutils.BitmapUtils;

public class BitmapHelp {
	private BitmapHelp() {
	}

	private static BitmapUtils bitmapUtils;

	public static BitmapUtils getBitmapUtils(Context appContext) {
		if (bitmapUtils == null) {
			bitmapUtils = new BitmapUtils(appContext);
		}
		return bitmapUtils;
	}
}

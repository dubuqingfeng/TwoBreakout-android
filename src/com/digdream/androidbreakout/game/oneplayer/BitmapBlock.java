package com.digdream.androidbreakout.game.oneplayer;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
/**
 * 自定义位图砖块类
 * 先分割，然后转化为Block
 * @author user
 *
 */

public class BitmapBlock extends BitmapDrawable{
	/**
	 * 构造器
	 * @param res
	 * @param bitmap
	 */
	BitmapBlock(Resources res,Bitmap bitmap){
		super();
		
		
	}
	
	/**
	 * 绘制砖块
	 * 
	 * @param canvas
	 *            graphical canvas
	 * */
	public void drawBlock(Canvas canvas) {
		//this.setBounds(left, top, right, bottom);
		this.draw(canvas);
	}

}

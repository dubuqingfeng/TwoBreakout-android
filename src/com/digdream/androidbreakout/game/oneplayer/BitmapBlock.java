package com.digdream.androidbreakout.game.oneplayer;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
/**
 * �Զ���λͼש����
 * �ȷָȻ��ת��ΪBlock
 * @author user
 *
 */

public class BitmapBlock extends BitmapDrawable{
	/**
	 * ������
	 * @param res
	 * @param bitmap
	 */
	BitmapBlock(Resources res,Bitmap bitmap){
		super();
		
		
	}
	
	/**
	 * ����ש��
	 * 
	 * @param canvas
	 *            graphical canvas
	 * */
	public void drawBlock(Canvas canvas) {
		//this.setBounds(left, top, right, bottom);
		this.draw(canvas);
	}

}

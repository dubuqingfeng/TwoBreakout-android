package com.digdream.androidbreakout.game.oneplayer;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.ShapeDrawable;

/**
 * �Զ���������࣬�����ש���Ժ󣬴��ϵ��£��������壬���ж��뵲���Ƿ�Ӵ���
 * @author user
 *
 */
public class Item extends ShapeDrawable{
	private Bitmap bitmap;
	
	public int Itemflg;
	public int X;
	public int Y;
	
	private int velocityX;
	private int velocityY;
	
	private Bitmap[] itembmp = new Bitmap[4];
	Item(){
		
	}
	Item(int Itemflg,Bitmap bitmap){
		this.Itemflg = Itemflg;
		this.bitmap = bitmap;
	}
	public Item(int Itemflg, int left,int top, Bitmap bitmap) {
		this.Itemflg = Itemflg;
		this.bitmap = bitmap;
		this.X = left;
		this.Y = top;
		velocityY = Y;
	}
	public void drawItem(Canvas canvas) {
		canvas.drawBitmap(bitmap, X, Y,
				null);
	}
	void setVelocityY(){
		
		this.Y += velocityY /2; 
	}
}
package com.digdream.androidbreakout.game.oneplayer;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.ShapeDrawable;

/**
 * 自定义的落物类，当打击砖块以后，从上到下，掉落物体，并判断与挡板是否接触。
 * 落物，判断Itemflg，决定状态。
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

	public boolean paddleCollision;

	private int randomnumber;
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
		randomnumber = (int)(Math.random() *10 +4);
	}
	public void drawItem(Canvas canvas) {
		canvas.drawBitmap(bitmap, X, Y,
				null);
	}
	void setVelocityY(){
		//随机速度,原来是8

		this.Y += velocityY / randomnumber; 
	}
	public void checkPaddleCollision(){
	}
	/**
	 * itemflg  
	 * 3	增大挡板
	 * 4	减小挡板
	 * 
	 * 3	增加速度
	 * 4 	减小速度
	 * 2 	strong球
	 * 6 	
	 */
}
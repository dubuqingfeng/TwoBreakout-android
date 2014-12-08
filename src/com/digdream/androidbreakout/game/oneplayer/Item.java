package com.digdream.androidbreakout.game.oneplayer;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.ShapeDrawable;

/**
 * �Զ���������࣬�����ש���Ժ󣬴��ϵ��£��������壬���ж��뵲���Ƿ�Ӵ���
 * ����ж�Itemflg������״̬��
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
		//����ٶ�,ԭ����8

		this.Y += velocityY / randomnumber; 
	}
	public void checkPaddleCollision(){
	}
	/**
	 * itemflg  
	 * 3	���󵲰�
	 * 4	��С����
	 * 
	 * 3	�����ٶ�
	 * 4 	��С�ٶ�
	 * 2 	strong��
	 * 6 	
	 */
}
package com.digdream.androidbreakout.game.onetwoplayer;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.util.Log;

/**
 * 这是一个玩家的砖块类
 * 
 * 代表一个游戏的砖块对象。延伸的ShapeDrawable为包括颜色值和出口，以便在坐标和颜色的方法保存其状态。
 * */
public class Block extends ShapeDrawable {

	private static Bitmap blockbmp = null;
	private Paint paint;
	private int blockColor;
	private Bitmap bitmap;
	//float left;
	//float top;
	//float right;
	//float bottom;
	private Rect localRect1;
	private Rect localRect2;
	private int state = 1;

	/**
	 * 构造器，使用父类方法构造Gect，设置颜色
	 * 
	 * @param rect
	 *            Android Rect object
	 * @param color 
	 *            number representing a Color value
	 * */
	public Block(Rect rect, int color) {
		super(new RectShape());
		this.setBounds(rect);
		paint = new Paint();
		paint.setColor(color);
		blockColor = color;
	}
	/**
	 * 构造器，使用父类方法构造Gect，设置颜色
	 * 
	 * @param rect
	 *            Android Rect object
	 * @param color 
	 *            number representing a Color value
	 * */
	public Block(int color,Bitmap bitmap ,float left,float top) {
		super(new RectShape());
		//this.setBounds(rect);
		this.bitmap = bitmap;
		paint = new Paint();
		paint.setColor(color);
		blockColor = color;
	}

	public Block(Rect localRect1, Rect localRect2, Bitmap blockbmp2 ,int color) {
		this.localRect1 = localRect1;
		this.localRect2 = localRect2;
		this.blockbmp = blockbmp2;
		paint = new Paint();
		paint.setColor(color);
		blockColor = color;
	}
	public Block(Rect localRect12, Rect localRect22, Bitmap blockbmp2,
			int color, int i) {
		this.localRect1 = localRect12;
		this.localRect2 = localRect22;
		this.blockbmp = blockbmp2;
		paint = new Paint();
		paint.setColor(color);
		blockColor = color;
		this.state = i;
	}
	public Rect getRect(){
		return localRect1;
	}
	/**
	 * 绘制砖块
	 * 
	 * @param canvas
	 *            graphic canvas
	 * */
	public void drawBlock(Canvas canvas) {
		Log.e("tet",Block.blockbmp.toString());
		canvas.drawBitmap(Block.blockbmp, localRect1, localRect2,null);
		//canvas.drawBitmap(bitmap,left,top,paint);
		//canvas.drawRect(this.getBounds(), paint);
	}

	/**
	 * 返回颜色值
	 * 
	 * @return color value
	 * */
	public int getColor() {
		return paint.getColor();
	}
	/**
	 * 返回砖块状态
	 * @return state
	 */
	public int getBlockState() {
		return state;
	}
	/**
	 * 改变砖块状态
	 */
	public void changeBlockState(){
		this.state = 1;
	}

	/***
	 * 返回包含颜色的整型数组和坐标的砖块。用于一个砖块的状态保存到数组中。前四个值表示该砖块的坐标。最后一个值是砖块的颜色。
	 * 
	 * @return integer array containing the block's coordinates and color values.
	 * */
	public int[] toIntArray() {
		int[] arr = { this.getBounds().left, this.getBounds().top,
				this.getBounds().right, this.getBounds().bottom, blockColor };
		return arr;
	}
}

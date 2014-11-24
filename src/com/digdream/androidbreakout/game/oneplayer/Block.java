package com.digdream.androidbreakout.game.oneplayer;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;

/**
 * 这是一个玩家的砖块类
 * 
 * 代表一个游戏的砖块对象。延伸的ShapeDrawable为包括颜色值和出口，以便在坐标和颜色的方法保存其状态。
 * */
public class Block extends ShapeDrawable {

	private Paint paint;
	private int blockColor;
	private Bitmap bitmap;
	private float left;
	private float top;

	/**
	 * 构造器，使用父类方法构造Gect，设置颜色
	 * 
	 * @param rect
	 *            Android Rect object
	 * @param color 
	 *            number representing a Color value
	 * */
	public Block(Rect rect, int color,Bitmap bitmap ,float left,float top) {
		//super(new RectShape());
		//this.setBounds(rect);
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
		//super(new RectShape());
		//this.setBounds(rect);
		this.bitmap = bitmap;
		this.left = left;
		this.top = top;
		paint = new Paint();
		paint.setColor(color);
		blockColor = color;
	}

	/**
	 * 绘制砖块
	 * 
	 * @param canvas
	 *            graphic canvas
	 * */
	public void drawBlock(Canvas canvas) {
		canvas.drawBitmap(bitmap,left,top,paint);
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

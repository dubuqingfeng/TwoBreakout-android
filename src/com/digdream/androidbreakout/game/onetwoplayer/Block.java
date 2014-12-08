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
 * ����һ����ҵ�ש����
 * 
 * ����һ����Ϸ��ש����������ShapeDrawableΪ������ɫֵ�ͳ��ڣ��Ա����������ɫ�ķ���������״̬��
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
	 * ��������ʹ�ø��෽������Gect��������ɫ
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
	 * ��������ʹ�ø��෽������Gect��������ɫ
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
	 * ����ש��
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
	 * ������ɫֵ
	 * 
	 * @return color value
	 * */
	public int getColor() {
		return paint.getColor();
	}
	/**
	 * ����ש��״̬
	 * @return state
	 */
	public int getBlockState() {
		return state;
	}
	/**
	 * �ı�ש��״̬
	 */
	public void changeBlockState(){
		this.state = 1;
	}

	/***
	 * ���ذ�����ɫ����������������ש�顣����һ��ש���״̬���浽�����С�ǰ�ĸ�ֵ��ʾ��ש������ꡣ���һ��ֵ��ש�����ɫ��
	 * 
	 * @return integer array containing the block's coordinates and color values.
	 * */
	public int[] toIntArray() {
		int[] arr = { this.getBounds().left, this.getBounds().top,
				this.getBounds().right, this.getBounds().bottom, blockColor };
		return arr;
	}
}

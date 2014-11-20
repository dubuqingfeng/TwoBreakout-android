package com.digdream.androidbreakout.game.oneplayer;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;

/**
 * ����һ����ҵ�ש����
 * 
 * ����һ����Ϸ��ש����������ShapeDrawableΪ������ɫֵ�ͳ��ڣ��Ա����������ɫ�ķ���������״̬��
 * */
public class Block extends ShapeDrawable {

	private Paint paint;
	private int blockColor;

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
	 * ����ש��
	 * 
	 * @param canvas
	 *            graphic canvas
	 * */
	public void drawBlock(Canvas canvas) {
		canvas.drawRect(this.getBounds(), paint);
	}

	/**
	 * ������ɫֵ
	 * 
	 * @return color value
	 * */
	public int getColor() {
		return paint.getColor();
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
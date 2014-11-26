package com.digdream.androidbreakout.game.twoplayer;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.util.Log;

/**
 *这是一个玩家的挡板类
 * 扩展增加挡板的长度
 * 代表游戏的挡板。延伸一个ShapeDrawable包括一个颜色值和用于在屏幕上设置挡板的定位方法。
 * */
public class Paddle extends ShapeDrawable {

	// paddle dimensions
	private int left;
	private int right;
	private int top;
	private int bottom;
	private int paddle_width;
	private int paddle_height;
	private int paddle_offset; // bottom screen offset
	private int paddle_move_offset; // move paddle to touch event speed

	private int SCREEN_WIDTH;
	private int SCREEN_HEIGHT;

	/**
	 * 构造器。调用父类方法并设置颜色值
	 * 
	 * */
	public Paddle() {
		super(new RectShape());
		this.getPaint().setColor(Color.WHITE);
	}

	/**
	 * 挡板的尺寸和坐标基于屏幕的宽度和高度。
	 * 
	 * @param width
	 *            screen width
	 * @param height
	 *            screen height
	 * */
	public void initCoords(int width, int height) {
		SCREEN_WIDTH = width;
		SCREEN_HEIGHT = height;

		paddle_width = SCREEN_WIDTH / 10;
		paddle_height = SCREEN_WIDTH / 72;
		paddle_offset = SCREEN_HEIGHT / 6;

		left = (SCREEN_WIDTH / 2) - paddle_width;
		right = (SCREEN_WIDTH / 2) + paddle_width;
		//宽高。
		if (!GameView2p.mIsInviter) {
            //这里不是邀请者
			
			top = (paddle_offset) - paddle_height;
			bottom = (paddle_offset) + paddle_height;
         }
         else
         {
        	 Log.w("test","test");
         	//这里是邀请者
         	top = (SCREEN_HEIGHT - paddle_offset) - paddle_height;
         	bottom = (SCREEN_HEIGHT - paddle_offset) + paddle_height;
         }
		

		paddle_move_offset = SCREEN_WIDTH / 15;
	}

	/**
	 * 绘制挡板
	 * 
	 * @param canvas
	 *            graphics canvas
	 * */
	public void drawPaddle(Canvas canvas) {
		this.setBounds(left, top, right, bottom);
		this.draw(canvas);
	}

	/**
	 *设置挡板的坐标。该挡板可以在屏幕上移动仅沿x轴。
	 * 
	 * @param x x axis value of touch event. Used to calculate the center of the paddle.
	 * */
	public void movePaddle(int x) {
		if (x >= left && x <= right) {
			left = x - paddle_width;
			right = x + paddle_width;
		} else if (x > right) {
			left += paddle_move_offset;
			right += paddle_move_offset;
		} else if (x < left) {
			left -= paddle_move_offset;
			right -= paddle_move_offset;
		}

		// keep paddle from going off screen left
		if (left < 0) {
			left = 0;
			right = paddle_width * 2;
		}

		// keep paddle from going off screen right
		if (right > SCREEN_WIDTH) {
			right = SCREEN_WIDTH;
			left = SCREEN_WIDTH - (paddle_width * 2);
		}
	}
}

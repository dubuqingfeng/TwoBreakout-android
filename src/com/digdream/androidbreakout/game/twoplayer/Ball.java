package com.digdream.androidbreakout.game.twoplayer;

import java.util.ArrayList;
import java.util.Random;

import com.digdream.androidbreakout.R;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.Log;

/**
 * 这是一个玩家的小球类。 增大半径，增加球，强壮球，遇到挡板砖块更改颜色，控制球速，增加球速，减慢球速，球初始位置和速度
 * 
 * 小球以立体球代替。。 负责绘制球，更新球的位置，碰撞检测，声音事件和得分点。
 * 
 * */
public class Ball extends ShapeDrawable {

	// 小球位置
	private int left;
	private int right;
	private int top;
	private int bottom;
	private int radius;

	// 球的速度
	private int velocityX;
	private int velocityY;

	// 当球击中屏幕底部的计时
	private final int resetBallTimer = 1000;

	private int SCREEN_WIDTH;
	private int SCREEN_HEIGHT;
	private boolean paddleCollision;
	private boolean topPaddleCollision;
	private boolean blockCollision;
	private Rect mPaddle;
	private Rect mTopPaddle;
	private Rect ballRect;

	private boolean soundOn;
	private SoundPool soundPool;
	private int paddleSoundId;
	private int blockSoundId;
	private int bottomSoundId;

	/**
	 * 构造器。设置颜色及声音参数.
	 * 
	 * @param context
	 *            Android context
	 * @param sound
	 *            sound on or off
	 * */
	public Ball(Context context, boolean sound) {
		super(new OvalShape());
		this.getPaint().setColor(Color.CYAN);
		soundOn = sound;

		if (soundOn) {
			soundPool = new SoundPool(2, AudioManager.STREAM_MUSIC, 0);
			paddleSoundId = soundPool.load(context, R.raw.paddle, 0);
			blockSoundId = soundPool.load(context, R.raw.block, 0);
			bottomSoundId = soundPool.load(context, R.raw.bottom, 0);
		}
	}

	/**
	 * 初始化球参数。计算，根据球的尺寸屏幕的宽度和高度。设置一个起始速度。随机选择球是否移动向左或向右的开始。
	 * 
	 * @param width
	 *            screen width
	 * @param height
	 *            screen height
	 * */
	public void initCoords(int width, int height) {
		Random rnd = new Random(); // starting x velocity direction

		paddleCollision = false;
		topPaddleCollision = false;
		blockCollision = false;
		SCREEN_WIDTH = width;
		SCREEN_HEIGHT = height;

		radius = SCREEN_WIDTH / 48;
		velocityX = radius;
		velocityY = radius * 2;

		// ball coordinates 球坐标
		left = (SCREEN_WIDTH / 2) - radius;
		right = (SCREEN_WIDTH / 2) + radius;
		top = (SCREEN_HEIGHT / 2) - radius;
		bottom = (SCREEN_HEIGHT / 2) + radius;

		int startingXDirection = 1;
		if (startingXDirection > 0) {
			if (!GameView2p.mIsInviter) {
				// 这里不是邀请者
				velocityX = -velocityX;
			} else {
				// 这里是邀请者
				velocityY = -velocityY;
			}
			
		}
	}

	/**
	 * 绘制球
	 * 
	 * @param canvas
	 *            graphical canvas
	 * */
	public void drawBall(Canvas canvas) {
		this.setBounds(left, top, right, bottom);
		this.draw(canvas);
	}

	/**
	 *  更新球的坐标。如果有冲突，方向球的速度而改变。返回一个整数，这取决于是否与屏幕底部的球碰撞。返回值使用递减生命数。
	 * 
	 * @return number to decrement player turns
	 * */
	public int setVelocity() {
		int bottomHit = 0;

		if (blockCollision) {
			velocityY = -velocityY;
			blockCollision = false; // reset
		}

		// 挡板碰撞
		if (paddleCollision && velocityY > 0) {
			int paddleSplit = (mPaddle.right - mPaddle.left) / 4;
			int ballCenter = ballRect.centerX();
			if (ballCenter < mPaddle.left + paddleSplit) {
				velocityX = -(radius * 3);
			} else if (ballCenter < mPaddle.left + (paddleSplit * 2)) {
				velocityX = -(radius * 2);
			} else if (ballCenter < mPaddle.centerX() + paddleSplit) {
				velocityX = radius * 2;
			} else {
				velocityX = radius * 3;
			}
			velocityY = -velocityY;
		}
		// side walls collision侧壁碰撞
		if (topPaddleCollision && velocityY > 0) {
			int toppaddleSplit = (mTopPaddle.right - mTopPaddle.left) / 4;
			int ballCenter = ballRect.centerX();
			if (ballCenter < mTopPaddle.left + toppaddleSplit) {
				velocityX = -(radius * 3);
			} else if (ballCenter < mTopPaddle.left + (toppaddleSplit * 2)) {
				velocityX = -(radius * 2);
			} else if (ballCenter < mTopPaddle.centerX() + toppaddleSplit) {
				velocityX = radius * 2;
			} else {
				velocityX = radius * 3;
			}
			velocityY = -velocityY;
		}

		// side walls collision
		if (this.getBounds().right >= SCREEN_WIDTH) {
			velocityX = -velocityX;
		} else if (this.getBounds().left <= 0) {
			this.setBounds(0, top, radius * 2, bottom);
			velocityX = -velocityX;
		}

		// screen top/bottom collisions
		if (this.getBounds().top <= 0) {
			velocityY = -velocityY;
		} else if (this.getBounds().top > SCREEN_HEIGHT) {
			bottomHit = 1; // lose a turn
			if (soundOn) {
				soundPool.play(bottomSoundId, 1, 1, 1, 0, 1);
			}
			try {
				Thread.sleep(resetBallTimer);
				initCoords(SCREEN_WIDTH, SCREEN_HEIGHT); // reset ball
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		// move ball
		left += velocityX / 2;
		right += velocityX / 2;
		top += velocityY / 2;
		bottom += velocityY / 2;

		return bottomHit;
	}

	/**
	 * 检查是否球已经撞上了挡板。音效如果声音效果有碰撞和声音被启用。
	 * 
	 * @param paddle
	 *            paddle object
	 * 
	 * @return true if there is a collision
	 * 
	 * */
	public boolean checkPaddleCollision(Paddle paddle) {
		mPaddle = paddle.getBounds();
		ballRect = this.getBounds();

		if (ballRect.left >= mPaddle.left - (radius * 2)
				&& ballRect.right <= mPaddle.right + (radius * 2)
				&& ballRect.bottom >= mPaddle.top - (radius * 2)
				&& ballRect.top <= mPaddle.bottom + (radius * 2)) {
			paddleCollision = true;
			if (soundOn && velocityY > 0) {
				soundPool.play(paddleSoundId, 1, 1, 1, 0, 1);
			}
		} else
			paddleCollision = false;

		return paddleCollision;
	}
	public boolean checkTopPaddleCollision(TopPaddle toppaddle) {
		mTopPaddle = toppaddle.getBounds();
		ballRect = this.getBounds();

		if (ballRect.left >= mTopPaddle.left - (radius * 2)
				&& ballRect.right <= mTopPaddle.right + (radius * 2)
				&& ballRect.bottom >= mTopPaddle.top - (radius * 2)
				&& ballRect.top <= mTopPaddle.bottom + (radius * 2)) {
			topPaddleCollision = true;
			if (soundOn && velocityY > 0) {
				soundPool.play(paddleSoundId, 1, 1, 1, 0, 1);
			}
		} else
			topPaddleCollision = false;

		return topPaddleCollision;
	}

	/**
	 * 检查在一个ArrayList每块与球的碰撞。如果有发生碰撞时，该块的点的值被添加到一个总点数。如果启用音效，声音效果会碰撞。如果有一个碰撞，
	 * blockCollision被设置为true 。
	 * 
	 * @param blocks
	 *            ArrayList of block objects
	 * 
	 * @return points total from blocks
	 * */
	public int checkBlocksCollision(ArrayList<Block> blocks) {
		int points = 0;
		int blockListLength = blocks.size();
		ballRect = this.getBounds();

		int ballLeft = ballRect.left + velocityX;
		int ballRight = ballRect.right + velocityY;
		int ballTop = ballRect.top + velocityY;
		int ballBottom = ballRect.bottom + velocityY;

		// check collision; remove block if true
		for (int i = blockListLength - 1; i >= 0; i--) {
			Rect blockRect = blocks.get(i).getBounds();
			int color = blocks.get(i).getColor();

			if (ballLeft >= blockRect.left - (radius * 2)
					&& ballLeft <= blockRect.right + (radius * 2)
					&& (ballTop == blockRect.bottom || ballTop == blockRect.top)) {
				blockCollision = true;
				blocks.remove(i);
			} else if (ballRight <= blockRect.right
					&& ballRight >= blockRect.left
					&& ballTop <= blockRect.bottom && ballTop >= blockRect.top) {
				blockCollision = true;
				blocks.remove(i);
			} else if (ballLeft >= blockRect.left
					&& ballLeft <= blockRect.right
					&& ballBottom <= blockRect.bottom
					&& ballBottom >= blockRect.top) {
				blockCollision = true;
				blocks.remove(i);
			} else if (ballRight <= blockRect.right
					&& ballRight >= blockRect.left
					&& ballBottom <= blockRect.bottom
					&& ballBottom >= blockRect.top) {
				blockCollision = true;
				blocks.remove(i);
			}

			if (blockCollision) {
				if (soundOn) {
					soundPool.play(blockSoundId, 1, 1, 1, 0, 1);
				}
				return points += getPoints(color);
			}
		}
		return points;
	}

	/**
	 * 返回一个基于颜色的块的分数。
	 * 
	 * @param color
	 *            block color
	 * 
	 * @return point value of block
	 * */
	private int getPoints(int color) {
		int points = 0;
		if (color == Color.LTGRAY)
			points = 100;
		else if (color == Color.MAGENTA)
			points = 200;
		else if (color == Color.GREEN)
			points = 300;
		else if (color == Color.YELLOW)
			points = 400;
		else if (color == Color.RED)
			points = 500;

		return points;
	}

	/**
	 * 返回球的当前y速度值。
	 * 
	 * @return current Y velocity of the ball
	 * */
	public int getVelocityY() {
		return velocityY;
	}

	/**
	 * Releases sound assets.
	 * */
	public void close() {
		if (soundOn) {
			soundPool.release();
			soundPool = null;
		}
	}
}

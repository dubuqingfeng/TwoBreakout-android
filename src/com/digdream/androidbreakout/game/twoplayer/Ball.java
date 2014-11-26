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
 * ����һ����ҵ�С���ࡣ ����뾶��������ǿ׳����������ש�������ɫ���������٣��������٣��������٣����ʼλ�ú��ٶ�
 * 
 * С������������档�� ��������򣬸������λ�ã���ײ��⣬�����¼��͵÷ֵ㡣
 * 
 * */
public class Ball extends ShapeDrawable {

	// С��λ��
	private int left;
	private int right;
	private int top;
	private int bottom;
	private int radius;

	// ����ٶ�
	private int velocityX;
	private int velocityY;

	// ���������Ļ�ײ��ļ�ʱ
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
	 * ��������������ɫ����������.
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
	 * ��ʼ������������㣬������ĳߴ���Ļ�Ŀ�Ⱥ͸߶ȡ�����һ����ʼ�ٶȡ����ѡ�����Ƿ��ƶ���������ҵĿ�ʼ��
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

		// ball coordinates ������
		left = (SCREEN_WIDTH / 2) - radius;
		right = (SCREEN_WIDTH / 2) + radius;
		top = (SCREEN_HEIGHT / 2) - radius;
		bottom = (SCREEN_HEIGHT / 2) + radius;

		int startingXDirection = 1;
		if (startingXDirection > 0) {
			if (!GameView2p.mIsInviter) {
				// ���ﲻ��������
				velocityX = -velocityX;
			} else {
				// ������������
				velocityY = -velocityY;
			}
			
		}
	}

	/**
	 * ������
	 * 
	 * @param canvas
	 *            graphical canvas
	 * */
	public void drawBall(Canvas canvas) {
		this.setBounds(left, top, right, bottom);
		this.draw(canvas);
	}

	/**
	 *  ����������ꡣ����г�ͻ����������ٶȶ��ı䡣����һ����������ȡ�����Ƿ�����Ļ�ײ�������ײ������ֵʹ�õݼ���������
	 * 
	 * @return number to decrement player turns
	 * */
	public int setVelocity() {
		int bottomHit = 0;

		if (blockCollision) {
			velocityY = -velocityY;
			blockCollision = false; // reset
		}

		// ������ײ
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
		// side walls collision�����ײ
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
	 * ����Ƿ����Ѿ�ײ���˵��塣��Ч�������Ч������ײ�����������á�
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
	 * �����һ��ArrayListÿ���������ײ������з�����ײʱ���ÿ�ĵ��ֵ����ӵ�һ���ܵ��������������Ч������Ч������ײ�������һ����ײ��
	 * blockCollision������Ϊtrue ��
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
	 * ����һ��������ɫ�Ŀ�ķ�����
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
	 * ������ĵ�ǰy�ٶ�ֵ��
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

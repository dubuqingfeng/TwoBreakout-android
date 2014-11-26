package com.digdream.androidbreakout.game.oneplayer;

import java.util.ArrayList;
import java.util.Random;

import com.digdream.androidbreakout.R;
import com.digdream.androidbreakout.game.twoplayer.GameView2p;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
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

	private static Random rand = new Random();

	// ����ٶ�
	private int velocityX;
	private int velocityY;

	// �ж��Ƿ������Լ���͸
	public boolean addflag;
	public boolean strongflag = false;
	// ���������Ļ�ײ��ļ�ʱ
	private final int resetBallTimer = 1000;

	private int SCREEN_WIDTH;
	private int SCREEN_HEIGHT;
	private boolean paddleCollision;
	private boolean blockCollision;
	private Rect mPaddle;
	private Rect ballRect;

	private Matrix matrix;
	Bitmap ballbmp;

	private boolean soundOn;
	private SoundPool soundPool;
	private int paddleSoundId;
	private int blockSoundId;
	private int bottomSoundId;
	private final String TAG = "Ball";
	private Bitmap[] itembmp = new Bitmap[4];

	/**
	 * ��������������ɫ����������
	 * 
	 * @param context
	 *            Android context
	 * @param sound
	 *            sound on or off
	 * */
	@SuppressWarnings("deprecation")
	public Ball(Context context, boolean sound) {

		// super();
		super(new OvalShape());

		// this.matrix = new Matrix();
		// this.matrix.postScale(0.7F, 0.7F);

		// this.ballbmp = Bitmap.createBitmap(readBitmap(context, "r" + 0), 0,
		// 0, 28, 28,
		// this.matrix, true);

		this.getPaint().setColor(Color.CYAN);
		soundOn = sound;

		if (soundOn) {
			soundPool = new SoundPool(2, AudioManager.STREAM_MUSIC, 0);
			paddleSoundId = soundPool.load(context, R.raw.paddle, 0);
			blockSoundId = soundPool.load(context, R.raw.block, 0);
			bottomSoundId = soundPool.load(context, R.raw.bottom, 0);
		}
		this.itembmp[1] = readBitmap(context, "item1");
	}

	private static Bitmap readBitmap(Context paramContext, String paramString) {
		int i = paramContext.getResources().getIdentifier(paramString,
				"drawable", paramContext.getPackageName());
		return BitmapFactory.decodeResource(paramContext.getResources(), i);
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
		// Random rnd = new Random(); // starting x velocity direction

		paddleCollision = false;
		blockCollision = false;
		SCREEN_WIDTH = width;
		SCREEN_HEIGHT = height;

		// ��İ뾶
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
				// ��ʼ�ٶȷ��򣬴�С
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
	
	public void drawItem(Canvas canvas,Item item) {
		item.drawItem(canvas);
	}

	/**
	 * ����������ꡣ����г�ͻ����������ٶȶ��ı䡣����һ����������ȡ�����Ƿ�����Ļ�ײ�������ײ������ֵʹ�õݼ���������
	 * 
	 * @return number to decrement player turns
	 * */
	public int setVelocity() {
		int bottomHit = 0;

		if (blockCollision) {
			// ����������򣬲�ִ��������
			if (!strongflag) {
				velocityY = -velocityY;
			}
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
			// this is a collision change color
			this.getPaint().setColor(Color.BLACK);
			if (soundOn && velocityY > 0) {
				soundPool.play(paddleSoundId, 1, 1, 1, 0, 1);
			}
		} else
			paddleCollision = false;

		return paddleCollision;
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
	public int checkBlocksCollision(ArrayList<Block> blocks,Canvas canvas) {
		int points = 0;
		int blockListLength = blocks.size();
		ballRect = this.getBounds();

		int ballLeft = ballRect.left + velocityX;
		int ballRight = ballRect.right + velocityY;
		int ballTop = ballRect.top + velocityY;
		int ballBottom = ballRect.bottom + velocityY;

		// check collision; remove block if true
		for (int i = blockListLength - 1; i >= 0; i--) {
			Rect blockRect = blocks.get(i).getRect();
			int color = blocks.get(i).getColor();

			/*
			 * if (ballLeft >= blocks.get(i).left - (radius * 2) && ballLeft <=
			 * blocks.get(i).right + (radius * 2) && (ballTop ==
			 * blocks.get(i).bottom || ballTop == blocks.get(i).top)) {
			 * blockCollision = true; blocks.remove(i); } else if (ballRight <=
			 * blocks.get(i).right && ballRight >= blocks.get(i).left && ballTop
			 * <= blocks.get(i).bottom && ballTop >= blocks.get(i).top) {
			 * blockCollision = true; blocks.remove(i); } else if (ballLeft >=
			 * blocks.get(i).left && ballLeft <= blocks.get(i).right &&
			 * ballBottom <= blocks.get(i).bottom && ballBottom >=
			 * blocks.get(i).top) { blockCollision = true; blocks.remove(i); }
			 * else if (ballRight <= blocks.get(i).right && ballRight >=
			 * blocks.get(i).left && ballBottom <= blocks.get(i).bottom &&
			 * ballBottom >= blocks.get(i).top) { blockCollision = true;
			 * blocks.remove(i); }
			 */
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
				Log.d(TAG, "blockCollision");
				// ���������Ӹı���ɫ
				this.getPaint().setColor(Color.BLUE);
				
				return points += getPoints(color);
			}
			
		}
		return points;
	}

	private static int rand(int paramInt) {
		return (rand.nextInt() >>> 1) % paramInt;
	}

	/**
	 * �������һ������������� Returns
	 * 
	 * @param
	 * 
	 * @author user
	 * 
	 * @return
	 */

	public void addBallVelocity() {
		if (velocityX <= 5) {

		}
		if (velocityY <= 5) {

		}
	}

	/**
	 * �����ṩһ������������ٵķ��� Returns
	 * 
	 * @param
	 * 
	 * @author user
	 * 
	 * @return
	 */
	public void subBallVelocity() {
		if (velocityX >= 10) {

		}
		if (velocityY >= 10) {

		}
	}

	/**
	 * 
	 */
	public void addBallSize() {

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
	 * ������ĵ�ǰx�ٶ�ֵ
	 * 
	 * @return current X velocity of the ball
	 */
	public int getVelocityX() {
		return velocityX;
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

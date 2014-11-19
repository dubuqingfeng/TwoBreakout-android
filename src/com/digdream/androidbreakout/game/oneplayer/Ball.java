package com.digdream.androidbreakout.game.oneplayer;

import java.util.ArrayList;

import com.digdream.androidbreakout.GameView2p;
import com.digdream.androidbreakout.R;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.media.AudioManager;
import android.media.SoundPool;

/**
 * 这是一个玩家的小球类。
 * 增大半径，增加球，强壮球，遇到挡板砖块更改颜色，控制球速，增加球速，减慢球速，球初始位置和速度
 * 
 * Represents a game ball. Responsible for drawing the ball, updating the ball's
 * position, collision checking, sound events, and point scoring.
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

	//判断是否增球以及强壮
	public boolean addflag;
	public boolean strongflag;
	// timer when ball hits screen bottom
	private final int resetBallTimer = 1000;

	private int SCREEN_WIDTH;
	private int SCREEN_HEIGHT;
	private boolean paddleCollision;
	private boolean blockCollision;
	private Rect mPaddle;
	private Rect ballRect;

	private boolean soundOn;
	private SoundPool soundPool;
	private int paddleSoundId;
	private int blockSoundId;
	private int bottomSoundId;

	/**
	 * Constructor. Sets the Paint and sound parameters.
	 * 
	 * @param context
	 *            Android context
	 * @param sound
	 *            sound on or off
	 * */
	@SuppressWarnings("deprecation")
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
	 * Initializes ball parameters. Calculates the ball's dimensions according
	 * to the screen's width and height. Sets a starting velocity. Randomly
	 * chooses whether the ball moves right or left at start.
	 * 
	 * @param width
	 *            screen width
	 * @param height
	 *            screen height
	 * */
	public void initCoords(int width, int height) {
		//Random rnd = new Random(); // starting x velocity direction

		paddleCollision = false;
		blockCollision = false;
		SCREEN_WIDTH = width;
		SCREEN_HEIGHT = height;

		//球的半径
		radius = SCREEN_WIDTH / 64;
		velocityX = radius;
		velocityY = radius * 2;

		// ball coordinates
		left = (SCREEN_WIDTH / 2) - radius;
		right = (SCREEN_WIDTH / 2) + radius;
		top = (SCREEN_HEIGHT / 2) - radius;
		bottom = (SCREEN_HEIGHT / 2) + radius;

		int startingXDirection = 1;
		if (startingXDirection > 0) {
			if (!GameView2p.mIsInviter) {
				// 这里不是邀请者
				// 初始速度方向，大小
				velocityX = -velocityX;
			} else {
				// 这里是邀请者
				velocityY = -velocityY;
			}
			
		}
	}

	/**
	 * Draws ball to canvas.
	 * 
	 * @param canvas
	 *            graphical canvas
	 * */
	public void drawBall(Canvas canvas) {
		this.setBounds(left, top, right, bottom);
		this.draw(canvas);
	}

	/**
	 * Updates the ball's coordinates. If there is a collision, the direction of
	 * the ball's velocity is changed. Returns an integer depending on whether
	 * the ball collides with the bottom of the screen. The return value is used
	 * to decrement player turns.
	 * 
	 * @return number to decrement player turns
	 * */
	public int setVelocity() {
		int bottomHit = 0;

		if (blockCollision) {
			velocityY = -velocityY;
			blockCollision = false; // reset
		}

		// paddle collision
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
		left += velocityX;
		right += velocityX;
		top += velocityY;
		bottom += velocityY;

		return bottomHit;
	}

	/**
	 * Checks if the ball has collided with the paddle. Plays a sound effect if
	 * there is a collision and sound is enabled.
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
			//this is a collision change color
			this.getPaint().setColor(Color.BLACK);
			if (soundOn && velocityY > 0) {
				soundPool.play(paddleSoundId, 1, 1, 1, 0, 1);
			}
		} else
			paddleCollision = false;

		return paddleCollision;
	}

	/**
	 * Checks for a ball collision with each block in an ArrayList. If there is
	 * a collision, the point value of the block is added to a points total. If
	 * sound is enabled, a sound effect will play on collision. If there is a
	 * collision, blockCollision is set to true for the setVelocity method.
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
				//这里可以添加改变颜色
				//there can add change color.
				this.getPaint().setColor(Color.BLUE);
				return points += getPoints(color);
			}
		}
		return points;
	}
	/**
	 * 这里添加一个落物，增加球速
	 * Returns
	 * 
	 * @param 
	 * 
	 * @author user
	 * 
	 * @return
	 */
	
	public void addBallVelocity(){
		if(velocityX <= 5 )
		{
			
		}
		if(velocityY <= 5 )
		{
			
		}
	}
	
	/**
	 * 这里提供一个落物，减慢球速的方法
	 * Returns 
	 * 
	 * @param
	 * 
	 * @author user
	 * 
	 * @return
	 */
	public void subBallVelocity(){
		if(velocityX >= 10 )
		{
			
		}
		if(velocityY >= 10 )
		{
			
		}
	}

	/**
	 * Returns the point value of a block based on color.
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
	 * Returns the current Y velocity value of the ball.
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

package com.digdream.androidbreakout.game.oneplayer;

import java.util.ArrayList;
import java.util.Random;

import com.digdream.androidbreakout.R;
import com.digdream.androidbreakout.data.UserPreferences;
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

	private static Random rand = new Random();

	// 球的速度
	private int velocityX;
	private int velocityY;

	// 判断是否增球以及穿透
	public boolean addflag;
	public boolean bigballflag = false;
	public boolean strongflag = false;
	// 当球击中屏幕底部的计时
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
	private Bitmap[] itembmp = new Bitmap[9];
	private UserPreferences preferences;
	boolean ItemExist;
	private Item item;
	private boolean newGame = false;

	/**
	 * 构造器。设置颜色及声音参数
	 * 
	 * @param context
	 *            Android context
	 * @param sound
	 *            sound on or off
	 * */
	@SuppressWarnings("deprecation")
	public Ball(Context context, boolean sound) {

		super(new OvalShape());

		Random random = new Random();
		int r = random.nextInt(256);
		int g = random.nextInt(256);
		int b = random.nextInt(256);
		this.getPaint().setColor(Color.rgb(r, g, b));
		soundOn = sound;

		soundPool = new SoundPool(2, AudioManager.STREAM_MUSIC, 0);
		paddleSoundId = soundPool.load(context, R.raw.paddle, 0);
		blockSoundId = soundPool.load(context, R.raw.block, 0);
		bottomSoundId = soundPool.load(context, R.raw.bottom, 0);
		for(int i = 1; i < 7 ;i++){
			this.itembmp[i] = readBitmap(context, "item"+i);
		}
		//读取球速
		preferences = new UserPreferences();
		preferences.init(context);
	}

	private static Bitmap readBitmap(Context paramContext, String paramString) {
		int i = paramContext.getResources().getIdentifier(paramString,
				"drawable", paramContext.getPackageName());
		return BitmapFactory.decodeResource(paramContext.getResources(), i);
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
		// Random rnd = new Random(); // starting x velocity direction

		paddleCollision = false;
		blockCollision = false;
		strongflag = false;
		
		SCREEN_WIDTH = width;
		SCREEN_HEIGHT = height;

		// 球的半径
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
				// 初始速度方向，大小
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
	
	public void drawItem(Canvas canvas) {
		item.drawItem(canvas);
	}

	/**
	 * 更新球的坐标。如果有冲突，方向球的速度而改变。返回一个整数，这取决于是否与屏幕底部的球碰撞。返回值使用递减生命数。
	 * 
	 * @return number to decrement player turns
	 * */
	public int setVelocity() {
		int bottomHit = 0;

		if (blockCollision) {
			// 如果有闪光球，不执行条件句
			if (!strongflag) {
				velocityY = -velocityY;
			}
			int a = (int)(Math.random() *100 +1);
			if(a < 20){
				//生成个掉落物体
				int i = (int)(Math.random() *6 +1);
				item = new Item(i,left,top,itembmp[i]);
				ItemExist = true;
			}
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
				newGame  = true;
				radius = SCREEN_WIDTH / 48;
				bigballflag = false;
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		int velocity = preferences.getOneVelocity();
		// move ball
		left += velocityX / 2 / velocity;
		right += velocityX / 2 / velocity;
		top += velocityY / 2 / velocity;
		bottom += velocityY / 2 / velocity;

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

		if(newGame){
			paddle.setPaddleWidth();
			newGame = false;
		}
		if (ballRect.left >= mPaddle.left - (radius * 2)
				&& ballRect.right <= mPaddle.right + (radius * 2)
				&& ballRect.bottom >= mPaddle.top - (radius * 2)
				&& ballRect.top <= mPaddle.bottom + (radius * 2)) {
			paddleCollision = true;
			// this is a collision change color
			//randColor();
			Random random = new Random();
			int r = random.nextInt(256);
			int g = random.nextInt(256);
			int b = random.nextInt(256);
			this.getPaint().setColor(Color.rgb(r, g, b));
			//this.getPaint().setColor(Color.BLACK);
			if (soundOn && velocityY > 0) {
				soundPool.play(paddleSoundId, 1, 1, 1, 0, 1);
			}
		} else
			paddleCollision = false;

		return paddleCollision;
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
				if(blocks.get(i).getBlockState() == 1)
					blocks.remove(i);
				else if (blocks.get(i).getBlockState() == 2){
					blocks.get(i).changeBlockState();
				}
			} else if (ballRight <= blockRect.right
					&& ballRight >= blockRect.left
					&& ballTop <= blockRect.bottom && ballTop >= blockRect.top) {
				blockCollision = true;
				if(blocks.get(i).getBlockState() == 1)
					blocks.remove(i);
				else if (blocks.get(i).getBlockState() == 2){
					blocks.get(i).changeBlockState();
				}
			} else if (ballLeft >= blockRect.left
					&& ballLeft <= blockRect.right
					&& ballBottom <= blockRect.bottom
					&& ballBottom >= blockRect.top) {
				blockCollision = true;
				if(blocks.get(i).getBlockState() == 1)
					blocks.remove(i);
				else if (blocks.get(i).getBlockState() == 2){
					blocks.get(i).changeBlockState();
				}
			} else if (ballRight <= blockRect.right
					&& ballRight >= blockRect.left
					&& ballBottom <= blockRect.bottom
					&& ballBottom >= blockRect.top) {
				blockCollision = true;
				if(blocks.get(i).getBlockState() == 1)
					blocks.remove(i);
					
				else if (blocks.get(i).getBlockState() == 2){
					blocks.get(i).changeBlockState();
				}
			}

			if (blockCollision) {
				if (soundOn) {
					soundPool.play(blockSoundId, 1, 1, 1, 0, 1);
				}
				// 这里可以添加改变颜色
				Random random = new Random();
				int r = random.nextInt(256);
				int g = random.nextInt(256);
				int b = random.nextInt(256);
				this.getPaint().setColor(Color.rgb(r, g, b));
				//randColor();
				//this.getPaint().setColor(Color.BLUE);
				
				return points += getPoints(color);
			}
			
		}
		return points;
	}
	/**
	 * 随机产生球的颜色
	 */

	private void randColor() {
		Random random = new Random();
		int r = random.nextInt(256);
		int g = random.nextInt(256);
		int b = random.nextInt(256);
		this.getPaint().setColor(Color.rgb(r, g, b));
	}

	private static int rand(int paramInt) {
		return (rand.nextInt() >>> 1) % paramInt;
	}

	/**
	 * 这里添加一个落物，增加球速 Returns
	 * 
	 * @param
	 * 
	 * @author user
	 * 
	 * @return
	 */

	public void addBallVelocity() {
		if (velocityX <= 500) {
			if(velocityX >= 0){
				velocityX += 10;
			}else{
				velocityX -= 10;
			}
		}
		if (velocityY <= 500) {
			if(velocityY >= 0){
				velocityY += 10;
			}else{
				velocityY -= 10;
			}
			
		}
	}

	/**
	 * 这里提供一个落物，减慢球速的方法 Returns
	 * 
	 * @param
	 * 
	 * @author user
	 * 
	 * @return
	 */
	public void subBallVelocity() {
		if (velocityX >= 2) {
			velocityX -= 10;
		}else if(velocityX <= -2){
			velocityX += 10;
		}
		if (velocityY >= 2) {
			velocityY -= 10;
		}else if(velocityY <= -2){
			velocityY += 10;
		}
	}

	/**
	 * 增大球尺寸
	 */
	public void addBallSize() {
		radius = SCREEN_WIDTH / 150;
		// ball coordinates 球坐标
		left -=  radius;
		right += radius;
		top -= radius;
		bottom +=  radius;
		bigballflag = true;
	}
	public void setStrongBall(){
		strongflag = true;
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
	 * 返回球的当前x速度值
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
			//NullPointer
			soundPool.release();
			soundPool = null;
		}
	}

	public void drawItemSetVelocity() {
		if(item.Y > SCREEN_HEIGHT){
			ItemExist = false;
		}
		item.setVelocityY();
	}

	/*
	 * 检查落物是否与挡板相撞
	 */
	public boolean checkItemPaddleCollision(Paddle paddle) {
		//if(item.X)
		//ballRect = this.getBounds();
		if(item.X >= mPaddle.left && item.X >= mPaddle.right){
			item.paddleCollision = false;
		}else if(item.X <= mPaddle.left && item.getBounds().right <= mPaddle.left){
			item.paddleCollision = false;
		}else if(item.Y >= mPaddle.top && item.Y >= mPaddle.bottom){
			item.paddleCollision = false;
		}else if(item.Y <= mPaddle.top && item.getBounds().bottom <= mPaddle.top){
			item.paddleCollision = false;
		}else {
			item.paddleCollision = true;
			/**
			 * itemflg  
			 * 3	增大挡板
			 * 4	减小挡板
			 * 2	strong球
			 * 5	增加速度
			 * 6 	减小速度
			 */
			switch(item.Itemflg){
			case 1:
				if(!bigballflag){
					addBallSize();
				}
				break;
			case 2:setStrongBall();
				break;
			case 3:
				paddle.addPaddleWidth();
				break;
			case 4:paddle.subPaddleWidth();break;
			case 5:addBallVelocity();break;
			case 6:subBallVelocity();break;
			}
		}
		return item.paddleCollision;
	}
}

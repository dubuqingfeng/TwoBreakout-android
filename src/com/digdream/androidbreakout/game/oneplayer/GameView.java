package com.digdream.androidbreakout.game.oneplayer;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.util.ArrayList;

import com.digdream.androidbreakout.R;
import com.lenovo.game.GameMessageListener;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * 创建并绘制图形的游戏。运行一个线程用于动画和游戏物理。保存并暂停或恢复时恢复游戏数据。
 * 
 */
public class GameView extends SurfaceView implements Runnable {

	private boolean showGameOverBanner = false;
	private int levelCompleted = 0;
	private int playerTurns;
	private int PLAYER_TURNS_NUM = 3;
	private Paint turnsPaint;
	private String playerTurnsText = "生命数 = ";
	private boolean soundToggle;
	private int startNewGame;
	private ObjectOutputStream oos;
	private final String FILE_PATH = "data/data/com.digdream.androidbreakout/data.dat";
	private final int frameRate = 33;
	private final int startTimer = 66;
	private boolean touched = false;
	private float eventX;
	private SurfaceHolder holder;
	private Thread gameThread = null;
	private boolean running = false;
	private Canvas canvas;
	private boolean checkSize = true;
	private boolean newGame = true;
	private int waitCount = 0;
	//ball应改为数组
	private Ball ball;
	private Paddle paddle;
	private ArrayList<Block> blocksList;
	private String getReady = "请准备 ...";
	private Paint getReadyPaint;
	private int points = 0;
	private Paint scorePaint;
	private String score = "总分  = ";
	private BitmapDrawable bitmapDrawable;
	private Bitmap bitmap;

	/**
	 * 构造函数。设置声音的状态和新的游戏信号，根据从breakout类传入intent。实例球，砖块和挡板。设置了paint参数绘制文本到屏幕上。
	 * 
	 * @param context
	 *            Android Context
	 * @param launchNewGame
	 *            start new game or load save game
	 * @param sound
	 *            sound on/off
	 * */
	public GameView(Context context, int launchNewGame, boolean sound) {
		super(context);
		startNewGame = launchNewGame; // new game or continue
		playerTurns = PLAYER_TURNS_NUM;
		soundToggle = sound;
		holder = getHolder();
		//初始化ball球。
		ball = new Ball(this.getContext(), soundToggle);
		paddle = new Paddle();
		blocksList = new ArrayList<Block>();
		//设置背景
		setBackgroundResource(R.drawable.chara1);
		scorePaint = new Paint();
		scorePaint.setColor(Color.WHITE);
		scorePaint.setTextSize(25);

		turnsPaint = new Paint();
		turnsPaint.setTextAlign(Paint.Align.RIGHT);
		turnsPaint.setColor(Color.WHITE);
		turnsPaint.setTextSize(25);

		getReadyPaint = new Paint();
		getReadyPaint.setTextAlign(Paint.Align.CENTER);
		getReadyPaint.setColor(Color.WHITE);
		getReadyPaint.setTextSize(45);
	}

	/**
	 * 运行游戏线程。设置帧速率来绘制图形。使用画布绘制。如果不存在的砖块，初始化游戏对象。移动根据触摸事件的挡板。
	 * 检查碰撞的球的移动。跟踪玩家结束比赛的时候跑出来。奖励的玩家，当等级完成一个额外的回合。绘制文本
	 * 屏幕显示玩家的分数和剩余生命。绘制文本宣布比赛开始或结束时。
	 * 
	 * */
	public void run() {
		while (running) {
			try {
				Thread.sleep(frameRate);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			if (holder.getSurface().isValid()) {
				canvas = holder.lockCanvas();
				//获得图片
				bitmapDrawable=(BitmapDrawable)getResources().getDrawable(R.drawable.chara1);
				//设置显示大小
				bitmapDrawable.setBounds(0, 0, 80, 80);
				bitmap=(bitmapDrawable).getBitmap();
				//画出图片
				canvas.drawBitmap(bitmap, 50, 50, null);
				//canvas.drawColor(Color.BLACK);
				canvas.drawColor(Color.TRANSPARENT, Mode.CLEAR);// 清屏幕.  
				if (blocksList.size() == 0) {
					checkSize = true;
					newGame = true;
					levelCompleted++;
				}

				if (checkSize) {
					initObjects(canvas);
					checkSize = false;
					// extra turn for finished level
					if (levelCompleted > 1) {
						playerTurns++;
					}
				}

				if (touched) {
					paddle.movePaddle((int) eventX);
				}

				drawToCanvas(canvas);

				// pause screen on new game
				if (newGame) {
					waitCount = 0;
					newGame = false;
				}
				waitCount++;

				engine(canvas, waitCount);
				String printScore = score + points;
				canvas.drawText(printScore, 0, 25, scorePaint);
				String turns = playerTurnsText + playerTurns;
				canvas.drawText(turns, canvas.getWidth(), 25, turnsPaint);
				holder.unlockCanvasAndPost(canvas); // release canvas
			}
		}
	}

	/**
	 * 画图像给屏幕
	 * 
	 * @param canvas
	 *            graphics canvas
	 * */
	private void drawToCanvas(Canvas canvas) {
		drawBlocks(canvas);
		paddle.drawPaddle(canvas);
		ball.drawBall(canvas);
	}

	/**
	 * 暂停动画，直到等待计数器被满足。设置速度和球坐标。检查碰撞。如果检查游戏就结束了。
	 * 绘制文本来提醒用户，如果球重新启动或游戏就结束了。...
	 * 
	 * @param canvas
	 *            graphics canvas
	 * @param waitCt
	 *            number of frames to pause the game
	 * */
	private void engine(Canvas canvas, int waitCt) {
		if (waitCount > startTimer) {
			showGameOverBanner = false;
			playerTurns -= ball.setVelocity();
			if (playerTurns < 0) {
				showGameOverBanner = true;
				gameOver(canvas);
				return;
			}
			// paddle collision
			ball.checkPaddleCollision(paddle);
			// block collision and points tally
			points += ball.checkBlocksCollision(blocksList);
		}

		else {
			if (showGameOverBanner) {
				//跳转到结果activity
				getReadyPaint.setColor(Color.RED);
				canvas.drawText("游戏结束!!!", canvas.getWidth() / 2,
						(canvas.getHeight() / 2) - (ball.getBounds().height())
								- 50, getReadyPaint);
			}
			//提示请准备。。。
			getReadyPaint.setColor(Color.WHITE);
			canvas.drawText(getReady, canvas.getWidth() / 2,
					(canvas.getHeight() / 2) - (ball.getBounds().height()),
					getReadyPaint);

		}
	}

	/**
	 * 重置变量预示着新的游戏。删除剩余的砖块列表。当运行看到砖块列表是空的，它会启动新的游戏板
	 * 
	 * @param canvas
	 *            graphics canvas
	 * */
	private void gameOver(Canvas canvas) {
		levelCompleted = 0;
		points = 0;
		playerTurns = PLAYER_TURNS_NUM;
		blocksList.clear();
	}

	/**
	 * 初始化图形对象。恢复比赛状态，如果现有的游戏继续
	 * 
	 * @param canvas
	 *            graphical canvas
	 * */
	private void initObjects(Canvas canvas) {
		touched = false; // reset paddle location
		ball.initCoords(canvas.getWidth(), canvas.getHeight());
		paddle.initCoords(canvas.getWidth(), canvas.getHeight());
		if (startNewGame == 0) {
			restoreGameData();
		} else {
			initBlocks(canvas);
		}
	}

	/**
	 * 恢复砖块保存的ArrayList。通过读取ArrayList整数数组。传递值砖块，并增加了砖块到一个ArrayList。
	 * 
	 * 
	 * @param arr
	 *            ArrayList of integer arrays containing the coordinates and
	 *            color of the saved blocks.
	 * */
	private void restoreBlocks(ArrayList<int[]> arr) {
		for (int i = 0; i < arr.size(); i++) {
			Rect r = new Rect();
			int[] blockNums = arr.get(i);
			r.set(blockNums[0], blockNums[1], blockNums[2], blockNums[3]);
			Block b = new Block(r, blockNums[4]);
			blocksList.add(b);
		}
	}

	/**
	 * 打开一个保存的游戏文件并读取数据恢复保存游戏状态。
	 * */
	private void restoreGameData() {
		try {
			FileInputStream fis = new FileInputStream(FILE_PATH);
			ObjectInputStream ois = new ObjectInputStream(fis);
			points = ois.readInt(); // restore player points
			playerTurns = ois.readInt(); // restore player turns
			@SuppressWarnings("unchecked")
			ArrayList<int[]> arr = (ArrayList<int[]>) ois.readObject();
			restoreBlocks(arr); // restore blocks
			ois.close();
			fis.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (StreamCorruptedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		startNewGame = 1; // only restore once
	}

	/**
	 * 初始化块。canvas的宽度和高度尺寸和砖块的坐标。设置颜色取决于砖块的行。添加砖块到一个ArrayList。
	 *
	 * 
	 * @param canvas
	 *            graphics canvas
	 * */
	private void initBlocks(Canvas canvas) {
		int blockHeight = canvas.getWidth() / 18;
		int spacing = canvas.getWidth() / 144;
		int topOffset = canvas.getHeight() / 10;
		int blockWidth = (canvas.getWidth() / 10) ;

		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				int y_coordinate = (i * (blockHeight )) + topOffset;
				int x_coordinate = j * (blockWidth );

				Rect r = new Rect();
				r.set(x_coordinate, y_coordinate, x_coordinate + blockWidth,
						y_coordinate + blockHeight);

				int color;

				if (i < 2)
					color = Color.RED;
				else if (i < 4)
					color = Color.YELLOW;
				else if (i < 6)
					color = Color.GREEN;
				else if (i < 8)
					color = Color.MAGENTA;
				else
					color = Color.LTGRAY;

				Block block = new Block(r, color);

				blocksList.add(block);
			}
		}
	}

	/**
	 * 画砖块
	 * 
	 * @param canvas
	 *            graphical canvas
	 * */
	private void drawBlocks(Canvas canvas) {
		for (int i = 0; i < blocksList.size(); i++) {
			blocksList.get(i).drawBlock(canvas);
		}
	}

	/**
	 * 保存游戏状态。读取的砖块色和坐标转换成一个ArrayList。保存砖块，玩家分数，玩家生命数变成一个数据文件。
	 * */
	private void saveGameData() {
		ArrayList<int[]> arr = new ArrayList<int[]>();

		for (int i = 0; i < blocksList.size(); i++) {
			arr.add(blocksList.get(i).toIntArray());
		}

		try {
			FileOutputStream fos = new FileOutputStream(FILE_PATH);
			oos = new ObjectOutputStream(fos);
			oos.writeInt(points);
			oos.writeInt(playerTurns);
			oos.writeObject(arr);
			oos.close();
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 保存游戏数据，并且摧毁线程
	 * Saves game data and destroys Thread.
	 * */
	public void pause() {
		saveGameData();
		running = false;
		while (true) {
			try {
				gameThread.join();
				break;
			} catch (InterruptedException e) {
				e.printStackTrace();
				break;
			}
		}
		gameThread = null;
		ball.close();
	}

	/**
	 * Resumes the game. Starts a new game Thread.
	 * 开始游戏线程
	 * */
	public void resume() {
		running = true;
		gameThread = new Thread(this);
		gameThread.start();
	}

	/**
	 * 重写触摸事件监听器。通过触控移动挡板。
	 * {@inheritDoc}
	 * 
	 * @param event
	 *            screen touch event
	 * 
	 * @return true
	 * */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN
				|| event.getAction() == MotionEvent.ACTION_MOVE) {
			eventX = event.getX();
			touched = true;
		}
		return touched;
	}
	 private static Bitmap readBitmap(Context paramContext, String paramString)
	  {
	    int i = paramContext.getResources().getIdentifier(paramString, "drawable", paramContext.getPackageName());
	    return BitmapFactory.decodeResource(paramContext.getResources(), i);
	  }
}

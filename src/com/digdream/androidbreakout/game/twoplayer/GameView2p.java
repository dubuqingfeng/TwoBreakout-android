package com.digdream.androidbreakout.game.twoplayer;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.util.ArrayList;

import org.json.JSONException;

import com.digdream.androidbreakout.R;
import com.digdream.androidbreakout.module.GameMessages;
import com.digdream.androidbreakout.module.GameMessages.AbstractGameMessage;
import com.digdream.androidbreakout.module.GameMessages.GameBallDataMessage;
import com.digdream.androidbreakout.module.GameMessages.GameDataMessage;
import com.digdream.androidbreakout.module.GameMessages.GameLevelMessage;
import com.lenovo.game.GameMessage;
import com.lenovo.game.GameMessageListener;
import com.lenovo.game.GameUserInfo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * 创建并绘制图形的游戏。运行一个线程用于动画和游戏物理。保存并暂停或恢复时恢复游戏数据。
 * 
 */
public class GameView2p extends SurfaceView implements Runnable {
	private int stage;
	private static final String TAG = "2p的游戏界面GameView2p extends SurfaceView implements Runnable";
	private boolean showGameOverBanner = false;
	private int levelCompleted = 0;
	private int playerTurns;
	private int PLAYER_TURNS_NUM = 3;
	private Paint turnsPaint;
	private String playerTurnsText = "生命数  = ";
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
	private Ball ball;
	private Paddle paddle;
	private ArrayList<Block> blocksList;
	private String getReady = "请准备...";
	private Paint getReadyPaint;
	private int points = 0;
	private Paint scorePaint;
	private String score = "总分  = ";

	public static boolean mIsInviter = true;
	private BitmapDrawable bitmapDrawable;
	private Bitmap bitmap;
	private TopPaddle toppaddle;
	// private Canvas canvas2p;
	private Ball2p ball2;
	private Bitmap blockbmp;
	private Matrix matrix;
	private Bitmap blockbmpfixed;
	private boolean single = true;
	private float eventY;

	private TwoStageData stagedata;
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
	public GameView2p(Context context, int launchNewGame, boolean sound) {
		super(context);
		stage = 1;
		//stage = StageActivity.stage;
		stagedata = new TwoStageData(stage);
		startNewGame = launchNewGame; // new game or continue
		playerTurns = PLAYER_TURNS_NUM;
		soundToggle = sound;
		holder = getHolder();
		ball = new Ball(this.getContext(), soundToggle);
		//ball2 = new Ball2p(this.getContext(), soundToggle);
		paddle = new Paddle();
		toppaddle = new TopPaddle();
		blocksList = new ArrayList<Block>();
		//设置背景
		// 设置背景
		switch (stage) {
		case 1:
			setBackgroundResource(R.drawable.chara1);
			break;
		case 2:
			setBackgroundResource(R.drawable.chara2);
			break;
		case 3:
			setBackgroundResource(R.drawable.chara3);
			break;
		case 4:
			setBackgroundResource(R.drawable.chara4);
			break;
		case 5:
			setBackgroundResource(R.drawable.chara5);
			break;
		case 6:
			setBackgroundResource(R.drawable.chara6);
			break;
		}		
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
		this.blockbmp = readBitmap(context, "chara" + this.stage + "_block");
		this.matrix = new Matrix();
	}
	/**
	 * 读取位图
	 * 
	 * @param paramContext
	 *            Android Context
	 * @param paramString
	 *            path
	 * @return bitmap
	 */

	private static Bitmap readBitmap(Context paramContext, String paramString) {
		int i = paramContext.getResources().getIdentifier(paramString,
				"drawable", paramContext.getPackageName());
		return BitmapFactory.decodeResource(paramContext.getResources(), i);
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
					if (mIsInviter) {
						paddle.movePaddle((int) eventX);
					} else {
						toppaddle.movePaddle((int) eventX);
					}
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
		toppaddle.drawPaddle(canvas);
		ball.drawBall(canvas);
		//ball2.drawBall(canvas);
	}

	/**
	 *暂停动画，直到等待计数器被满足。设置速度和球坐标。检查碰撞。如果检查游戏就结束了。 绘制文本来提醒用户，如果球重新启动或游戏就结束了。.
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
				// 发送失败的message
				Breakout2p.sendGameOverMessage(eventX);
				return;
			}
			// paddle collision
			ball.checkPaddleCollision(paddle);
			ball.checkTopPaddleCollision(toppaddle);
			// block collision and points tally
			points += ball.checkBlocksCollision(blocksList);
		}

		else {
			if (showGameOverBanner) {
				getReadyPaint.setColor(Color.RED);
				canvas.drawText("游戏结束!!!", canvas.getWidth() / 2,
						(canvas.getHeight() / 2) - (ball.getBounds().height())
								- 50, getReadyPaint);
				// 发送游戏结束的message

			}
			getReadyPaint.setColor(Color.WHITE);
			canvas.drawText(getReady, canvas.getWidth() / 2,
					(canvas.getHeight() / 2) - (ball.getBounds().height()),
					getReadyPaint);
			// 发送游戏结束的message

		}
	}

	/**
	 *重置变量预示着新的游戏。删除剩余的砖块列表。当运行看到砖块列表是空的，它会启动新的游戏
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
	 * 初始化图形对象。恢复状态，如果现有的游戏继续
	 * 
	 * @param canvas
	 *            graphical canvas
	 * */
	private void initObjects(Canvas canvas) {
		float w = canvas.getWidth();
		int width = this.blockbmp.getWidth();
		int height = this.blockbmp.getHeight();
		float h = width * height / w;
		this.matrix.postScale(w / width, h / height);
		blockbmpfixed = Bitmap.createBitmap(blockbmp, 0, 0, width, height,matrix,true);
		touched = false; // reset paddle location
		ball.initCoords(canvas.getWidth(), canvas.getHeight());
		paddle.initCoords(canvas.getWidth(), canvas.getHeight());
		toppaddle.initCoords(canvas.getWidth(), canvas.getHeight());
		/*if (startNewGame == 0) {
			restoreGameData();
		} else {*/
			initBlocks(canvas);
		//}
	}

	/**
	 * 恢复砖块保存的ArrayList。通过读取ArrayList整数数组。传递值砖块，并增加了砖块到一个ArrayList。
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
	 * @param canvas
	 *            graphics canvas
	 * */
	private void initBlocks(Canvas canvas) {
		int blockHeight = (((480 * 720 ) / canvas.getWidth() ) *16)/360;
		int spacing = canvas.getWidth() / 144;
		int topOffset = canvas.getHeight() / 10;
		int blockWidth = (canvas.getWidth() / 10);
		// 获得图片
		//bitmapDrawable = (BitmapDrawable) getResources().getDrawable(
			//	R.drawable.item1);
		// 设置显示大小
		//bitmapDrawable.setBounds(0, 0, (canvas.getWidth() / 10),
				//canvas.getWidth() / 18);
		//bitmap = (bitmapDrawable).getBitmap();
		/**
		 * 这里需要读取StageData.java的数据，读取关卡的数据 控制二维数组。 根据stage值。
		 * 读取关卡数据，值为2时需打两次。。。
		 */
		
		for (int i = 0; i < 20; i++) {
			for (int j = 0; j < 10; j++) {
				int y_coordinate = (i * (blockHeight)) + topOffset;
				int x_coordinate = j * (blockWidth);
				if (TwoStageData.GameDataArray[i][j] == 1) {
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
					
					Rect localRect1 = new Rect(blockWidth * (j % 10), blockHeight * (i % 10),
							blockWidth + blockWidth * (j % 10), blockHeight + blockHeight * (i % 10));
					Rect localRect2 = new Rect(blockWidth * (j % 10), blockHeight * (i % 10),
							blockWidth + blockWidth * (j % 10), blockHeight + blockHeight * (i % 10));
					//canvas.drawBitmap(this.blockbmp, localRect1, localRect2,
						//	null);
					Block block = new Block(localRect1, localRect2, blockbmpfixed,color);
					blocksList.add(block);
				}
				if(TwoStageData.GameDataArray[i][j] == 2) {
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
					Rect localRect1 = new Rect(blockWidth * (j % 10), blockHeight * (i % 10),
							blockWidth + blockWidth * (j % 10), blockHeight + blockHeight * (i % 10));
					Rect localRect2 = new Rect(blockWidth * (j % 10), blockHeight * (i % 10),
							blockWidth + blockWidth * (j % 10), blockHeight + blockHeight * (i % 10));
					//canvas.drawBitmap(this.blockbmp, localRect1, localRect2,
						//	null);
					Block block = new Block(localRect1, localRect2, blockbmpfixed,color,2);
					blocksList.add(block);
				}
				// Rect r = new Rect();
				// r.set(x_coordinate, y_coordinate, x_coordinate + blockWidth,
				// y_coordinate + blockHeight);
				
			//	Block block = new Block(color, bitmap, x_coordinate,
		//				y_coordinate);
			//	blocksList.add(block);
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
	 * 每碰撞一次砖块，传输message？
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
	 *保存游戏数据，并且摧毁线程 Saves game data and destroys Thread.
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
		ball2.close();
	}

	/**
	 * Resumes the game. Starts a new game Thread. 开始游戏线程
	 * */
	public void resume() {
		running = true;
		gameThread = new Thread(this);
		gameThread.start();
	}

	/**
	 * 重写触摸事件监听器。通过触控移动挡板。
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
			Breakout2p.sendMessage(event.getX());
			touched = true;
		}
		return touched;
	}

	GameMessageListener mMessageListener = new GameMessageListener() {
		private float gameData;
		private int[] gameballData;

		public void onMessage(GameMessage gameMessage) {
			Log.v(TAG, "onMessage, message : " + gameMessage.toString());
			AbstractGameMessage msg;
			try {
				msg = GameMessages.createGameMessage(gameMessage.getType(),
						gameMessage.getMessage());
				msg.setFrom(gameMessage.getFrom());
				msg.setTo(gameMessage.getTo());
			} catch (JSONException e) {
				Log.d(TAG, "json error!");
				return;
			}
			if (msg.getType().equalsIgnoreCase(GameMessages.MSG_TYPE_GAME_DATA)) {
				GameDataMessage dataMsg = (GameDataMessage) msg;
				gameData = dataMsg.getGameData();
				if (!mIsInviter) {
					// 这里不是邀请者
					paddle.movePaddle((int) gameData);
				} else {
					// 这里是邀请者
					toppaddle.movePaddle((int) gameData);
				}
				// mGameData.generateGameData(gameData);
			} else if (msg.getType().equalsIgnoreCase(
					GameMessages.MSG_TYPE_GAME_PREPARED)) {

			} else if (msg.getType().equalsIgnoreCase(
					GameMessages.MSG_TYPE_GAME_END)) {
				gameOver(canvas);
				getReadyPaint.setColor(Color.RED);
				canvas.drawText("对方游戏结束!!!", canvas.getWidth() / 2,
						(canvas.getHeight() / 2) - (ball.getBounds().height())
								- 50, getReadyPaint);
				//提示失败
			} else if (msg.getType().equalsIgnoreCase(
					GameMessages.MSG_TYPE_GAME_BEGIN)) {
				if (!mIsInviter) {
					// 这里不是邀请者
				} else {
					// 这里是邀请者

				}
			} else if (msg.getType().equalsIgnoreCase(
					GameMessages.MSG_TYPE_GAME_BALL_DATA)) {
				GameBallDataMessage dataMsg = (GameBallDataMessage) msg;
				gameballData = dataMsg.getGameData();
				if (!mIsInviter) {
					// 这里不是邀请者
					ball2.setBounds(gameballData[0], gameballData[1],
							gameballData[2], gameballData[3]);
				} else {
					// 这里是邀请者
					ball2.setBounds(gameballData[0], gameballData[1],
							gameballData[2], gameballData[3]);
				}
			}
		}
	};
}

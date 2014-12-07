package com.digdream.androidbreakout.game.onetwoplayer;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.util.ArrayList;

import com.digdream.androidbreakout.R;
import com.digdream.androidbreakout.game.oneplayer.StageData;
import com.digdream.androidbreakout.ui.StageActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.Rect;
import android.util.Log;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * ����������ͼ�ε���Ϸ������һ���߳����ڶ�������Ϸ�������沢��ͣ��ָ�ʱ�ָ���Ϸ���ݡ�
 * 
 */
public class GameView2p extends SurfaceView implements Runnable {
	private int stage;
	private static final String TAG = "2p����Ϸ����GameView2p extends SurfaceView implements Runnable";
	private boolean showGameOverBanner = false;
	private int levelCompleted = 0;
	private int playerTurns;
	private int PLAYER_TURNS_NUM = 3;
	private Paint turnsPaint;
	private String playerTurnsText = "������  = ";
	private boolean soundToggle;
	private int startNewGame;
	private ObjectOutputStream oos;
	private final String FILE_PATH = "data/data/com.digdream.androidbreakout/data.dat";
	private final int frameRate = 33;
	private final int startTimer = 66;
	private boolean touched = false;
	private float eventX;
	private float topeventX;
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
	private String getReady = "��׼��...";
	private Paint getReadyPaint;
	private int points = 0;
	private Paint scorePaint;
	private String score = "�ܷ�  = ";

	public static boolean mIsInviter = true;
	private BitmapDrawable bitmapDrawable;
	private Bitmap bitmap;
	private TopPaddle toppaddle;
	// private Canvas canvas2p;
	// ��㴥��
	private SparseArray<PointF> mActivePointers;
	private StageData stagemap;
	private Bitmap blockbmp;

	/**
	 * ���캯��������������״̬���µ���Ϸ�źţ����ݴ�breakout�ഫ��intent��ʵ����ש��͵��塣������paint���������ı�����Ļ�ϡ�
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
		startNewGame = launchNewGame; // new game or continue
		playerTurns = PLAYER_TURNS_NUM;
		soundToggle = sound;
		holder = getHolder();
		ball = new Ball(this.getContext(), soundToggle);
		// ball2 = new Ball2p(this.getContext(), soundToggle);
		paddle = new Paddle();
		toppaddle = new TopPaddle();
		blocksList = new ArrayList<Block>();
		// ���ñ���
		switch(stage){
			case 1:setBackgroundResource(R.drawable.chara1);break;
			case 2:setBackgroundResource(R.drawable.chara2);break;
			case 3:setBackgroundResource(R.drawable.chara3);break;
			case 4:setBackgroundResource(R.drawable.chara4);break;
			case 5:setBackgroundResource(R.drawable.chara5);break;
			case 6:setBackgroundResource(R.drawable.chara6);break;
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
		// ��㴥��
		mActivePointers = new SparseArray<PointF>();
		this.blockbmp = readBitmap(context, "chara" + this.stage + "_block");
	}

	/**
	 * ������Ϸ�̡߳�����֡����������ͼ�Ρ�ʹ�û������ơ���������ڵ�ש�飬��ʼ����Ϸ�����ƶ����ݴ����¼��ĵ��塣
	 * �����ײ������ƶ���������ҽ���������ʱ���ܳ�������������ң����ȼ����һ������Ļغϡ������ı�
	 * ��Ļ��ʾ��ҵķ�����ʣ�������������ı�����������ʼ�����ʱ��
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
				// canvas.drawColor(Color.BLACK);
				// ���ͼƬ
				bitmapDrawable = (BitmapDrawable) getResources().getDrawable(
						R.drawable.chara1);
				// ������ʾ��С
				bitmapDrawable.setBounds(0, 0, 80, 80);
				bitmap = (bitmapDrawable).getBitmap();
				// ����ͼƬ
				canvas.drawBitmap(bitmap, 50, 50, null);
				// canvas.drawColor(Color.BLACK);
				canvas.drawColor(Color.TRANSPARENT, Mode.CLEAR);// ����Ļ.
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
					//
					for (int size = mActivePointers.size(), i = 0; i < size; i++) {
						PointF point = mActivePointers.valueAt(i);
						if (point != null) {
							if (point.y >= canvas.getHeight() / 2)
								// ����������������Ļһ���ƶ����浲�塣
								paddle.movePaddle((int) point.x);
							else {
								toppaddle.movePaddle((int) point.x);
							}
						}
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
	 * ��ͼ�����Ļ
	 * 
	 * @param canvas
	 *            graphics canvas
	 * */
	private void drawToCanvas(Canvas canvas) {
		// canvas.drawColor(Color.TRANSPARENT, Mode.CLEAR);
		drawBlocks(canvas);
		paddle.drawPaddle(canvas);
		toppaddle.drawPaddle(canvas);
		ball.drawBall(canvas);
		// ball2.drawBall(canvas);
	}

	/**
	 * ��ͣ������ֱ���ȴ������������㡣�����ٶȺ������ꡣ�����ײ����������Ϸ�ͽ����ˡ� �����ı��������û��������������������Ϸ�ͽ����ˡ�.
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
				// ����ʧ�ܵ�message
				return;
			}
			// paddle collision
			ball.checkPaddleCollision(paddle);
			ball.checkTopPaddleCollision(toppaddle);
			points += ball.checkBlocksCollision(blocksList);
		}

		else {
			if (showGameOverBanner) {
				getReadyPaint.setColor(Color.RED);
				canvas.drawText("��Ϸ����!!!", canvas.getWidth() / 2,
						(canvas.getHeight() / 2) - (ball.getBounds().height())
								- 50, getReadyPaint);
				// ������Ϸ������message

			}
			getReadyPaint.setColor(Color.WHITE);
			canvas.drawText(getReady, canvas.getWidth() / 2,
					(canvas.getHeight() / 2) - (ball.getBounds().height()),
					getReadyPaint);
			// ������Ϸ������message

		}
	}

	/**
	 * ���ñ���Ԥʾ���µ���Ϸ��ɾ��ʣ���ש���б������п���ש���б��ǿյģ����������µ���Ϸ
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
	 * ��ʼ��ͼ�ζ��󡣻ָ�״̬��������е���Ϸ����
	 * 
	 * @param canvas
	 *            graphical canvas
	 * */
	private void initObjects(Canvas canvas) {
		touched = false; // reset paddle location
		ball.initCoords(canvas.getWidth(), canvas.getHeight());
		paddle.initCoords(canvas.getWidth(), canvas.getHeight());
		toppaddle.initCoords(canvas.getWidth(), canvas.getHeight());
		if (startNewGame == 0) {
			restoreGameData();
		} else {
			initBlocks(canvas);
		}
	}

	/**
	 * �ָ�ש�鱣���ArrayList��ͨ����ȡArrayList�������顣����ֵש�飬��������ש�鵽һ��ArrayList��
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
	 * ��һ���������Ϸ�ļ�����ȡ���ݻָ�������Ϸ״̬��
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
	 * ��ʼ���顣canvas�Ŀ�Ⱥ͸߶ȳߴ��ש������ꡣ������ɫȡ����ש����С����ש�鵽һ��ArrayList��
	 * 
	 *
	 * @param canvas
	 *            graphics canvas
	 * */
	private void initBlocks(Canvas canvas) {
		int blockHeight = canvas.getWidth() / 18;
		int spacing = canvas.getWidth() / 144;
		int topOffset = canvas.getHeight() / 10;
		int blockWidth = (canvas.getWidth() / 10);

		// ���ͼƬ
		bitmapDrawable = (BitmapDrawable) getResources().getDrawable(
				R.drawable.item1);
		// ������ʾ��С
		bitmapDrawable.setBounds(0, 0, (canvas.getWidth() / 10),
				canvas.getWidth() / 18);
		bitmap = (bitmapDrawable).getBitmap();
		/**
		 * ������Ҫ��ȡStageData.java�����ݣ���ȡ�ؿ������� ���ƶ�ά���顣 ����stageֵ��
		 * ��ȡ�ؿ����ݣ�ֵΪ2ʱ������Ρ�����
		 */
		
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				int y_coordinate = (i * (blockHeight)) + topOffset;
				int x_coordinate = j * (blockWidth);
				//if (StageData.GameDataArray[i][j] == 1) {
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
					Rect localRect1 = new Rect(48 * (j % 10), 16 * (i % 10),
							48 + 48 * (j % 10), 16 + 16 * (i % 10));
					Rect localRect2 = new Rect(48 * (j % 10), 16 * (i % 10),
							48 + 48 * (j % 10), 16 + 16 * (i % 10));
					//canvas.drawBitmap(this.blockbmp, localRect1, localRect2,
						//	null);
					Block block = new Block(localRect1, localRect2, blockbmp,color);
					blocksList.add(block);
				/*}
				if(StageData.GameDataArray[i][j] == 2) {
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
					Rect localRect1 = new Rect(48 * (j % 10), 16 * (i % 10),
							48 + 48 * (j % 10), 16 + 16 * (i % 10));
					Rect localRect2 = new Rect(48 * (j % 10), 16 * (i % 10),
							48 + 48 * (j % 10), 16 + 16 * (i % 10));
					//canvas.drawBitmap(this.blockbmp, localRect1, localRect2,
						//	null);
					Block block = new Block(localRect1, localRect2, blockbmp,color,2);
					blocksList.add(block);
				}*/
				
			}
		}
	}

	/**
	 * ��ש��
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
	 * ������Ϸ״̬����ȡ��ש��ɫ������ת����һ��ArrayList������ש�飬��ҷ�����������������һ�������ļ���
	 * ÿ��ײһ��ש�飬����message��
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
	 * ������Ϸ���ݣ����Ҵݻ��߳� Saves game data and destroys Thread.
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
	 * Resumes the game. Starts a new game Thread. ��ʼ��Ϸ�߳�
	 * */
	public void resume() {
		running = true;
		gameThread = new Thread(this);
		gameThread.start();
	}

	/**
	 * ��д�����¼���������ͨ�������ƶ����塣 ��㴥�ء�����
	 * 
	 * @param event
	 *            screen touch event
	 * 
	 * @return true
	 * */
	/*
	 * @Override public boolean onTouchEvent(MotionEvent event) { if
	 * (event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() ==
	 * MotionEvent.ACTION_MOVE) { eventX = event.getX(); touched = true; }
	 * return touched; }
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {

		// get pointer index from the event object
		int pointerIndex = event.getActionIndex();

		// get pointer ID
		int pointerId = event.getPointerId(pointerIndex);

		// get masked (not specific to a pointer) action
		int maskedAction = event.getActionMasked();

		switch (maskedAction) {

		case MotionEvent.ACTION_DOWN:
			Log.v(TAG,"action_down");
			break;
		case MotionEvent.ACTION_POINTER_DOWN: {
			// We have a new pointer. Lets add it to the list of pointers
			PointF f = new PointF();
			f.x = event.getX(pointerIndex);
			f.y = event.getY(pointerIndex);
			mActivePointers.put(pointerId, f);
			Log.v(TAG,"action_pointer_down");
			break;
		}
		case MotionEvent.ACTION_MOVE: { // a pointer was moved
			for (int size = event.getPointerCount(), i = 0; i < size; i++) {
				PointF point = mActivePointers.get(event.getPointerId(i));
				if (point != null) {
					point.x = event.getX(i);
					// eventX = event.getX(i);
					point.y = event.getY(i);
					touched = true;
				}

			}
			// eventX = event.getX();
			Log.v(TAG,"action_move");
			break;
		}
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_POINTER_UP:
		case MotionEvent.ACTION_CANCEL: {
			mActivePointers.remove(pointerId);
			break;
		}
		}
		invalidate();

		return touched;
	}
	/**
	 * ��ȡλͼ
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

}

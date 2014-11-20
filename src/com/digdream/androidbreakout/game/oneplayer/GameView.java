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
 * ����������ͼ�ε���Ϸ������һ���߳����ڶ�������Ϸ�������沢��ͣ��ָ�ʱ�ָ���Ϸ���ݡ�
 * 
 */
public class GameView extends SurfaceView implements Runnable {

	private boolean showGameOverBanner = false;
	private int levelCompleted = 0;
	private int playerTurns;
	private int PLAYER_TURNS_NUM = 3;
	private Paint turnsPaint;
	private String playerTurnsText = "������ = ";
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
	//ballӦ��Ϊ����
	private Ball ball;
	private Paddle paddle;
	private ArrayList<Block> blocksList;
	private String getReady = "��׼�� ...";
	private Paint getReadyPaint;
	private int points = 0;
	private Paint scorePaint;
	private String score = "�ܷ�  = ";
	private BitmapDrawable bitmapDrawable;
	private Bitmap bitmap;

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
	public GameView(Context context, int launchNewGame, boolean sound) {
		super(context);
		startNewGame = launchNewGame; // new game or continue
		playerTurns = PLAYER_TURNS_NUM;
		soundToggle = sound;
		holder = getHolder();
		//��ʼ��ball��
		ball = new Ball(this.getContext(), soundToggle);
		paddle = new Paddle();
		blocksList = new ArrayList<Block>();
		//���ñ���
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
				//���ͼƬ
				bitmapDrawable=(BitmapDrawable)getResources().getDrawable(R.drawable.chara1);
				//������ʾ��С
				bitmapDrawable.setBounds(0, 0, 80, 80);
				bitmap=(bitmapDrawable).getBitmap();
				//����ͼƬ
				canvas.drawBitmap(bitmap, 50, 50, null);
				//canvas.drawColor(Color.BLACK);
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
	 * ��ͼ�����Ļ
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
	 * ��ͣ������ֱ���ȴ������������㡣�����ٶȺ������ꡣ�����ײ����������Ϸ�ͽ����ˡ�
	 * �����ı��������û��������������������Ϸ�ͽ����ˡ�...
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
				//��ת�����activity
				getReadyPaint.setColor(Color.RED);
				canvas.drawText("��Ϸ����!!!", canvas.getWidth() / 2,
						(canvas.getHeight() / 2) - (ball.getBounds().height())
								- 50, getReadyPaint);
			}
			//��ʾ��׼��������
			getReadyPaint.setColor(Color.WHITE);
			canvas.drawText(getReady, canvas.getWidth() / 2,
					(canvas.getHeight() / 2) - (ball.getBounds().height()),
					getReadyPaint);

		}
	}

	/**
	 * ���ñ���Ԥʾ���µ���Ϸ��ɾ��ʣ���ש���б������п���ש���б��ǿյģ����������µ���Ϸ��
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
	 * ��ʼ��ͼ�ζ��󡣻ָ�����״̬��������е���Ϸ����
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
	 * �ָ�ש�鱣���ArrayList��ͨ����ȡArrayList�������顣����ֵש�飬��������ש�鵽һ��ArrayList��
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
	 * ������Ϸ���ݣ����Ҵݻ��߳�
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
	 * ��ʼ��Ϸ�߳�
	 * */
	public void resume() {
		running = true;
		gameThread = new Thread(this);
		gameThread.start();
	}

	/**
	 * ��д�����¼���������ͨ�������ƶ����塣
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

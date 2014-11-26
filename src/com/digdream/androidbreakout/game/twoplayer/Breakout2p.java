package com.digdream.androidbreakout.game.twoplayer;

import com.digdream.androidbreakout.module.GameMessages.GameBallDataMessage;
import com.digdream.androidbreakout.module.GameMessages.GameDataMessage;
import com.digdream.androidbreakout.module.GameMessages.GameEndMessage;
import com.digdream.androidbreakout.module.GameMessages.GameLevelMessage;
import com.digdream.androidbreakout.ui.BaseActivity;
import com.lenovo.game.GameMessage;
import com.lenovo.game.GameUserInfo;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.media.AudioManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

/**
 * activity������Ϸ��ӵ����Ϸ�е�ͼ���̡߳����沢��ͣ��ָ�ʱ�ָ���Ϸ���ݡ�
 * 
 */
public class Breakout2p extends BaseActivity {

	private boolean sound;
	private GameView2p gameView;
	private static final String TAG = "Breakout2p";

	/**
	 * activity �����������ý����������ơ����ر�����������ȫ�����ڡ�ͨ��intent������ֵ/������Ϸ��ͨ������Ϸֵ����Ϸ�е��̷߳��ź��Ƿ�ʼ����Ϸ����������е���Ϸ
	 * 
	 * @param savedInstanceState
	 *            saved data from a previous run of this Activity
	 * 
	 *            {@inheritDoc}
	 * */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setVolumeControlStream(AudioManager.STREAM_MUSIC);
		Intent intent = getIntent();
		int newGame = intent.getIntExtra("NEW_GAME", 1);
		sound = intent.getBooleanExtra("SOUND_ON_OFF", true);

		// fullscreen
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		// initialize graphics and game thread
		gameView = new GameView2p(this, newGame, sound);
		gameView.setZOrderOnTop(true);
		gameView.getHolder().setFormat(PixelFormat.TRANSLUCENT);
		mGameShare.addMessageListener(gameView.mMessageListener);
		setContentView(gameView);
	}

	@Override
	public void onBind(boolean success) {
		Log.v(TAG, "onBind(), success : " + success);
		if (!success) {
			Toast.makeText(getApplicationContext(), "Bind Service failed.",
					Toast.LENGTH_LONG).show();
			return;
		}
		// mGameShare.addMessageListener(gameView.mMessageListener);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mGameShare != null)
			mGameShare.removeMessageListener(gameView.mMessageListener);
	}

	/**
	 * ��ϵͳ��ͣ�˻ʱ���á�������Ϸ���ݣ���ֹͣ
	 * the game's thread from running.
	 * 
	 * {@inheritDoc}
	 * */
	@Override
	protected void onPause() {
		super.onPause();
		gameView.pause();
	}

	/**
	 *  ϵͳ�ָ���һ�ʱ���á��ָ���Ϸ���ݺ�������Ϸ�̡߳�
	 * 
	 * {@inheritDoc}
	 * */
	@Override
	protected void onResume() {
		super.onResume();
		gameView.resume();
	}

	public static void sendMessage(float eventX) {
		// ����message
		GameUserInfo localUser = mGameShare.getLocalUser();
		GameUserInfo remoteUser = getRemoteUser();
		if (localUser == null || remoteUser == null)
			return;
		GameDataMessage dataMsg = new GameDataMessage(localUser.id,
				remoteUser.id, eventX);
		GameMessage msg = dataMsg.toGameMessage();
		if (msg == null)
			return;
		mGameShare.sendMessage(msg);
	}

	public static void sendGameBallDataMessage(int[] data) {
		// ����message
		GameUserInfo localUser = mGameShare.getLocalUser();
		GameUserInfo remoteUser = getRemoteUser();
		if (localUser == null || remoteUser == null)
			return;
		GameBallDataMessage dataMsg = new GameBallDataMessage(localUser.id,
				remoteUser.id, data);
		GameMessage msg = dataMsg.toGameMessage();
		if (msg == null)
			return;
		mGameShare.sendMessage(msg);
	}

	// ������Ϸ������message
	public static void sendGameOverMessage(float eventX) {
		// ����message
		GameUserInfo localUser = mGameShare.getLocalUser();
		GameUserInfo remoteUser = getRemoteUser();
		if (localUser == null || remoteUser == null)
			return;
		GameEndMessage dataMsg = new GameEndMessage(localUser.id,
				remoteUser.id, eventX);
		GameMessage msg = dataMsg.toGameMessage();
		if (msg == null)
			return;
		mGameShare.sendMessage(msg);
	}
}

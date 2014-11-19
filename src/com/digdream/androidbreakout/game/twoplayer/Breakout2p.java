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
 * Activity for the running game. Holds the game's graphics thread. Saves and
 * restores game data when paused or resumed.
 * 
 */
public class Breakout2p extends BaseActivity {

	private boolean sound;
	private GameView2p gameView;
	private static final String TAG = "Breakout2p";

	/**
	 * Activity constructor. Acquires media volume control. Hides the titlebar
	 * and requests a fullscreen window. Receives an intent from Splash. Reads
	 * intent values for sound state and new/continue game. Passes the new game
	 * value to the game's thread which signals whether to start a new game or
	 * continue and existing game.
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
	 * Called when the system pauses this Activity. Saves game data and stops
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
	 * Called when the system resumes this Activity. Restores game data and runs
	 * game thread.
	 * 
	 * {@inheritDoc}
	 * */
	@Override
	protected void onResume() {
		super.onResume();
		gameView.resume();
	}

	public static void sendMessage(float eventX) {
		// 发送message
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
		// 发送message
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

	// 发送游戏结束的message
	public static void sendGameOverMessage(float eventX) {
		// 发送message
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

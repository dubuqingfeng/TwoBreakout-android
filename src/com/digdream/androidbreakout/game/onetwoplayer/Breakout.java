package com.digdream.androidbreakout.game.onetwoplayer;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

/**
 * activity运行游戏。拥有游戏中的图形线程。保存并暂停或恢复时恢复游戏数据。
 * 这是非茄子sdk的双人模式。
 * 
 */
public class Breakout extends Activity {

	private boolean sound;
	private GameView gameView;

	/**
	 * activity 构造器。获得媒体的音量控制。隐藏标题栏并请求全屏窗口。通过intent传来的值/继续游戏。通过新游戏值到游戏中的线程发信号是否开始新游戏或继续与现有的游戏。
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
		int stage = intent.getIntExtra("stage", 1);
		sound = intent.getBooleanExtra("SOUND_ON_OFF", true);

		// fullscreen
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		// initialize graphics and game thread
		gameView = new GameView(this, newGame, sound);
		gameView.setZOrderOnTop(true);
		gameView.getHolder().setFormat(PixelFormat.TRANSLUCENT);
		setContentView(gameView);
	}

	/**
	 * 当系统暂停此活动时调用。保存游戏数据，并停止
	 * the game's thread from running.
	 * 
	 */
	@Override
	protected void onPause() {
		super.onPause();
		gameView.pause();
	}

	/**
	 * 系统恢复这一活动时调用。恢复游戏数据和运行游戏线程。
	 * 
	 * */
	@Override
	protected void onResume() {
		super.onResume();
		gameView.resume();
	}
}

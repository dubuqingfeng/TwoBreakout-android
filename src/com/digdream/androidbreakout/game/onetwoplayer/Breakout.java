package com.digdream.androidbreakout.game.onetwoplayer;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

/**
 * activity������Ϸ��ӵ����Ϸ�е�ͼ���̡߳����沢��ͣ��ָ�ʱ�ָ���Ϸ���ݡ�
 * ���Ƿ�����sdk��˫��ģʽ��
 * 
 */
public class Breakout extends Activity {

	private boolean sound;
	private GameView gameView;

	/**
	 * activity �����������ý����������ơ����ر�����������ȫ�����ڡ�ͨ��intent������ֵ/������Ϸ��ͨ������Ϸֵ����Ϸ�е��̷߳��ź��Ƿ�ʼ����Ϸ����������е���Ϸ��
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
	 * ��ϵͳ��ͣ�˻ʱ���á�������Ϸ���ݣ���ֹͣ
	 * the game's thread from running.
	 * 
	 */
	@Override
	protected void onPause() {
		super.onPause();
		gameView.pause();
	}

	/**
	 * ϵͳ�ָ���һ�ʱ���á��ָ���Ϸ���ݺ�������Ϸ�̡߳�
	 * 
	 * */
	@Override
	protected void onResume() {
		super.onResume();
		gameView.resume();
	}
}

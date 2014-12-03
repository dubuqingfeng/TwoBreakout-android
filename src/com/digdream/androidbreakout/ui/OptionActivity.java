package com.digdream.androidbreakout.ui;

import com.digdream.androidbreakout.R;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;

/**
 * 这是设置的activity，设置一些声音，挡板颜色。
 * 排行
 * 控制
 * @author user
 *
 */
public class OptionActivity extends BaseActivity{
	private Button mBtnAudio;
	private boolean sound;
	private static final String SOUND_PREFS = "SOUND_PREFS";
	private final String SOUND_ON_OFF = "SOUND_ON_OFF";
	private ProgressBar mVelocityProgressBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_option);
		
		init();
	}

	private void init() {
		//控制声音开关
		mBtnAudio = (Button)findViewById(R.id.btn_audio);
		//控制单人模式速度
		mVelocityProgressBar = (ProgressBar) findViewById(R.id.velocityprogressBar);
		//mVelocityProgressBar.getProgress();
		SharedPreferences soundSettings = getSharedPreferences(SOUND_PREFS, 0);
		sound = soundSettings.getBoolean("soundOn", true);
		if(sound){
			//关闭声音
			mBtnAudio.setBackgroundResource(R.drawable.button_sound_off);
		}
		else{
			mBtnAudio.setBackgroundResource(R.drawable.button_sound_on);
		}
		mBtnAudio.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				if(sound){
					//关闭声音
					mBtnAudio.setBackgroundResource(R.drawable.button_sound_off);
					sound = false;
				}
				else{
					mBtnAudio.setBackgroundResource(R.drawable.button_sound_on);
					sound = true;
				}
				SharedPreferences soundSettings = getSharedPreferences(SOUND_PREFS, 0);
				SharedPreferences.Editor soundEditor = soundSettings.edit();
				soundEditor.putBoolean("soundOn", sound);
				soundEditor.commit();
			}

		});
		//帮助详情
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			toFrontActivity();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	private void toFrontActivity() {
		finish();
	}

	@Override
	public void onBind(boolean arg0) {
		super.onBind(arg0);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
}

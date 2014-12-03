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
 * �������õ�activity������һЩ������������ɫ��
 * ����
 * ����
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
		//������������
		mBtnAudio = (Button)findViewById(R.id.btn_audio);
		//���Ƶ���ģʽ�ٶ�
		mVelocityProgressBar = (ProgressBar) findViewById(R.id.velocityprogressBar);
		//mVelocityProgressBar.getProgress();
		SharedPreferences soundSettings = getSharedPreferences(SOUND_PREFS, 0);
		sound = soundSettings.getBoolean("soundOn", true);
		if(sound){
			//�ر�����
			mBtnAudio.setBackgroundResource(R.drawable.button_sound_off);
		}
		else{
			mBtnAudio.setBackgroundResource(R.drawable.button_sound_on);
		}
		mBtnAudio.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				if(sound){
					//�ر�����
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
		//��������
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

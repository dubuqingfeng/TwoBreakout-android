package com.digdream.androidbreakout.ui;

import com.digdream.androidbreakout.R;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextPaint;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 这是单人剧情模式过关以后的activity。
 * 传递关卡，分数等信息。
 * 有按钮，返回，控制声音，重来，下一关。。。
 * @author user
 *
 */
public class ResultActivity extends Activity {

	private static final String STAGE = "stage";
	private final String NEW_GAME = "NEW_GAME";
	private static final String SOUND_PREFS = "SOUND_PREFS";
	private final String SOUND_ON_OFF = "SOUND_ON_OFF";
	
	private TextView tv_title;
	private TextView tv_stage;
	private TextView tv_points;
	private Button btn_next;
	private Button btn_reload;
	private Button btn_return;
	private int stage;
	private boolean sound;
	private int points;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_result);
		Intent intent = getIntent();
		//传递关卡信息
		stage = intent.getIntExtra(STAGE, 1);
		points = intent.getIntExtra("score", 0);
		Boolean state = intent.getBooleanExtra("state", true);
		
		initView();
		initData();
	}

	private void initData() {
		Log.w("test",stage+"   "+points);
		tv_points.setText(String.valueOf(points));
		tv_stage.setText("第    " + String.valueOf(stage) +"    关");
	}

	private void initView() {
		tv_title = (TextView)findViewById(R.id.result_title);
		TextPaint tp = tv_title.getPaint();
		tp.setFakeBoldText(true);
		tv_stage = (TextView)findViewById(R.id.result_stage);
		TextPaint tps = tv_stage.getPaint();
		tps.setFakeBoldText(true);
		tv_points = (TextView)findViewById(R.id.result_points);
		TextPaint tpoint = tv_points.getPaint();
		tpoint.setFakeBoldText(true);
		btn_next = (Button)findViewById(R.id.result_btn_next);
		btn_next.setOnClickListener(mOnClickListener);
		btn_reload = (Button)findViewById(R.id.result_btn_reload);
		btn_reload.setOnClickListener(mOnClickListener);
		btn_return = (Button)findViewById(R.id.result_btn_return);
		btn_return.setOnClickListener(mOnClickListener);
		SharedPreferences soundSettings = getSharedPreferences(SOUND_PREFS, 0);
		sound = soundSettings.getBoolean("soundOn", true);
	}
	private OnClickListener mOnClickListener = new OnClickListener(){
		public void onClick(View v) {
			switch(v.getId())
			{
				case R.id.result_btn_next:
					Intent intent = new Intent();
					intent.putExtra("stage", stage+1);
					com.digdream.androidbreakout.ui.StageActivity.stage = stage +1;
					intent.putExtra(SOUND_ON_OFF, sound);
					intent.setClass(getApplicationContext(), com.digdream.androidbreakout.ui.SwitchActivity.class);
					startActivity(intent);
					com.digdream.androidbreakout.ui.ResultActivity.this.finish();
					break;
				case R.id.result_btn_reload:
					Intent intentreload = new Intent();
					intentreload.putExtra("stage", stage);
					intentreload.putExtra(SOUND_ON_OFF, sound);
					intentreload.setClass(getApplicationContext(), com.digdream.androidbreakout.ui.SwitchActivity.class);
					startActivity(intentreload);
					com.digdream.androidbreakout.ui.ResultActivity.this.finish();
					break;
				case R.id.result_btn_return:
					Intent intentreturn = new Intent();
					intentreturn.setClass(getApplicationContext(), com.digdream.androidbreakout.ui.AppActivity.class);
					startActivity(intentreturn);
					com.digdream.androidbreakout.ui.ResultActivity.this.finish();
					break;
				default :
			}
			
		}
		
	};

}

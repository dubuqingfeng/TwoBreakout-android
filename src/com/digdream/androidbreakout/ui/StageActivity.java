package com.digdream.androidbreakout.ui;

import com.digdream.androidbreakout.R;
import com.digdream.androidbreakout.data.UserPreferences;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

/**
 * 这是单人剧情模式的activity，如果没有通过第一关，通过图片滑动的动画，进行第一关。
 * @author user
 *
 */
public class StageActivity extends Activity {

	private Button mStage1;
	private Button mStage2;
	private Button mStage3;
	private Button mStage4;
	private Button mStage5;
	private Button mStage6;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//判断shareprefences
		//if没有通过第一关，则跳转activity
		UserPreferences preferences = new UserPreferences();
		preferences.init(StageActivity.this);
		if(preferences.getLevel() == 0)
		{
			Intent intent = new Intent(this , com.digdream.androidbreakout.ui.SwitchActivity.class);
			startActivity(intent);
		}
		setContentView(R.layout.activity_stage);
		buttonsetOnclick();
	}
	public void buttonsetOnclick(){
		//注册监听器
		mStage1 = (Button)findViewById(R.id.btn_stage_1);
		mStage1.setOnClickListener(mOnClickListener);
		mStage2 = (Button)findViewById(R.id.btn_stage_2);
		mStage2.setOnClickListener(mOnClickListener);
		mStage3 = (Button)findViewById(R.id.btn_stage_3);
		mStage3.setOnClickListener(mOnClickListener);
		mStage4 = (Button)findViewById(R.id.btn_stage_4);
		mStage4.setOnClickListener(mOnClickListener);
		mStage5 = (Button)findViewById(R.id.btn_stage_5);
		mStage5.setOnClickListener(mOnClickListener);
		mStage6 = (Button)findViewById(R.id.btn_stage_6);
		mStage6.setOnClickListener(mOnClickListener);
	}
	
	private OnClickListener mOnClickListener = new OnClickListener(){

		public void onClick(View v) {
			switch(v.getId())
			{
				case R.id.btn_stage_1:
				break;
				case R.id.btn_stage_2:
					break;
				case R.id.btn_stage_3:
					break;
				case R.id.btn_stage_4:
					break;
				case R.id.btn_stage_5:
					break;
				case R.id.btn_stage_6:
					break;
				default :
			}
			
		}
		
	};

}

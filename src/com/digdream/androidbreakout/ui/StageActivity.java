package com.digdream.androidbreakout.ui;

import com.digdream.androidbreakout.R;
import com.digdream.androidbreakout.data.UserPreferences;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

/**
 * 
 * ���ǵ��˾���ģʽ��activity�����û��ͨ����һ�أ�ͨ��ͼƬ�����Ķ��������е�һ�ء�
 * 
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
	private UserPreferences preferences;
	private View mBtnReturn;
	public static int stage;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//�ж�shareprefences
		//ifû��ͨ����һ�أ�����תactivity
		preferences = new UserPreferences();
		preferences.init(StageActivity.this);
		if(preferences.getLevel() == 0)
		{
			stage = 1;
			Intent intent = new Intent(this,com.digdream.androidbreakout.ui.SwitchActivity.class);
			startActivity(intent);
			this.finish();
		}
		setContentView(R.layout.activity_stage);
		buttonsetOnclick();
	}
	public void buttonsetOnclick(){
		//ע�������
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
		mBtnReturn = (Button)findViewById(R.id.btn_return);
		mBtnReturn.setOnClickListener(mOnClickListener);
	}
	
	public void toNextActivity(){
		Intent intent = new Intent();
		intent.putExtra("stage", stage);
		intent.setClass(getApplicationContext(), com.digdream.androidbreakout.ui.SwitchActivity.class);
		startActivity(intent);
		com.digdream.androidbreakout.ui.StageActivity.this.finish();
	}
	
	private OnClickListener mOnClickListener = new OnClickListener(){
		public void onClick(View v) {
			switch(v.getId())
			{
				//��ʶ������
				case R.id.btn_stage_1:
					if(preferences.getLevel() >= 0 )
					{
						stage = 1;
						toNextActivity();
					}else{
						Toast.makeText(getApplicationContext(), R.string.app_name, Toast.LENGTH_SHORT).show();
					}
					break;
				//��������ʶ
				case R.id.btn_stage_2:
					if(preferences.getLevel() >= 1 )
					{
						stage = 2;
						toNextActivity();
						//��ת���ڶ���
					}else{
						Toast.makeText(getApplicationContext(), "�ȼ�����", Toast.LENGTH_SHORT).show();
					}
					break;
					//��֪
				case R.id.btn_stage_3:
					if(preferences.getLevel() >= 2 )
					{
						stage = 3;
						toNextActivity();
					}else{
						Toast.makeText(getApplicationContext(), "�ȼ�����", Toast.LENGTH_SHORT).show();
					}
					break;
					//����
				case R.id.btn_stage_4:
					if(preferences.getLevel() >= 3 )
					{
						stage = 4;
						toNextActivity();
					}else{
						Toast.makeText(getApplicationContext(), "�ȼ�����", Toast.LENGTH_SHORT).show();
					}
					break;
					//����
				case R.id.btn_stage_5:
					if(preferences.getLevel() >= 4 )
					{
						stage = 5;
						toNextActivity();
					}else{
						Toast.makeText(getApplicationContext(), "�ȼ�����", Toast.LENGTH_SHORT).show();
					}
					break;
					//����
				case R.id.btn_stage_6:
					if(preferences.getLevel() >= 5 )
					{
						stage = 6;
						toNextActivity();
					}else{
						Toast.makeText(getApplicationContext(), "�ȼ�����", Toast.LENGTH_SHORT).show();
					}
					break;
				case R.id.btn_return:
					break;
				default :
			}
			
		}
		
	};

}

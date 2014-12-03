package com.digdream.androidbreakout.ui;

import com.digdream.androidbreakout.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * 这是单人剧情模式过关以后的activity。
 * 传递关卡，分数等信息。
 * 有按钮，返回，控制声音，重来，下一关。。。
 * @author user
 *
 */
public class ResultActivity extends Activity {

	private static final String STAGE = "stage";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_result);
		Intent intent = getIntent();
		//传递关卡信息
		int stage = intent.getIntExtra(STAGE, 1);
		Boolean state = intent.getBooleanExtra("state", true);
		
		initView();
		initData();
	}

	private void initData() {
		
	}

	private void initView() {
		
	}

}

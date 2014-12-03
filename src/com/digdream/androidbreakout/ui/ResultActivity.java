package com.digdream.androidbreakout.ui;

import com.digdream.androidbreakout.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * ���ǵ��˾���ģʽ�����Ժ��activity��
 * ���ݹؿ�����������Ϣ��
 * �а�ť�����أ�������������������һ�ء�����
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
		//���ݹؿ���Ϣ
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

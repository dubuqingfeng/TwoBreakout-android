package com.digdream.androidbreakout.ui;

import com.digdream.androidbreakout.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;

/**
 * 关于开发者的activity，前尘忆梦，总结游戏。
 * 
 * 参考rpg
 * @author user
 *
 */

public class AboutActivity extends BaseActivity{

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);
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
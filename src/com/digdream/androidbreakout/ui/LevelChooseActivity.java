package com.digdream.androidbreakout.ui;

import com.digdream.androidbreakout.R;
import com.digdream.androidbreakout.module.GameMessages.GameDataMessage;
import com.digdream.androidbreakout.module.GameMessages.GameLevelMessage;
import com.digdream.androidbreakout.module.GameMessages.GamePreparedMessage;
import com.lenovo.game.GameMessage;
import com.lenovo.game.GameUserInfo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

/**
 * 
 * @author user
 *
 */
public class LevelChooseActivity extends BaseActivity {

	
	private Button btn_level1;
	private Button btn_level2;
	private Button btn_level3;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_levelchoose);
		init();
	}

	
	private void init() {
		btn_level1 = (Button)findViewById(R.id.level1);
		/*btn_level2 = (Button)findViewById(R.id.level2);
		btn_level3 = (Button)findViewById(R.id.level3);*/
		btn_level1.setOnClickListener(mOnClickListener);
		/*btn_level2.setOnClickListener(mOnClickListener);
		btn_level3.setOnClickListener(mOnClickListener);*/
	}
	private OnClickListener mOnClickListener = new OnClickListener() {

		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.level1:
				//发送message
				GameUserInfo localUser = mGameShare.getLocalUser();
				GameUserInfo remoteUser = getRemoteUser();
				if (localUser == null || remoteUser == null)
					return;
				GameLevelMessage dataMsg = new GameLevelMessage(
						localUser.id, remoteUser.id, 1);
				GameMessage msg = dataMsg.toGameMessage();
				if (msg == null)
					return;
				mGameShare.sendMessage(msg);
				//这里同时进入游戏
				Intent intent = new Intent(LevelChooseActivity.this,
						com.digdream.androidbreakout.Breakout2p.class);
				startActivity(intent);
				LevelChooseActivity.this.finish();
				break;

			/*case R.id.level2:
				// 	第二关
				break;
			case R.id.level3:
				//	第三关
				break;*/
			default:
				break;

			}
		}
	};


	@Override
	public void onBind(boolean arg0) {
		super.onBind(arg0);
	}

	
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

}

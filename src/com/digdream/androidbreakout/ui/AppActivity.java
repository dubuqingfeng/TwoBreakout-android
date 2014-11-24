package com.digdream.androidbreakout.ui;

import org.json.JSONException;

import com.digdream.androidbreakout.Breakout;
import com.digdream.androidbreakout.GameView2p;
import com.digdream.androidbreakout.R;
import com.digdream.androidbreakout.module.GameData;
import com.digdream.androidbreakout.module.GameMessages;
import com.digdream.androidbreakout.module.GameMessages.AbstractGameMessage;
import com.digdream.androidbreakout.module.GameMessages.GameBeginMessage;
import com.digdream.androidbreakout.module.GameMessages.GameDataMessage;
import com.digdream.androidbreakout.module.GameMessages.GameLevelMessage;
import com.digdream.androidbreakout.module.GameMessages.GamePreparedMessage;
import com.lenovo.game.GameMessage;
import com.lenovo.game.GameMessageListener;
import com.lenovo.game.GameUserInfo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
/**
 * 程序主界面
 * 
 * 前尘忆梦--关于开发者
 * 
 * @author user
 *
 */
public class AppActivity extends BaseActivity {
	private static final String TAG = "AppActivity程序主界面";
	private final String NEW_GAME = "NEW_GAME";
	private static final String SOUND_PREFS = "SOUND_PREFS";
	private final String SOUND_ON_OFF = "SOUND_ON_OFF";
	private Button mPrepareBtn;
	private Button mAbout;
	private Button mBtn1pStart;
	private Button mAudioBtn;
	private Button mOptionBtn;
	private GameData mGameData = null;
	private int newGame;
	private boolean sound;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setVolumeControlStream(AudioManager.STREAM_MUSIC);
		setContentView(R.layout.activity_app);
		init();
	}

	private void init() {

		mPrepareBtn = (Button) findViewById(R.id.btn_prepare);
		mPrepareBtn.setOnClickListener(mOnClickListener);
		mAbout = (Button) findViewById(R.id.btn_about);
		mAbout.setOnClickListener(mOnClickListener);
		mBtn1pStart = (Button) findViewById(R.id.btn_1pstart);
		mBtn1pStart.setOnClickListener(mOnClickListener);
		mAudioBtn = (Button) findViewById(R.id.btn_audio);
		mAudioBtn.setOnClickListener(mOnClickListener);
		mOptionBtn = (Button) findViewById(R.id.btn_option);
		mOptionBtn.setOnClickListener(mOnClickListener);
		
		SharedPreferences soundSettings = getSharedPreferences(SOUND_PREFS, 0);
		sound = soundSettings.getBoolean("soundOn", true);
		
		Intent intent = getIntent();
		int mode = 1;
		boolean isInviter = false;
		if (intent.hasExtra("mode"))
			mode = intent.getIntExtra("mode", 1);
		Log.v(TAG, "game mode : " + mode);
		if (intent.hasExtra("inviter"))
			isInviter = intent.getBooleanExtra("inviter", false);
		Log.v(TAG, "is inviter : " + isInviter);
		mPrepareBtn.setClickable(mode == 1);
		mGameData = GameData.createGameData(mode, isInviter);
		if (mode == 1 || isInviter)
			mGameData.generateGameData(GameData.BLOCK_ROW_COUNT,
					GameData.BLOCK_COLUMN_COUNT);
	}

	private OnClickListener mOnClickListener = new OnClickListener() {

		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btn_prepare:
				mPrepareBtn.setText(R.string.prepared);
				mPrepareBtn.setBackgroundResource(R.drawable.create_account_button);
				mPrepareBtn.setClickable(false);
				mGameData.mIsLocalPrepared = true;
				if (mGameData.mMode == 1)
					startGame();
				else {
					if (mGameData.mIsInviter) {
						GameUserInfo localUser = mGameShare.getLocalUser();
						GameUserInfo remoteUser = getRemoteUser();
						if (localUser == null || remoteUser == null)
							return;
						GameDataMessage dataMsg = new GameDataMessage(
								localUser.id, remoteUser.id, (float) 1.22);
						GameMessage msg = dataMsg.toGameMessage();
						if (msg == null)
							return;

						mGameShare.sendMessage(msg);
						checkCanStartGame();
						GameView2p.mIsInviter = true;
					} else {
						GameUserInfo localUser = mGameShare.getLocalUser();
						GameUserInfo remoteUser = getRemoteUser();
						if (localUser == null || remoteUser == null)
							return;
						GamePreparedMessage preparedMsg = new GamePreparedMessage(
								localUser.id, remoteUser.id);
						GameMessage msg = preparedMsg.toGameMessage();
						if (msg == null)
							return;
						mGameShare.sendMessage(msg);
						GameView2p.mIsInviter = false;
					}
				}
				break;

			case R.id.btn_about:
				// 跳转关于开发者见面
				Intent intentabout = new Intent(AppActivity.this,
						com.digdream.androidbreakout.ui.AboutActivity.class);
				startActivity(intentabout);
				break;
			case R.id.btn_1pstart:
				// 单人开始游戏
				newGame = 1;
				Intent intent = new Intent(AppActivity.this,com.digdream.androidbreakout.ui.StageActivity.class);
				intent.putExtra(NEW_GAME, newGame);
				intent.putExtra(SOUND_ON_OFF, sound);
				startActivity(intent);
				break;
			case R.id.btn_audio:
				// 控制声音开关
				
				if(sound){
					//关闭声音
					mAudioBtn.setBackgroundResource(R.drawable.button_sound_off);
					sound = false;
				}
				else{
					mAudioBtn.setBackgroundResource(R.drawable.button_sound_on);
					sound = true;
				}
				SharedPreferences soundSettings = getSharedPreferences(SOUND_PREFS, 0);
				SharedPreferences.Editor soundEditor = soundSettings.edit();
				soundEditor.putBoolean("soundOn", sound);
				soundEditor.commit();
				break;
			case R.id.btn_option:
				// 跳转设置界面
				Intent intenttest = new Intent(AppActivity.this,
						com.digdream.androidbreakout.ui.OptionActivity.class);
				startActivity(intenttest);
				
				break;
			default:
				break;

			}
		}
	};

	@Override
	public void onBind(boolean success) {
		Log.v(TAG, "onBind(), success : " + success);
		if (!success) {
			Toast.makeText(getApplicationContext(), "Bind Service failed.",
					Toast.LENGTH_LONG).show();
			return;
		}

		mGameShare.addMessageListener(mMessageListener);
		mPrepareBtn.setClickable(true);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mGameShare != null)
			mGameShare.removeMessageListener(mMessageListener);
	}

	private GameMessageListener mMessageListener = new GameMessageListener() {

		public void onMessage(GameMessage gameMessage) {
			Log.v(TAG, "onMessage, message : " + gameMessage.toString());
			AbstractGameMessage msg;
			try {
				msg = GameMessages.createGameMessage(gameMessage.getType(),
						gameMessage.getMessage());
				msg.setFrom(gameMessage.getFrom());
				msg.setTo(gameMessage.getTo());
			} catch (JSONException e) {
				Log.d(TAG, "json error!");
				return;
			}
			if (msg.getType().equalsIgnoreCase(GameMessages.MSG_TYPE_GAME_DATA)) {
				GameDataMessage dataMsg = (GameDataMessage) msg;
				Float gameData = dataMsg.getGameData();
				/*
				 * if(gameData == "1") { Intent intent = new
				 * Intent(AppActivity.this,
				 * com.digdream.androidbreakout.Breakout2p.class);
				 * startActivity(intent); AppActivity.this.finish(); }
				 */
				mGameData.generateGameData(gameData);
			} else if (msg.getType().equalsIgnoreCase(
					GameMessages.MSG_TYPE_GAME_PREPARED)) {
				mGameData.mIsRemotePrepared = true;
				checkCanStartGame();
			} else if (msg.getType().equalsIgnoreCase(
					GameMessages.MSG_TYPE_GAME_BEGIN)) {
				if (mGameData.mIsInviter) {
					chooseLevel();
					// startGame();
				}
			} else if (msg.getType().equalsIgnoreCase(
					GameMessages.MSG_TYPE_GAME_LEVEL)) {
				GameLevelMessage levelMsg = (GameLevelMessage) msg;
				int gameData = levelMsg.getGameData();
				Log.w(TAG,""+gameData);
				if (gameData == 1) {
					startGame();
					
				}
			}
		}

	};

	private void chooseLevel() {
		Intent intent = new Intent(AppActivity.this,
				com.digdream.androidbreakout.ui.LevelChooseActivity.class);
		startActivity(intent);
		AppActivity.this.finish();
	}

	private void startGame() {
		Intent intent = new Intent(AppActivity.this,
				com.digdream.androidbreakout.Breakout2p.class);
		intent.putExtra(NEW_GAME, newGame);
		intent.putExtra(SOUND_ON_OFF, sound);
		startActivity(intent);
		AppActivity.this.finish();
	}

	private void checkCanStartGame() {
		if (mGameData.mIsLocalPrepared && mGameData.mIsRemotePrepared) {
			if (mGameData.mIsInviter) { 
				
				GameUserInfo localUser = mGameShare.getLocalUser();
				GameUserInfo remoteUser = getRemoteUser();
				if (localUser == null || remoteUser == null)
					return;
				GameBeginMessage beginMsg = new GameBeginMessage(localUser.id,
						remoteUser.id);
				GameMessage msg = beginMsg.toGameMessage();
				if (msg == null)
					return;
				mGameShare.sendMessage(msg);
				// startGame();
				chooseLevel();
				GameView2p.mIsInviter = true;
			} else {
				// 根据选择的关卡信息。跳转activity
				GameView2p.mIsInviter = false;
			}

		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		SharedPreferences soundSettings = getSharedPreferences(SOUND_PREFS, 0);
		sound = soundSettings.getBoolean("soundOn", true);
		if(sound){
			//关闭声音
			mAudioBtn.setBackgroundResource(R.drawable.button_sound_off);
			sound = false;
		}
		else{
			mAudioBtn.setBackgroundResource(R.drawable.button_sound_on);
			sound = true;
		}
		SharedPreferences soundSettingsresume = getSharedPreferences(SOUND_PREFS, 0);
		SharedPreferences.Editor soundEditorresume = soundSettingsresume.edit();
		soundEditorresume.putBoolean("soundOn", sound);
		soundEditorresume.commit();
	}

}

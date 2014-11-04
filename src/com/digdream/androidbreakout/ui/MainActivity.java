package com.digdream.androidbreakout.ui;

import java.util.List;
import org.json.JSONException;

import com.digdream.androidbreakout.R;
import com.digdream.androidbreakout.module.GameData;
import com.digdream.androidbreakout.module.GameMessages;
import com.digdream.androidbreakout.module.GameMessages.AbstractGameMessage;
import com.digdream.androidbreakout.module.GameMessages.GameBeginMessage;
import com.digdream.androidbreakout.module.GameMessages.GameDataMessage;
import com.digdream.androidbreakout.module.GameMessages.GamePreparedMessage;
import com.lenovo.game.GameMessage;
import com.lenovo.game.GameMessageListener;
import com.lenovo.game.GameUserInfo;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends BaseActivity {
	private Button mPrepareBtn;
	private View mExit;
	private GameData mGameData = null;

	private static final String TAG = "MainActivity∆Ù∂ØΩÁ√Ê";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);
		init();
	}
	
	private void init() {
	        mPrepareBtn = (Button)findViewById(R.id.btn_prepare);
	        mPrepareBtn.setOnClickListener(mOnClickListener);
	        mExit = findViewById(R.id.btn_exit);
	        mExit.setOnClickListener(mOnClickListener);

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
	            mGameData.generateGameData(GameData.BLOCK_ROW_COUNT, GameData.BLOCK_COLUMN_COUNT);
	    }
	private OnClickListener mOnClickListener = new OnClickListener() {

        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_prepare:
                    mPrepareBtn.setText(R.string.prepared);
                    mPrepareBtn.setBackgroundResource(R.drawable.ready_btn_bg);
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
                            GameDataMessage dataMsg = new GameDataMessage(localUser.id, remoteUser.id, (float) 1.22);
                            GameMessage msg = dataMsg.toGameMessage();
                            if (msg == null)
                                return;

                            mGameShare.sendMessage(msg);
                            checkCanStartGame();
                        } else {
                            GameUserInfo localUser = mGameShare.getLocalUser();
                            GameUserInfo remoteUser = getRemoteUser();
                            if (localUser == null || remoteUser == null)
                                return;
                            GamePreparedMessage preparedMsg = new GamePreparedMessage(localUser.id, remoteUser.id);
                            GameMessage msg = preparedMsg.toGameMessage();
                            if (msg == null)
                                return;

                            mGameShare.sendMessage(msg);
                        }
                    }
                    break;

                case R.id.btn_exit:
                    mGameShare.quitGame();
                    MainActivity.this.finish();
                    System.exit(0);
                    break;

                default:
                    break;

            }
        }
    };


    @Override
    protected void onDestroy() {
        if (mGameShare != null)
            mGameShare.removeMessageListener(mMessageListener);
        super.onDestroy();
    }

    private void checkCanStartGame() {
        if (mGameData.mIsLocalPrepared && mGameData.mIsRemotePrepared) {
            if (mGameData.mIsInviter) {
                GameUserInfo localUser = mGameShare.getLocalUser();
                GameUserInfo remoteUser = getRemoteUser();
                if (localUser == null || remoteUser == null)
                    return;
                GameBeginMessage beginMsg = new GameBeginMessage(localUser.id, remoteUser.id);
                GameMessage msg = beginMsg.toGameMessage();
                if (msg == null)
                    return;
                mGameShare.sendMessage(msg);
            }
            startGame();
        }
    }

    private GameMessageListener mMessageListener = new GameMessageListener() {


        public void onMessage(GameMessage gameMessage) {
            Log.v(TAG, "onMessage, message : " + gameMessage.toString());
            AbstractGameMessage msg;
            try {
                msg = GameMessages.createGameMessage(gameMessage.getType(), gameMessage.getMessage());
                msg.setFrom(gameMessage.getFrom());
                msg.setTo(gameMessage.getTo());
            } catch (JSONException e) {
                Log.d(TAG, "json error!");
                return;
            }
            if (msg.getType().equalsIgnoreCase(GameMessages.MSG_TYPE_GAME_DATA)) {
                GameDataMessage dataMsg = (GameDataMessage)msg;
                Float gameData = dataMsg.getGameData();
                mGameData.generateGameData(gameData);
            } else if (msg.getType().equalsIgnoreCase(GameMessages.MSG_TYPE_GAME_PREPARED)) {
                mGameData.mIsRemotePrepared = true;
                checkCanStartGame();
            } else if (msg.getType().equalsIgnoreCase(GameMessages.MSG_TYPE_GAME_BEGIN)) {
                if (!mGameData.mIsInviter) {
                    startGame();
                }
            }
        }
    };

    private void startGame() {
        Intent intent = new Intent(MainActivity.this, com.digdream.androidbreakout.Breakout2p.class);
        startActivity(intent);
        MainActivity.this.finish();
    }

    @Override
    public void onBind(boolean success) {
        Log.v(TAG, "onBind(), success : " + success);
        if (!success) {
            Toast.makeText(getApplicationContext(), "Bind Service failed.", Toast.LENGTH_LONG).show();
            return;
        }

        mGameShare.addMessageListener(mMessageListener);
        mPrepareBtn.setClickable(true);
    }
}

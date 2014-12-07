package com.digdream.androidbreakout.ui;

import java.util.List;

import com.digdream.androidbreakout.R;
import com.lenovo.game.GameUserInfo;
import com.lenovo.game.GameUserListener;
import com.lenovo.game.GameUserListener.UserEventType;
import com.lenovo.gamesdk.GameShare;
import com.lenovo.gamesdk.GameShare.Bindlistener;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnKeyListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;

public class BaseActivity extends Activity implements Bindlistener {
	private static final String TAG = "BaseActivity";

	protected static GameShare mGameShare = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (mGameShare == null)
			mGameShare = new GameShare(getApplicationContext());
		mGameShare.addUserListener(mUserListener);

		new Thread() {
			@Override
			public void run() {
				mGameShare.bind(BaseActivity.this);
			}
		}.start();
	}

	public void onBind(boolean arg0) {

	}

	@Override
	protected void onDestroy() {
		if (mGameShare != null) {
			mGameShare.removeUserListener(mUserListener);
			mGameShare.unbind(BaseActivity.this);
		}
		super.onDestroy();
	}

	protected static GameUserInfo getRemoteUser() {
		List<GameUserInfo> users = mGameShare.getRemoteUsers();
		if (users.size() < 1) {
			return null;
		}
		return users.get(0);
	}

	private GameUserListener mUserListener = new GameUserListener() {

		public void onLocalUserChanged(UserEventType type, GameUserInfo user) {
			Log.v(TAG, "onLocalUserChanged, eventType : " + type
					+ ", userInfo : " + user);
		}

		public void onRemoteUserChanged(UserEventType type, GameUserInfo user) {
			switch (type) {
			case OFFLINE:
				Log.d(TAG, "Remote offline " + user.name);
				if (user.id.equals(getRemoteUser().id)) {
					mHandler.sendEmptyMessage(0);
				}
				break;
			default:
				break;
			}
		}
	};

	protected void showOfflineDialog() {
		AlertDialog.Builder builder = new Builder(BaseActivity.this);
		builder.setMessage(R.string.offline);
		builder.setPositiveButton(R.string.ok, new OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				BaseActivity.this.finish();
				System.exit(0);
			}
		});
		builder.setOnKeyListener(new OnKeyListener() {

			public boolean onKey(DialogInterface dialog, int keyCode,
					KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_BACK) {
					return true;
				}
				return false;
			}
		});
		builder.create().show();
	}
	/**
	 * 展示退出的对话框，需修改样式。
	 */
	protected void showExitDialog() {
		AlertDialog.Builder builder = new Builder(BaseActivity.this , R.style.dialog);
		builder.setMessage(R.string.exit_text);
		builder.setTitle("温馨提示");
		builder.setPositiveButton(R.string.ok, new OnClickListener() {
		
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				mGameShare.quitGame();
				BaseActivity.this.finish();
				System.exit(0);
			}
		});
		builder.setNegativeButton(R.string.cancel, new OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.create().show();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			showExitDialog();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				showOfflineDialog();
				break;

			default:
				break;
			}
			super.handleMessage(msg);
		}

	};

}

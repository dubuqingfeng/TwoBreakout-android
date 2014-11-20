package com.digdream.androidbreakout.ui;

import com.digdream.androidbreakout.R;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.TextView;

/**
 * 
 * @author user
 *
 */
public class TestActivity extends Activity {

	private TextView info = null;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_test);
		this.info = (TextView) super.findViewById(R.id.info);
		// Ìí¼Ó´¥ÃþÊÂ¼þ
		this.info.setOnTouchListener(new TouchListenerImp());
	}

	private class TouchListenerImp implements OnTouchListener {

		public boolean onTouch(View v, MotionEvent event) {
			TestActivity.this.info.setText("x=" + event.getX() + "y="
					+ event.getY());
			return false;
		}
	}
}
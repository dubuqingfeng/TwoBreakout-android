package com.digdream.androidbreakout.ui;

import com.digdream.androidbreakout.R;
import com.digdream.androidbreakout.switchlayout.OnViewChangeListener;
import com.digdream.androidbreakout.switchlayout.SwitchLayout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * 故事情节
 * @author user
 *
 */

public class SwitchActivity extends Activity{
    SwitchLayout switchLayout;//自定义的控件
	LinearLayout linearLayout;
	int mViewCount;//自定义控件中子控件的个数
	ImageView mImageView[];//底部的imageView
	int mCurSel;//当前选中的imageView
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Intent intent = getIntent();
        int stage = intent.getIntExtra("stage", 1);
        switch(stage){
        case 1:setContentView(R.layout.mainswitch);break;
        case 2:setContentView(R.layout.stage_switch2);break;
        case 3:setContentView(R.layout.stage_switch3);break;
        case 4:setContentView(R.layout.stage_switch4);break;
        case 5:setContentView(R.layout.stage_switch5);break;
        case 6:setContentView(R.layout.stage_switch6);break;
        default:
        }
        init();
    }

	private void init() {
		switchLayout = (SwitchLayout) findViewById(R.id.switchLayoutID);
		linearLayout = (LinearLayout) findViewById(R.id.linerLayoutID);
		
		//得到子控件的个数
		mViewCount = switchLayout.getChildCount();
		mImageView = new ImageView[mViewCount];
		//设置imageView
		for(int i = 0;i < mViewCount;i++){
			//得到LinearLayout中的子控件
			mImageView[i] = (ImageView) linearLayout.getChildAt(i);
			mImageView[i].setEnabled(true);//控件激活
			mImageView[i].setOnClickListener(new MOnClickListener());
			mImageView[i].setTag(i);//设置与view相关的标签
		}
		//设置第一个imageView不被激活
		mCurSel = 0;
		mImageView[mCurSel].setEnabled(false);
		switchLayout.setOnViewChangeListener(new MOnViewChangeListener());
		
	}
	
	//点击事件的监听器
	private class MOnClickListener implements OnClickListener{
		public void onClick(View v) {
			int pos = (Integer) v.getTag();
			System.out.println("pos:--" + pos);
			//设置当前显示的ImageView
			setCurPoint(pos);
			//设置自定义控件中的哪个子控件展示在当前屏幕中
			switchLayout.snapToScreen(pos);
		}
	}
	

	/**
	 * 设置当前显示的ImageView
	 * @param pos
	 */
	private void setCurPoint(int pos) {
		if(pos < 0 || pos > mViewCount -1 || mCurSel == pos)
			return;
		//当前的imgaeView将可以被激活
		mImageView[mCurSel].setEnabled(true);
		//将要跳转过去的那个imageView变成不可激活
		mImageView[pos].setEnabled(false);
		mCurSel = pos;
	}
	
	//自定义控件中View改变的事件监听
	private class MOnViewChangeListener implements OnViewChangeListener{
		public void onViewChange(int view) {
			System.out.println("view:--" + view);
			if(view < 0 || mCurSel == view){
				return ;
			}else if(view > mViewCount - 1){
				//当滚动到第五个的时候activity会被关闭
				System.out.println("finish activity");
				finish();
			}
			setCurPoint(view);
		}
		
	}
	
}
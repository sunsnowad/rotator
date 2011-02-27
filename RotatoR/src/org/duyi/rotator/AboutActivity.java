package org.duyi.rotator;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;

/**
 * @author Yi Du <duyi001@gmail.com>
 * @Date 2011-2-22
 */
public class AboutActivity extends Activity {
	//TODO 关注代码：android:theme="@android:style/Theme.Dialog"
	//TODO 尝试使用dialog来实现about
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
        		WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.about);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		finish();
		return true;
	}
	
	
}

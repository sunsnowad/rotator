package org.duyi.rotator;

import org.duyi.rotator.logic.Choose;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

/**
 * @author Yi Du <duyi001@gmail.com>
 * @Date 2011-2-25
 */
public class ResultActivity extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
        		WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.result);
		
		TextView text = (TextView)findViewById(R.id.textResult);
		String s = getIntent().getExtras().getString(Choose.CHOOSE_NAME);
		if(s != null)
			text.setText(text.getText() + s);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		finish();
		return true;
	}

	@Override
	protected void onPause() {
		// TODO save result
		super.onPause();
	}
	
	
	
	
}

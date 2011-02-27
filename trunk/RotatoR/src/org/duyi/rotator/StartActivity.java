package org.duyi.rotator;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

public class StartActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
        		WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
        setContentView(R.layout.main);      
        
        Button buttonExit = (Button)findViewById(R.id.buttonExit);
        buttonExit.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				StartActivity.this.finish();				
			}
		});
        Button buttonAbout = (Button)findViewById(R.id.buttonAbout);
        buttonAbout.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				Intent i = new Intent(StartActivity.this, AboutActivity.class);
				startActivity(i);				
			}
		});
        
        Button buttonMyRotator = (Button)findViewById(R.id.buttonMyRotatoR);
        buttonMyRotator.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				Intent i = new Intent(StartActivity.this, RotatorActivity.class);
				startActivity(i);
			}
		});
    }
    
    
}
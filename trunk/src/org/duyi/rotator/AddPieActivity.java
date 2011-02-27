
package org.duyi.rotator;

import org.duyi.rotator.logic.Choose;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

/**
 * @Author Yi Du <duyi001@gmail.com>
 * @Date 2011-2-22
 */
//TODO 以后要设置根据类型过滤和添加
public class AddPieActivity extends Activity{

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
        		WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.newpie);

		final EditText text = (EditText)findViewById(R.id.textAddPie);
		Button buttonOk = (Button)findViewById(R.id.buttonAddPie);
		buttonOk.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				Intent i = new Intent(AddPieActivity.this, RotatorActivity.class);
				i.putExtra(Choose.CHOOSE_NAME, text.getText().toString());
				setResult(RESULT_OK, i);
				finish();
			}
		});
		
		Button buttonCancel = (Button)findViewById(R.id.buttonPieCancel);
		buttonCancel.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				setResult(RESULT_CANCELED, null);
				finish();
			}
		});

	}

	
}


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
public class ModifyPieActivity extends Activity{

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
        		WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.modifypie);

		final EditText text = (EditText)findViewById(R.id.textModifyPie);
		text.setText(getIntent().getExtras().getString(Choose.CHOOSE_NAME));
		
		final int selectionIndex = 
			getIntent().getExtras().getInt(RotatorActivity.SELECTION_INDEX);
		
		Button buttonOk = (Button)findViewById(R.id.buttonModifyPie);
		buttonOk.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				Intent i = new Intent(ModifyPieActivity.this, RotatorActivity.class);
				i.putExtra(Choose.CHOOSE_NAME, text.getText().toString());
				i.putExtra(RotatorActivity.SELECTION_INDEX, selectionIndex);
				setResult(RESULT_OK, i);
				finish();
			}
		});
		
		Button buttonCancel = (Button)findViewById(R.id.buttonPieDel);
		buttonCancel.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				Intent i = new Intent(ModifyPieActivity.this, RotatorActivity.class);
				i.putExtra(RotatorActivity.SELECTION_INDEX, selectionIndex);
				setResult(RESULT_CANCELED, i);
				finish();
			}
		});
	}
}

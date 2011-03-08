package org.duyi.rotator;

import java.util.ArrayList;

import org.duyi.rotator.logic.Choose;

import com.thoughtworks.xstream.XStream;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

/**
 * @Author Yi Du <duyi001@gmail.com>
 * @Date 2011-2-22
 */
public class RotatorActivity extends Activity{
	private static final String TAG = "Rotator";
	private static final String ROTATOR_HISTORY = "rotatorhistory";
	private static final int SHOW_ADDPIE = 1;
	private static final int SHOW_MODIFYPIE = 2;
	
	private ArrayList<Choose> chooseList;
	private RotatorView view;

	private XStream xstream = new XStream();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG, "on create");
		//TODO load history
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
        		WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //load history
        String history = 
        	getPreferences(MODE_PRIVATE).getString(ROTATOR_HISTORY, null);
        if(history != null){
        	Object o = xstream.fromXML(history);
        	if(o instanceof ArrayList)
        		chooseList = (ArrayList<Choose>)o;
        }
        initLogic();
        
        if(view == null){
        	view = new RotatorView(this);
        	view.setBackgroundResource(R.drawable.styledefault);
        }
        setContentView(view);
	}
	

	private void initLogic() {
		if(chooseList == null)
			chooseList = new ArrayList<Choose>();
		//load data
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch(requestCode){
			case SHOW_ADDPIE:
				if(resultCode == RESULT_OK){
					String name = data.getStringExtra(Choose.CHOOSE_NAME);
					if(name == null)
						break;
					Choose c = new Choose(name);
					chooseList.add(c);
					view.postInvalidate();
					Log.d(TAG, "add pie ok " + name);
				}else if(resultCode == RESULT_CANCELED){
					Log.d(TAG, "add pie cancel");
				}
				break;
			case SHOW_MODIFYPIE:
			
				if(resultCode == RESULT_OK){
					String name = data.getStringExtra(Choose.CHOOSE_NAME);
					getChooseList().get(currentSelectionIndex).setName(name);
				}else if(resultCode == RESULT_CANCELED){
					getChooseList().remove(currentSelectionIndex);
				}
				view.postInvalidate();
				break;
			default:
				break;
		}
	}
	
	
	@Override
	protected void onResume() {
		super.onResume();
		setContentView(view);
		Log.d(TAG, "on resume invalidate");
	}
	
	
	@Override
	protected void onPause() {
		//save data to local
		String result = xstream.toXML(chooseList);
		Log.d(TAG, "rotator activity pause");
		getPreferences(MODE_PRIVATE).edit().putString(ROTATOR_HISTORY, result).commit();
		//stop all handler in view
		view.removeAllHandler();
		super.onPause();
	}

	public void showAddActivity() {
		Intent i = new Intent(this, AddPieActivity.class);
		startActivityForResult(i, SHOW_ADDPIE);
	}

	public ArrayList<Choose> getChooseList() {
		return chooseList;
	}

	private int currentSelectionIndex = -1;
	public void showModifyActivity(int index) {
		Intent i = new Intent(this, ModifyPieActivity.class);
		if(index <= 0)
			return;
		i.putExtra(Choose.CHOOSE_NAME, getChooseList().get(index-1).getName());
		currentSelectionIndex = index-1;
		startActivityForResult(i, SHOW_MODIFYPIE);
	}
	
	public void showResultActivity(int index){
		Intent i = new Intent(this, ResultActivity.class);
		if(index <= 0)
			return;
		i.putExtra(Choose.CHOOSE_NAME, getChooseList().get(index-1).getName());
		currentSelectionIndex = index-1;
		startActivity(i);
	}
	
	
}

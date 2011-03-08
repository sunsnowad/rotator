package org.duyi.rotator;

import java.util.ArrayList;

import org.duyi.rotator.logic.Choose;
import org.duyi.rotator.view.RotatorView;

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
	//preference tag
	private static final String ROTATOR_HISTORY = "rotatorhistory";
	//selection index tag
	public static final String SELECTION_INDEX = "selectionIndex";
	//start for result tag
	private static final int SHOW_ADDPIE = 1;
	private static final int SHOW_MODIFYPIE = 2;
	
	private ArrayList<Choose> chooseList;
	private RotatorView view;
	private XStream xstream = new XStream();
	private boolean isShowTip = true;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG, "on create");
        initLogic();
        

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
        		WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if(view == null){
        	view = new RotatorView(this, isShowTip);
        	view.setBackgroundResource(R.drawable.styledefault);
        }
        setContentView(view);
	}
	

	private void initLogic() {
		//load history
        String history = 
        	getPreferences(MODE_PRIVATE).getString(ROTATOR_HISTORY, null);
        if(history != null){
        	Object o = xstream.fromXML(history);
        	if(o instanceof ArrayList)
        		chooseList = (ArrayList<Choose>)o;
        }else{

			chooseList = new ArrayList<Choose>();
        }
        //set isShowTip
        isShowTip = getPreferences(MODE_PRIVATE).getBoolean(StartActivity.SHOW_TIP, true);
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
					//TODO judge whether exceed limitation
					view.postInvalidate();
					Log.d(TAG, "add pie ok " + name);
				}else if(resultCode == RESULT_CANCELED){
					Log.d(TAG, "add pie cancel");
				}
				break;
			case SHOW_MODIFYPIE:
				int index = data.getIntExtra(SELECTION_INDEX, -1);	

				if(resultCode == RESULT_OK && index >= 0){
					String name = data.getStringExtra(Choose.CHOOSE_NAME);
					getChooseList().get(index).setName(name);
				}else if(resultCode == RESULT_CANCELED && index >= 0){
					getChooseList().remove(index);
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
		getPreferences(MODE_PRIVATE).edit().putBoolean(StartActivity.SHOW_TIP, false).commit();
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

//	private int currentSelectionIndex = -1;
	public void showModifyActivity(int index) {
		Intent i = new Intent(this, ModifyPieActivity.class);
		if(index <= 0)
			return;
		i.putExtra(Choose.CHOOSE_NAME, getChooseList().get(index-1).getName());
		i.putExtra(SELECTION_INDEX, index -1);
//		currentSelectionIndex = index-1;
		startActivityForResult(i, SHOW_MODIFYPIE);
	}
	
	public void showResultActivity(int index){
		Intent i = new Intent(this, ResultActivity.class);
		if(index <= 0)
			return;
		i.putExtra(Choose.CHOOSE_NAME, getChooseList().get(index-1).getName());
//		i.putExtra(SELECTION_INDEX, index-1);
//		currentSelectionIndex = index-1;
		startActivity(i);
	}
	
	
}

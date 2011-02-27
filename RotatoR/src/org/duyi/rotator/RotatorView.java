package org.duyi.rotator;


import java.util.Random;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Path.FillType;
import android.graphics.RadialGradient;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * @Author Yi Du <duyi001@gmail.com>
 * @Date 2011-2-22
 */
public class RotatorView extends View {
	private static final String TAG = "RotatorView";
	private RotatorActivity rotator;
	private static final int SELECTED_ADD = 0;
	private static final int SELECTED_NONE = -1;
	private static final int ROTATE_INTERVAL = 2;//一般每2ms重绘
	private static final int DAMP = 5;//五级的阻尼，即速度慢五次
	private static final int ROTATE_MSG = 5;
	
	private int[] colors = {Color.YELLOW, Color.RED, Color.CYAN,Color.LTGRAY,
			Color.GREEN, Color.BLUE};
	
	
	private static int currentRotation = 0;
	private RotationHandler handler;
	private Paint paint;
	private Bitmap bitmapAdd;
	private Random random;
	
	private RectF rectBigCircle;
	private RectF rectSmallCircle;
	private float widthPixels;
	private float heightPixels;
	private float borderSize;
	float smillCircleStart;
	float radiusOfSmallCircle;
	float radiusOfBigCircle;
	private float centerX;
	private float centerY;
	
	private int currentRotationTime = -1;//-1 means no value
	
	
	/**
	 * @param context
	 */
	public RotatorView(Context context) {
		super(context);
		rotator = (RotatorActivity)context;
		handler = new RotationHandler();        
        paint = new Paint();
        random = new Random(System.currentTimeMillis());

		if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
			heightPixels = getContext().getResources().getDisplayMetrics().widthPixels;
			widthPixels = getContext().getResources().getDisplayMetrics().heightPixels;
		}else{
			heightPixels = getContext().getResources().getDisplayMetrics().heightPixels;
			widthPixels = getContext().getResources().getDisplayMetrics().widthPixels;
		}
		centerX = centerY = widthPixels/2.0f;
		borderSize = widthPixels/16;
		rectBigCircle = new RectF(
				borderSize, borderSize, widthPixels-borderSize, widthPixels-borderSize);
		smillCircleStart = borderSize*5.5f;
		radiusOfSmallCircle = borderSize*2.5f;
		radiusOfBigCircle = borderSize*7.5f;
		rectSmallCircle = new RectF(
				smillCircleStart,smillCircleStart,widthPixels-smillCircleStart,widthPixels-smillCircleStart);
		
		//image at center
		bitmapAdd = BitmapFactory.
			decodeResource(rotator.getResources(),R.drawable.arrow);
		bitmapAdd = Bitmap.createScaledBitmap(bitmapAdd,(int)radiusOfSmallCircle*2,(int)radiusOfSmallCircle*3,true);
		Log.d(TAG, "pixel(width,height,borderSize):"+
				widthPixels+","+heightPixels+","+borderSize);
		
	}
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
//		rotate the whole canvas
//		canvas.rotate(currentRotation, centerX, centerY);
		
		//TODO Animination 貌似也可以
		paint.setAntiAlias(true);
		
		//绘制外圈
		paint.setColor(Color.DKGRAY);
		paint.setStyle(Paint.Style.STROKE);
		canvas.drawCircle(centerX, centerY, radiusOfBigCircle+1, paint);

		int numOfPie = rotator.getChooseList().size();
//		Log.d(TAG, "numOfPie "+numOfPie);
		if(numOfPie == 0){
			//draw the big circle
			paint.setColor(Color.GRAY);
			paint.setStrokeWidth(3);
			canvas.drawCircle(centerX, centerY, radiusOfBigCircle, paint);
		}else if(numOfPie > 0){
			float angleOfPie = 360/numOfPie;
			Path testPath;
			for(int i = 0; i < numOfPie; i ++){
				testPath = new Path();
				testPath.setFillType(FillType.EVEN_ODD);
				paint.setColor(colors[i]);

				testPath.addArc(rectBigCircle, currentRotation+i*angleOfPie, angleOfPie);
				testPath.lineTo(centerX, centerY);
				testPath.close();
				testPath.addArc(rectSmallCircle, currentRotation+(i+1)*angleOfPie, -angleOfPie);
				testPath.lineTo(centerX, centerY);
				testPath.close();
//				paint.setShadowLayer(1, 1, 1, Color.GREEN);
				
				canvas.drawPath(testPath, paint);
				paint.setColor(Color.BLACK);
//				paint.setTextSize(R.dimen.sizePieText);
//				canvas.drawTextOnPath(
//						rotator.getChooseList().get(i).getName(), testPath, 0, R.dimen.offsetPieText, paint);
				//TODO resolution independent
				paint.setTextSize(20);
//				paint.setTextAlign(Paint.Align.CENTER);
				canvas.drawTextOnPath(
						rotator.getChooseList().get(i).getName(), testPath, 30, 30, paint);
			}
		}else{
			
		}

		canvas.drawBitmap(bitmapAdd, smillCircleStart, smillCircleStart-bitmapAdd.getWidth()/2, null);
	}
	
	boolean touchMoving = false;
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int selected = getSelected(event.getX(), event.getY());
		if(event.getAction() == MotionEvent.ACTION_MOVE){
			touchMoving = true;
			Log.i(TAG, "action_move");
			int numOfPie = rotator.getChooseList().size();
			if(numOfPie == 0)
				return true;
			invalidate();
		}
		else if(event.getAction() == MotionEvent.ACTION_UP){
			if(touchMoving){
				for(int i = 1; i <= DAMP; i ++){
//					handler.postDelayed(rotatePie, i *100);
					Message message = new Message();
					message.arg1 = ROTATE_MSG;
					message.what = i;
					handler.sendMessageDelayed(message, i *ROTATE_INTERVAL);
					handler.sendEmptyMessageDelayed(i, getCurrentRotationTime()*(i+1));
				}

				touchMoving = false;
				return true;
			}
		}
		if(selected == SELECTED_ADD){
			if(event.getAction() == MotionEvent.ACTION_DOWN){
				//TODO change pic
				Log.i(TAG, "action_down");
			}
			else if(event.getAction() == MotionEvent.ACTION_UP){
				//TODO change image  and add new choice
				rotator.showAddActivity();
				Log.i(TAG, "action_up");
			}
		}
		else if(selected == SELECTED_NONE){
			
		}else{
			if(event.getAction() == MotionEvent.ACTION_DOWN){
				//TODO change pic
				Log.i(TAG, "selected action_down");
			}
			else if(event.getAction() == MotionEvent.ACTION_UP){
				rotator.showModifyActivity(selected);
				Log.i(TAG, "selected action_up");
			}
		}
//		handler.removeCallbacks(rotatePie);
//		handler.removeCallbacks(stopPie);
		return true;
		
	}
	
	private int getCurrentRotationTime(){
		if(currentRotationTime == -1){
			currentRotationTime = ROTATE_INTERVAL * 100 * (random.nextInt(5)+5);
			Log.d(TAG, "currentRotationTime:"+getCurrentRotationTime());
		}
		return currentRotationTime;
	}
	
	public void setCurrentRotationTime(int time){
		this.currentRotationTime = time;
	}
	
	/**
	 * calculate which pie is selected
	 * @param x
	 * @param y
	 * @return index of the selected pie.
	 * 0 if center is selected.
	 * -1 if none is selected.
	 */
	private int getSelected(float x, float y) {
		double distanceToCenter = Math.sqrt((x-centerX)*(x-centerX)+(y-centerY)*(y-centerY));
		if(distanceToCenter < radiusOfSmallCircle)
			return SELECTED_ADD;
		if(distanceToCenter > radiusOfBigCircle)
			return SELECTED_NONE;
		double angle;
		if(x == centerX && y == centerY){
			return -1;
		}
		else if(x == centerX && y > centerY){
			angle = Math.PI/2;
		}
		else if(x == centerX && y < centerY){
			angle = Math.PI*3/2;
		}
		else if(x > centerX && y == centerY){
			angle = 0;
		}
		else if(x < centerX && y ==centerY){
			angle = Math.PI;
		}
		else{
			float tan = (y-centerX)/(x-centerY);
			angle = (float) Math.atan(tan);
			if(x < centerX){
				angle += Math.PI;
			}
		}
		double currentAngle = (angle - currentRotation*Math.PI/180)%(2*Math.PI);
		if(currentAngle < 0)
			currentAngle += Math.PI*2;
		int index = (int)(currentAngle/((2*Math.PI)/rotator.getChooseList().size()))+1;
		Log.d(TAG, "listsize:"+rotator.getChooseList().size()+
				"index : "+index+";currentAngle:"+currentAngle);
		return index;
	}
	
	//下面是使用回调函数实现
//    private Runnable rotatePie = new Runnable() {  
//        public void run() {  
//        	rotateOneDegree();  
//        	handler.postDelayed(rotatePie, RotatorView.ROTATE_INTERVAL);  
//        }  
//    };  
//    private Runnable stopPie = new Runnable() {  
//        public void run() {  
//        	handler.removeCallbacks(rotatePie);
//        	int index = RotatorView.this.pointerIndex();
//        	RotatorView.this.rotator.showResultActivity(index);
//        	Log.d(TAG, "stopPie handler");
//        }  
//    };  
	public void rotateOneDegree() {
			currentRotation++;
			invalidate();
	}
	/**
	 * 
	 * @return index of the pointer pointing
	 */
	private int pointerIndex(){
		float x = centerX;
		float y = (radiusOfBigCircle+radiusOfSmallCircle)/2;
		return getSelected(x, y);
	}
	
	class RotationHandler extends Handler{
		public RotationHandler(){
			
		}
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if(msg.arg1 == ROTATE_MSG){
				rotateOneDegree();  
				Message message = new Message();
				message.arg1 = ROTATE_MSG;
				message.what = msg.what;
				handler.sendMessageDelayed(message, ROTATE_INTERVAL);
			}
			else{
				removeMessages(msg.what);
				Log.d(TAG, "remove msg :"+ msg.what);
				if(msg.what == DAMP){
					int index = pointerIndex();
					rotator.showResultActivity(index);
				}
			}
		}
		
	}

	public void removeAllHandler() {
		if(handler != null){
			for(int i = 1; i <= DAMP; i ++){
				handler.removeMessages(i);
				Log.i(TAG, "remove message :"+i);
			}
		}
	}
}

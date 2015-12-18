package kr.lavalse.lcalendarview.uiset;

import java.util.Calendar;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.MotionEvent;

public class DayObject{
	private Canvas canvas;
	
	/*
	 * Variable of data about the day
	 */
	private int dayNumber = 0;
	private int date = 0;
	
	private boolean isToday = false;
	private boolean isEndOfMonth = false;
	private boolean isSelected = false;
	
	private int timeType = OnViewCallbackListener.TIMETYPE_CURRENT;
	/*
	 *	 Variable of drawing
	 */
	private int offsetX;
	private int offsetY;
	
	private int width;
	private int height;
	
	// Background
	private Paint pFrame;
	private RectF frame;
	
	// Selected Background
	private Paint sFrame;
	private RectF selectedFrame;
	
	private Paint todayFrame;
	
	// Text of Day
	private Rect txtBounds;
	private Paint txtFrame;
	
	// Frame Line
	private Paint lPaint;
	private RectF lFrame;
	
	/*
	 * Variable of listener
	 */
	private OnViewCallbackListener listener;
	
	public DayObject(int dayNumber, int date){
		this.dayNumber = dayNumber;
		this.date = date;
		
		init();
	}
	
	private void init(){
		
		pFrame = new Paint();
		pFrame.setAntiAlias(true);
		
		sFrame = new Paint();
		sFrame.setAntiAlias(true);
		
		todayFrame = new Paint();
		todayFrame.setAntiAlias(true);
		
		txtFrame = new Paint();
		txtFrame.setAntiAlias(true);
		txtFrame.setTextSize(35);
		txtFrame.setTextAlign(Align.CENTER);
		if(date == 1) txtFrame.setColor(Color.BLUE);
		if(date == 7) txtFrame.setColor(Color.RED);
		
		lPaint = new Paint();
		lPaint.setAntiAlias(true);
		lPaint.setColor(Color.rgb(48, 162, 255));
		lPaint.setStrokeWidth(0.9f);
		
		txtBounds = new Rect();
	}
	
	protected void draw(Canvas canvas) {
		this.canvas = canvas;
		
		if(frame != null){			
			canvas.drawRect(frame, (!isToday) ? ((!isSelected) ? pFrame : sFrame) : ((!isSelected) ? todayFrame : sFrame));
			
			String dayStr = Integer.toString(dayNumber);
			
			txtFrame.getTextBounds(dayStr, 0, dayStr.length(), txtBounds);
			
			int txtOffsetX = (offsetX + width) - (width/2);
			int txtOffsetY = (offsetY + height) - (height/2)+10;
			
			canvas.drawText(Integer.toString(dayNumber), txtOffsetX, txtOffsetY, txtFrame);
			
			if(!isEndOfMonth) canvas.drawLine(offsetX, offsetY+height, offsetX+width, offsetY+height, lPaint);
			if(date != 1) canvas.drawLine(offsetX, offsetY, offsetX, offsetY+height, lPaint);
			
		}
	}
	
	/**
	 * Set Objects frame size
	 * @param frame Get width/height on the parent view from 'onLayout()' or 'setCalendar()'
	 */
	public void setFrame(RectF frame){
		this.frame = frame;
		
		offsetX = (int)frame.left;
		offsetY = (int)frame.top;
		width = (int) frame.width();
		height = (int) frame.height();
		
		this.selectedFrame = new RectF(offsetX, offsetY, offsetX+width, offsetY+height);
	}
	
	/**
	 * Set frame background color
	 * @param color
	 */
	public void setFrameColor(int color){
		pFrame.setColor(color);
	}
	
	/**
	 * Set frame background color when it is selected
	 * @param color
	 */
	public void setFrameSelectedColor(int color){
		sFrame.setColor(color);
	}
	
	/**
	 * Set frame today object of background color
	 * @param color
	 */
	public void setTodayColor(int color){
		todayFrame.setColor(color);
	}
	
	/**
	 * set day of each objects
	 * @param day
	 */
	public void setDay(int day){
		this.dayNumber = day;
	}
	
	/**
	 * set day of week of each objects
	 * @param date
	 */
	public void setDate(int date){
		this.date = date;
	}
	
	public void setSelected(boolean state){
		this.isSelected = state;
	}
	
	public void setStateToday(boolean state){
		this.isToday = state;
	}
	
	/**
	 * to know the object whether it is in the end of the month or not
	 * @param state
	 */
	public void setStateEndOfMonth(boolean state){
		this.isEndOfMonth = state;
	}
	
	/**
	 * classify object type of time
	 * 
	 * OnViewCallbackListener.TIMETYPE_PREVIOUS -1
	 * OnViewCallbackListener.TIMETYPE_CURRENT 0
	 * OnViewCallbackListener.TIMETYPE_NEXT 1
	 * 
	 * @param timeType
	 */
	public void setTimeType(int timeType){
		this.timeType = timeType;
	}
		
	public void setOnViewCallbackListener(OnViewCallbackListener listener){
		this.listener = listener;
	}
	
	public void onTouchEvent(MotionEvent ev){
		float touchX = ev.getX();
		float touchY = ev.getY();
		
		if(listener != null){
			if(offsetX <= touchX && offsetX+width >= touchX && offsetY <= touchY && offsetY+height >= touchY){
				if(timeType == OnViewCallbackListener.TIMETYPE_CURRENT)
					listener.refreshSelectedDay(dayNumber, date);
				else if(timeType == OnViewCallbackListener.TIMETYPE_PREVIOUS)
					listener.movePrevMonthWithSelectedDay(dayNumber);
				else
					listener.moveNextMonthWithSelectedDay(dayNumber);
			}
		}
		
		
	}
	
	public interface OnViewCallbackListener{
		public static final int TIMETYPE_PREVIOUS = -1;
		public static final int TIMETYPE_CURRENT = 0;
		public static final int TIMETYPE_NEXT = 1;
		
		/**
		 * When day of previous month is tapped
		 * @param day selected day
		 */
		public void movePrevMonthWithSelectedDay(int day);
		
		/**
		 * When day of next month is tapped
		 * @param day selected day
		 */
		public void moveNextMonthWithSelectedDay(int day);
		
		/**
		 * When day of current month is tapped
		 * @param day selected day
		 */
		public void refreshSelectedDay(int day, int dayOfWeek);
	}
}

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
	private boolean isWeekend = false;
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
	
	public void setFrame(RectF frame){
		this.frame = frame;
		
		offsetX = (int)frame.left;
		offsetY = (int)frame.top;
		width = (int) frame.width();
		height = (int) frame.height();
		
		this.selectedFrame = new RectF(offsetX, offsetY, offsetX+width, offsetY+height);
	}
	
	public void setFrameColor(int color){
		pFrame.setColor(color);
	}
	
	public void setFrameSelectedColor(int color){
		sFrame.setColor(color);
	}
	
	public void setTodayColor(int color){
		todayFrame.setColor(color);
	}
	
	public void setDay(int day){
		this.dayNumber = day;
	}
	
	public void setDate(int date){
		this.date = date;
	}
	
	public void setSelected(boolean state){
		this.isSelected = state;
	}
	
	public void setStateToday(boolean state){
		this.isToday = state;
	}
	
	public void setStateWeekend(boolean state){
		this.isWeekend = state;
	}
	
	public void setStateEndOfMonth(boolean state){
		this.isEndOfMonth = state;
	}
	
	public void setTimeType(int timeType){
		this.timeType = timeType;
	}
		
	public void setOnViewCallbackListener(OnViewCallbackListener listener){
		this.listener = listener;
	}
	
	private String convertDateIntToString(int date){
		String str = "";
		
		switch(date){
		case Calendar.MONDAY:
			str = "월";
			break;
		case Calendar.TUESDAY:
			str = "화";
			break;
		case Calendar.WEDNESDAY:
			str = "수";
			break;
		case Calendar.THURSDAY:
			str = "목";
			break;
		case Calendar.FRIDAY:
			str = "금";
			break;
		case Calendar.SATURDAY:
			str = "토";
			break;
		case Calendar.SUNDAY:
			str = "일";
			break;
		}
		
		if(str.equals("")){
			System.err.println("INVALID INTEGER VALUE "+date);
		}
		
		return str;
	}
	
	public void onTouchEvent(MotionEvent ev){
		float touchX = ev.getX();
		float touchY = ev.getY();
		
		if(listener != null){
			if(offsetX <= touchX && offsetX+width >= touchX && offsetY <= touchY && offsetY+height >= touchY){
				if(timeType == OnViewCallbackListener.TIMETYPE_CURRENT)
					listener.refreshSelectedDay(dayNumber);
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
		public void refreshSelectedDay(int day);
	}
}

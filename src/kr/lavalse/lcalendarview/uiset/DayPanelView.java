package kr.lavalse.lcalendarview.uiset;

import java.util.Calendar;

import com.example.calendarlib.R;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import kr.lavalse.lcalendarview.uiset.DayObject.OnViewCallbackListener;

public class DayPanelView extends View implements OnViewCallbackListener{
	private static final int MAX_DAY_OF_WEEK = 7;
	
	private static final int NAVIGATING_PREV = -1;
	private static final int NAVIGATING_CURRENT = 0;
	private static final int NAVIGATING_NEXT = 1;
	private int touchMode = NAVIGATING_CURRENT;
	
	private LCalendarView parent;
	
	private Paint p;
	private Bitmap shadow;
	/*
	 * Variable for Date objects
	 */
	private DayObject[] prevMonthObj;
	private DayObject[] curMonthObj;
	private DayObject[] nextMonthObj;
	
	private int cDaySize = 0;
	private int pDaySize = 0;
	private int nDaySize = 0;
	
	/*
	 * Variable for Calendar datas
	 */
	private Calendar cal;
	private int startDayOfMonth = 0; // Start day of this month
	private int startDateOfWeekInMonth = 0; // Start week of this month
	private int startDateOfWeek = 0; // Start first day of the week of this month
	
	private int endDayOfMonth = 0; // End day of this month
	private int endDateOfWeekInMonth = 0; // End week of this month
	private int endDateOfWeek = 0; // End day of the week of this month
	private int startDayOfEndWeek = 0; // Start day of end week of this month
	
	private int pEndDayOfMonth = 0; // End day of previous month 
	
	/*
	 * Variable for a Panel Size
	 */
	private int screenWidth = 0;
	private int screenHeight = 0;
	
	private int objWidth = 0;
	private int objHeight = 0;
	
	/*
	 * Variable of View Callback
	 */
	
	private int selectedDay = 0;
	private int today = 0;
	private boolean isTodayMonth = false;
	
	public DayPanelView(LCalendarView parent, Calendar cal) {
		super(parent.getContext());
		
		this.parent = parent;
		
		p = new Paint();
		p.setAntiAlias(true);
		
		shadow = BitmapFactory.decodeResource(getResources(), R.drawable.bottom_shadow);
		setCalendar(cal);
	}
	
	public void setCalendar(Calendar cal){
		this.cal = cal;
		
		clearRectData(prevMonthObj);
		clearRectData(nextMonthObj);
		
		/*
		 *	Get datas of current month 
		 */
		startDayOfMonth = cal.getActualMinimum(Calendar.DAY_OF_MONTH);
		endDayOfMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
		
		startDateOfWeekInMonth = cal.getActualMinimum(Calendar.DAY_OF_WEEK_IN_MONTH);
		cal.set(Calendar.DATE, endDayOfMonth);
		endDateOfWeekInMonth = cal.get(Calendar.WEEK_OF_MONTH);
		
		cal.set(Calendar.DATE, startDayOfMonth);
		startDateOfWeek = cal.get(Calendar.DAY_OF_WEEK);
		
		cal.set(Calendar.DATE, endDayOfMonth);
		endDateOfWeek = cal.get(Calendar.DAY_OF_WEEK);
		
		startDayOfEndWeek = endDayOfMonth - endDateOfWeek + 1;
		
		cal.add(Calendar.MONTH, -1);
		pEndDayOfMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
		//System.out.println("PE: "+pEndDayOfMonth);
		
		cal.add(Calendar.MONTH, 1);
		
		System.out.println("S: "+startDayOfMonth+" E: "+endDayOfMonth+" SD: "+startDateOfWeekInMonth+" ED: "+endDateOfWeekInMonth);
		//System.out.println("SW: "+startDateOfWeek+" EW: "+endDateOfWeek+" SE: "+startDayOfEndWeek);
		
		/*
		 * Initialize each array size of DayObject 
		 */
		cDaySize = endDayOfMonth;		
		pDaySize = startDateOfWeek - 1;
		nDaySize = MAX_DAY_OF_WEEK - endDateOfWeek;
		
		System.out.println("P: "+pDaySize+" C: "+cDaySize+" N: "+nDaySize);
		
		curMonthObj = new DayObject[cDaySize];
		if(pDaySize != 0) prevMonthObj = new DayObject[pDaySize];
		nextMonthObj = new DayObject[nDaySize];

		objHeight = screenHeight / endDateOfWeekInMonth;
		
		invalidate();
	}
		
	@Override
	public void draw(Canvas canvas) {
		super.draw(canvas);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		int x=0, y=0, right=0, bottom = objHeight;
		int date = 1;
		RectF frame = new RectF();
		
		if(pDaySize != 0){
			//System.out.println("[Prev]");
			
			for(int i=0, start = pEndDayOfMonth-pDaySize+1; i<pDaySize; i++, start++, date++){
				right = x + objWidth;
				
				frame.set(x, y, right, bottom);
				
				prevMonthObj[i] = new DayObject(start, date);
				prevMonthObj[i].setFrame(frame);
				prevMonthObj[i].setFrameColor(Color.rgb(196, 201, 232));
				prevMonthObj[i].setOnViewCallbackListener(this);
				prevMonthObj[i].setTimeType(OnViewCallbackListener.TIMETYPE_PREVIOUS);
				prevMonthObj[i].draw(canvas);
				
				x = right;
			}
			
			//System.out.println();
		}
		
		//System.out.println("[Current]");
		
		for(int i=0, start = 1; i<cDaySize; i++, start++, date++){
			right = x + objWidth;
			
			frame.set(x, y, right, bottom + y);
			
			curMonthObj[i] = new DayObject(start, date);
			curMonthObj[i].setFrame(frame);
			curMonthObj[i].setFrameColor(Color.rgb(233, 255, 214));
			curMonthObj[i].setFrameSelectedColor(Color.RED);
			curMonthObj[i].setTimeType(OnViewCallbackListener.TIMETYPE_CURRENT);
			curMonthObj[i].setOnViewCallbackListener(this);
			curMonthObj[i].setTodayColor(Color.GREEN);
			if(isTodayMonth){
				curMonthObj[i].setTodayColor(Color.GREEN);
				if(start == today) curMonthObj[i].setStateToday(true);
			}
			if(start == selectedDay) curMonthObj[i].setSelected(true);
			if(start >= startDayOfEndWeek) curMonthObj[i].setStateEndOfMonth(true);
			curMonthObj[i].draw(canvas);
			
			if(date % 7 == 0){
				date = 0;
				x = 0;
				y += objHeight;
				
				//System.out.println();
			}else{
				x = right;
			}
		}
		
		//System.out.println();
		
		
		if(nDaySize != 0){
			//System.out.println("[Next]");
			
			for(int i=0, start = 1; i<nDaySize; i++, start++, date++){
				right = x + objWidth;
				
				frame.set(x, y, right, bottom + y);
				
				nextMonthObj[i] = new DayObject(start,date);
				nextMonthObj[i].setFrame(frame);
				nextMonthObj[i].setFrameColor(Color.rgb(196, 201, 232));
				nextMonthObj[i].setStateEndOfMonth(true);
				nextMonthObj[i].setTimeType(OnViewCallbackListener.TIMETYPE_NEXT);
				nextMonthObj[i].setOnViewCallbackListener(this);
				nextMonthObj[i].draw(canvas);
				
				x = right;
			}
			
			//System.out.println();
		}
		
		canvas.drawBitmap(shadow, null, new Rect(0, 0, screenWidth, shadow.getHeight()),  p);
	}
	
	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
		super.onLayout(changed, left, top, right, bottom);
		
		if(screenWidth == 0){
			screenWidth = right;
			screenHeight = bottom - top;
			//System.out.println("Screen W: "+screenWidth+" H: "+screenHeight);
			
			objWidth = screenWidth / MAX_DAY_OF_WEEK;
			objHeight = screenHeight / endDateOfWeekInMonth;
			//System.out.println("Obj W: "+objWidth+" H: "+objHeight);
		}
	}
	
	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		int action = event.getAction();
		
		if(action == MotionEvent.ACTION_DOWN){
			dispatchEachObject(prevMonthObj, event);
			
			if(touchMode != NAVIGATING_PREV){
				dispatchEachObject(curMonthObj, event);
				dispatchEachObject(nextMonthObj, event);
			}

			touchMode = NAVIGATING_CURRENT;
		}
		
		return true;
	}
	
	/**
	 * Assign each object(DayObject) to get a touch event
	 * It will occur when the user touch(ACTION_DOWN)
	 * See the function 'dispatchTouchEvent()' 
	 * @param objs
	 * @param event
	 */
	private void dispatchEachObject(DayObject[] objs, MotionEvent event){		
		for(DayObject obj : objs){
			obj.onTouchEvent(event);
		}
	}
	
	/**
	 * Selected Day by touch
	 * @param day
	 */
	public void setSelectedDay(int day){
		this.selectedDay = day;
	}
	
	public void setToday(int day){
		this.today = day;
	}
		
	public void setStateTodayMonth(boolean state){
		this.isTodayMonth = state;
	}

	@Override
	public void refreshSelectedDay(int day) {
		this.selectedDay = day;

		invalidate();
	}

	@Override
	public void movePrevMonthWithSelectedDay(int day) {		
		touchMode = NAVIGATING_PREV;
		
		this.selectedDay = day;
		parent.movePrev();
	}

	@Override
	public void moveNextMonthWithSelectedDay(int day) {
		touchMode = NAVIGATING_NEXT;
		
		this.selectedDay = day;
		parent.moveNext();
	}
	
	/**
	 * To prevent remaining datas of Rect, It would have to remove rect datas of DayObject
	 * @param objs
	 */
	private void clearRectData(DayObject[] objs){
		if(objs != null){
			for(DayObject obj : objs){
				obj.setFrame(new RectF());
				obj.setTimeType(OnViewCallbackListener.TIMETYPE_CURRENT);
			}
		}
	}
}

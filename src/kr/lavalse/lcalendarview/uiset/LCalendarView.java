package kr.lavalse.lcalendarview.uiset;

import java.util.Calendar;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import kr.lavalse.lcalendarview.uiset.DayPanelView.OnDaySelectedListener;

public class LCalendarView extends LinearLayout{
	/*
	 * Variable of Date
	 */
	private Calendar cal;
	private int todayYear = 0;
	private int todayMonth = 0;
	private int today = 0;
	
	private int currentYear = 0;
	private int currentMonth = 0;
		
	/*
	 * Variable of Related View
	 */
	private NavigationObjectView nav;
	private DatePanelView datePanel;
	private DayPanelView panel;
	
	public LCalendarView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}
	
	public LCalendarView(Context context, AttributeSet attrs){
		super(context, attrs);
		init(context);
	}
	
	public LCalendarView(Context context){
		super(context);
		init(context);
	}
	
	private void init(Context context){
		setOrientation(LinearLayout.VERTICAL);
		cal = Calendar.getInstance();
		
		currentYear = todayYear = cal.get(Calendar.YEAR);
		currentMonth = todayMonth = cal.get(Calendar.MONTH) + 1;
		today = cal.get(Calendar.DATE);
		
		nav = new NavigationObjectView(context);
		nav.setCurrentDate(cal.getTime());
		nav.setNextOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				panel.setSelectedDay(0);
				
				moveNext();
			}
		});
		
		nav.setPrevOnClickListener(new View.OnClickListener() {
			public void onClick(View v) { 
				panel.setSelectedDay(0);
				
				movePrev();
			}
		});
		
		datePanel = new DatePanelView(context);
		panel = new DayPanelView(this, cal);
		panel.setStateTodayMonth(true);
		panel.setToday(today);
		
		addView(nav, new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, 0, 1));
		LinearLayout.LayoutParams dlp = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, 0);
		dlp.weight = 0.5f;
		addView(datePanel, dlp);
		addView(panel, new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, 0, 8));
	}
	
	public void movePrev(){
		cal.add(Calendar.MONTH, -1);
		
		currentYear = cal.get(Calendar.YEAR);
		currentMonth = cal.get(Calendar.MONTH)+1;
		
		boolean isTodayMonth = compareTodayAndCurrentDate(currentYear, currentMonth);

		nav.setCurrentDate(cal.getTime());
		panel.setStateTodayMonth(isTodayMonth);
		panel.setCalendar(cal);
	}
	
	public void moveNext(){
		cal.add(Calendar.MONTH, 1);
		
		currentYear = cal.get(Calendar.YEAR);
		currentMonth = cal.get(Calendar.MONTH)+1;
		
		boolean isTodayMonth = compareTodayAndCurrentDate(currentYear, currentMonth);

		nav.setCurrentDate(cal.getTime());
		panel.setStateTodayMonth(isTodayMonth);
		panel.setCalendar(cal);
	}
	
	private boolean compareTodayAndCurrentDate(int year, int month){
		return (todayYear == year && todayMonth == month) ? true : false;
	}
	
	@Override
	public boolean performClick() {
		return super.performClick();
	}
	
	public void setOnDaySelectedListener(OnDaySelectedListener listener){
		panel.setOnDaySelectedListener(listener);
	}
	
	public int getCurrentYear(){
		return currentYear;
	}
	
	public int getCurrentMonth(){
		return currentMonth;
	}
}

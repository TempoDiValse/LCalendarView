package kr.lavalse.lcalendarview;

import com.example.calendarlib.R;

import android.app.Activity;
import android.os.Bundle;
import kr.lavalse.lcalendarview.uiset.DayPanelView.OnDaySelectedListener;
import kr.lavalse.lcalendarview.uiset.LCalendarView;

public class MainActivity extends Activity {
	private LCalendarView cView;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		cView = (LCalendarView)findViewById(R.id.cView);
		cView.setOnDaySelectedListener(new OnDaySelectedListener() {
			public void onDaySelected(int year, int month, int day, int dayOfWeek) {
				System.out.println("Y: "+year+" M: "+month+" D: "+day+" DW: "+dayOfWeek);
			}
		});
	}
}

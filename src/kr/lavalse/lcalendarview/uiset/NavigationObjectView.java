package kr.lavalse.lcalendarview.uiset;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.example.calendarlib.R;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;

public class NavigationObjectView extends LinearLayout{
	private ImageButton btnNext;
	private ImageButton btnPrev;
	
	private TextView txtMonthYear;
	private Date date;
	private SimpleDateFormat formatter;
	
	public NavigationObjectView(Context context) {
		super(context);
		
		init(context);
	}
	
	private void init(Context context){
		setOrientation(LinearLayout.HORIZONTAL);
		
		LinearLayout.LayoutParams blp = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1);
		blp.gravity = Gravity.CENTER_VERTICAL;
		
		LinearLayout.LayoutParams tlp = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 2);
		tlp.gravity = Gravity.CENTER;

		btnPrev = new ImageButton(context);
		btnPrev.setLayoutParams(blp);
		btnPrev.setScaleType(ScaleType.CENTER_INSIDE);
		btnPrev.setAdjustViewBounds(true);
		btnPrev.setBackgroundResource(0);
		btnPrev.setImageResource(R.drawable.ic_chevron_left_black_36dp);

		txtMonthYear = new TextView(context);
		txtMonthYear.setLayoutParams(tlp);
		txtMonthYear.setGravity(Gravity.CENTER);
		
		btnNext = new ImageButton(context);
		btnNext.setLayoutParams(blp);
		btnNext.setScaleType(ScaleType.CENTER_INSIDE);
		btnNext.setAdjustViewBounds(true);
		btnNext.setBackgroundResource(0);
		btnNext.setImageResource(R.drawable.ic_chevron_right_black_36dp);
		
		formatter = new SimpleDateFormat("yyyy³â MM¿ù");
		
		addView(btnPrev);
		addView(txtMonthYear);
		addView(btnNext);
	}
	
	public void setNextOnClickListener(View.OnClickListener listener){
		btnNext.setOnClickListener(listener);
	}
	
	public void setPrevOnClickListener(View.OnClickListener listener){
		btnPrev.setOnClickListener(listener);
	}
	
	public void setCurrentDate(Date date){
		this.date = date;
		
		displayDate(formatter.format(date));
	}
	
	public void setFormat(String format){
		formatter.applyPattern(format);
		
		displayDate(formatter.format(date));
	}
	
	private void displayDate(String dateStr){
		txtMonthYear.setText(dateStr);
	}
}

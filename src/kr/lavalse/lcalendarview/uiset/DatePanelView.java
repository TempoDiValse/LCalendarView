package kr.lavalse.lcalendarview.uiset;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

public class DatePanelView extends LinearLayout{
	
	private final static String[] dateTxt= {"일", "월", "화", "수", "목", "금", "토"};
	
	public DatePanelView(Context context) {
		super(context);
		
		setOrientation(LinearLayout.HORIZONTAL);
		
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0, LayoutParams.MATCH_PARENT, 1);
		
		TextView[] tvArr = new TextView[dateTxt.length];
		for(int i=0; i<dateTxt.length; i++){
			
			tvArr[i] = new TextView(context);
			tvArr[i].setLayoutParams(lp);
			tvArr[i].setGravity(Gravity.CENTER);
			
			if(i == 0) tvArr[i].setTextColor(Color.RED);
			else if(i == dateTxt.length - 1) tvArr[i].setTextColor(Color.BLUE);
			else tvArr[i].setTextColor(Color.rgb(155, 155, 155));
			
			tvArr[i].setText(dateTxt[i]);
			tvArr[i].setTextSize(11.0f);
			
			addView(tvArr[i]);
		}
	}
}

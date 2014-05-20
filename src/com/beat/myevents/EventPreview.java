package com.beat.myevents;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class EventPreview extends LinearLayout {
	private TextView title;
	private TextView day;
	private TextView month;
	private TextView year;
	private TextView rest;
	private TextView hour;
	private ImageView photo;

	public EventPreview(Context context) {
		super(context);
		initView(context);
		
		// TODO Auto-generated constructor stub
	}
	   private void initView(Context context) {
	        final LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	 
	        inflater.inflate(R.layout.eventpreview, this, true);
	 
	        title = (TextView) findViewById(R.id.prevTitle);
	        day = (TextView) findViewById(R.id.prevDay);
	        month=(TextView) findViewById(R.id.prevMonth);
	        year=(TextView) findViewById(R.id.prevYear);
	        rest=(TextView) findViewById(R.id.prevRest);
	        hour=(TextView) findViewById(R.id.prevHour);
	       
	    //    photo = (ImageView) findViewById(R.id.prevPhoto);
	    }
	    public void setTitle(CharSequence subject) {
	        title.setText(subject);
	    }
	    public void setDay(CharSequence subject) {
	        day.setText(subject);
	    }
	    public void setMonth(CharSequence subject) {
	        month.setText(subject);
	    }
	    public void setYear(CharSequence subject) {
	        year.setText(subject);
	    }
	    public void setRest(CharSequence subject) {
	        rest.setText(subject);
	    }
	    public void setHour(CharSequence subject) {
	        hour.setText(subject);
	    }
	    public void setPhoto(CharSequence subject){
	    	//photo.seti
	    }
	    
	    
}

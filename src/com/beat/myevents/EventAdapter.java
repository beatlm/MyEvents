package com.beat.myevents;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class EventAdapter extends BaseAdapter {
private Activity activity;
private ArrayList<Event> items;

public EventAdapter(Activity activity, ArrayList<Event>items){
	this.activity=activity;
	this.items=items;
}
	@Override
	public int getCount() {
		return items.size();
	}

	@Override
	public Object getItem(int arg0) {
		return items.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
	return items.get(arg0).getId();
	}

	@SuppressLint("ResourceAsColor")
	@Override
	public View getView(int pos, View convertView, ViewGroup arg2) {
		View v= convertView;
		if(convertView==null){
			LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v=inflater.inflate(R.layout.eventpreview,null);
		}
		Log.d("beatlm","getView pos:"+pos);
	Event e=items.get(pos);
	
	ImageView photo=(ImageView)v.findViewById(R.id.prevPhoto);
	photo.setImageDrawable(e.getPhoto());
	
	TextView hora=(TextView)v.findViewById(R.id.prevHour);
	hora.setText(e.getHour());
	
	
	TextView dia=(TextView)v.findViewById(R.id.prevDate);
	dia.setText(e.getFecha());
	
	TextView tit=(TextView)v.findViewById(R.id.prevTitle);
	tit.setText(e.getTitle());
	
	TextView prev=(TextView)v.findViewById(R.id.prevRest);
	prev.setText(e.getRest());
	
	//Si la fecha es anterior a hoy el fondo ser‡ rojo
 
	Date date = null;
	try {
		date = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH).parse(e.getFecha());
	} catch (ParseException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}
	Date hoy = new Date();

	if(hoy.after(date)){
		arg2.setBackgroundColor(R.color.red);
	v.setBackgroundColor(R.color.red);
	
	}
	return v;
	        
	}

}

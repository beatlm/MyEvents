package com.beat.myevents;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Cabecera extends LinearLayout {
	private TextView usuario;
	private String foto;

	public Cabecera(Context context) {
		super(context);
		initView(context);
	}
	public Cabecera(Context context, AttributeSet attrs) {
		  super(context, attrs);
		initView(context);
	}
	@SuppressLint("NewApi")
	public Cabecera(Context context,AttributeSet attrs, int defStyle) {
	    super(context, attrs, defStyle);
		initView(context);
	}
	
	private void initView(Context context) {
        final LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
 
        inflater.inflate(R.layout.cabecera, this, true);
        usuario = (TextView) findViewById(R.id.userNameCab);
	}



	public void setUsuario(String u) {
		usuario.setText(u);
	}

}

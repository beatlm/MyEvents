package com.beat.myevents;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.os.Build;

public class MenuActivity extends ActionBarActivity {//implements android.content.DialogInterface.OnClickListener{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_menu);

		

		// Obtenemos el nombre de usuario y la foto

		Intent intent = getIntent();
		String usuario = intent.getStringExtra(Constants.USERNAME);
		TextView t = (TextView) findViewById(R.id.userNameCab);
		t.setText(usuario);
		
		Button b=(Button)findViewById(R.id.btnAdd);
		/*b.setOnClickListener((OnClickListener) this);
		 b=(Button)findViewById(R.id.btnPref);
		b.setOnClickListener((OnClickListener) this);
		 b=(Button)findViewById(R.id.btnFav);
		b.setOnClickListener((OnClickListener) this);
		 b=(Button)findViewById(R.id.btnSearch);
		b.setOnClickListener((OnClickListener) this);*/
	}

	public void onClick(DialogInterface dialog, int which) {
		Log.d("beatlm","Boton:"+which);
		switch (which) {
		case 1:
			
			break;

		default:
			break;
		}
		
	}

	


}

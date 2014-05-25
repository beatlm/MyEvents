package com.beat.myevents;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;

public class PrefActivity extends ActionBarActivity {
private String usuario;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_pref);

		Intent intent = getIntent();
		usuario = intent.getStringExtra(Constants.USERNAME);
		Cabecera c= (Cabecera)findViewById(R.id.cabecera);
		c.setUsuario(usuario);
	}

	

}

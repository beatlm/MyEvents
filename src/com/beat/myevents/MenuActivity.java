package com.beat.myevents;

import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;


public class MenuActivity extends ActionBarActivity implements OnClickListener {
	private String usuario;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_menu);

		// Obtenemos el nombre de usuario y la foto

		Intent intent = getIntent();
		usuario = intent.getStringExtra(Constants.USERNAME);
	

		Button b = (Button) findViewById(R.id.btnAdd);
		b.setOnClickListener((OnClickListener) this);
		b = (Button) findViewById(R.id.btnPref);
		b.setOnClickListener((OnClickListener) this);
		b = (Button) findViewById(R.id.btnFav);
		b.setOnClickListener((OnClickListener) this);
		b = (Button) findViewById(R.id.btnSearch);
		b.setOnClickListener((OnClickListener) this);
		Cabecera c= (Cabecera)findViewById(R.id.cabecera);
		c.setUsuario(usuario);
	}

	@Override
	public void onClick(View v) {
		Intent intent = null;
		switch (v.getId()) {
		case R.id.btnAdd:
			intent = new Intent(this, CreateEventActivity.class);

			break;
		case R.id.btnFav:
			intent = new Intent(this, MyEvents.class);

			break;
		case R.id.btnPref:
			intent = new Intent(this, PrefActivity.class);
			break;
		case R.id.btnSearch:
			intent = new Intent(this, SearchActivity.class);
			break;

		}
		intent.putExtra(Constants.USERNAME, usuario);

		startActivity(intent);

	}

}

package com.beat.myevents;

import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;


public class MenuActivity extends ActionBarActivity implements OnClickListener {
	private String usuario;
	private String idUsu;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_menu);

		// Obtenemos el nombre de usuario y la foto

		Intent intent = getIntent();
		usuario = intent.getStringExtra(Constants.USERNAME);
		idUsu = intent.getStringExtra(Constants.IDUSU);
		Log.d("beatlm","IDUSU RECOGIDO MENU"+idUsu);
	

		Button b = (Button) findViewById(R.id.btnAdd);
		b.setOnClickListener((OnClickListener) this);
		b = (Button) findViewById(R.id.btnPref);
		b.setOnClickListener((OnClickListener) this);
		b = (Button) findViewById(R.id.btnFavo);
		b.setOnClickListener((OnClickListener) this);
		b = (Button) findViewById(R.id.btnOwn);
		b.setOnClickListener((OnClickListener) this);
		b = (Button) findViewById(R.id.btnLast);
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
		case R.id.btnFavo:
			intent = new Intent(this, MyEvents.class);
			intent.putExtra(Constants.DATA, Constants.FAV);
			break;
		case R.id.btnPref:
			intent = new Intent(this, PrefActivity.class);
			break;
		case R.id.btnSearch:
			intent = new Intent(this, SearchActivity.class);
			break;
		case R.id.btnLast:
			intent = new Intent(this, MyEvents.class);
			intent.putExtra(Constants.DATA, Constants.LAST);
			break;
		case R.id.btnOwn:
			intent = new Intent(this, OwnEvents.class);
			intent.putExtra(Constants.DATA, Constants.OWN);
			break;

		}
		intent.putExtra(Constants.USERNAME, usuario);
		intent.putExtra(Constants.IDUSU, idUsu);
		Log.d("beatlm","IDUSU DEVUELTO MENU"+idUsu);

		startActivity(intent);

	}

}

package com.beat.myevents;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.beat.lib.WebServices;

import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

public class SearchActivity extends ActionBarActivity implements
		OnClickListener {
	private String usuario;
	private String idUsu;
	private String fechaI;
	private String fechaF;
	private int pulsado;//Boton fecha pulsado
	private Spinner types;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_search);

		
		new TypesEvent().execute();
		
		Intent intent = getIntent();
		usuario = intent.getStringExtra(Constants.USERNAME);
		idUsu = intent.getStringExtra(Constants.IDUSU);
		Cabecera c = (Cabecera) findViewById(R.id.cabecera);
		c.setUsuario(usuario);

		Button b = (Button) findViewById(R.id.searchButton);
		b.setOnClickListener((OnClickListener) this);


	}

	@SuppressLint("NewApi")
	@Override
	public void onClick(View v) {
		Map<String, Object> data = new HashMap<String, Object>();
	
		EditText t = (EditText) findViewById(R.id.userSearch);
		String us = t.getText().toString();
	
		if(!us.trim().isEmpty()){
			data.put(Constants.USUARIO, us);
		}
		TextView time = (TextView) findViewById(R.id.datePickerI);
		if(time.getText()!=""){
			data.put(Constants.FECHAI, fechaI);
 
		}
		 time = (TextView) findViewById(R.id.datePickerF);
		if(time.getText()!=""){
			data.put(Constants.FECHAF, fechaF);
 
		}
	
	 
		new SearchEvent().execute(data);
	}

	private class SearchEvent extends
			AsyncTask<Map<String, Object>, View, Void> {

		private String Content;
		private String Error = null;
		private ProgressDialog Dialog = new ProgressDialog(SearchActivity.this);

		// String data ="";

		protected void onPreExecute() {

			Dialog.setMessage("Buscando eventos...");
			Dialog.show();

		}

		// Call after onPreExecute method
		@Override
		protected Void doInBackground(Map<String, Object>... data) {

			try {
				Content = WebServices.search(data[0]);
			} catch (Exception e) {
		
				e.printStackTrace();
			}
			return null;
		}

		protected void onPostExecute(Void unused) {

			Dialog.dismiss();

			if (Error != null) {
				Log.d("beatlm", "Error onpostexecute"); 

			} else {

				JSONObject jsonResponse;

				try {

					Intent intent = new Intent(SearchActivity.this,
							MyEvents.class);
					intent.putExtra(Constants.DATA, Content);
					intent.putExtra(Constants.IDUSU, idUsu);
					
					startActivity(intent);

				} catch (Exception ex) {
					Error = ex.getMessage();
				} finally {

				}

			}

		}
	}
	
	public void resetDates(View v){
		fechaI=null;
		fechaF=null;
		
		TextView dpI=(TextView)findViewById(R.id.datePickerI);
		TextView dpF=(TextView)findViewById(R.id.datePickerF);
		dpI.setText(R.string.date);
		dpF.setText(R.string.date);
		
	}

	public void showDatePickerDialog(View v) {
 
		DialogFragment newFragment = new DatePickerFragment();
		pulsado=v.getId();
		newFragment.show(getSupportFragmentManager(), "datePicker");
	}

	@SuppressLint("ValidFragment")
	private class DatePickerFragment extends DialogFragment implements
			DatePickerDialog.OnDateSetListener {

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			// Use the current date as the default date in the picker
			final Calendar c = Calendar.getInstance();
			int year = c.get(Calendar.YEAR);
			int month = c.get(Calendar.MONTH);
			int day = c.get(Calendar.DAY_OF_MONTH);

			return new DatePickerDialog(getActivity(), this, year, month, day);
		}

		public void onDateSet(DatePicker view, int year, int month, int day) {
			TextView time=null;
			month+=1;
			if(pulsado==R.id.datePickerIButton){
			 time = (TextView) findViewById(R.id.datePickerI);
			 
				fechaI=year+"/"+month+"/"+day;
			}else if(pulsado==R.id.datePickerFButton){
				 time = (TextView) findViewById(R.id.datePickerF);
					fechaF=year+"/"+month+"/"+day;
			}
			
			time.setText(day + "/" + month + "/" + year);
			
	
		}
	}
	
	
	
	private class TypesEvent extends
	AsyncTask<Map<String, Object>, View, Void> {

private String[] Content;/*
private String Error = null;*/
private ProgressDialog Dialog = new ProgressDialog(SearchActivity.this);

// String data ="";

protected void onPreExecute() {

/*	Dialog.setMessage("Buscando eventos...");
	Dialog.show();
*/
}

// Call after onPreExecute method
@Override
protected Void doInBackground(Map<String, Object>... data) {

	try {
		Content = WebServices.getTypes();
	} catch (Exception e) {

		e.printStackTrace();
	}
	return null;
}

protected void onPostExecute(Void unused) {

	Dialog.dismiss();

	ArrayAdapter<String> adaptador =
	        new ArrayAdapter<String>(SearchActivity.this,android.R.layout.simple_spinner_item, Content);
	
	 types = (Spinner)findViewById(R.id.tipos);
	adaptador.setDropDownViewResource(
	        android.R.layout.simple_spinner_dropdown_item);
	 
	types.setAdapter(adaptador);


	}

}
}




package com.beat.myevents;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;

import com.beat.lib.WebAddress;
import com.beat.lib.WebServices;

import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.util.Log;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class CreateEventActivity extends ActionBarActivity {

private double latitud;
private double longitud;
private String usuario;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_create_event);
		EditText e = (EditText) findViewById(R.id.createAddress);
		
		
		e.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (!hasFocus)
					validarAddress();
			}
		});
		
		Intent intent = getIntent();
		usuario = intent.getStringExtra(Constants.USERNAME);

	}

	public void createEvent(View view) {
		
 EditText edit;
		Map<String, Object> result = new HashMap<String, Object>();

		result.put(Constants.CREADOR, usuario);
		 

		edit = (EditText) findViewById(R.id.createAddress);
		String lugar = edit.getText().toString();
		 
		result.put(Constants.LUGAR, lugar);

		TextView tv = (TextView) findViewById(R.id.datePicker);
		String fecha = tv.getText().toString();
		tv = (TextView) findViewById(R.id.timePicker);
		 fecha = fecha+" "+tv.getText().toString();
	 
		result.put(Constants.FECHA, fecha);

		edit = (EditText) findViewById(R.id.createDesc);
		String descripcion = edit.getText().toString();
 
		result.put(Constants.DESCRIPCION, descripcion);
		result.put(Constants.LATITUD, latitud);
		result.put(Constants.LONGITUD, longitud);
		result.put(Constants.LIKES, 0);
		result.put(Constants.FOTO, "FOTO");
		result.put(Constants.ACTIVO, true);
 
		 
			Log.d("beatlm", "result:"+result);
			//WebServices.post(result);
			
			
			new CreateEvent().execute(result);
		 
	}

	public void showTimePickerDialog(View v) {

		DialogFragment newFragment = new TimePickerFragment();
		newFragment.show(getSupportFragmentManager(), "timePicker");
	}

	public void showDatePickerDialog(View v) {

		DialogFragment newFragment = new DatePickerFragment();
		newFragment.show(getSupportFragmentManager(), "datePicker");
	}

	@SuppressLint("ValidFragment")
	private class TimePickerFragment extends DialogFragment implements
			TimePickerDialog.OnTimeSetListener {

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			// Use the current time as the default values for the picker
			final Calendar c = Calendar.getInstance();
			int hour = c.get(Calendar.HOUR_OF_DAY);
			int minute = c.get(Calendar.MINUTE);

			// Create a new instance of TimePickerDialog and return it
			return new TimePickerDialog(getActivity(), this, hour, minute,
					DateFormat.is24HourFormat(getActivity()));
		}

		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

			TextView time = (TextView) findViewById(R.id.timePicker);
			time.setText(hourOfDay + ":" + minute);

		}
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
			TextView time = (TextView) findViewById(R.id.datePicker);
			time.setText(day + "-" + month + "-" + year);
		}
	}

	private void validarAddress() {

		EditText e = (EditText) findViewById(R.id.createAddress);
	String	strAddress = e.getText().toString();
	
	//Sustituimos los espacios por '+'
	Log.d("beatlm", "address "+strAddress);
	strAddress=strAddress.replaceAll("\\s", "+");
	strAddress=strAddress.replaceAll(",", "+");
	Log.d("beatlm", "address "+strAddress);
		if(strAddress!=null && strAddress.trim().length()>0){
			new ValidateAddress().execute(strAddress);
			
		}

	
	}

	private class ValidateAddress extends AsyncTask<String, Void, Void> {

		private String Error = null;
		private ProgressDialog Dialog = new ProgressDialog(
				CreateEventActivity.this);
		private double[] res;


		protected void onPreExecute() {

			Dialog.setMessage("Validando direcci—n..");
			Dialog.show();

		}

		protected Void doInBackground(String... urls) {

			try {
				res = WebAddress.getLocationFromString(urls[0]);
			} catch (JSONException e) {
				Error = "e";
			}
			return null;
		}

		protected void onPostExecute(Void unused) {

			Dialog.dismiss();

			if (Error != null) {
				Log.d("beatlm", "Error onpostexecute " + Error); // uiUpdate.setText("Output : "+Error);

			} else {

				TextView t = (TextView) findViewById(R.id.latitud);
				t.setText(Double.toString(res[0]));

				TextView t2 = (TextView) findViewById(R.id.longitud);
				t2.setText(Double.toString(res[1]));
				
				//Establecemos los valores para mandarlos a BBDD
				latitud=res[0];
				longitud=res[1];
			}

		}
	}
	
	
	private class CreateEvent extends AsyncTask<Map<String, Object>, Void, Void> {

		private String Error = null;
		private ProgressDialog Dialog = new ProgressDialog(
				CreateEventActivity.this);
	 private Map<String, Object> data;


		protected void onPreExecute() {

			Dialog.setMessage("Creando evento..");
			Dialog.show();

		}
/*
		public void execute(Map<String, Object> result) {
			// TODO Auto-generated method stub
			data=result;
		}*/

		@Override
		protected Void doInBackground(Map<String, Object>... params) {
			  try {
				WebServices.post(params[0]);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				Error=e.getMessage();
			}
			return null;
		}
		
	

		protected void onPostExecute(Void unused) {

			Dialog.dismiss();

			if (Error != null) {
				Log.d("beatlm", "Error onpostexecute " + Error); // uiUpdate.setText("Output : "+Error);

			} else {

				Toast toast = Toast.makeText(getApplicationContext(), "Evento creado con Žxito!",  Toast.LENGTH_SHORT);
				toast.show();
			}

		}

		
	}


}

package com.beat.myevents;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;

import com.beat.lib.WebAddress;
import com.beat.lib.WebServices;

import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.util.Base64;
import android.util.Log;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class CreateEventActivity extends ActionBarActivity {

	private double latitud;
	private double longitud;
	private String usuario;
	private String fechaInput;
	private Spinner types;
	private ImageView imageView;
	private boolean foto;
	private String idUsu;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_create_event);
		EditText e = (EditText) findViewById(R.id.createAddress);
		new TypesEvent().execute();

		foto = false;

		Intent intent = getIntent();
		usuario = intent.getStringExtra(Constants.USERNAME);
		idUsu = intent.getStringExtra(Constants.USERNAME);

		Cabecera c = (Cabecera) findViewById(R.id.cabecera);
		c.setUsuario(usuario);

		imageView = (ImageView) findViewById(R.id.createPhoto);


	}
	
	
	public void showMap(View view){
		
		Intent intent = new Intent(this, MapaActivity.class);
		intent.putExtra(Constants.IDUSU, idUsu);
	

	
	intent.putExtra(Constants.USERNAME, usuario);
	intent.putExtra(Constants.LATITUD, latitud);
	intent.putExtra(Constants.LONGITUD, longitud);
 

	startActivity(intent);
		
		
	}

	public void addPhoto(View view) {
		Log.d("beatlm", "Add photo");
		Intent i = new Intent(Intent.ACTION_PICK,
				android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

		startActivityForResult(i, Constants.RESULT_LOAD_IMAGE);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == Constants.RESULT_LOAD_IMAGE
				&& resultCode == RESULT_OK && null != data) {
			Uri selectedImage = data.getData();
			String[] filePathColumn = { MediaStore.Images.Media.DATA };

			Cursor cursor = getContentResolver().query(selectedImage,
					filePathColumn, null, null, null);
			cursor.moveToFirst();

			int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
			String picturePath = cursor.getString(columnIndex);
			cursor.close();

			imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
			foto = true;
		}

	}

	public void createEvent(View view) {

		EditText edit;
		Map<String, Object> result = new HashMap<String, Object>();

		result.put(Constants.CREADOR, usuario);

		result.put(Constants.PRECIO, 0);
		edit = (EditText) findViewById(R.id.createAddress);
		String lugar = edit.getText().toString();

		edit = (EditText) findViewById(R.id.createEventTitle);
		String titulo = edit.getText().toString();
		result.put(Constants.TITULO, titulo);
		result.put(Constants.LUGAR, lugar);

		TextView tv = (TextView) findViewById(R.id.timePicker);
		String hora = tv.getText().toString();

		result.put(Constants.FECHA, fechaInput + " " + hora);

		edit = (EditText) findViewById(R.id.createDesc);
		String descripcion = edit.getText().toString();

		result.put(Constants.DESCRIPCION, descripcion);
		result.put(Constants.LATITUD, latitud);
		result.put(Constants.LONGITUD, longitud);
		result.put(Constants.LIKES, 0);

		result.put(Constants.HASFOTO, foto);
		if (foto) {

			Bitmap bm = ((BitmapDrawable) imageView.getDrawable()).getBitmap();

			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			bm.compress(Bitmap.CompressFormat.PNG, 80, baos); // bm is the
																// bitmap object
			byte[] b = baos.toByteArray();

			String str = Base64.encodeToString(b, Base64.DEFAULT);

			result.put(Constants.FOTO, str);
		} else {

			result.put(Constants.FOTO, Constants.EMPTY);
		}

		Log.d("beatlm", "result:" + result);

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
			month++;
			time.setText(day + "/" + month + "/" + year);
			fechaInput = year + "-" + month + "-" + day;
		}
	}

	public void validate(View v) {

		EditText e = (EditText) findViewById(R.id.createAddress);
		String strAddress = e.getText().toString();

		// Sustituimos los espacios por '+'
		Log.d("beatlm", "address " + strAddress);
		strAddress = strAddress.replaceAll("\\s", "+");
		strAddress = strAddress.replaceAll(",", "+");
		Log.d("beatlm", "address " + strAddress);
		if (strAddress != null && strAddress.trim().length() > 0) {
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

				// Establecemos los valores para mandarlos a BBDD
				latitud = res[0];
				longitud = res[1];
				Log.d("beatlm","Latitud long:"+latitud+"-"+longitud);
			}

		}
	}

	private class CreateEvent extends
			AsyncTask<Map<String, Object>, Void, Void> {

		private String Error = null;
		private boolean Ok;
		private ProgressDialog Dialog = new ProgressDialog(
				CreateEventActivity.this);
		private Map<String, Object> data;

		protected void onPreExecute() {

			Dialog.setMessage("Creando evento..");
			Dialog.show();

		}

		/*
		 * public void execute(Map<String, Object> result) { // TODO
		 * Auto-generated method stub data=result; }
		 */

		@Override
		protected Void doInBackground(Map<String, Object>... params) {
			try {
				Ok = WebServices.post(params[0]);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				Error = e.getMessage();
			}
			return null;
		}

		protected void onPostExecute(Void unused) {

			Dialog.dismiss();

			if (Error != null || !Ok) {
				Log.d("beatlm", "Error onpostexecute " + Error); // uiUpdate.setText("Output : "+Error);
				Toast toast = Toast
						.makeText(
								getApplicationContext(),
								"No se  ha podido crear el evento, por favor vuelve a intentarlo",
								Toast.LENGTH_LONG);
				toast.show();

			} else {

				Toast toast = Toast.makeText(getApplicationContext(),
						"Evento creado con Žxito!", Toast.LENGTH_LONG);
				toast.show();

				Intent intent = new Intent(CreateEventActivity.this,
						MyEvents.class);
				intent.putExtra(Constants.USERNAME, usuario);
				intent.putExtra(Constants.DATA, Constants.OWN);

				startActivity(intent);
			}

		}

	}

	private class TypesEvent extends AsyncTask<Map<String, Object>, View, Void> {

		private String[] Content;/*
								 * private String Error = null;
								 */
		private ProgressDialog Dialog = new ProgressDialog(
				CreateEventActivity.this);

		// String data ="";

		protected void onPreExecute() {

			/*
			 * Dialog.setMessage("Buscando eventos..."); Dialog.show();
			 */
		}

		// Call after onPreExecute method
		@Override
		protected Void doInBackground(Map<String, Object>... data) {

			try {
				Content = WebServices.getTypes();
			} catch (Exception e) {
				Content=new String[1];
Content[0]="Ninguno";
				e.printStackTrace();
			}
			return null;
		}

		protected void onPostExecute(Void unused) {

			Dialog.dismiss();
if(Content==null){
	Content[0]="Ninguno";
}
			ArrayAdapter<String> adaptador = new ArrayAdapter<String>(
					CreateEventActivity.this,
					android.R.layout.simple_spinner_item, Content);

			types = (Spinner) findViewById(R.id.tipo);
			adaptador
					.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

			types.setAdapter(adaptador);

		}

	}
}

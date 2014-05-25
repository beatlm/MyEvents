package com.beat.myevents;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MyEvents extends ActionBarActivity {
	private String usuario;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_my_events);

		// Obtenemos el nombre de usuario y la foto

		Intent intent = getIntent();
		usuario = intent.getStringExtra(Constants.USERNAME);
		Cabecera c= (Cabecera)findViewById(R.id.cabecera);
		c.setUsuario(usuario);

		// Obtenemos los eventos del usuario

		String serverURL = Constants.GET_OWN_EVENTS;

		// Use AsyncTask execute Method To Prevent ANR Problem
		new GetMyEvents().execute(serverURL);

	}
	
	public void goToCreateEvent(View v){
		
		
		 Intent intent=new Intent(this, CreateEventActivity.class);
	    intent.putExtra(Constants.USERNAME, usuario);
	
	     startActivity(intent);
		
		
	}


	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_my_events,
					container, false);
			return rootView;
		}	
	}
	

	private class GetMyEvents extends AsyncTask<String, View, Void> {

		private String Content;
		private String Error = null;
		private ProgressDialog Dialog = new ProgressDialog(MyEvents.this);

		// String data ="";

		protected void onPreExecute() {

			Dialog.setMessage("Cargando eventos...");
			Dialog.show();

		}

		// Call after onPreExecute method
		protected Void doInBackground(String... urls) {

			BufferedReader reader = null;

			try {

				// Defined URL where to send data
				URL url = new URL(urls[0]);

				URLConnection conn = url.openConnection();
				conn.setDoOutput(true);
				OutputStreamWriter wr = new OutputStreamWriter(
						conn.getOutputStream());

				String data = URLEncoder.encode(Constants.CREADOR, "UTF-8")
						+ "=" + URLEncoder.encode(usuario, "UTF-8");
				wr.write(data);

				wr.flush();

				// Get the server response

				reader = new BufferedReader(new InputStreamReader(
						conn.getInputStream()));
				StringBuilder sb = new StringBuilder();
				String line = null;

				// Read Server Response
				while ((line = reader.readLine()) != null) {
					// Append server response in string
					sb.append(line + " ");
				}

				// Append Server Response To Content String
				Content = sb.toString();
			} catch (Exception ex) {
				Error = ex.getMessage();
			} finally {
				try {
					reader.close();
				}
				catch (Exception ex) {
				}
			}
			return null;
		}

		protected void onPostExecute(Void unused) {

			Dialog.dismiss();

			if (Error != null) {
				Log.d("beatlm", "Error onpostexecute"); // uiUpdate.setText("Output : "+Error);

			} else {

				JSONObject jsonResponse;

				try {

					jsonResponse = new JSONObject(Content);

					JSONArray jsonMainNode = jsonResponse
							.optJSONArray("Events");

					int lengthJsonArr = jsonMainNode.length();

					LinearLayout lo = (LinearLayout) findViewById(R.id.myEventsList);
					for (int i = 0; i < lengthJsonArr; i++) {

						JSONObject jsonChildNode = jsonMainNode
								.getJSONObject(i);

						String fecha = jsonChildNode.optString("fecha")
								.toString();
						String lugar = jsonChildNode.optString("lugar")
								.toString();
						String titulo = jsonChildNode.optString("titulo")
								.toString();

						Log.d("beatlm", "FECHA: " + fecha + " LUGAR: " + lugar + "TITULO" + titulo);

						
						// AQUI RECOGEMOS LOS DATOS Y LOS PASAMOS A LA PANTALLA
						EventPreview ep = new EventPreview(MyEvents.this);
						ep.setTitle(titulo);
						ep.setId(i+5000);
						
						
						Log.d("beatlm", "TITULO:" + titulo + " FECHA:" + fecha+" ID:"+i);

						// Calculamos los d’as que quedan

						StringTokenizer st = new StringTokenizer(fecha, "-");
						String year = st.nextToken();
						String month = st.nextToken();
						String tmp = st.nextToken();
						st = new StringTokenizer(tmp);
						String day = st.nextToken();
						String hora = st.nextToken();

						Calendar hoy = Calendar.getInstance();
						Calendar f = Calendar.getInstance();

						f.set(Integer.parseInt(year),
								Integer.parseInt(month) - 1,
								Integer.parseInt(day));

						long dif = (f.getTimeInMillis() - hoy.getTimeInMillis())
								/ Constants.MILLSECS_PER_DAY;

						Log.d("beatlm", "DAY:" + day + " MONTH:" + month
								+ " YEAR:" + year + " HORA:" + hora);
						ep.setDay(day + "/");
						ep.setMonth(month + "/");
						ep.setYear(year);
						ep.setRest("Quedan " + dif + " d’as");
						ep.setHour(hora);
						lo.addView(ep);
						

					}

				} catch (JSONException e) {

					e.printStackTrace();
				}

			}
		}

	}

	public void showEvent(View v) {

		// v.getDisplay().getDisplayId();
		Log.d("beatlm", "SE ha pulsado en el evento:"+v.getId());

	}
}

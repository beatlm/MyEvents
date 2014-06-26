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
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MyEvents extends ActionBarActivity implements OnItemClickListener {
	private String usuario;
	private ListView lo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_my_events);
		lo = (ListView) findViewById(R.id.myEventsList);
		// Obtenemos el nombre de usuario y la foto

		Intent intent = getIntent();
		usuario = intent.getStringExtra(Constants.USERNAME);
		Cabecera c = (Cabecera) findViewById(R.id.cabecera);
		c.setUsuario(usuario);

		// Mostramos los datos obtenidos del buscador
		String data = intent.getStringExtra(Constants.DATA);
		if (data != null) {
			mostrarDatos(data);

		} else {
			// Obtenemos los eventos del usuario
			String serverURL = Constants.GET_OWN_EVENTS;

			new GetMyEvents().execute(serverURL);
			lo.setOnItemClickListener(this);
		}

	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long id) {

		Log.d("beatlm", "id+" + id);
		Intent intent = new Intent(this, DetailActivity.class);
		intent.putExtra(Constants.USERNAME, usuario);
		intent.putExtra(Constants.ID, id);
		startActivity(intent);

	}

	public void goToCreateEvent(View v) {

		Intent intent = new Intent(this, CreateEventActivity.class);
		intent.putExtra(Constants.USERNAME, usuario);

		startActivity(intent);

	}

	public void mostrarDatos(String datos) {
		JSONObject jsonResponse;

		try {
			Log.d("beatlm", "Content: " + datos);
			jsonResponse = new JSONObject(datos);

			JSONArray jsonMainNode = jsonResponse.optJSONArray("Events");

			int lengthJsonArr = jsonMainNode.length();
			Log.d("beatlm", "lengthJsonArr: " + lengthJsonArr);
			if (lengthJsonArr > 0) {
				ArrayList<Event> data = new ArrayList<Event>();

				for (int i = 0; i < lengthJsonArr; i++) {

					JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);

					String fecha = jsonChildNode.optString("fecha").toString();
					String lugar = jsonChildNode.optString("lugar").toString();
					String precio = jsonChildNode.optString("precio").toString();
					String titulo = jsonChildNode.optString("titulo")
							.toString();
					long id = Long.parseLong(jsonChildNode.optString("id"));

					Log.d("beatlm", "FECHA: " + fecha + " LUGAR: " + lugar
							+ "TITULO" + titulo);

					// Calculamos los d’as que quedan

					StringTokenizer st = new StringTokenizer(fecha, "-");
					String year = st.nextToken();
					String month = st.nextToken();
					String tmp = st.nextToken();
					st = new StringTokenizer(tmp);
					String day = st.nextToken();
					String hora = st.nextToken();
					String fec = day + "/" + month + "/" + year;

					Calendar hoy = Calendar.getInstance();
					Calendar f = Calendar.getInstance();

					f.set(Integer.parseInt(year), Integer.parseInt(month) - 1,
							Integer.parseInt(day));

					long dif = (f.getTimeInMillis() - hoy.getTimeInMillis())
							/ Constants.MILLSECS_PER_DAY;

					Log.d("beatlm", "DAY:" + day + " MONTH:" + month + " YEAR:"
							+ year + " HORA:" + hora);

					Event ev = new Event(id, titulo, fec, hora, "Quedan " + dif
							+ " d’as", null, precio);
					data.add(ev);
					Log.d("beatlm", "data size+" + data.size());
					EventAdapter adapter = new EventAdapter(MyEvents.this, data);

					lo.setAdapter(adapter);

				}
			} else {// No hay datos
				TextView t = new TextView(this);
				t.setText("No hay eventos");
				RelativeLayout rel=(RelativeLayout)findViewById(R.id.myEvents);
				rel.addView(t);

			}

		} catch (JSONException e) {

			e.printStackTrace();
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
				} catch (Exception ex) {
				}
			}
			return null;
		}

		protected void onPostExecute(Void unused) {

			Dialog.dismiss();

			if (Error != null) {
				Log.d("beatlm", "Error onpostexecute"); // uiUpdate.setText("Output : "+Error);

			} else {
				mostrarDatos(Content);
				/*
				 * JSONObject jsonResponse;
				 * 
				 * try { Log.d("beatlm", "Content: " + Content); jsonResponse =
				 * new JSONObject(Content);
				 * 
				 * JSONArray jsonMainNode = jsonResponse
				 * .optJSONArray("Events");
				 * 
				 * int lengthJsonArr = jsonMainNode.length(); Log.d("beatlm",
				 * "lengthJsonArr: " + lengthJsonArr);
				 * 
				 * ArrayList<Event> data = new ArrayList<Event>();
				 * 
				 * for (int i = 0; i < lengthJsonArr; i++) {
				 * 
				 * JSONObject jsonChildNode = jsonMainNode .getJSONObject(i);
				 * 
				 * String fecha = jsonChildNode.optString("fecha") .toString();
				 * String lugar = jsonChildNode.optString("lugar") .toString();
				 * String titulo = jsonChildNode.optString("titulo")
				 * .toString(); long id =
				 * Long.parseLong(jsonChildNode.optString("id"));
				 * 
				 * Log.d("beatlm", "FECHA: " + fecha + " LUGAR: " + lugar +
				 * "TITULO" + titulo);
				 * 
				 * // Calculamos los d’as que quedan
				 * 
				 * StringTokenizer st = new StringTokenizer(fecha, "-"); String
				 * year = st.nextToken(); String month = st.nextToken(); String
				 * tmp = st.nextToken(); st = new StringTokenizer(tmp); String
				 * day = st.nextToken(); String hora = st.nextToken(); String
				 * fec = day + "/" + month + "/" + year;
				 * 
				 * Calendar hoy = Calendar.getInstance(); Calendar f =
				 * Calendar.getInstance();
				 * 
				 * f.set(Integer.parseInt(year), Integer.parseInt(month) - 1,
				 * Integer.parseInt(day));
				 * 
				 * long dif = (f.getTimeInMillis() - hoy.getTimeInMillis()) /
				 * Constants.MILLSECS_PER_DAY;
				 * 
				 * Log.d("beatlm", "DAY:" + day + " MONTH:" + month + " YEAR:" +
				 * year + " HORA:" + hora);
				 * 
				 * Event ev = new Event(id, titulo, fec, hora, "Quedan " + dif +
				 * " d’as", null); data.add(ev);
				 * 
				 * }
				 * 
				 * Log.d("beatlm", "data size+" + data.size()); EventAdapter
				 * adapter = new EventAdapter(MyEvents.this, data);
				 * 
				 * lo.setAdapter(adapter);
				 * 
				 * } catch (JSONException e) {
				 * 
				 * e.printStackTrace(); }
				 */

			}
		}

	}

}

package com.beat.myevents;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.beat.lib.WebServices;

import android.support.v7.app.ActionBarActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class OwnEvents extends ActionBarActivity implements OnItemClickListener {
	private String usuario;
	private String idUsu;
	private ListView lo;
	private MyEventAdapter adapter;
	private String operacion;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_my_events);
		lo = (ListView) findViewById(R.id.myEventsList);

		Intent intent = getIntent();
		usuario = intent.getStringExtra(Constants.USERNAME);
		idUsu = intent.getStringExtra(Constants.IDUSU);
		Cabecera c = (Cabecera) findViewById(R.id.cabecera);
		c.setUsuario(usuario);

		lo.setOnItemClickListener(this);

		Map<String, Object> data = new HashMap<String, Object>();
		data.put(Constants.USUARIO, usuario);
		c.setZona(Constants.ZONAOWN);
		operacion = Constants.BUSCAR;
		new GetMyEvents().execute(data);

	}

	public void delete(View v) {
		long index = (Long) v.getTag();
		Map<String, Object> data = new HashMap<String, Object>();
		data.put(Constants.ID, index);
		operacion = Constants.DELETE;
		new GetMyEvents().execute(data);

		Log.d("beatlm", "delete " + index);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long id) {

		Log.d("beatlm", "id+" + id);
		Intent intent = new Intent(this, DetailActivity.class);
		intent.putExtra(Constants.USERNAME, usuario);
		intent.putExtra(Constants.ID, id);
		intent.putExtra(Constants.IDUSU, idUsu);
		Log.d("beatlm", "IDUSU MYEVENTS" + idUsu);
		startActivity(intent);

	}

	public void goToCreateEvent(View v) {

		Intent intent = new Intent(this, CreateEventActivity.class);
		intent.putExtra(Constants.USERNAME, usuario);
		intent.putExtra(Constants.IDUSU, idUsu);

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
					String precio = jsonChildNode.optString("precio")
							.toString();
					String titulo = jsonChildNode.optString("titulo")
							.toString();
					long id = Long.parseLong(jsonChildNode.optString("id"));

					Log.d("beatlm", "FECHA: " + fecha + " LUGAR: " + lugar
							+ "TITULO" + titulo);

					// Calculamos los días que quedan

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
							+ " días", null, precio);
					data.add(ev);
					Log.d("beatlm", "data size+" + data.size());
					adapter = new MyEventAdapter(OwnEvents.this, data);

					lo.setAdapter(adapter);

				}
			} else {// No hay datos
				TextView t = new TextView(this);
				t.setText("No hay eventos");

				RelativeLayout rel = (RelativeLayout) findViewById(R.id.myEvents);
				rel.addView(t);

			}

		} catch (JSONException e) {

			e.printStackTrace();
		}

	}

	private class GetMyEvents extends
			AsyncTask<Map<String, Object>, View, Void> {

		private String Content;
		private String Error = null;
		private ProgressDialog Dialog = new ProgressDialog(OwnEvents.this);
		private boolean delete;

		protected void onPreExecute() {
			Dialog.setMessage("Cargando eventos...");
			Dialog.show();
		}

		protected Void doInBackground(Map<String, Object>... data) {

			try {
				if (operacion.equals(Constants.BUSCAR)) {
					Content = WebServices.search(data[0]);
				} else {
					delete = WebServices.delete(data[0]);
				}

			} catch (Exception e) {

				e.printStackTrace();
			}
			return null;
		}

		protected void onPostExecute(Void unused) {
			Toast t;
			Dialog.dismiss();
			Log.d("beatlm", "op:" + operacion);
			if (operacion.equals(Constants.BUSCAR)) {// Acabamos la busqueda
				if (Error != null) {
					t = Toast
							.makeText(
									OwnEvents.this,
									"No se ha podido conectar con el servidor, inténtalo más tarde",
									Toast.LENGTH_LONG);
					Log.d("beatlm", "Error onpostexecute"); // uiUpdate.setText("Output : "+Error);
					t.show();
				} else {
					mostrarDatos(Content);

				}
			} else {// Hemos borrado el evento

				if (!delete) {
					t = Toast.makeText(OwnEvents.this,
							"Se ha borrado el evento", Toast.LENGTH_LONG);

					Intent intent = new Intent(OwnEvents.this, OwnEvents.class);
					intent.putExtra(Constants.DATA, Constants.OWN);

					intent.putExtra(Constants.USERNAME, usuario);
					intent.putExtra(Constants.IDUSU, idUsu);

					startActivity(intent);

				} else {
					t = Toast.makeText(OwnEvents.this,
							"NO Se ha borrado el evento", Toast.LENGTH_LONG);

				}
				t.show();
			}
		}

	}

}

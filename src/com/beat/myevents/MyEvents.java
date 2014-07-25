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
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MyEvents extends ActionBarActivity implements OnItemClickListener {
	private String usuario;
	private String idUsu;
	private ListView lo;
	private String searchType;

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

		//En searchType vendran los resultados de una busqueda anterior, o el tipo de busqueda
		 searchType = intent.getStringExtra(Constants.DATA);
			lo.setOnItemClickListener(this);
		if(searchType.equals(Constants.FAV)){

			Map<String, Object> data = new HashMap<String, Object>();
			data.put(Constants.CREADOR, usuario);
			c.setZona(Constants.ZONAFAV);
			new GetMyEvents().execute(data);
			
		
			
		}else if(searchType.equals(Constants.LAST)){
			Map<String, Object> data = new HashMap<String, Object>();
			c.setZona(Constants.ZONALAST);
			new GetMyEvents().execute(data);
		}else if(searchType != null) {
			c.setZona(Constants.ZONASEARCH);
			mostrarDatos(searchType);

		}  

	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long id) {

		Log.d("beatlm", "id+" + id);
		Intent intent = new Intent(this, DetailActivity.class);
		intent.putExtra(Constants.USERNAME, usuario);
		intent.putExtra(Constants.ID, id);
		intent.putExtra(Constants.IDUSU, idUsu);
		Log.d("beatlm","IDUSU MYEVENTS"+idUsu);
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
					String precio = jsonChildNode.optString("precio").toString();
					String titulo = jsonChildNode.optString("titulo")
							.toString();
					int estado=jsonChildNode.optInt("estado");
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
					ev.setEstado(estado);
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

	private class GetMyEvents extends AsyncTask<Map<String, Object>, View, Void> {

		private String Content;
		private String Error = null;
		private ProgressDialog Dialog = new ProgressDialog(MyEvents.this);


		protected void onPreExecute() {
			Dialog.setMessage("Cargando eventos...");
			Dialog.show();
		}

		protected Void doInBackground(Map<String, Object>... data)  {

			try {
				if(searchType.equals(Constants.FAV)){
					Content = WebServices.favourites(data[0]);
				}else if(searchType.equals(Constants.LAST)){
					Content = WebServices.last(data[0]);
				}else{
				Content = WebServices.search(data[0]);
				}
				
			} catch (Exception e) {
		
				e.printStackTrace();
			}
			return null;
		}

		protected void onPostExecute(Void unused) {

			Dialog.dismiss();

			if (Error != null) {
				Log.d("beatlm", "Error onpostexecute"); // uiUpdate.setText("Output : "+Error);

			} else {
				mostrarDatos(Content);
				
			}
		}

	}
	
	   public boolean onKeyDown(int keyCode, KeyEvent event) 
       {
           if(keyCode == KeyEvent.KEYCODE_BACK)
           {
        	   Log.d("beatlm","Se ha pulsado BACK");
        	   Intent intent = new Intent(MyEvents.this, MenuActivity.class);    
        		intent.putExtra(Constants.USERNAME, usuario);
        		intent.putExtra(Constants.IDUSU, idUsu);
               startActivity(intent);          
               finish();
               return true;
           }
           return false;
       }

}

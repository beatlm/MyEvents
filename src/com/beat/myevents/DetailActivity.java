package com.beat.myevents;

 

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.StringTokenizer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.os.Build;

public class DetailActivity extends ActionBarActivity {
private String usuario;
private Long id;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_detail);

		
		Intent intent = getIntent();
		usuario = intent.getStringExtra(Constants.USERNAME);
		 id=intent.getLongExtra(Constants.ID, 0);
		Cabecera c= (Cabecera)findViewById(R.id.cabecera);
		c.setUsuario(usuario);

		// Obtenemos los eventos del usuario

		String serverURL = Constants.GET_DETAIL;

		// Use AsyncTask execute Method To Prevent ANR Problem
		new GetDetail().execute(serverURL);
		 
	 
	}

	private class GetDetail extends AsyncTask<String, View, Void> {

		private String Content;
		private String Error = null;
		private ProgressDialog Dialog = new ProgressDialog(DetailActivity.this);

		// String data ="";

		protected void onPreExecute() {

			Dialog.setMessage("Cargando datos...");
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

				String data = URLEncoder.encode(Constants.ID, "UTF-8")
						+ "=" + URLEncoder.encode(id.toString(), "UTF-8");
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
Log.d("beatlm","Content: "+Content);
					jsonResponse = new JSONObject(Content);

					JSONArray jsonMainNode = jsonResponse
							.optJSONArray("Events");

					int lengthJsonArr = jsonMainNode.length();
					Log.d("beatlm","lengthJsonArr: "+lengthJsonArr);
					
					ArrayList<Event> data = new ArrayList<Event>();

					for (int i = 0; i < lengthJsonArr; i++) {

						JSONObject jsonChildNode = jsonMainNode
								.getJSONObject(i);

						String fecha = jsonChildNode.optString("fecha")
								.toString();
						String lugar = jsonChildNode.optString("lugar")
								.toString();
						String titulo = jsonChildNode.optString("titulo")
								.toString();
						long id= Long.parseLong(jsonChildNode.optString("id"));

						String descripcion = jsonChildNode.optString("descripcion")
								.toString();
						
						String precio = jsonChildNode.optString("precio")
								.toString();
						String likes = jsonChildNode.optString("likes")
								.toString();
						
						Log.d("beatlm", "FECHA: " + fecha + " LUGAR: " + lugar + "TITULO" + titulo);

						TextView t=(TextView)findViewById(R.id.detailTitle);
						t.setText(titulo);
						
						t=(TextView)findViewById(R.id.detailAddress);
						t.setText(lugar);
						
						t=(TextView)findViewById(R.id.detailDate);
						t.setText(fecha);
						
						t=(TextView)findViewById(R.id.detailDesc);
						t.setText(descripcion);
						
						t=(TextView)findViewById(R.id.detailPrice);
						t.setText(precio);
						
						t=(TextView)findViewById(R.id.detailLikes);
						t.setText(likes);
						
						


					

					}

			
					
				        
				} catch (JSONException e) {

					e.printStackTrace();
				}

			}
		}

	}
 

}

package com.beat.myevents;

import java.io.ByteArrayInputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.beat.lib.WebServices;

import android.support.v7.app.ActionBarActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class DetailActivity extends ActionBarActivity {
	private String usuario;
	private String idUsu;
	private Button btnFav;
 private int fav;
	private Long id;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_detail);

		btnFav=(Button)findViewById(R.id.buttonFav);
		Intent intent = getIntent();
		usuario = intent.getStringExtra(Constants.USERNAME);
		id = intent.getLongExtra(Constants.ID, 0);
		idUsu  = intent.getStringExtra(Constants.IDUSU);
		Cabecera c = (Cabecera) findViewById(R.id.cabecera);
		c.setUsuario(usuario);

		// Obtenemos los eventos del usuario
 

		// Use AsyncTask execute Method To Prevent ANR Problem
		
		Map<String, Object> data = new HashMap<String, Object>();
		data.put(Constants.ID, id);
		data.put(Constants.IDUSU, idUsu);
		
		new GetDetail().execute(data);

	}
	
	public void setFavorito(View v){
		Map<String, Object> data = new HashMap<String, Object>();
		data.put(Constants.IDEVENT, id);
		data.put(Constants.IDUSU, idUsu);
		new SetFav().execute(data);
		
	}
	
	
	private class SetFav extends AsyncTask<	Map<String, Object> , View, Void> {

		private String Content;
		private String Error = null;
		private ProgressDialog Dialog = new ProgressDialog(DetailActivity.this);
		private boolean Ok;
		// String data ="";

		protected void onPreExecute() {

		//	Dialog.setMessage("Convirtiendo en favorito...");
			//Dialog.show();

		}



		protected Void doInBackground(	Map<String, Object> ... data) {

			 

			try {
				if(fav==1){//Si ya es favorito lo eliminamos
				
				Ok=WebServices.unsetFav(data[0]);
				}else{
					
					Ok=WebServices.setFav(data[0]);
				}
			
			} catch (Exception ex) {
				Error = ex.getMessage();
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
								"No se  ha podido establecer como favorito.",
								Toast.LENGTH_LONG);
				toast.show();

			} else {
				if(fav==1){
				btnFav.setBackgroundResource(R.drawable.fav);
				fav=0;
				}else{
					btnFav.setBackgroundResource(R.drawable.favset);
					fav=1;
				}

				
			
			}
		}

	}


	private class GetDetail extends AsyncTask<Map<String, Object>, View, Void> {

		private String Content;
		private String Error = null;
		private ProgressDialog Dialog = new ProgressDialog(DetailActivity.this);

		// String data ="";

		protected void onPreExecute() {

			Dialog.setMessage("Cargando datos...");
			Dialog.show();

		}

		// Call after onPreExecute method
		protected Void doInBackground(Map<String, Object>... data) {

		

			try {
Content=WebServices.detail(data[0]);
				
			} catch (Exception ex) {
				Error = ex.getMessage();
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
					Log.d("beatlm", "Content: " + Content);
					jsonResponse = new JSONObject(Content);

					JSONArray jsonMainNode = jsonResponse
							.optJSONArray("Events");

					int lengthJsonArr = jsonMainNode.length();
					Log.d("beatlm", "lengthJsonArr: " + lengthJsonArr);

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
						 fav = jsonChildNode.optInt(Constants.FAV);
								
						long id = Long.parseLong(jsonChildNode.optString("id"));

						String descripcion = jsonChildNode.optString(
								"descripcion").toString();

						String precio = jsonChildNode.optString("precio")
								.toString();
						String likes = jsonChildNode.optString("likes")
								.toString();
						String foto = jsonChildNode.optString("foto")
								.toString();
						boolean hasFoto = jsonChildNode
								.optBoolean(Constants.HASFOTO);

						ImageView iV = (ImageView) findViewById(R.id.detailPhoto);
						if (hasFoto) {

							Log.d("beatlm", "FECHA: " + fecha + " LUGAR: "
									+ lugar + "TITULO" + titulo + " FOTO "
									+ foto);

							byte[] decodedString = Base64.decode(foto,
									Base64.DEFAULT);

							InputStream myInputStream = new ByteArrayInputStream(
									decodedString);

							Bitmap decodedByte = BitmapFactory
									.decodeStream(new FlushedInputStream(
											myInputStream));

							iV.setImageBitmap(decodedByte);
						} else {
							iV.setImageResource(R.drawable.abc_spinner_ab_focused_holo_light);
						}

						TextView t = (TextView) findViewById(R.id.detailTitle);
						t.setText(titulo);

						t = (TextView) findViewById(R.id.detailAddress);
						t.setText(lugar);

						t = (TextView) findViewById(R.id.detailDate);
						t.setText(fecha);

						t = (TextView) findViewById(R.id.detailDesc);
						t.setText(descripcion);

						t = (TextView) findViewById(R.id.detailPrice);
						t.setText(precio);

						t = (TextView) findViewById(R.id.detailLikes);
						t.setText(likes);
						
						Log.d("beatlm ","ES FAVORITO? "+fav);
						if(fav==1){
						btnFav.setBackgroundResource(R.drawable.favset);
						}
					}

				} catch (JSONException e) {

					e.printStackTrace();
				}

			}
		}

	}

	static class FlushedInputStream extends FilterInputStream {
		public FlushedInputStream(InputStream inputStream) {
			super(inputStream);
		}

		@Override
		public long skip(long n) throws IOException {
			long totalBytesSkipped = 0L;
			while (totalBytesSkipped < n) {
				long bytesSkipped = in.skip(n - totalBytesSkipped);
				if (bytesSkipped == 0L) {
					int b = read();
					if (b < 0) {
						break; // we reached EOF
					} else {
						bytesSkipped = 1; // we read one byte
					}
				}
				totalBytesSkipped += bytesSkipped;
			}
			return totalBytesSkipped;
		}
	}

}

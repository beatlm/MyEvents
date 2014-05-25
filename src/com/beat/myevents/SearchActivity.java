package com.beat.myevents;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.beat.lib.WebServices;

import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

public class SearchActivity extends ActionBarActivity implements
		OnClickListener {
	private String usuario;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_search);

		Intent intent = getIntent();
		usuario = intent.getStringExtra(Constants.USERNAME);
		Cabecera c = (Cabecera) findViewById(R.id.cabecera);
		c.setUsuario(usuario);

		Button b = (Button) findViewById(R.id.searchButton);
		b.setOnClickListener((OnClickListener) this);

	}

	@Override
	public void onClick(View v) {
		Map<String, Object> data = new HashMap<String, Object>();
		EditText t = (EditText) findViewById(R.id.userSearch);
		String us=t.getText().toString();
		data.put("usuario", us);
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
				// TODO Auto-generated catch block
				e.printStackTrace();
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

						Log.d("beatlm", "FECHA: " + fecha + " LUGAR: " + lugar
								+ "TITULO" + titulo);

					}
				} catch (Exception ex) {
					Error = ex.getMessage();
				} finally {

				}

			}

		}
	}
}

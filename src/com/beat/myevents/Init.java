package com.beat.myevents;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.beat.lib.WebServices;
 
import com.facebook.FacebookException;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;

import android.support.v7.app.ActionBarActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class Init extends ActionBarActivity {

	private String TAG = "MainActivity";
	private String user;

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {

		super.onActivityResult(requestCode, resultCode, data);

		Session.getActiveSession().onActivityResult(this, requestCode,
				resultCode, data);

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_init);


	//	LoginButton authButton = (LoginButton) findViewById(R.id.authButton);




	}

	public void logar(View v) {
		Log.d("beatlm", "Entramos en logar");
		EditText e = (EditText) findViewById(R.id.usuario);
		 user = e.getText().toString();

		 e = (EditText) findViewById(R.id.password);
		String pass = e.getText().toString();
		
		List <BasicNameValuePair>params =  new ArrayList();
        params.add(new BasicNameValuePair(Constants.ALIAS, user));
        params.add(new BasicNameValuePair(Constants.PASSWORD, pass));
        
        
    	  new LoginRegister().execute(params);

	/*	Intent intent = new Intent(this, MenuActivity.class);
		intent.putExtra(Constants.USERNAME, user);

		startActivity(intent);*/

	}
	
	public void registrar(View v) {
		Log.d("beatlm", "Entramos en registrar");	

		Intent intent = new Intent(this, RegisterActivity.class);
		startActivity(intent);

	}
	
	
	
	   private class LoginRegister extends AsyncTask<List, Void, JSONObject> {
		   /**
		    * Defining Process dialog
		    **/
		           private ProgressDialog pDialog;
		     
		           @Override
		           protected void onPreExecute() {
		               super.onPreExecute();
		     
		               pDialog = new ProgressDialog(Init.this);
		               pDialog.setTitle(R.string.msgServer);
		               pDialog.setMessage("Logando...");
		               pDialog.setIndeterminate(false);
		               pDialog.setCancelable(true);
		               pDialog.show();
		           }
		           @Override
		           protected JSONObject doInBackground(List... params)  {
		          
		           JSONObject json = WebServices.loginUser(params[0]);
		               return json;
		           }
		          @Override
		           protected void onPostExecute(JSONObject json) {
		        
		          
		          if(json==null){
		       	   pDialog.dismiss();
		       	   pDialog.setTitle(R.string.msgServer);
		              pDialog.setMessage("Ha ocurrido un error en el servidor por favor intentalo de nuevo mas tarde.");
		              pDialog.setIndeterminate(false);
		              pDialog.setCancelable(true);
		              pDialog.show();
		       	   
		          }else{
		       
		       	   Log.d("beatlm",json.toString());
		   	
		                   try {
		                   	 String res = json.getString(Constants.RESULT);
		                		Log.d("beatlm",res);
		                   	 TextView  error=(TextView)  findViewById(R.id.error);
		                       if (res != null) {
		                     
		                          
		                           Log.d("beatlm","Resultado "+res);
		                 
		                           if(Integer.parseInt(res) == 0){
		                     
		                           
		                               Intent registered = new Intent(getApplicationContext(), MenuActivity.class);
		                               registered.putExtra(Constants.USERNAME, user);
		                     
		                             
		                               pDialog.dismiss();
		                               startActivity(registered);
		                               Log.d("beatlm","Va al menu "+res);
		                                 finish();
		                           }
		                           else if (Integer.parseInt(res) ==1){
		                               pDialog.dismiss();
		                               Log.d("beatlm","Contrase–a incorrecta "+res);
		                            error.setText("Contrase–a incorrecta");
		                           }  else if (Integer.parseInt(res) ==2){
		                               pDialog.dismiss();
		                          
		                            error.setText("No existe ningun usuario con ese alias.");
		                           }
		                      
		                       }
		                           else{
		                           pDialog.dismiss();
		                           Log.d("beatlm","Error en servidores "+res);
		                              error.setText("Ha ocurrido un error en los servidores. Vuelve a intentarlo.");
		                           }
		                   } catch (JSONException e) {
		                       e.printStackTrace();
		                   }
		               }}
		   	}

}

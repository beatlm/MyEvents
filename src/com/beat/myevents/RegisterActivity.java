package com.beat.myevents;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.beat.lib.WebServices;

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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Build;

public class RegisterActivity extends ActionBarActivity {
	
	
	 
 
	    private static String KEY_ERROR = "error";
	    private String user;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_register);

	 
	}

	public void registro(View v){
		
		EditText e = (EditText) findViewById(R.id.regAlias);
		user = e.getText().toString();
		
		 e = (EditText) findViewById(R.id.regPassword);
		String pass = e.getText().toString();
		
		 e = (EditText) findViewById(R.id.regMail);
		String mail = e.getText().toString();

		Log.d("beatlm", "Recogemos los datos del registro:"+user+"-"+pass+"-"+mail);
		
		if (  ( !user.equals("")) && ( !pass.equals("")) &&   ( !mail.equals("")) )
        {
            if ( user.length() > 4 ){
            	List <BasicNameValuePair>params =  new ArrayList();
                params.add(new BasicNameValuePair(Constants.ALIAS, user));
                params.add(new BasicNameValuePair(Constants.PASSWORD, pass));
                params.add(new BasicNameValuePair(Constants.EMAIL, mail));
                
                
            	  new ProcessRegister().execute(params);
            }
            else
            {
                Toast.makeText(getApplicationContext(),R.string.msgMin5
                       , Toast.LENGTH_SHORT).show();
            }
        }
        else
        {
            Toast.makeText(getApplicationContext(),R.string.msgEmpty, Toast.LENGTH_SHORT).show();
        }
    }
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
    private class ProcessRegister extends AsyncTask<List, Void, JSONObject> {
/**
 * Defining Process dialog
 **/
        private ProgressDialog pDialog;
  
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
  
            pDialog = new ProgressDialog(RegisterActivity.this);
            pDialog.setTitle(R.string.msgServer);
            pDialog.setMessage("Registrando...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }
        @Override
        protected JSONObject doInBackground(List... params)  {
       
        JSONObject json = WebServices.registerUser(params[0]);
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
                            Log.d("beatlm","El suuario ya existe "+res);
                         error.setText("El usuario ya existe");
                        }  else if (Integer.parseInt(res) ==1){
                            pDialog.dismiss();
                       
                         error.setText("Ha ocurrido un error en el sistema.");
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

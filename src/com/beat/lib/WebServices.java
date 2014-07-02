package com.beat.lib;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.beat.myevents.Constants;
import com.beat.myevents.Event;
import com.beat.myevents.EventAdapter;
import com.beat.myevents.MyEvents;

import android.util.Log;

public class WebServices {
	private final static String URLNEW = "http://eventos.hol.es/php/createEvent.php";
	private final static String URLSEARCH = "http://eventos.hol.es/php/searchEvent.php";
	private final static String URLGETTYPES = "http://eventos.hol.es/php/getTypes.php";
	private final static String URLREGISTER = "http://eventos.hol.es/php/register.php";
	private static final String URLLOGIN = "http://eventos.hol.es/php/login.php";
	private static final String URLFAV = "http://eventos.hol.es/php/getFavEvents.php";
	private static final String URLLAST = "http://eventos.hol.es/php/getEvents.php";  
	private static final String URLSETFAV = "http://eventos.hol.es/php/addFav.php";  
	private static final String URLUNSETFAV = "http://eventos.hol.es/php/deleteFav.php";  
	public static final String URLDETAIL = "http://eventos.hol.es/php/getEventDetail.php";
	private static final String URLDELETE = "http://eventos.hol.es/php/deleteEvent.php";
	private static JSONParser jsonParser;
	
	
	public static boolean post(Map<String, Object> result) throws Exception {
		URL url = new URL(URLNEW);

		StringBuilder postData = new StringBuilder();
		for (Map.Entry<String, Object> param : result.entrySet()) {
			if (postData.length() != 0)
				postData.append('&');
			postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
			postData.append('=');
			postData.append(URLEncoder.encode(String.valueOf(param.getValue()),
					"UTF-8"));
		}

		Log.d("beatlm", "POSTDATA:" + postData.toString());
		byte[] postDataBytes = postData.toString().getBytes("UTF-8");

		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Content-Type",
				"application/x-www-form-urlencoded");
		conn.setRequestProperty("Content-Length",
				String.valueOf(postDataBytes.length));
		conn.setDoOutput(true);
		conn.getOutputStream().write(postDataBytes);
		StringBuilder stringBuilder = new StringBuilder();
		Reader in = new BufferedReader(new InputStreamReader(
				conn.getInputStream(), "UTF-8"));
		int c;
		while ((c = in.read()) != -1) {
			stringBuilder.append((char) c);
		}

		Log.d("beatlm", stringBuilder.toString());
		return stringBuilder.toString().contains("1");

	}
	
	
	public static boolean setFav(Map<String, Object> result) throws Exception {
		URL url = new URL(URLSETFAV);

		StringBuilder postData = new StringBuilder();
		for (Map.Entry<String, Object> param : result.entrySet()) {
			if (postData.length() != 0)
				postData.append('&');
			postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
			postData.append('=');
			postData.append(URLEncoder.encode(String.valueOf(param.getValue()),
					"UTF-8"));
		}

		Log.d("beatlm", "POSTDATA:" + postData.toString());
		byte[] postDataBytes = postData.toString().getBytes("UTF-8");

		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Content-Type",
				"application/x-www-form-urlencoded");
		conn.setRequestProperty("Content-Length",
				String.valueOf(postDataBytes.length));
		conn.setDoOutput(true);
		conn.getOutputStream().write(postDataBytes);
		StringBuilder stringBuilder = new StringBuilder();
		Reader in = new BufferedReader(new InputStreamReader(
				conn.getInputStream(), "UTF-8"));
		int c;
		while ((c = in.read()) != -1) {
			stringBuilder.append((char) c);
		}

		Log.d("beatlm", stringBuilder.toString());
		return stringBuilder.toString().contains("1");

	}
	public static boolean unsetFav(Map<String, Object> result) throws Exception {
		URL url = new URL(URLUNSETFAV);

		StringBuilder postData = new StringBuilder();
		for (Map.Entry<String, Object> param : result.entrySet()) {
			if (postData.length() != 0)
				postData.append('&');
			postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
			postData.append('=');
			postData.append(URLEncoder.encode(String.valueOf(param.getValue()),
					"UTF-8"));
		}

		Log.d("beatlm", "POSTDATA:" + postData.toString());
		byte[] postDataBytes = postData.toString().getBytes("UTF-8");

		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Content-Type",
				"application/x-www-form-urlencoded");
		conn.setRequestProperty("Content-Length",
				String.valueOf(postDataBytes.length));
		conn.setDoOutput(true);
		conn.getOutputStream().write(postDataBytes);
		StringBuilder stringBuilder = new StringBuilder();
		Reader in = new BufferedReader(new InputStreamReader(
				conn.getInputStream(), "UTF-8"));
		int c;
		while ((c = in.read()) != -1) {
			stringBuilder.append((char) c);
		}

		Log.d("beatlm", stringBuilder.toString());
		return stringBuilder.toString().contains("1");

	}
	
	public static boolean delete(Map<String, Object> result) throws Exception {
		URL url = new URL(URLDELETE);

		StringBuilder postData = new StringBuilder();
		for (Map.Entry<String, Object> param : result.entrySet()) {
			if (postData.length() != 0)
				postData.append('&');
			postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
			postData.append('=');
			postData.append(URLEncoder.encode(String.valueOf(param.getValue()),
					"UTF-8"));
		}

		Log.d("beatlm", "POSTDATA:" + postData.toString());
		byte[] postDataBytes = postData.toString().getBytes("UTF-8");

		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Content-Type",
				"application/x-www-form-urlencoded");
		conn.setRequestProperty("Content-Length",
				String.valueOf(postDataBytes.length));
		conn.setDoOutput(true);
		conn.getOutputStream().write(postDataBytes);
		StringBuilder stringBuilder = new StringBuilder();
		Reader in = new BufferedReader(new InputStreamReader(
				conn.getInputStream(), "UTF-8"));
		int c;
		while ((c = in.read()) != -1) {
			stringBuilder.append((char) c);
		}

		Log.d("beatlm", stringBuilder.toString());
		return stringBuilder.toString().contains("1");

	}
	
	//Metodo que obtiene el detalle de un evento
	public static String detail(Map<String, Object> result) throws Exception {
		URL url = new URL(URLDETAIL);

		StringBuilder postData = new StringBuilder();
		for (Map.Entry<String, Object> param : result.entrySet()) {
			if (postData.length() != 0)
				postData.append('&');
			postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
			postData.append('=');
			postData.append(URLEncoder.encode(String.valueOf(param.getValue()),
					"UTF-8"));
		}
		Log.d("beatlm", "postDATA" + postData.toString());

		byte[] postDataBytes = postData.toString().getBytes("UTF-8");

		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Content-Type",
				"application/x-www-form-urlencoded");
		conn.setRequestProperty("Content-Length",
				String.valueOf(postDataBytes.length));
		conn.setDoOutput(true);
		conn.getOutputStream().write(postDataBytes);
		StringBuilder stringBuilder = new StringBuilder();
		Reader in = new BufferedReader(new InputStreamReader(
				conn.getInputStream(), "UTF-8"));
		int c;
		while ((c = in.read()) != -1) {
			stringBuilder.append((char) c);
		}

		Log.d("beatlm", stringBuilder.toString());
		return stringBuilder.toString();

	}
	

	public static String search(Map<String, Object> result) throws Exception {
		URL url = new URL(URLSEARCH);

		StringBuilder postData = new StringBuilder();
		for (Map.Entry<String, Object> param : result.entrySet()) {
			if (postData.length() != 0)
				postData.append('&');
			postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
			postData.append('=');
			postData.append(URLEncoder.encode(String.valueOf(param.getValue()),
					"UTF-8"));
		}
		Log.d("beatlm", "postDATA" + postData.toString());

		byte[] postDataBytes = postData.toString().getBytes("UTF-8");

		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Content-Type",
				"application/x-www-form-urlencoded");
		conn.setRequestProperty("Content-Length",
				String.valueOf(postDataBytes.length));
		conn.setDoOutput(true);
		conn.getOutputStream().write(postDataBytes);
		StringBuilder stringBuilder = new StringBuilder();
		Reader in = new BufferedReader(new InputStreamReader(
				conn.getInputStream(), "UTF-8"));
		int c;
		while ((c = in.read()) != -1) {
			stringBuilder.append((char) c);
		}

		Log.d("beatlm", stringBuilder.toString());
		return stringBuilder.toString();

	}

	//Metodo que devuelve los eventos favoritos de un usuario
	
	public static String favourites(Map<String, Object> result) throws Exception {
		URL url = new URL(URLFAV);

		StringBuilder postData = new StringBuilder();
		for (Map.Entry<String, Object> param : result.entrySet()) {
			if (postData.length() != 0)
				postData.append('&');
			postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
			postData.append('=');
			postData.append(URLEncoder.encode(String.valueOf(param.getValue()),
					"UTF-8"));
		}
		Log.d("beatlm", "postDATA" + postData.toString());

		byte[] postDataBytes = postData.toString().getBytes("UTF-8");

		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Content-Type",
				"application/x-www-form-urlencoded");
		conn.setRequestProperty("Content-Length",
				String.valueOf(postDataBytes.length));
		conn.setDoOutput(true);
		conn.getOutputStream().write(postDataBytes);
		StringBuilder stringBuilder = new StringBuilder();
		Reader in = new BufferedReader(new InputStreamReader(
				conn.getInputStream(), "UTF-8"));
		int c;
		while ((c = in.read()) != -1) {
			stringBuilder.append((char) c);
		}

		Log.d("beatlm", stringBuilder.toString());
		return stringBuilder.toString();

	}
	
	//Metodo que devuelve todos los eventos
	
		public static String last(Map<String, Object> result) throws Exception {
			URL url = new URL(URLLAST);

			//StringBuilder postData = new StringBuilder();
			/*for (Map.Entry<String, Object> param : result.entrySet()) {
				if (postData.length() != 0)
					postData.append('&');
				postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
				postData.append('=');
				postData.append(URLEncoder.encode(String.valueOf(param.getValue()),
						"UTF-8"));
			}
			Log.d("beatlm", "postDATA" + postData.toString());
*/
		//	byte[] postDataBytes = postData.toString().getBytes("UTF-8");

			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");
	//		conn.setRequestProperty("Content-Length",
		//			String.valueOf(postDataBytes.length));
			conn.setDoOutput(true);
		//	conn.getOutputStream().write(postDataBytes);
			StringBuilder stringBuilder = new StringBuilder();
			Reader in = new BufferedReader(new InputStreamReader(
					conn.getInputStream(), "UTF-8"));
			int c;
			while ((c = in.read()) != -1) {
				stringBuilder.append((char) c);
			}

			Log.d("beatlm", stringBuilder.toString());
			return stringBuilder.toString();

		}


	// Metodo que devuelve el listado de tipos de evento

	public static String[] getTypes( )
			throws Exception {
		String[] datos =null;
		URL url = new URL(URLGETTYPES);

		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Content-Type",
				"application/x-www-form-urlencoded");

		conn.setDoOutput(true);
		//conn.getOutputStream().write(0);
		StringBuilder stringBuilder = new StringBuilder();
		Reader in = new BufferedReader(new InputStreamReader(
				conn.getInputStream(), "UTF-8"));
		int c;
		while ((c = in.read()) != -1) {
			stringBuilder.append((char) c);
		}

		Log.d("beatlm", stringBuilder.toString());

		JSONObject jsonResponse = new JSONObject(stringBuilder.toString());

		JSONArray jsonMainNode = jsonResponse.optJSONArray("Events");

		int lengthJsonArr = jsonMainNode.length();
		Log.d("beatlm", "lengthJsonArr: " + lengthJsonArr);
		if (lengthJsonArr > 0) {

			datos = new String[lengthJsonArr+1];
			datos[0]="";
			for (int i = 0; i < lengthJsonArr; i++) {
				JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
				String alias = jsonChildNode.optString("alias").toString();
				datos[i+1] = alias;
			}

		}
		return datos;
	}
	
	  public static JSONObject registerUser(List params){
		  Log.d("beatlm","PARAMETROS:"+params+"-"+URLREGISTER);
	 jsonParser=new JSONParser();
	        JSONObject json = jsonParser.getJSONFromUrl(URLREGISTER,params);
	        return json;
	    }
	  
	  public static JSONObject loginUser(List params){
		  Log.d("beatlm","PARAMETROS:"+params+"-"+URLLOGIN);
	 jsonParser=new JSONParser();
	        JSONObject json = jsonParser.getJSONFromUrl(URLLOGIN,params);
	        return json;
	    }

}

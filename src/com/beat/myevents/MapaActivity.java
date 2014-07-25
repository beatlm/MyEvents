package com.beat.myevents;


import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;

import android.content.Intent;
import android.os.Bundle;

public class MapaActivity  extends android.support.v4.app.FragmentActivity {//extends MapActivity{
	//private MapView mapa = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.fragment_map);
	    
	    Intent intent = getIntent();
		double lat = intent.getDoubleExtra(Constants.LATITUD,0);
		double longi = intent.getDoubleExtra(Constants.LONGITUD,0);
		
	    
	    GoogleMap mapa = ((SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map)).getMap();
	    CameraUpdate camUpd1 =
	    	    CameraUpdateFactory.newLatLng(new LatLng(lat, longi));
	    	 
	    	mapa.moveCamera(camUpd1);
	    	
	    //Obtenemos una referencia al control MapView
     //   mapa = (MapView)findViewById(R.id.mapa);
 
        
        //Mostramos los controles de zoom sobre el mapa
       //mapa.setBuiltInZoomControls(true);
	}
	

	/*@Override
    protected boolean isRouteDisplayed() {
        return false;
    }*/


	
}

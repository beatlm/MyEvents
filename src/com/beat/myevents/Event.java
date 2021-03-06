package com.beat.myevents;

import android.graphics.drawable.Drawable;

public class Event  {
	private long id;
	private String fecha;
private String title;
	private String rest;
	private String hour;
	private Drawable photo;
	private String precio;
	private int estado;
	

	public Event() {		 
		super();
	}

	public Event(long id, String title, String fecha, String hour, String rest, Drawable photo,String precio) {		 
		super();
		this.id=id;
		this.title=title;
		this.fecha=fecha;

		this.hour=hour;
		this.rest=rest;
		this.setPrecio(precio);
		//this.photo=photo;
	}
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}




	public String getRest() {
		return rest;
	}

	public void setRest(String rest) {
		this.rest = rest;
	}

	public String getHour() {
		return hour;
	}

	public void setHour(String hour) {
		this.hour = hour;
	}

	public Drawable getPhoto() {
		return photo;
	}

	public void setPhoto(Drawable photo) {
		this.photo = photo;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getFecha() {
		return fecha;
	}

	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	public String getPrecio() {
		return precio;
	}

	public void setPrecio(String precio) {
		this.precio = precio;
	}

	public int getEstado() {
		return estado;
	}

	public void setEstado(int estado) {
		this.estado = estado;
	}
	  
	
	
}
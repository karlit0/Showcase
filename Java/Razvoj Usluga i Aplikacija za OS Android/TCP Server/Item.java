package com.example.tcpserver;

import java.io.Serializable;

public class Item implements Serializable {

	/** 
     *  
     */
	private static final long serialVersionUID = 1L;
	private String uriString;
	private String name;
	private float NWlong;
	private float NWlat;
	private float SElong;
	private float SElat;
	private short type;
	private int isIn;

	public int getIsIn() {
		return isIn;
	}

	public void setIsIn(int isIn) {
		this.isIn = isIn;
	}

	public short getType() {

		return type;
	}

	public Item(String name, String uriString, float NWlong, float NWlat,
			float SElong, float SElat, short type, int isIn) {
		this.uriString = uriString;
		this.name = name;
		this.NWlong = NWlong;
		this.NWlat = NWlat;
		this.SElong = SElong;
		this.SElat = SElat;
		this.type = type;
		this.isIn = isIn;
	}

	public String getName() {
		return name;
	}

	public String getValue() {
		return uriString;
	}

	public float getNWlong() {
		return NWlong;
	}

	public float getNWlat() {
		return NWlat;
	}

	public float getSElong() {
		return SElong;
	}

	public float getSElat() {
		return SElat;
	}

}
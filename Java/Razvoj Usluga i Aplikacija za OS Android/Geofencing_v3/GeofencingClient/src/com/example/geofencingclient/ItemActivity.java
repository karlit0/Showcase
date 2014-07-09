package com.example.geofencingclient;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.widget.TextView;

public class ItemActivity extends Activity {

	// private LocationListener gpsListener = new GPSListener();
	private String provider;
	private LocationManager locationManager;
	private double NWlong;
	private double NWlat;
	private double SElong;
	private double SElat;
	private short type;
	Uri uri;
	private String uriString;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_item);

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			// get established coordinates
			NWlong = extras.getDouble("NWlong");
			NWlat = extras.getDouble("NWlat");
			SElong = extras.getDouble("SElong");
			SElat = extras.getDouble("SElat");
			type = extras.getShort("Type");

			// get designated URI
			uriString = extras.getString("uriString");

			// show established coordinates
			TextView textView = (TextView) findViewById(R.id.textView3);
			textView.setText("North West longitude: " + NWlong);
			textView = (TextView) findViewById(R.id.textView4);
			textView.setText("North West latitude: " + NWlat);
			textView = (TextView) findViewById(R.id.textView5);
			textView.setText("South East longitude: " + SElong);
			textView = (TextView) findViewById(R.id.textView6);
			textView.setText("South East latitude: " + SElat);
		}

		TextView textView = (TextView) findViewById(R.id.textView1);
		textView.setText("Current longitude: ");
		textView = (TextView) findViewById(R.id.textView2);
		textView.setText("Current latitude: ");

	}

}
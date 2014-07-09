package com.example.geofencingclient;

import java.util.List;

import com.example.geofencingclient.shared.SharedData;
import com.example.tcpserver.Item;

import android.app.IntentService;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

public class LocationUpdateService extends Service implements LocationListener {

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub

		provider = Context.LOCATION_SERVICE;
		locationManager = (LocationManager) getSystemService(provider);
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,
				0, this);
		return super.onStartCommand(intent, flags, startId);
	}

	private String provider;
	private LocationManager locationManager;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub

		List<Item> lokacije = SharedData.getListaPodataka();

		Double longitude = location.getLongitude();
		Double latitude = location.getLatitude();

		// check if current coordinates are at designated coordinates

		if (lokacije == null) {
			Log.i("service", "nije proctiao");
			return;
		}
		for (int i = 0; i < lokacije.size(); i++) {
			Item cur_lokacija = lokacije.get(i);

			if (longitude >= cur_lokacija.getNWlong()
					&& longitude <= cur_lokacija.getSElong()
					&& latitude <= cur_lokacija.getNWlat()
					&& latitude >= cur_lokacija.getSElat()) {

				if (cur_lokacija.getIsIn() == 0) {

					if (cur_lokacija.getType() == 0) {
						Intent intent = new Intent(LocationUpdateService.this,
								WebViewActivity.class);
						intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						intent.putExtra("uriString", cur_lokacija.getValue());
						startActivity(intent);
					} else {
						AudioManager manager = (AudioManager) getApplicationContext()
								.getSystemService(Context.AUDIO_SERVICE);
						manager.setRingerMode(AudioManager.RINGER_MODE_SILENT);

					}

				}

				cur_lokacija.setIsIn(1);

			} else
				cur_lokacija.setIsIn(0);

		}

	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}

}

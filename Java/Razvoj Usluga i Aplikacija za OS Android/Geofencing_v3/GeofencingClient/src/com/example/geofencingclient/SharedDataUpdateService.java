package com.example.geofencingclient;

import java.io.ObjectInputStream;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.util.List;
import com.example.geofencingclient.shared.SharedData;
import com.example.tcpserver.Item;
import android.app.IntentService;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

public class SharedDataUpdateService extends IntentService {

	public static final String NOTIFICATION = "com.example.androidclient.locationsreceived";
	private TCPClient mTcpClient;
	private List<Item> locations = null;

	public SharedDataUpdateService() {
		super("Shared Data Update Service");
	}

	@Override
	protected void onHandleIntent(Intent intent) {

		new connectTask().execute("");

	}

	public class connectTask extends AsyncTask<String, String, TCPClient> {

		@Override
		protected TCPClient doInBackground(String... message) {

			Log.i("SharedData", "Started AsyncTask");

			mTcpClient = new TCPClient(new TCPClient.OnLocationsReceived() {

				// implemented locationsReceived method
				@Override
				public void locationsReceived(List<Item> _locations) {
					locations = _locations;
					// calls onProgressUpdate method
					publishProgress();
				}
			});
			try {
				mTcpClient.run();
			} catch (Exception e) {
				Log.i("SharedData", "failed to connect to server");
				Intent notify = new Intent(NOTIFICATION);
				notify.putExtra("succeded", false);
				sendBroadcast(notify);
			}

			return null;
		}

		@Override
		protected void onProgressUpdate(String... values) {
			super.onProgressUpdate(values);

			SharedData.setListaPodataka(locations);
			Log.i("SharedData", "Number of locations: " + locations.size());

			Intent notify = new Intent(NOTIFICATION);
			notify.putExtra("succeded", true);
			sendBroadcast(notify);
		}
	}
}
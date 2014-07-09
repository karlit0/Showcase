package com.example.geofencingclient;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;
import com.example.geofencingclient.shared.SharedData;
import com.example.tcpserver.Item;

public class MainActivity extends Activity {
	private TCPClient mTcpClient;
	private List<Item> locations;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Intent intent = new Intent(this, SharedDataUpdateService.class);
		startService(intent);

		Intent intent2 = new Intent(this, LocationUpdateService.class);
		startService(intent2);

		Toast toast = Toast.makeText(this,
				"Retrieving locations from server...", Toast.LENGTH_SHORT);
		toast.show();

	}

	@Override
	protected void onResume() {
		super.onResume();
		registerReceiver(receiver, new IntentFilter(
				SharedDataUpdateService.NOTIFICATION));
	}

	@Override
	protected void onPause() {
		super.onPause();
		unregisterReceiver(receiver);
	}

	private BroadcastReceiver receiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getBooleanExtra("succeded", true)) {
				LayoutInflater inflater = (LayoutInflater) getApplicationContext()
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				LinearLayout itemsLayout = (LinearLayout) inflater.inflate(
						R.layout.activity_main, null);
				ListView itemsList = (ListView) itemsLayout
						.findViewById(R.id.list_items);
				itemsList.setAdapter(new ItemAdapter(MainActivity.this,
						R.layout.list_item, SharedData.getListaPodataka()));

	

				setContentView(itemsLayout);
				
				Button button = (Button) findViewById(R.id.button1);
				button.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Intent intent = new Intent(MainActivity.this,
								SharedDataUpdateService.class);
						startService(intent);
					}
				});

			} else {
				Toast toast = Toast.makeText(MainActivity.this,
						"Server unavailable", Toast.LENGTH_SHORT);
				toast.show();
			}
		}
	};

}
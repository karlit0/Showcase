package com.example.geofencingclient;

import java.util.List;

import com.example.tcpserver.Item;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

// ItemAdapter inspiriran QuestionAdapterom u QuizApp primjeru s predavanja 

public class ItemAdapter extends ArrayAdapter<Item> {

	private List<Item> items;
	private Context context;

	public ItemAdapter(Context context, int textViewResourceId, List<Item> items) {
		super(context, textViewResourceId, textViewResourceId, items);

		this.context = context;
		this.items = items;
	}

	public View getView(final int itemID, View convertView, ViewGroup parent) {
		View view = convertView;

		if (view == null) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(R.layout.list_item, null);
		}

		Item item = items.get(itemID);
		if (item != null) {
			TextView itemView = (TextView) view
					.findViewById(R.id.list_item_text);
			if (itemView != null) {
				itemView.setText(item.getName());
			}

			final String uriString = item.getValue();
			final double NWlong = item.getNWlong();
			final double NWlat = item.getNWlat();
			final double SElong = item.getSElong();
			final double SElat = item.getSElat();
			final short type = item.getType();

			itemView.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent = new Intent(context, ItemActivity.class);
					intent.putExtra("uriString", uriString);
					intent.putExtra("NWlong", NWlong);
					intent.putExtra("NWlat", NWlat);
					intent.putExtra("SElong", SElong);
					intent.putExtra("SElat", SElat);
					intent.putExtra("Type", type);
					context.startActivity(intent);
				}
			});
		}

		return view;
	}

}

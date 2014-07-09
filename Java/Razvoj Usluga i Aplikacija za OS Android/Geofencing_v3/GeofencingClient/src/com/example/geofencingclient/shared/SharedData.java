package com.example.geofencingclient.shared;

import java.util.List;
import com.example.tcpserver.*;

public class SharedData {

	private static List<Item> listaPodataka;

	public static List<Item> getListaPodataka() {
		return listaPodataka;
	}

	public static void setListaPodataka(List<Item> listaPodataka) {
		SharedData.listaPodataka = listaPodataka;
	}

}

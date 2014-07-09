package com.example.tcpserver;

import java.util.List;
import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * The class extends the Thread class so we can receive and send messages at the
 * same time
 */
public class TCPServer extends Thread {

	public static final int SERVERPORT = 4444;

	public static void main(String[] args) {
		TCPServer server = new TCPServer();
		server.start();
	}

	public TCPServer() {
	}

	@Override
	public void run() {
		super.run();

		/* open database and read it */
		List<Item> locations = Database.getLocations();

		System.out.println("Database entries:");
		for (int i = 0; i < locations.size(); i++) {
			String name = locations.get(i).getName();
			String url = locations.get(i).getValue();
			float NWLong = locations.get(i).getNWlong();
			float NWLat = locations.get(i).getNWlat();
			float SELong = locations.get(i).getSElong();
			float SELat = locations.get(i).getSElat();

			System.out.println(name + " " + url + " " + NWLong + " " + NWLat
					+ " " + SELong + " " + SELat);
		}

		ServerSocketChannel ssChannel = null;
		try {
			// set serversocketchannel
			ssChannel = ServerSocketChannel.open();
			ssChannel.socket().bind(new InetSocketAddress(SERVERPORT));
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		while (true) {
			try {
				System.out.println("S: Waiting for client...");

				// accept client
				SocketChannel sChannel = ssChannel.accept();

				System.out.println("S: Received client");

				// set objectoutputstream
				ObjectOutputStream oos = new ObjectOutputStream(sChannel
						.socket().getOutputStream());

				// send locations
				oos.writeObject(locations);

				// close objectoutputstream
				oos.close();

			} catch (Exception e) {
				System.out.println("S: Error");
				e.printStackTrace();
			}
		}

	}

}
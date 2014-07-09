package com.example.tcpserver;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Database {

	public static void main(String[] args) {
		Connection c = null;
		Statement stmt = null;
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:userdatabase.db");
			System.out.println("Opened database successfully");

			stmt = c.createStatement();

			/* obrisi tablicu u bazi ako postoji */
			String sql = "DROP TABLE LOCATION";
			try {
				stmt.executeUpdate(sql);
			} catch (Exception e) {
			}

			/* stvori tablicu */
			sql = "CREATE TABLE LOCATION "
					+ "(NAME     TEXT PRIMARY KEY    NOT NULL,"
					+ " URL      TEXT                NOT NULL,"
					+ " NWLONG   REAL                NOT NULL,"
					+ " NWLAT    REAL                NOT NULL,"
					+ " SELONG   REAL                NOT NULL,"
					+ " SELAT    REAL                NOT NULL,"
					+ " TYPE     INTEGER             NOT NULL,"
					+ " ISIN	INTEGER				NOT NULL)";
			stmt.executeUpdate(sql);

			/* ubaci pocetne podatke u tablicu */
			sql = "INSERT INTO LOCATION (NAME, URL, NWLONG, NWLAT, SELONG, SELAT, TYPE , ISIN) "
					+ "VALUES ('FER', 'http://www.fer.unizg.hr', 10.0, 20.0, 20.0, 10.0, 0 ,0)";
			stmt.executeUpdate(sql);

			sql = "INSERT INTO LOCATION (NAME, URL, NWLONG, NWLAT, SELONG, SELAT, TYPE, ISIN) "
					+ "VALUES ('Google', 'http://www.google.hr', 50.0, 60.0, 60.0, 50.0, 0,0 )";
			stmt.executeUpdate(sql);

			sql = "INSERT INTO LOCATION (NAME, URL, NWLONG, NWLAT, SELONG, SELAT, TYPE, ISIN) "
					+ "VALUES ('FER2', 'http://www.fer2.net', 100.0, 110.0, 110.0, 100.0, 0,0 )";
			stmt.executeUpdate(sql);

			sql = "INSERT INTO LOCATION (NAME, URL, NWLONG, NWLAT, SELONG, SELAT, TYPE, ISIN) "
					+ "VALUES ('Youtube', 'http://www.youtube.com', 200.0, 220.0, 220.0, 200.0, 0 ,0)";
			stmt.executeUpdate(sql);

			sql = "INSERT INTO LOCATION (NAME, URL, NWLONG, NWLAT, SELONG, SELAT, TYPE, ISIN) "
					+ "VALUES ('Silencer', 'http://www.silencer.com', 500.0, 550.0, 550.0, 500.0, 1,0 )";
			stmt.executeUpdate(sql);

			stmt.close();
			c.close();
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
		System.out.println("Table created successfully");
	}

	public static List<Item> getLocations() {
		Connection c = null;
		Statement stmt = null;
		List<Item> locations = new ArrayList<Item>(50);
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:userdatabase.db");
			System.out.println("Opened database successfully");

			stmt = c.createStatement();

			ResultSet rs = stmt.executeQuery("SELECT * FROM LOCATION;");

			while (rs.next()) {
				String name = rs.getString("name");
				String url = rs.getString("url");
				float NWLong = rs.getFloat("nwlong");
				float NWLat = rs.getFloat("nwlat");
				float SELong = rs.getFloat("selong");
				float SELat = rs.getFloat("selat");
				int type = rs.getInt("type");
				int isIn = rs.getInt("isin");

				Item item = new Item(name, url, NWLong, NWLat, SELong, SELat,
						(short) type, isIn);
				locations.add(item);
			}

			stmt.close();
			c.close();
			return locations;
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			return null;
		}
	}

	//
	// public static void addUser(User user) {
	// Connection c = null;
	// Statement stmt = null;
	//
	// try {
	// Class.forName("org.sqlite.JDBC");
	// c = DriverManager.getConnection("jdbc:sqlite:userdatabase.db");
	//
	// stmt = c.createStatement();
	//
	// String sql = "INSERT INTO USER (USERNAME, PASSWORD) " +
	// "VALUES ('"+ user.getUsername() + "', '"
	// + user.getPassword() + "')";
	// stmt.executeUpdate(sql);
	//
	// } catch ( Exception e ) {
	// System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	// }
	// }
	//

}
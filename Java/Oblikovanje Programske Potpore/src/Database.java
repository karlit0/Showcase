package pagru_v05;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/* hipotetska tablica frendova
 * 1. stupac User - 2. stupac friend (ako bi user1 i user2 bili frendovi, moralo bi biti 2 retka koja to pokazuju - "potrebna" redudancija)
 * ako se zeli dohvatit lista frendova od Usera, pretrazi se tablica Friends gdje je username = 1. stupcu
 * svaki redak sadrzi frenda u 2. stupcu - te vrijednosti se pobacaju u neku listu i ta lista se preda useru
 * 
 */

public class Database {
	
	/* pokretanjem maina, baza se resetira na pocetno stanje
	 * s 4 usera: admin, user1, user2, user3	*/
	public static void main(String[] args) {
		Connection c = null;
		Statement stmt = null;
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:userdatabase.db");
			System.out.println("Opened database successfully");
			
			stmt = c.createStatement();
			
			/* obrisi tablicu u bazi ako postoji */
			String sql = "DROP TABLE USER";
			try{
				stmt.executeUpdate(sql);
			} catch (Exception e) {
			}
			
			sql = "DROP TABLE FRIENDS";
			try{
				stmt.executeUpdate(sql);
			} catch (Exception e) {			
			}
			
			sql = "DROP TABLE GROUPCONV";
			try{
				stmt.executeUpdate(sql);
			} catch (Exception e) {			
			}
			
			/* stvori tablicu */
			sql = "CREATE TABLE USER " +
						 "(USERNAME TEXT PRIMARY KEY	NOT NULL," +
						 " PASSWORD TEXT				NOT NULL)";
			stmt.executeUpdate(sql);
			
			/* ubaci pocetne podatke u tablicu */
			sql = "INSERT INTO USER (USERNAME, PASSWORD) " +
					"VALUES ('admin', 'pass')";
			stmt.executeUpdate(sql);
			
			sql = "INSERT INTO USER (USERNAME, PASSWORD) " +
					"VALUES ('user1', 'pass1')";
			stmt.executeUpdate(sql);
			
			sql = "INSERT INTO USER (USERNAME, PASSWORD) " +
					"VALUES ('user2', 'pass2')";
			stmt.executeUpdate(sql);
			
			sql = "INSERT INTO USER (USERNAME, PASSWORD) " +
					"VALUES ('user3', 'pass3')";
			stmt.executeUpdate(sql);
			
			sql = "CREATE TABLE FRIENDS " +
						"(USERNAME TEXT 	NOT NULL," +
						" FRIEND TEXT		NOT NULL," +
						" PRIMARY KEY (USERNAME, FRIEND))";
			stmt.executeUpdate(sql);
			
			/* grupa moze imati max. 5 clanova */
			sql = "CREATE TABLE GROUPCONV " +
						"(USER1 TEXT	NOT NULL," +
						" USER2 TEXT	NOT NULL," +
						" USER3 TEXT	NOT NULL," +
						" USER4 TEXT," +
						" USER5 TEXT," +
						" PRIMARY KEY (USER1, USER2, USER3, USER4, USER5))";
			stmt.executeUpdate(sql);
			
			stmt.close();
			c.close();
		} catch(Exception e) {
			System.err.println( e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
		System.out.println("Table created successfully");
	}
	
	public static List<User> getRegisteredUsers() {
		Connection c = null;
		Statement stmt = null;
		List<User> users_registered = new ArrayList<User>(50);
		try {
			Class.forName("org.sqlite.JDBC");
		    c = DriverManager.getConnection("jdbc:sqlite:userdatabase.db");
		    System.out.println("Opened database successfully");
		    
		    stmt = c.createStatement();
		    
		    ResultSet rs = stmt.executeQuery( "SELECT * FROM USER;" );
		    
		    
		    
		    while ( rs.next() ) {
		    	String username = rs.getString("username");
		    	String password = rs.getString("password");

		    	User user = new User(username, password);
		    	users_registered.add(user);
		    }
		    
		    stmt.close();
		    c.close();
		    return users_registered;
		} catch ( Exception e ) {
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
			return null;
		}
	}
        
	public static void addUser(User user) {
		Connection c = null;
		Statement stmt = null;
            
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:userdatabase.db");
			
			stmt = c.createStatement();
                
			String sql = "INSERT INTO USER (USERNAME, PASSWORD) " +
					"VALUES ('"+ user.getUsername() + "', '" 
					+ user.getPassword() + "')";
			stmt.executeUpdate(sql);
                        
		} catch ( Exception e ) {
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );                
		}
	}
        
	public static void addFriends(String userUsername, String friendUsername) {
		Connection c = null;
		Statement stmt = null;
		
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:userdatabase.db");
			
			stmt = c.createStatement();
			
			String sql = "INSERT INTO FRIENDS (USERNAME, FRIEND) " +
					"VALUES ('" + userUsername + "', '" + friendUsername + "')";					
			stmt.executeUpdate(sql);
			
			sql = "INSERT INTO FRIENDS (USERNAME, FRIEND) " +
					"VALUES ('" + friendUsername + "', '" + userUsername + "')";					
			stmt.executeUpdate(sql);
		} catch ( Exception e ) {
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
		}
	}
	
	public static String getFriends(String userUsername) {
		Connection c = null;
		Statement stmt = null;
		
		try {
			Class.forName("org.sqlite.JDBC");
		    c = DriverManager.getConnection("jdbc:sqlite:userdatabase.db");
		    System.out.println("Opened database successfully");
		    
		    stmt = c.createStatement();
		    
		    ResultSet rs = stmt.executeQuery( "SELECT * FROM FRIENDS WHERE USERNAME='" + userUsername + "';" );
		    
		    String reply = "";
		    
		    while ( rs.next() ) {		    	
		    	String friend = rs.getString("friend");

		    	reply = reply + " " + friend;
		    }
		    
		    stmt.close();
		    c.close();
		    
		    return reply;
		} catch ( Exception e ) {
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
			return "";
		}
				
	}
	
	public static void listAllFriends() { /* kontrolni ispis*/
		Connection c = null;
		Statement stmt = null;		
		try {
			Class.forName("org.sqlite.JDBC");
		    c = DriverManager.getConnection("jdbc:sqlite:userdatabase.db");
		    System.out.println("Opened database successfully");
		    
		    stmt = c.createStatement();
		    
		    ResultSet rs = stmt.executeQuery( "SELECT * FROM FRIENDS;" );
		    
		    
		    
		    while ( rs.next() ) {
		    	String username = rs.getString("username");
		    	String friend = rs.getString("friend");

		    	System.out.println("User " + username + " IS FRIENDS WITH " + friend);
		    }
		    
		    stmt.close();
		    c.close();
		    return;
		} catch ( Exception e ) {
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
			return;
		}
	}
	
	public static void addGroup(List<String> _users) {
		Connection c = null;
		Statement stmt = null;		
		try {
			String users[] = new String[5];
			
			for (int i=0; i<5; i++)
				users[i] = "";	/* inicijaliziraj sve clanove na prazan string	*/
			
			for (int i=0; i<_users.size(); i++)
				users[i] = _users.get(i);
			
			Class.forName("org.sqlite.JDBC");
		    c = DriverManager.getConnection("jdbc:sqlite:userdatabase.db");
		    System.out.println("Opened database successfully");
		    
		    stmt = c.createStatement();					   
		    
			String sql = "INSERT INTO GROUPCONV (USER1, USER2, USER3, USER4, USER5) " +
					"VALUES ('" + users[0] + "', '" + users[1] + "', '" + users[2] + "', '"
						+ users[3] + "', '" + users[4] + "')";					
			stmt.executeUpdate(sql);
			
			stmt.close();
			c.close();
		} catch ( Exception e ) {
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
		}				    
	}
	
	public static void listAllGroups() { /* kontrolni ispis*/
		Connection c = null;
		Statement stmt = null;		
		try {
			Class.forName("org.sqlite.JDBC");
		    c = DriverManager.getConnection("jdbc:sqlite:userdatabase.db");
		    System.out.println("Opened database successfully");
		    
		    stmt = c.createStatement();
		    
		    ResultSet rs = stmt.executeQuery( "SELECT * FROM GROUPCONV;" );
		    
		    
		    
		    while ( rs.next() ) {
		    	String user1 = rs.getString("user1");
		    	String user2 = rs.getString("user2");
		    	String user3 = rs.getString("user3");
		    	String user4 = rs.getString("user4");
		    	String user5 = rs.getString("user5");

		    	System.out.println("User1: " + user1 + ", User2: " + user2 + ", User3: " + user3
		    				+ ", User4: " + user4 + ", User5: " + user5);
		    }
		    
		    stmt.close();
		    c.close();
		    return;
		} catch ( Exception e ) {
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
			return;
		}
	}
	
	public static List<Group> getGroups(String username) {
		Connection c = null;
		Statement stmt = null;		
		try {
			Class.forName("org.sqlite.JDBC");
		    c = DriverManager.getConnection("jdbc:sqlite:userdatabase.db");
		    System.out.println("Opened database successfully");
		    
		    stmt = c.createStatement();
		    
		    /* dohvati sve grupe */
		    ResultSet rs = stmt.executeQuery( "SELECT * FROM GROUPCONV;" );
		    
		    List<Group> groups = new ArrayList<Group>();

		    
		    
		    while ( rs.next() ) {
		    	String user1 = rs.getString("user1");
		    	String user2 = rs.getString("user2");
		    	String user3 = rs.getString("user3");
		    	String user4 = rs.getString("user4");
		    	String user5 = rs.getString("user5");

		    	/* provjeri je li u grupi postoji user s var username */
		    	if ( (username.equals(user1)) || (username.equals(user2)) || (username.equals(user3)) || 
		    			(username.equals(user4)) || (username.equals(user5)) ) {

		    		
		    		/* user1 do user5 su abecedno poredani, tak da ih mogu tim redom bacat u Group */
				    List<String> groupMembers = new ArrayList<String>();
		    		groupMembers.add(user1); /* ne moram provjeravat jel postoje useri 1-3 jer min. grupa je od 3 usera */
		    		groupMembers.add(user2);
		    		groupMembers.add(user3);
		    		if ( !(user4.equals("")) )
		    			groupMembers.add(user4);
		    		if ( !(user5.equals("")) )
		    			groupMembers.add(user5);
		    		
		    		Group group = new Group();
		    		group.groupMembers = groupMembers;
		    		
		    		groups.add(group);		    		
		    	}
		    }
		    
		    stmt.close();
		    c.close();
		    return groups;
		} catch ( Exception e ) {
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
			return null;
		}
	}

}

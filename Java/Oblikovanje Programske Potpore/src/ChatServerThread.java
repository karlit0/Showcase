package pagru_v05;

import java.net.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.*;

/* nemam pojma sta je ovo ni odakle je doslo, ali meni baca error */
//import com.sun.corba.se.spi.activation.Server;

public class ChatServerThread extends Thread {

	private Socket			socket      = null;
	private ChatServer		server      = null;
	private int			ID          = -1;
	private DataInputStream         streamIn    = null;
	private DataOutputStream        streamOut   = null;
	private String                  username    = "";
	
	public ChatServerThread(ChatServer _server, Socket _socket) {
		super();
		server = _server;
		socket = _socket;
		ID = socket.getPort();
	}

	public void send(String msg) {
                           
		try {
			streamOut.writeUTF(msg);
			streamOut.flush();
		} catch(IOException ioe) {
			System.out.println(ID + " ERROR sending " + ioe.getMessage());
			server.remove(ID);
			stop();
		}
	}
	
	public int getID() {
		return ID;
	}
	
	public String getUsername() {
		return username;
	}	
	
	public int authenticate(String _username, String _password) {            
		int loginResult = server.checklogin(_username, _password);
		/* loginResult =	1 - uspjesan login	
		 *			0 - taj user je vec online
		 *			-1 - user ne postoji u listi registriranih usera
		 * */
		
		if (loginResult == 1) {	/* successful login*/                
			username = _username;
			if (username.equals("admin"))
				send("OKADMIN");
			else
				send("OK");
			System.out.println("Successful login!");
			return 1;
		}                    
		else if (loginResult == 0) {
			send("LOGIN0");
			System.out.println("User already online");
			return 0;
		}
		else {
			send("LOGIN-1");
			System.out.println("User ne postoji");
			return -1;
		}
	}
        
	public void register(String _username, String _password) {
		int registerResult = server.tryregister(_username, _password);
		
		if (registerResult == 1)
			send("REGISTEROK");
		else {
			send("REGISTERFAIL");
			System.out.println("Registration fail");
		}
	}
        
	@Override
	public void run() {
            System.out.println("Server Thread " + ID + " running.");
		                        
            boolean loginOK = false;
            String input = "";
            
            while(!loginOK) {
                try {
                    input = streamIn.readUTF();
                } catch (IOException ioe) {				
                    System.out.println(ID + " ERROR reading: " + ioe.getMessage());
                    server.remove(ID);
                }
                
                String delims = "[ ]+";			/* parsiranje stringa */
                String[] tokens = input.split(delims);
            	
                if (tokens[0].equals("LOG")) {
                		if (tokens.length != 3) {
                			send ("INVALIDLOGININFO");
                			continue;
                		}
                        if (authenticate(tokens[1], tokens[2]) == 1)                        
                            loginOK = true;
                    }
                
                if (tokens[0].equals("REG")) {
                	
                	if (tokens.length != 4) {
                		send ("REGISTERFAIL");
                		continue;
                	}
                	
                	System.out.println("Trying to register: " + tokens[1] + " " + tokens[2]);
                        

                        	
                		/* 		provjeri regex za email			*/
                	Pattern p = Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$", Pattern.CASE_INSENSITIVE);
                	Matcher m = p.matcher(tokens[3]);
                    		
                	if(!m.matches()) {
                		send("REGISTERFAIL");
                		System.out.println("Registration fail - invalid email address");	                	
                	}
                	else {
                		System.out.println("Trying to register: " + tokens[1] + " " + tokens[2]);
                		register(tokens[1], tokens[2]);                
                	}                                            
                	            
                }                                            
            }
	                       
                  
            /*
             * uspjesan login
             */
            
            /* provjeri inbox */
            server.checkInbox(username);
            
            
            /* ostatak programa (mainwindow) */
            while (true) {
            	try {
            		input = streamIn.readUTF();
            	} catch (IOException ioe) {				
            		System.out.println(ID + " ERROR reading: " + ioe.getMessage());
            		server.remove(ID);
            	}
            	
            	System.out.println("DOBIO SAM OVAJ MSG: " + input);
            	
            	/* more IF-ova, handleanje svih mogucih zahtjeva od usera/admina  	 */
            	          	
            	/* Reglistu zahtijeva admin */
            	if (input.equals("GIVEREGUSERS")) {
            		List<User> regusers_list = server.getRegUsers();
            		String reply = "SENDINGREGUSERS";
            		
            		for (int i=0; i<regusers_list.size(); i++){
            			/* dodaj u string reply username od svih frendova, delimiter " " */
            			reply = reply + " " + regusers_list.get(i).getUsername();
            		}
            		send(reply);            		
            	}            	            
            	
            	String delims = "[ ]+";			/* parsiranje stringa */
            	String[] tokens = input.split(delims);    
            	
            	if (tokens[0].equals("REG")) { /* ako se admin loginno on moze reg. nove korisnike */
                	
                	if (tokens.length != 4) {
                		send ("REGISTERFAIL");
                		continue;
                	}
                	
                	System.out.println("Trying to register: " + tokens[1] + " " + tokens[2]);
                        

                        	
                		/* 		provjeri regex za email			*/
                	Pattern p = Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$", Pattern.CASE_INSENSITIVE);
                	Matcher m = p.matcher(tokens[3]);
                    		
                	if(!m.matches()) {
                		send("REGISTERFAIL");
                		System.out.println("Registration fail - invalid email address");	                	
                	}
                	else {
                		System.out.println("Trying to register: " + tokens[1] + " " + tokens[2]);
                		register(tokens[1], tokens[2]);                
                	}                                            
                	            
                }   
           		 
            	if (tokens[0].equals("GIVEFLIST")) {
            		String userUsername = tokens[1];
            		String friends_string = server.getFriends(userUsername);
            		send("SENDINGFLIST" + friends_string); /*  ne treba " " izmedju njih jer je razmak stavljen vec u varijabli friends 
		 														(vidit metodu getFriends() u Database.java) */
            	}
            	
            	if (tokens[0].equals("MSG")) {
            		/*
            		 * MSG sendderUsername targetUsernames ENDOFUSERS msg
            		 * 
            		 *  tokens[1 je sender
            		 *  for i do tokens.length dok je !(tokens.equals)ENDOFUSERS
            		 *    bacaj u List<String> targets
            		 *    
            		 *    svakom od tih targeta sastavi msg: MSG senderusername targetusernames(tu se ponovno nalazi sender) msg
            		 */
            		
            		String senderUsername = tokens[1];
            		List<String> targets = new ArrayList<String>();
            		int beginIndex = 0;
            		
            		beginIndex = 3 + 1 + senderUsername.length() + 1;
            		
            		for (int i=2; !(tokens[i].equals("ENDOFUSERS")); i++) {
            			targets.add(tokens[i]);
              			beginIndex += tokens[i].length() + 1; 
            		}
            		
            		beginIndex += "ENDOFUSERS".length() + 1;
            		
        			String msg_text = input.substring(beginIndex); /* uzeta je samo poruka, izbacene su kljucne rijeci koje se nalaze prije nje */
        			
            		for (int i=0; i<targets.size(); i++) {
            			String message = "MSG" + " " + senderUsername;
            			String cur_target = targets.get(i);
            			
            			message = message + " " + senderUsername; /* taj koji je poslao poruku je takodjer target */ 
            			for (int j=0; j<targets.size(); j++) {
            				if ( !( cur_target.equals(targets.get(j)) ) )
            					message = message + " " + targets.get(j);
            			}
            			
            			message = message + " " + "ENDOFUSERS" + " " + msg_text;
            			
            			server.handle(cur_target, message);
            		}
            		

            		
//            		String msg = input;
//           		  	int beginIndex = 3 + 1 + senderUsername.length() + 1 + targetUsername.length() + 1; /* varijabla za fun substring (pocetno mjesto poruke u stringu) */
//           		  	msg = input.substring(beginIndex); /* uzeta je samo poruka, izbacene su kljucne rijeci koje se nalaze prije nje */
//           		  	server.handle(senderUsername, targetUsername, msg);
           	  	}
           	  
           	  	if (tokens[0].equals("ADDFRIEND")) {
           	  		String userUsername = tokens[1];
           	  		String friendUsername = tokens[2];
           	  		server.addFriends(userUsername, friendUsername);
           	  	}
           	  	
           	  	if (tokens[0].equals("GROUP")) {
           	  		List<String> users = new ArrayList<String>();
        	  		for (int i=1; i<tokens.length; i++)
        	  			users.add(tokens[i]);
        	  		
        	  		Collections.sort(users);
        	  		server.addGroup(users);
        	  	}
           	  	
           	  	if (tokens[0].equals("GIVEGROUPS")) {
           	  		String userUsername = tokens[1];
        	  		
        	  		
        	  		/* provjeri ima li grupa definiranih s ovim usernameom */
        	  		List<Group> groups = server.getGroups(userUsername);
        	  		if (groups != null)
        	  			for(int i=0; i < groups.size(); i++) {	/* za svaku grupu sastavi string */
        	  				Group current_group = groups.get(i);
        	  				String group_string = "GROUP";
        	  				
        	  				for (int j=0; j < current_group.groupMembers.size(); j++) /* u pojedinoj grupi sastavi string GROUP + " " + members */
        	  					group_string = group_string + " " + current_group.groupMembers.get(j);
        	  				
        	  				send(group_string); /* posalji sastavljeni string */
        	  			}
        	  		

        	  	}
            }
	}
	
	public void open() throws IOException {
		streamIn = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
		streamOut = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
	}
	
	public void close() throws IOException {
		if (socket != null)	socket.close();
		if (streamIn != null)	streamIn.close();
		if (streamOut != null)	streamOut.close();
	}
}

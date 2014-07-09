package pagru_v05;

//package Paketic;
//
//import java.net.*;
//import java.io.*;
//
//public class ChatServerThread extends Thread {
//
//	private Socket			socket      = null;
//	private ChatServer		server      = null;
//	private int			ID          = -1;
//	private DataInputStream         streamIn    = null;
//	private DataOutputStream        streamOut   = null;
//	private String                  username    = "";
//	
//	public ChatServerThread(ChatServer _server, Socket _socket) {
//		super();
//		server = _server;
//		socket = _socket;
//		ID = socket.getPort();
//	}
//
//	public void send(String msg) {
//                           
//            	try {
//			streamOut.writeUTF(msg);
//			streamOut.flush();
//		} catch(IOException ioe) {
//			System.out.println(ID + " ERROR sending " + ioe.getMessage());
//			server.remove(ID);
//			stop();
//		}
//	}
//	
//	public int getID() {
//		return ID;
//	}
//	
//	public String getUsername() {
//		return username;
//	}	
//	
//        public int authenticate(String _username, String _password) {            
//            int loginResult = server.checklogin(_username, _password);
//                    /* loginResult =	1 - uspjesan login	
//                    *			0 - taj user je vec online
//                    *			-1 - user ne postoji u listi registriranih usera
//                    * */
//	
//            if (loginResult == 1) {	/* successful login*/                
//                username = _username;
//                send("OK");
//                System.out.println("Successful login!");
//                return 1;
//            }                    
//            else if (loginResult == 0) {
//                send("login0");
//                System.out.println("User already online");
//                return 0;
//            }
//            else {
//                send("login-1");
//                System.out.println("User ne postoji");
//                return -1;
//            }
//        }
//        
//        public void register(String _username, String _password) {
//            int registerResult = server.tryregister(_username, _password);
//            
//            if (registerResult == 1)
//                send("registerOK");
//            else {
//                send("registerFAIL");
//                System.out.println("Registration fail");
//            }
//        }
//        
//	@Override
//	public void run() {
//            System.out.println("Server Thread " + ID + " running.");
//		                        
//            boolean loginOK = false;
//            String input = "";
//            
//            while(!loginOK) {
//                try {
//                    input = streamIn.readUTF();
//                } catch (IOException ioe) {				
//                    System.out.println(ID + " ERROR reading: " + ioe.getMessage());
//                    server.remove(ID);
//                }
//                
//                String delims = "[ ]+";			/* parsiranje stringa */
//		String[] tokens = input.split(delims);
//            	
//                if (tokens.length == 3) {
//                    if (tokens[0].equals("log")) {
//                        if (authenticate(tokens[1], tokens[2]) == 1)                        
//                            loginOK = true;
//                    }
//                
//                    if (tokens[0].equals("reg")) {
//                        System.out.println("Trying to register: " + tokens[1] + " " + tokens[2]);
//                        register(tokens[1], tokens[2]);                
//                    }                                            
//                }
//            }
//        /*
//            registriranje je uspjesno, vidit zasto se ne mogu loginat odmah nakon registriranja
//            */
//                        
//            try {
//		input = streamIn.readUTF();
//            } catch (IOException ioe) {				
//                System.out.println(ID + " ERROR reading: " + ioe.getMessage());
//                server.remove(ID);
//            }
//	}
//	
//	public void open() throws IOException {
//		streamIn = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
//		streamOut = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
//	}
//	
//	public void close() throws IOException {
//		if (socket != null)	socket.close();
//		if (streamIn != null)	streamIn.close();
//		if (streamOut != null)	streamOut.close();
//	}
//}

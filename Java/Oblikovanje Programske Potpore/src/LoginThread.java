package pagru_v05;

import java.net.*;
import java.io.*;

public class LoginThread extends Thread {

	private Socket			socket 	= null;
	private Login                   login   = null;
	private DataInputStream	streamIn	= null;
	private boolean         stopper       = false;
        
        public LoginThread(Login _login, Socket _socket) {
            login = _login;
            socket = _socket;
            open();
        }
        
        public void setStopper(boolean b) {
            stopper = b;
        }
        
        public void open() {
            try {
                streamIn = new DataInputStream(socket.getInputStream());
            } catch (IOException ioe) {
		System.err.println("Error getting input stream: " + ioe);
		login.stop();
            }
        }
        
        public void close() {
            try {
                if (streamIn != null) streamIn.close();
            } catch(IOException ioe) {
                System.err.println("Error closing input stream: " + ioe);
            }
	}
        
        @Override
        public void run() {            
            
            while (!stopper) {
            	try {
                   	login.handle(streamIn.readUTF());
            	} catch(IOException ioe) {
                    System.out.println("Listening error: " + ioe.getMessage());
                    login.stop();
                    stopper = true;
            	}
            }
        }
        
}

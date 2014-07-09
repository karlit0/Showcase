package pagru_v05;

import java.net.*;
import java.io.*;

public class AdminWindowThread extends Thread {

	private Socket			socket 	= null;
	private AdminWindow  	adminWindow   = null;
	private DataInputStream	streamIn	= null;
	private boolean         stopper       = false;
        
        public AdminWindowThread(AdminWindow _adminWindow, Socket _socket) {
            adminWindow = _adminWindow;
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
            	adminWindow.stop();
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
                   	adminWindow.handle(streamIn.readUTF());
            	} catch(IOException ioe) {
                    System.out.println("Listening error: " + ioe.getMessage());
                    adminWindow.stop();
                    stopper = true;
            	}
            }
        }
        
}

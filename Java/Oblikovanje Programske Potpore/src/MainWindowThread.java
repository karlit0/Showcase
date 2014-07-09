package pagru_v05;

import java.net.*;
import java.io.*;

public class MainWindowThread extends Thread {
	
	
	private Socket			socket 	= null;
	private MainWindow  	mainWindow   = null;
	private DataInputStream	streamIn	= null;
        
        public MainWindowThread(MainWindow _mainWindow, Socket _socket) {
            mainWindow = _mainWindow;
            socket = _socket;
            open();
        }
        
        
        public void open() {
            try {
                streamIn = new DataInputStream(socket.getInputStream());
            } catch (IOException ioe) {
            	System.err.println("Error getting input stream: " + ioe);
            	mainWindow.stop();
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
            
        	boolean stopper = false;
        	
        	while (!stopper) {
        		try {
        			mainWindow.handle(streamIn.readUTF());
        		} catch(IOException ioe) {
        			System.out.println("Listening error: " + ioe.getMessage());
        			mainWindow.stop();
        			stopper = true;
        		}
        	}
        }
        
}

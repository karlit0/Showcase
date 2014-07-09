package com.example.geofencingclient;

import android.util.Log; 
  
import com.example.tcpserver.Item; 
  
import java.io.*; 
import java.net.InetAddress; 
import java.net.InetSocketAddress; 
import java.net.Socket; 
import java.nio.channels.SocketChannel; 
import java.util.List; 
   
public class TCPClient { 
   
    public static final String SERVERIP = "10.129.0.203"; 
    public static final int SERVERPORT = 4444; 
    private OnLocationsReceived mLocationsListener = null; 
   
    public TCPClient(OnLocationsReceived listener){ 
        mLocationsListener = listener; 
    } 
      
   
    public void run() { 
   
        try { 
  
            Log.e("TCP Client", "C: Connecting..."); 
              
            SocketChannel sChannel = SocketChannel.open(); 
            sChannel.configureBlocking(true); 
            //set socketchannel 
            sChannel.connect(new InetSocketAddress(SERVERIP, SERVERPORT)); 
              
            //set objectinputstream 
            ObjectInputStream ois = new ObjectInputStream(sChannel.socket().getInputStream()); 
   
            //read locations from input stream 
            List<Item> locations = (List<Item>) ois.readObject(); 
         
            //notify android app of locations 
            if (locations != null && mLocationsListener != null) { 
                mLocationsListener.locationsReceived(locations); 
            } 
  
              
        } catch (Exception e) {  
            Log.e("TCP", "C: Error", e); 
        } 
   
    } 
   
    public interface OnLocationsReceived { 
        public void locationsReceived(List<Item> locations); 
    }     
}
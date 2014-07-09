package pagru_v05;

public class MainWindowThreadRefresher extends Thread {

	private MainWindow  	mainWindow   = null;
	private boolean         stopper       = false;
        
        public MainWindowThreadRefresher(MainWindow _mainWindow) {
            mainWindow = _mainWindow;
        }
        
        public void setStopper(boolean b) {
            stopper = b;
        }        
                
        @Override
        public void run() {            
            
           do {            
        	   mainWindow.send("GIVEFLIST" + " " + mainWindow.getMyUsername()); /* posalji serveru poruku da zelis Friend listu*/ 
        	   try {
        		   sleep(3000); /* cekaj 3 sekunde pa posalji ponovno */
        	   } catch (InterruptedException e) {
					e.printStackTrace();
        	   }
            } while(!stopper); /* nacin da MainWindow zaustavi ovaj thread (kada se gasi MainWindow) */
        }
        
}

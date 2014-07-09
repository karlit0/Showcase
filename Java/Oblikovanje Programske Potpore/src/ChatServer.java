package pagru_v05;

import java.net.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.io.*;
import java.text.*;
import java.util.*;


/*
 * 	rad sa bazom podataka:
 * 	kada se upali server, povuce iz baze sve (ako ih uopce ima) registrirane korisnike i ubaci ih u listu users_registered
 *  tokom rada servera, ta lista se moze prosirivati (korisnik se moze registrirati pri spajanju na server)
 * 	kada se server gasi, spremi sadasnje stanje liste users_registered u bazu (mora se ocistiti baza i isponova upisati lista u nju)
 *  
 *  mozda je tesko napravit tako da se sprema lista u bazu tek kada se server gasi, jer onda treba biti pazljiv kako gasiti program ChatServera
 *  jer vecinom znam gasit program samo sa CTRL+C, a s time bi instant prekinuo izvodjenje programa - bez da se lista spasi
 *  
 *  mozd je "bolje" napravit da sa svakom promjenom liste users_registered (registriranje novog korisnika ili brisanje korisnika)
 *  server zapisuje tu promjenu u bazu (najjednostavnije obrisat cijelu bazu pa unijet iznova listu registriranih korisnika) 
 *  
 *  mozda nije potrebno brisanje cijele baze nego se moze sa INSERT i DELETE to jednostavno napraviti
 */

public class ChatServer implements Runnable {

	private ChatServerThread clients[] = new ChatServerThread[maxBrClients];
	private ServerSocket    server	= null;
	private Thread      thread	= null;
	private int         clientCount = 0;
	private List<User>  users_registered;	/* lista registriranih korisnika (inicijalizirana u Database.java, limit 50 */
	private List<User>  users_online = new ArrayList<User>(50);	/* lista online usera */
	private static int  maxBrClients = 20; /*defaultni broj prijavljenih korisnika, ukoliko nista ne pise u datoteci*/
	private Spremnik[] buffer = new Spremnik[maxBrClients]; 
	public int buffSize = 0; //broj Spremnika u bufferu
	
	
	class Spremnik{
		String[] messages = new String[10]; /* max. 10 poruka spremljeno */
		String owner = null; //vlasnik spremnika
	}
	
	public ChatServer(int port, String IP) {
		try {
			System.out.println("Binding to port " + port + " and IP " + IP + ", please wait ...");
			server = new ServerSocket();
			SocketAddress socAdd = new InetSocketAddress(IP,port);
			server.bind(socAdd);
			System.out.println("Server started: " + server);
			
			/*	dohvacanje registriranih korisnika iz baze, i ispis istih */
			users_registered = Database.getRegisteredUsers();
			if (users_registered == null)
				System.out.println("Error loading registered users from database");
			System.out.println("Registered users: (username) (password)");
			for (int i=0; i<users_registered.size(); i++){
				initBuffer(users_registered.get(i).getUsername());
				System.out.println((i+1) + ": " + users_registered.get(i).getUsername() + " " +
							users_registered.get(i).getPassword());
			}
			

			start();
		} catch(IOException ioe) {
			System.out.println("Can not bind to port " + port + ": " + ioe.getMessage());
		}
	}
	
	public List<User> getRegUsers() {
		/*
		 *  metoda za dohvacanje registriranih usera (koju koristi chatserverthread za dohvacanje usera i onda ih salje klijentu
		 *  metoda ce evoluirati u metodu za dohvacanje prijatelja umjesto svih registriranih usera
		 */
		return users_registered;
	}
	
	public String getFriends(String userUsername) {
		String friends = Database.getFriends(userUsername);
		return friends;	
	}
	
	public synchronized void addFriends(String userUsername, String friendUsername) {
		/* provjeri je li taj friend postoji u listi reg usera */		
		boolean friendExists = false;
		for(int i=0; i<users_registered.size(); i++)
			if (friendUsername.equals(users_registered.get(i).getUsername()))
				friendExists = true;
		if (friendExists == false)
			return;	/* ako friend kojeg pokusavamo dodat ne postoji u listi reg usera, prekini cijelu operaciju
			 (moze se prilagodit da vrati neku poruku da user zna zasto nije uspio dodat nekog) */
		Database.addFriends(userUsername, friendUsername);
		Database.listAllFriends();
	}
	
	//postavlja vlasnike spremnika, to ce trebati promjeniti kada budemo iz baze mogli ucitati velicinu spremnika
	
	void initBuffer(String usr){
		Spremnik help = new Spremnik();
		for(int i=0; i<10; ++i){
			help.messages[i]="";
		}
		help.owner = usr;
		buffer[buffSize]=help;
		buffSize++;
	}
	
	//poruka se u polju poruka nalazi na istom indexu na kojem se u polju sender-a nalazi korisnik koji ju je
	//poslao
	
	void saveInBuffer(String receiver, String msg){
		Spremnik help = new Spremnik();
		for(int i=0; i < buffSize; ++i) {//za svakog registriranog korisnika postojat ce Spremnik
			if( buffer[i].owner.equals(receiver) ) {
				String[] msgs = buffer[i].messages;
				
				for(int j=0; j<msgs.length; ++j){
					if(msgs[j].equals("")){ /* ako je slobodno mjesto */
						msgs[j] = msg;
						break;
					}
				}
				
				help.messages=msgs;				
				help.owner=receiver;
				
				buffer[i] = help;
				return;
			}
		}
	}
	
	public void checkInbox (String usr){
		for(int i=0;i<buffSize;++i){
			if (buffer[i] != null)
				if(buffer[i].owner.equals(usr)){
					System.out.println("uso sam u inbox");
					String[] msg = new String[10];
					msg = buffer[i].messages;
					for(int j=0; j<msg.length; ++j){
						if( !(msg[j].equals("")) ){
							handle(usr, msg[j]);
						}
					}
				}
			}
	}
	

	public synchronized void handle(String targetUsername, String msg) {
		ChatServerThread targetClient = findClient(targetUsername);
		if (targetClient == null)	/* ako nije online user, sacuvaj poruku */
			saveInBuffer(targetUsername, msg);
		else
			targetClient.send(msg);
	}
		
	public synchronized int tryregister(String username, String password) {
		/*
            provjeri postoji li vec takav registrirani korisnik (sa istim usernameom)
            ako postoji vrati 0 (fail), 
            ako ne postoji dodaj ga u bazu, dodaj ga u listu registriranih korisnika
                i vrati 1 (ok)
		 */
                                    
		/* provjeri ima il takav reg. korisnik */
		for (int i=0; i<users_registered.size(); i++)
			if ( username.equals( users_registered.get(i).getUsername() ) )
				return 0;
            
		/* takav registrirani korisnik ne postoji, dodaj ga u listu i bazu */            
		User user = new User(username, password);
		Database.addUser(user);
		users_registered.add(user);
            
		System.out.println("New User added: " + username + " " + password);
		return 1;
	}
        
    public synchronized int checkIfOnline(User user){				
		if (user == null)
			return -1;	/* nije pronasao usera u listi registriranih usera	*/
		else {
			/* provjeri je li taj user online */
			
		for (int i=0; i<users_online.size(); i++)	/* provjera po kljucu - username */
			if (user.getUsername().equals(users_online.get(i).getUsername()))
				return 0;	/* taj user je vec online, vrati neuspjesan login (return 0)	*/
		}
		return 1;
    }
        
	public synchronized int checklogin(String username, String password) {

		/*
		 *  provjeri postoji li registrirani korisnik takav - ako ga nema fja vraca -1
		 * 	ako postoji, provjeri je li online
		 *  ako je online, login je neuspjesan - fja vraca 0
		 *  ako nije online, ubaci tog usera u listu online usera, vrati uspjesan login
		 */
		User user = null;
    	for (int i=0; i<users_registered.size(); i++)
			if (username.equals(users_registered.get(i).getUsername()) &&
					password.equals(users_registered.get(i).getPassword()))
				user = users_registered.get(i);
		int ret;
		ret = checkIfOnline(user);
		if(ret == -1)
			return ret;
		if(ret == 1){
			users_online.add(user);
			return ret;
		}
		return 0;
	}
	
	public synchronized void addGroup(List<String> users) {
		/* obavijesti ostale usere o grupi */
		String msg = "GROUP";
		for (int i=0; i<users.size(); i++)
			msg = msg + " " + users.get(i); /* sastavi poruku */
		for (int i=0; i<users.size(); i++) { /* posalji poruku svima */
			ChatServerThread targetClient = findClient(users.get(i));
			if (targetClient != null)	/* ako je online user */
				targetClient.send(msg); // TODO ako je offline stavit poruku u spremnik ili nest
		}
		
		/* dodaj grupu u databazu */
		Database.addGroup(users);

	}
	
	public synchronized List<Group> getGroups(String userUsername) {
		List<Group> groups = Database.getGroups(userUsername);
		return groups;
	}
	
	
	@Override
	public void run() {
		while (thread != null) {
			try {
				System.out.println("Waiting for a client ...");
				addThread(server.accept());
			} catch(IOException ioe) {
				System.out.println("Server accept error: " + ioe);
				stop();
			}
		}
	}
		
	public void start() {
		if (thread == null) {
			thread = new Thread(this);
			thread.start();
		}
	}
	
	public void stop() {
		if (thread != null) {
			thread.stop();
			thread = null;
		}
	}
	
	private ChatServerThread findClient(String usrnm) {
		for (int i = 0; i < clientCount; i++)
			if (clients[i].getUsername().equals(usrnm))
				return clients[i];
		return null;
	}
	
	private int findClientpos(int ID) {
		for (int i = 0; i < clientCount; i++)
			if (clients[i].getID() == ID)
				return i;
		return -1;
	}
	
	public synchronized String getTime(){
		DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
		Calendar cal = Calendar.getInstance();
		String time;
		time = dateFormat.format(cal.getTime());
		return time;
	}
	
		
	
	public synchronized void setOffline(String username) {
		for (int i=0; i<users_online.size(); i++)
			if (username.equals(users_online.get(i).getUsername())) {
				users_online.remove(i);	/* ako pronadjes tog usera u listi online korisnika (a trebao bi), izbaci ga	*/
				return;
			}
	}
	
	public synchronized void remove(int ID) {
		
		int pos = findClientpos(ID);
		if (pos >= 0) {
			ChatServerThread toTerminate = clients[pos];
			
			/* izbaci tog usera iz liste online korisnika	*/
			setOffline(clients[pos].getUsername());
						
			System.out.println("Removing client thread " + ID + " at " + pos);
			if (pos < clientCount-1)
				for (int i = pos+1; i < clientCount; i++)	/* shiftanje elemenata liste */
					clients[i-1] = clients[i];
			clientCount--;
			try {
				toTerminate.close();
			} catch (IOException ioe) {
				System.out.println("Error closing thread: " + ioe);
			}
			toTerminate.stop();
		}			
	}
	
	public void addThread(Socket socket) {
		if (clientCount < maxBrClients) {
			System.out.println("Client accepted: " +  socket);
			clients[clientCount] = new ChatServerThread(this, socket);
			try {
				clients[clientCount].open();
				clients[clientCount].start();
				clientCount++;
			} catch(IOException ioe) {
				System.out.println("Error opening thread: " + ioe);
			}
		} 
		else
			System.out.println("Client refused: maximum " + maxBrClients + " reached.");
	}
	
	public static void main(String args[]) {
		ChatServer server = null;
		final String FILE_NAME = "//home//nicole//workspace//OPPv2//src//params.txt"; //parametri su pohranjeni u datoteci params.txt, mora se nalaziti
																				      //u src folderu, ili je potrebno ovdje namjestiti path
		
		/*try {
			Scanner reader = new Scanner(new File(FILE_NAME));
			String IP = reader.nextLine();
			int port = reader.nextInt();
			maxBrClients = reader.nextInt();
			server = new ChatServer(port,IP);
			reader.close();
		} catch (FileNotFoundException e) {*/
			// TODO Auto-generated catch block
			Scanner sc = new Scanner(System.in);
			//System.out.println("Enter IP: ");
			//String IP = sc.nextLine();
			String IP = "0.0.0.0"; /* pretpostavljam ip 0.0.0.0 */
			System.out.print("Enter port: ");
			int port = sc.nextInt();
			server = new ChatServer(port,IP);
			sc.close();
		//} 
	}
}
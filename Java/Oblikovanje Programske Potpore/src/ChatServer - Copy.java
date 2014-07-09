package pagru_v05;

//package Paketic;
//
//import java.net.*;
//import java.util.ArrayList;
//import java.util.InputMismatchException;
//import java.util.List;
//import java.util.Scanner;
//import java.io.*;
//
///*
// * 	rad sa bazom podataka:
// * 	kada se upali server, povuce iz baze sve (ako ih uopce ima) registrirane korisnike i ubaci ih u listu users_registered
// *  tokom rada servera, ta lista se moze prosirivati (korisnik se moze registrirati pri spajanju na server)
// * 	kada se server gasi, spremi sadasnje stanje liste users_registered u bazu (mora se ocistiti baza i isponova upisati lista u nju)
// *  
// *  mozda je tesko napravit tako da se sprema lista u bazu tek kada se server gasi, jer onda treba biti pazljiv kako gasiti program ChatServera
// *  jer vecinom znam gasit program samo sa CTRL+C, a s time bi instant prekinuo izvodjenje programa - bez da se lista spasi
// *  
// *  mozd je "bolje" napravit da sa svakom promjenom liste users_registered (registriranje novog korisnika ili brisanje korisnika)
// *  server zapisuje tu promjenu u bazu (najjednostavnije obrisat cijelu bazu pa unijet iznova listu registriranih korisnika) 
// *  
// *  mozda nije potrebno brisanje cijele baze nego se moze sa INSERT i DELETE to jednostavno napraviti
// */
//
//public class ChatServer implements Runnable {
//
//	private ChatServerThread clients[] = new ChatServerThread[50];
//	private ServerSocket    server	= null;
//	private Thread      thread	= null;
//	private int         clientCount = 0;
//	private List<User>  users_registered;	/* lista registriranih korisnika (inicijalizirana u Database.java, limit 50 */
//	private List<User>  users_online = new ArrayList<User>(50);	/* lista online usera */
//	
//	public ChatServer(int port) {
//		try {
//			System.out.println("Binding to port " + port + ", please wait ...");
//			server = new ServerSocket(port);
//			System.out.println("Server started: " + server);
//			
//			/*	dohvacanje registriranih korisnika iz baze, i ispis istih */
//			users_registered = Database.getRegisteredUsers();
//			if (users_registered == null)
//				System.out.println("Error loading registered users from database");
//			System.out.println("Registered users: (username) (password)");
//			for (int i=0; i<users_registered.size(); i++)
//				System.out.println((i+1) + ": " + users_registered.get(i).getUsername() + " " +
//							users_registered.get(i).getPassword());
//				
//			start();
//		} catch(IOException ioe) {
//			System.out.println("Can not bind to port " + port + ": " + ioe.getMessage());
//		}
//	}
//	
//        public synchronized int tryregister(String username, String password) {
//            /*
//            provjeri postoji li vec takav registrirani korisnik (sa istim usernameom)
//            ako postoji vrati 0 (fail), 
//            ako ne postoji dodaj ga u bazu, dodaj ga u listu registriranih korisnika
//                i vrati 1 (ok)
//            */
//                                    
//            /* provjeri ima il takav reg. korisnik */
//            for (int i=0; i<users_registered.size(); i++)
//			if ( username.equals( users_registered.get(i).getUsername() ) )
//				return 0;
//            
//            /* takav registrirani korisnik ne postoji, dodaj ga u listu i bazu */            
//            User user = new User(username, password);
//            Database.AddUser(user);
//            users_registered.add(user);
//            
//            System.out.println("New User added: " + username + " " + password);
//            return 1;
//        }
//        
//	public synchronized int checklogin(String username, String password) {
//
//		/*
//		 *  provjeri postoji li registrirani korisnik takav
//		 * 	ako postoji, provjeri je li online
//		 *  ako je online, login je neuspjesan
//		 *  ako nije online, ubaci tog usera u listu online usera, vrati uspjesan login
//		 */
//		
//		User user = null;
//		
//		/*	pronadji je li postoji registrirani korisnik sa zadanim usernameom i passom	*/
//		for (int i=0; i<users_registered.size(); i++)
//			if (username.equals(users_registered.get(i).getUsername()) &&
//					password.equals(users_registered.get(i).getPassword()))
//				user = users_registered.get(i);
//				
//		if (user == null)
//			return -1;	/* nije pronasao usera u listi registriranih usera	*/
//		else {
//			/* provjeri je li taj user online */
//			
//		for (int i=0; i<users_online.size(); i++)	/* provjera po kljucu - username */
//			if (user.getUsername().equals(users_online.get(i).getUsername()))
//				return 0;	/* taj user je vec online, vrati neuspjesan login (return 0)	*/
//		
//		/* user nije pronadjen u listi online usera, tako da ga dodaj u tu listu i vrati uspjesan login*/
//		users_online.add(user);
//		return 1;
//			
//		}
//	}
//	
//	@Override
//	public void run() {
//		while (thread != null) {
//			try {
//				System.out.println("Waiting for a client ...");
//				addThread(server.accept());
//			} catch(IOException ioe) {
//				System.out.println("Server accept error: " + ioe);
//				stop();
//			}
//		}
//	}
//		
//	public void start() {
//		if (thread == null) {
//			thread = new Thread(this);
//			thread.start();
//		}
//	}
//	
//	public void stop() {
//		if (thread != null) {
//			thread.stop();
//			thread = null;
//		}
//	}
//	
//	private int findClient(int ID) {
//		for (int i = 0; i < clientCount; i++)
//			if (clients[i].getID() == ID)
//				return i;
//		return -1;
//	}
//	
//	public synchronized void handle(int ID, String input) {
//		if (input.equals(".bye")) {
//			clients[findClient(ID)].send(".bye");
//			remove(ID);
//		} else
//			for (int i = 0; i < clientCount; i++)
//				clients[i].send(ID + ": " + input);
//	}
//	
//	/* dodatni handle za "whisper funkciju" - /w targetID(port) msg */
//	public synchronized void handle(int senderID, int targetID, String msg) {
//		if (findClient(targetID) == -1)
//			clients[findClient(senderID)].send("User with ID " + targetID + " does not exist.");
//		else if (senderID == targetID)
//			clients[findClient(targetID)].send(msg);
//		else
//			clients[findClient(targetID)].send("[Private message " + senderID + "] : " + msg);
//		
//	}
//
//	public synchronized void setOffline(String username) {
//		for (int i=0; i<users_online.size(); i++)
//			if (username.equals(users_online.get(i).getUsername())) {
//				users_online.remove(i);	/* ako pronadjes tog usera u listi online korisnika (a trebao bi), izbaci ga	*/
//				return;
//			}
//	}
//	
//	public synchronized void remove(int ID) {
//		
//		int pos = findClient(ID);
//		if (pos >= 0) {
//			ChatServerThread toTerminate = clients[pos];
//			
//			/* izbaci tog usera iz liste online korisnika	*/
//			setOffline(clients[pos].getUsername());
//						
//			System.out.println("Removing client thread " + ID + " at " + pos);
//			if (pos < clientCount-1)
//				for (int i = pos+1; i < clientCount; i++)	/* shiftanje elemenata liste */
//					clients[i-1] = clients[i];
//			clientCount--;
//			try {
//				toTerminate.close();
//			} catch (IOException ioe) {
//				System.out.println("Error closing thread: " + ioe);
//			}
//			toTerminate.stop();
//		}			
//	}
//	
//	public void addThread(Socket socket) {
//		if (clientCount < clients.length) {
//			System.out.println("Client accepted: " +  socket);
//			clients[clientCount] = new ChatServerThread(this, socket);
//			try {
//				clients[clientCount].open();
//				clients[clientCount].start();
//				clientCount++;
//			} catch(IOException ioe) {
//				System.out.println("Error opening thread: " + ioe);
//			}
//		} 
//		else
//			System.out.println("Client refused: maximum " + clients.length + " reached.");
//	}
//	
//	public static void main(String args[]) {
//		ChatServer server = null;
//		
//		Scanner sc = new Scanner(System.in);
//		System.out.print("Enter port: ");
//		try {
//			int port = sc.nextInt();
//			server = new ChatServer(port);
//		} catch(InputMismatchException ime) {
//			System.out.println("That is not a number");
//		}
//		sc.close();
//		
//	}
//
//	
//}

/*
 * MainWindow.java
 *
 * Created on 26.12.2013., 16:00:23
 */

/*
 *  (Ines A.) Prva izmjena: nastanak, dodavanje elemenata GUI-u
 *  (Ines A.) 27.12. druga izmjena: premještanje elemenata na dodatni panel,
 *                                  dodavanje elemenata
 *  (Antonio S.) 1.1.2014.: dodavanje paketa, promjena konstruktora
 */

package pagru_v05;

import java.io.*;
import java.net.*;
import java.util.*;

import javax.swing.*;

import java.awt.event.*;

import javax.swing.text.Position.Bias;

import java.awt.Color;
import java.awt.Font;

/**
 *
 * @author MadHatress
 */

// TODO popravit ono selektiranje i refreshanje

public class MainWindow extends javax.swing.JFrame {

    private Socket 			socket 				= null;
    private DataOutputStream streamOut			= null;
    private MainWindowThread mainWindowThread 	= null;
    private MainWindowThreadRefresher mainWindowThreadRefresher = null;
    private String 			myUsername;
    private MessageWindow 	msgWindows[]		 = new MessageWindow[50]; // pretpostavljam da ima max. 50 razgovora odjednom
    private int 			msgCount			 = 0;
    private int				groupCount			 = 0;
    private List<String>	friends				 = null;
    private List<Group>		groups				= new ArrayList<Group>();
	private	DefaultListModel groupModel 		= new DefaultListModel();
    public String path = "C:\\Users\\Public\\msglog.txt";
    public Color color;
    public Font nfont;
    
    /** Creates new form MainWindow
     * @param _socket */
    public MainWindow(Socket _socket, String _username) {
        initComponents();
        setTitle("Main Window");
        myUsername = _username;
        UsernameLabel.setText(myUsername);
        socket = _socket;
        try {
			streamOut = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
		} catch (IOException e1) {
			System.out.println("Failed to open output stream.");
			e1.printStackTrace();
		}
        

        mainWindowThread = new MainWindowThread(this, socket);	/* thread koji slusa odgovore od servera */
        mainWindowThread.start();
        mainWindowThreadRefresher = new MainWindowThreadRefresher(this); /* thread koji salje serveru zahtjev za Friend listom (svake 3 sekunde) */
        mainWindowThreadRefresher.start();
        
        ViewFriendsList.setText("Friends List");
        ViewFriendsList.setState(true);
        GroupsPanel.setVisible(ViewGroupList.getState());
        BlockedPanel.setVisible(ViewBlockedList.getState());

        this.setVisible(true);
        this.addWindowListener(new java.awt.event.WindowAdapter() {
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        if (socket != null) try {
                            socket.close();
                        } catch (IOException ioe) {
                            System.err.println("Error closing socket: " + ioe.getMessage());
                        }
                        closeMsgWindows();	/* pozatvaraj (ako ih ima) otvorene MessageWindowe */
                        mainWindowThreadRefresher.setStopper(true); /* zaustavi thread */
                        Login.main(null);   /* starta se ispocetka Login 
                                (mora se bas startati main metoda, jer je tamo dodan listener
                                pri zatvaranju prozora, da se zatvori i socket/stream */                       
                    }
                });
        
        /* Metoda za double click elementa na listi */      
        FriendsList.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {                
                if (evt.getClickCount() == 2) {            	
                	String sender = myUsername;
                	List<String> targets = new ArrayList<String>();
                	String target = FriendsList.getSelectedValue().toString(); /* ovdje i tak moze biti samo 1 frend oznacen */
                	targets.add(target);
                	if (findMsgWin(targets) == null) { /* ako taj msgwin nije vec otvoren */
                    	openMsgWin(sender, targets);
                	}
                }
            }
        });
        
        GroupsList.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                   	String sender = myUsername;
                	List<String> targets = new ArrayList<String>();
                	
               	  	String delims = "[ ]+";			 /*parsiranje stringa */
               	  	String group_string = GroupsList.getSelectedValue().toString();
               	  	String[] tokens = group_string.split(delims);
               	  	for (int i=1; i<tokens.length; i++)
               	  		targets.add(tokens[i]);
                	
                	if (findMsgWin(targets) == null) { /* ako taj msgwin nije vec otvoren */
                    	openMsgWin(sender, targets);
                	}
                }
            }
        });
        
        send("GIVEGROUPS" + " " + myUsername);	/* posalji serveru zahtjev za grupama u kojima se ja nalazim */
    }
    
    public String getMyUsername() {
    	return myUsername;
    }
    
    public void createGroup(List<String> friends_list, boolean iCreated) {
    	/* bool varijabla govori jesam ja kreiro grupu (stisnuo gumb Create) ili je ova grupa dosla preko servera
    	 * ovo je korisno za znati tako da znam jel da saljem poruku serveru info o novoj grupi ili ne (ne moze se nist brejkat,
    	 * ali bi se cloggo server pomalo) */
    	/* grupe se trebaju razlikovati po clanovima - unique su po skupu clanova */   	  	
    	  	
    	Collections.sort(friends_list);
    	
   	  	Group newGroup = new Group();
   	  	newGroup.groupMembers = friends_list;
   	  	   	  	
   	  	
   	  	/*	provjeri ima li dopusten broj ljudi u toj grupi (max. 4 frenda + user) i ima li barem 2 frenda oznacena */
   	  	if (friends_list.size() < 3 || friends_list.size() > 5)
   	  		return;
   	  
   	  	/* provjeri postoji li takva grupa vec */
   	  	for (int i=0; i<groups.size(); i++)
   	  		if (newGroup.equals(groups.get(i)))
   	  			return;	/* ako postoji, zaustavi ovu metodu	*/
   	  	

   	  	/* ako ne postoji, dodaj je */
   	 	groupCount++;
    	String group_string = "Group" + groupCount + ":";
    	String group_string_forServer = "GROUP";
        
   	  	for (int i=0; i < friends_list.size(); i++) { 
   	  		if ( !(friends_list.get(i).equals(myUsername)) )
   	  			group_string = group_string + " " + friends_list.get(i);
   	  		group_string_forServer = group_string_forServer + " " + friends_list.get(i);
   	  	}  
   	  	
   	  	groups.add(newGroup);
   	
        groupModel.addElement(group_string);            
         
        GroupsList.setModel(groupModel);
        GroupsList.setFont(new Font("Tahoma",Font.BOLD,11));
        
        /* obavijesti server o novoj grupi */
        if (iCreated)
        	send(group_string_forServer);
    }
    
    public void closeMsgWindows() { /* zatvara sve otvorene MessageWindowe (poziva se pri ga�enju MainWindowa) */    	
    	for (int i=0; i < msgCount; i++)
    		msgWindows[i].dispose();
    }
    
	public void send(String msg) {        
		try {
			streamOut.writeUTF(msg);
			streamOut.flush();
		} catch(IOException ioe) {
			System.out.println("ERROR sending " + ioe.getMessage());			
		}
	}
	
	public void handle(String msg) {
		/* handlea poruke od servera (odnosno od MainWindowThreada koji slusa poruka od servera)*/
		System.out.println("Idem handleat ovaj msg: " + msg);
      	
   	  	String delims = "[ ]+";			/* parsiranje stringa */
   	  	String[] tokens = msg.split(delims);
      
   	  	if (tokens[0].equals("SENDINGFLIST"))
   	  		refreshFriends(msg);
   	  	
   	  	if (tokens[0].equals("MSG")) {
   	  		String sender = myUsername;   	 // var za otvaranje prozora MsgWin 		
   	  		
   	  		/* ovdje se sender i target obrnu,
   	  		 * pojasnjenje: user1 zatrazi komunikaciju s userom2, otvara msgwindow gdje je user1 sender a user2 target
   	  		 * ovdje user2 sazna za taj zahtjev, otvara msgwindow gdje je user2 sender a user1 target
   	  		 * */
   	  		
   	  		String whoSentMsg = tokens[1];
   	  		List<String> targets = new ArrayList<String>();
   	  		int beginIndex = 0;
   	  		
   	  		beginIndex = 3 + 1 + whoSentMsg.length() + 1;
   	  				
   	  		for (int i=2; !(tokens[i].equals("ENDOFUSERS")); i++) {
    			targets.add(tokens[i]);
    			beginIndex += tokens[i].length() + 1; 
   	  		}
   	  		
   	  		Collections.sort(targets);
   	  		
   	  		beginIndex += "ENDOFUSERS".length() + 1;
   	  		
   	  		String msg_text = msg.substring(beginIndex);
  
   	  		
   	  		MessageWindow targetMsgWin = findMsgWin(targets);
   	  		if (targetMsgWin == null) /* ako msgwin nije otvoren*/
   	  			targetMsgWin = openMsgWin(sender, targets);
   	  			
   	  		String message = msg.substring(beginIndex);	/* uzeta je samo poruka, izbacene su kljucne rijeci koje se nalaze prije nje */
   	  	  	targetMsgWin.handle(msg_text, whoSentMsg);
   	  	}
   	  	
   		if (tokens[0].equals("GROUP")){
   	  		int beginIndex = 5 + 1;
   	  		String group_string = msg.substring(beginIndex);

   	   	  	String _delims = "[ ]+";			/* parsiranje stringa */
   	   	  	String[] _tokens = group_string.split(delims);
   	   	  	
   	   	  	List<String> group = new ArrayList<String>();
   	   	  	
   	   	  	for (int i=0; i<_tokens.length; i++)
   	   	  		group.add(_tokens[i]);
   	   	  	
   	   	  	createGroup(group, false);   	   	  	
   	  	}
	}
	
	public MessageWindow openMsgWin(String senderUsername, List<String> targetUsername) {
		msgWindows[msgCount] = new MessageWindow(MainWindow.this, senderUsername, targetUsername);
		msgWindows[msgCount].setVisible(true);
		msgCount++;		
		return msgWindows[msgCount-1];
	}

	/* uklanja msgWin (zna koji po targetusernameu) iz liste msgWindows*/
	public void removeMsgWin(List<String> targetUsername) {
		int pos = findMsgWinpos(targetUsername);
		if (pos >= 0) {
			System.out.println("Removing MsgWindow[" + pos + "]");
			if (pos < msgCount-1) /* ako nije zadnji clan u listi */
				for (int i = pos+1; i < msgCount; i++) /* shiftaj sve desne clanove ulijevo */
					msgWindows[i-1] = msgWindows[i];
			msgCount--;
		}
	}
	
	public MessageWindow findMsgWin(List<String> targetUsername) {
		for (int i=0; i < msgCount; i++) {
			if (msgWindows[i].getTargetUsername().equals(targetUsername))
				return msgWindows[i];
		}
		return null;
	}
	
	private int findMsgWinpos(List<String> targetUsername) {
		for (int i = 0; i < msgCount; i++)
			if (msgWindows[i].getTargetUsername().equals(targetUsername))
				return i;
		return -1;
	}
	
      
	public void refreshFriends(String friends_string){
    	/*
    	 *	ova metoda se pozove iz handle metode - handle zna da ovdje posalje string po prvoj kljucnoj rijeci - SENDINGFLIST
    	 */
    	
        /*
         * Dio koda koji nariktava listu prijatelja
         * Ideja moze biti sljedeca: importamo vamo listu prijatelja (cijelu,
         * onako iz baze) i sortiramo ju.
         * Onda importamo listu tih ljudi koji su online i oni koji su u presjeku,
         * budu boldani.
         */
        
    	/*
    	parsiraj friends string: frendovi odvojeni delimiterom " ", prva rijec je kljucna rijec - nju preskocit!
    	*/
 	  	String delims = "[ ]+";			/* parsiranje stringa */
   	  	String[] tokens = friends_string.split(delims);
   	  	
    	List<String> listy = new ArrayList<String>();
    	
        Collections.sort(listy);
              
   	  	for (int i=1; i < tokens.length; i++) { /* krece se od polozaja 1 jer je na tokens[0] kljucna rijec */
   	  		listy.add(tokens[i]);
   	  	}

        /* provjeri ima li promjene 
        if (listy.equals(friends)) {
        	return;	/*	ako nema promjene, zaustavi ovu metodu (nema potrebe za refreshom) 
        } else {
        	friends = listy;
        }
*/
    
   	  	       
        // Kod dolje je za sortiranje (jer mora biti sortirana lista)

        Collections.sort(listy);

        /*
         * Ovdje osim DefaultListModel moze biti jos� neka od dvije vrste - vidjet
         * cemo koja je osim ove najzgodnija.
         * U for petlji se prodjuu svi elementi te liste i dodaju se u model koji
         * se onda bubne u listu friendova. Slicna stvar je i za preostale
         * dvije liste.
         */

        DefaultListModel model = new DefaultListModel();
        for (int i=0; i<listy.size(); i++) {
        	/* dohvati sve usere osim admina i samog sebe */
        	if (!(listy.get(i).equals("admin")) && !(listy.get(i).equals(myUsername)))
        		model.addElement(listy.get(i));            
        }
        //Bubnu se dakle u listu i onda budu boldani drugom naredbom
        
        FriendsList.setModel(model);
        FriendsList.setFont(new Font("Tahoma",Font.BOLD,11));


/*
        GroupsList.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                JList GroupsList = (JList)evt.getSource();
                if (evt.getClickCount() == 2) {
                    MessageWindow msgwin1 = new MessageWindow();
                    msgwin1.setVisible(true);
                }
            }
        });
  */      
    }
        
    public Color Colorize () {
       return color;
    }
    
    public void stop() {
        try {
            if (streamOut != null)  streamOut.close();
            if (socket != null)     socket.close();
        } catch (IOException ioe) {
            System.err.println("Error closing ...");
            System.exit(0);
        }
        mainWindowThread.close();        
    }
    
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroup2 = new javax.swing.ButtonGroup();
        buttonGroup3 = new javax.swing.ButtonGroup();
        UsernameLabel = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        FriendsList = new javax.swing.JList();
        jLabel2 = new javax.swing.JLabel();
        GroupsPanel = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        GroupsList = new javax.swing.JList();
        jToolBar1 = new javax.swing.JToolBar();
        AddFriendButton = new javax.swing.JButton();
        BlockFriendButton = new javax.swing.JButton();
        DeleteFriendButton = new javax.swing.JButton();
        jSeparator6 = new javax.swing.JToolBar.Separator();
        CreateGroupButton = new javax.swing.JButton();
        DeleteGroupButton = new javax.swing.JButton();
        jSeparator7 = new javax.swing.JToolBar.Separator();
        OptionsButton = new javax.swing.JButton();
        OnlineRadio = new javax.swing.JRadioButton();
        AwayRadio = new javax.swing.JRadioButton();
        BusyRadio = new javax.swing.JRadioButton();
        StealthRadio = new javax.swing.JRadioButton();
        SearchForFriend = new javax.swing.JTextField();
        SearchForFriendButton = new javax.swing.JButton();
        BlockedPanel = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        BlockedList = new javax.swing.JList();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        LogOutMainMenu = new javax.swing.JMenuItem();
        ExitMainMenu = new javax.swing.JMenuItem();
        MainAddFriendMenu = new javax.swing.JMenu();
        AddFriendMenu = new javax.swing.JMenuItem();
        BlockFriendMenu = new javax.swing.JMenuItem();
        DeleteFriendMenu = new javax.swing.JMenuItem();
        FindFriendMenu = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        CreateGroupMenu = new javax.swing.JMenuItem();
        DeleteGroupMenu = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JPopupMenu.Separator();
        jMenu5 = new javax.swing.JMenu();
        SendMsgFriendMenu = new javax.swing.JMenuItem();
        SendMsgGroupMenu = new javax.swing.JMenuItem();
        OptionsMenu = new javax.swing.JMenuItem();
        jMenu3 = new javax.swing.JMenu();
        ViewFriendsList = new javax.swing.JCheckBoxMenuItem();
        ViewGroupList = new javax.swing.JCheckBoxMenuItem();
        ViewBlockedList = new javax.swing.JCheckBoxMenuItem();
        jSeparator3 = new javax.swing.JPopupMenu.Separator();
        ViewSettings = new javax.swing.JMenuItem();
        jMenu4 = new javax.swing.JMenu();
        Troubleshooting = new javax.swing.JMenuItem();
        jSeparator4 = new javax.swing.JPopupMenu.Separator();
        About = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        UsernameLabel.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        UsernameLabel.setText("Username");

        FriendsList.setFont(nfont);
        FriendsList.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item1", "item2", "item3", "item4", "item5", "item6", "item7", "item8", "item9", "item10", "item11", "item12", "item13", "item14", "item15", "item16", "item17", "item18", "item19", "item20", "item21", "item22", "item23", "item24", "item25", "item26" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane2.setViewportView(FriendsList);

        jLabel2.setFont(nfont);
        jLabel2.setText("Friends:");

        jLabel3.setFont(nfont);
        jLabel3.setText("Groups:");

        GroupsList.setFont(nfont);
        jScrollPane3.setViewportView(GroupsList);

        javax.swing.GroupLayout GroupsPanelLayout = new javax.swing.GroupLayout(GroupsPanel);
        GroupsPanel.setLayout(GroupsPanelLayout);
        GroupsPanelLayout.setHorizontalGroup(
            GroupsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(GroupsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(GroupsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 144, Short.MAX_VALUE)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 144, Short.MAX_VALUE))
                .addContainerGap())
        );
        GroupsPanelLayout.setVerticalGroup(
            GroupsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(GroupsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 398, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jToolBar1.setRollover(true);

        AddFriendButton.setFont(nfont);
        AddFriendButton.setText("Add a friend");
        AddFriendButton.setFocusable(false);
        AddFriendButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        AddFriendButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        AddFriendButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AddFriendButtonActionPerformed(evt);
            }
        });
        jToolBar1.add(AddFriendButton);

        BlockFriendButton.setFont(nfont);
        BlockFriendButton.setText("Block a friend");
        BlockFriendButton.setFocusable(false);
        BlockFriendButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        BlockFriendButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        BlockFriendButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BlockFriendButtonActionPerformed(evt);
            }
        });
        jToolBar1.add(BlockFriendButton);

        DeleteFriendButton.setFont(nfont);
        DeleteFriendButton.setText("Delete a friend");
        DeleteFriendButton.setFocusable(false);
        DeleteFriendButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        DeleteFriendButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        DeleteFriendButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DeleteFriendButtonActionPerformed(evt);
            }
        });
        jToolBar1.add(DeleteFriendButton);
        jToolBar1.add(jSeparator6);

        CreateGroupButton.setFont(nfont);
        CreateGroupButton.setText("Create group");
        CreateGroupButton.setFocusable(false);
        CreateGroupButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        CreateGroupButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        CreateGroupButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CreateGroupButtonActionPerformed(evt);
            }
        });
        jToolBar1.add(CreateGroupButton);

        DeleteGroupButton.setFont(nfont);
        DeleteGroupButton.setText("Delete group");
        DeleteGroupButton.setFocusable(false);
        DeleteGroupButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        DeleteGroupButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(DeleteGroupButton);
        jToolBar1.add(jSeparator7);

        OptionsButton.setFont(nfont);
        OptionsButton.setText("Options");
        OptionsButton.setFocusable(false);
        OptionsButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        OptionsButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        OptionsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                OptionsButtonActionPerformed(evt);
            }
        });
        jToolBar1.add(OptionsButton);

        buttonGroup3.add(OnlineRadio);
        OnlineRadio.setFont(nfont);
        OnlineRadio.setSelected(true);
        OnlineRadio.setText("Online");
        OnlineRadio.setFocusable(false);
        OnlineRadio.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        OnlineRadio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                OnlineRadioActionPerformed(evt);
            }
        });

        buttonGroup3.add(AwayRadio);
        AwayRadio.setFont(nfont);
        AwayRadio.setText("Away");
        AwayRadio.setFocusable(false);
        AwayRadio.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);

        buttonGroup3.add(BusyRadio);
        BusyRadio.setFont(nfont);
        BusyRadio.setText("Busy");
        BusyRadio.setFocusable(false);
        BusyRadio.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        BusyRadio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BusyRadioActionPerformed(evt);
            }
        });

        buttonGroup3.add(StealthRadio);
        StealthRadio.setFont(nfont);
        StealthRadio.setText("Stealth");
        StealthRadio.setFocusable(false);
        StealthRadio.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);

        SearchForFriendButton.setFont(nfont);
        SearchForFriendButton.setText("Search");
        SearchForFriendButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SearchForFriendButtonActionPerformed(evt);
            }
        });

        jLabel4.setFont(nfont);
        jLabel4.setText("Blocked:");

        BlockedList.setFont(nfont);
        jScrollPane1.setViewportView(BlockedList);

        javax.swing.GroupLayout BlockedPanelLayout = new javax.swing.GroupLayout(BlockedPanel);
        BlockedPanel.setLayout(BlockedPanelLayout);
        BlockedPanelLayout.setHorizontalGroup(
            BlockedPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(BlockedPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(BlockedPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 180, Short.MAX_VALUE)
                    .addComponent(jLabel4))
                .addContainerGap())
        );
        BlockedPanelLayout.setVerticalGroup(
            BlockedPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(BlockedPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 396, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jMenu1.setText("Main");
        jMenu1.setFont(nfont);

        LogOutMainMenu.setFont(nfont);
        LogOutMainMenu.setText("Log Out");
        LogOutMainMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                LogOutMainMenuActionPerformed(evt);
            }
        });
        jMenu1.add(LogOutMainMenu);

        ExitMainMenu.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Q, java.awt.event.InputEvent.CTRL_MASK));
        ExitMainMenu.setFont(nfont);
        ExitMainMenu.setText("Exit");
        ExitMainMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ExitMainMenuActionPerformed(evt);
            }
        });
        jMenu1.add(ExitMainMenu);

        jMenuBar1.add(jMenu1);

        MainAddFriendMenu.setText("Edit");
        MainAddFriendMenu.setFont(nfont);

        AddFriendMenu.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_A, java.awt.event.InputEvent.CTRL_MASK));
        AddFriendMenu.setFont(nfont);
        AddFriendMenu.setText("Add a friend");
        AddFriendMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AddFriendMenuActionPerformed(evt);
            }
        });
        MainAddFriendMenu.add(AddFriendMenu);

        BlockFriendMenu.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_B, java.awt.event.InputEvent.CTRL_MASK));
        BlockFriendMenu.setFont(nfont);
        BlockFriendMenu.setText("Block a friend");
        BlockFriendMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BlockFriendMenuActionPerformed(evt);
            }
        });
        MainAddFriendMenu.add(BlockFriendMenu);

        DeleteFriendMenu.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_DELETE, java.awt.event.InputEvent.CTRL_MASK));
        DeleteFriendMenu.setFont(nfont);
        DeleteFriendMenu.setText("Delete a friend");
        DeleteFriendMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DeleteFriendMenuActionPerformed(evt);
            }
        });
        MainAddFriendMenu.add(DeleteFriendMenu);

        FindFriendMenu.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F, java.awt.event.InputEvent.CTRL_MASK));
        FindFriendMenu.setFont(nfont);
        FindFriendMenu.setText("Find a friend");
        FindFriendMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                FindFriendMenuActionPerformed(evt);
            }
        });
        MainAddFriendMenu.add(FindFriendMenu);
        MainAddFriendMenu.add(jSeparator1);

        CreateGroupMenu.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_G, java.awt.event.InputEvent.CTRL_MASK));
        CreateGroupMenu.setFont(nfont);
        CreateGroupMenu.setText("Create group");
        CreateGroupMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CreateGroupMenuActionPerformed(evt);
            }
        });
        MainAddFriendMenu.add(CreateGroupMenu);

        DeleteGroupMenu.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_DELETE, java.awt.event.InputEvent.ALT_MASK));
        DeleteGroupMenu.setFont(nfont);
        DeleteGroupMenu.setText("Delete group");
        DeleteGroupMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DeleteGroupMenuActionPerformed(evt);
            }
        });
        MainAddFriendMenu.add(DeleteGroupMenu);
        MainAddFriendMenu.add(jSeparator2);

        jMenu5.setText("Send message to...");

        SendMsgFriendMenu.setFont(nfont);
        SendMsgFriendMenu.setText("... a friend");
        SendMsgFriendMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SendMsgFriendMenuActionPerformed(evt);
            }
        });
        jMenu5.add(SendMsgFriendMenu);

        SendMsgGroupMenu.setFont(nfont);
        SendMsgGroupMenu.setText("... a group");
        SendMsgGroupMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SendMsgGroupMenuActionPerformed(evt);
            }
        });
        jMenu5.add(SendMsgGroupMenu);

        MainAddFriendMenu.add(jMenu5);

        OptionsMenu.setFont(nfont);
        OptionsMenu.setText("Options");
        OptionsMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                OptionsMenuActionPerformed(evt);
            }
        });
        MainAddFriendMenu.add(OptionsMenu);

        jMenuBar1.add(MainAddFriendMenu);

        jMenu3.setText("View");
        jMenu3.setFont(nfont);

        ViewFriendsList.setFont(nfont);
        ViewFriendsList.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ViewFriendsListActionPerformed(evt);
            }
        });
        jMenu3.add(ViewFriendsList);

        ViewGroupList.setFont(nfont);
        ViewGroupList.setText("Group list");
        ViewGroupList.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ViewGroupListActionPerformed(evt);
            }
        });
        jMenu3.add(ViewGroupList);

        ViewBlockedList.setFont(nfont);
        ViewBlockedList.setText("Blocked list");
        ViewBlockedList.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ViewBlockedListActionPerformed(evt);
            }
        });
        jMenu3.add(ViewBlockedList);
        jMenu3.add(jSeparator3);

        ViewSettings.setFont(nfont);
        ViewSettings.setText("View settings");
        ViewSettings.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ViewSettingsActionPerformed(evt);
            }
        });
        jMenu3.add(ViewSettings);

        jMenuBar1.add(jMenu3);

        jMenu4.setText("Help");
        jMenu4.setFont(nfont);

        Troubleshooting.setFont(nfont);
        Troubleshooting.setText("Troubleshooting");
        Troubleshooting.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TroubleshootingActionPerformed(evt);
            }
        });
        jMenu4.add(Troubleshooting);
        jMenu4.add(jSeparator4);

        About.setFont(nfont);
        About.setText("About");
        About.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AboutActionPerformed(evt);
            }
        });
        jMenu4.add(About);

        jMenuBar1.add(jMenu4);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, 579, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(OnlineRadio)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(BusyRadio)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(AwayRadio)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(StealthRadio)
                        .addGap(337, 337, 337))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(UsernameLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 559, Short.MAX_VALUE)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(SearchForFriend, javax.swing.GroupLayout.DEFAULT_SIZE, 183, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(GroupsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(BlockedPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(SearchForFriendButton))))
                        .addContainerGap())))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(OnlineRadio)
                    .addComponent(BusyRadio)
                    .addComponent(AwayRadio)
                    .addComponent(StealthRadio))
                .addGap(18, 18, 18)
                .addComponent(UsernameLabel)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(SearchForFriendButton)
                    .addComponent(SearchForFriend, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(BlockedPanel, 0, 0, Short.MAX_VALUE)
                    .addComponent(GroupsPanel, 0, 0, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(23, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void AddFriendMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AddFriendMenuActionPerformed
        AddFriend addFriendWin = new AddFriend(this);
        addFriendWin.setVisible(true);
    }//GEN-LAST:event_AddFriendMenuActionPerformed

    private void BlockFriendMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BlockFriendMenuActionPerformed
        InstructionsOnBlock blockFriendWin = new InstructionsOnBlock(new javax.swing.JFrame(), true);
        blockFriendWin.setVisible(true);
    }//GEN-LAST:event_BlockFriendMenuActionPerformed

    private void DeleteFriendMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DeleteFriendMenuActionPerformed
        InstructionsOnDelete delFriendWin = new InstructionsOnDelete(new javax.swing.JFrame(), true);
        delFriendWin.setVisible(true);
    }//GEN-LAST:event_DeleteFriendMenuActionPerformed

    private void FindFriendMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_FindFriendMenuActionPerformed
        SearchForFriend.requestFocusInWindow();
    }//GEN-LAST:event_FindFriendMenuActionPerformed

    private void CreateGroupMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CreateGroupMenuActionPerformed
        InstructionsOnCreateGroup helpWin = new InstructionsOnCreateGroup(new javax.swing.JFrame(), true);
        helpWin.setVisible(true);
    }//GEN-LAST:event_CreateGroupMenuActionPerformed

    private void DeleteGroupMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DeleteGroupMenuActionPerformed
        InstructionsOnDeleteGroup helpDelWin = new InstructionsOnDeleteGroup(new javax.swing.JFrame(), true);
        helpDelWin.setVisible(true);
    }//GEN-LAST:event_DeleteGroupMenuActionPerformed

    private void SendMsgFriendMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SendMsgFriendMenuActionPerformed
        InstructionsOnMessageFriend helpMessageFriend = new InstructionsOnMessageFriend(new javax.swing.JFrame(), true);
        helpMessageFriend.setVisible(true);
    }//GEN-LAST:event_SendMsgFriendMenuActionPerformed

    private void SendMsgGroupMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SendMsgGroupMenuActionPerformed
        InstructionsOnMessageGroup helpMessageGroup = new InstructionsOnMessageGroup(new javax.swing.JFrame(), true);
        helpMessageGroup.setVisible(true);
    }//GEN-LAST:event_SendMsgGroupMenuActionPerformed

    private void OptionsMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_OptionsMenuActionPerformed
         OptionsWindowMain options = new OptionsWindowMain(new javax.swing.JFrame(), true, this);
         options.setVisible(true);
         path = options.getPath();
    }//GEN-LAST:event_OptionsMenuActionPerformed

    private void ViewSettingsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ViewSettingsActionPerformed
         ViewSettings setView = new ViewSettings(new javax.swing.JFrame(), true);
         setView.setVisible(true);
         
         nfont = setView.FontIt();
         getContentPane().removeAll();
         initComponents();
         UsernameLabel.setText(myUsername);
         ViewFriendsList.setText("Friends List");
         ViewFriendsList.setState(true);
         GroupsPanel.setVisible(ViewGroupList.getState());
         BlockedPanel.setVisible(ViewBlockedList.getState());
         
         color = setView.collectColor();
         this.getContentPane().setBackground(color);
         jToolBar1.setBackground(color);
         OnlineRadio.setBackground(color);
         BusyRadio.setBackground(color);
         AwayRadio.setBackground(color);
         StealthRadio.setBackground(color);
         AddFriendButton.setBackground(color);
         BlockFriendButton.setBackground(color);
         DeleteFriendButton.setBackground(color);
         CreateGroupButton.setBackground(color);
         DeleteGroupButton.setBackground(color);
         OptionsButton.setBackground(color);
         jMenuBar1.setBackground(color);
         GroupsPanel.setForeground(color);
         BlockedPanel.setForeground(color);
    }//GEN-LAST:event_ViewSettingsActionPerformed

    private void TroubleshootingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TroubleshootingActionPerformed
        Troubleshooting trouble = new Troubleshooting(new javax.swing.JFrame(), true, color);
        trouble.setVisible(true);
    }//GEN-LAST:event_TroubleshootingActionPerformed

    private void AboutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AboutActionPerformed
        About about = new About(new javax.swing.JFrame(), true);
        about.setVisible(true);
    }//GEN-LAST:event_AboutActionPerformed

    private void AddFriendButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AddFriendButtonActionPerformed
        AddFriend addFriendWin = new AddFriend(this);
        addFriendWin.setVisible(true);
    }//GEN-LAST:event_AddFriendButtonActionPerformed

    private void BlockFriendButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BlockFriendButtonActionPerformed
    }//GEN-LAST:event_BlockFriendButtonActionPerformed

    private void DeleteFriendButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DeleteFriendButtonActionPerformed
    }//GEN-LAST:event_DeleteFriendButtonActionPerformed

    private void CreateGroupButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CreateGroupButtonActionPerformed
    	if ( !(FriendsList.isSelectionEmpty()) ) { /* ako je itko uopce oznacen */
    		List<String> friends_list = FriendsList.getSelectedValuesList();   
    		friends_list.add(myUsername);
    		createGroup(friends_list, true);
    	}
    }//GEN-LAST:event_CreateGroupButtonActionPerformed

    private void OptionsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_OptionsButtonActionPerformed
        OptionsWindowMain options = new OptionsWindowMain(new javax.swing.JFrame(), true, this);
        options.setVisible(true);
        path = options.getPath();
    }//GEN-LAST:event_OptionsButtonActionPerformed

    private void OnlineRadioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_OnlineRadioActionPerformed
    }//GEN-LAST:event_OnlineRadioActionPerformed

    private void BusyRadioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BusyRadioActionPerformed
    }//GEN-LAST:event_BusyRadioActionPerformed

    private void SearchForFriendButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SearchForFriendButtonActionPerformed
         int indx = FriendsList.getNextMatch(SearchForFriend.getText(), WIDTH, Bias.Forward);
         if (indx != -1) {
           FriendsList.setSelectedValue(SearchForFriend.getText(), true);
       }
    }//GEN-LAST:event_SearchForFriendButtonActionPerformed

    private void LogOutMainMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_LogOutMainMenuActionPerformed
    	System.out.println("Triggeran LogOutMainMenuActionPerformed");
        if (socket != null) try {
                            socket.close();
                        } catch (IOException ioe) {
                            System.err.println("Error closing socket: " + ioe.getMessage());
                        }
                        Login.main(null);
                        closeMsgWindows();
                        this.dispose();
                        /* starta se ispocetka Login
                                (mora se bas startati main metoda, jer je tamo dodan listener
                                pri zatvaranju prozora, da se zatvori i socket/stream */   
    }//GEN-LAST:event_LogOutMainMenuActionPerformed

    private void ExitMainMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ExitMainMenuActionPerformed
    	System.out.println("Triggeran ExitMainMenuActionPerformed");
        if (socket != null) try {
                            socket.close();
                        } catch (IOException ioe) {
                            System.err.println("Error closing socket: " + ioe.getMessage());
                        }
                        Login.main(null);
                        closeMsgWindows();
                        this.dispose();
                        /* starta se ispocetka Login
                                (mora se bas startati main metoda, jer je tamo dodan listener
                                pri zatvaranju prozora, da se zatvori i socket/stream */   
    }//GEN-LAST:event_ExitMainMenuActionPerformed

    private void ViewFriendsListActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ViewFriendsListActionPerformed
        FriendsList.setVisible(ViewFriendsList.getState());
    }//GEN-LAST:event_ViewFriendsListActionPerformed

    private void ViewGroupListActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ViewGroupListActionPerformed
        GroupsPanel.setVisible(ViewGroupList.getState());
    }//GEN-LAST:event_ViewGroupListActionPerformed

    private void ViewBlockedListActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ViewBlockedListActionPerformed
        BlockedPanel.setVisible(ViewBlockedList.getState());
    }//GEN-LAST:event_ViewBlockedListActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem About;
    private javax.swing.JButton AddFriendButton;
    private javax.swing.JMenuItem AddFriendMenu;
    private javax.swing.JRadioButton AwayRadio;
    private javax.swing.JButton BlockFriendButton;
    private javax.swing.JMenuItem BlockFriendMenu;
    private javax.swing.JList BlockedList;
    private javax.swing.JPanel BlockedPanel;
    private javax.swing.JRadioButton BusyRadio;
    private javax.swing.JButton CreateGroupButton;
    private javax.swing.JMenuItem CreateGroupMenu;
    private javax.swing.JButton DeleteFriendButton;
    private javax.swing.JMenuItem DeleteFriendMenu;
    private javax.swing.JButton DeleteGroupButton;
    private javax.swing.JMenuItem DeleteGroupMenu;
    private javax.swing.JMenuItem ExitMainMenu;
    private javax.swing.JMenuItem FindFriendMenu;
    private javax.swing.JList FriendsList;
    private javax.swing.JList GroupsList;
    private javax.swing.JPanel GroupsPanel;
    private javax.swing.JMenuItem LogOutMainMenu;
    private javax.swing.JMenu MainAddFriendMenu;
    private javax.swing.JRadioButton OnlineRadio;
    private javax.swing.JButton OptionsButton;
    private javax.swing.JMenuItem OptionsMenu;
    private javax.swing.JTextField SearchForFriend;
    private javax.swing.JButton SearchForFriendButton;
    private javax.swing.JMenuItem SendMsgFriendMenu;
    private javax.swing.JMenuItem SendMsgGroupMenu;
    private javax.swing.JRadioButton StealthRadio;
    private javax.swing.JMenuItem Troubleshooting;
    private javax.swing.JLabel UsernameLabel;
    private javax.swing.JCheckBoxMenuItem ViewBlockedList;
    private javax.swing.JCheckBoxMenuItem ViewFriendsList;
    private javax.swing.JCheckBoxMenuItem ViewGroupList;
    private javax.swing.JMenuItem ViewSettings;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.ButtonGroup buttonGroup3;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenu jMenu5;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JPopupMenu.Separator jSeparator2;
    private javax.swing.JPopupMenu.Separator jSeparator3;
    private javax.swing.JPopupMenu.Separator jSeparator4;
    private javax.swing.JToolBar.Separator jSeparator6;
    private javax.swing.JToolBar.Separator jSeparator7;
    private javax.swing.JToolBar jToolBar1;
    // End of variables declaration//GEN-END:variables

}
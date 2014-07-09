/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pagru_v05;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.PrintWriter;
import java.awt.Component;
import java.awt.Font;
import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.*;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.DefaultListModel;
/*
 * AdminWindow.java
 *
 * Created on 02.01.2014., 00:36:50
 */
/**
 *
 * @author MadHatress
 */
public class AdminWindow extends javax.swing.JFrame {

    private Socket socket = null;
    private DataOutputStream streamOut = null;
    private AdminWindowThread adminWindowThread = null;
    private RegisterWin reg = null;
    private List<String> users = new ArrayList<String>();


    /** Creates new form AdminWindow */
    public AdminWindow(Socket _socket, String username) {
        initComponents();
        setTitle("Admin window");
        AdminUsername.setText(username);
        socket = _socket;
        try {
        	streamOut = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
        } catch (IOException e1) {
        	// TODO Auto-generated catch block
        	System.out.println("Failed to open output stream.");
        	e1.printStackTrace();
        }

        
        adminWindowThread = new AdminWindowThread(this, socket);
        adminWindowThread.start();
        
        /* posalji serveru zahtjev za friend listom */
        send("GIVEREGUSERS");
        
        this.setVisible(true);
        this.addWindowListener(new java.awt.event.WindowAdapter() {
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        if (socket != null) try {
                            socket.close();
                        } catch (IOException ioe) {
                            System.err.println("Error closing socket: " + ioe.getMessage());
                        }
                        Login.main(null);   /* starta se ispocetka Login
                                (mora se bas startati main metoda, jer je tamo dodan listener
                                pri zatvaranju prozora, da se zatvori i socket/stream */
                    }
                });
    }

    public void handle(String msg) {
    	System.out.println("Idem handleat ovaj msg: " + msg);
          	
    	String delims = "[ ]+";			/* parsiranje stringa */
    	String[] tokens = msg.split(delims);
          
    	if (tokens[0].equals("SENDINGREGUSERS"))
    		refreshRegUsers(msg);     	
    	
    	 if (msg.equals("REGISTEROK")) { /*  otvori onaj dialog koji kaze successful registration */
             SuccessRegister dialog = new SuccessRegister();
             dialog.setVisible(true);
             String newUser = reg.getUsername();
             addNewUser(newUser);
             if (reg != null)
             	reg.dispose();             
         }
    	 
    	 if (msg.equals("REGISTERFAIL")) {
         	try { /* provjera za slucaj lose definiranog puta za sliku */
         	WarningRegister warnReg = new WarningRegister(new javax.swing.JFrame(), true);
         	warnReg.setVisible(true);
         	} catch (Exception e) {        		
         	}
         }
    }
    
    public void saveInFile(String FILE_NAME){
    	String IP;
    	int port;
    	int maxBr;
    	IP = ServerIPAdmin.getText();
    	//if(IP.equals("")) send("Specify IP, please!");
    	port = Integer.parseInt(ServerPortAdmin.getText());
    	//if(port == 0) System.out.println("Specify port, please!");
    	maxBr = Integer.parseInt(MaxUserNoAdmin.getText());
    	Path pathe = Paths.get(FILE_NAME);
    }
    
    public void addNewUser(String newUser) {
    	users.add(newUser);
    	Collections.sort(users);
    	
    	DefaultListModel model = new DefaultListModel();
    	for (int i=0; i<users.size(); i++) {
    		model.addElement(users.get(i));
    	}

    	//Bubnu se dakle u listu i onda budu boldani drugom naredbom
    	
    	ListOfUsersAdmin.setModel(model);
    	ListOfUsersAdmin.setFont(new Font("Tahoma",Font.BOLD,11));
    }
    
    public void refreshRegUsers(String regusers_string) {
       	/*
   	   		parsiraj string: useri odvojeni delimiterom " ", prva rijec je kljucna rijec - nju preskocit!
       	 */
    	String delims = "[ ]+";			/* parsiranje stringa */
    	String[] tokens = regusers_string.split(delims);
    	   	  	
    	List<String> listy = new ArrayList<String>();
    	        
    	for (int i=1; i < tokens.length; i++) { /* krece se od polozaja 1 jer je na tokens[0] kljucna rijec */
    		listy.add(tokens[i]);
    	}
    	   	  	
    	// Kod dolje je za sortiranje (jer mora biti sortirana lista)
    	Collections.sort(listy);

    	users = listy;
    	/*
    	 * Ovdje osim DefaultListModel moze biti jos� neka od dvije vrste - vidjet
    	 * cemo koja je osim ove najzgodnija.
    	 * U for petlji se prodjuu svi elementi te liste i dodaju se u model koji
    	 * se onda bubne u listu friendova. Slicna stvar je i za preostale
    	 * dvije liste.
    	 */

    	DefaultListModel model = new DefaultListModel();
    	for (int i=0; i<listy.size(); i++) {
    		model.addElement(listy.get(i));
    	}

    	//Bubnu se dakle u listu i onda budu boldani drugom naredbom
    	
    	ListOfUsersAdmin.setModel(model);
    	ListOfUsersAdmin.setFont(new Font("Tahoma",Font.BOLD,11));
    	
    } 	
    
    
	public void send(String msg) {        
		try {
			streamOut.writeUTF(msg);
			streamOut.flush();
		} catch(IOException ioe) {
			System.out.println("ERROR sending " + ioe.getMessage());			
		}
	}
    
    public void stop() {
        try {
            if (streamOut != null)  streamOut.close();
            if (socket != null)     socket.close();
        } catch (IOException ioe) {
            System.err.println("Error closing ...");
            System.exit(0);
        }
        adminWindowThread.close();        
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        AdminUsername = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        ListOfUsersAdmin = new javax.swing.JList();
        DelUserAdminButton = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        MaxMessBuffSize = new javax.swing.JTextField();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel3 = new javax.swing.JLabel();
        ServerIPAdmin = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        ServerPortAdmin = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        MaxUserNoAdmin = new javax.swing.JTextField();
        AddUserAdminButton = new javax.swing.JButton();
        SaveButtonAdmin = new javax.swing.JButton();
        CancelButtonAdmin = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        AddUserAdminMenu = new javax.swing.JMenuItem();
        DelUserAdminMenu = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JPopupMenu.Separator();
        AdminCloseMenu = new javax.swing.JMenuItem();
        SetServerParamsMenu = new javax.swing.JMenu();
        jMenuItem3 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        AdminUsername.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        AdminUsername.setText("Username");

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 14));
        jLabel1.setText("List of all users:");

        jScrollPane1.setViewportView(ListOfUsersAdmin);

        DelUserAdminButton.setText("Delete user");
        DelUserAdminButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DelUserAdminButtonActionPerformed(evt);
            }
        });

        jLabel2.setText("Max message buffer size for user (in bytes):");

        MaxMessBuffSize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MaxMessBuffSizeActionPerformed(evt);
            }
        });

        jLabel3.setText("IP address of the server:");

        jLabel4.setText("Server port:");

        ServerPortAdmin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ServerPortAdminActionPerformed(evt);
            }
        });

        jLabel5.setText("Max number of users logged in:");

        AddUserAdminButton.setText("Add new user");
        AddUserAdminButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AddUserAdminButtonActionPerformed(evt);
            }
        });

        SaveButtonAdmin.setText("Save");
        SaveButtonAdmin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SaveButtonAdminActionPerformed(evt);
            }
        });

        CancelButtonAdmin.setText("Cancel");

        jMenu1.setText("File");

        AddUserAdminMenu.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_A, java.awt.event.InputEvent.CTRL_MASK));
        AddUserAdminMenu.setText("Add new user");
        jMenu1.add(AddUserAdminMenu);

        DelUserAdminMenu.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_DELETE, java.awt.event.InputEvent.CTRL_MASK));
        DelUserAdminMenu.setText("Delete user");
        jMenu1.add(DelUserAdminMenu);
        jMenu1.add(jSeparator2);

        AdminCloseMenu.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Q, java.awt.event.InputEvent.CTRL_MASK));
        AdminCloseMenu.setText("Close");
        AdminCloseMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AdminCloseMenuActionPerformed(evt);
            }
        });
        jMenu1.add(AdminCloseMenu);

        jMenuBar1.add(jMenu1);

        SetServerParamsMenu.setText("Edit");

        jMenuItem3.setText("Set server parameters");
        SetServerParamsMenu.add(jMenuItem3);

        jMenuBar1.add(SetServerParamsMenu);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(AdminUsername, javax.swing.GroupLayout.DEFAULT_SIZE, 386, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(AddUserAdminButton)
                            .addComponent(jLabel5)
                            .addComponent(jLabel4)
                            .addComponent(jLabel3)
                            .addComponent(MaxMessBuffSize, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(DelUserAdminButton)
                            .addComponent(jSeparator1, javax.swing.GroupLayout.DEFAULT_SIZE, 213, Short.MAX_VALUE)
                            .addComponent(ServerIPAdmin, javax.swing.GroupLayout.DEFAULT_SIZE, 213, Short.MAX_VALUE)
                            .addComponent(ServerPortAdmin, javax.swing.GroupLayout.DEFAULT_SIZE, 213, Short.MAX_VALUE)
                            .addComponent(MaxUserNoAdmin, javax.swing.GroupLayout.DEFAULT_SIZE, 213, Short.MAX_VALUE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(SaveButtonAdmin, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(CancelButtonAdmin, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(AdminUsername, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(AddUserAdminButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 313, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(DelUserAdminButton)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(MaxMessBuffSize, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(ServerIPAdmin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(ServerPortAdmin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(MaxUserNoAdmin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 28, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(SaveButtonAdmin)
                            .addComponent(CancelButtonAdmin))))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void DelUserAdminButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DelUserAdminButtonActionPerformed
    	String user_toRemove = ListOfUsersAdmin.getSelectedValue().toString();
    	
    	/*
    	 * Ovdje osim DefaultListModel moze biti jos� neka od dvije vrste - vidjet
    	 * cemo koja je osim ove najzgodnija.
    	 * U for petlji se prodjuu svi elementi te liste i dodaju se u model koji
    	 * se onda bubne u listu friendova. Slicna stvar je i za preostale
    	 * dvije liste.
    	 */

    	List<String> newUsers = new ArrayList<String>();
    	
    	DefaultListModel model = new DefaultListModel();
    	for (int i=0; i<users.size(); i++) {
    		if ( !(users.get(i).equals(user_toRemove)) ){
    			model.addElement(users.get(i));
    			newUsers.add(users.get(i));
    		}
    	}
    	
    	users = newUsers;

    	//Bubnu se dakle u listu i onda budu boldani drugom naredbom
    	
    	ListOfUsersAdmin.setModel(model);
    	ListOfUsersAdmin.setFont(new Font("Tahoma",Font.BOLD,11));
    	

        // TODO add your handling code here:
    }//GEN-LAST:event_DelUserAdminButtonActionPerformed

    private void MaxMessBuffSizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MaxMessBuffSizeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_MaxMessBuffSizeActionPerformed

    private void ServerPortAdminActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ServerPortAdminActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ServerPortAdminActionPerformed

    private void AddUserAdminButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AddUserAdminButtonActionPerformed
        reg = new RegisterWin(new javax.swing.JFrame(), true, AdminWindow.this);
        reg.setVisible(true);
    }//GEN-LAST:event_AddUserAdminButtonActionPerformed

    private void SaveButtonAdminActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SaveButtonAdminActionPerformed
        // TODO add your handling code here:
    	
    	saveInFile("//home//nicole//workspace//OPPv2//src//params.txt");
    	
    }//GEN-LAST:event_SaveButtonAdminActionPerformed

    private void AdminCloseMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AdminCloseMenuActionPerformed
        if (socket != null) try {
                         socket.close();
                        } catch (IOException ioe) {
                            System.err.println("Error closing socket: " + ioe.getMessage());
                        }
                        Login.main(null);   /* starta se ispocetka Login
                                (mora se bas startati main metoda, jer je tamo dodan listener
                                pri zatvaranju prozora, da se zatvori i socket/stream */   
    }//GEN-LAST:event_AdminCloseMenuActionPerformed

    /**
    * @param args the command line arguments
    */
    /*public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new AdminWindow().setVisible(true);
            }
        });
    }
*/
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton AddUserAdminButton;
    private javax.swing.JMenuItem AddUserAdminMenu;
    private javax.swing.JMenuItem AdminCloseMenu;
    private javax.swing.JLabel AdminUsername;
    private javax.swing.JButton CancelButtonAdmin;
    private javax.swing.JButton DelUserAdminButton;
    private javax.swing.JMenuItem DelUserAdminMenu;
    private javax.swing.JList ListOfUsersAdmin;
    private javax.swing.JTextField MaxMessBuffSize;
    private javax.swing.JTextField MaxUserNoAdmin;
    private javax.swing.JButton SaveButtonAdmin;
    private javax.swing.JTextField ServerIPAdmin;
    private javax.swing.JTextField ServerPortAdmin;
    private javax.swing.JMenu SetServerParamsMenu;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JPopupMenu.Separator jSeparator2;
    // End of variables declaration//GEN-END:variables

}

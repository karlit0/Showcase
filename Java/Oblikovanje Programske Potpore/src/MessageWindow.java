/*
 * MessageWindow.java
 *
 * Created on 26.12.2013., 18:18:28
 */

package pagru_v05;

import java.awt.event.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.io.*;

/**
 *
 * @author MadHatress
 */

/*
 * (Ines A., 27.12.2013.) = dodavanje elemenata na sucelje
 * (Anja �. haha 15.01.2014.) = dodavanje funkcije getTime() i ispis vremena unutar chat prozora
 */
public class MessageWindow extends javax.swing.JFrame {

	private MainWindow mainWindow;
	private String myUsername;
	private List<String> targetUsername;
	
	private ChatServer catchTime;

	
    /** Creates new form MessageWindow */
    public MessageWindow(MainWindow _mainWindow, String _myUsername, List<String> _targetUsername) {
        initComponents();
        setTitle(_targetUsername.toString());	//TODO mozd sa forom "lijepo" napisat u naslovu
        mainWindow = _mainWindow;
        myUsername = _myUsername;
        targetUsername = _targetUsername;
        
        /* doda listener koji javi MainWindowu da se ovaj msgwin gasi tak da moze izbacit iz liste
         * MainWindow zna o kojem se msgwindowu radi po targetUsernameu
         */
        this.addWindowListener(new java.awt.event.WindowAdapter() {
        	public void windowClosing(java.awt.event.WindowEvent e) {
                    
                    try { 
                        BufferedWriter writer = new BufferedWriter(new FileWriter(mainWindow.path, true));
                        writer.append(MsgBox.getText() + "\n");
                        writer.close();
                    } catch (IOException ex) {
                        WarningFile warn = new WarningFile(new javax.swing.JFrame(), true);
                        warn.setVisible(true);
                        System.out.println(ex);
                    }
                     
        		mainWindow.removeMsgWin(targetUsername);
        	}
		});
        
        System.out.println("OTVOREN MSG WINDOW: " + myUsername + " TO " + targetUsername );
    }
    
   
    public List<String> getTargetUsername() {
    	return targetUsername;
    }
    
    public synchronized String getTime(){
		DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
		Calendar cal = Calendar.getInstance();
		String time;
		time = dateFormat.format(cal.getTime());
		return time;
	}

    public void handle(String msg, String whoSentMsg) {
    	
    	String delims = "[ ]+";			/* parsiranje stringa */
        String[] tokens = msg.split(delims);
		
		String time = tokens[0];
		//TODO kada promijenim format msga, treba tu izvadit "sendera"
		String msg_text;
        int beginIndex = time.length() + 1; /* varijabla za fun substring (pocetno mjesto poruke u stringu) */
        msg_text = msg.substring(beginIndex);
    	
    	MsgBox.append("[" + time + "] " + whoSentMsg +": " + msg_text + "\n");    	
        MsgBox.setCaretPosition(MsgBox.getText().length() - 1);
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        MsgBox = new javax.swing.JTextArea();
        MessageTxt = new javax.swing.JTextField();
        SendButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        MsgBox.setColumns(20);
        MsgBox.setEditable(false);
        MsgBox.setRows(5);
        jScrollPane1.setViewportView(MsgBox);

        MessageTxt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MessageTxtActionPerformed(evt);
            }
        });

        SendButton.setText("Send");
        SendButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SendButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 384, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(MessageTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 315, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(SendButton, javax.swing.GroupLayout.DEFAULT_SIZE, 63, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 398, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(MessageTxt, javax.swing.GroupLayout.DEFAULT_SIZE, 25, Short.MAX_VALUE)
                    .addComponent(SendButton))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void SendButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SendButtonActionPerformed
    	
    	String time;
    	time = getTime();
    	String message = "MSG" + " " + myUsername;
    	// nadodat targete
    	for (int i=0; i<targetUsername.size(); i++)
    		message = message + " " + targetUsername.get(i).toString();
    	
    	message = message + " " + "ENDOFUSERS";
    	
    	//nadodat msg
    	message = message + " " + time + " " + MessageTxt.getText().toString();
    	
    	
    	mainWindow.send(message);
    	MsgBox.append("[" + time + "] " + myUsername + ": " +  MessageTxt.getText() + "\n");
        MsgBox.setCaretPosition(MsgBox.getText().length() - 1);
        MessageTxt.setText("");
        MessageTxt.requestFocusInWindow();
    }//GEN-LAST:event_SendButtonActionPerformed

    private void MessageTxtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MessageTxtActionPerformed
    }//GEN-LAST:event_MessageTxtActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField MessageTxt;
    private javax.swing.JTextArea MsgBox;
    private javax.swing.JButton SendButton;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables

}
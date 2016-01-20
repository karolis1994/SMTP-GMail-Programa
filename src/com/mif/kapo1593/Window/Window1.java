//Programa parase: Karolis Pockevicius

package com.mif.kapo1593.Window;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JTextField;

import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JList;

import com.mif.kapo1593.SMTPClientSocket.SMTPClientSocket;

import javax.swing.JPasswordField;
import javax.xml.bind.DatatypeConverter;

//Klase sugeneruota WindowsBuilder plugin'o.

public class Window1 {

	private JFrame frame;
	private JTextField idTextField;
	private JTextField toTextField;
	private JTextField subjectTextField;
	private JTextArea txtrYourMessage;
	private DefaultListModel<String> model;
	private JPasswordField passwordField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Window1 window = new Window1();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Window1() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame("Gmail Mail Sender");
		frame.setBounds(100, 100, 654, 344);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel lblFrom = new JLabel("Gmail ID:");
		lblFrom.setBounds(10, 8, 89, 14);
		frame.getContentPane().add(lblFrom);
		
		JLabel lblTo = new JLabel("To:");
		lblTo.setBounds(10, 55, 46, 14);
		frame.getContentPane().add(lblTo);
		
		JLabel lblSubject = new JLabel("Subject:");
		lblSubject.setBounds(10, 80, 46, 14);
		frame.getContentPane().add(lblSubject);
		
		idTextField = new JTextField();
		lblFrom.setLabelFor(idTextField);
		idTextField.setBounds(109, 8, 156, 20);
		frame.getContentPane().add(idTextField);
		idTextField.setColumns(10);
		
		toTextField = new JTextField();
		lblTo.setLabelFor(toTextField);
		toTextField.setBounds(109, 55, 156, 20);
		frame.getContentPane().add(toTextField);
		toTextField.setColumns(10);
		
		subjectTextField = new JTextField();
		lblSubject.setLabelFor(subjectTextField);
		subjectTextField.setBounds(109, 80, 156, 20);
		frame.getContentPane().add(subjectTextField);
		subjectTextField.setColumns(10);
		
		txtrYourMessage = new JTextArea();
		txtrYourMessage.setText("Your message.");
		txtrYourMessage.setBounds(10, 111, 255, 154);
		frame.getContentPane().add(txtrYourMessage);
		
		JButton btnSend = new JButton("Send");
		
		btnSend.setBounds(10, 276, 89, 23);
		frame.getContentPane().add(btnSend);
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
		});
		btnCancel.setBounds(109, 276, 89, 23);
		frame.getContentPane().add(btnCancel);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(275, 30, 353, 235);
		frame.getContentPane().add(scrollPane);
		
		JList<String> list = new JList<String>();
		scrollPane.setViewportView(list);
		frame.getContentPane().add(scrollPane);		
		model = new DefaultListModel<String>();
		list.setModel(model);		
		
		JLabel lblPassword = new JLabel("Password:");
		lblPassword.setBounds(10, 30, 89, 14);
		frame.getContentPane().add(lblPassword);
		
		passwordField = new JPasswordField();
		lblPassword.setLabelFor(passwordField);
		passwordField.setBounds(109, 30, 156, 20);
		frame.getContentPane().add(passwordField);
		
		JLabel lblFullIdex = new JLabel("Full ID (ex: jonasjonaitis@gmail.com");
		lblFullIdex.setLabelFor(idTextField);
		lblFullIdex.setBounds(275, 8, 259, 14);
		frame.getContentPane().add(lblFullIdex);
		model.addElement("Error list:");
		
		//"Send" mygtuko paspaudimo funkcija, kuri bendrauja su SMTP serveriu:
		//siuncia jam komandas, bei minimaliai apdoroja serverio atsakymus.
		btnSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					String response;
					SMTPClientSocket S = new SMTPClientSocket();
					if(!(response = S.in.readLine()).startsWith("220")){
						model.addElement(response.substring(4, response.length()));
						S.soc.close();					
					}
					else {
						S.send("HELO " + java.net.InetAddress.getLocalHost().getHostName());
						if(!(response = S.in.readLine()).startsWith("250")){					
							S.send("RSET");
							S.send("QUIT");
							model.addElement(response.substring(4, response.length()));
							S.soc.close();
						}
						else {
							S.send("AUTH LOGIN");
							if(!(response = S.in.readLine()).startsWith("334")){
								model.addElement(response.substring(4, response.length()));
								S.send("RSET");
								S.send("QUIT");
								S.soc.close();
							}
							else {
								S.send(new String(DatatypeConverter.printBase64Binary(idTextField.getText().getBytes())));
								if(!(response = S.in.readLine()).startsWith("334")){
									model.addElement(response.substring(4, response.length()));
									S.send("RSET");
									S.send("QUIT");
									S.soc.close();
								}
								else {
									S.send(new String(DatatypeConverter.printBase64Binary(new String(passwordField.getPassword()).getBytes())));
									if(!(response = S.in.readLine()).startsWith("235")){					
										S.in.readLine();
										model.addElement("Wrong username or password.");
										if(S.in.ready()) {
											model.removeElementAt(model.size() - 1);
											model.removeElementAt(model.size() - 1);
											model.addElement("Wrong gmail account settings,change them:");
											model.addElement("Login to your Gmail account.");
											model.addElement("Click the 'Cog' icon in the top right-hand corner of the Window and then click 'Settings'.");
											model.addElement("Go to the 'Accounts and Import' tab.");
											model.addElement("At Change 'account settings:' press 'Other Google Account settings'.");
											model.addElement("At 'Signing in' section, change 'Access for less secure apps.'");
										}
										S.send("RSET");
										S.send("QUIT");
										S.soc.close();
									}
									else {
										S.send("MAIL FROM:<" + idTextField.getText() + ">");
										S.in.readLine();
										S.send("RCPT TO:<" + toTextField.getText() + ">");
										if(!(response = S.in.readLine()).startsWith("250")){
											model.addElement("The recipient not found.");
											S.send("RSET");
											S.send("QUIT");
											S.soc.close();
										}
										else {
											S.send("DATA");
											if(!(response = S.in.readLine()).startsWith("354")){
												model.addElement(response.substring(4, response.length()));
												S.send("RSET");
												S.send("QUIT");
												S.soc.close();
											}
											else {
												S.send("Subject:" + subjectTextField.getText());
											    S.send(txtrYourMessage.getText());
											    S.send(".");
											    if(!(response = S.in.readLine()).startsWith("250")){
													model.addElement(response.substring(4, response.length()));
													S.send("RSET");
													S.send("QUIT");
													S.soc.close();
												}
											    else {
											    	model.addElement("Mail succesfuly sent.");
											    	S.send("QUIT");
													S.soc.close();						
											    }
											}
										}
									}
								}
							}
						}
					}					
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					System.out.println(e1);
				}
				passwordField.setText("");
			}
		});
	}
}

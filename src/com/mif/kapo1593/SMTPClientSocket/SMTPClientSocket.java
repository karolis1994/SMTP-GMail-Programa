//Programa parase: Karolis Pockevicius

package com.mif.kapo1593.SMTPClientSocket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.swing.JOptionPane;

public class SMTPClientSocket {
	
	public PrintWriter out;
	public BufferedReader in;
	public java.net.Socket soc;
	
	public SMTPClientSocket() {
		try {
			SSLSocket soc = (SSLSocket) ((SSLSocketFactory) SSLSocketFactory.getDefault()).createSocket(InetAddress.getByName("smtp.gmail.com"), 465);
			if(soc == null) {
				JOptionPane.showMessageDialog(null, "Connection to Gmail SMTP server failed.");
				System.exit(1);
			}	
	        out = new PrintWriter(soc.getOutputStream());
	        in = new BufferedReader(
	        		new InputStreamReader(soc.getInputStream()));
		} catch (UnknownHostException e) {
			System.err.println("Unkown Host.");
			System.exit(1);
        } catch (IOException e) {
        	System.err.println("Couldn't get I/O for the connection.");
        	e.printStackTrace();
        	System.exit(1);
        }
	}
	
	
	//Metodas, kuriuo zinute nusiunciama SMTP serveriui.
	public void send(String s) throws java.io.IOException {
	    if (s != null) {
	    	System.out.println(s);
	    	out.println(s);
	    	out.flush();
	    }
	  }
}

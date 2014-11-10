package com.ds.project.client;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.*;

public class PeerServer extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JTextArea textArea = new JTextArea(5, 30);
	JLabel lblServer = new JLabel("Server Activities");

	public PeerServer(String peerAddress, int peerPort) {
		// TODO Auto-generated constructor stub
//		setSize(600, 400);
//		setVisible(true);
//		setTitle("SERVER");
//		add(textArea);
//
//		JScrollPane scrollPane = new JScrollPane(textArea);
//		scrollPane.setPreferredSize(new Dimension(335, 250));
//
//		textArea.setLineWrap(true);
//		textArea.setWrapStyleWord(true);
//		textArea.setEditable(false);
//		scrollPane
//				.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
//		textArea.setBounds(20, 20, 350, 250);
//
//		setLayout(new FlowLayout());
//		add(scrollPane, SwingConstants.CENTER);
		
		
		ServerSocket peerServerSocket = null;
		Socket peerClientSocket = null;

		//System.out.println("Server started.");
//		textArea.append("Server started.");

		try {
			// Peer server starts at port 13001
			peerServerSocket = new ServerSocket(peerPort);
			while (true) {
//				textArea.append("Waiting for connection...");
				peerClientSocket = peerServerSocket.accept();
				System.out.println("Connected from "
						+ peerClientSocket.getInetAddress());

				new PeerServerThread(peerClientSocket).start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				peerServerSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("Hi from Server");
		System.out.println("Success");

		new PeerServer("sample",13001);
		System.out.println("test");
	}
}

package com.ds.project.server;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.*;

public class ChatServer extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JTextArea textArea = new JTextArea(5, 30);
	JLabel lblServer = new JLabel("Server Activities");

	public ChatServer() {
		// TODO Auto-generated constructor stub
		setSize(600, 400);
		setVisible(true);
		setTitle("SERVER");
		add(textArea);

		JScrollPane scrollPane = new JScrollPane(textArea);
		scrollPane.setPreferredSize(new Dimension(335, 250));

		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		textArea.setEditable(false);
		scrollPane
				.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		textArea.setBounds(20, 20, 350, 250);

		setLayout(new FlowLayout());
		add(scrollPane, SwingConstants.CENTER);
		
		
		ServerSocket serverSocket = null;
		Socket clientSocket = null;

		//System.out.println("Server started.");
		textArea.append("Server started.");

		try {
			// server starts at port 13000
			serverSocket = new ServerSocket(13000);
			while (true) {
				textArea.append("Waiting for connection...");
				clientSocket = serverSocket.accept();
				textArea.append("Connected from "
						+ clientSocket.getInetAddress());

				new ChatServerThread(clientSocket).start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				serverSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("Hi from Server");
<<<<<<< HEAD
		System.out.println("Success");

		new ChatServer();
=======
		System.out.println("test");
>>>>>>> branch 'master' of https://github.com/suni619/P2PChatRoomApplication.git
	}
}

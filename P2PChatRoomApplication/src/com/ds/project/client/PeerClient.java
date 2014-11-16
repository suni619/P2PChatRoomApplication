package com.ds.project.client;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class PeerClient extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JTextArea textArea = new JTextArea(5, 30);
	JTextField textField = new JTextField();

	public PeerClient() {
		// TODO Auto-generated constructor stub
		Socket socket = null;
		String serverIp = "localhost";
		int serverPort = 13000;
		setSize(600, 400);
		setVisible(true);
		setTitle("CLIENT");
		add(textArea);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		JScrollPane scrollPane = new JScrollPane(textArea);
		scrollPane.setPreferredSize(new Dimension(335, 250));

		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		textArea.setEditable(false);
		scrollPane
				.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		textArea.setBounds(20, 20, 350, 250);

		final JTextField userInputField = new JTextField(30);

		userInputField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				String messageFromUser = userInputField.getText();

				if (messageFromUser != null) {
					textArea.append("\nMe: " + messageFromUser);
					textArea.setCaretPosition(textArea.getDocument()
							.getLength());
					userInputField.setText("");
				}
			}
		});

		setLayout(new FlowLayout());
		add(userInputField, SwingConstants.CENTER);
		add(scrollPane, SwingConstants.CENTER);
		
		
		try {
			final DataOutputStream outStreamToChatServer;
			final DataInputStream inStreamFromChatServer;

			final String peerAddress = "localhost";
			final int peerPort = 13004;
			final String peerName = "user4";
			
			InetAddress serverAddress = InetAddress.getByName(serverIp);
			socket = new Socket(serverAddress, serverPort);
			System.out.println("Connected to server " + serverIp + " on "
					+ serverPort);

			inStreamFromChatServer = new DataInputStream(socket.getInputStream());
			outStreamToChatServer = new DataOutputStream(socket.getOutputStream());
			
			Thread peerServerThread = new Thread(new Runnable() {
				
				public void run() {
					// TODO Auto-generated method stub
					// create a peer server
					PeerServer peerServer = new PeerServer(peerAddress, peerPort);

				}
			});
			peerServerThread.setDaemon(true);
			peerServerThread.start();
			
			// send peer info (name, IP address and port)
			outStreamToChatServer.writeUTF(peerName + "delim" + "Server" + "delim" + "JOIN" + "delim" +
					peerAddress + "," +	peerPort);
			
			// receive peer join/quit message from chat server
			Thread receiverThread = new Thread(new Runnable() {
				
				public void run() {
					// TODO Auto-generated method stub
					// receive new peer join or peer quit information
					try {
						while(true){
							String inputMessageFromServer = inStreamFromChatServer.readUTF();
							String[] inputMessage = inputMessageFromServer.split("delim");
							String incomingPeerName = inputMessage[0];
							String messageType = inputMessage[2];
							String message = inputMessage[3];
							System.out.println("In Peer Cli MSG:" + inputMessageFromServer);
							if (messageType.equals("PEERJOIN")){
								// handle join
								// send chat room info to newly connected peer
								// retrieve chat room info from database
								String chatRoomInfo = "ChatRoom1";
								// connect to the peer server of newly connected peer
								String[] peerServerInfo = message.split(",");
								String peerServerAdd = peerServerInfo[0];
								int peerServerPort = Integer.parseInt(peerServerInfo[1]);
								PeerServerThread.peerSocketMap.put(incomingPeerName,peerServerAdd+"-"+peerServerPort);
								System.out.println("peerServerAdd: " + peerServerAdd + "peerServerPort:" + peerServerPort);
								Socket peerServerSocket;
								try {
									peerServerSocket = new Socket(peerServerAdd, peerServerPort);
									DataOutputStream outStreamToPeerServer = new DataOutputStream(peerServerSocket.getOutputStream());
									// send chat room info and current peer info (address and port of this peer server)
									outStreamToPeerServer.writeUTF(chatRoomInfo + "|" + peerAddress + "-" + peerPort + "|" + peerName);
									peerServerSocket.close();
								} catch (Exception e) {
									System.out.println("Peer Server socket not created exception");
									e.printStackTrace();
								}
								
							} else if(messageType.equals("PEERQUIT")) {
								// handle quit
								// update chat room info locally
								
								// update peer info locally
								PeerServerThread.peerSocketMap.remove(incomingPeerName);
								System.out.println("Peer Removed: " + PeerServerThread.peerSocketMap);
							}
						}
					} catch (IOException e) {
						// TODO Auto-generated catch block
						System.out.println("in 1");
						e.printStackTrace();
					}
				}
			});
			receiverThread.setDaemon(true);
			receiverThread.start();
			
			do {
				outStreamToChatServer.writeUTF(peerName + "delim" + "Server" + "delim" + "ALIVE" + "delim" +
						peerAddress + "," +	peerPort);
				Thread.sleep(2000);
				
			} while(true);			

		}
		
		catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("Hi from Client");
		System.out.println("Success.");
		new PeerClient();
	}

}

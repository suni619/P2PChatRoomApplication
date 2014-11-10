/**
 * Team members:
 * Jagadish Shivanna - 1001050680
 * Jyoti Salitra - 1001055011
 * Prerana Patil - 1001054381
 * Sunil Koundinya – 1000986932
 * 
 */
package com.ds.project.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.ds.project.entity.PeerInfo;

public class ChatServerThread extends Thread {
	protected Socket clientSocket;
	private static Map<String, PeerInfo> clientSocketMap = new ConcurrentHashMap<String, PeerInfo>();
	//private static Map<String, Set<String>> chatRoomUsersMap = new ConcurrentHashMap<String, Set<String>>();

	ChatServerThread(Socket clientSocket) {
		this.clientSocket = clientSocket;
	}

	public void run() {
		DataInputStream inputStream = null;
		DataOutputStream outputStream = null;
		String[] inputMsg = new String[4];
		String userName = "user"; // default username (not the case)
		try {
			inputStream = new DataInputStream(clientSocket.getInputStream());
			outputStream = new DataOutputStream(clientSocket.getOutputStream());
			// read username and password
			/*String authParam = inputStream.readUTF();

			// get the username part and hash of the password
			String[] authParams = authParam.split("\\(\\)_delim_\\(\\)");
			userName = authParams[0];
			String password = authParams[1];*/
			
			while(true) {
				// receive alive message
				try {
					String msg = inputStream.readUTF();
					String[] message = msg.split("delim");
					//System.out.println("Msg from PeerCli " + msg);
					//System.out.println("JOIN MESS " + message[2]);
					if(message[2].equals("JOIN"))
					{
						//System.out.println("Inside JOIN");
						// read peer server info for the first time
						String[] peerSeverInfo = message[3].split(",");
						String peerServerAddress = peerSeverInfo[0];
						String peerServerPort = peerSeverInfo[1];
						// store peer server info
						
						PeerInfo peerInfo = new PeerInfo();
						peerInfo.setName(message[0]);
						peerInfo.setAddress(peerServerAddress);
						peerInfo.setPort(Integer.parseInt(peerServerPort));
						peerInfo.setSocket(clientSocket);
						clientSocketMap.put(message[0], peerInfo);
						System.out.println("CS Map" + clientSocketMap);
						// send new peer server info to all other peers
						Set<String> mapKeys = clientSocketMap.keySet();
						for (String user : mapKeys) {
							System.out.println("User=" + user);
							if(!user.equals(message[0])) {
								PeerInfo peer = clientSocketMap.get(user);
								System.out.println("Peer if" + peer);
								Socket socketStream = peer.getSocket();
								System.out.println("Socket STR:" + socketStream);
								DataOutputStream outStream = new DataOutputStream(socketStream.getOutputStream());
								System.out.println("DOS:" + outStream);
							outStream.writeUTF("Server"+"delim"+user+"PEERJOIN"+"delim"+peerServerAddress+","+peerServerPort+"User Joined");
							}
							else {
								System.out.println("Peer else");
							}
						}
					}
					else {
						// read alive message subsequently
						//System.out.println("ALIVE from " + message[0]);
					}
					
					
				} catch (Exception e) {
					System.out.println("Peer disconnected");
					break;
				}
				
			}
			// authenticate (using information from the database)
			//boolean authSuccess = authenticate(userName, password);

			// on success
			/*			if (authSuccess) {
				// return list of available chat rooms
				clientSocketMap.put(userName, clientSocket);
				System.out.println(userName + " joined.");
				//String chatRoomList = getChatRoomList();
				outputStream.writeUTF("Authentication successful"
						+ chatRoomList);

				while (true) {
					try {
						String msgIn = inputStream.readUTF();
						System.out.println("Message from client: " + msgIn);
						inputMsg = msgIn.split("\\(\\)_delim_\\(\\)");
						System.out.println("Message from: " + inputMsg[0]);
						System.out.println("Message to: " + inputMsg[1]);
						System.out.println("Type: " + inputMsg[2]);
						System.out.println("Message: " + inputMsg[3]);
						String sender = inputMsg[0];
						String chatRoomName = inputMsg[1];
						String messageType = inputMsg[2];
						String message = inputMsg[3];

						// add user to chat room list if an user joins
						if (messageType.equals("JOIN")) {
							Set<String> chatRoomUsers = chatRoomUsersMap
									.get(chatRoomName);
							if (chatRoomUsers == null) {
								chatRoomUsers = new HashSet<String>();
							}
							chatRoomUsers.add(sender);
							chatRoomUsersMap.put(chatRoomName, chatRoomUsers);
						}
						if (messageType.equals("QUIT")) {
							Set<String> chatRoomUsers = chatRoomUsersMap
									.get(chatRoomName);
							if (chatRoomUsers == null) {
								chatRoomUsers = new HashSet<String>();
							}
							chatRoomUsers.remove(sender);
							chatRoomUsersMap.put(chatRoomName, chatRoomUsers);
						}
						Set<String> chatRoomUsers = chatRoomUsersMap
								.get(chatRoomName);
						if (messageType.equals("FETCH")&&chatRoomUsers!=null) {
							outputStream.writeUTF("Server "
									+ "()_delim_()" + chatRoomName
									+ "()_delim_()" + "FETCH"
									+ "()_delim_()" + chatRoomUsers.toString());
						} else {
							for (String user : chatRoomUsers) {
								if (clientSocketMap.containsKey(user)
										&& !user.equals(sender)) {
									System.out
											.println("User present. Sending message");
									Socket toSocket = clientSocketMap.get(user);
									DataOutputStream toSocketOutput = new DataOutputStream(
											toSocket.getOutputStream());
									toSocketOutput.writeUTF(sender
											+ "()_delim_()" + chatRoomName
											+ "()_delim_()" + messageType
											+ "()_delim_()" + message);
								} else if (!clientSocketMap.containsKey(user)) {
									System.out.println("User not present");
									outputStream.writeUTF("Server "
											+ "()_delim_()" + chatRoomName
											+ "()_delim_()" + "DATA"
											+ "()_delim_()" + chatRoomName
											+ " is Offline");
								}
							}
						}
					} catch (EOFException e) {
						System.out.println("Client disconnected.");
						break;
					} catch (IOException e) {
						e.printStackTrace();
						System.out.println("Client disconnected.");
						break;
					}
				}
			} else {
				// on failure, return failure message
				outputStream.writeUTF("Authentication failed");
			} */
			//System.out.println(clientSocketMap);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

/*	private String getChatRoomList() {
		ConnectDB connect = new ConnectDB();
		String chatRoomList = connect.getChatRoomList();
		return chatRoomList;
	}

	*//**
	 * authenticate the user and return if the authentication is successful or
	 * not
	 * 
	 * @param userName
	 * @param password
	 * @return
	 *//*
	private boolean authenticate(String userName, String password) {
		// perform database operation here
		ConnectDB connect = new ConnectDB();
		return connect.authenticate(userName, password);
	}*/
}

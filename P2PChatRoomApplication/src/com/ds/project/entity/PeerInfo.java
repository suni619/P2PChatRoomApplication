package com.ds.project.entity;

import java.net.Socket;


public class PeerInfo {

	private String name;
	private String address;
	private int port;
	private Socket socket;
	
	public PeerInfo() {
		// TODO Auto-generated constructor stub
		
	}

	@Override
	public String toString() {
		return "PeerInfo [name=" + name + ", address=" + address + ", port="
				+ port + ", socket=" + socket + "]";
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}
}

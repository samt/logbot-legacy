package com.logbot;
/**
 * Logbot
 *
 * Copyright (c) 2011 Sam Thompson <sam@websyntax.net>
 * http://www.opensource.org/licenses/mit-license.php The MIT License
 *
 */

import java.util.*;
import java.io.*;
import java.net.*;

public class IrcConnection {
	/*
	 * Connection info
	 */
	private String host = "";
	private int port = 6667;

	/*
	 * Socket connection
	 */
	private Socket sock = null;

	/*
	 * Buffered reader and writer
	 */
	private BufferedWriter writer;
	private BufferedReader reader;

	/*
	 * Default constructor
	 */
	public IrcConnection() {
	}

	/*
	 * Normal constructor 1
	 * @param String host
	 * @param String port
	 */
	public IrcConnection(String host, String port) {
		this(host, Integer.parseInt(port));
	}

	/*
	 * Normal constructor 2
	 * @param String host
	 * @param int port
	 */
	public IrcConnection(String host, int port) {
		this.host = host;
		this.port = port;
	}

	/*
	 * Connect
	 * @throws IrcConnectException
	 */
	public void connect() throws IrcConnectException {
		try {
			this.sock = new Socket(host, port);

			this.writer = new BufferedWriter(new OutputStreamWriter(this.sock.getOutputStream()));
			this.reader = new BufferedReader(new InputStreamReader(this.sock.getInputStream()));
		}
		catch (UnknownHostException e) {
			throw new IrcConnectException("Unknown Host \"" + host + "\"");
		}
		catch (IOException e) {
			throw new IrcConnectException("Could not create read or write buffer: " + e.getMessage());
		}
	}

	/*
	 * Check to see if we're connected or not
	 * @return boolean - Are we connected? 
	 */
	public boolean isConnected() {
		return (this.sock != null) ? true : false;
	}

	/*
	 * Send/Write to socket
	 * @param String msg - Message to send (without \r\n)
	 * @throws IOException
	 */
	public void send(String msg) throws IOException {
		this.writer.write(msg + "\r\n");
		this.writer.flush();
	}

	/*
	 * Read socket 
	 * @throws IrcConnectException
	 */
	public IrcMessage read() throws IOException {
		return new IrcMessage(this.reader.readLine());
	}

	/*
	 * Close
	 */
	public void close() {
		try {
			this.sock.close();
		}
		catch (Exception e) {
		}

		this.sock = null;
	}
}

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
import java.lang.*;

public class NetComWorker extends Thread {
	/*
	 * We're putting this conneciton object in here so we can do operations 
	 * asynchronously
	 */
	public IrcConnection ircServer = new IrcConnection();

	/*
	 * Constructor
	 * @param IrcConnection server - A connected 
	 *
	 * This was designed so you don't even need an object, just call 
	 * new NetworkCommander(server) in the main thread. 
	 */
	public NetComWorker(IrcConnection ircServer) {
		super("NetCom1");
		this.ircServer = ircServer;
		this.start();
	}

	/*
	 * Run the thread
	 *
	 * This worker thread does nothing but listen to connections and opens an
	 * instance of NetComConnection to do a console for the connecting party.
	 */
	public void run() {
		ServerSocket netComServer;
		NetComConnection cWorker;

		try {
			netComServer = new ServerSocket(Integer.parseInt(IrcConfig.netComPort));

			while (true) {
				cWorker = new NetComConnection(netComServer.accept(), this.ircServer);
				(new Thread(cWorker)).start();
			}
		}
		catch(Exception e) {
			Logbot.error("NetCom Listener Failed: " + e.getMessage());
		}
	}
}

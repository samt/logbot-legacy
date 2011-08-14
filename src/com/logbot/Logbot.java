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
import java.text.*;

public class Logbot {
	/*
	 * For our GC suggestor 
	 */
	public static int rounds = 0;

	/*
	 * Main
	 * @param String[] args - args[1] is the current config file to use (you 
	 *	can specify other files for different connections). 
	 */
	public static void main(String[] args) {
		String cfgfile = "logbot.cfg";
		int connectRetryCount = 0;

		// Load our configuration file
		try {
			cfgfile = (args.length > 0) ? args[0] : cfgfile;
			IrcConfig.load(cfgfile);
		}
		catch (IrcConfigException e) {
			error(e.getMessage());
		}

		// Start up our manager objects
		IrcConnection server = new IrcConnection(IrcConfig.host, IrcConfig.port);
		EventManager event = new EventManager();
		IrcChannelManager chanManager = new IrcChannelManager();
		IrcChannelFile chanFile = new IrcChannelFile(IrcConfig.channelFile);
		IrcMessage msg;
		String[] queue;

		// Register events 
		event.register(new ModulePing());
		event.register(new ModuleId());
		event.register(new ModuleChanManagerHelper(chanManager));
		event.register(new ModuleCommand(chanManager, chanFile));
		// End Register events

		// start our NetCom thread
		new NetComWorker(server);

		// Connection loop
		while (true) {
			try {
				server.connect();

				server.send("NICK " + IrcConfig.nick);
				server.send("USER " + IrcConfig.user + " 8 * :" + IrcConfig.name);

				// Message recv loop
				while (true) {
					msg = server.read();
					System.out.println(msg.raw);

					// Run through queue
					queue = event.getQueue(msg);
					for (int i = 0; i < queue.length; i++) {
						server.send(queue[i]);
						System.out.println(queue[i]);

						if (queue[i].startsWith("QUIT")) {
							System.out.println("*** Bot is shutting down ***");
							System.exit(0);
						}
					}
				}
			}
			catch (IrcConnectException e) {
				server.close();
				connectRetryCount++;
				System.out.println("Failed to connect to " + IrcConfig.host + ":" + IrcConfig.port);

				if (connectRetryCount > 10) {
					error("Retry attempt count exceeded");
				}
				else {
					System.out.println("Retrying in " + (connectRetryCount * 5) + " seconds...");
					try {
						Thread.sleep(connectRetryCount * 5000);
					} catch (InterruptedException e2) {
					}
				}
			}
			catch (IOException e) { // Something went wrong with our I/O (reading/writing to the socket)
				server.close(); // Just in case

				System.out.println("Connection to IRC server lost, reconnecting...");
				errorLog("Connection to IRC server lost");
			}
			catch (Exception e) { // "Catch"-all
				server.close();
				System.out.println("Unknown Exception was thrown:");
				e.printStackTrace();
				error("Terminated");
			}
		}
	}

	/*
	 * Error out and kill the program
	 * @param String msg
	 */
	public static void error(String msg) {
		System.out.println("[ERROR] " + msg);
		errorLog(msg);
		System.exit(-1);
	}

	/*
	 * Write to the error log (Does not exit the program)
	 * @param String msg
	 */
	public static void errorLog(String msg) {
		if (!IrcConfig.errorLog.equals("")) {
			try {
				Date date = new Date();
				Format ts = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");

				BufferedWriter out = new BufferedWriter(new FileWriter(IrcConfig.errorLog, true));
				out.write("[" + ts.format(date) + "] " + msg + "\n");
				out.close();
			}
			catch (Exception e) {
				System.out.println("[ERROR] Could not write to log file: " + e.getMessage());
			}
		}
		else {
			// Send a message out to warn the onlooker that there will not be 
			// any logging
			System.out.println("[ERROR] Error log not defined or config was not properly loaded");
		}
	}
}
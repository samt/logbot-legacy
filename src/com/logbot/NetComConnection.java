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

public class NetComConnection implements Runnable {
	/*
	 * Our properties... we do need these for communication purposes. 
	 *
	 */
	private Socket client = null;
	private IrcConnection ircServer = null;

	/*
	 * Flags
	 */
	private final static int CMD_OKAY = 20;
	private final static int CMD_SUCCESS = 21;
	private final static int CMD_NO_SUCCESS = 22;

	private final static int AUTH_REQ = 30;
	private final static int AUTH_INCORRECT = 31;
	private final static int CON_REFUSED = 32;

	/*
	 * Only way to Construct this object
	 */
	public NetComConnection(Socket s, IrcConnection i) {
		this.client = s;
		this.ircServer = i;
	}

	/*
	 * The method we must implement
	 * This code is fairly messy becaues I did not bother going too resuable 
	 * here.
	 *
	 * For the program opening a connect to this server, it runs on simple 
	 * status codes. 
	 *
	 * 20  - Okay, ready to send commands
	 * 21  - Command successful
	 * 22  - Command not successful
	 * 30  - Authentication required (send a line in this format: "username:password")
	 * 31  - Incorrect credentials 
	 * 32  - Connection refused
	 */
	public void run() {
		BufferedReader in = null;
		PrintWriter out = null;
		String line = "";
		int flag = CMD_OKAY;

		try {
			in = new BufferedReader(new InputStreamReader(this.client.getInputStream()));
			out = new PrintWriter(this.client.getOutputStream(), true);
		}
		catch (IOException e) {
			// If this is a one-time thing, I don't want the bot to shit itself
			// and die because a silly command dispatcher could not get in.
			Logbot.errorLog("Could not create I/O Streams for Net Commander Child");
			return;
		}

		// If we require auth, then we must set the flag appropriately
		if (IrcConfig.netComUser.length() > 0 && IrcConfig.netComPass.length() > 0) {
			flag = AUTH_REQ;
		}

		// If the connection was refused, why even go through the trouble of 
		// looping and serving any more than we have to.
		if (flag == CON_REFUSED) {
			out.println(flag);

			try {
				client.close();
			}
			catch (IOException e) {
			}
		}

		// Now it is time to go through a nice loop with try/catch outside as 
		// to terminate execution should we encounter an error
		try {
			while (true) {
				out.println(flag);

				// In case we want to refuse this connection now
				if (flag == CON_REFUSED) {
					out.println(flag);

					try {
						client.close();
					}
					catch (IOException e) {
					}

					break;
				}

				line = in.readLine();

				// Check login
				if (line == null) {
					throw new NullPointerException();
				}
				else if (flag == AUTH_REQ || flag == AUTH_INCORRECT) {
					if (line.indexOf(":") > 0 && 
						line.substring(0, line.indexOf(":")).equals(IrcConfig.netComUser) &&
						line.substring(line.indexOf(":") + 1).equals(IrcConfig.netComPass)) {

						flag = CMD_OKAY;
					}
					else
					{
						flag = AUTH_INCORRECT;
					}
				}
				else if (flag == CMD_OKAY || flag == CMD_SUCCESS || flag == CMD_NO_SUCCESS) {
					try {
						this.ircServer.send(line);
						System.out.println(line);
						flag = CMD_SUCCESS;
					}
					catch (IOException e) {
						flag = CMD_NO_SUCCESS;
					}
				}
			}
		}
		catch (IOException e) {
			Logbot.errorLog(e.getMessage());
		}
		catch (NullPointerException e) {
			// When they end their session, they go here (and the thread subsequently terminates)
		}
	}
}

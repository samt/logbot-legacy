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
import java.math.*;

public class ModuleChanLogger implements ModuleInterface {
	/*
	 * All our channels
	 */
	public Hashtable channels = new Hashtable();
 
	/*
	 */
	public ModuleChanLogger() {
	}

	/*
	 * This module requires us to do a few things:
	 * - Keep track of users per channel
	 * - Catch user channel actions 
	 * - Log messages and channel actions
	 *
	 */
	public String[] run(IrcMessage m) {
		try {

			IrcChannelFile chanFile = IrcChannelFile.getInstance();

			// Normal privmsg (first!)
			if (m.command.equals("PRIVMSG")) {
			
				// @todo CTCP
				this.addLog(m.target, "<" + m.nick + "> " + m.argument);

				// If they did the irclog command
				if (m.argument.startsWith("!irclog") && chanFile.get(m.target).isIrclogEnabled()) {
					IrcChannelConfig chanCfg = chanFile.get(m.target);
					String url = new String("http://logbot.com/" + IrcConfig.networkName + "/" + m.target + "/");

					if (!chanCfg.getKey().equals("")) {

						// replace key if nessiary
						if (chanCfg.newKeyRequired()) {
							chanCfg.setKey(new BigInteger(45, new Random()).toString(32));
						}

						url = url + "?k=" + chanCfg.getKey();
					}

					this.addLog(m.target, "<" + IrcConfig.nick + "> " + url);
					return new String[] {"PRIVMSG " + m.target + " :" + url};
				}
			}
			// Our bot joins a channel 
			else if (m.command.equals("JOIN") && m.nick.equals(IrcConfig.nick)) {
				// Add the channel to our Hashtable
				if (!this.channels.containsKey(m.argument)) {
					this.channels.put(m.argument, new IrcChannel());
				}

				// Teh log
				this.addLog(m.argument, "=-= " + m.nick + " has joined " + m.argument);
			}
			// Normal join
			else if (m.command.equals("JOIN")) {
				((IrcChannel) this.channels.get(m.argument)).userJoin(m.nick);
				this.addLog(m.argument, "=-= " + m.nick + " has joined " + m.argument);
			}
			else if (m.command.equals("PART")) {
				this.addLog(m.target, "=-= " + m.nick + " has left " + m.target);

				// Special case, we just want to get rid of this entry because the 
				// moment we leave it will no longer be maintainted 
				if (m.nick.equals(IrcConfig.nick)) {
					this.channels.remove(m.target);
				}
			}
			else if (m.command.equals("QUIT")) {
				// This is where it gets tricky, we have to find all the channels this user belonged to.
				Enumeration keys = this.channels.keys();
				String key;

				while (keys.hasMoreElements()) {
					key = (String) keys.nextElement();
					if (((IrcChannel) this.channels.get(key)).userInChannel(m.nick)) {
							this.addLog(key, "=-= " + m.nick + " has quit " + IrcConfig.networkName + " (" + m.argument + ")");
					}
				}
			}
			else if (m.command.equals("KICK")) {
				String chan = m.target.substring(0, m.target.indexOf(" "));
				this.addLog(chan, "=-= " + m.target.substring(m.target.indexOf(" ") + 1) + " has been booted by " + m.nick);			
			}
			// NAMES list from initial join
			else if (m.command.equals("353")) {
				String[] users = m.argument.split("\\s");

				String chan = m.target.substring(m.target.indexOf("#"));
				((IrcChannel) this.channels.get(chan)).userJoin(users);
			}
			// Join channels after id process
			else if (m.command.equals("NOTICE") && m.argument.startsWith("You are now identified")) {
				// Get our joins
				String joins = chanFile.getJoins();

				if (joins.length() > 0) {
					return new String[] {"JOIN " + joins};
				}
			}
		
		}
		catch (Exception e) {
			Logbot.errorLog("UNKNOWN ERROR: " + e.getMessage());
		}

		// This module will typically send nothing
		return null;
	}

	/*
	 * The channel logger 
	 * @param String channel - Channel 
	 * @param String msg - message to log
	 */
	public static void addLog(String channel, String msg) {
		try {
			Date date = new Date();
			Format day = new SimpleDateFormat("dd-MMM-yyyy");
			Format time = new SimpleDateFormat("HH:mm:ss");
			
			BufferedWriter out = new BufferedWriter(new FileWriter(IrcConfig.logPath + channel + "-" + day.format(date), true));
			out.write("[" + time.format(date) + "] " + msg + "\n");
			out.close();
		}
		catch (IOException e) {
			System.out.println("Something went wrong with writing to a file: " + e.getMessage());
		}
	}
}

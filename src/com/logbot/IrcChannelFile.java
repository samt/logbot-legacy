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

public class IrcChannelFile {
	/*
	 * Instances of IrcChannelConfig
	 */
	private Hashtable chans = new Hashtable();

	/*
	 * Filename of our JSON file
	 */
	private String filename;

	/*
	 * Load in the file (Construtor)
	 * @param String filename
	 */
	public IrcChannelFile(String filename) {
		this.filename = filename;

		try {
			BufferedReader reader = new BufferedReader(new FileReader(this.filename));
			String line;
			String[] parts;
			Boolean irclogEnabled;
			IrcChannelConfig ccfg;

			while ((line = reader.readLine()) != null) {
				parts = line.split(":");
				irclogEnabled = parts[2].trim().equals("1") ? true : false;

				ccfg = new IrcChannelConfig(parts[0], parts[1]).setIrclogEnabled( parts[2].equals("1") ? true : false );

				if (parts.length > 3) {
					ccfg.setKey(parts[3]);
				}

				this.add(ccfg);
			}

			reader.close();
		}
		catch (IOException e) {
			Logbot.error(e.getMessage());
		}
	}

	/*
	 * Add a new channel ( All adding should go through here )
	 * @param IrcChannelConfig c
	 */
	public void add(IrcChannelConfig c) {
		this.chans.put(c.toString(), c);
	}

	/*
	 * Delete a channel
	 * This is a cool trick... IrcChannelConfig has a toString() method to accomodate this
	 */
	public void delete(String name) {
		this.chans.remove(name);
	}

	/*
	 * Get the ChannelConfig object
	 * @param String chan - Channel name (With #)
	 * @return IrcChannelConfig
	 */
	public IrcChannelConfig get(String name) {
		return (IrcChannelConfig) this.chans.get(name);
	}

	/*
	 * Get join arguments 
	 */
	public String getJoins() {
		Enumeration e = this.chans.keys();
		IrcChannelConfig c;
		StringBuffer buffer = new StringBuffer(1024);
		
		while(e.hasMoreElements()) {
			c = (IrcChannelConfig) this.chans.get(e.nextElement());
			buffer.append(c.toString());

			if (!c.getJoinPass().equals("")) {
				buffer.append(" " + c.getJoinPass());
			}

			if (e.hasMoreElements()) {
				buffer.append(",");
			}
		}

		return buffer.toString();
	}

	/*
	 * Save the modified objects into the file
	 */
	public void save() {
		StringBuffer buffer = new StringBuffer(1024);

		Enumeration enu = this.chans.keys();
		IrcChannelConfig c;
		while(enu.hasMoreElements()) {
			c = (IrcChannelConfig) this.chans.get(enu.nextElement());

			buffer.append(c.toString() + ":");
			buffer.append(c.getJoinPass() + ":");
			buffer.append( (c.isIrclogEnabled() ? "1" : "0") + ":");
			buffer.append(c.getKey() + ":");
			buffer.append("\n");
		}

		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(this.filename));
			out.write(buffer.toString());
			out.close();
		}
		catch (IOException e) {
			Logbot.errorLog("Could not write to config file: " + e.getMessage());
		}
	}
}

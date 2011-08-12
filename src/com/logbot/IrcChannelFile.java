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
	private ArrayList<IrcChannelConfig> chans = new ArrayList();

	/*
	 * Filename of our JSON file
	 */
	private String filename;

	/*
	 * Static singleton instance
	 */
	private static IrcChannelFile cf = null;

	/*
	 * Instance new'er
	 */
	public static IrcChannelFile newInstance(String filename) throws IOException {
		IrcChannelFile.cf = new IrcChannelFile(filename);

		return IrcChannelFile.cf;
	}

	/*
	 * Instance getter
	 */
	public static IrcChannelFile getInstance() {
		return IrcChannelFile.cf;
	}

	/*
	 * Load in the file (Construtor)
	 * @param ???
	 */
	public IrcChannelFile(String filename) throws IOException {
		this.filename = filename;

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

	/*
	 * Add a new channel ( All adding should go through here )
	 * @param IrcChannelConfig c
	 */
	public void add(IrcChannelConfig c) {
		this.delete(c.toString());
		this.chans.add(c);
	}

	/*
	 * Delete a channel
	 * This is a cool trick... IrcChannelConfig has a toString() method to accomodate this
	 */
	public void delete(String name) {
		for (int i = 0; i < this.chans.size(); i++) {
			if ( ((IrcChannelConfig) this.chans.get(i)).toString().equals(name) ) {
				this.chans.remove(i);
				break;
			}
		}
	}

	/*
	 * Get the ChannelConfig object
	 * @param String chan - Channel name (With #)
	 * @return IrcChannelConfig
	 */
	public IrcChannelConfig get(String name) {
		IrcChannelConfig ccfg = null;

		for (int i = 0; i < this.chans.size(); i++) {
			ccfg = (IrcChannelConfig) this.chans.get(i);

			if ( !ccfg.toString().equals(name) ) {
				break;
			}

			ccfg = null;
		}

		return ccfg;
	}

	/*
	 * Get join arguments 
	 */
	public String getJoins() {
		Iterator chanIterator = this.chans.iterator();
		IrcChannelConfig c;
		StringBuffer buffer = new StringBuffer(1024);

		while(chanIterator.hasNext()) {
			c = (IrcChannelConfig) chanIterator.next();
			buffer.append(c.toString());

			if (!c.getJoinPass().equals("")) {
				buffer.append(" " + c.getJoinPass());
			}

			if (chanIterator.hasNext()) {
				buffer.append(",");
			}
		}

		return buffer.toString();
	}

	/*
	 * Save the modified objects into the file
	 */
	public void save() throws IOException {
		StringBuffer buffer = new StringBuffer(1024);

		Iterator chanIterator = this.chans.iterator();
		IrcChannelConfig c;
		while(chanIterator.hasNext()) {
			c = (IrcChannelConfig) chanIterator.next();

			buffer.append(c.toString() + ":");
			buffer.append(c.getJoinPass() + ":");
			buffer.append( (c.isIrclogEnabled() ? "1" : "0") + ":");
			buffer.append(c.getKey() + ":");
			buffer.append("\n");
		}

		BufferedWriter out = new BufferedWriter(new FileWriter(this.filename));
		out.write(buffer.toString());
		out.close();
	}
}

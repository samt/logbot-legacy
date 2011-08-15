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

public class IrcChannelConfig {
	/*
	 * Name of the channel
	 */
	private String name = "";

	/*
	 * Join Pass
	 */
	private String joinPass = "";

	/*
	 * Is !irclog enabled?
	 */
	private boolean irclogEnabled = true;

	/*
	 * Key (Empty if chan is public)
	 */
	private String key = "";

	/*
	 * Last time the key was reset 
	 */
	private Date lastRst = new Date();

	/*
	 * Sorta-const 
	 * 12 hours
	 */
	public static final int KEY_EXP = 43200000;

	/*
	 * Only need the name
	 * @param String name - Name of the channel
	 */
	public IrcChannelConfig(String name) {
		this(name, "");
	}

	/*
	 * Normal constructor
	 * @param String name - Name of the channel
	 * @param String key - Private key (leave empty if no key) for web access
	 * @param boolean irclogEnabled - Private key (leave empty if no key) for web access
	 */
	public IrcChannelConfig(String name, String joinPass) {
		this.name = name.trim().toLowerCase();
		this.joinPass = joinPass.trim();
	}

	/*
	 * Set a channel join pass
	 * @param String joinPass
	 */
	public IrcChannelConfig setJoinPass(String joinPass) {
		this.joinPass = joinPass.trim();
		
		return this;
	}

	/*
	 * Set a (new) key
	 * @param String key
	 */
	public IrcChannelConfig setKey(String key) {
		this.key = key.trim();
		lastRst = new Date();

		return this;
	}

	/*
	 * Set the !irclog command to be enabled or not
	 * @param boolean irclogEnabled
	 */
	public IrcChannelConfig setIrclogEnabled(boolean irclogEnabled) {
		this.irclogEnabled = irclogEnabled;

		return this;
	}

	/*
	 * Get the name
	 * @return String - name
	 */
	public String getName() {
		return this.name;
	}

	/*
	 * Get the join pass
	 * @return String - Join pass
	 */
	public String getJoinPass() {
		return this.joinPass;
	}

	/*
	 * Get the key
	 * @return String - Key
	 */
	public String getKey() {
		return this.key;
	}

	/*
	 * Do we need a new key?
	 */
	public boolean newKeyRequired() {
		return ( ((new Date()).getTime() - lastRst.getTime()) > KEY_EXP ) ? true : false;
	}

	/*
	 * Is !irclog enabled?
	 * @return boolean 
	 */
	public boolean isIrclogEnabled() {
		return this.irclogEnabled;
	}

	/*
	 * This is a cool trick, when ArrayList locates this object as it compares 
	 * to a string, it will invoke this method and compare string with string.
	 *
	 * @return String
	 */
	public String toString() {
		return this.name;
	}

	public final boolean equals(IrcChannelConfig c) {
		return this.name.equals(c.name) ? true : false;
	}
}

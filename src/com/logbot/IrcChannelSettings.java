package com.logbot;
/**
 * Logbot
 *
 * Copyright (c) 2011 Sam Thompson <sam@websyntax.net>
 * http://www.opensource.org/licenses/mit-license.php The MIT License
 *
 */

public class IrcChannelSettings {
	/*
	 * Location of the file
	 */
	public static String file;

	/*
	 */
	public String joinPass = "";
	public boolean enableLog = true;
	public boolean privateChan = false;
	public String key = "";

	/*
	 * Select a channel
	 */
	public IrcChannelSettings(String channel) {
	}

	/*
	 * Update the join password
	 * @param String pass
	 */
	public void updatePassword(String pass) {
	}

	/*
	 * 
	 */
	public void updateLoggingEnabled(boolean enable) {
	}

	/*
	 * 
	 */
	public void updatePrivateChan(boolean privateChan) {
	}

	/*
	 *
	 */
	public void updateKey(String key) {
	}

	/*
	 * Write changes to file
	 */
	private void writeChanges() {
	}
}
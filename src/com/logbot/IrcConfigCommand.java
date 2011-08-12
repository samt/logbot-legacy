package com.logbot;
/**
 * Logbot
 *
 * Copyright (c) 2011 Sam Thompson <sam@websyntax.net>
 * http://www.opensource.org/licenses/mit-license.php The MIT License
 *
 */

public class IrcConfigCommand {
	/*
	 * User
	 */
	private String user = "";

	/*
	 * Channel
	 */
	private String channel = "";

	/*
	 * Config name
	 */
	private String cfgName = "";

	/*
	 * Config Value
	 */
	private String cfgValue = "";

	public IrcConfigCommand(String user, String arg) throws Exception {
		this.user = user;
		String[] parts = arg.split("\\s");

		if (parts.length < 4 ||
			!parts[1].startsWith("#")) {
			throw new Exception("Bad command");
		}

		this.channel = parts[1].toLowerCase();
		this.cfgName = parts[2].toUpperCase();
		this.cfgValue = parts[3];
	}

	public boolean isSetterAndOp(String user) {
		boolean op = false;

		if (user.charAt(0) == '@' ||
			user.charAt(0) == '%' ||
			user.charAt(0) == '&' ||
			user.charAt(0) == '~') {

			op = true;
			user = user.substring(1);
		}

		return user.equals(this.user) && op ? true : false;
	}

	public boolean getBoolVal() {
		String raw = this.cfgValue.toLowerCase();
		return (raw.equals("1") || raw.equals("true") || raw.equals("yes")) ? true : false;
	}

	public String getVal() {
		return this.cfgValue;
	}

	public String getCfg() {
		return this.cfgName;
	}

	public String getChan() {
		return this.channel;
	}
}

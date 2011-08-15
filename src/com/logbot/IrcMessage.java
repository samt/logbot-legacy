package com.logbot;
/**
 * Logbot
 *
 * Copyright (c) 2011 Sam Thompson <sam@websyntax.net>
 * http://www.opensource.org/licenses/mit-license.php The MIT License
 *
 */

import java.io.*;

public class IrcMessage {
	public String raw = "";
	public String prefix = "";
	public String nick = "";
	public String host = "";
	public String command = "";
	public String target = "";
	public String argument = "";

	public IrcMessage(String msg) throws IOException {
		if (msg == null) {
			throw new IOException();
		}

		this.raw = msg;

		// Hardcore parsing action
		int nextspace = msg.indexOf(' ');

		// Check for our prefix
		if (msg.charAt(0) == ':') {
			this.prefix = new String(msg.substring(1, nextspace)).trim();
			msg = msg.substring(nextspace + 1).trim();
			nextspace = msg.indexOf(" ");
		}

		// The command will exist.
		this.command = new String(msg.substring(0, nextspace)).trim();
		msg = msg.substring(nextspace + 1);

		// Extract the target and the message
		if (msg.indexOf(':') == -1) {
			// Command does not exist.
			this.target = msg.trim();
		}
		else if (msg.indexOf(':') > 0) {
			this.target = msg.substring(0, msg.indexOf(':')).trim();
			this.argument = msg.substring(msg.indexOf(':') + 1).trim();
		}
		else {
			this.argument = msg.substring(1).trim();
		}

		// Now we need to extract items such as the nickname and host
		if (this.prefix.length() > 0 && this.prefix.indexOf('!') > 0) {
			this.nick = this.prefix.substring(0, this.prefix.indexOf('!')).trim();
			this.host = this.prefix.substring(this.prefix.indexOf('@')).trim();
		}
	}
}

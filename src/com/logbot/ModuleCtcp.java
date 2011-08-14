package com.logbot;
/**
 * Logbot
 *
 * Copyright (c) 2011 Sam Thompson <sam@websyntax.net>
 * http://www.opensource.org/licenses/mit-license.php The MIT License
 *
 */

public class ModuleCtcp implements ModuleInterface {
	/*
	 */
	public ModuleCtcp() {
	}

	/*
	 * CTCP stuffs
	 */
	public String[] run(IrcMessage m) {
		if (m.command.equals("PRIVMSG") && m.argument.startsWith("\001")) {
			String ctcpCmd = m.argument.substring(1);

			if (ctcpCmd.equals("VERSION")) {
				return new String[] {"NOTICE " + m.nick + " :\001VERSION logbot 1.1.0 [GNU/Linux Java2 SE]\001"};
			}
		}

		return null;
	}
}

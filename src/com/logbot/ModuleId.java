package com.logbot;
/**
 * Logbot
 *
 * Copyright (c) 2011 Sam Thompson <sam@websyntax.net>
 * http://www.opensource.org/licenses/mit-license.php The MIT License
 *
 */

public class ModuleId implements ModuleInterface {
	/*
	 * Current nickname in use 
	 */
	private String nickname = "";

	/*
	 * Need to ghost flag
	 */
	private boolean needToGhost = false;

	/*
	 */
	public ModuleId() {
		this.nickname = IrcConfig.nick;
	}

	/*
	 * Identify to services or ghost
	 */
	public String[] run(IrcMessage m) {
		if (m.command.equals("NOTICE") && m.argument.startsWith("This nickname is registered")) {
			return new String[] {"PRIVMSG NickServ :IDENTIFY " + IrcConfig.pass};
		}
		else if (m.command.equals("433")) {
			this.nickname = this.nickname.concat("_");
			this.needToGhost = true;
			return new String[] {"NICK " + this.nickname};
		}
		else if (needToGhost) {
			this.needToGhost = false;
			return new String[] {"PRIVMSG NickServ :GHOST " + IrcConfig.nick + " " + IrcConfig.pass};
		}
		else if (m.command.equals("NOTICE") && m.argument.toUpperCase().indexOf("GHOST") != -1) {
			this.nickname = IrcConfig.nick;
			return new String[] {"NICK " + this.nickname};
		}
		else {
			return null;
		}
	}
}

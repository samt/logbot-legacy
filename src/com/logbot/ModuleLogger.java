package com.logbot;
/**
 * Logbot
 *
 * Copyright (c) 2011 Sam Thompson <sam@websyntax.net>
 * http://www.opensource.org/licenses/mit-license.php The MIT License
 *
 */

import java.util.*;

public class ModuleLogger implements ModuleInterface {
	/*
	 * We need two managers
	 */
	private IrcChannelManager chanManager;

	/*
	 */
	public ModuleLogger(IrcChannelManager m) {
		this.chanManager = m;
	}

	/*
	 * All this does is log the channel
	 * Nothing more, nothing less.
	 */
	public String[] run(IrcMessage m) {
		if (m.command.equals("PRIVMSG")) {
		}
		else if (m.command.equals("JOIN")) {
			this.chanManager.join(m.argument);
		}
		else if (m.command.equals("JOIN")) {
			this.chanManager.userJoin(m.argument, m.nick);
		}
		else if (m.command.equals("PART")) {
			this.chanManager.userPart(m.target, m.nick);
		}
		else if (m.command.equals("QUIT")) {
			this.chanManager.userQuit(m.nick);
		}
		else if (m.command.equals("KICK")) {
			this.chanManager.userPart(m.target.substring(0, m.target.indexOf(" ")), m.target.substring(m.target.indexOf(" ") + 1));
		}

		return null;
	}
}

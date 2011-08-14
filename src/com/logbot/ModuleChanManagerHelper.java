package com.logbot;
/**
 * Logbot
 *
 * Copyright (c) 2011 Sam Thompson <sam@websyntax.net>
 * http://www.opensource.org/licenses/mit-license.php The MIT License
 *
 */

import java.math.*;
import java.util.*;
import java.text.*;

public class ModuleChanManagerHelper implements ModuleInterface {
	/*
	 * We need one manager
	 */
	private IrcChannelManager chanManager;

	/*
	 */
	public ModuleChanManagerHelper(IrcChannelManager m) {
		this.chanManager = m;
	}

	/*
	 * This module's responsibility is simply to make sure the manager is up 
	 * to date.
	 */
	public String[] run(IrcMessage m) {
		if (m.command.equals("JOIN") && m.nick.equals(IrcConfig.nick)) {
			this.chanManager.join(m.argument);
		}
		else if (m.command.equals("JOIN")) {
			this.chanManager.userJoin(m.argument, m.nick);
		}
		else if (m.command.equals("PART") && m.nick.equals(IrcConfig.nick)) {
			this.chanManager.part(m.target);
		}
		else if (m.command.equals("PART")) {
			this.chanManager.userPart(m.target, m.nick);
		}
		else if (m.command.equals("QUIT")) {
			this.chanManager.userQuit(m.nick);
		}
		else if (m.command.equals("KICK") && m.target.substring(m.target.indexOf(" ") + 1).equals(IrcConfig.nick)) {
			this.chanManager.part(m.target.substring(0, m.target.indexOf(" ")));
		}
		else if (m.command.equals("KICK")) {
			this.chanManager.userPart(m.target.substring(0, m.target.indexOf(" ")), m.target.substring(m.target.indexOf(" ") + 1));
		}
		else if (m.command.equals("NICK")) {
			this.chanManager.changeNick(m.nick, m.argument);
		}
		else if (m.command.equals("MODE")) {
			String[] parts = m.target.split("\\s");
			
			if (parts.length > 2) {
				return new String[] {"WHOIS " + parts[2]};
			}
		}
		else if (m.command.equals("319")) {
			String nick = m.target.substring(m.target.indexOf(" ") + 1);
			String[] parts = m.argument.split("\\s");
			char prefix;

			for (int i = 0; i < parts.length; i++) {
				prefix = IrcUser.getPrefix(parts[i]);
				this.chanManager.updatePrefix(IrcUser.stripPrefix(parts[i]), nick, prefix);
			}
		}
		else if (m.command.equals("353")) {
			String[] users = m.argument.split("\\s");
			String chan = m.target.substring(m.target.indexOf("#"));

			for (int i = 0; i < users.length; i++) {
				this.chanManager.userJoin(chan, users[i]);
			}
		}

		return null;
	}
}

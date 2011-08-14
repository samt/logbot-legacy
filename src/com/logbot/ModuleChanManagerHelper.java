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
		else if (m.command.equals("KICK")) {
			this.chanManager.userPart(m.target.substring(0, m.target.indexOf(" ")), m.target.substring(m.target.indexOf(" ") + 1));
		}
		else if (m.command.equals("NICK")) {
			this.chanManager.changeNick(m.nick, m.argument);
		}
//		else if (m.command.equals("MODE")) {
//			// This really does not belong here, I am creating a new module 
//			// just for keeping the MODE updated.
//		}
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

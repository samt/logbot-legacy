package com.logbot;
/**
 * Logbot
 *
 * Copyright (c) 2011 Sam Thompson <sam@websyntax.net>
 * http://www.opensource.org/licenses/mit-license.php The MIT License
 *
 */

import java.util.*;

public class ModuleInvite implements ModuleInterface {
	/*
	 * We need two managers
	 */
	private IrcChannelManager chanManager;
	private IrcChannelFile chanFile;

	/*
	 * Invite hash table
	 */
	private Hashtable invites = new Hashtable();

	/*
	 */
	public ModuleInvite(IrcChannelManager m, IrcChannelFile f) {
		this.chanManager = m;
		this.chanFile = f;
	}

	/*
	 * respond to invites
	 * keep chan file up to date
	 */
	public String[] run(IrcMessage m) {
		if (m.nick.equals(IrcConfig.nick) && m.command.equals("JOIN") && this.chanFile.get(m.target) == null) {
			this.chanFile.add(new IrcChannelConfig(m.argument));
			this.chanFile.save();
		}
		else if (m.command.equals("KICK") && m.target.substring(m.target.indexOf(" ") + 1).equals(IrcConfig.nick)) {
			this.chanFile.delete(m.target.substring(0, m.target.indexOf(" ")));
			this.chanFile.save();
		}
		else if(m.command.equals("PART") && m.nick.equals(IrcConfig.nick)) {
			this.chanFile.delete(m.target);
			this.chanFile.save();
		}

		// Join the channel, but we have to track who invited us and to which channel
		if (m.command.equals("INVITE")) {
			this.invites.put(m.argument, m.nick);

			return new String[] { "JOIN " + m.argument };
		}
		else if (m.command.equals("366")) { // End of /NAMES list
			String chan = m.target.substring(m.target.indexOf(" ") + 1);

			if (this.invites.containsKey(chan)) {
				boolean part = true;

				if (this.chanManager.isOperator(chan, (String) this.invites.get(chan))) {
					part = false;
				}

				this.invites.remove(chan);

				if (part) {
					return new String[] {"PART " + chan};
				}
			}
		}

		return null;
	}
}
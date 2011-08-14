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
	 * Channel Manager
	 */
	private IrcChannelManager chanManager;

	/*
	 */
	public ModuleLogger(IrcChannelManager chanManager) {
		this.chanManager = chanManager;
	}

	/*
	 * All this does is log the channel
	 * Nothing more, nothing less.
	 */
	public String[] run(IrcMessage m) {
		if (m.command.equals("PRIVMSG")) {
			IrcLogger.write(m.target, "<" + this.chanManager.getFullNick(m.target, m.nick) + "> " + m.argument);
		}
		else if (m.command.equals("JOIN")) {
			IrcLogger.write(m.argument, "=-= " + m.nick + " has joined " + m.argument);
		}
		else if (m.command.equals("PART")) {
			IrcLogger.write(m.target, "=-= " + m.nick + " has left " + m.target);
		}
		else if (m.command.equals("QUIT")) {
			String[] chans = this.chanManager.getChannelsUserMemeber(m.nick);

			for (int i = 0; i < chans.length; i++) {
				IrcLogger.write(chans[i], "=-= " + m.nick + " has left " + IrcConfig.networkName + " (" + m.argument + ")");
			}
		}
		else if (m.command.equals("KICK")) {
			IrcLogger.write(m.target.substring(0, m.target.indexOf(" ")), 
				"=-= " + m.target.substring(m.target.indexOf(" ") + 1) + " has been booted by " + m.nick);	
		}
		else if (m.command.equals("NICK")) {
			String[] chans = this.chanManager.getChannelsUserMemeber(m.nick);

			for (int i = 0; i < chans.length; i++) {
				IrcLogger.write(chans[i], "=-= " + m.nick + " is now know as " + m.argument);
			}
		}

		return null;
	}
}

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

public class ModuleCommand implements ModuleInterface {
	/*
	 * We need two managers
	 */
	private IrcChannelManager chanManager;
	private IrcChannelFile chanFile;

	/*
	 */
	public ModuleCommand(IrcChannelManager m, IrcChannelFile f) {
		this.chanManager = m;
		this.chanFile = f;
	}

	/*
	 * This module handles 2 commands:
	 * !irclog
	 * !set
	 *
	 * We need both the channel manager (which holds user levels) and the 
	 * channel file, which holds settings about the channel itself.
	 */
	public String[] run(IrcMessage m) {
	
		// This is done via PRIVMSG
		if (m.command.equals("PRIVMSG")) {

			// First check for !irclog (more common)
			if (m.argument.toUpperCase().startsWith("!IRCLOG") && this.chanFile.get(m.target).isIrclogEnabled()) {
				IrcChannelConfig chanCfg = chanFile.get(m.target);
				String url = new String("http://logbot.com/" + IrcConfig.networkName + "/" + m.target + "/");

				if (!chanCfg.getKey().equals("")) {
					// Replace key if needed
					if (chanCfg.newKeyRequired()) {
						chanCfg.setKey(new BigInteger(45, new Random()).toString(32));

						chanFile.save(); // Make sure our new key gets in there
					}

					url = url + "?k=" + chanCfg.getKey();
				}

				IrcLogger.write(m.target, "<" + IrcConfig.nick + "> " + url);
				return new String[] {"PRIVMSG " + m.target + " :" + url};
			}

			// Time for the !set command
			else if (m.argument.toUpperCase().startsWith("!SET") && this.chanManager.isOperator(m.target, m.nick)) {
				return new String[] {"PRIVMSG " + m.target + " :You are trying to set something."};
			}
		}

		return null;
	}
}

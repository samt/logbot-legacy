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
				String url = new String("http://logbot.com/" + IrcConfig.networkName + "/" + m.target.replaceAll("\\#", "%23") + "/");

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

			boolean isOp = false;
			if(m.argument.toUpperCase().startsWith("!")) {
				isOp = this.chanManager.isOperator(m.target, m.nick) ? true : false;
			}

			int index = m.argument.indexOf(" ");
			String value = (index > -1) ? m.argument.substring(index) : "";

			if (isOp && (m.argument.toUpperCase().startsWith("!SETPRIVATE") || m.argument.toUpperCase().startsWith("!ENABLEIRCLOG"))) {
				String[] result;
				value = value.toUpperCase().trim();

				if (value.equals("TRUE") || value.equals("1") || value.equals("YES")) {
					IrcChannelConfig cCfg = chanFile.get(m.target);
					
					if (m.argument.toUpperCase().startsWith("!SETPRIVATE")) {
						cCfg.setKey(new BigInteger(45, new Random()).toString(32));
						result = new String[] {"PRIVMSG " + m.target + " :" + m.nick + ": Channel is now being privately logged."};
					}
					else {
						cCfg.setIrclogEnabled(true);
						result = new String[] {"PRIVMSG " + m.target + " :" + m.nick + ": !irclog command has been enabled."};
					}
	
					chanFile.save();
				}
				else if (value.equals("FALSE") || value.equals("0") || value.equals("NO")) {
					IrcChannelConfig cCfg = chanFile.get(m.target);

					if (m.argument.toUpperCase().startsWith("!SETPRIVATE")) {
						cCfg.setKey("");
						result = new String[] {"PRIVMSG " + m.target + " :" + m.nick + ": Channel is now being publicly logged."};
					}
					else {
						cCfg.setIrclogEnabled(false);
						result = new String[] {"PRIVMSG " + m.target + " :" + m.nick + ": !irclog command has been disabled."};
					}
	
					chanFile.save();
				}
				else {
					result = new String[] {"PRIVMSG " + m.target + " :" + m.nick + ": Invalid value, please use 'true' or 'false'"};
				}

				return result;
			}
			else if (isOp && m.argument.toUpperCase().startsWith("!SETJOINPASS")) {
				IrcChannelConfig cCfg = chanFile.get(m.target);
				cCfg.setJoinPass(value);
				chanFile.save();

				return new String[] {"PRIVMSG " + m.target + " :" + m.nick + ": Join password set."};
			}
		}

		return null;
	}
}

package com.logbot;
/**
 * Logbot
 *
 * Copyright (c) 2011 Sam Thompson <sam@websyntax.net>
 * http://www.opensource.org/licenses/mit-license.php The MIT License
 *
 */
import java.util.*;
import java.text.*;
import java.math.*;

public class ModuleManage implements ModuleInterface {
	/*
	 */
	public ModuleManage() {
	}

	/*
	 * Irc Invites
	 */
	private ArrayList<IrcInvite> invites = new ArrayList();
	
	private ArrayList<IrcConfigCommand> configCmds = new ArrayList();

	/*
	 * Identify to services or ghost
	 */
	public String[] run(IrcMessage m) {
		IrcChannelFile chanFile = IrcChannelFile.getInstance();
		boolean changedCfg = false;

		if (m.target.equals(IrcConfig.nick))
		{
			// We're doing everything in here
			if (m.command.equals("PRIVMSG")) {
				// @todo help and config stuff
				IrcConfigCommand ccmd;

				if (m.argument.toUpperCase().startsWith("SET")) {
					try {
						ccmd = new IrcConfigCommand(m.nick, m.argument);
						configCmds.add(ccmd);
						return new String[] {"NAMES " + ccmd.getChan()};
					} catch (Exception e) {
						return new String[] {"PRIVMSG " + m.nick + " :Invalid command, syntax: /msg " + IrcConfig.nick + " SET #channel config_name config_value"};
					}
				}
			}
			else if (m.command.equals("INVITE")) {
				// @todo add to config file
				// We have to set flags though... we are checking for 2 things:
				// 1) we have succesfully entered the channel
				// 2) we the invitor has something higher than voice
				this.invites.add(new IrcInvite(m.nick, m.argument));

				return new String[] { "JOIN " + m.argument };
			}
		}

		if (m.command.equals("KICK")) {
			String chan = m.target.substring(0, m.target.indexOf(" "));

			if ( m.target.substring(m.target.indexOf(" ") + 1).equals(IrcConfig.nick) ) {
				chanFile.delete(chan);
				changedCfg = true;
			}
		}
		// Here we are checking to ensure the invitor is actually an OP in the channel they say they are in
		else if (m.command.equals("353") && (m.target.indexOf("=") != -1 || m.target.indexOf("@") != -1)) { // results of /NAMES command

			String[] users = m.argument.split("\\s");
			String separator = m.target.indexOf("@") != -1 ? "@" : "=";
			String[] parts = m.target.split(separator);
			String chan = parts[1].trim();
			IrcInvite inv = null;
			IrcConfigCommand ccfg = null;

			// Handle invites
			for (Iterator iterator = this.invites.iterator(); iterator.hasNext(); ) {
				inv = (IrcInvite) iterator.next();

				if (!inv.toString().equals(chan)) {
					inv = null;
				}
			}

			if (inv != null) {
				for (int i = 0; i < users.length; i++) {
					if (inv.isInvitorAndOp(users[i])) {
						// Confirmed.
						this.invites.remove(this.invites.indexOf(inv));
						chanFile.add(new IrcChannelConfig(chan));
						changedCfg = true;
					}
				}
			}

			// Handle SETs 
			for (Iterator iterator = this.configCmds.iterator(); iterator.hasNext(); ) {
				ccfg = (IrcConfigCommand) iterator.next();

				if (!ccfg.getChan().equals(chan)) {
					ccfg = null;
				}
			}

			if (ccfg != null) {
				for (int i = 0; i < users.length; i++) {
					if (ccfg.isSetterAndOp(users[i])) {
						
						if ( ccfg.getCfg().toUpperCase().equals("ENABLEIRCLOG") )
						{
							chanFile.get(ccfg.getChan()).setIrclogEnabled(ccfg.getBoolVal());
							changedCfg = true;
						}
						else if ( ccfg.getCfg().toUpperCase().equals("PRIVATE") )
						{
							if (ccfg.getBoolVal()) {
								chanFile.get(ccfg.getChan()).setKey(new BigInteger(45, new Random()).toString(32));
							}
							else {
								chanFile.get(ccfg.getChan()).setKey("");
							}
						
							changedCfg = true;
						}
						else if (ccfg.getCfg().toUpperCase().equals("JOINPASS") ) {
							chanFile.get(ccfg.getChan()).setJoinPass(ccfg.getVal());
							changedCfg = true;
						}
						else {
							return new String[] {"PRIVMSG " + m.nick + " :Unknown command, the ones available are 'enableirclog', 'private', and 'joinpass'"};
						}
  
						if (changedCfg) {
							this.configCmds.remove(this.configCmds.indexOf(ccfg));
						}
					}
				}
			}
			
			// ...that was ugly 
		}
		// If we get to the end of this point and there is still an IrcInvite 
		// matching this channel, we need to leave
		else if (m.command.equals("366")) { // end of /NAMES command
			String[] parts = m.target.split("\\s");
			String chan = parts[1].trim();
			IrcInvite inv = null;

			for (Iterator iterator = this.invites.iterator(); iterator.hasNext(); ) {
				inv = (IrcInvite) iterator.next();
				if (inv.toString().equals(chan)) {

					this.invites.remove(this.invites.indexOf(inv));
					return new String[] { "PART " + chan };
				}
			}
		}

		if (changedCfg) {
			try {
				chanFile.save();
			}
			catch (Exception e) {
				// Not the end of the world... we can have a way to manually resave it later as it is still stored in memory 
				Logbot.errorLog("Could not save modified chan config file: " + e.getMessage());
			}
		}

		return null;
	}
}

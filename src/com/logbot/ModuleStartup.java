package com.logbot;
/**
 * Logbot
 *
 * Copyright (c) 2011 Sam Thompson <sam@websyntax.net>
 * http://www.opensource.org/licenses/mit-license.php The MIT License
 *
 */

import java.util.*;

public class ModuleStartup implements ModuleInterface {
	/*
	 * We need one manager
	 */
	private IrcChannelFile chanFile;

	/*
	 */
	public ModuleStartup(IrcChannelFile f) {
		this.chanFile = f;
	}

	/*
	 * Make sure the file stays up to date
	 */
	public String[] run(IrcMessage m) {
		if (m.command.equals("NOTICE") && ( // I know this is ugly, but I may want to add some more later.
				m.argument.startsWith("You are now identified") || 
				m.argument.startsWith("You are successfully")
		)) {
			String joins = chanFile.getJoins();

			if (joins.length() > 0) {
				return new String[] {"JOIN " + joins};
			}
		}

		return null;
	}
}

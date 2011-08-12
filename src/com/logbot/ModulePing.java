package com.logbot;
/**
 * Logbot
 *
 * Copyright (c) 2011 Sam Thompson <sam@websyntax.net>
 * http://www.opensource.org/licenses/mit-license.php The MIT License
 *
 */

public class ModulePing implements ModuleInterface {
	/*
	 */
	public ModulePing() {
	}

	/*
	 * Let's run it.
	 */
	public String[] run(IrcMessage m) {
		return m.command.equals("PING") ? new String[] {"PONG :" + m.argument} : null;
	}
}

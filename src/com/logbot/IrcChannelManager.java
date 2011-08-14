package com.logbot;
/**
 * Logbot
 *
 * Copyright (c) 2011 Sam Thompson <sam@websyntax.net>
 * http://www.opensource.org/licenses/mit-license.php The MIT License
 *
 */

import java.util.*;

public class IrcChannelManager {
	/*
	 * Channels 
	 */
	private Hashtable chans = new Hashtable();

	/*
	 * IRC Channel Manager
	 */
	public IrcChannelManager() {
	}

	/*
	 * Join a channel
	 * @param String chan - Channel name
	 */
	public void join(String chan) {
		chans.put(chan, new IrcChannel(chan));
	}

	/*
	 * Part a channel
	 * @param String chan - Channel name
	 */
	public void part(String chan) {
		chans.remove(chan);
	}

	/*
	 * User join
	 * @param String chan - Channel name
	 * @param String nick - Nickname
	 */
	public void userJoin(String chan, String nick) {
		((IrcChannel) chans.get(chan)).userJoin(nick);
	}

	/*
	 * User part 
	 * @param String chan - Channel name
	 * @param String nick - Nickname
	 */
	public void userPart(String chan, String nick) {
		((IrcChannel) chans.get(chan)).userPart(nick);
	}

	/*
	 * Change nickname
	 * @param String nick - Old nick
	 * @param String nick - New nick
	 */
	public void changeNick(String oldNick, String newNick) {
		Enumeration e = this.chans.keys();
		IrcChannel c;

		while (e.hasMoreElements()) {
			c = (IrcChannel) this.chans.get(e.nextElement());

			if (c.userInChannel(oldNick)) {
				c.changeNick(oldNick, newNick);
			}
		}
	}

	/*
	 * User Quit
	 * @param String nick - Nickname
	 */
	public void userQuit(String nick) {
		Enumeration e = this.chans.keys();
		IrcChannel c;

		while (e.hasMoreElements()) {
			c = (IrcChannel) this.chans.get(e.nextElement());

			if (c.userInChannel(nick)) {
				c.userPart(nick);
			}
		}
	}

	/*
	 * Update prefix
	 * @param String chan
	 * @param String nick
	 * @param char prefix
	 */
	public void updatePrefix(String chan, String nick, char prefix) {
		((IrcChannel) chans.get(chan)).updateUserPrefix(nick, prefix);
	}

	/*
	 * Is the user an operator for this channel?
	 *
	 * @param String nick
	 * @param String chan
	 * @return boolean 
	 */
	public boolean isOperator(String chan, String nick) {
		char prefix = ((IrcUser) ((IrcChannel) chans.get(chan)).getUser(nick)).getUserPrefix();
		return (prefix == '~' || prefix == '&' || prefix == '@' || prefix == '%') ? true : false;
	}
}

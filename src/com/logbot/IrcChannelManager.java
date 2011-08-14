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
		IrcUser u = (IrcUser) ((IrcChannel) chans.get(chan)).getUser(nick);
		
		if (u == null) {
			return false;
		}

		char prefix = u.getUserPrefix();
		return (prefix == '~' || prefix == '&' || prefix == '@' || prefix == '%') ? true : false;
	}

	/*
	 * Get Channels user is a memeber of
	 * @String user
	 * @return String[] - Channels
	 */
	public String[] getChannelsUserMemeber(String nick) {
		Enumeration e = this.chans.keys();
		IrcChannel c;
		ArrayList<String> userChans = new ArrayList();

		while (e.hasMoreElements()) {
			c = (IrcChannel) this.chans.get(e.nextElement());

			if (c.userInChannel(nick)) {
				userChans.add(c.toString());
			}
		}

		return (String[]) userChans.toArray(new String[0]);
	}

	/*
	 * Get full nick
	 * @param String nick
	 * @param String chan
	 * @return String - Nickname with a prefix
	 */
	public String getFullNick(String chan, String nick) {
		try {
			IrcChannel c = (IrcChannel) this.chans.get(chan);
			return c.getUser(nick).getFullNick();
		}
		catch (NullPointerException e) {
			return nick;
		}
	}

/*	public void debug() {
		Enumeration e = this.chans.keys();
		IrcChannel c;
		IrcUser[] u;

		while (e.hasMoreElements()) {
			c = (IrcChannel) this.chans.get(e.nextElement());
	
			System.out.println("  " + c.toString());
			u = c.getUsers();

			for (int i = 0; i < u.length; i++) {
				System.out.println("     " + u[i].getFullNick());
			}
		}
	}*/
}

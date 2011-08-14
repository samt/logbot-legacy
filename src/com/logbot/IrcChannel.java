package com.logbot;
/**
 * Logbot
 *
 * Copyright (c) 2011 Sam Thompson <sam@websyntax.net>
 * http://www.opensource.org/licenses/mit-license.php The MIT License
 *
 */

import java.util.*;

public class IrcChannel {
	/*
	 * Users 
	 */
	private ArrayList<IrcUser> users = new ArrayList();

	/*
	 * Channel name
	 */
	private String name;

	/*
	 * The only constructor
	 * @param String name - Name of the channel (including the #)
	 */
	public IrcChannel(String name) {
		this.name = name;
	}

	/*
	 * User Join 
	 */
	public void userJoin(String user) {
		IrcUser u = this.getUser(user);

		if (u == null) {
			this.users.add(new IrcUser(user));
		}
	}

	/*
	 * Is the user in the channel?
	 * @param String user - Username of the person to check 
	 * @param boolean - I really hope this is obvious
	 */
	public boolean userInChannel(String user) {
		IrcUser u = this.getUser(user);
		return (u != null) ? true : false;
	}

	/*
	 * User has exited the IRC channel
	 * @param String user - Users who left
	 */
	public void userPart(String user) {
		user = IrcUser.stripPrefix(user);

		Iterator<IrcUser> it = this.users.iterator();
		IrcUser u;

		while (it.hasNext()) {
			u = (IrcUser) it.next();

			if (u.equals(user)) {
				it.remove();
			}
		}
	}

	/*
	 * User has changed nick 
	 * @param String oldNick - Old nickname
	 * @param String newNick - New nickname
	 */
	public void changeNick(String oldNick, String newNick) {
		IrcUser u = this.getUser(oldNick);

		if (u != null) {
			u.changeNick(newNick);
		}
	}

	/*
	 * @param String user 
	 * @param char prefix 
	 */
	public void updateUserPrefix(String user, char prefix) {
		IrcUser u = this.getUser(user);

		if (u != null) {
			u.changePrefix(prefix);
		}
	}

	/*
	 * @param String user 
	 * @param char prefix 
	 */
	public void updateUserPrefix(String user) {
		char prefix = IrcUser.getPrefix(user);

		if (prefix != ' ') {
			this.updateUserPrefix(user, prefix);
		}
	}

	/*
	 * Search for a user by name
	 * @param String user - Users who left
	 * @return IrcUser - returns null if user does not exist. 
	 */
	public IrcUser getUser(String user) {
		user = IrcUser.stripPrefix(user);

		Iterator<IrcUser> it = this.users.iterator();
		IrcUser u;

		while (it.hasNext()) {
			u = (IrcUser) it.next();
			
			if (u.equals(user)) {
				return u;
			}
		}

		return null;
	}

	/*
	 * Equals
	 * @return boolean
	 */
	public boolean equals(IrcChannel c) {
		return this.name.equals(c.name) ? true : false;
	}

	/*
	 * Equals
	 * @return boolean
	 */
	public boolean equals(String name) {
		return this.name.equals(name) ? true : false;
	}

	/*
	 * toString()
	 * @return String - (duh)
	 */
	public String toString() {
		return this.name;
	}
}

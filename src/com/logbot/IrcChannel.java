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
	 * List of users
	 */
	public ArrayList users = new ArrayList();

	/*
	 * Good old default 
	 */
	public IrcChannel() {
	}

	/*
	 * Overloaded function: userJoin
	 * @param String usr - Single user who joined
	 */
	public void userJoin(String usr) {
		if (usr.charAt(0) == '@' ||
			usr.charAt(0) == '%' ||
			usr.charAt(0) == '&' ||
			usr.charAt(0) == '+' ||
			usr.charAt(0) == '~') {

			this.users.add(usr.substring(1));
		}
		else {
			this.users.add(usr);
		}
	}

	/*
	 * Overloaded function: userJoin
	 * @param String[] usrs - Users who join at once (like /NAMES)
	 */
	public void userJoin(String[] usrs) {
		for (int i = 0; i < usrs.length; i++) {
			this.userJoin(usrs[i]);
		}
	}

	/*
	 * Is the user in the channel?
	 * @param String user - Username of the person to check 
	 * @param boolean - I really hope this is obvious
	 */
	public boolean userInChannel(String user) {
		return (this.users.indexOf(user) > -1) ? true : false;
	}

	/*
	 * User has exited the IRC channel
	 * @param String user - Users who left
	 */
	public void userLeave(String user) {
		int pos = this.users.indexOf(user);

		if (pos >= 0) {
			try {
				this.users.remove(pos);
			}
			catch (IndexOutOfBoundsException e) {
			}
		}
	}

	/*
	 * User has changed nick 
	 * @param String oldNick - Old nickname
	 * @param String newNick - New nickname
	 */
	public void changeNick(String oldNIck, String newNick) {
		int pos = this.users.indexOf(oldNIck);

		if (pos >= 0) {
			try {
				this.users.remove(pos);
				this.userJoin(newNick); // This does not have an effect on logging 
			}
			catch (IndexOutOfBoundsException e) {
			}
		}
	}
}

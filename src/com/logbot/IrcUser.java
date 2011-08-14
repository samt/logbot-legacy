package com.logbot;
/**
 * Logbot
 *
 * Copyright (c) 2011 Sam Thompson <sam@websyntax.net>
 * http://www.opensource.org/licenses/mit-license.php The MIT License
 *
 */

import java.util.Arrays;

public class IrcUser {
	/*
	 * Nickname
	 */
	private String nick;

	/*
	 * Prefix
	 */
	private char prefix;

	/*
	 * Prefix list
	 */                                    // +q  +a  +o  +h  +v
	public static final char[] PREFIX_LIST = {'~','&','@','%','+'};

	/*
	 * @param String nick - Nickname
	 * @param char prefix - One of the prefixing symbols
	 */
	public IrcUser(String nick, char prefix) {
		this.nick = nick;
		this.prefix = prefix;
	}

	/*
	 * @param String nick - Nickname
	 * @param char prefix - One of the prefixing symbols
	 */
	public IrcUser(String nick, String prefix) {
		this(nick, prefix.charAt(0));
	}

	/*
	 * Probably the more used constructor - It breaks the stuff apart first 
	 * @param String nick - Full nickname (with prefix)
	 */
	public IrcUser(String nick) {
		this.nick = IrcUser.stripPrefix(nick);
		this.prefix = IrcUser.getPrefix(nick);
	}

	/*
	 * Get the prefix (@, ~, %, &, +)
	 * @param String nick - Nickname
	 * @return char - The prefix, or a space if none exists.
	 */
	public static char getPrefix(String nick) {
		char c = nick.charAt(0);

		for (int i = 0; i < IrcUser.PREFIX_LIST.length; i++) {
			if (IrcUser.PREFIX_LIST[i] == c) {
				return c;
			}
		}

		return ' ';
	}

	/*
	 * Get the name without the prefix
	 * @param String nick - Nickname
	 * @return String - Cleaned nickname
	 */
	public static String stripPrefix(String nick) {
		char c = nick.charAt(0);
		boolean found = false;

		for (int i = 0; i < IrcUser.PREFIX_LIST.length && !found; i++) {
			if (IrcUser.PREFIX_LIST[i] == c) {
				found = true;
			}
		}

		return (found) ? nick.substring(1) : nick;
	}

	/*
	 * User changed their nickname
	 * @param String nick - Nickname
	 */
	public void changeNick(String nick) {
		this.nick = nick;
	}

	/*
	 * Change the user's mode
	 * @param String mode - Mode from raw IRC
	 */
	public void changePrefix(char prefix) {
		this.prefix = prefix;
	}

	/*
	 * @return char prefix
	 */
	public char getUserPrefix() {
		return this.prefix;
	}

	/*
	 * @return char prefix
	 */
	public String getFullNick() {
		return (this.prefix == ' ') ? this.nick : new Character(this.prefix).toString() + this.nick;
	}

	/*
	 * For .contains()
	 */
	public final boolean equals(IrcUser u) {
		return this.nick.equals(u.nick) ? true : false;
	}

	/*
	 * For .contains()
	 */
	public final boolean equals(String nick) {
		return this.nick.equals(nick) ? true : false;
	}

	/*
	 * For automagicical stuff
	 */
	public final String toString() {
		return this.nick;
	}
}

package com.logbot;
/**
 * Logbot
 *
 * Copyright (c) 2011 Sam Thompson <sam@websyntax.net>
 * http://www.opensource.org/licenses/mit-license.php The MIT License
 *
 */

public class IrcInvite {
	/*
	 * Invitor
	 */
	private String invitor = new String();

	/*
	 * Channel
	 */
	private String channel;

	/*
	 */
	public IrcInvite(String invitor, String channel) {
		this.invitor = invitor.trim();
		this.channel = channel.trim();
	}

	/*
	 * @return Boolean
	 */
	public boolean isInvitorAndOp(String name) {
		name = name.trim();
		boolean op = false;

		if (name.charAt(0) == '@' ||
			name.charAt(0) == '%' ||
			name.charAt(0) == '&' ||
			name.charAt(0) == '~') {

			op = true;
			name = name.substring(1);
		}
		else if (name.charAt(0) == '+') {
			name = name.substring(1);
		}

		return name.equals(this.invitor) && op ? true : false;
	}

	/*
	 * To make comparisons easier
	 */
	public final String toString() {
		return this.channel;
	}

	public final boolean equals(IrcInvite i) {
		return this.channel.equals(i.channel) ? true : false;
	}
}

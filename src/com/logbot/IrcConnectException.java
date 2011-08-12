package com.logbot;
/**
 * Logbot
 *
 * Copyright (c) 2011 Sam Thompson <sam@websyntax.net>
 * http://www.opensource.org/licenses/mit-license.php The MIT License
 *
 */

public class IrcConnectException extends Throwable {
	/*
	 * Default constructor
	 */
	public IrcConnectException() {
		super();
	}

	/*
	 * Normal constructor 
	 * @param String err - Error message
	 */
	public IrcConnectException(String err) {
		super(err);
	}
}

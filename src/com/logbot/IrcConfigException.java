package com.logbot;
/**
 * Logbot
 *
 * Copyright (c) 2011 Sam Thompson <sam@websyntax.net>
 * http://www.opensource.org/licenses/mit-license.php The MIT License
 *
 */

public class IrcConfigException extends Throwable {
	/*
	 * Default constructor
	 */
	public IrcConfigException() {
		super();
	}

	/*
	 * Normal constructor 
	 * @param String err - Error message
	 */
	public IrcConfigException(String err) {
		super(err);
	}
}

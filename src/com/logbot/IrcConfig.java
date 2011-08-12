package com.logbot;
/**
 * Logbot
 *
 * Copyright (c) 2011 Sam Thompson <sam@websyntax.net>
 * http://www.opensource.org/licenses/mit-license.php The MIT License
 *
 */

import java.util.*;
import java.io.*;

public class IrcConfig {
	/*
	 * Static properties to be filled and accessed without a config object
	 *
	 * General IRC Configuration
	 */
	public static String host = "";
	public static String port = "";
	public static String user = "";
	public static String name = "";
	public static String nick = "";
	public static String pass = "";

	/*
	 * Net Commander 
	 */
	public static String netComPort = "";
	public static String netComUser = "";
	public static String netComPass = "";

	/*
	 * Log an additional Config file path settings
	 */
	public static String logPath = "";
	public static String errorLog = "";
	public static String channelFile = "";
	public static String networkName = "";

	/*
	 * Load the configuration file
	 * @param String path - Path to the configuration file
	 */
	public static void load(String path) throws IrcConfigException {
		FileInputStream cFStream;
		Properties config = new Properties();
	
		try {
			cFStream = new FileInputStream(path);
		}
		catch (FileNotFoundException e) {
			throw new IrcConfigException("Error loading configuration file: \"" + path + "\" does not exist");
		}

		try {
			config.load(cFStream);
		}
		catch (IOException e) {
			throw new IrcConfigException("Error loading configuration file, IOException was thrown with the message: " + e.getMessage());
		}

		// Fill our configuration vars
		IrcConfig.host = config.getProperty("host");
		IrcConfig.port = config.getProperty("port");
		IrcConfig.user = config.getProperty("user");
		IrcConfig.name = config.getProperty("name");
		IrcConfig.nick = config.getProperty("nick");
		IrcConfig.pass = config.getProperty("pass");

		IrcConfig.netComPort = config.getProperty("netcom_port");
		IrcConfig.netComUser = config.getProperty("netcom_user");
		IrcConfig.netComPass = config.getProperty("netcom_pass");

		IrcConfig.logPath = config.getProperty("logpath");
		IrcConfig.errorLog = config.getProperty("errorlog");
		IrcConfig.channelFile = config.getProperty("channelfile");
		IrcConfig.networkName = (config.getProperty("network_name").equals("")) ? IrcConfig.host : config.getProperty("network_name");
	}
}

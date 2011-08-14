package com.logbot;
/**
 * Logbot
 *
 * Copyright (c) 2011 Sam Thompson <sam@websyntax.net>
 * http://www.opensource.org/licenses/mit-license.php The MIT License
 *
 */

import java.io.*;
import java.text.*;
import java.util.*;

public class IrcLogger {
	/*
	 */
	public IrcLogger() {
	}

	/*
	 * Write a log entry
	 *
	 * @param String channel - Channel to send to
	 * @param String msg - Message to write 
	 */
	public static void write(String channel, String msg) {
		try {
			Date date = new Date();
			Format day = new SimpleDateFormat("dd-MMM-yyyy");
			Format time = new SimpleDateFormat("HH:mm:ss");
			
			BufferedWriter out = new BufferedWriter(new FileWriter(IrcConfig.logPath + channel + "-" + day.format(date), true));
			out.write("[" + time.format(date) + "] " + msg + "\n");
			out.close();
		}
		catch (IOException e) {
			System.out.println("Something went wrong with writing to a file: " + e.getMessage());
		}
	}
}

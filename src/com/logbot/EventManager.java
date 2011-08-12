package com.logbot;
/**
 * Logbot
 *
 * Copyright (c) 2011 Sam Thompson <sam@websyntax.net>
 * http://www.opensource.org/licenses/mit-license.php The MIT License
 *
 */

import java.util.*;

public class EventManager {
	/*
	 * Event array list 
	 */
	public ArrayList events = new ArrayList();

	/*
	 */
	public EventManager() {
	}

	/*
	 * Register
	 * @param ModuleInterface m
	 */
	public void register(ModuleInterface e) {
		this.events.add(e);
	}

	/*
	 * Get Queue 
	 * @param IrcMessage m
	 */
	public String[] getQueue(IrcMessage m) {
		ArrayList<String> queue = new ArrayList();
		String[] buffer;
		int size = this.events.size();

		for (int i = 0; i < size; i++) {
			buffer = ((ModuleInterface) this.events.get(i)).run(m);

			if (buffer != null) {
				for (int j = 0; j < buffer.length; j++) {
					queue.add((String) buffer[j]);
				}
			}
		}

		return Arrays.copyOf(queue.toArray(), queue.size(), String[].class);
	}
}

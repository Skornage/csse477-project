/*
 * HangmanGame.java
 * Nov 11, 2015
 *
 * Simple Web Server (SWS) for EE407/507 and CS455/555
 * 
 * Copyright (C) 2011 Chandan Raj Rupakheti, Clarkson University
 * 
 * This program is free software: you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public License 
 * as published by the Free Software Foundation, either 
 * version 3 of the License, or any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/lgpl.html>.
 * 
 * Contact Us:
 * Chandan Raj Rupakheti (rupakhcr@clarkson.edu)
 * Department of Electrical and Computer Engineering
 * Clarkson University
 * Potsdam
 * NY 13699-5722
 * http://clarkson.edu/~rupakhcr
 */

package server;

import java.util.Arrays;

/**
 * 
 * @author Chandan R. Rupakheti (rupakhcr@clarkson.edu)
 */
public abstract class HangmanGame {

	protected String postedByUser;
	protected String word;
	protected String name;
	protected String currentWord;
	protected int id;

	public HangmanGame(int id, String name, String word, String postedByUser) {
		this.id = id;
		this.name = name;
		this.word = word;
		this.postedByUser = postedByUser;
		char[] startWord = new char[word.length()];
		Arrays.fill(startWord, '*');
		this.currentWord = new String(startWord);
	}

	public String getPostedByUser() {
		return postedByUser;
	}

	public String getName() {
		return name;
	}

	public String getCurrentWord() {
		return currentWord;
	}

	public int getID() {
		return this.id;
	}
}

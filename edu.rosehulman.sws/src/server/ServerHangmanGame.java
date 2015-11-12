/*
 * ServerHangmanGame.java
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

/**
 * 
 * @author Chandan R. Rupakheti (rupakhcr@clarkson.edu)
 */
public class ServerHangmanGame extends HangmanGame {
	private String guessesMade;
	private int incorrectGuesses;

	public ServerHangmanGame(int id, String name, String word, String postedByUser) {
		super(id, name, word, postedByUser);
		this.guessesMade = "";
		this.incorrectGuesses = 0;
	}

	// returns true if the guess was valid, regardless of whether the guess was
	// correct or not.
	public boolean makeGuess(char c) {
		if (this.guessesMade.contains(c + "")) {
			return false;
		} else {
			this.guessesMade += c + " ";
			boolean guessedCorrectly = false;
			StringBuilder sb = new StringBuilder(this.currentWord);
			for (int i = 0; i < this.word.length(); i++) {
				if (this.word.charAt(i) == c) {
					guessedCorrectly = true;
					sb.setCharAt(i, c);
				}
			}
			this.currentWord = sb.toString();
			if (!guessedCorrectly) {
				this.incorrectGuesses++;
			}
			return true;
		}
	}

	public String getGuessesMade() {
		return guessesMade;
	}

	public int getIncorrectGuesses() {
		return incorrectGuesses;
	}
}

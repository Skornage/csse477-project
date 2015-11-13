/*
 * HangmanPutGameServerServlet.java
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

import protocol.HttpRequest;
import protocol.HttpResponse;
import protocol.HttpResponseFactory;
import protocol.Protocol;

/**
 * 
 * @author Chandan R. Rupakheti (rupakhcr@clarkson.edu)
 */
public class HangmanPutGameServerServlet extends AbstractPluginServlet {
	private GameManager mgr;

	@Override
	public String getPluginURI() {
		return "hangman";
	}

	@Override
	public String getRequestType() {
		return Protocol.PUT;
	}

	@Override
	public String getServletURI() {
		return "game";
	}

	@Override
	public HttpResponse HandleRequest(HttpRequest request) {
		char c;
		int id;
		try {
			c = request.getBody().charAt(0);
			id = Integer.parseInt(request.getUri().split("/")[3]);
		} catch (Exception e) {
			return null;
		}
		ServerHangmanGame game = (ServerHangmanGame) this.mgr.getGame(id);

		boolean rslt = game.makeGuess(c);
		HttpResponse response = null;
		if (rslt) {
			response = HttpResponseFactory.getPreMadeResponse("200");
			String currentWord = game.getCurrentWord();
			String guessesMade = game.getGuessesMade();
			int incorrectGuesses = game.getIncorrectGuesses();

			if (currentWord.indexOf('*') < 0) {
				response.setBody("you won!");
			} else if (incorrectGuesses > 5) {
				response.setBody("you lose!");
			} else {
				response.setBody(currentWord +":"+ guessesMade +":"+ incorrectGuesses);
			}
		}
		return response;
	}

	@Override
	public void setServer(Server server) {
		this.mgr = ((GameServer) server).getGameManager();
		
	}
}

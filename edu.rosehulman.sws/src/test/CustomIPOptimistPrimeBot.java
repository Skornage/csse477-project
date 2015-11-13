/*
 * CustomIPOptimistPrimeBot.java
 * Nov 2, 2015
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

package test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashMap;

import protocol.HttpResponse;

/**
 * 
 * @author Chandan R. Rupakheti (rupakhcr@clarkson.edu)
 */
public class CustomIPOptimistPrimeBot extends OptimistPrimeBot {
	private String botIP;
	private HashMap<Integer, MockServerSocket> map;

	public CustomIPOptimistPrimeBot(String host, int port,
			int requestsToMakeCount, int sleepTime, String botIP,
			HashMap<Integer, MockServerSocket> map) {
		super(host, port, requestsToMakeCount, sleepTime);
		this.botIP = botIP;
		this.map = map;
	}

	@Override
	protected boolean playGame() {
		try {
			MockServerSocket brokerMss = this.map.get(this.port);
			HttpResponse response = makeRequest(
					"GET /games/ HTTP/1.1\naccept-language: en-US,en;q=0.8\n\n",
					brokerMss);
			checkResponse(response);

			response = makeRequest(
					"POST /games/create HTTP/1.1\naccept-language: en-US,en;q=0.8\nname: andy\nposted-by: andy\nword: iamthewalrus\n\n",
					brokerMss);
			checkResponse(response);

			response = makeRequest(
					"GET /games/ HTTP/1.1\naccept-language: en-US,en;q=0.8\n\n",
					brokerMss);
			checkResponse(response);

			int gameID = -1;

			gameID = Integer.parseInt(response.getBody());

			Thread.sleep(10);

			response = makeRequest("GET /games/play/" + gameID
					+ " HTTP/1.1\naccept-language: en-US,en;q=0.8\n\n",
					brokerMss);
			checkResponse(response);

			Thread.sleep(10);

			int gamePort = Integer.parseInt(response.getBody().split(":")[1]);

			MockServerSocket mssGame = this.map.get(gamePort);

			response = makeRequest(
					"PUT /hangman/game/0 HTTP/1.1\naccept-language: en-US,en;q=0.8\ncontent-length: 1\n\na\n\n",
					mssGame);
			checkResponse(response);

			response = makeRequest(
					"PUT /hangman/game/0 HTTP/1.1\naccept-language: en-US,en;q=0.8\ncontent-length: 1\n\ni\n\n",
					mssGame);
			checkResponse(response);

			response = makeRequest(
					"PUT /hangman/game/0 HTTP/1.1\naccept-language: en-US,en;q=0.8\ncontent-length: 1\n\nm\n\n",
					mssGame);
			checkResponse(response);

			response = makeRequest(
					"PUT /hangman/game/0 HTTP/1.1\naccept-language: en-US,en;q=0.8\ncontent-length: 1\n\nt\n\n",
					mssGame);
			checkResponse(response);

			response = makeRequest(
					"PUT /hangman/game/0 HTTP/1.1\naccept-language: en-US,en;q=0.8\ncontent-length: 1\n\nh\n\n",
					mssGame);
			checkResponse(response);

			response = makeRequest(
					"PUT /hangman/game/0 HTTP/1.1\naccept-language: en-US,en;q=0.8\ncontent-length: 1\n\ne\n\n",
					mssGame);
			checkResponse(response);

			response = makeRequest(
					"PUT /hangman/game/0 HTTP/1.1\naccept-language: en-US,en;q=0.8\ncontent-length: 1\n\nw\n\n",
					mssGame);
			checkResponse(response);

			response = makeRequest(
					"PUT /hangman/game/0 HTTP/1.1\naccept-language: en-US,en;q=0.8\ncontent-length: 1\n\nu\n\n",
					mssGame);
			checkResponse(response);

			response = makeRequest(
					"PUT /hangman/game/0 HTTP/1.1\naccept-language: en-US,en;q=0.8\ncontent-length: 1\n\nr\n\n",
					mssGame);
			checkResponse(response);

			response = makeRequest(
					"PUT /hangman/game/0 HTTP/1.1\naccept-language: en-US,en;q=0.8\ncontent-length: 1\n\nl\n\n",
					mssGame);
			checkResponse(response);

			response = makeRequest(
					"PUT /hangman/game/0 HTTP/1.1\naccept-language: en-US,en;q=0.8\ncontent-length: 1\n\ns\n\n",
					mssGame);
			checkResponse(response);

			if (!response.getBody().equals("you won!")) {
				throw new Exception("didn't win!");
			}

			return true;

		} catch (Exception e) {
			return false;
		}

	}

	protected HttpResponse makeRequest(String request, MockServerSocket mss)
			throws Exception {
		MockSocket socket = new MockSocket(new MockSocketAddress(this.botIP),
				request);

		mss.addMockSocket(socket);

		ByteArrayOutputStream baos = (ByteArrayOutputStream) socket
				.getOutputStream();

		while (!socket.isClosed()) {
			Thread.sleep(10);
		}
		String resp = baos.toString();

		InputStream in = new ByteArrayInputStream(resp.getBytes());
		return HttpResponse.read(in);
	}
}

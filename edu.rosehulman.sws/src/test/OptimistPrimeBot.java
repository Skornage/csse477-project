/*
 * OptimistPrimeBot.java
 * Oct 29, 2015
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

import protocol.HttpRequest;
import protocol.HttpResponse;
import protocol.Protocol;

/**
 * 
 * @author Chandan R. Rupakheti (rupakhcr@clarkson.edu)
 */
public class OptimistPrimeBot implements Runnable {
	private String host = "localhost";
	protected int port = 8080;
	private int failedCount = 0;
	private int successCount = 0;
	private int sleepTime;
	private int gamesToPlayCount;
	private int gamesPlayedCount = 0;
	private static long startTime = Long.MIN_VALUE;
	private static long endTime = Long.MIN_VALUE;

	public OptimistPrimeBot(String host, int port, int gamesToPlayCount,
			int sleepTime) {
		this.host = host;
		this.port = port;
		this.sleepTime = sleepTime;
		this.gamesToPlayCount = gamesToPlayCount;
		resetTimes();
	}

	private static void resetTimes() {
		startTime = Long.MIN_VALUE;
		endTime = Long.MIN_VALUE;
	}

	@Override
	public void run() {
		if (startTime == Long.MIN_VALUE) {
			startTime = System.currentTimeMillis();
		}
		while (this.gamesToPlayCount > this.gamesPlayedCount) {
			this.gamesPlayedCount++;
			boolean rslt = this.playGame();
			if (!rslt) {
				this.failedCount++;
			} else {
				this.successCount++;
			}
			try {
				Thread.sleep(this.sleepTime);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		long myEndTime = System.currentTimeMillis();
		if (myEndTime > endTime) {
			endTime = myEndTime;
		}
	}

	protected int getFailedRequestCount() {
		return this.failedCount;
	}

	protected int getSuccededRequestCount() {
		return this.successCount;
	}

	protected boolean playGame() {
		try {
			HttpResponse response = HttpRequest
					.makeRequest(
							"GET /games/ HTTP/1.1\naccept-language: en-US,en;q=0.8\n\n",
							this.host, this.port);
			checkResponse(response);

			response = HttpRequest
					.makeRequest(
							"POST /games/create HTTP/1.1\naccept-language: en-US,en;q=0.8\nname: andy\nposted-by: andy\nword: iamthewalrus\n\n",
							this.host, this.port);
			checkResponse(response);

			response = HttpRequest
					.makeRequest(
							"GET /games/ HTTP/1.1\naccept-language: en-US,en;q=0.8\n\n",
							this.host, this.port);
			checkResponse(response);

			int gameID = -1;

			gameID = Integer.parseInt(response.getBody());

			Thread.sleep(10);

			response = HttpRequest.makeRequest("GET /games/play/" + gameID
					+ " HTTP/1.1\naccept-language: en-US,en;q=0.8\n\n",
					this.host, this.port);
			checkResponse(response);

			Thread.sleep(10);

			int gamePort = Integer.parseInt(response.getBody().split(":")[1]);
			String ip = response.getBody().split(":")[0];

			response = HttpRequest
					.makeRequest(
							"PUT /hangman/game/0 HTTP/1.1\naccept-language: en-US,en;q=0.8\ncontent-length: 1\n\na\n\n",
							ip, gamePort);
			checkResponse(response);

			response = HttpRequest
					.makeRequest(
							"PUT /hangman/game/0 HTTP/1.1\naccept-language: en-US,en;q=0.8\ncontent-length: 1\n\ni\n\n",
							ip, gamePort);
			checkResponse(response);

			response = HttpRequest
					.makeRequest(
							"PUT /hangman/game/0 HTTP/1.1\naccept-language: en-US,en;q=0.8\ncontent-length: 1\n\nm\n\n",
							ip, gamePort);
			checkResponse(response);

			response = HttpRequest
					.makeRequest(
							"PUT /hangman/game/0 HTTP/1.1\naccept-language: en-US,en;q=0.8\ncontent-length: 1\n\nt\n\n",
							ip, gamePort);
			checkResponse(response);

			response = HttpRequest
					.makeRequest(
							"PUT /hangman/game/0 HTTP/1.1\naccept-language: en-US,en;q=0.8\ncontent-length: 1\n\nh\n\n",
							ip, gamePort);
			checkResponse(response);

			response = HttpRequest
					.makeRequest(
							"PUT /hangman/game/0 HTTP/1.1\naccept-language: en-US,en;q=0.8\ncontent-length: 1\n\ne\n\n",
							ip, gamePort);
			checkResponse(response);

			response = HttpRequest
					.makeRequest(
							"PUT /hangman/game/0 HTTP/1.1\naccept-language: en-US,en;q=0.8\ncontent-length: 1\n\nw\n\n",
							ip, gamePort);
			checkResponse(response);

			response = HttpRequest
					.makeRequest(
							"PUT /hangman/game/0 HTTP/1.1\naccept-language: en-US,en;q=0.8\ncontent-length: 1\n\nu\n\n",
							ip, gamePort);
			checkResponse(response);

			response = HttpRequest
					.makeRequest(
							"PUT /hangman/game/0 HTTP/1.1\naccept-language: en-US,en;q=0.8\ncontent-length: 1\n\nr\n\n",
							ip, gamePort);
			checkResponse(response);

			response = HttpRequest
					.makeRequest(
							"PUT /hangman/game/0 HTTP/1.1\naccept-language: en-US,en;q=0.8\ncontent-length: 1\n\nl\n\n",
							ip, gamePort);
			checkResponse(response);

			response = HttpRequest
					.makeRequest(
							"PUT /hangman/game/0 HTTP/1.1\naccept-language: en-US,en;q=0.8\ncontent-length: 1\n\ns\n\n",
							ip, gamePort);
			checkResponse(response);

			if (!response.getBody().equals("you won!")) {
				throw new Exception("didn't win!");
			}

			return true;

		} catch (Exception e) {
			return false;
		}

	}

	protected void checkResponse(HttpResponse response) throws Exception {
		if (response == null || !Protocol.VERSION.equals(response.getVersion())
				|| !Protocol.OK_TEXT.equals(response.getPhrase())
				|| response.getStatus() != Protocol.OK_CODE) {
			throw new Exception("bad request!");
		}
	}

	public static long getEndTime() {
		return OptimistPrimeBot.endTime;
	}

	public static long getStartTime() {
		return OptimistPrimeBot.startTime;
	}
}

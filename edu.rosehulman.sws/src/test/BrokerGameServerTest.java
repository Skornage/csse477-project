/*
 * BrokerGameServerTest.java
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

package test;

import java.io.ByteArrayInputStream;

import org.junit.Test;

import protocol.HttpRequest;
import protocol.HttpResponse;
import protocol.Protocol;
import server.Broker;
import server.GameServer;

/**
 * 
 * @author Chandan R. Rupakheti (rupakhcr@clarkson.edu)
 */
public class BrokerGameServerTest {

	private Broker broker;
	private Thread brokerThread;
	private GameServer gameServer;
	private Thread serverThread;
	private GameServer gameServerB;
	private Thread serverThreadB;

	@Test
	public void test() {
		String rootDirectoryPath = System.getProperty("user.dir")
				+ System.getProperty("file.separator") + "web";

		// String request =
		// "POST / HTTP/1.1\naccept-language: en-US,en;q=0.8\n\n";

		// try {
		// HttpRequest parsedRequest = HttpRequest.read(
		// new ByteArrayInputStream(request.getBytes()));
		// System.out.println(parsedRequest.getBody());
		// System.out.println(parsedRequest.toString());
		// } catch (Exception e1) {
		// e1.printStackTrace();
		// }

		this.broker = new Broker(rootDirectoryPath, 8080, new MockWebServer(),
				2000, 1000, 82,
				"super-secret-application-key-whatever-you-do-dont-commit-this-to-github");
		this.brokerThread = new Thread(this.broker);
		this.brokerThread.start();

		this.gameServer = new GameServer(rootDirectoryPath, 81,
				new MockWebServer(), 2000, 1000, 82, "localhost",
				"super-secret-application-key-whatever-you-do-dont-commit-this-to-github");
		this.serverThread = new Thread(this.gameServer);
		this.serverThread.start();

		// this.gameServerB = new GameServer(rootDirectoryPath, 83,
		// new MockWebServer(), 2000, 1000, 82, "localhost",
		// "super-secret-application-key-whatever-you-do-dont-commit-this-to-github");
		// this.serverThreadB = new Thread(this.gameServerB);
		// this.serverThreadB.start();

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		// HttpResponse response =
		// HttpRequest.makeRequest(Protocol.GET+" /games/ "+Protocol.VERSION,
		// "localhost", 80);
		// HttpResponse response =
		// HttpRequest.makeRequest("GET /games/ HTTP/1.1\naccept-language: en-US,en;q=0.8\n\n",
		// "localhost", 8080);
		// HttpResponse response =
		// HttpRequest.makeRequest("POST /games/create HTTP/1.1\naccept-language: en-US,en;q=0.8\nname: andy\nposted-by: andy\nword: iamthewalrus\n\n",
		// "localhost", 8080);
		HttpRequest.makeRequest(
				"GET /games/ HTTP/1.1\naccept-language: en-US,en;q=0.8\n\n",
				"localhost", 8080);
		HttpRequest
				.makeRequest(
						"POST /games/create HTTP/1.1\naccept-language: en-US,en;q=0.8\nname: andy\nposted-by: andy\nword: iamthewalrus\n\n",
						"localhost", 8080);
		HttpRequest.makeRequest(
				"GET /games/ HTTP/1.1\naccept-language: en-US,en;q=0.8\n\n",
				"localhost", 8080);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		HttpResponse response = HttpRequest
				.makeRequest(
						"GET /games/play/0 HTTP/1.1\naccept-language: en-US,en;q=0.8\n\n",
						"localhost", 8080);

		int port = Integer.parseInt(response.getBody().split(":")[1]);
		// String ip = response.getBody().split(":")[0].split("/")[1];
		String ip = "localhost";

		HttpRequest
		.makeRequest(
				"PUT /hangman/game/0 HTTP/1.1\naccept-language: en-US,en;q=0.8\ncontent-length: 1\n\na\n\n",
				ip, port);
		
		HttpRequest
				.makeRequest(
						"PUT /hangman/game/0 HTTP/1.1\naccept-language: en-US,en;q=0.8\ncontent-length: 1\n\ni\n\n",
						ip, port);
		
		HttpRequest
		.makeRequest(
				"PUT /hangman/game/0 HTTP/1.1\naccept-language: en-US,en;q=0.8\ncontent-length: 1\n\nm\n\n",
				ip, port);
		
		HttpRequest
		.makeRequest(
				"PUT /hangman/game/0 HTTP/1.1\naccept-language: en-US,en;q=0.8\ncontent-length: 1\n\nt\n\n",
				ip, port);
		
		HttpRequest
		.makeRequest(
				"PUT /hangman/game/0 HTTP/1.1\naccept-language: en-US,en;q=0.8\ncontent-length: 1\n\nh\n\n",
				ip, port);
		
		HttpRequest
		.makeRequest(
				"PUT /hangman/game/0 HTTP/1.1\naccept-language: en-US,en;q=0.8\ncontent-length: 1\n\ne\n\n",
				ip, port);
		
		HttpRequest
		.makeRequest(
				"PUT /hangman/game/0 HTTP/1.1\naccept-language: en-US,en;q=0.8\ncontent-length: 1\n\nw\n\n",
				ip, port);
		
		HttpRequest
		.makeRequest(
				"PUT /hangman/game/0 HTTP/1.1\naccept-language: en-US,en;q=0.8\ncontent-length: 1\n\nu\n\n",
				ip, port);
		
		HttpRequest
		.makeRequest(
				"PUT /hangman/game/0 HTTP/1.1\naccept-language: en-US,en;q=0.8\ncontent-length: 1\n\nr\n\n",
				ip, port);
		
		HttpRequest
		.makeRequest(
				"PUT /hangman/game/0 HTTP/1.1\naccept-language: en-US,en;q=0.8\ncontent-length: 1\n\nl\n\n",
				ip, port);

		response = HttpRequest
				.makeRequest(
						"PUT /hangman/game/0 HTTP/1.1\naccept-language: en-US,en;q=0.8\ncontent-length: 1\n\ns\n\n",
						ip, port);
		System.out.println(response.toString());

		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

}

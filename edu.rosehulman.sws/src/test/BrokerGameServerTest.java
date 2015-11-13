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

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.junit.Test;

import protocol.HttpRequest;
import protocol.HttpResponse;
import server.Broker;
import server.GameServer;
import server.TestableBroker;
import server.TestableGameServer;

/**
 * 
 * @author Chandan R. Rupakheti (rupakhcr@clarkson.edu)
 */
public class BrokerGameServerTest {

	private Broker broker;
	private Thread brokerThread;
	private GameServer gameServerA;
	private Thread serverThreadA;
	private GameServer gameServerB;
	private Thread serverThreadB;
	private GameServer gameServerC;
	private Thread serverThreadC;

	@Test
	public void testSingle() {

		startServers();

		HttpResponse response;
		HttpRequest.makeRequest(
				"GET /games/ HTTP/1.1\naccept-language: en-US,en;q=0.8\n\n",
				"localhost", 8080);
		response = HttpRequest
				.makeRequest(
						"POST /games/create HTTP/1.1\naccept-language: en-US,en;q=0.8\nname: andy\nposted-by: andy\nword: iamthewalrus\n\n",
						"localhost", 8080);
		System.out.println(response);
		HttpRequest.makeRequest(
				"GET /games/ HTTP/1.1\naccept-language: en-US,en;q=0.8\n\n",
				"localhost", 8080);
		int gameID = -1;
		try {
			gameID = Integer.parseInt(response.getBody());
		} catch (Exception e) {

		}
		try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		response = HttpRequest.makeRequest("GET /games/play/" + gameID
				+ " HTTP/1.1\naccept-language: en-US,en;q=0.8\n\n",
				"localhost", 8080);

		try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		int port = Integer.parseInt(response.getBody().split(":")[1]);
		String ip = response.getBody().split(":")[0];

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

	}

	@Test
	public void testThroughput() {
		System.out.println("Throughput Test:");
		startServers();
		ArrayList<OptimistPrimeBot> bots = new ArrayList<OptimistPrimeBot>();
		int numberOfRequests = 10;
		int sleepTime = 1;
		int numberOfThreads = 100;
		int serverRunTime = 8000;
		for (int i = 0; i < numberOfThreads; i++) {
			bots.add(new OptimistPrimeBot("localhost", 8080, numberOfRequests,
					sleepTime));
		}

		double totalTime = 0;

		for (int i = 0; i < numberOfThreads; i++) {
			new Thread(bots.get(i)).start();
		}

		try {
			Thread.sleep(serverRunTime);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		totalTime = (double) ((double) (OptimistPrimeBot.getEndTime() - OptimistPrimeBot
				.getStartTime()) / (double) 1000);

		System.out.println("  Total time:" + totalTime);

		System.out.println("	The server processed "
				+ (numberOfRequests * numberOfThreads) + " requests in "
				+ totalTime + " seconds at an average rate of "
				+ (int) ((numberOfRequests * numberOfThreads) / totalTime)
				+ " requests per second.");
	}

	@Test
	public void testLatency() {
		System.out.println("Latency Test: ");
		startServers();

		int numberOfGames = 1000;
		int serverRunTime = 7000;
		int sleepTime = 1;

		OptimistPrimeBot bot = new OptimistPrimeBot("localhost", 8080,
				numberOfGames, sleepTime);
		new Thread(bot).start();

		try {
			Thread.sleep(serverRunTime);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		System.out
				.println("	The broker server processed requests at an average service time of "
						+ this.broker.getAvgRequestProcessTime()
						+ " milliseconds per request");

		System.out
				.println("Game Server A processed requests at an average service time of "
						+ this.gameServerA.getAvgRequestProcessTime()
						+ " milliseconds per request");

		System.out
				.println("Game Server B processed requests at an average service time of "
						+ this.gameServerB.getAvgRequestProcessTime()
						+ " milliseconds per request");

		System.out
				.println("Game Server C processed requests at an average service time of "
						+ this.gameServerC.getAvgRequestProcessTime()
						+ " milliseconds per request");
	}

	@Test
	public void testMaximumLoadMock() {
		System.out.println("Maximum Load MOCK Test:");

		HashMap<Integer, MockServerSocket> map = startMockServers(7, 1);
		ArrayList<OptimistPrimeBot> bots = new ArrayList<OptimistPrimeBot>();
		int numberOfThreads = 4000;
		int serverRunTime = 30000;
		int numberOfRequests = 400;

		// int numberOfRequests = 10000000;
		// int numberOfThreads = 40000;
		// int serverRunTime = 90000;

		int sleepTime = 1;
		for (int i = 0; i < numberOfThreads; i++) {
			bots.add(new CustomIPOptimistPrimeBot("localhost", 8080,
					numberOfRequests, sleepTime, Integer.toString(i), map));
		}

		for (int i = 0; i < numberOfThreads; i++) {
			new Thread(bots.get(i)).start();
		}

		try {
			Thread.sleep(serverRunTime);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		int totalRequestsExpected = numberOfRequests * numberOfThreads;
		int totalFailedRequests = 0;
		int totalSucceededRequests = 0;

		for (int i = 0; i < numberOfThreads; i++) {
			totalFailedRequests += bots.get(i).getFailedRequestCount();
			totalSucceededRequests += bots.get(i).getSuccededRequestCount();
		}
		double avgFailedRequests = ((double) totalFailedRequests)
				/ ((double) numberOfThreads);
		int requestsMade = totalSucceededRequests + totalFailedRequests;
		System.out
				.println("	The server successfully serviced "
						+ totalSucceededRequests
						+ " out of "
						+ requestsMade
						+ " requests, "
						+ ((double) ((totalSucceededRequests * 100) / (double) requestsMade))
						+ "%.");
		System.out.println("	There was a total of " + totalFailedRequests
				+ " failed requests across " + numberOfThreads
				+ " threads for an average of " + avgFailedRequests
				+ " failed requests per thread");
		if (requestsMade < totalRequestsExpected) {
			System.out
					.println("Only "
							+ requestsMade
							+ " of the "
							+ numberOfRequests
							+ " intended requests were made before the test ended.  Consider lengthening the server running time for this test.");
		}
	}

	@Test
	public void testDOSRepelledMock() {
		System.out.println("DDOS Repel Mock Test: ");
		HashMap<Integer, MockServerSocket> map = startMockServers(7, 1000);

		int numberOfRequests = 4000;
		int serverRunTime = 15000;
		int sleepTime = 1;

		CustomIPOptimistPrimeBot bot = new CustomIPOptimistPrimeBot(
				"localhost", 8080, numberOfRequests, sleepTime, "my ip", map);
		new Thread(bot).start();

		try {
			Thread.sleep(serverRunTime);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		int failCount = bot.getFailedRequestCount();
		int successCount = bot.getSuccededRequestCount();
		int requestsMade = successCount + failCount;
		System.out.println("	The server successfully serviced " + successCount
				+ " out of " + requestsMade + " requests, "
				+ ((double) ((successCount * 100) / (double) requestsMade))
				+ "%.");
		if (requestsMade < numberOfRequests) {
			System.out
					.println("Only "
							+ requestsMade
							+ " of the "
							+ numberOfRequests
							+ " intended requests were made before the test ended.  Consider lengthening the server running time for this test.");
		}
	}

	private void startServers() {

		String rootDirectoryPath = System.getProperty("user.dir")
				+ System.getProperty("file.separator") + "web";

		this.broker = new Broker(
				rootDirectoryPath,
				8080,
				new MockWebServer(),
				2000,
				1000,
				82,
				"super-secret-application-key-whatever-you-do-dont-commit-this-to-github",
				"BrokerPlugins");
		this.brokerThread = new Thread(this.broker);
		this.brokerThread.start();

		this.gameServerA = new GameServer(
				rootDirectoryPath,
				81,
				new MockWebServer(),
				2000,
				1000,
				82,
				"localhost",
				"super-secret-application-key-whatever-you-do-dont-commit-this-to-github",
				"GameServerPlugins");
		this.serverThreadA = new Thread(this.gameServerA);
		this.serverThreadA.start();

		this.gameServerB = new GameServer(
				rootDirectoryPath,
				83,
				new MockWebServer(),
				2000,
				1000,
				82,
				"localhost",
				"super-secret-application-key-whatever-you-do-dont-commit-this-to-github",
				"GameServerPlugins");
		this.serverThreadB = new Thread(this.gameServerB);
		this.serverThreadB.start();

		this.gameServerC = new GameServer(
				rootDirectoryPath,
				84,
				new MockWebServer(),
				2000,
				1000,
				82,
				"localhost",
				"super-secret-application-key-whatever-you-do-dont-commit-this-to-github",
				"GameServerPlugins");
		this.serverThreadC = new Thread(this.gameServerC);
		this.serverThreadC.start();

		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private HashMap<Integer, MockServerSocket> startMockServers(
			int DOSRequestsAllowed, int DOSTimeInterval) {

		String rootDirectoryPath = System.getProperty("user.dir")
				+ System.getProperty("file.separator") + "web";

		MockServerSocket mssBroker;
		MockServerSocket mssA;
		MockServerSocket mssB;
		MockServerSocket mssC;
		try {
			mssBroker = new MockServerSocket();
			mssA = new MockServerSocket();
			mssB = new MockServerSocket();
			mssC = new MockServerSocket();
		} catch (IOException e1) {
			e1.printStackTrace();
			return null;
		}

		this.broker = new TestableBroker(
				rootDirectoryPath,
				8080,
				new MockWebServer(),
				DOSRequestsAllowed,
				DOSTimeInterval,
				82,
				"super-secret-application-key-whatever-you-do-dont-commit-this-to-github",
				"BrokerPlugins", mssBroker);
		this.brokerThread = new Thread(this.broker);
		this.brokerThread.start();

		this.gameServerA = new TestableGameServer(
				rootDirectoryPath,
				81,
				new MockWebServer(),
				DOSRequestsAllowed,
				DOSTimeInterval,
				82,
				"localhost",
				"super-secret-application-key-whatever-you-do-dont-commit-this-to-github",
				"GameServerPlugins", mssA);
		this.serverThreadA = new Thread(this.gameServerA);
		this.serverThreadA.start();

		this.gameServerB = new TestableGameServer(
				rootDirectoryPath,
				83,
				new MockWebServer(),
				DOSRequestsAllowed,
				DOSTimeInterval,
				82,
				"localhost",
				"super-secret-application-key-whatever-you-do-dont-commit-this-to-github",
				"GameServerPlugins", mssB);
		this.serverThreadB = new Thread(this.gameServerB);
		this.serverThreadB.start();

		this.gameServerC = new TestableGameServer(
				rootDirectoryPath,
				84,
				new MockWebServer(),
				DOSRequestsAllowed,
				DOSTimeInterval,
				82,
				"localhost",
				"super-secret-application-key-whatever-you-do-dont-commit-this-to-github",
				"GameServerPlugins", mssC);
		this.serverThreadC = new Thread(this.gameServerC);
		this.serverThreadC.start();

		HashMap<Integer, MockServerSocket> map = new HashMap<Integer, MockServerSocket>();
		map.put(this.broker.getPort(), mssBroker);
		map.put(this.gameServerA.getPort(), mssA);
		map.put(this.gameServerB.getPort(), mssB);
		map.put(this.gameServerC.getPort(), mssC);

		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		return map;

	}

}

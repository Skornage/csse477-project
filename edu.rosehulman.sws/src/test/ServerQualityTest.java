/*
 * ServerQualityTest.java
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

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;

import org.junit.Test;

import protocol.HttpResponse;
import server.MockServer;
import server.Server;

/**
 * 
 * @author Chandan R. Rupakheti (rupakhcr@clarkson.edu)
 */
public class ServerQualityTest {
	static Server server;
	private Thread serverThread;

	@Test
	public void testTimeToRepair() {
		System.out.println("Time To Repair Test:");
		startServer();
		ArrayList<OptimistPrimeBot> bots = new ArrayList<OptimistPrimeBot>();
		int numberOfRequests = 10;
		int sleepTime = 1;
		int numberOfThreads = 400;
		int serverRunTime = 100;
		for (int i = 0; i < numberOfThreads; i++) {
			bots.add(new OptimistPrimeBot(numberOfRequests, sleepTime));
		}

		for (int i = 0; i < numberOfThreads; i++) {
			new Thread(bots.get(i)).start();
		}

		try {
			Thread.sleep(serverRunTime);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("	stopping");
		long startTime = System.currentTimeMillis();
		server.stop();

		// try {
		// serverThread.sleep(4000);
		// } catch (InterruptedException e) {
		// }

		startServer();
		long interval = System.currentTimeMillis() - startTime;
		System.out.println("	started");
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

		System.out.println("	The system was down for a total of " + interval
				+ " milliseconds.");
		System.out.println("	During this time, there was a total of "
				+ totalFailedRequests + " failed requests across "
				+ numberOfThreads + " threads for an average of "
				+ avgFailedRequests + " failed requests per thread");
		if (requestsMade < totalRequestsExpected) {
			System.out
					.println("Only "
							+ requestsMade
							+ " of the "
							+ totalRequestsExpected
							+ " intended requests were made before the test ended.  Consider lengthening the server running time for this test.");
		}
		server.stop();
	}

	@Test
	public void testThroughput() {
		System.out.println("Throughput Test:");
		startServer();
		ArrayList<OptimistPrimeBot> bots = new ArrayList<OptimistPrimeBot>();
		int numberOfRequests = 10;
		int sleepTime = 1;
		int numberOfThreads = 100;
		int serverRunTime = 8000;
		for (int i = 0; i < numberOfThreads; i++) {
			bots.add(new OptimistPrimeBot(numberOfRequests, sleepTime));
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
		server.stop();
	}

	@Test
	public void testLatency() {
		System.out.println("Latency Test: ");
		startServer();

		int numberOfRequests = 1000;
		int serverRunTime = 7000;
		int sleepTime = 1;

		OptimistPrimeBot bot = new OptimistPrimeBot(numberOfRequests, sleepTime);
		new Thread(bot).start();

		try {
			Thread.sleep(serverRunTime);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		System.out
				.println("	The server processed requests at an average service time of "
						+ server.getAvgRequestProcessTime()
						+ " milliseconds per request");
		server.stop();
	}

	@Test
	public void testMaximumLoadMock() {
		System.out.println("Maximum Load MOCK Test:");
		MockServerSocket mss = null;
		try {
			mss = new MockServerSocket();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		startMockServer(mss, 7, 1);
		ArrayList<OptimistPrimeBot> bots = new ArrayList<OptimistPrimeBot>();
		int numberOfThreads = 4000;
		int serverRunTime = 30000;
		int numberOfRequests = 400;

		// int numberOfRequests = 10000000;
		// int numberOfThreads = 40000;
		// int serverRunTime = 90000;

		int sleepTime = 1;
		for (int i = 0; i < numberOfThreads; i++) {
			bots.add(new CustomIPOptimistPrimeBot(numberOfRequests, sleepTime,
					Integer.toString(i), mss));
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
		server.stop();

	}

	@Test
	public void testMaximumLoad() {
		System.out.println("Maximum Load Test:");
		startServer();
		ArrayList<OptimistPrimeBot> bots = new ArrayList<OptimistPrimeBot>();
		int numberOfThreads = 400;
		int serverRunTime = 30000;
		int numberOfRequests = 40;

		// int numberOfRequests = 10000000;
		// int numberOfThreads = 40000;
		// int serverRunTime = 90000;

		int sleepTime = 1;
		for (int i = 0; i < numberOfThreads; i++) {
			bots.add(new OptimistPrimeBot(numberOfRequests, sleepTime));
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
		server.stop();

	}

	@Test
	public void testEncryption() {
		System.out.println("Encryption Test: ");
		startServer();

		String put = "PUT /FilePlugin/FilePutServlet/test.html HTTP/1.1\naccept-language: en-US,en;q=0.8\nContent-Length: 4\nhost: localhost\nconnection: Keep-Alive\nuser-agent: HttpTestClient/1.0\naccept: text/html,text/plain,application/xml,application/json\n\ntest";

		HttpResponse actualResponse = new OptimistPrimeBot(1, 1)
				.makeRequest(put);

		String expectedBody = "test";

		if (expectedBody.equals(actualResponse.getBody().replace("\n", "")
				.replace("\r", ""))) {
			System.out.println("	The response was not encrypted.");
			fail();
		} else {
			System.out.println("	The response was encrypted!  Good Job!");
		}
		server.stop();

	}

	@Test
	public void testDDOSRepelledMock() {
		System.out.println("DDOS Repel Mock Test: ");
		MockServerSocket mss = null;
		try {
			mss = new MockServerSocket();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		startMockServer(mss, 7, 1000);

		int numberOfRequests = 4000;
		int serverRunTime = 15000;
		int sleepTime = 1;

		CustomIPOptimistPrimeBot bot = new CustomIPOptimistPrimeBot(
				numberOfRequests, sleepTime, "my ip", mss);
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
		server.stop();
	}

	private void startServer() {
		String rootDirectoryPath = System.getProperty("user.dir")
				+ System.getProperty("file.separator") + "web";
		server = new Server(rootDirectoryPath, 8080, new MockWebServer(), 2000,
				1000);
		serverThread = new Thread(server);
		serverThread.start();
	}

	private void startMockServer(MockServerSocket mss, int DOSRequestsAllowed,
			int DOSTimeInterval) {
		String rootDirectoryPath = System.getProperty("user.dir")
				+ System.getProperty("file.separator") + "web";
		server = new MockServer(rootDirectoryPath, 8080, new MockWebServer(),
				DOSRequestsAllowed, DOSTimeInterval, mss);
		serverThread = new Thread(server);
		serverThread.start();
	}
}

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

import java.util.ArrayList;

import org.junit.Test;

import protocol.HttpResponse;
import server.Server;

/**
 * 
 * @author Chandan R. Rupakheti (rupakhcr@clarkson.edu)
 */
public class ServerQualityTest {
	static Server server;
	private Thread serverThread;

//	@Test
//	public void testTimeToRepair() {
//		System.out.println("Time To Repair Test:");
//		startServer();
//		ArrayList<OptimistPrimeBot> bots = new ArrayList<OptimistPrimeBot>();
//		int numberOfRequests = 100000;
//		int sleepTime = 1;
//		int numberOfThreads = 4000;
//		int serverRunTime = 9000;
//		for (int i = 0; i < numberOfThreads; i++) {
//			bots.add(new OptimistPrimeBot(numberOfRequests, sleepTime));
//		}
//
//		for (int i = 0; i < numberOfThreads; i++) {
//			new Thread(bots.get(i)).start();
//		}
//
//		try {
//			Thread.sleep(serverRunTime);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
//		System.out.println("	stopping");
//		long startTime = System.currentTimeMillis();
//		server.stop();
//
//		// try {
//		// serverThread.sleep(4000);
//		// } catch (InterruptedException e) {
//		// }
//
//		startServer();
//		long interval = System.currentTimeMillis() - startTime;
//		System.out.println("	started");
//		try {
//			Thread.sleep(serverRunTime);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
//
//		int totalFailedRequests = 0;
//
//		for (int i = 0; i < numberOfThreads; i++) {
//			totalFailedRequests += bots.get(i).getFailedRequestCount();
//		}
//		double avgFailedRequests = ((double) totalFailedRequests)
//				/ ((double) numberOfThreads);
//		System.out.println("	The system was down for a total of " + interval
//				+ " milliseconds.");
//		System.out.println("	During this time, there was a total of "
//				+ totalFailedRequests + " failed requests across "
//				+ numberOfThreads + " threads for an average of "
//				+ avgFailedRequests + " failed requests per thread");
//
//	}
//
//	@Test
//	public void testThroughput() {
//		System.out.println("Throughput Test:");
//		startServer();
//		ArrayList<OptimistPrimeBot> bots = new ArrayList<OptimistPrimeBot>();
//		int numberOfRequests = 10;
//		int sleepTime = 1;
//		int numberOfThreads = 100;
//		int serverRunTime = 4000;
//		for (int i = 0; i < numberOfThreads; i++) {
//			bots.add(new OptimistPrimeBot(numberOfRequests, sleepTime));
//		}
//
//		double totalTime = 0;
//
//		for (int i = 0; i < numberOfThreads; i++) {
//			new Thread(bots.get(i)).start();
//		}
//
//		try {
//			Thread.sleep(serverRunTime);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
//
//		totalTime = (double) ((double) (OptimistPrimeBot.getEndTime() - OptimistPrimeBot
//				.getStartTime()) / (double) 1000);
//
//		System.out.println("  Total time:" + totalTime);
//
//		System.out.println("	The server processed "
//				+ (numberOfRequests * numberOfThreads) + " requests in "
//				+ totalTime + " seconds at an average rate of "
//				+ (int) ((numberOfRequests * numberOfThreads) / totalTime)
//				+ " requests per second.");
//	}
//
//	@Test
//	public void testLatency() {
//		System.out.println("Latency Test: ");
//		startServer();
//
//		int numberOfRequests = 1000;
//		int serverRunTime = 3000;
//		int sleepTime = 1;
//
//		OptimistPrimeBot bot = new OptimistPrimeBot(numberOfRequests, sleepTime);
//		new Thread(bot).start();
//
//		try {
//			Thread.sleep(serverRunTime);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
//
//		System.out
//				.println("	The server processed requests at an average service time of "
//						+ server.getAvgRequestProcessTime() + " milliseconds");
//	}

//	@Test
//	public void testMaximumLoad() {
//		System.out.println("Maximum Load Test:");
//		startServer();
//		ArrayList<OptimistPrimeBot> bots = new ArrayList<OptimistPrimeBot>();
//		int numberOfThreads = 400;
//		int serverRunTime = 3000;
//		int numberOfRequests = 100;
//
//		// int numberOfRequests = 10000000;
//		// int numberOfThreads = 40000;
//		// int serverRunTime = 90000;
//
//		int sleepTime = 1;
//		for (int i = 0; i < numberOfThreads; i++) {
//			bots.add(new OptimistPrimeBot(numberOfRequests, sleepTime));
//		}
//
//		for (int i = 0; i < numberOfThreads; i++) {
//			new Thread(bots.get(i)).start();
//		}
//
//		try {
//			Thread.sleep(serverRunTime);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
//
//		int totalFailedRequests = 0;
//
//		for (int i = 0; i < numberOfThreads; i++) {
//			totalFailedRequests += bots.get(i).getFailedRequestCount();
//		}
//		double avgFailedRequests = ((double) totalFailedRequests)
//				/ ((double) numberOfThreads);
//		System.out.println("	There was a total of " + totalFailedRequests
//				+ " failed requests across " + numberOfThreads
//				+ " threads for an average of " + avgFailedRequests
//				+ " failed requests per thread");
//
//	}
//
//	@Test
//	public void testEncryption() {
//		System.out.println("Encryption Test: ");
//		startServer();
//
//		String put = "PUT /FilePlugin/FilePutServlet/test.html HTTP/1.1\naccept-language: en-US,en;q=0.8\nContent-Length: 4\nhost: localhost\nconnection: Keep-Alive\nuser-agent: HttpTestClient/1.0\naccept: text/html,text/plain,application/xml,application/json\n\ntest";
//
//		HttpResponse actualResponse = OptimistPrimeBot.makeRequest(put);
//
//		String expectedBody = "test";
//
//		if (expectedBody.equals(actualResponse.getBody().replace("\n", "")
//				.replace("\r", ""))) {
//			System.out.println("	The response was not encrypted.");
//			fail();
//		} else {
//			System.out.println("	The response was encrypted!  Good Job!");
//		}
//
//	}

	@Test
	public void testDDOSRepelled() {
		System.out.println("DDOS Repel Test: ");
		startServer();

		int numberOfRequests = 1000000;
		int serverRunTime = 10000;
		int sleepTime = 1;

		OptimistPrimeBot bot = new OptimistPrimeBot(numberOfRequests, sleepTime);
		new Thread(bot).start();

		try {
			Thread.sleep(serverRunTime);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		int successCount = numberOfRequests - bot.getFailedRequestCount();
		System.out.println("	The server successfully serviced " + successCount
				+ " out of " + numberOfRequests + " requests, "
				+ ((double) ((successCount * 100) / (double) numberOfRequests))
				+ "%.");
	}

	private void startServer() {
		String rootDirectoryPath = System.getProperty("user.dir")
				+ System.getProperty("file.separator") + "web";
		server = new Server(rootDirectoryPath, 8080, new MockWebServer());
		serverThread = new Thread(server);
		serverThread.start();
	}

}

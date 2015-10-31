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

import java.util.ArrayList;

import org.junit.Test;

import server.Server;

/**
 * 
 * @author Chandan R. Rupakheti (rupakhcr@clarkson.edu)
 */
public class ServerQualityTest {
	static Server server;
	private Thread serverThread;

	@Test
	public void testSteadyState() {
		// TODO log time
		startServer();
		ArrayList<OptimistPrimeBot> bots = new ArrayList<OptimistPrimeBot>();
		int numberOfRequests = 100000;
		int sleepTime = 1;
		int numberOfThreads = 4000;
		int serverRunTime = 9000;
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
		System.out.println("stopping");
		server.stop();

//		try {
//			serverThread.sleep(9000);
//		} catch (InterruptedException e) {
//		}

		startServer();
		System.out.println("started");
		try {
			Thread.sleep(serverRunTime);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		int totalFailedRequests = 0;

		for (int i = 0; i < numberOfThreads; i++) {
			totalFailedRequests += bots.get(i).getFailedRequestCount();
		}
		double avgFailedRequests = ((double) totalFailedRequests)
				/ ((double) numberOfThreads);
		System.out.println("There was a total of " + totalFailedRequests
				+ " failed requests across " + numberOfThreads
				+ " threads for an average of " + avgFailedRequests
				+ " failed requests per thread");

	}

	private void startServer() {
		String rootDirectoryPath = System.getProperty("user.dir")
				+ System.getProperty("file.separator") + "web";
		server = new Server(rootDirectoryPath, 8080, new MockWebServer());
		serverThread = new Thread(server);
		serverThread.start();
	}

}

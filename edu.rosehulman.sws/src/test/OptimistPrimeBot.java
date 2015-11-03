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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import protocol.HttpResponse;
import protocol.Protocol;

/**
 * 
 * @author Chandan R. Rupakheti (rupakhcr@clarkson.edu)
 */
public class OptimistPrimeBot implements Runnable {
	private static final String host = "localhost";
	private static final int port = 8080;
	private int failedRequestCount = 0;
	private int successCount = 0;
	private int sleepTime;
	private int requestsToMakeCount;
	private int requestsMadeCount = 0;
	private static long startTime = Long.MIN_VALUE;
	private static long endTime = Long.MIN_VALUE;

	public OptimistPrimeBot(int requestsToMakeCount, int sleepTime) {
		this.sleepTime = sleepTime;
		this.requestsToMakeCount = requestsToMakeCount;
		resetTimes();
	}

	/**
	 * 
	 */
	private static void resetTimes() {
		startTime = Long.MIN_VALUE;
		endTime = Long.MIN_VALUE;
	}

	@Override
	public void run() {
		if (startTime == Long.MIN_VALUE) {
			startTime = System.currentTimeMillis();
		}
		while (this.requestsToMakeCount > this.requestsMadeCount) {
			this.requestsMadeCount++;
			boolean rslt = this.makeGetHomePageRequest();
			if (!rslt) {
				this.failedRequestCount++;
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

	protected HttpResponse makeRequest(String request) {
		Socket socket = null;
		try {
			socket = new Socket(host, port);
		} catch (Exception e1) {
			try {
				socket.close();
			} catch (Exception e) {
				return null;
			}
			return null;
		}
		try {
			if (!socket.isConnected()) {
				socket.close();
				throw new Exception("Socket is not connected!");
			}

			OutputStream out = socket.getOutputStream();
			out.write(request.getBytes());
			out.flush();
		} catch (final Exception e) {
			try {
				socket.close();
			} catch (IOException e2) {
				return null;
			}
			return null;
		}
		try {
			InputStream in = socket.getInputStream();
			return HttpResponse.read(in);

		} catch (final Exception e) {
			try {
				socket.close();
			} catch (IOException e2) {
				return null;
			}
			return null;
		}
	}

	protected int getFailedRequestCount() {
		return this.failedRequestCount;
	}

	protected int getSuccededRequestCount() {
		return this.successCount;
	}

	protected boolean makeGetHomePageRequest() {
		String request = "GET /FilePlugin/FileGetServlet/index.html HTTP/1.1\naccept-language: en-US,en;q=0.8\nhost: localhost\nconnection: Keep-Alive\nuser-agent: HttpTestClient/1.0\naccept: text/html,text/plain,application/xml,application/json\n\n";

		HttpResponse actualResponse = makeRequest(request);
		if (actualResponse == null) {
			return false;
		}

		String expectedBody = "<html><head>	<title>Test Page</title></head><body>	<p>Test Page Successful!</p></body></html>";

		if (!expectedBody.equals(actualResponse.getBody().replace("\n", "")
				.replace("\r", ""))) {
			return false;
		}

		if (!Protocol.VERSION.equals(actualResponse.getVersion())) {
			return false;
		}

		if (!Protocol.OK_TEXT.equals(actualResponse.getPhrase())) {
			return false;
		}
		if (actualResponse.getStatus() != Protocol.OK_CODE) {
			return false;
		}
		return true;
	}

	public static long getEndTime() {
		return OptimistPrimeBot.endTime;
	}

	public static long getStartTime() {
		return OptimistPrimeBot.startTime;
	}
}

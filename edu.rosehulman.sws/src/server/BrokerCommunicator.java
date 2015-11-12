/*
 * BrokerCommunicator.java
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

import java.net.ServerSocket;
import java.net.Socket;

import protocol.HttpRequest;
import protocol.HttpResponse;
import protocol.HttpResponseFactory;

/**
 * 
 * @author Chandan R. Rupakheti (rupakhcr@clarkson.edu)
 */
public class BrokerCommunicator implements Runnable {

	private int brokerPort;
	private String brokerIP;
	private String applicationKey;
	private ServerSocket welcomeSocket;
	private GameManager mgr;
	private int clientPort;

	/**
	 * @param brokerIP
	 * @param brokerPort
	 * @param applicationKey
	 */
	public BrokerCommunicator(String brokerIP, int brokerPort,
			String applicationKey, GameManager mgr, int clientPort) {
		this.applicationKey = applicationKey;
		this.brokerIP = brokerIP;
		this.brokerPort = brokerPort;
		this.mgr = mgr;
		this.clientPort = clientPort;
	}

	@Override
	public void run() {
		// start by sending the broker a request to establish a port. They will
		// send a port to use back in the request. Then, open a new
		// serversocket at that port. Wait for requests.
		// When you get one, check its ip, add game to hashmap
		// and send response back.
		try {
			int commPort = -1;
			while (commPort == -1) {
				String request = "POST / HTTP/1.1\naccept-language: en-US,en;q=0.8\nclient-port: "
						+ this.clientPort
						+ "\napp-key: "
						+ this.applicationKey
						+ "\n\n";
				try {
					HttpResponse response = HttpRequest.makeRequest(request,
							this.brokerIP, this.brokerPort);
					String rslt = response.getHeader().get("broker-port");
					commPort = Integer.parseInt(rslt);
					System.out
							.println("I was assigned to accept games on port:"
									+ commPort + "!!!");
				} catch (Exception e) {
					Thread.sleep(1000);
				}
			}

			this.welcomeSocket = new ServerSocket(commPort);

			while (true) {
				Socket connectionSocket = this.welcomeSocket.accept();

				String ip = connectionSocket.getRemoteSocketAddress()
						.toString().split(":")[0];
				if (ip != this.brokerIP) {
					connectionSocket.close();
				} else {
					new Thread(new BrokerConnectionHandler(connectionSocket,
							this.mgr)).start();
				}
			}
		} catch (Exception e) {

		}
	}
}

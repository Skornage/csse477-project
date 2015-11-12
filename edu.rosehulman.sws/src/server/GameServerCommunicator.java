/*
 * GameServerCommunicator.java
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

import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import protocol.HttpRequest;
import protocol.HttpResponse;
import protocol.HttpResponseFactory;
import protocol.Protocol;
import protocol.ProtocolException;

/**
 * 
 * @author Chandan R. Rupakheti (rupakhcr@clarkson.edu)
 */
public class GameServerCommunicator implements Runnable {

	private int serverHandlerPort;
	private String applicationKey;
	private ServerSocket welcomeSocket;
	private int nextAvailablePort;
	private GameDistributionQueue queue;

	/**
	 * @param serverHandlerPort
	 * @param applicationKey
	 */
	public GameServerCommunicator(int serverHandlerPort, String applicationKey,
			GameDistributionQueue queue) {
		this.serverHandlerPort = serverHandlerPort;
		this.applicationKey = applicationKey;
		this.nextAvailablePort = 95;
		this.queue = queue;
	}

	// start by opening a server socket on port. Wait for requests. When
	// request comes in with proper key, give port number of next available
	// port. Then add them to a shared queue that the broker's server
	// servlets can contact. Do not send them any requests here.
	@Override
	public void run() {

		try {
			this.welcomeSocket = new ServerSocket(this.serverHandlerPort);

			while (true) {
				Socket connectionSocket = this.welcomeSocket.accept();

				new Thread(new GameServerConnectionHandler(connectionSocket,
						applicationKey, this)).start();
			}
		} catch (Exception e) {

		}
	}

	public int addGameServerAddress(String ip, int clientPort) {
		int brokerPort = this.nextAvailablePort;
		this.nextAvailablePort++;
		this.queue.add(new GameServerAddress(ip, brokerPort, clientPort));
		return brokerPort;
	}

}

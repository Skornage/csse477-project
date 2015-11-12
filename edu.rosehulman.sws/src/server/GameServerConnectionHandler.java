/*
 * GameServerConnectionHandler.java
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

import java.net.Socket;

import protocol.HttpRequest;
import protocol.HttpResponse;
import protocol.HttpResponseFactory;

/**
 * 
 * @author Chandan R. Rupakheti (rupakhcr@clarkson.edu)
 */
public class GameServerConnectionHandler extends AbstractConnectionHandler {
	private String applicationKey;
	private GameServerCommunicator gsComm;

	public GameServerConnectionHandler(Socket socket, String applicationKey,
			GameServerCommunicator gameServerCommunicator) {
		super(socket);
		this.gsComm = gameServerCommunicator;
		this.applicationKey = applicationKey;
	}

	@Override
	protected void startServiceTimer() {
		// does nothing.
	}

	@Override
	protected void logServiceTime() {
		// does nothing.
	}

	@Override
	protected HttpResponse handleValidRequest(HttpRequest request) {
		String key = request.getHeaderField("app-key");
		// System.out.println("key from request: " + key);
		if (key.equals(this.applicationKey)) {
			String ip = this.socket.getRemoteSocketAddress().toString()
					.split(":")[0];
			// System.out.println("adding server at " + ip
			// + " to list of game servers!");
			int clientPort = Integer.parseInt(request
					.getHeaderField("client-port"));
			// System.out.println("got the game server's client port:"
			// + clientPort);
			HttpResponse response = HttpResponseFactory.getSingleton()
					.getPreMadeResponse("200");
			int brokerPort = this.gsComm
					.addGameServerAddress(this.socket.getRemoteSocketAddress()
							.toString().split("/")[1].split(":")[0], clientPort);
			response.put("broker-port", brokerPort + "");
			// System.out.println("assigned broker port: " + brokerPort);
			return response;
		} else {
			return null;
		}
	}
}

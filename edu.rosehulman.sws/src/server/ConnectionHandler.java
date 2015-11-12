/*
 * ConnectionHandler.java
 * Oct 7, 2012
 *
 * Simple Web Server (SWS) for CSSE 477
 * 
 * Copyright (C) 2012 Chandan Raj Rupakheti
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
 */

package server;

import java.net.Socket;
import java.util.HashMap;

import protocol.HttpRequest;
import protocol.HttpResponse;

/**
 * This class is responsible for handling a incoming request by creating a
 * {@link HttpRequest} object and sending the appropriate response be creating a
 * {@link HttpResponse} object. It implements {@link Runnable} to be used in
 * multi-threaded environment.
 * 
 * @author Chandan R. Rupakheti (rupakhet@rose-hulman.edu)
 */
public class ConnectionHandler extends AbstractConnectionHandler{
	private Server server;
	private long start;

	public ConnectionHandler(Server server, Socket socket) {
		super(socket);
		this.server = server;
	}

	protected void startServiceTimer() {
		this.start = System.currentTimeMillis();
	}

	protected void logServiceTime() {
		long end = System.currentTimeMillis();
		server.incrementConnections(1);
		this.server.incrementServiceTime(end - this.start);
	}

	protected HttpResponse handleValidRequest(HttpRequest request) {
		String[] URIs = request.getUri().split("/");
		if (server.plugins.containsKey(URIs[1])) {
			HashMap<String, AbstractPluginServlet> servlets = server.plugins
					.get(URIs[1]);
			if (servlets.containsKey(URIs[2])) {
				AbstractPluginServlet servlet = servlets.get(URIs[2]);
				return servlet.HandleRequest(request);
			} else {
				return null;
			}
		} else {
			return null;
		}
	}
}

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

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.HashMap;


import protocol.HttpRequest;
import protocol.HttpResponse;
import protocol.HttpResponseFactory;
import protocol.Protocol;
import protocol.ProtocolException;

/**
 * This class is responsible for handling a incoming request by creating a
 * {@link HttpRequest} object and sending the appropriate response be creating a
 * {@link HttpResponse} object. It implements {@link Runnable} to be used in
 * multi-threaded environment.
 * 
 * @author Chandan R. Rupakheti (rupakhet@rose-hulman.edu)
 */
public class ConnectionHandler implements Runnable {
	private Server server;
	private Socket socket;
	//private HashMap<String, HashMap<String, AbstractPluginServlet>> plugins;

	public ConnectionHandler(Server server, Socket socket) {
		this.server = server;
		this.socket = socket;
		//plugins = new HashMap<String, HashMap<String, AbstractPluginServlet>>();
		// plugins.put("SamplePlugin",
		// new HashMap<String, AbstractPluginServlet>());
		// AbstractPluginServlet sampleGet = new SamplePluginGetServlet();
		// AbstractPluginServlet samplePost = new SamplePluginPostServlet();
		// AbstractPluginServlet samplePut = new SamplePluginPutServlet();
		// AbstractPluginServlet sampleDelete = new SamplePluginDeleteServlet();
		// plugins.get("SamplePlugin").put(sampleGet.getServletURI(),
		// sampleGet);
		// plugins.get("SamplePlugin").put(samplePost.getServletURI(),
		// samplePost);
		// plugins.get("SamplePlugin").put(samplePut.getServletURI(),
		// samplePut);
		// plugins.get("SamplePlugin").put(sampleDelete.getServletURI(),
		// sampleDelete);
		//
		// plugins.put("FilePlugin", new HashMap<String,
		// AbstractPluginServlet>());
		// AbstractPluginServlet get = new FilePluginGetServlet();
		// AbstractPluginServlet post = new FilePluginPostServlet();
		// AbstractPluginServlet put = new FilePluginPutServlet();
		// AbstractPluginServlet delete = new FilePluginDeleteServlet();
		// plugins.get("FilePlugin").put(get.getServletURI(), get);
		// plugins.get("FilePlugin").put(post.getServletURI(), post);
		// plugins.get("FilePlugin").put(put.getServletURI(), put);
		// plugins.get("FilePlugin").put(delete.getServletURI(), delete);

	}

	/**
	 * @return the socket
	 */
	public Socket getSocket() {
		return socket;
	}

	/**
	 * The entry point for connection handler. It first parses incoming request
	 * and creates a {@link HttpRequest} object, then it creates an appropriate
	 * {@link HttpResponse} object and sends the response back to the client
	 * (web browser).
	 */
	public void run() {
		long start = System.currentTimeMillis();

		InputStream inStream = null;
		OutputStream outStream = null;

		try {
			inStream = this.socket.getInputStream();
			outStream = this.socket.getOutputStream();
		} catch (Exception e) {
			e.printStackTrace();

			server.incrementConnections(1);
			long end = System.currentTimeMillis();
			this.server.incrementServiceTime(end - start);
			return;
		}

		HttpRequest request = null;
		HttpResponse response = null;
		try {
			request = HttpRequest.read(inStream);
		} catch (ProtocolException pe) {
			int status = pe.getStatus();
			if (status == Protocol.BAD_REQUEST_CODE) {
				response = HttpResponseFactory.getSingleton()
						.getPreMadeResponse("400");
			} else {
				response = HttpResponseFactory.getSingleton()
						.getPreMadeResponse("505");
			}
		} catch (Exception e) {
			e.printStackTrace();
			response = HttpResponseFactory.getSingleton().getPreMadeResponse(
					"400");
		}

		if (response != null) {
			try {
				response.write(outStream);
			} catch (Exception e) {
				e.printStackTrace();
			}
			server.incrementConnections(1);
			long end = System.currentTimeMillis();
			this.server.incrementServiceTime(end - start);
			return;
		}
		try {
			if (!request.getVersion().equalsIgnoreCase(Protocol.VERSION)) {
				response = HttpResponseFactory.getSingleton()
						.getPreMadeResponse("505");

			} else {
				String[] URIs = request.getUri().split("/");
				if (server.plugins.containsKey(URIs[1])) {
					HashMap<String, AbstractPluginServlet> servlets = server.plugins
							.get(URIs[1]);
					if (servlets.containsKey(URIs[2])) {
						AbstractPluginServlet servlet = servlets.get(URIs[2]);
						response = servlet.HandleRequest(request);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (response == null) {
			response = HttpResponseFactory.getSingleton().getPreMadeResponse(
					"400");
		}

		try {
			response.write(outStream);
			socket.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		server.incrementConnections(1);
		long end = System.currentTimeMillis();
		this.server.incrementServiceTime(end - start);
	}
}

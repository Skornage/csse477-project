/*
 * AbstractConnectionHandler.java
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

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.Date;

import protocol.HttpRequest;
import protocol.HttpResponse;
import protocol.HttpResponseFactory;
import protocol.Protocol;
import protocol.ProtocolException;

/**
 * 
 * @author Chandan R. Rupakheti (rupakhcr@clarkson.edu)
 */
public abstract class AbstractConnectionHandler implements Runnable{
	protected Socket socket;

	public AbstractConnectionHandler(Socket socket) {
		this.socket = socket;
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
		startServiceTimer();

		InputStream inStream = null;
		OutputStream outStream = null;

		try {
			inStream = this.socket.getInputStream();
			outStream = this.socket.getOutputStream();
		} catch (Exception e) {
			e.printStackTrace();

			logServiceTime();
			return;
		}

		HttpResponse response = processRequest(inStream);

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

		logServiceTime();
	}

	private HttpResponse processRequest(InputStream inStream) {
		HttpRequest request = null;
		try {
			request = HttpRequest.read(inStream);
			File auditLogFile = new File("log/audit.txt");
			String content = new Date() + "\n" + request.toString() + "\n";
			Files.write(auditLogFile.toPath(), content.getBytes(),
					StandardOpenOption.CREATE, StandardOpenOption.APPEND);

		} catch (ProtocolException pe) {
			int status = pe.getStatus();
			if (status == Protocol.BAD_REQUEST_CODE) {
				return null;
			} else {
				return HttpResponseFactory.getSingleton().getPreMadeResponse(
						"505");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		try {
			if (!request.getVersion().equalsIgnoreCase(Protocol.VERSION)) {
				return HttpResponseFactory.getSingleton().getPreMadeResponse(
						"505");

			} else {
				return handleValidRequest(request);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return HttpResponseFactory.getSingleton().getPreMadeResponse("505");
		}
	}

	protected abstract void startServiceTimer();

	protected abstract void logServiceTime();

	protected abstract HttpResponse handleValidRequest(HttpRequest request);
}

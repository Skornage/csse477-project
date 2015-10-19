/*
 * PutRequestHandler.java
 * Oct 17, 2015
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
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;

import protocol.HttpRequest;
import protocol.HttpResponse;
import protocol.HttpResponseFactory;
import protocol.Protocol;

/**
 * 
 * @author Chandan R. Rupakheti (rupakhcr@clarkson.edu)
 */
public class PutRequestHandler implements IRequestHandler {

	@Override
	public HttpResponse handleRequest(HttpRequest request, String rootDirectory) {
		File file = new File(rootDirectory + request.getUri());
		if (file.exists()) {
			if (file.isDirectory()) {
				return HttpResponseFactory.getSingleton().getResponse("404", null, Protocol.CLOSE);
			}
		}
		try {
			System.out.println("TESTING: " + file.toPath());
			System.out.println("BODY: " + new String(request.getBody()));
			Files.write(file.toPath(), new String(request.getBody()).getBytes());
			return HttpResponseFactory.getSingleton().getResponse("200", file.getPath(), Protocol.CLOSE);
		} catch (IOException e) {
			return HttpResponseFactory.getSingleton().getResponse("500", null, Protocol.CLOSE);
		}
	}

}

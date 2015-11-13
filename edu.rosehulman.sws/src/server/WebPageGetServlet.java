/*
 * FilePluginGetServlet.java
 * Oct 25, 2015
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
import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.Date;

import protocol.HttpRequest;
import protocol.HttpResponse;
import protocol.HttpResponseFactory;
import protocol.Protocol;

/**
 * 
 * @author Chandan R. Rupakheti (rupakhcr@clarkson.edu)
 */
public class WebPageGetServlet extends AbstractWebPageServlet {

	@Override
	public String getRequestType() {
		return Protocol.GET;
	}

	@Override
	public String getServletURI() {
		return "lobby";
	}

	@Override
	protected HttpResponse handleFileNotExists(File file, HttpRequest request) {
		return HttpResponseFactory.getSingleton().getPreMadeResponse("404");
	}

	@Override
	protected HttpResponse handleFileExists(File file, HttpRequest request) {
		String uri = request.getUri();
		HttpResponse response = HttpResponseFactory.getSingleton().getPreMadeResponse("200");
		
		response.setBody(HttpResponseFactory.getFileContentsForResponseBody(file));

		long timeSinceEpoch = file.lastModified();
		Date modifiedTime = new Date(timeSinceEpoch);
		response.put(Protocol.LAST_MODIFIED, modifiedTime.toString());

		long length = file.length();
		response.put(Protocol.CONTENT_LENGTH, length + "");

		FileNameMap fileNameMap = URLConnection.getFileNameMap();
		String mime = fileNameMap.getContentTypeFor(file.getName());
		if (mime != null) {
			response.put(Protocol.CONTENT_TYPE, mime);
		}

		return response;
	}
}

/*
 * AbstractFilePluginServletf.java
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
public abstract class AbstractFilePluginServlet extends AbstractPluginServlet {

	@Override
	public String getPluginURI() {
		return "FilePlugin";
	}

	@Override
	public HttpResponse HandleRequest(HttpRequest request) {
		String uri = null;
		try {
			uri = request.getUri().split("/")[3];
		} catch (Exception e) {
			return HttpResponseFactory.getSingleton().getPreMadeResponse("404");
		}
		File file = new File(Server.getRootDirectory() + System.getProperty("file.separator") + uri);
		if (file.exists()) {
			if (file.isDirectory()) {
				return HttpResponseFactory.getSingleton().getPreMadeResponse(
						"404");
			} else {
				return handleFileExists(file, request);
			}

		} else {
			return handleFileNotExists(file, request);
		}
	}

	protected abstract HttpResponse handleFileNotExists(File file,
			HttpRequest request);

	protected abstract HttpResponse handleFileExists(File file,
			HttpRequest request);

	protected static HttpResponse appendFileToResponse(HttpResponse response,
			File file) {
		response.setBody(HttpResponseFactory
				.getFileContentsForResponseBody(file));

		long timeSinceEpoch = file.lastModified();
		Date modifiedTime = new Date(timeSinceEpoch);
		response.put(Protocol.LAST_MODIFIED, modifiedTime.toString());

		long length = file.length();
		response.put(Protocol.CONTENT_LENGTH, length + "");

		// Lets get MIME type for the file
		FileNameMap fileNameMap = URLConnection.getFileNameMap();
		String mime = fileNameMap.getContentTypeFor(file.getName());
		// The fileNameMap cannot find mime type for all of the documents,
		// e.g.
		// doc, odt, etc.
		// So we will not add this field if we cannot figure out what a mime
		// type is for the file.
		// Let browser do this job by itself.
		if (mime != null) {
			response.put(Protocol.CONTENT_TYPE, mime);
		}

		return response;
	}

}

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

import protocol.HttpRequest;
import protocol.HttpResponse;
import protocol.HttpResponseFactory;

/**
 * 
 * @author Chandan R. Rupakheti (rupakhcr@clarkson.edu)
 */
public abstract class AbstractWebPageServlet extends AbstractPluginServlet {

	private Server server;

	@Override
	public String getPluginURI() {
		return "playhangman";
	}

	@Override
	public HttpResponse HandleRequest(HttpRequest request) {
		String uri = null;
		try {
			uri = request.getUri().split("/")[3];
		} catch (Exception e) {
			return HttpResponseFactory.getSingleton().getPreMadeResponse("404");
		}
		File file = new File(this.server.getRootDirectory()
				+ System.getProperty("file.separator") + uri);
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

	@Override
	public void setServer(Server server) {
		this.server = server;
	}

	protected abstract HttpResponse handleFileNotExists(File file,
			HttpRequest request);

	protected abstract HttpResponse handleFileExists(File file,
			HttpRequest request);

}

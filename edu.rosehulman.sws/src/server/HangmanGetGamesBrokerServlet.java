/*
 * HangmanGetGamesBrokerHandler.java
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

import protocol.HttpRequest;
import protocol.HttpResponse;
import protocol.HttpResponseFactory;
import protocol.Protocol;

/**
 * 
 * @author Chandan R. Rupakheti (rupakhcr@clarkson.edu)
 */
public class HangmanGetGamesBrokerServlet extends AbstractHangmanBrokerServlet {

	public HangmanGetGamesBrokerServlet(Broker broker) {
		super(broker);
	}

	@Override
	public String getRequestType() {
		return Protocol.GET;
	}

	@Override
	public HttpResponse HandleRequest(HttpRequest request) {
		HttpResponse response = HttpResponseFactory.getPreMadeResponse("200");
		StringBuilder sb = new StringBuilder();
		sb.append('[');
		for (HangmanGame game : this.mgr.getAllGames()) {
			sb.append(',');
			sb.append(game.toString());
		}
		sb.append(']');
		if (sb.length() > 1) {
			sb.deleteCharAt(1);
		}
		response.setBody(sb.toString());
		return response;
	}

	@Override
	public String getServletURI() {
		return "";
	}
}

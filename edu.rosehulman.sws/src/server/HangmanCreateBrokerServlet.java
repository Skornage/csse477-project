/*
 * HangmanCreateBrokerServlet.java
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

import java.util.Map;

import protocol.HttpRequest;
import protocol.HttpResponse;
import protocol.HttpResponseFactory;
import protocol.Protocol;

/**
 * 
 * @author Chandan R. Rupakheti (rupakhcr@clarkson.edu)
 */
public class HangmanCreateBrokerServlet extends AbstractHangmanBrokerServlet {

	public HangmanCreateBrokerServlet(Broker broker) {
		super(broker);
	}

	@Override
	public String getRequestType() {
		return Protocol.POST;
	}
	
	@Override
	public String getServletURI() {
		return "create";
	}

	@Override
	public HttpResponse HandleRequest(HttpRequest request) {
		Map<String, String> map = request.getHeader();
		String name = map.get("name");
		String postedBy = map.get("posted by");
		String word = map.get("word");
		
		this.mgr.addGame(new BrokerHangmanGame(name, word, postedBy));
		
		HttpResponse response = HttpResponseFactory.getPreMadeResponse("200");
		return response;
	}


}
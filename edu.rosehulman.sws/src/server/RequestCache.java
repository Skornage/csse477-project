/*
 * RequestChache.java
 * Oct 29, 2015
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

import java.util.HashMap;
import java.util.Date;

/**
 * 
 * @author Chandan R. Rupakheti (rupakhcr@clarkson.edu)
 */
public class RequestCache {
	
	private HashMap<String, Integer> numberOfRequests = new HashMap<String, Integer>();
	private HashMap<String, String> cachedRequest = new HashMap<String, String>();
	private HashMap<String, Date> lastModified = new HashMap<String, Date>();
	
	public RequestCache() {}
	
	public boolean contains(String requestURI) {
		return cachedRequest.containsKey(requestURI);
	}
	
	public void addRequest(String requestURI, String content) {
		numberOfRequests.put(requestURI, 1);
		cachedRequest.put(requestURI, content);
		Date d = new Date();
		lastModified.put(requestURI, d);
	}
	
	public void updateRequest(String requestURI, String content) {
		this.incrementNumberRequests(requestURI);
		this.setLastModified(requestURI, new Date());
		this.setCachedRequest(requestURI, content);
	}
	
	public int getNumberRequests(String requestURI) {
		return numberOfRequests.get(requestURI);
	}
	
	public String getCachedRequest(String requestURI) {
		return cachedRequest.get(requestURI);
	}
	
	public Date getLastModified(String requestURI) {
		return lastModified.get(requestURI);
	}
	
	public void incrementNumberRequests(String requestURI) {
		int numRequests = numberOfRequests.get(requestURI);
		numberOfRequests.put(requestURI, numRequests++);
	}
	
	public void setNumberRequests(String requestURI, int num) {
		numberOfRequests.put(requestURI, num);
	}
	
	public void setCachedRequest(String requestURI, String content) {
		cachedRequest.put(requestURI, content);
	}
	
	public void setLastModified(String requestURI, Date date) {
		lastModified.put(requestURI, date);
	}
}

/*
 * CustomIPOptimistPrimeBot.java
 * Nov 2, 2015
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

package test;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringBufferInputStream;

import protocol.HttpResponse;

/**
 * 
 * @author Chandan R. Rupakheti (rupakhcr@clarkson.edu)
 */
public class CustomIPOptimistPrimeBot extends OptimistPrimeBot {
	private String ip;
	private MockServerSocket mockServerSocket;

	public CustomIPOptimistPrimeBot(int requestsToMakeCount, int sleepTime,
			String ip, MockServerSocket mock) {
		super(requestsToMakeCount, sleepTime);
		this.ip = ip;
		this.mockServerSocket = mock;
	}

	@Override
	protected HttpResponse makeRequest(String request) {
		MockSocket socket = new MockSocket(new MockSocketAddress(this.ip), request);
		try {
			this.mockServerSocket.addMockSocket(socket);
		} catch (final Exception e) {
			return null;
		}
		
		try {
			
			ByteArrayOutputStream baos = (ByteArrayOutputStream) socket.getOutputStream();
			//System.out.println(socket.isClosed());
			while(!socket.isClosed()){
				Thread.sleep(10);
			}
			//System.out.println("socket closed");
			String resp = baos.toString();
			//System.out.println("resp:"+resp);
			
			InputStream in = new StringBufferInputStream(resp);
			return HttpResponse.read(in);
		} catch (final Exception e) {
			return null;
		}
	}
}

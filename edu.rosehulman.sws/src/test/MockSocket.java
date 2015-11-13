/*
 * MockSocket.java
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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketAddress;

/**
 * 
 * @author Chandan R. Rupakheti (rupakhcr@clarkson.edu)
 */
public class MockSocket extends Socket {
	private MockSocketAddress mockSocketAddress;
	private String request;
	private ByteArrayOutputStream outputStream;
	private ByteArrayInputStream inputStream;
	private boolean closed;

	public MockSocket(MockSocketAddress mockSocketAddress, String request) {
		this.mockSocketAddress = mockSocketAddress;
		this.request = request;
		this.inputStream = new ByteArrayInputStream(this.request.getBytes());
		this.outputStream = new ByteArrayOutputStream();
		this.closed = false;
	}

	@Override
	public SocketAddress getRemoteSocketAddress() {
		return this.mockSocketAddress;
	}

	@Override
	public InputStream getInputStream() {
		return this.inputStream;
	}

	@Override
	public OutputStream getOutputStream() {
		return this.outputStream;
	}

	@Override
	public void close() throws IOException {
		// System.out.println("hey");
		this.closed = true;
	}

	public boolean isClosed() {
		return this.closed;
	}

}

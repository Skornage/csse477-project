/*
 * ServerTest.java
 * Oct 18, 2015
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

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import protocol.HttpResponse;

/**
 * 
 * @author Chandan R. Rupakheti (rupakhcr@clarkson.edu)
 */
public class ServerTest {

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGet() {
		// String expectedResponse =
		// "----------- Header ----------------\nHTTP/1.1 200 OK\nServer: SimpleWebServer(SWS)/1.0.0 (Windows 7/6.1/amd64)\nConnection: Close\nLast-Modified: Tue Nov 05 22:00:22 EST 2013\nContent-Length: 107\nDate: Sun Oct 18 21:40:53 EDT 2015\nProvider: Chandan R. Rupakheti\nContent-Type: text/html\n\n\n----------------------------------\n------------- Body ---------------\n<html>\n<head>\n	<title>Test Page</title>\n</head>\n<body>\n	<p>Test Page Successful!</p>\n</body>\n</html>\n----------------------------------\n";
		String request = "GET /index.html HTTP/1.1\naccept-language: en-US,en;q=0.8\nhost: localhost\nconnection: Keep-Alive\nuser-agent: HttpTestClient/1.0\naccept: text/html,text/plain,application/xml,application/json\n\n";

		HttpResponse actualResponse = makeRequest(request);
		String expectedBody = "<html><head>	<title>Test Page</title></head><body>	<p>Test Page Successful!</p></body></html>";

		assertEquals(expectedBody, actualResponse.getBody().replace("\n", "").replace("\r", ""));
		assertEquals("HTTP/1.1", actualResponse.getVersion());
		assertEquals("OK", actualResponse.getPhrase());
		assertEquals(200, actualResponse.getStatus());

		// String host = "localhost";
		// int port = 8080;
		// Socket socket = null;
		// try {
		// socket = new Socket(host, port);
		// } catch (IOException e1) {
		// fail("socket could not connect");
		// e1.printStackTrace();
		// }
		// try {
		// if (!socket.isConnected())
		// throw new Exception("Socket is not connected!");
		//
		// OutputStream out = socket.getOutputStream();
		// out.write(request.getBytes());
		// out.flush();
		// } catch (final Exception e) {
		// fail("Error with socket connection");
		// }
		// try {
		// InputStream in = socket.getInputStream();
		// // Read the response
		// HttpResponse actualResponse = HttpResponse.read(in);
		// System.out.print("!" + expectedResponse + "!" + actualResponse
		// + "!");
		// assertEquals(expectedResponse, actualResponse.toString());
		//
		// } catch (final Exception e) {
		// fail("Error with response");
		// }
		// fail("no response received");
	}
	
	@Test
	public void testPutNew() {
		
		String put = "PUT /test.html HTTP/1.1\naccept-language: en-US,en;q=0.8\nContent-Length: 4\nhost: localhost\nconnection: Keep-Alive\nuser-agent: HttpTestClient/1.0\naccept: text/html,text/plain,application/xml,application/json\n\ntest";

		HttpResponse actualResponse = makeRequest(put);
		
		String expectedBody = "test";

		assertEquals(expectedBody, actualResponse.getBody().replace("\n", "").replace("\r", ""));
		assertEquals("HTTP/1.1", actualResponse.getVersion());
		assertEquals("OK", actualResponse.getPhrase());
		assertEquals(200, actualResponse.getStatus());
		
		String delete = "DELETE /test.html HTTP/1.1\naccept-language: en-US,en;q=0.8\nhost: localhost\nconnection: Keep-Alive\nuser-agent: HttpTestClient/1.0\naccept: text/html,text/plain,application/xml,application/json\n\n";
		makeRequest(delete);
	}
	
	@Test
	public void testPutOverwrite() {
		String put = "PUT /test.html HTTP/1.1\naccept-language: en-US,en;q=0.8\nContent-Length: 4\nhost: localhost\nconnection: Keep-Alive\nuser-agent: HttpTestClient/1.0\naccept: text/html,text/plain,application/xml,application/json\n\ntest";

		makeRequest(put);
		
		String put2 = "PUT /test.html HTTP/1.1\naccept-language: en-US,en;q=0.8\nContent-Length: 4\nhost: localhost\nconnection: Keep-Alive\nuser-agent: HttpTestClient/1.0\naccept: text/html,text/plain,application/xml,application/json\n\ntest";

		HttpResponse actualResponse = makeRequest(put2);
		String expectedBody = "test";

		assertEquals(expectedBody, actualResponse.getBody().replace("\n", "").replace("\r", ""));
		assertEquals("HTTP/1.1", actualResponse.getVersion());
		assertEquals("OK", actualResponse.getPhrase());
		assertEquals(200, actualResponse.getStatus());
		
		String delete = "DELETE /test.html HTTP/1.1\naccept-language: en-US,en;q=0.8\nhost: localhost\nconnection: Keep-Alive\nuser-agent: HttpTestClient/1.0\naccept: text/html,text/plain,application/xml,application/json\n\n";
		makeRequest(delete);
	}
	
	@Test
	public void testPostNew() {
		
		String put = "POST /test2.html HTTP/1.1\naccept-language: en-US,en;q=0.8\nContent-Length: 4\nhost: localhost\nconnection: Keep-Alive\nuser-agent: HttpTestClient/1.0\naccept: text/html,text/plain,application/xml,application/json\n\ntest";

		HttpResponse actualResponse = makeRequest(put);
		
		String expectedBody = "test";

		assertEquals(expectedBody, actualResponse.getBody().replace("\n", "").replace("\r", ""));
		assertEquals("HTTP/1.1", actualResponse.getVersion());
		assertEquals("OK", actualResponse.getPhrase());
		assertEquals(200, actualResponse.getStatus());
		
		String delete = "DELETE /test2.html HTTP/1.1\naccept-language: en-US,en;q=0.8\nhost: localhost\nconnection: Keep-Alive\nuser-agent: HttpTestClient/1.0\naccept: text/html,text/plain,application/xml,application/json\n\n";
		HttpResponse res = makeRequest(delete);
		assertEquals("OK", res.getPhrase());
	}
	
	@Test
	public void testPostAppend() {
		String put = "POST /test2.html HTTP/1.1\naccept-language: en-US,en;q=0.8\nContent-Length: 4\nhost: localhost\nconnection: Keep-Alive\nuser-agent: HttpTestClient/1.0\naccept: text/html,text/plain,application/xml,application/json\n\ntest";

		makeRequest(put);
		
		String put2 = "POST /test2.html HTTP/1.1\naccept-language: en-US,en;q=0.8\nContent-Length: 4\nhost: localhost\nconnection: Keep-Alive\nuser-agent: HttpTestClient/1.0\naccept: text/html,text/plain,application/xml,application/json\n\ntest";

		HttpResponse actualResponse = makeRequest(put2);
		
		String expectedBody = "testtest";

		assertEquals(expectedBody, actualResponse.getBody().replace("\n", "").replace("\r", ""));
		assertEquals("HTTP/1.1", actualResponse.getVersion());
		assertEquals("OK", actualResponse.getPhrase());
		assertEquals(200, actualResponse.getStatus());
		
		String delete = "DELETE /test2.html HTTP/1.1\naccept-language: en-US,en;q=0.8\nhost: localhost\nconnection: Keep-Alive\nuser-agent: HttpTestClient/1.0\naccept: text/html,text/plain,application/xml,application/json\n\n";
		makeRequest(delete);
	}
	
	@Test
	public void testDelete() {
		
		String put = "PUT /test.html HTTP/1.1\naccept-language: en-US,en;q=0.8\nhost: localhost\nconnection: Keep-Alive\nuser-agent: HttpTestClient/1.0\naccept: text/html,text/plain,application/xml,application/json\n\n";
		makeRequest(put);
		String request = "DELETE /test.html HTTP/1.1\naccept-language: en-US,en;q=0.8\nhost: localhost\nconnection: Keep-Alive\nuser-agent: HttpTestClient/1.0\naccept: text/html,text/plain,application/xml,application/json\n\n";

		HttpResponse actualResponse = makeRequest(request);

		assertEquals("HTTP/1.1", actualResponse.getVersion());
		assertEquals("OK", actualResponse.getPhrase());
		assertEquals(200, actualResponse.getStatus());
	}
	
	@Test
	public void testDelete404() {
		String request = "DELETE /test.html HTTP/1.1\naccept-language: en-US,en;q=0.8\nhost: localhost\nconnection: Keep-Alive\nuser-agent: HttpTestClient/1.0\naccept: text/html,text/plain,application/xml,application/json\n\n";

		HttpResponse actualResponse = makeRequest(request);

		assertEquals("HTTP/1.1", actualResponse.getVersion());
		assertEquals("Not", actualResponse.getPhrase());
		assertEquals(404, actualResponse.getStatus());
	}

	public HttpResponse makeRequest(String request) {
		String host = "localhost";
		int port = 8080;
		Socket socket = null;
		try {
			socket = new Socket(host, port);
		} catch (IOException e1) {
			fail("socket could not connect");
			e1.printStackTrace();
		}
		try {
			if (!socket.isConnected())
				throw new Exception("Socket is not connected!");

			OutputStream out = socket.getOutputStream();
			out.write(request.getBytes());
			out.flush();
		} catch (final Exception e) {
			fail("Error with socket connection");
		}
		try {
			InputStream in = socket.getInputStream();
			return HttpResponse.read(in);

		} catch (final Exception e) {
			fail("Error with response");
		}
		fail("no response received");

		return null;
	}
}

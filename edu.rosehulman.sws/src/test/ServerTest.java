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

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import protocol.HttpResponse;
import server.Server;

/**
 * 
 * @author Chandan R. Rupakheti (rupakhcr@clarkson.edu)
 */
public class ServerTest {
	static Server server;

	@BeforeClass
	public static void setUpBeforeClass() {

		String rootDirectoryPath = System.getProperty("user.dir")
				+ System.getProperty("file.separator") + "web";
		server = new Server(rootDirectoryPath, 8080, new MockWebServer());
		Thread t = new Thread(server);
		t.start();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		server.stop();
	}

	@Test
	public void testFileGet() {
		String request = "GET /FilePlugin/FileGetServlet/index.html HTTP/1.1\naccept-language: en-US,en;q=0.8\nhost: localhost\nconnection: Keep-Alive\nuser-agent: HttpTestClient/1.0\naccept: text/html,text/plain,application/xml,application/json\n\n";

		HttpResponse actualResponse = makeRequest(request);
		String expectedBody = "<html><head>	<title>Test Page</title></head><body>	<p>Test Page Successful!</p></body></html>";

		assertEquals(expectedBody, actualResponse.getBody().replace("\n", "")
				.replace("\r", ""));
		assertEquals("HTTP/1.1", actualResponse.getVersion());
		assertEquals("OK", actualResponse.getPhrase());
		assertEquals(200, actualResponse.getStatus());
	}

	@Test
	public void testFilePutNew() {

		String put = "PUT /FilePlugin/FilePutServlet/test.html HTTP/1.1\naccept-language: en-US,en;q=0.8\nContent-Length: 4\nhost: localhost\nconnection: Keep-Alive\nuser-agent: HttpTestClient/1.0\naccept: text/html,text/plain,application/xml,application/json\n\ntest";

		HttpResponse actualResponse = makeRequest(put);

		String expectedBody = "test";

		assertEquals(expectedBody, actualResponse.getBody().replace("\n", "")
				.replace("\r", ""));
		assertEquals("HTTP/1.1", actualResponse.getVersion());
		assertEquals("OK", actualResponse.getPhrase());
		assertEquals(200, actualResponse.getStatus());

		String delete = "DELETE /FilePlugin/FileDeleteServlet/test.html HTTP/1.1\naccept-language: en-US,en;q=0.8\nhost: localhost\nconnection: Keep-Alive\nuser-agent: HttpTestClient/1.0\naccept: text/html,text/plain,application/xml,application/json\n\n";
		makeRequest(delete);
	}

	@Test
	public void testFilePutOverwrite() {
		String put = "PUT /FilePlugin/FilePutServlet/test.html HTTP/1.1\naccept-language: en-US,en;q=0.8\nContent-Length: 4\nhost: localhost\nconnection: Keep-Alive\nuser-agent: HttpTestClient/1.0\naccept: text/html,text/plain,application/xml,application/json\n\ntest";

		makeRequest(put);

		String put2 = "PUT /FilePlugin/FilePutServlet/test.html HTTP/1.1\naccept-language: en-US,en;q=0.8\nContent-Length: 4\nhost: localhost\nconnection: Keep-Alive\nuser-agent: HttpTestClient/1.0\naccept: text/html,text/plain,application/xml,application/json\n\ntest";

		HttpResponse actualResponse = makeRequest(put2);
		String expectedBody = "test";

		assertEquals(expectedBody, actualResponse.getBody().replace("\n", "")
				.replace("\r", ""));
		assertEquals("HTTP/1.1", actualResponse.getVersion());
		assertEquals("OK", actualResponse.getPhrase());
		assertEquals(200, actualResponse.getStatus());

		String delete = "DELETE /FilePlugin/FileDeleteServlet/test.html HTTP/1.1\naccept-language: en-US,en;q=0.8\nhost: localhost\nconnection: Keep-Alive\nuser-agent: HttpTestClient/1.0\naccept: text/html,text/plain,application/xml,application/json\n\n";
		makeRequest(delete);
	}

	@Test
	public void testFilePostNew() {

		String put = "POST /FilePlugin/FilePostServlet/test2.html HTTP/1.1\naccept-language: en-US,en;q=0.8\nContent-Length: 4\nhost: localhost\nconnection: Keep-Alive\nuser-agent: HttpTestClient/1.0\naccept: text/html,text/plain,application/xml,application/json\n\ntest";

		HttpResponse actualResponse = makeRequest(put);

		String expectedBody = "test";

		assertEquals(expectedBody, actualResponse.getBody().replace("\n", "")
				.replace("\r", ""));
		assertEquals("HTTP/1.1", actualResponse.getVersion());
		assertEquals("OK", actualResponse.getPhrase());
		assertEquals(200, actualResponse.getStatus());

		String delete = "DELETE /FilePlugin/FileDeleteServlet/test2.html HTTP/1.1\naccept-language: en-US,en;q=0.8\nhost: localhost\nconnection: Keep-Alive\nuser-agent: HttpTestClient/1.0\naccept: text/html,text/plain,application/xml,application/json\n\n";
		HttpResponse res = makeRequest(delete);
		assertEquals("OK", res.getPhrase());
	}

	@Test
	public void testFilePostAppend() {
		String put = "POST /FilePlugin/FilePostServlet/test2.html HTTP/1.1\naccept-language: en-US,en;q=0.8\nContent-Length: 4\nhost: localhost\nconnection: Keep-Alive\nuser-agent: HttpTestClient/1.0\naccept: text/html,text/plain,application/xml,application/json\n\ntest";

		makeRequest(put);

		String put2 = "POST /FilePlugin/FilePostServlet/test2.html HTTP/1.1\naccept-language: en-US,en;q=0.8\nContent-Length: 4\nhost: localhost\nconnection: Keep-Alive\nuser-agent: HttpTestClient/1.0\naccept: text/html,text/plain,application/xml,application/json\n\ntest";

		HttpResponse actualResponse = makeRequest(put2);

		String expectedBody = "testtest";

		assertEquals(expectedBody, actualResponse.getBody().replace("\n", "")
				.replace("\r", ""));
		assertEquals("HTTP/1.1", actualResponse.getVersion());
		assertEquals("OK", actualResponse.getPhrase());
		assertEquals(200, actualResponse.getStatus());

		String delete = "DELETE /FilePlugin/FileDeleteServlet/test2.html HTTP/1.1\naccept-language: en-US,en;q=0.8\nhost: localhost\nconnection: Keep-Alive\nuser-agent: HttpTestClient/1.0\naccept: text/html,text/plain,application/xml,application/json\n\n";
		makeRequest(delete);
	}

	@Test
	public void testFileDelete() {

		String put = "PUT /FilePlugin/FilePutServlet/test.html HTTP/1.1\naccept-language: en-US,en;q=0.8\nhost: localhost\nconnection: Keep-Alive\nuser-agent: HttpTestClient/1.0\naccept: text/html,text/plain,application/xml,application/json\n\n";
		makeRequest(put);
		String request = "DELETE /FilePlugin/FileDeleteServlet/test.html HTTP/1.1\naccept-language: en-US,en;q=0.8\nhost: localhost\nconnection: Keep-Alive\nuser-agent: HttpTestClient/1.0\naccept: text/html,text/plain,application/xml,application/json\n\n";

		HttpResponse actualResponse = makeRequest(request);

		assertEquals("HTTP/1.1", actualResponse.getVersion());
		assertEquals("OK", actualResponse.getPhrase());
		assertEquals(200, actualResponse.getStatus());
	}

	@Test
	public void testFileDelete404() {
		String request = "DELETE /FilePlugin/FileDeleteServlet/test.html HTTP/1.1\naccept-language: en-US,en;q=0.8\nhost: localhost\nconnection: Keep-Alive\nuser-agent: HttpTestClient/1.0\naccept: text/html,text/plain,application/xml,application/json\n\n";

		HttpResponse actualResponse = makeRequest(request);

		assertEquals("HTTP/1.1", actualResponse.getVersion());
		assertEquals("Not", actualResponse.getPhrase());
		assertEquals(404, actualResponse.getStatus());
	}

	@Test
	public void testSampleGet() {
		String request = "GET /SamplePlugin/SampleGetServlet HTTP/1.1\naccept-language: en-US,en;q=0.8\nhost: localhost\nconnection: Keep-Alive\nuser-agent: HttpTestClient/1.0\naccept: text/html,text/plain,application/xml,application/json\n\n";

		HttpResponse actualResponse = makeRequest(request);
		String expectedBody = "I think we have GET working";
		assertEquals(expectedBody, actualResponse.getBody());
	}

	@Test
	public void testSamplePost() {
		String request = "POST /SamplePlugin/SamplePostServlet/ HTTP/1.1\naccept-language: en-US,en;q=0.8\nhost: localhost\nconnection: Keep-Alive\nuser-agent: HttpTestClient/1.0\naccept: text/html,text/plain,application/xml,application/json\n\n";

		HttpResponse actualResponse = makeRequest(request);
		String expectedBody = "I think we have POST working";

		assertEquals(expectedBody, actualResponse.getBody());
	}

	@Test
	public void testSamplePut() {
		String request = "PUT /SamplePlugin/SamplePutServlet/ HTTP/1.1\naccept-language: en-US,en;q=0.8\nhost: localhost\nconnection: Keep-Alive\nuser-agent: HttpTestClient/1.0\naccept: text/html,text/plain,application/xml,application/json\n\n";

		HttpResponse actualResponse = makeRequest(request);
		String expectedBody = "I think we have PUT working";

		assertEquals(expectedBody, actualResponse.getBody());
	}

	@Test
	public void testSampleDelete() {
		String request = "DELETE /SamplePlugin/SampleDeleteServlet/ HTTP/1.1\naccept-language: en-US,en;q=0.8\nhost: localhost\nconnection: Keep-Alive\nuser-agent: HttpTestClient/1.0\naccept: text/html,text/plain,application/xml,application/json\n\n";

		HttpResponse actualResponse = makeRequest(request);
		String expectedBody = "I think we have DELETE working";

		assertEquals(expectedBody, actualResponse.getBody());
	}

	@SuppressWarnings("resource")
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
	
	@Test
	public void testRequestFromCache() {
		String request = "GET /FilePlugin/FileGetServlet/index.html HTTP/1.1\naccept-language: en-US,en;q=0.8\nhost: localhost\nconnection: Keep-Alive\nuser-agent: HttpTestClient/1.0\naccept: text/html,text/plain,application/xml,application/json\n\n";

		HttpResponse actualResponse = makeRequest(request);
		String expectedBody = "<html><head>	<title>Test Page</title></head><body>	<p>Test Page Successful!</p></body></html>";

		assertEquals(expectedBody, actualResponse.getBody().replace("\n", "")
				.replace("\r", ""));
		assertEquals("HTTP/1.1", actualResponse.getVersion());
		assertEquals("OK", actualResponse.getPhrase());
		assertEquals(200, actualResponse.getStatus());
		
		System.out.println("Request from cache: ");
		makeRequest(request);
	}
}

/*
 * HttpResponse.java
 * Oct 7, 2012
 *
 * Simple Web Server (SWS) for CSSE 477
 * 
 * Copyright (C) 2012 Chandan Raj Rupakheti
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
 */

package protocol;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents a response object for HTTP.
 * 
 * @author Chandan R. Rupakheti (rupakhet@rose-hulman.edu)
 */
public class HttpResponse {
	private String version;
	private int status;
	private String phrase;
	private Map<String, String> header;
	private String body;

	public HttpResponse(String version, int status, String phrase,
			Map<String, String> header, String body) {
		this.version = version;
		this.status = status;
		this.phrase = phrase;
		this.header = header;
		this.setBody(body);
	}

	public HttpResponse(String version, int status, String phrase, String body) {
		this.version = version;
		this.status = status;
		this.phrase = phrase;
		this.header = new HashMap<String, String>();
		this.setBody(body);
	}

	/**
	 * Gets the version of the HTTP.
	 * 
	 * @return the version
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * Gets the status code of the response object.
	 * 
	 * @return the status
	 */
	public int getStatus() {
		return status;
	}

	/**
	 * Gets the status phrase of the response object.
	 * 
	 * @return the phrase
	 */
	public String getPhrase() {
		return phrase;
	}

	/**
	 * Gets the body of the response.
	 * 
	 * @return The body of the response.
	 */
	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
		if (this.body != null) {
			this.header.put(Protocol.CONTENT_LENGTH, body.length() + "");
		}
	}

	public String getHeaderField(String key) {
		return this.header.get(key);
	}

	// /**
	// * Returns the header fields associated with the response object.
	// *
	// * @return the header
	// */
	// public Map<String, String> getHeader() {
	// // Lets return the unmodifable view of the header map
	// return Collections.unmodifiableMap(header);
	// }

	/**
	 * Maps a key to value in the header map.
	 * 
	 * @param key
	 *            A key, e.g. "Host"
	 * @param value
	 *            A value, e.g. "www.rose-hulman.edu"
	 */
	public void put(String key, String value) {
		this.header.put(key, value);
	}

	/**
	 * Reads raw data from the supplied input stream and constructs a
	 * <tt>HttpRequest</tt> object out of the raw data.
	 * 
	 * @param inputStream
	 *            The input stream to read from.
	 * @return A <tt>HttpRequest</tt> object.
	 * @throws Exception
	 *             Throws either {@link ProtocolException} for bad request or
	 *             {@link IOException} for socket input stream read errors.
	 */
	public static HttpResponse read(InputStream inputStream) throws Exception {

		String inVersion;
		int inStatus;
		String inPhrase;
		Map<String, String> inHeader = new HashMap<String, String>();
		String inBody = null;

		InputStreamReader inStreamReader = new InputStreamReader(inputStream);
		BufferedReader reader = new BufferedReader(inStreamReader);

		// First Response Line: HTTP/1.1 200 OK
		String line = reader.readLine();

		if (line == null) {
			throw new ProtocolException();
		}

		String[] args = line.split(" ");
		inVersion = args[0];
		inStatus = Integer.parseInt(args[1]);
		inPhrase = args[2];

		line = reader.readLine().trim();
		while (!line.equals("")) {

			line = line.trim();
			int index = line.indexOf(' ');

			if (index > 0 && index < line.length() - 1) {
				String key = line.substring(0, index);
				String value = line.substring(index + 1);

				key = key.trim();

				key = key.substring(0, key.length() - 1);

				value = value.trim();

				inHeader.put(key, value);
			}
			line = reader.readLine().trim();
		}

		int contentLength = 0;
		try {
			contentLength = Integer.parseInt(inHeader
					.get(Protocol.CONTENT_LENGTH));
		} catch (Exception e) {
		}
		if (contentLength > 0) {
			char[] tempBody = new char[contentLength];
			reader.read(tempBody);
			inBody = new String(tempBody);
		}
		return new HttpResponse(inVersion, inStatus, inPhrase, inHeader, inBody);
	}

	/**
	 * Writes the data of the http response object to the output stream.
	 * 
	 * @param outStream
	 *            The output stream
	 * @throws Exception
	 */
	public void write(OutputStream outStream) throws Exception {
		BufferedOutputStream out = new BufferedOutputStream(outStream,
				Protocol.CHUNK_LENGTH);

		String line = this.version + Protocol.SPACE + this.status
				+ Protocol.SPACE + this.phrase + Protocol.CRLF;
		out.write(line.getBytes());

		if (header != null && !header.isEmpty()) {
			for (Map.Entry<String, String> entry : header.entrySet()) {
				String key = entry.getKey();
				String value = entry.getValue();

				line = key + Protocol.SEPERATOR + Protocol.SPACE + value
						+ Protocol.CRLF;
				out.write(line.getBytes());
			}
		}

		out.write(Protocol.CRLF.getBytes());

		if (this.getStatus() == Protocol.OK_CODE && this.body != null) {

			out.write(this.body.getBytes());

		}
		out.flush();
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("----------- Header ----------------\n");
		buffer.append(this.version);
		buffer.append(Protocol.SPACE);
		buffer.append(this.status);
		buffer.append(Protocol.SPACE);
		buffer.append(this.phrase);
		buffer.append(Protocol.LF);

		for (Map.Entry<String, String> entry : this.header.entrySet()) {
			buffer.append(entry.getKey());
			buffer.append(Protocol.SEPERATOR);
			buffer.append(Protocol.SPACE);
			buffer.append(entry.getValue());
			buffer.append(Protocol.LF);
		}

		buffer.append(Protocol.LF);

		buffer.append("\n----------------------------------\n");
		buffer.append("------------- Body ---------------\n");
		buffer.append(this.body);
		buffer.append("\n----------------------------------\n");
		return buffer.toString();
	}

}

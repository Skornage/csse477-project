/*
 * HttpResponseFactory.java
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

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

/**
 * This is a factory to produce various kind of HTTP responses.
 * 
 * @author Chandan R. Rupakheti (rupakhet@rose-hulman.edu)
 */
public class HttpResponseFactory {
	private static HashMap<String, AbstractResponseFactory> abstractResponseFactories = new HashMap<String, AbstractResponseFactory>();
	private static HttpResponseFactory singleton = new HttpResponseFactory();

	private HttpResponseFactory() {
		abstractResponseFactories.put("404", new Response404Factory());
		abstractResponseFactories.put("200", new Response200Factory());
		abstractResponseFactories.put("500", new Response500Factory());
		abstractResponseFactories.put("400", new Response400Factory());
		abstractResponseFactories.put("505", new Response505Factory());
	}

	public static HttpResponseFactory getSingleton() {
		return singleton;
	}

	/**
	 * Convenience method for adding general header to the supplied response
	 * object.
	 * 
	 * @param response
	 *            The {@link HttpResponse} object whose header needs to be
	 *            filled in.
	 * @param connection
	 *            Supported values are {@link Protocol#OPEN} and
	 *            {@link Protocol#CLOSE}.
	 */
	static void fillGeneralHeader(HttpResponse response) {

		// Lets add current date
		Date date = Calendar.getInstance().getTime();
		response.put(Protocol.DATE, date.toString());

		// Lets add server info
		response.put(Protocol.Server, Protocol.getServerInfo());

		// Lets add extra header with provider info
		response.put(Protocol.PROVIDER, Protocol.AUTHOR);
	}

	public static HttpResponse getPreMadeResponse(String key) {
		return abstractResponseFactories.get(key).getResponse();
	}

	/**
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public static String getFileContentsForResponseBody(File file) {
		try {
			FileInputStream fileInStream = new FileInputStream(file);
			BufferedInputStream inStream = new BufferedInputStream(
					fileInStream, Protocol.CHUNK_LENGTH);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			byte[] buffer = new byte[Protocol.CHUNK_LENGTH];
			int bytesRead = 0;
			// While there is some bytes to read from file, read each chunk and
			// send
			// to the socket out stream
			while ((bytesRead = inStream.read(buffer)) != -1) {
				baos.write(buffer, 0, bytesRead);
			}
			// Close the file input stream, we are done reading
			inStream.close();
			return baos.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return "Error parsing file";
		}
	}
}

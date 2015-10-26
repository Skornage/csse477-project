/*
 * ConnectionHandler.java
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

package server;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import protocol.HttpRequest;
import protocol.HttpResponse;
import protocol.HttpResponseFactory;
import protocol.Protocol;
import protocol.ProtocolException;

/**
 * This class is responsible for handling a incoming request by creating a
 * {@link HttpRequest} object and sending the appropriate response be creating a
 * {@link HttpResponse} object. It implements {@link Runnable} to be used in
 * multi-threaded environment.
 * 
 * @author Chandan R. Rupakheti (rupakhet@rose-hulman.edu)
 */
public class ConnectionHandler implements Runnable {
	private Server server;
	private Socket socket;
	private HashMap<String, HashMap<String, AbstractPluginServlet>> plugins;

	public ConnectionHandler(Server server, Socket socket) {
		this.server = server;
		this.socket = socket;
		plugins = new HashMap<String, HashMap<String, AbstractPluginServlet>>();

//		plugins.put("SamplePlugin",
//				new HashMap<String, AbstractPluginServlet>());
//		AbstractPluginServlet get = new SamplePluginGetServlet();
//		AbstractPluginServlet post = new SamplePluginPostServlet();
//		plugins.get("SamplePlugin").put(get.getServletURI(), get);
//		plugins.get("SamplePlugin").put(post.getServletURI(), post);
	}

	/**
	 * @return the socket
	 */
	public Socket getSocket() {
		return socket;
	}

	/**
	 * The entry point for connection handler. It first parses incoming request
	 * and creates a {@link HttpRequest} object, then it creates an appropriate
	 * {@link HttpResponse} object and sends the response back to the client
	 * (web browser).
	 */
	public void run() {
		// Get the start time
		long start = System.currentTimeMillis();

		InputStream inStream = null;
		OutputStream outStream = null;
		
		this.loadPlugins();

		JarDirectoryListener jarListener = new JarDirectoryListener(this);
		new Thread(jarListener).start();
		

		try {
			inStream = this.socket.getInputStream();
			outStream = this.socket.getOutputStream();
		} catch (Exception e) {
			// Cannot do anything if we have exception reading input or output
			// stream
			// May be have text to log this for further analysis?
			e.printStackTrace();

			// Increment number of connections by 1
			server.incrementConnections(1);
			// Get the end time
			long end = System.currentTimeMillis();
			this.server.incrementServiceTime(end - start);
			return;
		}

		// At this point we have the input and output stream of the socket
		// Now lets create a HttpRequest object
		HttpRequest request = null;
		HttpResponse response = null;
		try {
			request = HttpRequest.read(inStream);
			System.out.println("REQUEST: " + request.toString());
		} catch (ProtocolException pe) {
			// We have some sort of protocol exception. Get its status code and
			// create response
			// We know only two kind of exception is possible inside
			// fromInputStream
			// Protocol.BAD_REQUEST_CODE and Protocol.NOT_SUPPORTED_CODE
			int status = pe.getStatus();
			if (status == Protocol.BAD_REQUEST_CODE) {
				response = HttpResponseFactory.getSingleton()
						.getPreMadeResponse("400");
			} else {
				response = HttpResponseFactory.getSingleton()
						.getPreMadeResponse("505");
			}
		} catch (Exception e) {
			e.printStackTrace();
			// For any other error, we will create bad request response as well
			response = HttpResponseFactory.getSingleton().getPreMadeResponse(
					"400");
		}

		if (response != null) {
			// Means there was an error, now write the response object to the
			// socket
			try {
				response.write(outStream);
				// System.out.println(response);
			} catch (Exception e) {
				// We will ignore this exception
				e.printStackTrace();
			}

			// Increment number of connections by 1
			server.incrementConnections(1);
			// Get the end time
			long end = System.currentTimeMillis();
			this.server.incrementServiceTime(end - start);
			return;
		}

		// We reached here means no error so far, so lets process further
		try {
			// Fill in the code to create a response for version mismatch.
			// You may want to use constants such as Protocol.VERSION,
			// Protocol.NOT_SUPPORTED_CODE, and more.
			// You can check if the version matches as follows
			if (!request.getVersion().equalsIgnoreCase(Protocol.VERSION)) {
				// Here you checked that the "Protocol.VERSION" string is not
				// equal to the
				// "request.version" string ignoring the case of the letters in
				// both strings
				response = HttpResponseFactory.getSingleton()
						.getPreMadeResponse("505");

			} else {
				String[] URIs = request.getUri().split("/");
				if (plugins.containsKey(URIs[1])) {
					HashMap<String, AbstractPluginServlet> servlets = plugins
							.get(URIs[1]);
					if (servlets.containsKey(URIs[2])) {
						AbstractPluginServlet servlet = servlets.get(URIs[2]);
						response = servlet.HandleRequest(request);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (response == null) {
			response = HttpResponseFactory.getSingleton().getPreMadeResponse(
					"400");
		}

		try {
			// Write response and we are all done so close the socket
			response.write(outStream);
			socket.close();
		} catch (Exception e) {
			// We will ignore this exception
			e.printStackTrace();
		}

		// Increment number of connections by 1
		server.incrementConnections(1);
		// Get the end time
		long end = System.currentTimeMillis();
		this.server.incrementServiceTime(end - start);
	}

	private void loadPlugins() {

		File f = new File("plugins");
		File[] jarsToAdd = f.listFiles();

		if (jarsToAdd != null) {
			for (File jar : jarsToAdd) {
				loadPlugin(jar);
			}
		}
	}

	protected void loadPlugin(File jar) {
		if (jar.isFile()) {
			String name = jar.getName();
			int i = name.lastIndexOf('.');
			if (i > 0) {
				String extension = name.substring(i + 1);
				if (extension.toLowerCase().equals("jar")) {

					try {
						URL fileUrl = jar.toURI().toURL();
						URL[] urls = { fileUrl };
						URLClassLoader jarLoader = new URLClassLoader(urls);
						ZipInputStream zip = new ZipInputStream(
								new FileInputStream(fileUrl.toString().substring(5)));
						for (ZipEntry entry = zip.getNextEntry(); entry != null; entry = zip.getNextEntry()) {
							if (!entry.isDirectory() && entry.getName().endsWith(".class")) {
								String className = entry.getName().replace(".class", "");
								Class<?> c = jarLoader.loadClass(className);
								Object o = c.newInstance();
								String pluginUri = ((AbstractPluginServlet) o).getPluginURI();
								HashMap<String, AbstractPluginServlet> servlets = plugins.get(pluginUri);
								if (AbstractPluginServlet.class.isAssignableFrom(o.getClass())) {
									if (servlets == null) {
										servlets = new HashMap<String, AbstractPluginServlet>();
										plugins.put(pluginUri, servlets);
									}
									servlets.put(((AbstractPluginServlet) o).getServletURI(), (AbstractPluginServlet) o);
								}
							}
						}

						jarLoader.close();
						zip.close();
					} catch (Exception e) {
						System.out.println("Error: " + e.toString());
					}
				}
			}
		}
	}

	protected void reinitializePlugins() {
		this.plugins = new HashMap<String, HashMap<String, AbstractPluginServlet>>();
		this.loadPlugins();
	}
}

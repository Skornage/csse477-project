/*
 * Server.java
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

import gui.WebServer;

import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.Modifier;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * This represents a welcoming server for the incoming TCP request from a HTTP
 * client such as a web browser.
 * 
 * @author Chandan R. Rupakheti (rupakhet@rose-hulman.edu)
 */
public class Server implements Runnable {
	private static String rootDirectory;
	private int port;
	private boolean stop;
	private ServerSocket welcomeSocket;

	private long connections;
	private long serviceTime;

	private WebServer window;
	private Server server;
	HashMap<String, HashMap<String, AbstractPluginServlet>> plugins;

	/**
	 * @param rootDirectory
	 * @param port
	 */
	public Server(String rootDirectory, int port, WebServer window) {
		this.rootDirectory = rootDirectory;
		this.port = port;
		this.stop = false;
		this.connections = 0;
		this.serviceTime = 0;
		this.window = window;
		plugins = new HashMap<String, HashMap<String, AbstractPluginServlet>>();

		this.loadPlugins();

		JarDirectoryListener jarListener = new JarDirectoryListener(this);
		new Thread(jarListener).start();
	}

	/**
	 * Gets the root directory for this web server.
	 * 
	 * @return the rootDirectory
	 */
	public static String getRootDirectory() {
		return rootDirectory;
	}

	/**
	 * Gets the port number for this web server.
	 * 
	 * @return the port
	 */
	public int getPort() {
		return port;
	}

	/**
	 * Returns connections serviced per second. Synchronized to be used in
	 * threaded environment.
	 * 
	 * @return
	 */
	public synchronized double getServiceRate() {
		if (this.serviceTime == 0)
			return Long.MIN_VALUE;
		double rate = this.connections / (double) this.serviceTime;
		rate = rate * 1000;
		return rate;
	}

	/**
	 * Increments number of connection by the supplied value. Synchronized to be
	 * used in threaded environment.
	 * 
	 * @param value
	 */
	public synchronized void incrementConnections(long value) {
		this.connections += value;
	}

	/**
	 * Increments the service time by the supplied value. Synchronized to be
	 * used in threaded environment.
	 * 
	 * @param value
	 */
	public synchronized void incrementServiceTime(long value) {
		this.serviceTime += value;
	}

	/**
	 * The entry method for the main server thread that accepts incoming TCP
	 * connection request and creates a {@link ConnectionHandler} for the
	 * request.
	 */
	public void run() {
		try {
			this.welcomeSocket = new ServerSocket(port);

			// Now keep welcoming new connections until stop flag is set to true
			while (true) {
				// Listen for incoming socket connection
				// This method block until somebody makes a request
				Socket connectionSocket = this.welcomeSocket.accept();

				// Come out of the loop if the stop flag is set
				if (this.stop)
					break;

				// Create a handler for this incoming connection and start the
				// handler in a new thread
				ConnectionHandler handler = new ConnectionHandler(this,
						connectionSocket);
				new Thread(handler).start();
			}
			this.welcomeSocket.close();
		} catch (Exception e) {
			window.showSocketException(e);
		}
	}

	/**
	 * Stops the server from listening further.
	 */
	public synchronized void stop() {
		if (this.stop)
			return;

		this.stop = true;
		try {
			// This will force welcomeSocket to come out of the blocked accept()
			// method
			// in the main loop of the start() method
			Socket socket = new Socket(InetAddress.getLocalHost(), port);

			// We do not have any other job for this socket so just close it
			socket.close();
		} catch (Exception e) {
		}
	}

	/**
	 * Checks if the server is stopeed or not.
	 * 
	 * @return
	 */
	public boolean isStoped() {
		if (this.welcomeSocket != null)
			return this.welcomeSocket.isClosed();
		return true;
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
								new FileInputStream(fileUrl.toString()
										.substring(5)));
						for (ZipEntry entry = zip.getNextEntry(); entry != null; entry = zip
								.getNextEntry()) {
							if (!entry.isDirectory()
									&& entry.getName().endsWith(".class")) {
								String className = entry.getName().replace(
										".class", "");
								Class<?> c = jarLoader.loadClass(className
										.replace('/', '.'));

								boolean isConcreteClass = true;

								try {
									c.newInstance();
								} catch (Exception e) {
									isConcreteClass = false;
								}

								// System.out.println("current class: "
								// + c.getName());
								// System.out.println("Concrete?: "
								// + isConcreteClass);
								boolean isAbstractPluginServletSubclass = false;

								Class<?> parent = c.getSuperclass();
								while (parent != null) {
									// System.out.println("Current parent:  "
									// + parent.getName());
									if (parent
											.equals(AbstractPluginServlet.class)) {
										isAbstractPluginServletSubclass = true;
										break;
									}
									parent = parent.getSuperclass();
								}
								// System.out.println("isAServlet?   "
								// + isAbstractPluginServletSubclass);

								if (isConcreteClass
										&& isAbstractPluginServletSubclass) {
									AbstractPluginServlet o = (AbstractPluginServlet) c
											.newInstance();
									String pluginUri = o.getPluginURI();
									HashMap<String, AbstractPluginServlet> servlets = plugins
											.get(pluginUri);
									if (servlets == null) {
										servlets = new HashMap<String, AbstractPluginServlet>();
										plugins.put(pluginUri, servlets);
									}
									servlets.put(o.getServletURI(), o);
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

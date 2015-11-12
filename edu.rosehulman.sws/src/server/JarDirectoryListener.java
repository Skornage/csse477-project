/*
 * DirectoryListener.java
 * Oct 25, 2015
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

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.List;

/**
 * 
 * @author Chandan R. Rupakheti (rupakhcr@clarkson.edu)
 */
public class JarDirectoryListener implements Runnable {

	private Server server;
	private String filePath;

	public JarDirectoryListener(Server server, String filePath) {
		this.server = server;
		this.filePath = filePath;
	}

	@Override
	public void run() {

		Path myDir = Paths.get(this.filePath);

		try {
			WatchService watcher = myDir.getFileSystem().newWatchService();
			myDir.register(watcher, ENTRY_CREATE, ENTRY_DELETE);

			WatchKey watckKey = watcher.take();
			while (true) {
				List<WatchEvent<?>> events = watckKey.pollEvents();
				for (WatchEvent<?> event : events) {
					if (event.kind() == ENTRY_CREATE) {
						server.loadPlugin(new File(this.filePath
								+ System.getProperty("file.separator")
								+ event.context().toString()));
					} else if (event.kind() == ENTRY_DELETE) {
						server.reinitializePlugins();
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Directory Listener Error: " + e.toString());
		}

	}

}

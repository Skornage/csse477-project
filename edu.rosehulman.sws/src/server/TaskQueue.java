/*
 * TaskQueue.java
 * Nov 1, 2015
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

import java.net.Socket;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * 
 * @author Chandan R. Rupakheti (rupakhcr@clarkson.edu)
 */
public class TaskQueue implements Runnable {
	private ConcurrentLinkedDeque<Socket> taskQueue = new ConcurrentLinkedDeque<Socket>();
	private Server server;

	public TaskQueue(Server server) {
		this.server = server;
	}

	public synchronized void addTask(Socket s) {
		this.taskQueue.add(s);
	}

	@Override
	public void run() {
		while (true) {
			if (!this.taskQueue.isEmpty()) {
				Socket connectionSocket = this.taskQueue.pollFirst();
				ConnectionHandler handler = new ConnectionHandler(this.server,
						connectionSocket);
				//System.out.println("starting a task");
				new Thread(handler).start();
			}
		}
	}
}

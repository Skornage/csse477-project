/*
 * Broker.java
 * Nov 11, 2015
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

import java.util.HashMap;

/**
 * 
 * @author Chandan R. Rupakheti (rupakhcr@clarkson.edu)
 */
public class Broker extends Server {
	private GameDistributionQueue queue;
	private GameManager mgr;

	public Broker(String rootDirectory, int port, IWebServer window,
			int DOSRequestLimit, int DOSTimeInterval, int serverHandlerPort,
			String applicationKey) {
		super(rootDirectory, port, window, DOSRequestLimit, DOSTimeInterval);
		this.queue = new GameDistributionQueue();
		this.mgr = new GameManager();
		HashMap<String, AbstractPluginServlet> map = new HashMap<String, AbstractPluginServlet>();
		map.put("play", new HangmanStartGameBrokerServlet(this));
		map.put("", new HangmanGetGamesBrokerServlet(this));
		map.put("create", new HangmanCreateBrokerServlet(this));
		map.put("update", new HangmanUpdateBrokerServlet(this));
		map.put("delete", new HangmanDeleteBrokerServlet(this));
		this.plugins.put("games", map);

		GameServerCommunicator comm = new GameServerCommunicator(
				serverHandlerPort, applicationKey, queue);
		new Thread(comm).start();
	}

	public GameManager getGameManager() {
		return this.mgr;
	}

	public GameDistributionQueue getGameDistributionQueue() {
		return this.queue;
	}
}

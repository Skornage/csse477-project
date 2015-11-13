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

/**
 * 
 * @author Chandan R. Rupakheti (rupakhcr@clarkson.edu)
 */
public class Broker extends Server {
	private GameDistributionQueue queue;
	private GameManager mgr;

	public Broker(String rootDirectory, int port, IWebServer window,
			int DOSRequestLimit, int DOSTimeInterval, int serverHandlerPort,
			String applicationKey, String pluginDirectory) {
		super(rootDirectory, port, window, DOSRequestLimit, DOSTimeInterval, pluginDirectory);
		this.queue = new GameDistributionQueue();
		this.mgr = new GameManager();
//		 HashMap<String, AbstractPluginServlet> map = new HashMap<String,
//		 AbstractPluginServlet>();
//		 HangmanStartGameBrokerServlet start = new HangmanStartGameBrokerServlet();
//		 HangmanGetGamesBrokerServlet games = new HangmanGetGamesBrokerServlet();
//		 HangmanCreateBrokerServlet create = new HangmanCreateBrokerServlet();
//		 HangmanUpdateBrokerServlet update = new HangmanUpdateBrokerServlet();
//		 HangmanDeleteBrokerServlet delete = new HangmanDeleteBrokerServlet();
//		 
//		 start.setServer(this);
//		 games.setServer(this);
//		 create.setServer(this);
//		 update.setServer(this);
//		 delete.setServer(this);
//		 
//		 map.put("play", start);
//		 map.put("", games);
//		 map.put("create", create);
//		 map.put("update", update);
//		 map.put("delete", delete);
//		 this.plugins.put("games", map);

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

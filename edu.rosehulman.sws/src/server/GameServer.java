/*
 * GameServer.java
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
public class GameServer extends Server {
	private GameManager mgr;

	/**
	 * @param rootDirectory
	 * @param port
	 * @param window
	 * @param DOSRequestLimit
	 * @param DOSTimeInterval
	 * @param applicationKey
	 * @param brokerIP
	 */
	public GameServer(String rootDirectory, int port, IWebServer window,
			int DOSRequestLimit, int DOSTimeInterval, int brokerPort,
			String brokerIP, String applicationKey) {
		super(rootDirectory, port, window, DOSRequestLimit, DOSTimeInterval);
		this.mgr = new GameManager();
		HashMap<String, AbstractPluginServlet> map = new HashMap<String, AbstractPluginServlet>();
		map.put("game", new HangmanPutGameServerServlet(this));
		this.plugins.put("hangman", map);
		
		BrokerCommunicator comm = new BrokerCommunicator(brokerIP, brokerPort,
				applicationKey, mgr, port);
		new Thread(comm).start();
	}

	public GameManager getGameManager() {
		return this.mgr;
	}
}

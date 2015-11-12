/*
 * GameDistributionQueue.java
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

import java.util.concurrent.ConcurrentLinkedDeque;

import protocol.HttpRequest;
import protocol.HttpResponse;

/**
 * 
 * @author Chandan R. Rupakheti (rupakhcr@clarkson.edu)
 */
public class GameDistributionQueue {
	private ConcurrentLinkedDeque<GameServerAddress> queue;

	public GameDistributionQueue() {
		this.queue = new ConcurrentLinkedDeque<GameServerAddress>();
	}

	public void add(GameServerAddress gameServerAddress) {
		this.queue.offer(gameServerAddress);
	}

	// TODO public void remove(){}

	public GameServerAddress assignGameToServer(BrokerHangmanGame game) {
		GameServerAddress gameServerAddr = this.queue.pollFirst();
		this.queue.offer(gameServerAddr);
		String request = "POST / HTTP/1.1\naccept-language: en-US,en;q=0.8;\nid: "
				+ game.getID()
				+ "\nname: "
				+ game.getName()
				+ "\npostedbyuser: "
				+ game.getPostedByUser() + "\nword: " + game.getWord() + "\n\n ";
		String ip = gameServerAddr.getIp();
		int port = gameServerAddr.getBrokerCommPort();
		HttpResponse response = HttpRequest.makeRequest(request, ip, port);

		if (response == null) {
			return null;
		}
		return gameServerAddr;
	}
}

/*
 * DOSDetector.java
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

/**
 * 
 * @author Chandan R. Rupakheti (rupakhcr@clarkson.edu)
 */
public class DOSDetector implements Runnable {
	private Server server;
	private ArrayList<String> events = new ArrayList<String>();
	private int DOSRequestLimit;
	private int DOSTimeInterval;

	public DOSDetector(Server server, int DOSRequestLimit, int DOSTimeInterval) {
		this.server = server;
		this.DOSRequestLimit = DOSRequestLimit;
		this.DOSTimeInterval = DOSTimeInterval;
	}

	@Override
	public void run() {
		while (true) {
			try {
				Thread.sleep(DOSTimeInterval);
			} catch (InterruptedException e) {
			}
			ArrayList<String> temp = this.events;
			this.events = new ArrayList<String>();

			HashMap<String, Integer> map = new HashMap<String, Integer>();
			for (String ip : temp) {
				if (map.containsKey(ip)) {
					map.put(ip, map.get(ip) + 1);
				} else {
					map.put(ip, 1);
				}
			}

			for (Entry<String, Integer> e : map.entrySet()) {
//				 System.out.println("IP " + e.getKey() + " has " +
//				 e.getValue()
			//	 + " occurences");
				if (e.getValue() > DOSRequestLimit) {
					 System.out.println("IP " + e.getKey() + " has " +
					 e.getValue()
					 + " occurences");
					this.server.addIPBan(e.getKey());
				}
			}
		}
	}

	public void addEvent(String ipAddress) {
		this.events.add(ipAddress);
	}

}

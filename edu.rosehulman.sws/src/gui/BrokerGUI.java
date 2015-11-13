/*
 * BrokerGUI.java
 * Nov 12, 2015
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

package gui;

import server.Broker;

/**
 * 
 * @author Chandan R. Rupakheti (rupakhcr@clarkson.edu)
 */
public class BrokerGUI extends AbstractServerGUI {

	private static final long serialVersionUID = -8128535038518676956L;

	@Override
	protected void setDefaultPortNumber() {
		this.port = 8080;
		this.txtPortNumber.setText(this.port + "");
	}

	@Override
	protected void startServer() {

		// Get hold of the root directory
		String rootDirectory = this.txtRootDirectory.getText();

		Broker broker = new Broker(rootDirectory, getPortNumber(), this, 2000,
				1000, 82,
				"super-secret-application-key-whatever-you-do-dont-commit-this-to-github", "BrokerPlugins");
		Thread brokerThread = new Thread(broker);
		brokerThread.start();
	}

	public static void main(String args[]) {
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				new BrokerGUI().setVisible(true);
			}
		});
	}
}

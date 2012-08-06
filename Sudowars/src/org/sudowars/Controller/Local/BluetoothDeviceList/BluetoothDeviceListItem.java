/*******************************************************************************
 * Copyright (c) 2011 - 2012 Adrian Vielsack, Christof Urbaczek, Florian Rosenthal, Michael Hoff, Moritz Lüdecke, Philip Flohr.
 * 
 * This file is part of Sudowars.
 * 
 * Sudowars is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * Sudowars is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Sudowars.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * 
 * 
 * Diese Datei ist Teil von Sudowars.
 * 
 * Sudowars ist Freie Software: Sie können es unter den Bedingungen
 * der GNU General Public License, wie von der Free Software Foundation,
 * Version 3 der Lizenz oder (nach Ihrer Option) jeder späteren
 * veröffentlichten Version, weiterverbreiten und/oder modifizieren.
 * 
 * Sudowars wird in der Hoffnung, dass es nützlich sein wird, aber
 * OHNE JEDE GEWÄHELEISTUNG, bereitgestellt; sogar ohne die implizite
 * Gewährleistung der MARKTFÄHIGKEIT oder EIGNUNG FÜR EINEN BESTIMMTEN ZWECK.
 * Siehe die GNU General Public License für weitere Details.
 * 
 * Sie sollten eine Kopie der GNU General Public License zusammen mit diesem
 * Programm erhalten haben. Wenn nicht, siehe <http://www.gnu.org/licenses/>.
 * 
 * Contributors:
 * initial API and implementation:
 * Adrian Vielsack
 * Christof Urbaczek
 * Florian Rosenthal
 * Michael Hoff
 * Moritz Lüdecke
 * Philip Flohr 
 ******************************************************************************/
package org.sudowars.Controller.Local.BluetoothDeviceList;


/**
 * One list element of the bluetooth device list in the {@link MultiplayerMenu}-Activity.
 */
public class BluetoothDeviceListItem {
	/**
	 * the device name
	 */
	private String name;
	
	/**
	 * the mac address
	 */
	private String mac;
	
	/**
	 * the state, if the local and the other bluetooth device are paired
	 */
	private boolean paired;
	
	/**
	 * Constructor which initializes a new instance of this class with the given variables
	 *
	 * @param name the device name
	 * @param mac the mac address
	 * @param paired the state, if the local and the other bluetooth device are paired
	 */
	public BluetoothDeviceListItem (String name, String mac, boolean paired) {
		if (mac == null) {
			throw new IllegalArgumentException("Given mac is null.");
		}
		
		if (name == null) {
			this.name = "";
		} else {
			this.name = name;
		}
		
		this.mac = mac;
		this.paired = paired;
	}

	/**
	 * Returns the device {@link name}.
	 * 
	 * @return the device {@link name}
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Returns the {@link mac} addresse of the bluetooth device.
	 * 
	 * @return the {@link mac} addresse of the bluetooth device
	 */
	public String getMac() {
		return this.mac;
	}

	/**
	 * Returns <code>true</code>, if the local and the other bluetooth device are paired
	 * Otherwise returns <code>false</code>.
	 * 
	 * @return <code>true</code>, if the local and the other bluetooth device are paired
	 */
	public boolean isPaired() {
		return this.paired; 	
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object object) {
		if (object != null && object instanceof BluetoothDeviceListItem
				&& this.name.equals(((BluetoothDeviceListItem) object).getName())
				&& this.mac.equals(((BluetoothDeviceListItem) object).getMac())
				&& this.paired == ((BluetoothDeviceListItem) object).isPaired()) {
			return true;
		} else {
			return false;
		}
	}
}

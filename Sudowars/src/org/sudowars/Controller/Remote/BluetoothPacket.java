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
package org.sudowars.Controller.Remote;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;
import java.io.Serializable;
import java.io.StreamCorruptedException;
import java.util.zip.CRC32;
import org.sudowars.Model.CommandManagement.Command;
import org.sudowars.DebugHelper;

/**
 * This class describes the packet store structure of the {@link BluetoothConnection} class.
 */

/**
 * This class describes the packet store structure of the {@link BluetoothConnection} class.
 */
public class BluetoothPacket implements Serializable{
	private static final long serialVersionUID = 2125177040121071495L;
	
	private static int packetIndex = 0;
	private static Object indexSync = new Object();
	
	private int graceCounter = 10;
	
	/**
	 * The arguments of the command as serializable class
	 */
	private Object data;

	private boolean packetCorrupted = false;
	/**
	 * the CRC32 checksum for this packet for validation.
	 */
	private byte[] crc32 = new byte[5];

	/**
	 * The time when the packet was sent.
	 */
	private long timeSend = 0;

	
	private byte[] rawData;
	/**
	 * The transfer id of the packet
	 */
	private byte packetId;
	
	public static int getNextCommandId() {
		int newPacketId;
		synchronized (indexSync) {
			newPacketId = (byte)packetIndex;
			packetIndex = (packetIndex + 1) % 256;	
		}
		return newPacketId;
	}
	
	/**
	 * Initializes a new instance of the {@link BluetoothPacket} class with a given command object.
	 * @param command A reference to a command  with shall be sent via bluetooth.
	 *
	 * @throws IllegalArgumentException if the given param is <code>null</code> or  doesn't implement the command interface
	 */
	public BluetoothPacket (Command command) throws IllegalArgumentException {
		this.data = command;
		
		this.packetId = (byte) getNextCommandId();
	}

	public BluetoothPacket(byte[] header, byte[] data){
		validate(header, data);
	}
	/**
	 * Returns the command of the current instance.
	 *
	 * @return A reference to the command.
	 */
	public Object getCommand() {
		if (rawData == null)
			return this.data;
		return deserializeCommand(rawData);
		//return this.data;
	}

	/**
	 * Returns the time (in milliseconds) when the current packet has been sent.
	 *
	 * @return <code>-1</code> for an unsent packet and a timestamp (in milliseconds) for a sent packet
	 */
	public long getTimeSend() {
		return this.timeSend;
	}

	/**
	 * Returns the CRC32 checksum for this packet for validation.
	 *
	 * @return Checksum as byte[]
	 */
	public byte[] getCRC() {
		return this.crc32;
	}

	/**
	 * Marks the packet as sent by setting the sent timestamp, which can be received via getTimeSend()
	 */
	public void setSent() {
		this.timeSend = System.currentTimeMillis();
	}
	
	public boolean isValid() {
		return ! this.packetCorrupted;
	}
	
	/**
	 * Validates an incoming packet against this packet
	 * @param header header of the packet
	 * @param data data of the packet
	 */
	public void validate (byte[] header, byte[] data) {
		//int len =  ((int)(header[8] & 0xFF) << 8) | (int)(header[9] & 0xFF);
		this.packetId = header[7];
		this.packetCorrupted = false;
		
		this.rawData = data;

		byte[] remCrc32 = new byte[5];
		
		System.arraycopy(header, 2, remCrc32, 0, 5);
		calculateCRC32(data);
		

		if (!java.util.Arrays.equals(remCrc32, crc32)) {
			this.packetCorrupted = true;
			DebugHelper.log(DebugHelper.PackageName.BluetoothPacket, "CRC32 did not match, sorry :-(");
			return;
		}
	}
	
	
	private void calculateCRC32(byte[] data) {
		CRC32 crc = new CRC32();
		
		crc.update(data);
		long crcVal = crc.getValue();
		for (int n = 0; n < 5; n++) {
			this.crc32[n] = (byte)(crcVal & 0xff);
			crcVal >>= 8;
		}
	}
	
	public Object deserializeCommand(byte[] data) {
		ByteArrayInputStream inp = new ByteArrayInputStream(data, 0, data.length);
		ObjectInputStream objIn;
		try {
			objIn = new ObjectInputStream(inp);
		} catch (StreamCorruptedException e1) {
			this.packetCorrupted = true;
			DebugHelper.log(DebugHelper.PackageName.BluetoothPacket, "Could not read Object " + e1);
			return  null;
		} catch (IOException e1) {
			this.packetCorrupted = true;
			DebugHelper.log(DebugHelper.PackageName.BluetoothPacket, "Could not read Object " + e1);
			return null;
		}
		
		try {
			this.data = objIn.readObject();
		} catch (OptionalDataException e) {
			this.packetCorrupted = true;
			DebugHelper.log(DebugHelper.PackageName.BluetoothPacket, "Could not read Object " + e);
			return null;
		} catch (ClassNotFoundException e) {
			this.packetCorrupted = true;
			DebugHelper.log(DebugHelper.PackageName.BluetoothPacket, "Could not read Object " + e);
			return null;
		} catch (IOException e) {
			this.packetCorrupted = true;
			DebugHelper.log(DebugHelper.PackageName.BluetoothPacket, "Could not read Object " + e);
			return null;
		}
		
		return this.data;
	}
	
	public byte[] getPacket() {
		
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ObjectOutputStream objo = null;
		try {
			objo = new ObjectOutputStream(out);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			if (objo != null)
				objo.writeObject(this.data);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		byte[] serData = out.toByteArray();
		
		calculateCRC32(serData);
		
		byte[] output = new byte[serData.length + 10];
		
		System.arraycopy(serData, 0, output, 10, serData.length);
		
		byte[] header = {
				'S', 'W',	//Header Magic
				0, 0, 0, 0, 0, //CRC32
				(byte)this.packetId,
				(byte)(serData.length >> 8), (byte)(serData.length & 0xFF)
				};
		System.arraycopy(this.crc32, 0, header, 2, 5);
		System.arraycopy(header, 0, output, 0, 10);
		return output;
	}
	
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof BluetoothPacket))
			return false;
		BluetoothPacket btPacket = (BluetoothPacket)o;
		
		if (btPacket.packetId != this.packetId){
			DebugHelper.log(DebugHelper.PackageName.BluetoothPacket, "Packet id does not match " + btPacket.packetId + " " + this.packetId);
			return false;
		}
		int a = 0;
		for (int n = 0; n < 5; n++) 
			a += btPacket.crc32[n] - this.crc32[n];
		if (a != 0){
			DebugHelper.log(DebugHelper.PackageName.BluetoothPacket, "Packet CRCs do not match");
			
			
			return false;
		}
		return true;
	}
	
	public byte getPacketId() {
		return this.packetId;
	}
	public boolean isRemoteCorrupted() {
		return !(this.graceCounter > 0);
	}
	
	public void markRemoteCorrupted() {
		this.graceCounter --;
	}
}


package org.sudowars.Controller.Remote;

import java.io.Serializable;

import org.sudowars.DebugHelper;

import android.os.SystemClock;

public class TimeSyncer implements Serializable {

	public static final byte CMD_TIMESYNC = (byte)0x45;
	public static final byte CMD_TIMESYNC_PONG = (byte)0xF0;
	/**
	 * 
	 */
	private static final long serialVersionUID = 4137026736197609825L;
	
	private long timeOffset = 0;
	
	private long sentTs = 0;
	
	private SudowarsSocket swSocket;
	
	public TimeSyncer (SudowarsSocket swSocket) {
		this.swSocket = swSocket;
	}
	
	private long getActualTimestamp() {
		return SystemClock.uptimeMillis();
	}
	
	private long byteToLong(byte data[]) {
		long ret = 0;
		for (int n = 0; n < 8; n++) {
			ret <<= 8;
			ret |= (data[n] & 0xFF);
		}
		return ret;
		
	}
	
	private byte[] longToByte(long l) {
		byte ret[] = new byte[8];
		for (int n = 0; n < 8; n++) {
			ret[7 - n] = (byte) (l & 0xFF);
			l >>= 8;
		}
		return ret;
	}
	
	public void syncTime() {
		sentTs = getActualTimestamp();
		sendPacket(CMD_TIMESYNC, longToByte(sentTs));
		DebugHelper.log(DebugHelper.PackageName.TimeSyncer, "New Sync Time Command Actual time is " + this.getCorrectedTimestamp());
	}
	
	private byte[] cutTheCrap(byte[] data) {
		byte ret[] = new byte[data.length - 1];
		System.arraycopy(data, 1, ret, 0, data.length - 1);
		return ret;
	}
	
	public void syncTimeCommand(byte[] data) {
		long timeReceived = getActualTimestamp();
		
		this.timeOffset = byteToLong(cutTheCrap(data)) - timeReceived;
		
		sendPacket(CMD_TIMESYNC_PONG);
		DebugHelper.log(DebugHelper.PackageName.TimeSyncer, "New Time Offset: " + this.timeOffset + " Actual Synced time is " + this.getCorrectedTimestamp());
	}
	
	public void syncTimePongCommand() {
		long timeReceived = getActualTimestamp();
		this.timeOffset = ((timeReceived - sentTs) >> 1) * -1;
		DebugHelper.log(DebugHelper.PackageName.TimeSyncer, "New Time Offset: " + this.timeOffset + " Actual Synced time is " + this.getCorrectedTimestamp());
	}
	
	public long getCorrectedTimestamp() {
		return getActualTimestamp() + this.timeOffset;
	}
	
	private void sendPacket (byte cmd) {
		byte data[] = new byte[1];
		sendPacket(cmd, data);
	}
	
	private void sendPacket (byte cmd, byte[] data) {
		int length = data.length + 1;
		byte syncPacketHeader[] = {
				'S', 'W', //Magic Header
				0x00, 0x00, 0x00, 0x00, 0x00,	//Fake CRC
				0x00,							//Fake PacketID
				(byte)(length >> 8), (byte)(length & 0xFF),	//Payload Length
				cmd		//CMD
		};
		
		byte syncPacket[] = new byte[11 + data.length];
		System.arraycopy(syncPacketHeader, 0, syncPacket, 0, 11);
		System.arraycopy(data, 0, syncPacket, 11, data.length);
		
		swSocket.sendData(syncPacket);
	}
	
	
	
}

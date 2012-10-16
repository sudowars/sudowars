package org.sudowars.Controller.Remote;



public interface SudowarsSocket{
	
	
	public abstract void setEventHandler(SocketEvent evtHandler);
	
	public abstract boolean listen() ;
	
	public abstract void close() ;
	
	public abstract boolean recv(byte[] data) ;
	
	public abstract boolean sendData(byte[] data) ;
	
	public abstract boolean isConnected();
	public abstract boolean connect(String deviceAddress);
	public abstract void kick();
	public abstract void ban();
	public abstract void stop();
	
	public abstract String getRemoteHost();
}

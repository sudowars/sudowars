package org.sudowars.Controller.Remote;

public abstract class SocketEvent {
	abstract public void onClose();
	abstract public void onConnected();
	abstract public void onConnecting();
	abstract public void onListening();
}

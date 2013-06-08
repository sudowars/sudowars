package org.sudowars.Controller.Remote;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class ConnectionService extends Service {

	public enum ConnectionTypes {Bluetooth, Wifi};
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

}

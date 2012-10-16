package org.sudowars.Model.Game;

import java.util.EventListener;

public interface StopWatchTickEventListener extends EventListener {
	public void onTick(int tickCount, long elapsedMilliseconds);
}

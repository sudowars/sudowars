package org.sudowars.Model.Game;

import java.io.Serializable;
import android.os.Handler;
import android.os.SystemClock;

/**
 * This class encapsulates a stop watch.
 */
abstract class StopWatch extends Handler implements Serializable, Runnable {

	private static final long serialVersionUID = 3030509319423347544L;
	
	transient protected boolean running = false;
	protected long elapsedMilliseconds;
	transient protected long lastLogTime;
	transient protected long nextTime;
	protected long tickInterval; 
	protected int tickCount;
		
	/**Initializes a new instance of the {@link StopWatch} class.
	*/
	public StopWatch() {
		//define a tick interval of 1s
		this(1000);
	}
	
	/**
	 * Initializes a new instance of the {@link StopWatch} class with a given elapsed time.
	 *
	 * @param tickInterval the amount of time between ticks of the current instance
	 *
	 * @throws IllegalArgumentException if the given tickInterval is < <code>0</code>
	*/
	public StopWatch(long tickInterval) throws IllegalArgumentException {
		if (tickInterval < 0) {
			throw new IllegalArgumentException("tickInterval cannot be negative.");
		}
		this.tickInterval = tickInterval;
	}
	
	/**
	 * Implementation of the run method which triggers {@link StopWatch#step()} and posts the next tick.
	 * @see StopWatch#step()
	 */
	@Override
	public void run() {
		if (this.running) {
			long now = SystemClock.uptimeMillis();
			this.elapsedMilliseconds += (now - this.lastLogTime);
			this.lastLogTime = now;
			
			step();
			//schedule the next event.
			this.nextTime += this.tickInterval;
			if (this.nextTime <= now) {
				this.nextTime += this.tickInterval;
			}
			//queue runner to trigger the event at the time stamp specified by nextTime
			postAtTime(this, this.nextTime);
		}
	}
	
	/**
	 * This method contains all the action to be executed at each tick of the watch.
	 * @see StopWatch#run()
	 */
	public abstract void step();
		
	/**
	 * Starts the stop watch.
	 *
	 * @return <code>true</code> at any time
	 */
	public boolean start() {
		if (!this.running) {
			this.running = true;
			long now = SystemClock.uptimeMillis();
			this.lastLogTime = now;
			this.nextTime = now;
			//the first event to be triggered now
			postAtTime(this, this.nextTime);
		}
		return true;
	}
	
	/**
	 * Pauses the stop watch.
	 *
	 * @return <code>true</code> at any time
	 */
	public boolean stop() {
		// TODO move in brackets
		this.removeCallbacks(this);
		if (this.running) {
			this.running = false;
			long now = SystemClock.uptimeMillis();
			this.elapsedMilliseconds += (now - this.lastLogTime);
			this.lastLogTime = now;
		}
		return true;
	}
	
	 /**
     * Stops the current instance, and resets the elapsed time.
     */
    public final void reset() {
    	stop();
        this.tickCount = 0;
        this.elapsedMilliseconds = 0;
    }
	
	/**
	 * Gets the elapsed time of the current instance.
	 *
	 * @return The elapsed time, in milliseconds.
	 */
	public long getElapsedTime() {
		if (this.running) {
			long now = SystemClock.uptimeMillis();
			return this.elapsedMilliseconds + (now - this.lastLogTime);
		}
		return this.elapsedMilliseconds;
		
	}
}

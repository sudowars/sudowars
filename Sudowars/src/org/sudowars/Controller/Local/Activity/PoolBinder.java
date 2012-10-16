package org.sudowars.Controller.Local.Activity;

import org.sudowars.Model.SudokuManagement.Pool.SudokuFilePool;
import org.sudowars.Model.SudokuManagement.Pool.SudokuFilePool.SudokuFilePoolBinder;
import org.sudowars.Model.SudokuManagement.Pool.SudokuPool;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

public class PoolBinder extends Activity {
	/**
	 * the pool full of Sudokus
	 */
	protected SudokuPool pool;
	
	/**
	 * tricker, if this activity is bound to the pool
	 */
	private boolean bound = false;
	
	/*
	 * (non-Javadoc)
	 * @see android.app.Activity#onStart()
	 */
	@Override
    protected void onStart() {
        super.onStart();
        
        Intent intent = new Intent(this, SudokuFilePool.class);
        bindService(intent, connection, Context.BIND_AUTO_CREATE);
    }
	
	/*
	 * (non-Javadoc)
	 * @see android.app.Activity#onStop()
	 */
    @Override
    protected void onStop() {
        super.onStop();
        
        if (this.bound) {
            unbindService(connection);
            this.bound = false;
        }
    }
    
    /*
     * Defines callbacks for service binding, passed to bindService()
     */
    private ServiceConnection connection = new ServiceConnection() {
    	/*
    	 * (non-Javadoc)
    	 * @see android.content.ServiceConnection#onServiceConnected(android.content.ComponentName, android.os.IBinder)
    	 */
        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            //we've bound to SudokuFilePool, cast the IBinder and get SudokuFilePool instance
            SudokuFilePoolBinder binder = (SudokuFilePoolBinder) service;
            PoolBinder.this.pool = binder.getService();
            PoolBinder.this.bound = true;
        }
        
        /*
         * (non-Javadoc)
         * @see android.content.ServiceConnection#onServiceDisconnected(android.content.ComponentName)
         */
        @Override
        public void onServiceDisconnected(ComponentName arg0) {
        	PoolBinder.this.bound = false;
        }
    };
}
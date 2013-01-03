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
package org.sudowars.Controller.Local.Activity;

import org.sudowars.Model.SudokuManagement.Pool.SudokuFilePool;
import org.sudowars.Model.SudokuManagement.Pool.SudokuFilePool.SudokuFilePoolBinder;
import org.sudowars.Model.SudokuManagement.Pool.SudokuPool;

import android.app.ActionBar;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceActivity;
import android.view.MenuItem;

public class Settings extends PreferenceActivity {
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
	 * @see android.preference.PreferenceActivity#onStop()
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
            Settings.this.pool = binder.getService();
            Settings.this.bound = true;
        }
        
        /*
         * (non-Javadoc)
         * @see android.content.ServiceConnection#onServiceDisconnected(android.content.ComponentName)
         */
        @Override
        public void onServiceDisconnected(ComponentName arg0) {
        	Settings.this.bound = false;
        }
    };
	
	/**
	 * Android calls this Function if the Preference Activity is invoked
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
	    ActionBar actionBar = getActionBar();
	    actionBar.setDisplayHomeAsUpEnabled(true);
	}
	
	/*
	 * (non-Javadoc)
	 * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
	 */
	@Override
	public boolean onOptionsItemSelected (MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			this.onBackPressed();
			return true;
		} else {
			return super.onOptionsItemSelected(item);
		}
	}
}

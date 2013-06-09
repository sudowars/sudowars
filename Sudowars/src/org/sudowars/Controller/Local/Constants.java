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
package org.sudowars.Controller.Local;

import java.lang.reflect.Method;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Point;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

/**
 * This class makes the life of a programmer sweeter...
 */
public class Constants {
	/**
	 * the status bar heights
	 */
	private static final int LOW_DPI_STATUS_BAR_HEIGHT = 19;
	private static final int MEDIUM_DPI_STATUS_BAR_HEIGHT = 25;
	private static final int HIGH_DPI_STATUS_BAR_HEIGHT = 38;
	private static final int XTRA_HIGH_DPI_STATUS_BAR_HEIGHT = 46;
	private int statusBarHeight;
	private boolean landscapeMode;
	
	/**
	 * The Constructor
	 * 
	 * @param activity the activity
	 */
	public Constants (Activity activity) {
		//get height of the status bar
		DisplayMetrics displayMetrics = new DisplayMetrics();
		((WindowManager) activity.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(displayMetrics);
		
    	if (isTabletDevice(activity.getResources().getConfiguration())) {
			this.statusBarHeight = XTRA_HIGH_DPI_STATUS_BAR_HEIGHT;
		} else {
			switch (displayMetrics.densityDpi) {
			    case DisplayMetrics.DENSITY_HIGH:
			    	this.statusBarHeight = HIGH_DPI_STATUS_BAR_HEIGHT;
			        break;
			    case DisplayMetrics.DENSITY_MEDIUM:
			    	this.statusBarHeight = MEDIUM_DPI_STATUS_BAR_HEIGHT;
			        break;
			    case DisplayMetrics.DENSITY_LOW:
			    	this.statusBarHeight = LOW_DPI_STATUS_BAR_HEIGHT;
			        break;
			    default:
			    	this.statusBarHeight = MEDIUM_DPI_STATUS_BAR_HEIGHT;
			}
		}
    	
    	Display display = activity.getWindowManager().getDefaultDisplay();
    	Point size = new Point();
		display.getSize(size);
    	this.landscapeMode = (size.x > size.y) ? true : false;
	}
	
	/**
	 * Returns <code>true</code>, if the device is a tablet.
	 * 
	 * @param configuration the configuration
	 * 
	 * @return <code>true</code>, if the device is a tablet.
	 */
	private boolean isTabletDevice(Configuration configuration) {
		boolean result = false;
		
		if (android.os.Build.VERSION.SDK_INT >= 11) {
			// Honeycomb: test screen size, use reflection because isLayoutSizeAtLeast is only available since 11
			try {
				Method mIsLayoutSizeAtLeast = configuration.getClass().getMethod("isLayoutSizeAtLeast", int.class);
				// Configuration.SCREENLAYOUT_SIZE_XLARGE
				result = (Boolean) mIsLayoutSizeAtLeast.invoke(configuration, 0x00000004);
			} catch (Exception x) {
				x.printStackTrace();
			}
		}
		
		return result;
	}

	
	/**
	 * Returns the height of the status bar
	 * 
	 * @param activity the activity
	 * 
	 * @return the height of the status bar
	 */
	public int getStatusBarHeight() {
		return this.statusBarHeight;
	}
	
	/**
	 * Returns <code>true</code>, if the device is in landscape mode,
	 * otherwise <code>false</code>
	 * 
	 * @return
	 */
	public boolean isLandscapeMode() {
		return this.landscapeMode;
	}
}

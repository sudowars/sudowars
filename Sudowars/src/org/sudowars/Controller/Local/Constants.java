package org.sudowars.Controller.Local;

import java.lang.reflect.Method;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
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
    	this.landscapeMode = display.getWidth() > display.getHeight() ? true : false;
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

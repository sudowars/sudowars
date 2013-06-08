/*******************************************************************************
 * Copyright (c) 2011 - 2012 Adrian Vielsack, Christof Urbaczek, Florian Rosenthal, Michael Hoff, Moritz Lüdecke, Philip Flohr.
 * 
 * This file is part of Sudowars,
 * Based on an official Android sample app
 * http://developer.android.com/training/implementing-navigation/lateral.html
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

import org.sudowars.R;
import org.sudowars.Model.SudokuManagement.Pool.SudokuFilePool;
import org.sudowars.Model.SudokuManagement.Pool.SudokuPool;
import org.sudowars.Model.SudokuManagement.Pool.SudokuFilePool.SudokuFilePoolBinder;

import android.app.ActionBar;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class Manual extends FragmentActivity {
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
            Manual.this.pool = binder.getService();
            Manual.this.bound = true;
        }
        
        /*
         * (non-Javadoc)
         * @see android.content.ServiceConnection#onServiceDisconnected(android.content.ComponentName)
         */
        @Override
        public void onServiceDisconnected(ComponentName arg0) {
        	Manual.this.bound = false;
        }
    };
    
	/**
     * The {@link android.support.v4.view.PagerAdapter} that will provide fragments representing
     * each object in a collection.
     */
    ManualPagerAdapter mManualPagerAdapter;
    
    /**
     * The {@link android.support.v4.view.ViewPager} that will display the object collection.
     */
    private ViewPager mViewPager;
    
    /*
     * (non-Javadoc)
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
		setContentView(R.layout.manual);
		
		final ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
		
        mManualPagerAdapter = new ManualPagerAdapter(getSupportFragmentManager(), getResources());
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mManualPagerAdapter);
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent upIntent = new Intent(this, MainMenu.class);
                if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
                    TaskStackBuilder.from(this)
                            .addNextIntent(upIntent)
                            .startActivities();
                    finish();
                } else {
                    NavUtils.navigateUpTo(this, upIntent);
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A {@link android.support.v4.app.FragmentStatePagerAdapter} that returns a fragment
     * representing an object in the collection.
     */
    public static class ManualPagerAdapter extends FragmentStatePagerAdapter {
        private String[] mTitles;
        private static String[] mBodies;
        private static int[] mImages = new int[] {
        	R.drawable.manual_image_1,
        	R.drawable.manual_image_2,
        	R.drawable.manual_image_3,
        	R.drawable.manual_image_4,
        	R.drawable.manual_image_5,
        	R.drawable.manual_image_6,
        	R.drawable.manual_image_7
    	};
    	
        public ManualPagerAdapter(FragmentManager fm, Resources res) {
            super(fm);
            mTitles = res.getStringArray(R.array.manual_title);
            mBodies = res.getStringArray(R.array.manual_body);
        }
        
        @Override
        public Fragment getItem(int i) {
            Fragment fragment = new ManualObjectFragment();
            Bundle args = new Bundle();
            args.putInt(ManualObjectFragment.INDEX, i);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public int getCount() {
            return mTitles.length;
        }
        
        @Override
        public CharSequence getPageTitle(int position) {
        	return mTitles[position];
        }
    }

    /**
     * The fragment object
     */
    public static class ManualObjectFragment extends Fragment {
        public static final String INDEX = "index";
        
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.manual_object, container, false);
            Bundle args = getArguments();
            int i = args.getInt(INDEX);
            ((TextView) rootView.findViewById(android.R.id.text1)).setText(
            		ManualPagerAdapter.mBodies[i]);
            ((ImageView) rootView.findViewById(R.id.image)).setImageResource(
            		ManualPagerAdapter.mImages[i]);
            return rootView;
        }
    }
}
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

import org.sudowars.R;
import org.sudowars.Controller.Local.ManualPageAdapter;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;

import com.viewpagerindicator.CirclePageIndicator;
import com.viewpagerindicator.PageIndicator;

public class Manual extends PoolBinder {
	/**
	 * the pager
	 */
    private ViewPager mPager;
    
    /**
     * the adapter for the pager
     */
    private ManualPageAdapter mAdapter;
    
    /**
     * the indicator showing the current site of the page
     */
    private PageIndicator mIndicator;
    
    /**
     * the current view
     */
    private View currentView;
    
    /**
     * the current page
     */
    private int currentPage;
    
    /*
     * (non-Javadoc)
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        
		setContentView(R.layout.manual);

        mAdapter = new ManualPageAdapter(this);
        mAdapter.parent = this;

        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(mAdapter);

        mIndicator = (CirclePageIndicator) findViewById(R.id.indicator);
        mIndicator.setViewPager(mPager);
    }
    
    /**
     * Run, when a page is changed
     * 
     * @param view the view
     * @param position the position
     */
    public void onPageChanged(View view, int position) {
        currentPage = position;
        currentView = view;
        
        findViewById(R.id.next).setVisibility(position == mAdapter.getCount() - 1 ? View.GONE : View.VISIBLE);
        
        if (currentPage == mAdapter.getCount() - 1) {
            OnClickListener done = new OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    finish();
                }
            };
            
            currentView.findViewById(R.id.manual_page_title).setOnClickListener(done);
            currentView.findViewById(R.id.manual_page_image).setOnClickListener(done);
            currentView.findViewById(R.id.manual_page_body).setOnClickListener(done);
        }
    }
}

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

import org.sudowars.R;
import org.sudowars.Controller.Local.Activity.Manual;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import com.viewpagerindicator.TitleProvider;

public class ManualPageAdapter extends PagerAdapter implements TitleProvider {
	/**
	 * the images
	 */
	private static int[] image = new int[] {
	    R.drawable.manual_image_1,
	    R.drawable.manual_image_2,
	    R.drawable.manual_image_3,
	    R.drawable.manual_image_4,
	    R.drawable.manual_image_5,
	    R.drawable.manual_image_6,
	    R.drawable.manual_image_7,
	    R.drawable.logo
	};
	
	/**
	 * the titles about the images
	 */
	private static int[] title = new int[] {
	    R.string.manual_title_1,
	    R.string.manual_title_2,
	    R.string.manual_title_3,
	    R.string.manual_title_4,
	    R.string.manual_title_5,
	    R.string.manual_title_6,
	    R.string.manual_title_7,
	    R.string.manual_title_8
	};
	
	/**
	 * the body with text about the images
	 */
	private static int[] body = new int[] {
	    R.string.manual_body_1,
	    R.string.manual_body_2,
	    R.string.manual_body_3,
	    R.string.manual_body_4,
	    R.string.manual_body_5,
	    R.string.manual_body_6,
	    R.string.manual_body_7,
	    R.string.manual_body_8
	};
	
	/**
	 * the context
	 */
	private final Context context;
	
	/**
	 * the parent
	 */
	public Manual parent;
	
	/**
	 * the constructor
	 * 
	 * @param context the context
	 */
	public ManualPageAdapter(Context context) {
		this.context = context;
	}
	
	/*
	 * (non-Javadoc)
	 * @see android.support.v4.view.PagerAdapter#setPrimaryItem(android.view.ViewGroup, int, java.lang.Object)
	 */
	@Override
	public void setPrimaryItem(ViewGroup container, int position, Object object) {
		super.setPrimaryItem(container, position, object);
		
		parent.onPageChanged(container, position);
	}
	
	/*
	 * (non-Javadoc)
	 * @see android.support.v4.view.PagerAdapter#getCount()
	 */
	@Override
	public int getCount() {
		return image.length;
	}
	
	/*
	 * (non-Javadoc)
	 * @see android.support.v4.view.PagerAdapter#instantiateItem(android.view.View, int)
	 */
	@Override
	public Object instantiateItem(final View pager, final int position) {
		LayoutInflater inflater = LayoutInflater.from(context);
		
		View pageView = inflater.inflate(R.layout.manual_page, null, true);
		pageView.setLayoutParams(new ViewGroup.LayoutParams(
				WindowManager.LayoutParams.MATCH_PARENT,
				WindowManager.LayoutParams.MATCH_PARENT));
		
		if (pageView.findViewById(R.id.manual_page_image) != null) {
			ImageView imageView = (ImageView) pageView.findViewById(R.id.manual_page_image);
		    imageView.setImageResource(image[position]);
		    
		    TextView titleView = (TextView) pageView.findViewById(R.id.manual_page_title);
		    titleView.setText(title[position]);
		    
		    TextView bodyView = (TextView) pageView.findViewById(R.id.manual_page_body);
		    bodyView.setText(body[position]);
		}
		
		((ViewPager) pager).addView(pageView, 0);
		return pageView;
	}
	
	/*
	 * (non-Javadoc)
	 * @see android.support.v4.view.PagerAdapter#destroyItem(android.view.View, int, java.lang.Object)
	 */
	@Override
	public void destroyItem(View pager, int position, Object view) {
		((ViewPager) pager).removeView((View) view);
	}
	
	/*
	 * (non-Javadoc)
	 * @see android.support.v4.view.PagerAdapter#isViewFromObject(android.view.View, java.lang.Object)
	 */
	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view.equals(object);
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.viewpagerindicator.TitleProvider#getTitle(int)
	 */
	@Override
	public String getTitle(int position) {
		return context.getString(title[position]);
	}
}

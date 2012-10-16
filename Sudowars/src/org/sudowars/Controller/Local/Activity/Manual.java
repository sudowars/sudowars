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
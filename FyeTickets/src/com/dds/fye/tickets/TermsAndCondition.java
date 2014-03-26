package com.dds.fye.tickets;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.LinearLayout;

public class TermsAndCondition extends Activity{
	LinearLayout back_linearlayout;
	WebView webView;
	String url ;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.terms_and_conditions);
		  back_linearlayout = (LinearLayout)findViewById(R.id.back_linearlayout);
		  webView = (WebView)findViewById(R.id.term_condition_webView1);
		  url = getResources().getString(R.string.term_and_condition_link);
	        Handler handler = new Handler();
	        final Runnable r = new Runnable()
	        {
	            public void run() 
	            {
	            	webView.getSettings().setLoadsImagesAutomatically(true);
	     		    webView.getSettings().setJavaScriptEnabled(true);
	     		    webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);		
	     			webView.loadUrl(url);//http://m.cellseats.com/policies
	            }
	        };
	        handler.postDelayed(r, 2);
	        back_linearlayout.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					finish();
				}
			});
	}	
}

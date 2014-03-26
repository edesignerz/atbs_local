package com.dds.fye.tickets;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;


public class WebViewFragmentActivity extends Activity{
	private WebView webView;
	private String ticketCheckOut;
	LinearLayout webPurchesPageBack ;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.purchase_web_view);
		
		String event_id_query_code = getIntent().getStringExtra("event_id_query_code");	
		String ticketId = getIntent().getStringExtra("ticketId");	
		String no_ticket = getIntent().getStringExtra("no_ticket");	
		ticketCheckOut = getIntent().getStringExtra("ticketCheckOut");	
		String ticketReceipt_begins = getIntent().getStringExtra("ticketReceipt_begins");
		
		Log.d("WebViewFragmentActivity", "no_ticket:"+no_ticket);
		
		ticketCheckOut = ticketCheckOut.replace("<event_id>", event_id_query_code);
		ticketCheckOut = ticketCheckOut.replace("<ticket_id>", ticketId);
		ticketCheckOut = ticketCheckOut.replace("<quantity>", no_ticket);
		
		/*Log.i("ArtistTeamsFragment", "event_id_query_code:"+event_id_query_code);
		Log.i("ArtistTeamsFragment", "ticketId:"+ticketId);		
		Log.i("ArtistTeamsFragment", "no_ticket:"+no_ticket);
		Log.i("ArtistTeamsFragment", "ticketCheckOut:"+ticketCheckOut);
		Log.i("ArtistTeamsFragment", "ticketReceipt_begins:"+ticketReceipt_begins);*/
		
		Log.i("ArtistTeamsFragment", "ticketCheckOut:"+ticketCheckOut);
		//&evtID=<event_id>&tgid=<ticket_id>&treq=<quantity>
		/*intent.putExtra("event_id_query_code", event_id_query_code);
		intent.putExtra("ticketId", ticketId);
		intent.putExtra("no_ticket", no_ticket);*/
		webPurchesPageBack = (LinearLayout)findViewById(R.id.web_purchase_page_back_linearlayout);
		webPurchesPageBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		webView = (WebView) findViewById(R.id.webView1);
	    webView.getSettings().setLoadsImagesAutomatically(true);
	    webView.getSettings().setJavaScriptEnabled(true);
	    webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);		
		webView.loadUrl(ticketCheckOut);
		//webView.setWebViewClient(new MyBrowser());
	}
	
	 @Override
	 public void finish() {	    			  
	    super.finish();
	 }	 
}

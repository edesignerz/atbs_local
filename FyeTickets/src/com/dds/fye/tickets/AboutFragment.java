package com.dds.fye.tickets;


import android.app.Fragment;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

public class AboutFragment extends Fragment{
	Button termAndConditionButton, FYETicketWebSiteButton, contactUsButton;
	TextView copyrightFyeTicketTextView, versionTextView;
	
	WebView webView;
	String version;
	
	 public AboutFragment(){}
     
	    @Override
	    public View onCreateView(LayoutInflater inflater, ViewGroup container,
	            Bundle savedInstanceState) {	  
	        View rootView = inflater.inflate(R.layout.about_fragment, container, false); 
	        
	        
	        webView = (WebView)rootView.findViewById(R.id.about_webView1);
	        termAndConditionButton = (Button)rootView.findViewById(R.id.terms_and_conditions_button1);
	        FYETicketWebSiteButton = (Button)rootView.findViewById(R.id.go_fye_tickets_web_site_button2);
	        contactUsButton = (Button)rootView.findViewById(R.id.contact_us_button3);
	        
	        copyrightFyeTicketTextView = (TextView)rootView.findViewById(R.id.copyright_fye_tickets_textView1);
	        versionTextView = (TextView)rootView.findViewById(R.id.app_version_textView2);
	        getVersionAndCopyRight();
	        
	        termAndConditionButton.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(getActivity(), TermsAndCondition.class);
					startActivity(intent);
				}
			});
	        FYETicketWebSiteButton.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					String webSiteLink = getResources().getString(R.string.web_site_domain_link);//"http://www.fyetickets.com"
					Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(webSiteLink));
					startActivity(browserIntent);
				}
			});
	        contactUsButton.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					//String str = android.os.Build.MODEL;
					 /*Intent	intentemail = new Intent(getActivity(), MailActivity.class);
			         startActivity(intentemail);
					*/
					try {
						   String manufacturer = "\n\nSent from my "+Build.MANUFACTURER;
						   String[] mailIdArray = getResources().getStringArray(R.array.mail_id_array);
						
				            Intent emailIntent = new Intent( Intent.ACTION_VIEW);
				            emailIntent.setClassName("com.google.android.gm", "com.google.android.gm.ComposeActivityGmail");
				            emailIntent.putExtra(Intent.EXTRA_EMAIL, mailIdArray);			           
						    emailIntent.putExtra(Intent.EXTRA_SUBJECT, "FYE Tickets");
						    emailIntent.putExtra(Intent.EXTRA_TEXT, manufacturer);
						    emailIntent.setType("message/rfc822");
				            startActivity(emailIntent );
					} catch (Exception e) {
						// TODO: handle exception
					}

					
					/*String text ="FYE Tickets APP "+ version+"\n\n"+"Sent from my "+manufacturer;
					Intent email = new Intent(Intent.CATEGORY_APP_EMAIL);
			         email.putExtra(Intent.EXTRA_EMAIL,new String[] { "sales@fyetickets.com"});//sales@fyetickets.com
			         email.putExtra(Intent.EXTRA_SUBJECT,"Comments");
			         email.putExtra(Intent.EXTRA_TEXT, text);
			         email.setType("message/rfc822");
			         startActivity(Intent.createChooser(email, "Choose an Email client :"));*/
			         //startActivity(email);
				}
			});
	        //Load fyetickets site.
	        Handler handler = new Handler();
	        final Runnable r = new Runnable()
	        {
	            public void run() 
	            {
	            	String webSiteLink = getResources().getString(R.string.web_site_domain_link);//"http://www.fyetickets.com"
	            	webView.getSettings().setLoadsImagesAutomatically(true);
	     		    webView.getSettings().setJavaScriptEnabled(true);
	     		    webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);		
	     			webView.loadUrl(webSiteLink);
	            }
	        };
	        handler.postDelayed(r, 5);
		   
	        return rootView;
	    }
	    /**
	     * Get application version name and id.
	     * 
	     */
	    private void getVersionAndCopyRight(){
	    	PackageManager manager = getActivity().getPackageManager();
	    	PackageInfo info;
			try {
				info = manager.getPackageInfo(getActivity().getPackageName(), 0);
				version = "Version " +info.versionName;
				versionTextView.setText(version);
			} catch (NameNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	    	
	    }
	    
}

package com.dds.fye.tickets.share;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

import com.dds.fye.tickets.ConnectionDetector;
import com.dds.fye.tickets.R;
import com.dds.fye.tickets.share.TwitterShare.updateTwitterStatus;

import android.app.Activity;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.IBinder;
import android.os.StrictMode;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;




public class TwitterShareActivity extends Service{
	// Constants
    /**
     * Register your here app https://dev.twitter.com/apps/new and get your
     * consumer key and secret
     **/

    // Preference Constants
    static String PREFERENCE_NAME = "twitter_oauth";
    static final String PREF_KEY_OAUTH_TOKEN = "oauth_token";
    static final String PREF_KEY_OAUTH_SECRET = "oauth_token_secret";
    static final String PREF_KEY_TWITTER_LOGIN = "isTwitterLogedIn";
 
    static final String TWITTER_CALLBACK_URL = "oauth://t4jsample";
 
    // Twitter oauth urls
    static final String URL_TWITTER_AUTH = "auth_url";
    static final String URL_TWITTER_OAUTH_VERIFIER = "oauth_verifier";
    static final String URL_TWITTER_OAUTH_TOKEN = "oauth_token";
	 	// Progress dialog
		ProgressDialog pDialog;
 
		// Twitter
		private static Twitter twitter;
		private static RequestToken requestToken;
     
		// Shared Preferences
		private static SharedPreferences mSharedPreferences;
		 // Alert Dialog Manager
	    AlertDialogManager alert = new AlertDialogManager();
	    private Activity mActivity;
	    private String mMessage = "";

	    private enum FROM {
	        TWITTER_POST, TWITTER_LOGIN
	    };

	    private enum MESSAGE {
	        SUCCESS, DUPLICATE, FAILED, CANCELLED
	    };
	    
	    public  TwitterShareActivity(Activity context){
	    	  //this.mContext = context;
	    	  mActivity = context;	    	
	    }

	    public void onTweet(String message){
	    	mMessage = message;
	        if (android.os.Build.VERSION.SDK_INT > 9) {
	            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
	            StrictMode.setThreadPolicy(policy);
	        }
	       /* cd = new ConnectionDetector(getApplicationContext());
	 
	        // Check if Internet present
	        if (!cd.isConnectingToInternet()) {
	            // Internet Connection is not present
	            alert.showAlertDialog(TwitterShare.this, "Internet Connection Error",
	                    "Please connect to working Internet connection", false);
	            // stop executing code by return
	            return;
	        }*/
	        String TWITTER_CONSUMER_KEY = mActivity.getResources().getString(R.string.twitter_appkey);
	        String TWITTER_CONSUMER_SECRET = mActivity.getResources().getString(R.string.twitter_api_secret);
	        // Check if twitter keys are set
	        if(TWITTER_CONSUMER_KEY.trim().length() == 0 || TWITTER_CONSUMER_SECRET.trim().length() == 0){
	            // Internet Connection is not present
	            alert.showAlertDialog(mActivity, "Twitter oAuth tokens", "Please set your twitter oauth tokens first!", false);
	            // stop executing code by return
	            return;
	        }
	    	// Shared Preferences
	        mSharedPreferences = mActivity.getApplicationContext().getSharedPreferences(
	                "MyPref", 0);
	    	 /** This if conditions is tested once is
	         * redirected from twitter page. Parse the uri to get oAuth
	         * Verifier
	         * */
	        Log.e("Twitter Login Error", "> " + "isTwitterLoggedInAlready():"+isTwitterLoggedInAlready()); 
	        if (!isTwitterLoggedInAlready()) {
	        	Log.e("Twitter Login Error", "> " + "if isTwitterLoggedInAlready():"+isTwitterLoggedInAlready());
	            Uri uri = mActivity.getIntent().getData();
	            if (uri != null && uri.toString().startsWith(TWITTER_CALLBACK_URL)) {
	                // oAuth verifier
	                String verifier = uri
	                        .getQueryParameter(URL_TWITTER_OAUTH_VERIFIER);
	 
	                try {
	                	  Log.e("Twitter OAuth Token", "> " + "verifier:"+verifier);
	                    // Get the access token
	                    AccessToken accessToken = twitter.getOAuthAccessToken(
	                            requestToken, verifier);
	                    Log.e("Twitter OAuth Token", "> " + "accessToken:"+accessToken);
	                    // Shared Preferences
	                    Editor e = mSharedPreferences.edit();
	 
	                    // After getting access token, access token secret
	                    // store them in application preferences
	                    e.putString(PREF_KEY_OAUTH_TOKEN, accessToken.getToken());
	                    e.putString(PREF_KEY_OAUTH_SECRET,
	                            accessToken.getTokenSecret());
	                    // Store login status - true
	                    e.putBoolean(PREF_KEY_TWITTER_LOGIN, true);
	                    e.commit(); // save changes
	 
	                    Log.e("Twitter OAuth Token", "> " + accessToken.getToken());

	                    long userID = accessToken.getUserId();
	                    User user = twitter.showUser(userID);
	                    String username = user.getName();
	                    updateStatus();
	                } catch (Exception e) {
	                    // Check log for login errors
	                    Log.e("Twitter Login Error", "> " + e.getMessage());
	                }
	            }
	        }
	        
	        loginToTwitter();    	
	    }
	    /**
	     * Function to update status
	     */
	    private void updateStatus(){
	    	  new updateTwitterStatus().execute(mMessage);
	    }
	    /**
	     * Function to login twitter
	     **/
	    private void loginToTwitter() {
	    	 Log.e("Twitter Login Error", "> " + "loginToTwitter isTwitterLoggedInAlready():"+isTwitterLoggedInAlready());
	        // Check if already logged in
	        if (!isTwitterLoggedInAlready()) {
	        	 Log.e("Twitter Login Error", "> " + "loginToTwitter if isTwitterLoggedInAlready():"+isTwitterLoggedInAlready());
	        	String TWITTER_CONSUMER_KEY = mActivity.getResources().getString(R.string.twitter_appkey);
	 	        String TWITTER_CONSUMER_SECRET = mActivity.getResources().getString(R.string.twitter_api_secret);
	            ConfigurationBuilder builder = new ConfigurationBuilder();
	            builder.setOAuthConsumerKey(TWITTER_CONSUMER_KEY);
	            builder.setOAuthConsumerSecret(TWITTER_CONSUMER_SECRET);
	            Configuration configuration = builder.build();
	             
	            TwitterFactory factory = new TwitterFactory(configuration);
	            twitter = factory.getInstance();
	 
	            try {
	                requestToken = twitter
	                        .getOAuthRequestToken(TWITTER_CALLBACK_URL);
	                this.startActivity(new Intent(Intent.ACTION_VIEW, Uri
	                        .parse(requestToken.getAuthenticationURL())));
	            } catch (TwitterException e) {
	                e.printStackTrace();
	            }
	        } else {
	            // user already logged into twitter
	            //Toast.makeText(mActivity.getApplicationContext(),
	                   // "Already Logged into twitter", Toast.LENGTH_LONG).show();
	            updateStatus();
	        }
	    }
	    
	    /**
	     * Function to update status
	     * */
	    class updateTwitterStatus extends AsyncTask<String, String, String> {
	 
	        /**
	         * Before starting background thread Show Progress Dialog
	         * */
	        @Override
	        protected void onPreExecute() {
	            super.onPreExecute();
	            pDialog = new ProgressDialog(mActivity);
	            pDialog.setMessage("Updating to twitter...");
	            pDialog.setIndeterminate(false);
	            pDialog.setCancelable(false);
	            pDialog.show();
	        }
	 
	        /**
	         * getting Places JSON
	         * */
	        protected String doInBackground(String... args) {
	            Log.d("Tweet Text", "> " + args[0]);
	            String status = args[0];
	            try {
	            	String TWITTER_CONSUMER_KEY = mActivity.getResources().getString(R.string.twitter_appkey);
	     	        String TWITTER_CONSUMER_SECRET = mActivity.getResources().getString(R.string.twitter_api_secret);
	                ConfigurationBuilder builder = new ConfigurationBuilder();
	                builder.setOAuthConsumerKey(TWITTER_CONSUMER_KEY);
	                builder.setOAuthConsumerSecret(TWITTER_CONSUMER_SECRET);
	                 
	                // Access Token 
	                String access_token = mSharedPreferences.getString(PREF_KEY_OAUTH_TOKEN, "");
	                // Access Token Secret
	                String access_token_secret = mSharedPreferences.getString(PREF_KEY_OAUTH_SECRET, "");
	                 
	                AccessToken accessToken = new AccessToken(access_token, access_token_secret);
	                Twitter twitter = new TwitterFactory(builder.build()).getInstance(accessToken);
	                 
	                // Update status
	                twitter4j.Status response = twitter.updateStatus(status);
	                 
	                Log.d("Status", "> " + response.getText());
	            } catch (TwitterException e) {
	                // Error in updating status
	                Log.d("Twitter Update Error", e.getMessage());
	            }
	            return null;
	        }
	 
	        /**
	         * After completing background task Dismiss the progress dialog and show
	         * the data in UI Always use runOnUiThread(new Runnable()) to update UI
	         * from background thread, otherwise you will get error
	         * **/
	        protected void onPostExecute(String file_url) {
	            // dismiss the dialog after getting all products
	            pDialog.dismiss();	                                       
	            // updating UI from Background Thread                
                  mActivity.runOnUiThread(new Runnable() {
	                @Override
	                public void run() {
	                    Toast.makeText(mActivity,
	                            "Post tweeted successfully", Toast.LENGTH_SHORT)
	                            .show();
	                    
	                }
	            });
	           
	        }
	 
	    }
	    /**
	     * Check user already logged in your application using twitter Login flag is
	     * fetched from Shared Preferences
	     * */
	    private boolean isTwitterLoggedInAlready() {
	        // return twitter login status from Shared Preferences
	        return mSharedPreferences.getBoolean(PREF_KEY_TWITTER_LOGIN, false);
	    }

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
		
}
 



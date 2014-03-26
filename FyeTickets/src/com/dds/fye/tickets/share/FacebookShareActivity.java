package com.dds.fye.tickets.share;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.location.Location;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dds.fye.tickets.R;
import com.facebook.*;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;
import com.facebook.model.GraphObject;
import com.facebook.model.GraphPlace;
import com.facebook.model.GraphUser;
import com.facebook.model.OpenGraphAction;
import com.facebook.widget.*;
import com.facebook.widget.WebDialog.OnCompleteListener;

import java.util.*;

import org.json.JSONException;
import org.json.JSONObject;
public class FacebookShareActivity extends Activity{
	 // LoginButton liLoginButton;
   // ImageView shareImageView;
    
    Handler mHandler;

    
    String shareMessage ;
	String playStoreLink ;
	String webSiteLink;
	String webLogoShare ;
	
	Session.StatusCallback statusCallback = new SessionStatusCallback();
	List<String> permissions;
	ProgressDialog dialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

        //setContentView(R.layout.facebook_view);
        
         shareMessage = getIntent().getStringExtra("shareMessage");
		 playStoreLink = getIntent().getStringExtra("playStoreLink");
		 webSiteLink = getIntent().getStringExtra("webSiteLink");
		 webLogoShare = getIntent().getStringExtra("webLogoShare");
      
        //shareImageView = (ImageView)findViewById(R.id.facebook_share_imageView1);  
        
		/**
		 * Facebook Permissions
		 */
		
		permissions = new ArrayList<String>();
		permissions.add("publish_actions");
		
		/** End */
		
		/*shareImageView.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				publishStory();
			}
		});*/
		
		/**
		 * Facebook Session Initialization
		 */
		Session session = Session.getActiveSession();
		if(session == null) {
			if(savedInstanceState != null) {
				session = Session.restoreSession(this, null, statusCallback, savedInstanceState);
			}
			if(session == null) {
				session = new Session(this);
			}
			session.addCallback(statusCallback);
			Session.setActiveSession(session);
		}
		Log.d("FbShare", "Session State - " + session.getState());
		
		/** End **/
		/**
		 * Share Message
		 */
		publishStory();
    }
    /**
	 * Facebook Methods.
	 * Publish message. 
	 */
	private void publishStory() {
 
		Session session = Session.getActiveSession();
		if(session != null && session.getState().isOpened()) {
				checkSessionAndPost();
		} else {
			
			Log.d("FbShare", "Session is null");
			session = new Session(FacebookShareActivity.this);
			Session.setActiveSession(session);
			session.addCallback(statusCallback);
			
			Log.d("FbShare", "Session info - " + session);
			try {
				Log.d("FbShare", "Opening session for read");
				session.openForRead(new Session.OpenRequest(FacebookShareActivity.this));
			} catch(UnsupportedOperationException exception) {
				exception.printStackTrace();
				Log.d("FbShare", "Exception Caught");
				Toast.makeText(FacebookShareActivity.this, "Unable to post your events on facebook", Toast.LENGTH_LONG).show();
			}
		}
	}
	private void checkSessionAndPost (){
		 
		Session session = Session.getActiveSession();
		session.addCallback(statusCallback);
		Log.d("FbShare", "Session Permissions Are - " + session.getPermissions());
			
		if(session.getPermissions().contains("publish_actions")) {
			publishAction(session);
		} else {
			session.requestNewPublishPermissions(new Session.NewPermissionsRequest(FacebookShareActivity.this, permissions));
		} 
	}
	private void publishAction(Session session) {
		 
		Log.d("FbShare", "Inside publishAction()");
		dialog = new ProgressDialog(FacebookShareActivity.this);
		dialog.setMessage("Please wait...Posting the status");
		dialog.show();
		Bundle postParams = new Bundle();
		postParams.putString("name", "FYE Tickets");
		postParams.putString("caption", webLogoShare);
		postParams.putString("description", playStoreLink);
		postParams.putString("link", webSiteLink);
		postParams.putString("message", shareMessage);
 
		Request.Callback callback = new Request.Callback() {
 
			@Override
			public void onCompleted(Response response) {
				dialog.dismiss();
				FacebookRequestError error = response.getError();
				if(error != null) {
					Log.d("FbShare", "Facebook error - " + error.getErrorMessage());
					Log.d("FbShare", "Error code - " + error.getErrorCode());
					Log.d("FbShare", "JSON Response - " + error.getRequestResult());
					Log.d("FbShare", "Error Category - " + error.getCategory());
					Toast.makeText(FacebookShareActivity.this, "Failed to share the post.Please try again", Toast.LENGTH_SHORT).show();
					//shareImageView.setEnabled(true);
					finish();
				} else {
					Toast.makeText(FacebookShareActivity.this, "Successfully shared.", Toast.LENGTH_SHORT).show();
					//shareImageView.setEnabled(false);
					finish();
				}
			}
		};
 
		Request request = new Request(session, "me/feed", postParams, HttpMethod.POST, callback);
 
		RequestAsyncTask asyncTask = new RequestAsyncTask(request);
		asyncTask.execute();
	}
	
	private class SessionStatusCallback implements Session.StatusCallback {
		 
		@Override
		public void call(Session session, SessionState state, Exception exception) {
			//Check if Session is Opened or not, if open & clicked on share button publish the story
			if(session != null && state.isOpened()) {
				Log.d("FbShare", "Session is opened");
				if(session.getPermissions().contains("publish_actions")) {
					Log.d("FbShare", "Starting share");
					publishAction(session);
				} else {
					Log.d("FbShare", "Session dont have permissions");
					publishStory();
				}
			} else {
				Log.d("FbShare", "Invalid fb Session");
			}
		}
 
	}
	
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("FbShare", "Result Code is - " + resultCode +"");
		Session.getActiveSession().addCallback(statusCallback);
		Session.getActiveSession().onActivityResult(FacebookShareActivity.this, requestCode, resultCode, data);
    }

    @Override
    public void onResume() {
        super.onResume();
        AppEventsLogger.activateApp(this);
        //uiHelper.onResume();
    }
     
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //outState.putBoolean(PENDING_PUBLISH_KEY, pendingPublishReauthorization);
       // uiHelper.onSaveInstanceState(outState);
		super.onSaveInstanceState(outState);
		Session session = Session.getActiveSession();
		Session.saveSession(session, outState);
    }
     
    @Override
    public void onPause() {
        super.onPause();
       // uiHelper.onPause();
    }
     
    @Override
    public void onDestroy() {
        super.onDestroy();
       // uiHelper.onDestroy();
    }
    
	@Override
	protected void onStart() {
		// TODO Add status callback
		super.onStart();
		Session.getActiveSession().addCallback(statusCallback);
	}
	
	@Override
	protected void onStop() {
		// TODO Remove callback
		super.onStop();
		Session.getActiveSession().removeCallback(statusCallback);
	}
}

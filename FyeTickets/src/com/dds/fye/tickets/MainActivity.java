package com.dds.fye.tickets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.dds.fye.database.DatabaseHandler;
import com.dds.fye.tickets.adapter.CustomSpinnerAdapter;
import com.dds.fye.tickets.adapter.LocationSearchAdapter;
import com.dds.fye.tickets.model.SpinnerModel;
import com.dds.fye.tickets.share.FacebookShareActivity;
import com.dds.fye.tickets.share.TwitterShare;
import com.dds.fye.tickets.share.TwitterShareActivity;

import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.LabeledIntent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.TextUtils.TruncateAt;
import android.text.style.URLSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.ShareActionProvider;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements LocationListener {
    public int pos = -1;   
    private int positionFlag = 0;
    private SharedPreferences preferences ;
    private LinearLayout linearLayout;
    // RelativeLayout relativeLayout;
    private TextView spinnerTitleTextView,main_location_textView2;
    private ImageView filterImageView, searchImageView, locationsearchImageView, shareImageView;
    //The "x" and "y" position of the "Show Button" on screen.
    //Point p;
    private PopupWindow popup;
    private String radiusRange_ = "";  
    private GPSTracker  gps;
    public  ArrayList<SpinnerModel> CustomListViewValuesArr = new ArrayList<SpinnerModel>();
    boolean searchlocationFlag = false;
    //boolean stopHideFragment = false;
    // int count = 0;
    private ArrayList<Integer> counter = new ArrayList<Integer>();
    // FrameLayout fram_cont;
   
    private Fragment fragment1,fragment2,fragment3,fragment4,fragment5;
    private Fragment secondFragment;
    private FragmentManager fragmentManager = getFragmentManager();
    
    private ArrayList<Fragment> loadedFragmentArrayList = new ArrayList<Fragment>();
   
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		 DatabaseHandler db = new DatabaseHandler(this);
		// db.onUpgradDatabase();
		// create class object
		gps = new GPSTracker(this);
		getCurrentLocationName();
		//
		preferences = PreferenceManager.getDefaultSharedPreferences(this);	
		//fram_cont = (FrameLayout)findViewById(R.id.frame_container);
		linearLayout = (LinearLayout)findViewById(R.id.custom_spinner_layout);
		linearLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//applicationlanguageSetting();				
				 //Open popup window
				if (popup != null) {
					popup.dismiss();
				}
			       //if (p != null)
			       showPopup(MainActivity.this);
			}
		});
		spinnerTitleTextView = (TextView)findViewById(R.id.spinner_title_textView2);
		main_location_textView2 = (TextView)findViewById(R.id.main_location_textView2);
		filterImageView = (ImageView)findViewById(R.id.event_filter_imageView3);
		searchImageView = (ImageView)findViewById(R.id.events_search_imageView2);
		locationsearchImageView = (ImageView)findViewById(R.id.events_location_search_imageView3);
		shareImageView = (ImageView)findViewById(R.id.event_share_buton_imageView2);
		//Get current location name.
		changeCurrentLoaction();
        
		//if (savedInstanceState == null) {
            // on first time display view for first nav item
       //     displayView(0);
       // }
		
		//Share message social network. 
		shareImageView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				onShareSocialNetwork();
				//Intent	intent = new Intent(MainActivity.this, ShareActivity.class);
				 //Intent	intent = new Intent(MainActivity.this, FacebookShareActivity.class);
			   // Intent	intent = new Intent(MainActivity.this, TwitterShareActivity.class);
			   // Intent	intent = new Intent(MainActivity.this, MailActivity.class);
		         //startActivity(intent);
				/*String tweetUrl = "https://twitter.com/intent/tweet?text=Search";				
				Uri uri = Uri.parse(tweetUrl);
				startActivity(new Intent(Intent.ACTION_SENDTO, uri));*/
			   /* Resources resources = getResources();

			    Intent emailIntent = new Intent();
			    emailIntent.setAction(Intent.ACTION_SEND);
			    // Native email client doesn't currently support HTML, but it doesn't hurt to try in case they fix it
			    emailIntent.putExtra(Intent.EXTRA_TEXT, Html.fromHtml("hello"));
			    emailIntent.putExtra(Intent.EXTRA_SUBJECT, "hi");
			    emailIntent.setType("message/rfc822");

			    PackageManager pm = getPackageManager();
			    Intent sendIntent = new Intent(Intent.ACTION_SEND);     
			    sendIntent.setType("text/plain");


			    Intent openInChooser = Intent.createChooser(emailIntent, "Share on:");
			    

			    List<ResolveInfo> resInfo = pm.queryIntentActivities(sendIntent, 0);
			    List<LabeledIntent> intentList = new ArrayList<LabeledIntent>();        
			    for (int i = 0; i < resInfo.size(); i++) {
			        // Extract the label, append it, and repackage it in a LabeledIntent
			        ResolveInfo ri = resInfo.get(i);
			        String packageName = ri.activityInfo.packageName;
			        if(packageName.contains("android.email")) {
			            emailIntent.setPackage(packageName);
			        } else if(packageName.contains("twitter") || packageName.contains("facebook") || 
			        		packageName.contains("mms") || packageName.contains("android.gm")) {
			            Intent intent = new Intent();
			            intent.setComponent(new ComponentName(packageName, ri.activityInfo.name));
			            intent.setAction(Intent.ACTION_SEND);
			            intent.setType("text/plain");
			            if(packageName.contains("twitter")) {
			                intent.putExtra(Intent.EXTRA_TEXT,"twitter");
			            } else if(packageName.contains("facebook")) {
			                // Warning: Facebook IGNORES our text. They say "These fields are intended for users to express themselves. Pre-filling these fields erodes the authenticity of the user voice."
			                // One workaround is to use the Facebook SDK to post, but that doesn't allow the user to choose how they want to share. We can also make a custom landing page, and the link
			                // will show the <meta content ="..."> text from that page with our link in Facebook.
			                intent.putExtra(Intent.EXTRA_TEXT, "facebook");
			            } else if(packageName.contains("mms")) {
			                intent.putExtra(Intent.EXTRA_TEXT, "mms");
			            } else if(packageName.contains("android.gm")) {
			                intent.putExtra(Intent.EXTRA_TEXT, Html.fromHtml("gmail"));
			                intent.putExtra(Intent.EXTRA_SUBJECT, "gm");               
			                intent.setType("message/rfc822");			               
			            }
			            
			            intentList.add(new LabeledIntent(intent, packageName, ri.loadLabel(pm), ri.icon));
			        }
			    }

			    // convert intentList to array
			    LabeledIntent[] extraIntents = intentList.toArray( new LabeledIntent[ intentList.size() ]);

			    openInChooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, extraIntents);
			    startActivity(openInChooser); */
			}
		});
		filterImageView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					Log.d("ArtistTeamsFragment", "accessActionBarView()eventTheatreImageView:");					
					//Call the events filter pop.
					accessActionBarView();
				}
			}); 
		searchImageView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent ;
				searchlocationFlag = false;
				switch (positionFlag) {
				case 0:					
					intent = new Intent(MainActivity.this, SearchFragmentActivity.class);
			        startActivity(intent);
					break;
				case 1:
					intent = new Intent(MainActivity.this, VenueSearchFragmentActivity.class);
			        startActivity(intent);
					break;
				case 2:	
					//List<EventsData> artistDataList = ((ArtistTeamsFragment) fragment3).getArtistTeamsData();
					//Bundle bundle = new Bundle();
					//bundle.putSerializable("artists_data", artistDataList);
					//bundle.putSerializable("artists_data", (Serializable) artistDataList);
					intent = new Intent(MainActivity.this, PerformerSearchFragmentActivity.class);	
					//intent.putExtras(bundle);
			        startActivity(intent);
					break;

				default:
					break;
				}
						
			}
		}); 
		 locationsearchImageView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					/*FragmentManager fm = getFragmentManager();
					MyAlertDialogWIndow alert = new MyAlertDialogWIndow();
		      		alert.show(fm, "Alert_Dialog");*/
					searchlocationFlag = true;
		      		Intent intent = new Intent(MainActivity.this, SearchLocation.class);
			        startActivity(intent);
				}
		 });
		 fragment1 = new EventsFragment();
		 fragment2 = new VenuesFragment();  
		 fragment3 = new ArtistTeamsFragment();
		 fragment4 = new FavoritesFragment();
		 fragment5 = new AboutFragment();
       displayView(positionFlag);	     
	}

    
    @Override
    public void onResume(){
    	super.onResume();
    	Log.d("MainActivity", "onResume :"+searchlocationFlag);
    	getCurrentLocationName();
    	if (searchlocationFlag) {
    		Log.d("MainActivity", "onResume searchlocationFlag:"+searchlocationFlag);
    		searchlocationFlag = false;
    		
    		counter.clear();
    		counter.add(0);
    		counter.add(1);
    		counter.add(2);
    		switch (positionFlag) {
			case 0:
				int index = counter.indexOf(positionFlag);
				if (index != -1) {
					counter.remove(index);
					((EventsFragment) fragment1).getVenues();
				}
				break;
			case 1:
				int index_ = counter.indexOf(positionFlag);
				if (index_!= -1) {
					counter.remove(index_);
					((VenuesFragment) fragment2).getVenues();
				}

				break;
			case 2:
				int index_1 = counter.indexOf(positionFlag);
				if (index_1 != -1) {
					counter.remove(index_1);
					((ArtistTeamsFragment) fragment3). getArtistTeams();
				}

				break;
			case 3:
				
				break;
			case 4:
				
				break;
			}
    		 String selectedCity = preferences.getString("selectcity", "");
    		 main_location_textView2.setText(selectedCity);
    		callBackClass();
		}
    }
	
	 /****** Function to set data in ArrayList *************/
  /**
   * Call back refresh.
   * 
   */
   public void callBackClass(){
   	displayView(positionFlag);
   	//changeCurrentLoaction();
   }
   /**
    * Refresh.
    * Load data by current location and change location.
    */
   public void changeCurrentLoaction(){
   	// makeArrayList
        String selectedCity = preferences.getString("selectcity", "");
        if (selectedCity.equals("")) {
       	 selectedCity =  preferences.getString("user_current_location", "Location");
       	 Log.d("MainActivity", "changeLoaction selectedCity:"+selectedCity);
       	 if (!selectedCity.equals("Location")) {
           	 SharedPreferences.Editor editor = preferences.edit();
   	    	 editor.putString("selectcity",selectedCity);	        		
   	    	 editor.commit();
   	    	 
   	    		switch (positionFlag) {
   				case 0:
   					//int index = counter.indexOf(positionFlag);
   					//counter.remove(index);
   					((EventsFragment) fragment1).getVenues();
   					break;
   				case 1:
   					//int index_ = counter.indexOf(positionFlag);
   					//counter.remove(index_);
   					((VenuesFragment) fragment2).getVenues();
   					break;
   				case 2:
   					//int index_1 = counter.indexOf(positionFlag);
   					//counter.remove(index_1);
   					((ArtistTeamsFragment) fragment3). getArtistTeams();
   					break;
   				}
			 }
		 }
        Log.d("MainActivity", "changeLoaction selectedCity:"+selectedCity);
        main_location_textView2.setText(selectedCity);
    }
    /**
     * Get current location.
     */
    private void getCurrentLocationName(){
     //Get location	
     // check if GPS enabled     
     if(gps.canGetLocation()){
    	 Location currentLocation = gps.getLocation();
         double latitude = gps.getLatitude();
         double longitude = gps.getLongitude();
         Log.d("ArtistTeamsFragment", "currentLocation:"+currentLocation);
         if (currentLocation != null) {
        	 (new MainActivity.GetAddressTask(this)).execute(latitude,longitude);
		 }   
         
         // \n is for new line
        // Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();    
     }else{
         // can't get location
         // GPS or Network is not enabled
         // Ask user to enable GPS/network in settings
         gps.showSettingsAlert();
     }  
    }
    
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	/**
	 * 
     * Load selected fragment class view.
     * Refresh data.
     **/
    public void displayView(int position) {
    	 Log.d("MainActivity", "Error in creating position:"+position);
    	// update the main content by replacing fragments
    	
        //Fragment fragment = null; 
       // FragmentManager fragmentManager = getFragmentManager();  
    	switch (position) {
    	    case 0:   
    	    	//Events 
    	    	positionFlag = position;
    	    	//fragment = new EventsFragment();
    	    	//fragment = fragment1;
    	    	if (secondFragment != null) {
    	    		//if (!stopHideFragment) {
        	    		Log.d("MainActivity", " fragment1 null not:");
        	    		fragmentManager.beginTransaction().hide(secondFragment).commit();	
        	    		Log.d("MainActivity", "after fragment1 null not:");
					//}									
				} 
    	    	
    	    	try {
        	    	//Log.d("MainActivity", "fragmentManager.getBackStackEntryCount():"+fragmentManager.getBackStackEntryCount());
        	    	//Log.d("MainActivity", "fragmentManager.getBackStackEntryCount():"+fragmentManager.getBackStackEntryAt(position).getId());          	    	
    	    		int fragIndex = loadedFragmentArrayList.indexOf(fragment1);
    	    		Log.d("MainActivity", "fragIndex:"+fragIndex);
    	    		if (fragIndex != -1) {
    	    			fragmentManager.beginTransaction().show(fragment1).commit();
    	    	    	if (counter.size() != 0) {
    	    	    		int index_1 = counter.indexOf(positionFlag);
    	    	    		if (index_1!=-1) {
    	    	    			counter.remove(index_1);
    	        	    		((EventsFragment) fragment1).getVenues();	
    						}  				
    					}else{
    						Log.d("MainActivity", "fragIndex:"+fragIndex);
   	    	    	 		//((EventsFragment) fragment3).dataList();
   	    	    	 	}
					}else{
						fragmentManager.beginTransaction()
						.add(R.id.frame_container, fragment1)
						.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
						.show(fragment1)
						.commit();
						loadedFragmentArrayList.add(fragment1);
   	    	    	 	if (counter.size() != 0) {
   	    	    	 		int index_1 = counter.indexOf(positionFlag);
   	    	    	 		if (index_1!=-1) {
   	    	   	    	 		counter.remove(index_1);  	    	   	    	 		
   	    	    	 		}
   	    	    	 	}
					}
        	    	
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
	    		
    	    	Log.d("MainActivity", "after after 2 fragment1 null not :");
	
    	    	Log.d("MainActivity", "after after 3 fragment1 null not :");
    	    	secondFragment = fragment1;   	    	
    	    	Log.d("MainActivity", "after after 4 fragment1 null not :");
     	    	spinnerTitleTextView.setText("Events");
     	    	//linearLayout.setMinimumWidth(100);
     	    	//relativeLayout.setBackgroundColor(R.color.deep_blue);
             	filterImageView.setVisibility(View.VISIBLE);
             	searchImageView.setVisibility(View.VISIBLE);
             	locationsearchImageView.setVisibility(View.VISIBLE);
             	shareImageView.setVisibility(View.VISIBLE);
             	Log.d("MainActivity", "after after 5 fragment1 null not :");
                break;
            case 1:
            	//Venues
            	positionFlag = position;
   	    	 	//fragment = new VenuesFragment();  
   	    	 	//fragment = fragment2;
   	    	 	if (secondFragment != null) {
   	    	 		//if (!stopHideFragment) {
   	   	    	 	    Log.d("ArtistTeamsFragment", " fragment2 null not:");
    	    	 		fragmentManager.beginTransaction().hide(secondFragment).commit();
   	    	 		//}
   	    	 	}
   	    	 	
   	    	 	try {
   	    	 		
   	    	 	    int fragIndex = loadedFragmentArrayList.indexOf(fragment2);
   	    	 	    Log.d("MainActivity", "fragIndex:"+fragIndex);
   	    	 	    if (fragIndex != -1) {
   	    	 	    	fragmentManager.beginTransaction().show(fragment2).commit();
   	    	    	 	if (counter.size() != 0) {
   	    	    	 		int index_1 = counter.indexOf(positionFlag);
   	    	    	 		if (index_1!=-1) {
   	    	   	    	 		counter.remove(index_1);
   	    	   	    	 		((VenuesFragment) fragment2).getVenues();
   	    	    	 		}
   	    	    	 	}else{
   	    	    	 		//((VenuesFragment) fragment3).dataList();
   	    	    	 	}
   	    	 	    }else{
   	    	 	    	fragmentManager.beginTransaction()
   	    	 	    	.add(R.id.frame_container, fragment2)
   	    	 	    	.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
   	    	 	    	.show(fragment2)
   	    	 	    	.commit();
   	    	 	    	loadedFragmentArrayList.add(fragment2);
   	    	    	 	if (counter.size() != 0) {
   	    	    	 		int index_1 = counter.indexOf(positionFlag);
   	    	    	 		if (index_1!=-1) {
   	    	   	    	 		counter.remove(index_1);  	    	   	    	 		
   	    	    	 		}
   	    	    	 	}
   	    	 	    }
   	    	 		
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}

   	    	 	//linearLayout.setMinimumWidth(100);
   	    	 	secondFragment = fragment2;
            	spinnerTitleTextView.setText("Venues");
            	filterImageView.setVisibility(View.GONE);
            	searchImageView.setVisibility(View.VISIBLE);
            	locationsearchImageView.setVisibility(View.GONE);
            	shareImageView.setVisibility(View.VISIBLE);
                break;
            case 2:
            	//Artist / Teams
            	positionFlag = position;
            	//fragment = new ArtistTeamsFragment();
            	//fragment = fragment3;
            	if (secondFragment != null) {
            		//if (!stopHideFragment) {
         	    	 	Log.d("ArtistTeamsFragment", " fragment2 null not:");
       	    	 		fragmentManager.beginTransaction().hide(secondFragment).commit();
            		//}
      	    	}
            	
   	    	 	try {
   	    	 		Log.d("MainActivity", "fragmentManager.getBackStackEntryCount():"+fragmentManager.getBackStackEntryCount());
   	    	 	    int fragIndex = loadedFragmentArrayList.indexOf(fragment3);
   	    	 	    if (fragIndex != -1) {
   	    	 	    	fragmentManager.beginTransaction().show(fragment3).commit();
   	    	    	 	if (counter.size() != 0) {
   	    	    	 		int index_1 = counter.indexOf(positionFlag);
   	    	    	 		if (index_1!=-1) {
   	    	   	    	 		counter.remove(index_1);
   	    	   	    	 		((ArtistTeamsFragment) fragment3). getArtistTeams();
   	    	    	 		}
   	    	    	 	}else{	    	    	 		
   	    	    	 	}
   	    	 	    }else{
   	    	 	    	fragmentManager.beginTransaction()
   	    	 	    	.add(R.id.frame_container, fragment3)
   	    	 	    	.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
   	    	 	    	.show(fragment3)
   	    	 	    	.commit();
   	    	 	    	loadedFragmentArrayList.add(fragment3);
   	    	 	    	if (counter.size() != 0) {
	    	    	 		int index_1 = counter.indexOf(positionFlag);
	    	    	 		if (index_1!=-1) {
	    	   	    	 		counter.remove(index_1);  	    	   	    	 		
	    	    	 		}
	    	    	 	}
   	    	 	    }
   	            
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}

            	secondFragment = fragment3;
            	//linearLayout.setMinimumWidth(100);
            	spinnerTitleTextView.setText("Artist/Teams");
            	//relativeLayout.setBackgroundColor(R.color.white);
            	filterImageView.setVisibility(View.GONE);
            	searchImageView.setVisibility(View.VISIBLE);
            	locationsearchImageView.setVisibility(View.GONE);
            	shareImageView.setVisibility(View.VISIBLE);
                break;
            case 3:
            	//Favorites
            	positionFlag = position;
            	//fragment = new FavoritesFragment();
            	//fragment = fragment4;
            	if (secondFragment != null) {
            		//if (!stopHideFragment) {
        	    	 	Log.d("MainActivity", " fragment4 null not:");
      	    	 		fragmentManager.beginTransaction().hide(secondFragment).commit();
            		//}
     	    	}
       	
   	    	 	try {
   	    	 		Log.d("MainActivity", "fragmentManager.getBackStackEntryCount():"+fragmentManager.getBackStackEntryCount());
   	    	 	    int fragIndex = loadedFragmentArrayList.indexOf(fragment4);
   	    	 	    if (fragIndex != -1) {
   	    	 	        Log.d("MainActivity", " fragment4 refres:");
   	    	 	        ((FavoritesFragment) fragment4).getAllData();
   	    	 	    	fragmentManager.beginTransaction().show(fragment4).commit();   	    	 	         	    	 	        
   	    	 	    }else{
   	    	 	    	fragmentManager.beginTransaction()
   	    	 	    	.add(R.id.frame_container, fragment4)
   	    	 	    	.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
   	    	 	    	.show(fragment4)
   	    	 	    	.commit();
   	    	 	    	loadedFragmentArrayList.add(fragment4);
   	    	 	    }
   	    	 		
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
            	secondFragment = fragment4;
            	//linearLayout.setMinimumWidth(100);
            	filterImageView.setVisibility(View.GONE);
            	searchImageView.setVisibility(View.GONE);
            	locationsearchImageView.setVisibility(View.GONE);
            	shareImageView.setVisibility(View.GONE);
            	spinnerTitleTextView.setText("Favorites");
                break;
            case 4:
            	//About
            	positionFlag = position;
            	//fragment = new AboutFragment();
            	//fragment = fragment5;
            	if (secondFragment != null) {
            		//if (!stopHideFragment) {
            			Log.d("ArtistTeamsFragment", " fragment2 null not:");
    	    	 		fragmentManager.beginTransaction().hide(secondFragment).commit();
            		//}  	    	 	 
    	    	}

   	    	 	try {
   	    	 		Log.d("MainActivity", "fragmentManager.getBackStackEntryCount():"+fragmentManager.getBackStackEntryCount());
   	    	 	    int fragIndex = loadedFragmentArrayList.indexOf(fragment5);
   	    	 	    if (fragIndex != -1) {
   	    	 	    	fragmentManager.beginTransaction().show(fragment5).commit();
   	    	 	    }else{
   	    	 	    	fragmentManager.beginTransaction()
   	    	 	    	.add(R.id.frame_container, fragment5)
   	    	 	    	.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
   	    	 	    	.show(fragment5)
   	    	 	    	.commit();
   	    	 	    	loadedFragmentArrayList.add(fragment5);  	    	 	    	
   	    	 	    }
   	    	 		
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
            	secondFragment = fragment5;
            	//linearLayout.setMinimumWidth(100);
            	filterImageView.setVisibility(View.GONE);
            	searchImageView.setVisibility(View.GONE);
            	locationsearchImageView.setVisibility(View.GONE);
            	shareImageView.setVisibility(View.GONE);
            	spinnerTitleTextView.setText("About");
                break;
            case 5:
            	//Location
            	searchlocationFlag = true;
	      		Intent intent = new Intent(MainActivity.this, SearchLocation.class);
		        startActivity(intent);
            default:
                break;
    	 }
    }

  	// The method that displays the popup.
    /**
     * Main page menu string is the string that initially show in spinner.
     * @param context
     */
  	private void showPopup(final Activity context) {
  		 
  		  LinearLayout linearLauout = (LinearLayout)findViewById(R.id.relativeLayout1);

  		 Log.d("Log", "Height: " + "+linearLauout.getHeight:"+linearLauout.getHeight());
  		// We need to get the instance of the LayoutInflater
  		// Log.d("MainActivity", "Error in creating p:"+p);
  		 LinearLayout viewGroup = (LinearLayout) context.findViewById(R.id.popup);
  		 LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
  		 View layout = inflater.inflate(R.layout.spinner_listview, viewGroup);
  		 popup = new PopupWindow(layout, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, true);//350, 400
  		 popup.setBackgroundDrawable(new BitmapDrawable());
  		 popup.showAtLocation(layout, Gravity.LEFT|Gravity.TOP, 0, linearLauout.getHeight()+15);

  		String selectedCity = preferences.getString("selectcity", "");
        if (selectedCity.equals("")) {
       	 selectedCity =  preferences.getString("user_current_location", "Location");
		}
        
  		 String[] data = getResources().getStringArray(R.array.fye_menu_array);
  		 data[5] = selectedCity;
  		 ListView listView = (ListView)layout.findViewById(R.id.spinner_listView1);
  		 CustomSpinnerAdapter adapter = new CustomSpinnerAdapter(this, data);
			//final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
	               // R.array.fye_menu_array, android.R.layout.simple_spinner_item);
			listView.setAdapter(adapter);
			listView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					// TODO Auto-generated method stub
					  popup.dismiss();
					  if (positionFlag != arg2) {
						  displayView(arg2);
					  }
					 
					  Log.d("MainActivity", "Error in creating arg2:"+arg2);			   		 
				}
			});
  		}
  		
  		 /**
  	     * A subclass of AsyncTask that calls getFromLocation() in the
  	     * background. The class definition has these generic types:
  	     * Location - A Location object containing
  	     * the current location.
  	     * Void     - indicates that progress units are not used
  	     * String   - An address passed to onPostExecute()
  	     */
  	     private class GetAddressTask extends
  	             AsyncTask<Double, Void, String> {
  	         Context mContext;
  	         public GetAddressTask(Context context) {
  	             super();
  	             mContext = context;
  	         }

  	         /**
  	          * Get a Geocoder instance, get the latitude and longitude
  	          * look up the address, and return it
  	          *
  	          * @params params One or more Location objects
  	          * @return A string containing the address of the current
  	          * location, or an empty string if no address can be found,
  	          * or an error message
  	          */
  	         @Override
  	         protected String doInBackground(Double... params) {
  	             Geocoder geocoder =
  	                     new Geocoder(mContext, Locale.getDefault());
  	             // Get the current location from the input parameter list
  	            //Location loc = params[0];
  	             // Create a list to contain the result address
  	             List<Address> addresses = null;
  	             try {
  	                 /*
  	                  * Return 1 address.
  	                  */
  	                 addresses = geocoder.getFromLocation(params[0],
  	                		params[1], 1);
  	             } catch (IOException e1) {
  	             Log.e("LocationSampleActivity",
  	                     "IO Exception in getFromLocation()");
  	             e1.printStackTrace();
  	             return ("Location");
  	             } catch (IllegalArgumentException e2) {
  	             // Error message to post in the log
  	             String errorString = "Illegal arguments " +
  	                     Double.toString(params[0]) +
  	                     " , " +
  	                     Double.toString(params[1]) +
  	                     " passed to address service";
  	             Log.e("LocationSampleActivity", errorString);
  	             e2.printStackTrace();
  	             return "Location";
  	             }
  	            // Log.i("LocationSampleActivity", "addresses:"+addresses);
  	             // If the reverse geocode returned an address
  	             if (addresses != null && addresses.size() > 0) {
  	            	
  	                 // Get the first address
  	                 Address address = addresses.get(0);
  	                // Log.i("LocationSampleActivity", "address:"+address);
  	                 /*
  	                  * Format the first line of address (if available),
  	                  * city, and country name.
  	                  */
  	                 String addressText = String.format(
  	                         "%s",
  	                         // If there's a street address, add it
  	                        // address.getMaxAddressLineIndex() > 0 ?
  	                                // address.getAddressLine(0) : "",
  	                         //address.getAdminArea(),
  	                         // Locality is usually a city
  	                         address.getLocality());
  	                         // The country of the address
  	                         //address.getCountryName());
  	                 
  	                 //Log.e("LocationSampleActivity", addressText);
  	                 // Return the text
  	                 return addressText;
  	             } else {
  	                 return "Location";
  	             }
  	         }
  	         @Override
  	         protected void onPostExecute(String address) {
  	             // Set activity indicator visibility to "gone"            
  	             // Display the results of the lookup.
  	        	  Log.e("LocationSampleActivity", "address:"+address);
  	        	 // Log.e("LocationSampleActivity", "navDrawerItems.get(5):"+navDrawerItems.get(5));
  	        	 // if (!address.equals("Location")){
  	  	        	  SharedPreferences.Editor editor = preferences.edit();
  	  	      		  editor.putString("user_current_location",address);	        		
  	  	      		  editor.commit();
  	  	      		  
  	  	      		  changeCurrentLoaction();
  	        	 // } 	        	  
  	         }
  	     }
  	    
  		@Override
  		public void onLocationChanged(Location arg0) {
  			// TODO Auto-generated method stub
  			
  		}
  		@Override
  		public void onProviderDisabled(String arg0) {
  			// TODO Auto-generated method stub
  			
  		}
  		@Override
  		public void onProviderEnabled(String arg0) {
  			// TODO Auto-generated method stub
  			
  		}
  		@Override
  		public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
  			// TODO Auto-generated method stub
  			
  		}
  		
  		 //Radius filter
  		/**
  		 * Call the events filter pop.
  		 */
  		 private void accessActionBarView(){
  			 LinearLayout linearLauout = (LinearLayout)findViewById(R.id.relativeLayout1);

  	  		 Log.d("Log", "Height: " + "+linearLauout.getHeight:"+linearLauout.getHeight());
  	  		// We need to get the instance of the LayoutInflater
  	  		// Log.d("MainActivity", "Error in creating p:"+p);
  	  		 LinearLayout viewGroup = (LinearLayout) this.findViewById(R.id.popup);
  	  		 LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
  	  		 View layout = inflater.inflate(R.layout.event_filter_view_by_radius, viewGroup);
  	  		 popup = new PopupWindow(layout, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
  	  		 //popup.setBackgroundDrawable(new BitmapDrawable());
  	  		 popup.showAtLocation(layout, Gravity.LEFT|Gravity.TOP, 0, linearLauout.getHeight()+15);
  	  		 
 			//Filter by radius and date
  	  		//LinearLayout filterRadiusLinearLayout = (LinearLayout)layout.findViewById(R.id.event_filter_by_radius);
  	  	    ImageView radiusFilterCloseImageView = (ImageView)layout.findViewById(R.id.event_radius_filter_close_textView2);
  	  	    ImageView radiusFilterSelectDate = (ImageView)layout.findViewById(R.id.radius_select_date_imageView1);
  	  	    TextView radiusFilterDate = (TextView)layout.findViewById(R.id.selected_date_textView1);
  	  	    final TextView filterRadiusTextview = (TextView)layout.findViewById(R.id.event_filter_radius_textView1);
  	  	    SeekBar radiusSeekBar = (SeekBar)layout.findViewById(R.id.event_radius_seekBar1);
 			
  			 //View view = getActivity().getLayoutInflater().inflate(R.layout.activity_main, null); // inflating popup layout
  			// ImageView eventTheatreImageView = (ImageView)layout.findViewById(R.id.event_filter_imageView3); 
  			/* radiusFilterDate.setOnClickListener(new OnClickListener() {
  					
  					@Override
  					public void onClick(View arg0) {
  						// TODO Auto-generated method stub
  						Log.d("ArtistTeamsFragment", "accessActionBarView()eventTheatreImageView:");
  						filterRadiusLinearLayout.setVisibility(View.VISIBLE);
  					}
  				});*/
  			
  			 radiusFilterCloseImageView.setOnClickListener(new OnClickListener() {
  					
  					@Override
  					public void onClick(View arg0) {
  						// TODO Auto-generated method stub
  						popup.dismiss();
  						callBackClass();
  					}
  				});
  			 String radiusRang = preferences.getString("radius_range", "1");
  			 radiusSeekBar.setProgress(Integer.parseInt(radiusRang));
  			 filterRadiusTextview.setText(radiusRang+"mi"); 
  			   
  			 radiusSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
  				
  				@Override
  				public void onStopTrackingTouch(SeekBar seekBar) {
  					// TODO Auto-generated method stub
  					
  				}
  				
  				@Override
  				public void onStartTrackingTouch(SeekBar seekBar) {
  					// TODO Auto-generated method stub
  					
  				}
  				
  				@Override
  				public void onProgressChanged(SeekBar seekBar, int progress,
  						boolean fromUser) {
  					// TODO Auto-generated method stub
  					if (progress >= 1 && progress <= 500) {
  						filterRadiusTextview.setText(String.valueOf(progress)+"mi"); 
  						radiusRange_ = String.valueOf(progress);
  						SharedPreferences.Editor editor = preferences.edit();
  						editor.putString("radius_range",String.valueOf(progress));		    			        		
  			    		editor.commit();
  					}
  				}
  			});
  			radiusFilterSelectDate.setOnClickListener(new OnClickListener() {
  					
  					@Override
  					public void onClick(View arg0) {
  						// TODO Auto-generated method stub
  						FragmentManager fm = getFragmentManager();
  						DatePickerFragment datealert = new DatePickerFragment();
  						datealert.show(fm, "Alert_Dialog");
  					}
  			});
  			radiusFilterDate.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						FragmentManager fm = getFragmentManager();
  						DatePickerFragment datealert = new DatePickerFragment();
  						datealert.show(fm, "Alert_Dialog");
					}
			});
  		 } 
  		 /**
  		  * Share social network.
  		  */
  		 private void onShareSocialNetwork(){
  			LinearLayout linearLauout = (LinearLayout)findViewById(R.id.relativeLayout1);

 	  		 Log.d("Log", "Height: " + "+linearLauout.getHeight:"+linearLauout.getHeight());
 	  		// We need to get the instance of the LayoutInflater
 	  		// Log.d("MainActivity", "Error in creating p:"+p);
 	  		 LinearLayout viewGroup = (LinearLayout) this.findViewById(R.id.popup);
 	  		 LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
 	  		 View layout = inflater.inflate(R.layout.share_view_layout, viewGroup);
 	  		 popup = new PopupWindow(layout, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
 	  		 popup.setBackgroundDrawable(new BitmapDrawable());
 	  		 popup.showAtLocation(layout, Gravity.RIGHT|Gravity.TOP, 0, linearLauout.getHeight()+15);
 	  		 
 	  		 LinearLayout facebookShare = (LinearLayout)layout.findViewById(R.id.facebook_share_);
 	  		 LinearLayout twitterShare = (LinearLayout)layout.findViewById(R.id.twitter_share_);
 	  		 LinearLayout mailShare = (LinearLayout)layout.findViewById(R.id.mail_share_);
 	  		 LinearLayout messageShare = (LinearLayout)layout.findViewById(R.id.message_share_);
 	  		 ConnectionDetector  networkInfo = new ConnectionDetector(MainActivity.this);
 	  	    final boolean isInternetPresent =networkInfo.isConnectingToInternet();
 	  		facebookShare.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (isInternetPresent) {
						 //Intent	intent = new Intent(MainActivity.this, ShareActivity.class);
						String shareMessage = getResources().getString(R.string.share_message);
						String playStoreLink = getResources().getString(R.string.play_store_link);
						String webSiteLink = getResources().getString(R.string.web_site_link);
						String webLogoShare = getResources().getString(R.string.web_logo_share);
						
						 Intent	intent = new Intent(MainActivity.this, FacebookShareActivity.class);
						 intent.putExtra("shareMessage", shareMessage);
						 intent.putExtra("playStoreLink", playStoreLink);
						 intent.putExtra("webSiteLink", webSiteLink);
						 intent.putExtra("webLogoShare", webLogoShare);
					     //Intent	intent = new Intent(MainActivity.this, MailActivity.class);
				         startActivity(intent);
					}else{
						Toast.makeText(MainActivity.this, getResources().getString(R.string.network_issue), Toast.LENGTH_LONG).show();
					}

					popup.dismiss();
				}
			});
 	  		twitterShare.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
				
					if (isInternetPresent) {
						TwitterShareActivity  tweet = new TwitterShareActivity(MainActivity.this);
						String shareMessage = getResources().getString(R.string.share_message);
						String playStoreLink = getResources().getString(R.string.play_store_link);
						String webSiteLink = getResources().getString(R.string.web_site_link);
						String webLogoShare = getResources().getString(R.string.web_logo_share);
						 tweet.onTweet(shareMessage+"\n"+playStoreLink+"\n"+webSiteLink+"\n " +webLogoShare);
						 //Intent i = new Intent(Intent.ACTION_VIEW);
							//i.setData(Uri.parse("http://twitter.com/?status=" + Uri.encode(shareMessage)));
							//startActivity(i);
							//Intent	intent = new Intent(MainActivity.this, ShareActivity.class);
							/* intent.putExtra("shareMessage", shareMessage);
							 intent.putExtra("playStoreLink", playStoreLink);
							 intent.putExtra("webSiteLink", webSiteLink);
							 intent.putExtra("webLogoShare", webLogoShare);*/
						    // Intent	intent = new Intent(MainActivity.this, TwitterShare.class);
					         //startActivity(intent);
						 popup.dismiss();
					}else{
						Toast.makeText(MainActivity.this, getResources().getString(R.string.network_issue), Toast.LENGTH_LONG).show();
					}									
				}
			});
 	  		mailShare.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					String shareMessage = getResources().getString(R.string.share_message);
					String playStoreLink = getResources().getString(R.string.play_store_link);
					String webSiteLink = getResources().getString(R.string.web_site_link);
					String webLogoShare = getResources().getString(R.string.web_logo_share);
					
					SpannableStringBuilder builder = new SpannableStringBuilder();
				    builder.append(shareMessage);
				    builder.append("\n");
				    builder.append(playStoreLink);
				    builder.append("\n");
				    builder.append(webSiteLink);
				    builder.append("\n");
				    builder.append(webLogoShare);
				   
		            Intent emailIntent = new Intent( Intent.ACTION_VIEW);
		            emailIntent.setClassName("com.google.android.gm", "com.google.android.gm.ComposeActivityGmail");		            
		            emailIntent.putExtra(Intent.EXTRA_TEXT, builder);
				    emailIntent.putExtra(Intent.EXTRA_SUBJECT, "FYE Tickets");
				    emailIntent.setType("message/rfc822");
		            startActivity(emailIntent );
					 popup.dismiss();
				}
			});
 	  		messageShare.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					String shareMessage = getResources().getString(R.string.share_message);
					String playStoreLink = getResources().getString(R.string.play_store_link);
					String webSiteLink = getResources().getString(R.string.web_site_link);
					StringBuilder message = new StringBuilder();
					message.append(shareMessage);
					message.append("\n");
					message.append(playStoreLink);
					message.append("\n");
					message.append(webSiteLink);
					
		            Intent intentsms = new Intent( Intent.ACTION_VIEW, Uri.parse( "sms:" + "" ) );
		            intentsms.setType("vnd.android-dir/mms-sms");
		            intentsms.putExtra( "sms_body", message.toString() );
		            startActivity( intentsms );
		            popup.dismiss();
				}
			});	  		 
  		 }
}

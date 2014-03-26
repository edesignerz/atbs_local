package com.dds.fye.tickets;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;


import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.dds.fye.animation.AnimationHelper;
import com.dds.fye.tickets.adapter.EventAlphabetListAdapter.Item;
import com.dds.fye.tickets.adapter.EventAlphabetListAdapter.Row;
import com.dds.fye.tickets.adapter.EventAlphabetListAdapter.Section;
import com.dds.fye.tickets.adapter.EventAlphabetListAdapter;
import com.dds.fye.tickets.jsonparse.JSONParse;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;


import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class EventsFragment extends Fragment{
	
	 private static EventAlphabetListAdapter adapter = new EventAlphabetListAdapter();	  
	 private List<Object[]> alphabet = new ArrayList<Object[]>();
	 private HashMap<String, Integer> sections = new HashMap<String, Integer>();
	 //private ListView myListView;
	 private ImageView sortSetting;
	 private Button sortByDate, sortByName;
	 private TextView eventsNearCity; 
	 private SharedPreferences preferences ;
	 private ConnectionDetector networkInfo;
	 private String sortByNameORSortByDate="";
	 private List<EventsData> eventsSortArraylist = new ArrayList<EventsData>();
	 
	 private LinearLayout eventAllEventLinearLayout,eventSportslinearLayout,
	 			eventConcertsLinearLayout,eventTheatreLinearLayout;
	 private ImageView allEventImageView,eventSportsImageView,eventConcertsImageView,
	 				eventTheatreImageView;
	 
	 ProgressDialog progress;
	 		
	 private PullToRefreshListView mPullRefreshListView;
	 ListView actualListView;

	 public EventsFragment(){}
     
	    @Override
	    public View onCreateView(LayoutInflater inflater, ViewGroup container,
	            Bundle savedInstanceState) {	  
	        View rootView = inflater.inflate(R.layout.event_view, container, false); 
	        progress = new ProgressDialog(getActivity());
	        preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());	
	        networkInfo = new ConnectionDetector(getActivity());
	        //myListView = (ListView) rootView.findViewById(R.id.event_page_listView1);
	        
	        mPullRefreshListView = (PullToRefreshListView) rootView.findViewById(R.id.pull_refresh_list);
	    	// Set a listener to be invoked when the list should be refreshed.
			mPullRefreshListView.setOnRefreshListener(new OnRefreshListener<ListView>() {
				@Override
				public void onRefresh(PullToRefreshBase<ListView> refreshView) {
					String label = DateUtils.formatDateTime(getActivity().getApplicationContext(), System.currentTimeMillis(),
							DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);

					// Update the LastUpdatedLabel
					refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);

					// Do work to refresh the list here.					
					 //Log.d("EventFragment", "onRefresh:");
					 getVenues();
				}
			});
			
			actualListView = mPullRefreshListView.getRefreshableView();
	       //mPullRefreshListView.setFastScrollEnabled(true);
			// Need to use the Actual ListView when registering for Context Menu
			registerForContextMenu(actualListView);
			
			mPullRefreshListView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
						long arg3) {
					// TODO Auto-generated method stub
					Log.d("EventsFragment", "arg2-1:"+(arg2-1));
					//Log.d("ArtistTeamsFragment", "arg2:"+rows.get);				
					Item item = (Item) adapter.getItem(arg2-1);
					//Log.d("ArtistTeamsFragment", "item.text:"+item.text);
					EventsData data = item.eventsData;;
					String eventid = data.eventId;
					if (!eventid.equals("")) {
						String eventId = data.eventId;
						String eventTitle = data.eventTitle;
						String eventDateString = data.eventDateString;
						String evantDateTimeLocal = data.eventDateTimeLocal;
						String eventVenuName = data.eventVenueName;
						String eventMapStatic = data.eventMapStatic;
						
						String eventAddress = data.localEventAddress;
						String eventVenueLocation =data.eventVenueLocation;
						String eventVenueLat = data.eventVenueLat;
						String eventVenueLon = data.eventVenueLon;
						String eventVenuePostalCode = data.eventVenueZipCode;
						
						String eventPerformerName = data.eventPerformerName;
						Log.d("EventsFragment", "Eventfragment eventPerformerName:"+eventPerformerName);

						
						Intent intent = new Intent(getActivity(), ArtistTeamsEventList.class);
						//intent.putExtra("eventData", data);
						intent.putExtra("eventId", eventId);
						intent.putExtra("eventTitle", eventTitle);
						intent.putExtra("eventDateString", eventDateString);
						intent.putExtra("evantDateTimeLocal", evantDateTimeLocal);
						intent.putExtra("eventVenuName", eventVenuName);
						
						intent.putExtra("eventAddress", eventAddress);
						intent.putExtra("eventVenueLocation", eventVenueLocation);
						intent.putExtra("eventVenuePostalCode", eventVenuePostalCode);
						intent.putExtra("eventVenueLat", eventVenueLat);
						intent.putExtra("eventVenueLon", eventVenueLon);
						intent.putExtra("eventPerformerName", eventPerformerName);
						
						intent.putExtra("map", eventMapStatic);
						intent.putExtra("call_class", "events");
						intent.putExtra("listViewPosition", String.valueOf(arg2));
				        startActivity(intent);
					}			        
				}
			});
	       	        
			eventAllEventLinearLayout = (LinearLayout)rootView.findViewById(R.id.event_all_events_linearlayout);
			eventSportslinearLayout = (LinearLayout)rootView.findViewById(R.id.event_sports_linearlayout);
			eventConcertsLinearLayout = (LinearLayout)rootView.findViewById(R.id.event_concerts_linearlayout);
			eventTheatreLinearLayout = (LinearLayout)rootView.findViewById(R.id.event_theatre_linearlayout);
			
			allEventImageView = (ImageView)rootView.findViewById(R.id.event_all_event_imageView1);
			eventSportsImageView = (ImageView)rootView.findViewById(R.id.event_sport_imageView);
			eventConcertsImageView = (ImageView)rootView.findViewById(R.id.event_consert_imageView); 
			eventTheatreImageView = (ImageView)rootView.findViewById(R.id.event_theatre_imageView);
						
			
			getVenues();
			sortEventsByType();
			eventFilterInList();
			
	        return rootView;
	    }
	    
	    @Override
	    public void onResume(){
	    	super.onResume();
	    	adapter.notifyDataSetChanged();
	    }
	    /**
	     * List refresh. 
	     */
	    public static void upDateList(){
	    	adapter.notifyDataSetChanged();
	    }
	    /**
	     * General method to call loader class thread.
	     * For download Data
	     */
	    public void getVenues(){
	    	String sortCode = preferences.getString("event_filter", "event");
	    	String selectedCity = preferences.getString("selectcity", "");
	    	String radius = preferences.getString("radius_range", "10");
	    	//"event.type=conserts&","event.type=theater&";"event.type=sports&";&range=12mi,datetime_local.gte=2013-10-01
	    	//sort=distance.desc'
	    	/*String selectedCity = preferences.getString("selectcity", "");
	    	Boolean isInternetPresent = networkInfo.isConnectingToInternet();
			if (isInternetPresent) {
				AsyncTaskRunner myWebFetch = new AsyncTaskRunner();
		 	     myWebFetch.execute(baseUrl);
			}*/
	    	if (selectedCity.equals("")) {
      			Log.d("EventsFragment", "selectedCity:"+selectedCity);
      			
			}else{
				Log.d("EventsFragment", "selectedCity:"+selectedCity);
				Log.d("EventsFragment", "radius:"+radius);
				int year = preferences.getInt("year", 0);
				int month = preferences.getInt("month", 0);
				int day = preferences.getInt("day", 0);
				String date_ = ""; 
				if (year!=0 && month!=0 && day!=0) {
					date_ = String.valueOf(year)+"-"+String.valueOf(month)+"-"+String.valueOf(day);				    
				}else{
				    final Calendar c = Calendar.getInstance();
				    int yy = c.get(Calendar.YEAR);
				    int mm = c.get(Calendar.MONTH);
				    mm = mm + 1;
				    int dd = c.get(Calendar.DAY_OF_MONTH);
				    
				    Log.d("EventsFragment", "else yy:"+yy);
				    Log.d("EventsFragment", "else mm:"+mm);
				    Log.d("EventsFragment", "else dd:"+dd);
				    
				    date_ = String.valueOf(yy)+"-"+String.valueOf(mm)+"-"+String.valueOf(dd);
				    Log.d("EventsFragment", "else date_:"+date_);
				}
				Log.d("EventsFragment", "date_:"+date_);
				//String[] citySplite = selectedCity.split("[ ]");
				String selectCitytName = "";
				/*String[] citySplite = selectedCity.split("[,]");
				//for (int i = 0; i < citySplite.length; i++) {
					String[] spaceFreeCity = citySplite[0].split("[ ]");
					
					for (String string : spaceFreeCity) {
						if (selectCitytName.equals("")) {
							selectCitytName = string;
						}else{
							selectCitytName = selectCitytName+"+"+string;
						}					
					}*/
				//}
				selectCitytName = selectedCity.replace(" ", "+");
				//selectCitytName = selectCitytName.replace(",", "");
				Log.d("EventsFragment", "selectCitytName:"+selectCitytName);
				String baseUrl = "";
		    	if (sortCode.equals("event")){
		    		baseUrl = getResources().getString(R.string.artist_teams_events)+"venue.city="+
		    				selectCitytName+"&range="+radius+"mi"+"&sort=datetime_local.asc"+
		    				"&datetime_local.gte="+date_;
		    		//even_type="";
				}else{
					//even_type = sortCode; 
					baseUrl = getResources().getString(R.string.artist_teams_events)+"venue.city="+
							selectCitytName+"&"+"event.type="+sortCode+"&range="+radius+"mi"+"&sort=datetime_local.asc"+
								"&datetime_local.gte="+date_;
				}
				Boolean isInternetPresent = networkInfo.isConnectingToInternet();
				if (isInternetPresent) {
					AsyncTaskRunner myWebFetch = new AsyncTaskRunner();
			 	     myWebFetch.execute(baseUrl);
				}
			}
	    }
	    /**
	     * Add the listener in events type tab.
	     * Sorting events by events type.
	     */
	    private void sortEventsByType(){
	    	eventAllEventLinearLayout.setOnClickListener(new OnClickListener() {				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub				
					allEventImageView.setBackgroundColor(getResources().getColor(R.color.selected_bar_tab_orange_tab));
					eventSportsImageView.setBackgroundColor(Color.TRANSPARENT);
				    eventConcertsImageView.setBackgroundColor(Color.TRANSPARENT);
					eventTheatreImageView.setBackgroundColor(Color.TRANSPARENT);
					SharedPreferences.Editor editor = preferences.edit();
			    	editor.putString("event_filter","event");	        		
			    	editor.commit();
			    	getVenues();
				}
			});
	    	eventSportslinearLayout.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub				
					  allEventImageView.setBackgroundColor(Color.TRANSPARENT);
					  eventSportsImageView.setBackgroundColor(getResources().getColor(R.color.selected_bar_tab_orange_tab));
					  eventConcertsImageView.setBackgroundColor(Color.TRANSPARENT);
					  eventTheatreImageView.setBackgroundColor(Color.TRANSPARENT);
					  SharedPreferences.Editor editor = preferences.edit();
					  editor.putString("event_filter","sport");	        		
					  editor.commit();
					  getVenues();
				}
			});
	    	eventConcertsLinearLayout.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					  allEventImageView.setBackgroundColor(Color.TRANSPARENT);
					  eventSportsImageView.setBackgroundColor(Color.TRANSPARENT);
					  eventConcertsImageView.setBackgroundColor(getResources().getColor(R.color.selected_bar_tab_orange_tab));
					  eventTheatreImageView.setBackgroundColor(Color.TRANSPARENT);	
					  SharedPreferences.Editor editor = preferences.edit();
					  editor.putString("event_filter","concert");	        		
					  editor.commit();
					  getVenues();
				}
			});
	    	eventTheatreLinearLayout.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					  allEventImageView.setBackgroundColor(Color.TRANSPARENT);
					  eventSportsImageView.setBackgroundColor(Color.TRANSPARENT);
					  eventConcertsImageView.setBackgroundColor(Color.TRANSPARENT);
					  eventTheatreImageView.setBackgroundColor(getResources().getColor(R.color.selected_bar_tab_orange_tab));;	
					  SharedPreferences.Editor editor = preferences.edit();
					  editor.putString("event_filter","theatre");	        		
					  editor.commit();
					  getVenues();
				}
			});
	    }
		//Ticket filter by price , e-ticket, section, row in second view 
        /**
         * Select events to sorting list.
         * Change events type.
         */
		private void eventFilterInList(){
			  String sortCode = preferences.getString("event_filter", "event");
			 // allEventImageView,eventSportsImageView,eventConcertsImageView,
				//eventThratreImageView;
			  if (sortCode.equals("event")) {//R.color.orange_tab
				  allEventImageView.setBackgroundColor(getResources().getColor(R.color.selected_bar_tab_orange_tab));
				  eventSportsImageView.setBackgroundColor(Color.TRANSPARENT);
				  eventConcertsImageView.setBackgroundColor(Color.TRANSPARENT);
				  eventTheatreImageView.setBackgroundColor(Color.TRANSPARENT);				
			  }
			
			  if (sortCode.equals("sport")) {
				  allEventImageView.setBackgroundColor(Color.TRANSPARENT);
				  eventSportsImageView.setBackgroundColor(getResources().getColor(R.color.selected_bar_tab_orange_tab));
				  eventConcertsImageView.setBackgroundColor(Color.TRANSPARENT);
				  eventTheatreImageView.setBackgroundColor(Color.TRANSPARENT);							
			  }
			 
			  if (sortCode.equals("concert")) {
				  allEventImageView.setBackgroundColor(Color.TRANSPARENT);
				  eventSportsImageView.setBackgroundColor(Color.TRANSPARENT);
				  eventConcertsImageView.setBackgroundColor(getResources().getColor(R.color.selected_bar_tab_orange_tab));
				  eventTheatreImageView.setBackgroundColor(Color.TRANSPARENT);						
			  }
			  if (sortCode.equals("theatre")) {
				  allEventImageView.setBackgroundColor(Color.TRANSPARENT);
				  eventSportsImageView.setBackgroundColor(Color.TRANSPARENT);
				  eventConcertsImageView.setBackgroundColor(Color.TRANSPARENT);
				  eventTheatreImageView.setBackgroundColor(getResources().getColor(R.color.selected_bar_tab_orange_tab));						
			  }
		}
	    /**
	     * Create alphabet list view.
	     * Interact data with list view.
	     */
	    public void dataList(){
	    	 // List<String> countries = populateCountries();
	    	 // List<String> countries = nameList;
	    	 Log.d("EventsFragment", "eventsSortArraylist.size():"+eventsSortArraylist.size());
	    	 //Log.d("ArtistTeamsFragment", "eventsSortArraylist:"+eventsSortArraylist);
	          //Collections.sort(nameList);
				/*Collections.sort(eventsSortArraylist, new Comparator<EventsData>(){
				      public int compare(EventsData obj1, EventsData obj2)
				      {
				            // TODO Auto-generated method stub
				            return obj1.eventTitle.compareToIgnoreCase(obj2.eventTitle);
				      }
				});*/

	          List<Row> rows = new ArrayList<Row>();
	          rows.clear();
	          int start = 0;
	          int end = 0;
	          String previousLetter = null;
	          Object[] tmpIndexItem = null;
	          Pattern numberPattern = Pattern.compile("[0-9]");
	          for (EventsData country : eventsSortArraylist) {        	 
	              String firstLetter = country.eventTitle.substring(0, 1);

	              // Group numbers together in the scroller
	              if (numberPattern.matcher(firstLetter).matches() || firstLetter.equals(".")) {
	                  firstLetter = "#";
	              }

	              // If we've changed to a new letter, add the previous letter to the alphabet scroller
	              if (previousLetter != null && !firstLetter.equals(previousLetter)){
	                  end = rows.size() - 1;
	                  tmpIndexItem = new Object[3];
	                  tmpIndexItem[0] = previousLetter.toUpperCase(Locale.UK);
	                  tmpIndexItem[1] = start;
	                  tmpIndexItem[2] = end;
	                  alphabet.add(tmpIndexItem);

	                  start = end + 1;
	              }

	              // Check if we need to add a header row
	             /* if (!firstLetter.equals(previousLetter)) {
	                  rows.add(new Section(firstLetter));
	                  sections.put(firstLetter, start);
	              }*/

	              // Add the country to the list
	              rows.add(new Item(country.eventTitle,country));
	              previousLetter = firstLetter;
	          }

	          if (previousLetter != null) {
	              // Save the last letter
	              tmpIndexItem = new Object[3];
	              tmpIndexItem[0] = previousLetter.toUpperCase(Locale.UK);
	              tmpIndexItem[1] = start;
	              tmpIndexItem[2] = rows.size() - 1;
	              alphabet.add(tmpIndexItem);
	          }
	          Log.d("EventsFragment", "rows.size():"+rows.size());
	          adapter.setRows(rows);
	          actualListView.setAdapter(adapter);
	    }  
	    
	   /*public void onRadiusFilter(){
	    	filterRadiusLinearLayout.setVisibility(View.VISIBLE);
	    }*/
	    /**
	     * Progress bar.
	     * Run only on loading time.
	     */
	    public void openProgress(){
	        progress.setMessage("Wait...");
	        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);	      
	        progress.setIndeterminate(true);
	        progress.setCanceledOnTouchOutside(false);	        
	        progress.show();
	    }
	    /**
	     * A subclass of AsyncTask that calls events in the
  	     * background. The class definition has these generic types:
	     * Download data from api.
	     * Run on background.
	     * @author USER
	     *
	     */
	    public class AsyncTaskRunner extends AsyncTask<String, String, String>{
	    	/*
	    	 * (non-Javadoc)
	    	 * 
	    	 * @see android.os.AsyncTask#onPreExecute()
	    	 */
	    	@Override
	    	protected void onPreExecute() {
	    		// Things to be done before execution of long running operation. For
	    		// example showing ProgessDialog
	    		openProgress();
	    	}
	    	@Override
	    	protected String doInBackground(String... arg0) {
	    		// TODO Auto-generated method stub
	    		InputStream is = null;
	    		// Only display the first 500 characters of the retrieved
	    		// web page content.
	    		int len = 500;
	    		int numberOfDownload_per_page = 1;
		    	eventsSortArraylist.clear();
		        
		        //api_url= getResources().getString(R.string.artist_teams);
		    
		    	try {
		    		//URL url = new URL("http://mobapi.atbss.com/performers?sort=performer.name.desc");	
		    		String contentAsString = "";
		    		int numberOfDownload = 0;
		    		int per_page_download = 1000;
		    		do {
						String api_url = arg0[0]+"&per_page="+per_page_download+"&page="+numberOfDownload_per_page;
				    	Log.d("AsyncTaskRunner", "EventsFragment The url : " + api_url);
				    	String response = "";
				    	DefaultHttpClient client = new DefaultHttpClient();
				        HttpGet httpGet = new HttpGet(api_url);
				        try {
				          HttpResponse execute = client.execute(httpGet);
				          InputStream content = execute.getEntity().getContent();				          
				          BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
				          String s = "";
				          while ((s = buffer.readLine()) != null) {
				            response += s;
				          }

				        } catch (Exception e) {
				          e.printStackTrace();
				        }

				        
				         contentAsString = response;
				        //Log.d("AsyncTaskRunner", "The contentAsString is: " + contentAsString);		        
				        ArrayList<ArrayList<HashMap<String, String>>> artistTeamsArrayList= new ArrayList<ArrayList<HashMap<String, String>>>();
				        artistTeamsArrayList.clear();
				        artistTeamsArrayList =  JSONParse.artistTeamsAllEvents(contentAsString, per_page_download);
				        //Log.d("AsyncTaskRunner", "The artistTeamsArrayList.size is: " + artistTeamsArrayList.size());
				       // ArrayList<Integer> idList = new ArrayList<Integer>();	
				       	       				       
				        for (int i = 0; i < artistTeamsArrayList.size(); i++) {
				        	ArrayList<HashMap<String, String>> dataArrayList = artistTeamsArrayList.get(i);
							HashMap<String, String> hashMap = dataArrayList.get(0);
							String event_id = hashMap.get("id");
							String event_title = hashMap.get("title");
							String event_data_string = hashMap.get("data_string");
							String event_datetime_local = hashMap.get("datetime_local");
							String event_map_static = hashMap.get("map_static");
							numberOfDownload = Integer.parseInt(hashMap.get("numberOfDownload"));
							hashMap = dataArrayList.get(1);
							String event_venue_name = hashMap.get("name");
							String event_venue_location = hashMap.get("location");
							String event_venue_address = hashMap.get("address");
							String event_venue_postal_code = hashMap.get("postal_code");
							String event_venue_lat = hashMap.get("lat");
							String event_venue_lon = hashMap.get("lon");
							
							String performerName = "";
							String performerId = "";
							for (int j = 2; j < dataArrayList.size(); j++) {
								HashMap<String, String> hashMap_ = dataArrayList.get(j);
								if (performerName.equals("") ) {
									performerName = hashMap_.get("name");
								}else{
									performerName = performerName + ","+hashMap_.get("name");
								}
								if (performerId.equals("") ) {
									performerId = hashMap_.get("id");
								}else{
									performerId = performerId + ","+hashMap_.get("id");
								}
							}							
							//idList.add(Integer.parseInt(id));
							/* Log.d("ArtistTeamsFragment", "event_id:"+event_id);
							 Log.d("ArtistTeamsFragment", "event_title:"+event_title);
							 Log.d("ArtistTeamsFragment", "event_data_string:"+event_data_string);
							 Log.d("ArtistTeamsFragment", "event_datetime_local:"+event_datetime_local);
							 Log.d("ArtistTeamsFragment", "event_map_static:"+event_map_static);
							 Log.d("ArtistTeamsFragment", "event_venue_name:"+event_venue_name);
							 Log.i("ArtistTeamsFragment", "event_venue_location:"+event_venue_location);*/
							 
							eventsSortArraylist.add(new EventsData(event_id, event_title, event_data_string, 
									event_datetime_local, event_map_static, event_venue_name, 
									event_venue_location,event_venue_address,"",performerName,performerId,
									event_venue_postal_code,event_venue_lon,event_venue_lat));
						}
				        numberOfDownload_per_page++;
					} while (numberOfDownload >= numberOfDownload_per_page);   	
		    		//Collections.sort(idList);	
		    		Log.d("ArtistTeamsFragment", "asynk eventsSortArraylist.size():"+eventsSortArraylist.size());
		    		return contentAsString;
		        
		    		// Makes sure that the InputStream is closed after the app is
		    		// finished using it.
		    	} finally {
		    		if (is != null) {
		    			try {
		    				is.close();
		    			} catch (IOException e) {
		    				// TODO Auto-generated catch block
		    				e.printStackTrace();
		    			}
		    		} 
		    	}				
	    	}
	    	/*
	    	 * (non-Javadoc)
	    	 * 
	    	 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
	    	 */
	    	@Override
	    	protected void onPostExecute(String result) {
	    		// execution of result of Long time consuming operations
	    		progress.dismiss();
	    		// Call onRefreshComplete when the list has been refreshed.
	    		mPullRefreshListView.onRefreshComplete();
	    		dataList();
	    		//updateList();		 
	    	}
	    }
  
}

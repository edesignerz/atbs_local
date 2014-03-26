package com.dds.fye.tickets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import com.dds.fye.tickets.adapter.AlphabetListAdapter;
import com.dds.fye.tickets.adapter.AlphabetListAdapter.Item;
import com.dds.fye.tickets.adapter.AlphabetListAdapter.Row;
import com.dds.fye.tickets.adapter.AlphabetListAdapter.Section;
import com.dds.fye.tickets.jsonparse.JSONParse;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class MorePerformerDetailList extends Activity {
	private ListView morePerformerListView1;
	private TextView performerName;
	private LinearLayout linearLayout;
	private ProgressDialog progress;
	private String performer_name ;
	private String moreEventOnVenne;
	private ConnectionDetector networkInfo;
	private List<Object[]> alphabet = new ArrayList<Object[]>();
	private HashMap<String, Integer> sections = new HashMap<String, Integer>();
	private AlphabetListAdapter adapter = new AlphabetListAdapter();
	private List<EventsData> eventsSortArraylist = new ArrayList<EventsData>();
	Intent intent = new Intent();
	 @Override
	  public void onCreate(Bundle bundle) {
	    super.onCreate(bundle);
	    setContentView(R.layout.more_performer_detail_list);
	    progress = new ProgressDialog(this);
	    networkInfo = new ConnectionDetector(getApplicationContext());
	    Bundle extras = getIntent().getExtras();
	    performer_name = extras.getString("performer_name");
	    moreEventOnVenne = extras.getString("venu_name");
	    linearLayout = (LinearLayout)findViewById(R.id.more_perfo_detail_back);
	    performerName = (TextView)findViewById(R.id.more_info_performer_name_textView1);
	    performerName.setText(performer_name);
	    morePerformerListView1 = (ListView)findViewById(R.id.more_performer_listView1);
	    linearLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub				
				finish();
			}
		});
	    morePerformerListView1.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Log.d("EventsFragment", "arg2:"+arg2);
				//Log.d("ArtistTeamsFragment", "arg2:"+rows.get);				
				Item item = (Item) adapter.getItem(arg2);
				//Log.d("ArtistTeamsFragment", "item.text:"+item.text);
				EventsData data = item.eventsData;
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
					intent.putExtra("returnkey", "result");
					finish();
			       
				}			        
			}
		});
	    if (moreEventOnVenne.equals("")) {	    	
	    	loadingPerformerView();
		}else{
			loadingEventsOnVenue();	
		}
	  }
	    /**
	     * General method to call loader class.
	     * Call a subclass of AsyncTask 
	     * For download Data.
	     */
	 private void loadingPerformerView(){		
			//listview_position = Integer.parseInt(position);
			//Log.d("ArtistTeamsFragment", "position:"+position);
			//Log.d("ArtistTeamsFragment", "listview_position:"+listview_position);		
			String[] split_name = performer_name.split("[ ]");
			Log.d("ArtistTeamsFragment", "split_name[0]:"+split_name[0]);		
			String title_ ="";
			title_ = performer_name.replace(" ", "+");
			/*for (int i = 0; i < split_name.length; i++) {
				if(title_.equals("")){
					title_ = split_name[i];
				}else{
					title_ = title_ +"+"+ split_name[i];
				}
			}*/
			Log.d("ArtistTeamsFragment", "title_:"+title_);
			String query_code = "performer.name="+title_+"&performer.home=0";		
		    String api_url = getResources().getString(R.string.artist_teams_events)+query_code;
		    Log.d("ArtistTeamsFragment", "api_url:"+api_url);
		    Boolean isInternetPresent = networkInfo.isConnectingToInternet();
			if (isInternetPresent) {
				AsyncTaskRunner myWebFetch = new AsyncTaskRunner();
		        myWebFetch.execute(api_url,"events");
			}
	 }
	    /**
	     * General method to call loader class.
	     * Call a subclass of AsyncTask
	     * More events on current venue. 
	     * For download Data.
	     */
	 private void loadingEventsOnVenue(){		
			//listview_position = Integer.parseInt(position);
			//Log.d("ArtistTeamsFragment", "position:"+position);
			//Log.d("ArtistTeamsFragment", "listview_position:"+listview_position);		
			String[] split_name = performer_name.split("[ ]");
			Log.d("ArtistTeamsFragment", "split_name[0]:"+split_name[0]);		
			String title_ ="";
			title_ = performer_name.replace(" ", "+");
			/*for (int i = 0; i < split_name.length; i++) {
				if(title_.equals("")){
					title_ = split_name[i];
				}else{
					title_ = title_ +"+"+ split_name[i];
				}
			}*/
			Log.d("ArtistTeamsFragment", "title_:"+title_);
			String query_code = "venue.name="+title_;
		    String api_url = getResources().getString(R.string.artist_teams_events)+query_code;
		    Log.d("ArtistTeamsFragment", "api_url:"+api_url);
		    Boolean isInternetPresent = networkInfo.isConnectingToInternet();
			if (isInternetPresent) {
				AsyncTaskRunner myWebFetch = new AsyncTaskRunner();
		        myWebFetch.execute(api_url,"events");
			}
	 }
	 
	 @Override
	  public void finish() {	    		
	    setResult(RESULT_OK, intent);
	    super.finish();
	  }
	    /**
	     * A subclass of AsyncTask that calls performer detail in the
  	     * background. The class definition has these generic types:
		 * Create artist and teams list view.
		 * Integrate events data with event list view.
		 */
		//Artist and teams list create
		private void onDataList(){

	        List<Row> rows = new ArrayList<Row>();
	        int start = 0;
	        int end = 0;
	        String previousLetter = null;
	        Object[] tmpIndexItem = null;
	        //Pattern numberPattern = Pattern.compile("[0-9]");
	       // for (int i = 0; i < itemAvalible.size(); i++) {
	        for (EventsData country : eventsSortArraylist) {
	        	 String allOrLocalEvents = country.localEventOrAllEvent;
	        	
	        	String firstLetter = null;
	        	if (allOrLocalEvents.equals("allevents")) {
	        		 firstLetter = "All Events";
				}else{
					 firstLetter = "Local Events";
				}
	        	 

	            // Group numbers together in the scroller
	            /*if (numberPattern.matcher(firstLetter).matches()) {
	                firstLetter = "#";
	            }*/

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
	            if (!firstLetter.equals(previousLetter)) {
	                rows.add(new Section(firstLetter));
	                sections.put(firstLetter, start);
	            }
	           // Log.d("AsyncTaskRunner", "The date_timeArray_ is: " + date_timeArray_);	
	            // Add the events to the list            
	            rows.add(new Item(country.eventTitle, country));
	            previousLetter = firstLetter;
	        }

	       /* if (previousLetter != null) {
	            // Save the last letter
	            tmpIndexItem = new Object[3];
	            tmpIndexItem[0] = previousLetter.toUpperCase(Locale.UK);
	            tmpIndexItem[1] = start;
	            tmpIndexItem[2] = rows.size() - 1;
	            alphabet.add(tmpIndexItem);
	        }*/
	        adapter.setRows(rows);
	        morePerformerListView1.setAdapter(adapter);
		}
		
		/**
	     * Progress Bar
	     * Run only loading time.
	     */
	    public void openProgress(){
	        progress.setMessage("Wait...");
	        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);	      
	        progress.setIndeterminate(true);
	        progress.setCanceledOnTouchOutside(false);	        
	        progress.show();
	    }
	    /**
	     * Pull data by api.
	     * Get Events, Venues and tickets data. 
	     * Run background thread.
	     * @author USER
	     *
	     */
		private class AsyncTaskRunner extends AsyncTask<String, String, String>{
			
			String AsyncTaskRunnerperformCondition="";
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
				    //String api_url = arg0[0];
				    AsyncTaskRunnerperformCondition = arg0[1];
				    
				    try {
				    	int numberOfDownload_per_page = 1;
		  		    	String contentAsString = "";
				    	int numberOfDownload = 0;
				    	int per_page_download = 1000;
				    	do {
				    		String api_url = arg0[0]+"&per_page="+per_page_download+"&page="+numberOfDownload_per_page;
					        //URL url = new URL("http://mobapi.atbss.com/performers?sort=performer.name.desc");	
				    		Log.d("AsyncTaskRunner", "The api_url is: " + api_url);
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
					        if (AsyncTaskRunnerperformCondition.equals("events")) {	
					        	ArrayList<ArrayList<HashMap<String, String>>>    artistTeams_AlleventArrayList =
					      			  new ArrayList<ArrayList<HashMap<String,String>>>();
					        	artistTeams_AlleventArrayList.clear();
						        artistTeams_AlleventArrayList = JSONParse.artistTeamsAllEvents(contentAsString, per_page_download);
						        //Log.d("AsyncTaskRunner", "The artistTeams_AlleventArrayList is: " + artistTeams_AlleventArrayList);
						       // Log.d("AsyncTaskRunner", "The artistTeams_AlleventArrayList.size is: " + artistTeams_AlleventArrayList.size());
						        eventsSortArraylist.clear();
						        for (int i = 0; i < artistTeams_AlleventArrayList.size(); i++) {
						        	ArrayList<HashMap<String, String>> dataArrayList = artistTeams_AlleventArrayList.get(i);
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
									
									// Log.d("ArtistTeamsFragment", "performerId:"+performerId);
									 //Log.d("ArtistTeamsFragment", "performerName:"+performerName);
									//idList.add(Integer.parseInt(id));
									 /*Log.d("ArtistTeamsFragment", "event_id:"+event_id);
									 Log.d("ArtistTeamsFragment", "event_title:"+event_title);
									 Log.d("ArtistTeamsFragment", "event_data_string:"+event_data_string);
									 Log.d("ArtistTeamsFragment", "event_datetime_local:"+event_datetime_local);
									 Log.d("ArtistTeamsFragment", "event_map_static:"+event_map_static);
									 Log.d("ArtistTeamsFragment", "event_venue_name:"+event_venue_name);
									 Log.i("ArtistTeamsFragment", "event_venue_location:"+event_venue_location);*/
									 
									eventsSortArraylist.add(new EventsData(event_id, event_title, event_data_string, 
											event_datetime_local, event_map_static, event_venue_name, event_venue_location,
											event_venue_address,"allevents",performerName,performerId,
											event_venue_postal_code,event_venue_lon,event_venue_lat));
								}
					        
					        }
				    		  numberOfDownload_per_page++;
						} while (numberOfDownload >= numberOfDownload_per_page);

				      

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
				  if (AsyncTaskRunnerperformCondition.equals("events")) {	
					  onDataList();					 
				  }		  
			  }
		   }		
}

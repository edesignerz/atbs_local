package com.dds.fye.tickets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.dds.fye.tickets.EventsFragment.AsyncTaskRunner;
import com.dds.fye.tickets.adapter.EventAlphabetListAdapter;
import com.dds.fye.tickets.adapter.EventAlphabetListAdapter.Item;
import com.dds.fye.tickets.adapter.EventAlphabetListAdapter.Row;
import com.dds.fye.tickets.jsonparse.JSONParse;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class SearchFragmentActivity extends Activity{
	private LinearLayout backButton;
	private EditText dataSerachByVenuEventPerformers;
	private ListView searchResultListView;
	private SharedPreferences preferences ;
	private ConnectionDetector networkInfo;
	private List<EventsData> eventsSortArraylist = new ArrayList<EventsData>();
	private EventAlphabetListAdapter adapter = new EventAlphabetListAdapter();	  
	private List<Object[]> alphabet = new ArrayList<Object[]>();
	private HashMap<String, Integer> sections = new HashMap<String, Integer>();
	private ProgressDialog progress;
	AsyncTaskRunner myWebFetch = new AsyncTaskRunner();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_search_data_from_list);
		  progress = new ProgressDialog(this);
		  preferences = PreferenceManager.getDefaultSharedPreferences(this);	
	      networkInfo = new ConnectionDetector(this);
		  backButton = (LinearLayout)findViewById(R.id.data_search_back_button);
		  dataSerachByVenuEventPerformers = (EditText)findViewById(R.id.event_venu_perfo_search_EditText);
		  dataSerachByVenuEventPerformers.setHint("Search Events");
		  dataSerachByVenuEventPerformers.addTextChangedListener(textWatcher);
		  searchResultListView = (ListView)findViewById(R.id.search_event_page_listView1);
		  
		  
		  searchResultListView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
						long arg3) {
					// TODO Auto-generated method stub
					InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(dataSerachByVenuEventPerformers.getWindowToken(), 0);
					Log.d("EventsFragment", "arg2:"+arg2);
					//Log.d("ArtistTeamsFragment", "arg2:"+rows.get);				
					Item item = (Item) adapter.getItem(arg2);
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
						
						Intent intent = new Intent(SearchFragmentActivity.this, ArtistTeamsEventList.class);
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
		  backButton.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					myWebFetch.cancel(true);
					InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(dataSerachByVenuEventPerformers.getWindowToken(), 0);
					finish();
				}
		 });
		 String selectedCity = preferences.getString("selectcity", "");
		 getVenues(selectedCity);
	}
    /**
     * General method to call loader class thread.
     * Call subclass AsyncTask.
     * For download Data
     */
    public void getVenues(String selectedCity){
    	String sortCode = preferences.getString("event_filter", "event");
    	String radius = preferences.getString("radius_range", "10");
    	//"event.type=conserts&","event.type=theater&";"event.type=sports&";&range=12mi,datetime_local.gte=2013-10-01
    	//sort=distance.desc'

    	if (selectedCity.equals("")) {
  			Log.d("ArtistTeamsFragment", "selectedCity:"+selectedCity);
  			
		}else{
			Log.d("ArtistTeamsFragment", "selectedCity:"+selectedCity);
			Log.d("ArtistTeamsFragment", "radius:"+radius);
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
			    date_ = String.valueOf(yy)+"-"+String.valueOf(mm)+"-"+String.valueOf(dd);
			}
			Log.d("ArtistTeamsFragment", "date_:"+date_);

			String selectCitytName = "";
			selectCitytName = selectedCity.replace(" ", "+");
			//selectCitytName = selectCitytName.replace(",", "");
			Log.d("ArtistTeamsFragment", "selectCirtName:"+selectCitytName);
			String baseUrl = "";
	    	//if (sortCode.equals("event")){
	    		baseUrl = getResources().getString(R.string.artist_teams_events)+"venue.city="+
	    				selectCitytName+"&range="+radius+"mi"+"&datetime_local.gte="+date_;
	    		//even_type="";
			/*}else{
				//even_type = sortCode; 
				baseUrl = getResources().getString(R.string.artist_teams_events)+"venue.city="+
							selectCirtName+"&"+"event.type="+sortCode+"&range="+radius+"mi"+
							"&datetime_local.gte="+date_;
			}*/
			Boolean isInternetPresent = networkInfo.isConnectingToInternet();
			if (isInternetPresent) {
				
		 	     myWebFetch.execute(baseUrl);
			}
		}
    }
    /**
     * Create alphabet list view.
     */
    private void dataList(){
    	 // List<String> countries = populateCountries();
    	 // List<String> countries = nameList;
    	 Log.d("ArtistTeamsFragment", "eventsSortArraylist.size():"+eventsSortArraylist.size());
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
          Log.d("ArtistTeamsFragment", "rows.size():"+rows.size());
          adapter.setRows(rows);
          searchResultListView.setAdapter(adapter);
    }
    
    
    /**
     * TextWatcher handle input text.
     */
   private  TextWatcher textWatcher = new TextWatcher() {
		
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {			
			// TODO Auto-generated method stub
			try {
				if (count%3 == 1) {
					Log.d("SearchFragmentActivity", "count :"+s);
			      	  //adapter.clear();
					//getVenues(s.toString());
					SearchFragmentActivity.this.adapter.getFilter().filter(s);
			    }else{
			    	dataList();
			    }
			} catch (Exception e) {
				// TODO: handle exception
				Log.d("error", "ask"+e);
			}
			
		}
		
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub
			//Log.d("GetText", "String:"+s);
		}
	 };
	 /**
	  *Progress bar 
	  *Run only on loading time.
	  */
	 public void openProgress(){
	        progress.setMessage("Wait...");
	        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);	      
	        progress.setIndeterminate(true);
	        progress.setCanceledOnTouchOutside(false);	        
	        progress.show();
	    }
	    /**
	     * A subclass of AsyncTask that calls events detail in the
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

  		        //String api_url = arg0[0];
  		        //api_url= getResources().getString(R.string.artist_teams);
  		    try {
  		    	int numberOfDownload_per_page = 1;
  		    	String contentAsString = "";
		    	int numberOfDownload = 0;
		    	int per_page_download = 1000;
  		    	do {
  		    		String api_url = arg0[0]+"&per_page="+per_page_download+"&page="+numberOfDownload_per_page;
  		    	//URL url = new URL("http://mobapi.atbss.com/performers?sort=performer.name.desc");	
  	  		    	Log.d("AsyncTaskRunner", "The url : " + api_url);
  	  		    	Log.d("AsyncTaskRunner", "Search Fragment The numberOfDownload_per_page : " + numberOfDownload_per_page);
  	  		    	Log.d("AsyncTaskRunner", "Search Fragment The numberOfDownload : " + numberOfDownload);
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
  	  		      
  	  		        
  	  		        eventsSortArraylist.clear();
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
  		  
  		  dataList();
  		  //updateList();	
  		progress.dismiss();
  	  }
     }
}

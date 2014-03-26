package com.dds.fye.tickets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.dds.fye.tickets.ArtistTeamsFragment.AsyncTaskRunner;
import com.dds.fye.tickets.adapter.ArtistTeamsAlphabetListAdapter.Item;
import com.dds.fye.tickets.adapter.ArtistTeamsAlphabetListAdapter.Row;
import com.dds.fye.tickets.adapter.ArtistTeamsAlphabetListAdapter.Section;
import com.dds.fye.tickets.adapter.ArtistTeamsAlphabetListAdapter;
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

public class PerformerSearchFragmentActivity extends Activity{
	
    private ArtistTeamsAlphabetListAdapter adapter = new ArtistTeamsAlphabetListAdapter();
    private List<Object[]> alphabet = new ArrayList<Object[]>();
    private HashMap<String, Integer> sections = new HashMap<String, Integer>();
   
   // LinearLayout sideIndex ;
    boolean condition  = true;
    private ConnectionDetector networkInfo;
    private List<EventsData> eventsSortArraylist = new ArrayList<EventsData>();
    private SharedPreferences preferences ;
    
    private LinearLayout backButton;
	private EditText dataSerachByVenuEventPerformers;
	private ListView searchResultListView;
	private ProgressDialog progress;
	AsyncTaskRunner task = new AsyncTaskRunner();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_search_data_from_list);
		  progress = new ProgressDialog(this);
		  preferences = PreferenceManager.getDefaultSharedPreferences(this);	
	      networkInfo = new ConnectionDetector(this);
		  backButton = (LinearLayout)findViewById(R.id.data_search_back_button);
		  dataSerachByVenuEventPerformers = (EditText)findViewById(R.id.event_venu_perfo_search_EditText);
		  dataSerachByVenuEventPerformers.setHint("Search All Performers");
		  dataSerachByVenuEventPerformers.addTextChangedListener(textWatcher);
		  searchResultListView = (ListView)findViewById(R.id.search_event_page_listView1);
		 			
		  searchResultListView.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
							long arg3) {
						// TODO Auto-generated method stub
						Log.d("ArtistTeamsFragment", "arg2:"+arg2);
						//Log.d("ArtistTeamsFragment", "arg2:"+rows.get);
						try {
							Item item = (Item) adapter.getItem(arg2);
							//Log.d("ArtistTeamsFragment", "item.text:"+item.text);
							EventsData data = item.eventsData;
							Intent intent = new Intent(PerformerSearchFragmentActivity.this, ArtistTeamsEventList.class);
							intent.putExtra("name", item.text);
							intent.putExtra("performer_name",data.eventPerformerName);
							intent.putExtra("performer_id", data.eventsPerformerId);
							intent.putExtra("call_class", "artist_teams");
							intent.putExtra("listViewPosition", String.valueOf(arg2));
					        startActivity(intent);
						} catch (Exception e) {
							// TODO: handle exception
						}
					}
				});
		  backButton.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(dataSerachByVenuEventPerformers.getWindowToken(), 0);
					task.cancel(true);
					ArtistTeamsFragment.upDateList();
					finish();
				}
		 });
		  getArtistTeams();
	}

    /**
     * General method to call loader class thread.
     * For download Data
     */
	public void getArtistTeams(){
        Boolean isInternetPresent = networkInfo.isConnectingToInternet();
			Log.d("ArtistTeamsFragment", "isInternetPresent:"+isInternetPresent);
		if (isInternetPresent) {
			String selectedCity = preferences.getString("selectcity", "");
			if (!selectedCity.equals("")) {
				AsyncTaskRunner myWebFetch = new AsyncTaskRunner();
	 	 	     myWebFetch.execute();
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
			Collections.sort(eventsSortArraylist, new Comparator<EventsData>(){
			      public int compare(EventsData obj1, EventsData obj2)
			      {
			            // TODO Auto-generated method stub
			            return obj1.eventTitle.compareToIgnoreCase(obj2.eventTitle);
			      }
			});
			//Remove duplicate value.
			removeDuplicateWithOrder(eventsSortArraylist);
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
              if (numberPattern.matcher(firstLetter).matches() || firstLetter.equals(".") || firstLetter.equals("!")) {
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
              if (!firstLetter.equals(previousLetter)) {
                  rows.add(new Section(firstLetter));
                  sections.put(firstLetter, start);
              }

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
     * Remove duplicate value.
     * @param venuesSortArraylist2
     */
    private void removeDuplicateWithOrder(List<EventsData> venuesSortArraylist2){
    	Set<String> set = new HashSet<String>();
    	List<EventsData> newList = new ArrayList<EventsData>();
    	for (Iterator<EventsData> iter = venuesSortArraylist2.iterator();    iter.hasNext(); ) {
    		EventsData element = iter.next();
    		if (set.add(element.eventTitle)){
    			newList.add(element);
    		}
    	}	    	
    	venuesSortArraylist2.clear();
    	venuesSortArraylist2.addAll(newList);
   }
    /**
     * TextWatcher handle input text.
     * Filter list.
     */
   private  TextWatcher textWatcher = new TextWatcher() {
		
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {			
			// TODO Auto-generated method stub
			try {
				if (count%3 == 1) {
					Log.d("SearchFragmentActivity", "count :"+s);
			      	  //adapter.clear();
					  PerformerSearchFragmentActivity.this.adapter.getFilter().filter(s);		             
		              //now pass the argument in the textview to the task
		              //task.execute(symbolText.getText().toString());
		             // task.execute(s.toString());
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
	     * A subclass of AsyncTask that calls venue detail in the
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
			    //Log.d("AsyncTaskRunner", "The arg0[0] : " + arg0[0]);
			    //String performerName = arg0[0];
			    	String selectedCity = preferences.getString("selectcity", "");
					String[] citySplite = selectedCity.split("[,]");				
					String[] spaceFreeCity = citySplite[0].split("[ ]");
		/*			String selectCirtName = "";
					for (String string : spaceFreeCity) {
						if (selectCirtName.equals("")) {
							selectCirtName = string;
						}else{
							selectCirtName = selectCirtName+"+"+string;
						}					
					}*/
					String selectCitytName = "";
					selectCitytName = selectedCity.replace(" ", "+");
					//selectCitytName = selectCitytName.replace(",", "");
					Log.d("ArtistTeamsFragment", "selectCirtName:"+selectCitytName);
					/*String[] performerName_ =  performerName.split("[ ]");
					String performers_name = "";
					for (String string : performerName_) {
						if (selectCitytName.equals("")) {
							performers_name = string;
						}else{
							performers_name = performers_name+"+"+string;
						}					
					}*/
			        //String api_url = getResources().getString(R.string.artist_teams_events);
			        
			        //api_url= getResources().getString(R.string.artist_teams);
			        //http://mobapi.atbss.com/events?performer.name=atlanta+braves&performer.home=1
					
			    try {
			    	int numberOfDownload_per_page = 1;
	  		    	String contentAsString = "";
			    	int numberOfDownload = 0;
			    	int per_page_download = 1000;
			    	String baseUrl = getResources().getString(R.string.artist_teams_events);
			    	do {
			    		 /*String api_url = getResources().getString(R.string.artist_teams_events)+
			    				 "performer.name="+performers_name+"&venue.city="+selectCitytName+
			    				 "&per_page="+per_page_download+"&page="+numberOfDownload_per_page;*/
			    		 String api_url = baseUrl +"venue.city="+selectCitytName+
				    				"&per_page="+per_page_download+"&page="+numberOfDownload_per_page;
			    		 
			    		//URL url = new URL("http://mobapi.atbss.com/performers?sort=performer.name.desc");	
				    	Log.d("AsyncTaskRunner", "The url : " + api_url);
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

							String eventperformerName_ = "";
							String performerId = "";
							for (int j = 2; j < dataArrayList.size(); j++) {
								HashMap<String, String> hashMap_ = dataArrayList.get(j);
								if (eventperformerName_.equals("") ) {
									eventperformerName_ = hashMap_.get("name");
								}else{
									eventperformerName_ = eventperformerName_ + ","+hashMap_.get("name");
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
									event_venue_location,event_venue_address,"",eventperformerName_,performerId,
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

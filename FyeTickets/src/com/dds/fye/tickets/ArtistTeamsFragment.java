package com.dds.fye.tickets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;


import com.dds.fye.tickets.adapter.ArtistTeamsAlphabetListAdapter.Item;
import com.dds.fye.tickets.adapter.ArtistTeamsAlphabetListAdapter.Row;
import com.dds.fye.tickets.adapter.ArtistTeamsAlphabetListAdapter.Section;
import com.dds.fye.tickets.adapter.ArtistTeamsAlphabetListAdapter;
import com.dds.fye.tickets.jsonparse.JSONParse;

import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.app.ListActivity;
import android.app.ListFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.CalendarContract.Colors;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SectionIndexer;
import android.widget.TextView;
import android.widget.ViewFlipper;

public class ArtistTeamsFragment extends Fragment{
	
	
    private static ArtistTeamsAlphabetListAdapter adapter = new ArtistTeamsAlphabetListAdapter();
    //private GestureDetector mGestureDetector;
    private List<Object[]> alphabet = new ArrayList<Object[]>();
    private HashMap<String, Integer> sections = new HashMap<String, Integer>();
    private int sideIndexHeight;
    private static float sideIndexX;
    private static float sideIndexY;
    private int indexListSize;
    //private List<String> nameList = new ArrayList<String>();
    private ListView myListView;
   // LinearLayout sideIndex ;
    boolean condition  = true;
    private boolean isTaskRunning = false;
    private ProgressDialog progressDialog;
    private ConnectionDetector networkInfo;
    private List<EventsData> eventsSortArraylist = new ArrayList<EventsData>();
    private SharedPreferences preferences ;
    /**
     * Create a new instance of DetailsFragment, initialized to
     * show the text at 'index'.
     */
    ProgressDialog progress;
    public ArtistTeamsFragment(){}

    /*class SideIndexGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            sideIndexX = sideIndexX - distanceX;
            sideIndexY = sideIndexY - distanceY;

            if (sideIndexX >= 0 && sideIndexY >= 0) {
                displayListItem();
            }

            return super.onScroll(e1, e2, distanceX, distanceY);
        }
    }*/
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);       	
		Log.d("AsyncTaskRunner", "hari: ");
    }
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // If we are returning here from a screen orientation
        // and the AsyncTask is still working, re-create and display the
        // progress dialog.
        //if (isTaskRunning) {
           // progressDialog = ProgressDialog.show(getActivity(), "Loading", "Please wait a moment!");
       // }
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {	  
        View rootView = inflater.inflate(R.layout.artist_teams, container, false); 
        progress = new ProgressDialog(getActivity());
        preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());	
       // mGestureDetector = new GestureDetector(getActivity(), new SideIndexGestureListener());
        networkInfo = new ConnectionDetector(getActivity());	  
     // listview
        myListView = (ListView) rootView.findViewById(R.id.artist_teams_listView1);
        myListView.setFastScrollEnabled(true);
       // sideIndex = (LinearLayout) rootView.findViewById(R.id.sideIndex);	
        myListView.setOnItemClickListener(new OnItemClickListener() {

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
					Intent intent = new Intent(getActivity(), ArtistTeamsEventList.class);
					intent.putExtra("name", item.text);
					intent.putExtra("performer_name", data.eventPerformerName);
					intent.putExtra("performer_id", data.eventsPerformerId);
					intent.putExtra("call_class", "artist_teams");
					intent.putExtra("listViewPosition", String.valueOf(arg2));
			        startActivity(intent);
				} catch (Exception e) {
					// TODO: handle exception
				}

			}
		});
        Log.d("ArtistTeamsFragment", "eventsSortArraylist.size():"+eventsSortArraylist.size());
        if (eventsSortArraylist.size()!=0) {
        	//dataList();
      		//updateList();
 		 }else{
 					
 		 }
        getArtistTeams();
        return rootView;
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
  /*  public List<EventsData> getArtistTeamsData(){
    	return eventsSortArraylist;
    }*/
    /**
     * Create alphabet list view.
     * Interact data with list view.
     */
    public void dataList(){
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
			// Remove duplicates value.
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
             // adapter.setRows(rows);
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
          myListView.setAdapter(adapter);
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
     * A subclass of AsyncTask that calls Artist Teams in the
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
		    	String selectedCity = preferences.getString("selectcity", "");
		    	Log.d("AsyncTaskRunner", "The artist teams  selectedCity : " + selectedCity);
				String[] citySplite = selectedCity.split("[,]");				
				String[] spaceFreeCity = citySplite[0].split("[ ]");
				String selectCiteName = "";
				/*for (String string : spaceFreeCity) {
					if (selectCirtName.equals("")) {
						selectCirtName = string;
					}else{
						selectCirtName = selectCirtName+"+"+string;
					}					
				}*/
				
				selectCiteName = selectedCity.replace(" ", "+");
				//selectCiteName = selectCiteName.replace(",", "");
		       // String api_url = getResources().getString(R.string.artist_teams_events);
		       // api_url = api_url+"&venue.city="+selectCirtName;//+"page=1&per_page=10";
		        //api_url= getResources().getString(R.string.artist_teams);
				 eventsSortArraylist.clear();
		    try {
		    	int numberOfDownload_per_page = 1;
  		    	String contentAsString = "";
		    	int numberOfDownload = 0;
		    	int per_page_download = 500;
		    	String baseUrl = getResources().getString(R.string.artist_teams_events);
		    	do {
		    		String api_url = baseUrl +"venue.city="+selectCiteName+
		    				"&per_page="+per_page_download+"&page="+numberOfDownload_per_page;
		    		 //URL url = new URL("http://mobapi.atbss.com/performers?sort=performer.name.desc");	
			    	Log.d("AsyncTaskRunner", "The artist teams  url : " + api_url);
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
			        
			       // Log.d("AsyncTaskRunner", "The contentAsString is: " + contentAsString);		        
			        ArrayList<ArrayList<HashMap<String, String>>> artistTeamsArrayList= new ArrayList<ArrayList<HashMap<String, String>>>();
			        artistTeamsArrayList.clear();
			        artistTeamsArrayList =  JSONParse.artistTeamsAllEvents(contentAsString,per_page_download);
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
		  dataList();
		  //updateList();		 
	  }
   }
}

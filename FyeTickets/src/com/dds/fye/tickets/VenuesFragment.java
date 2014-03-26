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
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.dds.fye.tickets.EventsFragment.AsyncTaskRunner;
import com.dds.fye.tickets.adapter.VenueAlphabetListAdapter.Item;
import com.dds.fye.tickets.adapter.VenueAlphabetListAdapter.Row;
import com.dds.fye.tickets.adapter.VenueAlphabetListAdapter.Section;
import com.dds.fye.tickets.adapter.VenueAlphabetListAdapter;
import com.dds.fye.tickets.jsonparse.JSONParse;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class VenuesFragment extends Fragment { 
	  private static  VenueAlphabetListAdapter adapter = new VenueAlphabetListAdapter();
	  private List<Object[]> alphabet = new ArrayList<Object[]>();
	  private HashMap<String, Integer> sections = new HashMap<String, Integer>();
	  private GestureDetector mGestureDetector;	 
	  private int sideIndexHeight;
	  private static float sideIndexX;
	  private static float sideIndexY;
	  private int indexListSize;
	 // private List<String> nameList = new ArrayList<String>();
	  private ListView myListView;
	  private LinearLayout sideIndex ;
	  private SharedPreferences preferences ;
	  private List<String> nameList = new ArrayList<String>();
	  private List<VenueData> venuesSortArraylist = new ArrayList<VenueData>();
	  private ConnectionDetector networkInfo;
	  ProgressDialog progress;
	 public VenuesFragment(){}

	    @Override
	    public View onCreateView(LayoutInflater inflater, ViewGroup container,
	            Bundle savedInstanceState) {	  
	        View rootView = inflater.inflate(R.layout.venues, container, false);
	        progress = new ProgressDialog(getActivity());
	        preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());	
	        networkInfo = new ConnectionDetector(getActivity());
	        //listview
	         myListView = (ListView) rootView.findViewById(R.id.venue_listView1_);
	         myListView.setFastScrollEnabled(true);
	      	 myListView.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						// TODO Auto-generated method stub
						Log.d("ArtistTeamsFragment", "arg2:"+arg2);
						try {
							//Log.d("ArtistTeamsFragment", "arg2:"+rows.get);				
							Item item = (Item) adapter.getItem(arg2);
							VenueData data = item.venueData;
							/*Log.d("ArtistTeamsFragment", "VenueEventId:"+data.VenueEventId);
							Log.d("ArtistTeamsFragment", "VenueEventTitle:"+data.VenueEventTitle);
							
							Log.d("ArtistTeamsFragment", "VenueId:"+data.VenueId);
							Log.d("ArtistTeamsFragment", "VenueName:"+data.VenueName);
							Log.d("ArtistTeamsFragment", "VenueLocation:"+data.VenueLocation);
							Log.d("ArtistTeamsFragment", "VenueAddress:"+data.VenueAddress);
							Log.d("ArtistTeamsFragment", "VenueCity:"+data.VenueCity);
							Log.d("ArtistTeamsFragment", "VenuePerformerName:"+data.VenuePerformerName);
							Log.d("ArtistTeamsFragment", "VenuePerformerId:"+data.VenuePerformerId);*/

							//Log.d("ArtistTeamsFragment", "item.text:"+item.text);
							Intent intent = new Intent(getActivity(), ArtistTeamsEventList.class);
							intent.putExtra("eventTitle", data.VenueEventTitle);
							intent.putExtra("venuName", data.VenueName);
							intent.putExtra("venuLocation", data.VenueLocation);
							intent.putExtra("call_class", "venues");
							intent.putExtra("listViewPosition", String.valueOf(arg2));
					        startActivity(intent);
						} catch (Exception e) {
							// TODO: handle exception
						}						
					}
				}); 
	      		getVenues();
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
	    	//String sortCode = preferences.getString("event_filter", "event");
	    	String selectedCity = preferences.getString("selectcity", "");
	    	/*Boolean isInternetPresent = networkInfo.isConnectingToInternet();
			if (isInternetPresent) {
				AsyncTaskRunner myWebFetch = new AsyncTaskRunner();
		 	     myWebFetch.execute("atlanta");
			}*/
	    	if (selectedCity.equals("")) {
      			Log.d("ArtistTeamsFragment", "selectedCity:"+selectedCity);
      			/*Boolean isInternetPresent = networkInfo.isConnectingToInternet();
				if (isInternetPresent) {
					AsyncTaskRunner myWebFetch = new AsyncTaskRunner();
			 	     myWebFetch.execute("atlanta");
				}*/
			}else{
				Log.d("ArtistTeamsFragment", "selectedCity:"+selectedCity);
				Log.d("ArtistTeamsFragment", "selectedCity:"+selectedCity);
				
				//String[] citySplite = selectedCity.split("[ ]");
				String[] citySplite = selectedCity.split("[,]");				
				String[] spaceFreeCity = citySplite[0].split("[ ]");
				String selectCityName = "";
				/*for (String string : spaceFreeCity) {
					if (selectCityName.equals("")) {
						selectCityName = string;
					}else{
						selectCityName = selectCityName+"+"+string;
					}					
				}*/
				selectCityName = selectedCity.replace(" ", "+");
				//selectCityName = selectCityName.replace(",", "");
				String baseUrl = "";
		    	//if (sortCode.equals("event")){
		    		//baseUrl = getResources().getString(R.string.artist_teams_events)+"venue.city="+selectCityName;
		    		//even_type="";
				//}else{
					//even_type = sortCode; 
					baseUrl = getResources().getString(R.string.artist_teams_events)+"venue.city="+selectCityName;//+"&sort=datetime_local.asc";//+"&"+"event.type="+sortCode;
				//}
				Boolean isInternetPresent = networkInfo.isConnectingToInternet();
				Log.d("ArtistTeamsFragment", "isInternetPresent:"+isInternetPresent);
				if (isInternetPresent) {
					AsyncTaskRunner myWebFetch = new AsyncTaskRunner();
			 	     myWebFetch.execute(baseUrl);
				}
			}
	    }

	    /**
	     * Create alphabet list view.
	     * Interact data with list view.
	     */
	    public void dataList(){
	    	 // List<String> countries = populateCountries();
	    	 // List<String> countries = nameList;
	    	 Log.d("ArtistTeamsFragment", "eventsSortArraylist.size():"+venuesSortArraylist.size());
	    	 //Log.d("ArtistTeamsFragment", "eventsSortArraylist:"+eventsSortArraylist);
	          //Collections.sort(nameList);
				Collections.sort(venuesSortArraylist, new Comparator<VenueData>(){
				      public int compare(VenueData obj1, VenueData obj2)
				      {
				            // TODO Auto-generated method stub
				            return obj1.VenueName.compareToIgnoreCase(obj2.VenueName);
				      }
				});                                                                            
               // Remove duplicates value.
				removeDuplicateWithOrder(venuesSortArraylist);
	
	          List<Row> rows = new ArrayList<Row>();
	          rows.clear();
	          int start = 0;
	          int end = 0;
	          String previousLetter = null;
	          Object[] tmpIndexItem = null;
	          Pattern numberPattern = Pattern.compile("[0-9]");
	          for (VenueData country : venuesSortArraylist) {  
	          //for (VenueData country : listWithoutDuplicates) { 
	              String firstLetter = country.VenueName.substring(0, 1);

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
	              if (!firstLetter.equals(previousLetter)) {
	                  rows.add(new Section(firstLetter));
	                  sections.put(firstLetter, start);
	              }

	              // Add the country to the list
	              rows.add(new Item(country.VenueName, country));
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
	          Log.d("ArtistTeamsFragment", "sections.size():"+sections.size());
	          Log.d("ArtistTeamsFragment", "rows.size():"+rows.size());
	          adapter.setRows(rows);
	          myListView.setAdapter(adapter);
	    }	
	    /**
	     * Remove duplicate value.
	     * @param venuesSortArraylist2
	     */
	    private void removeDuplicateWithOrder(List<VenueData> venuesSortArraylist2){
	    	Set<String> set = new HashSet<String>();
	    	List<VenueData> newList = new ArrayList<VenueData>();
	    	for (Iterator<VenueData> iter = venuesSortArraylist2.iterator();    iter.hasNext(); ) {
	    		VenueData element = iter.next();
	    		if (set.add(element.VenueName)){
	    			newList.add(element);
	    		}
	    	}	    	
	    	venuesSortArraylist2.clear();
	    	venuesSortArraylist2.addAll(newList);
	   }
	    /**
	     * Progress Bar.
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
	     * A subclass of AsyncTask that calls Venue in the
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
	  		   /* String city = arg0[0];
	  		     String[] citySplite = city.split("[ ]");
	  		    // web page content.
	  		    int len = 500;
	  		       // String api_url = getResources().getString(R.string.artist_teams_venues);	
	  		    	String api_url = getResources().getString(R.string.artist_teams_events);
	  		       // api_url = api_url+"venue.city="+"Atlanta";
	  		       api_url = api_url+"venue.city="+citySplite[0];*/
	  		    // String api_url = arg0[0]+"&per_page=100";
	  		   venuesSortArraylist.clear();
	  		    try {
	  		    	int numberOfDownload_per_page = 1;
	  		    	String contentAsString = "";
			    	int numberOfDownload = 0;
			    	 int per_page_download = 1000;
	  		    	do {
	  		    		 //URL url = new URL("http://mobapi.atbss.com/performers?sort=performer.name.desc");
	  		    		String api_url = arg0[0]+"&per_page="+per_page_download+"&page="+numberOfDownload_per_page;
				    	Log.d("AsyncTaskRunner", "The url : " + api_url);
		  		    	Log.d("AsyncTaskRunner", "The url :" + api_url);
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
		  		      /* ArrayList<HashMap<String, String>>    allVenuesArrayList = new ArrayList<HashMap<String,String>>();
		  		       allVenuesArrayList.clear();
		  		       allVenuesArrayList = JSONParse.venuesParse(contentAsString);*/
		  		      
				        for (int i = 0; i < artistTeamsArrayList.size(); i++) {
				        	ArrayList<HashMap<String, String>> dataArrayList = artistTeamsArrayList.get(i);
				        	
				        	HashMap<String, String> hashMap = dataArrayList.get(0);
				        	String VenueEventId = hashMap.get("id");
							String VenueEventTitle = hashMap.get("title");
							numberOfDownload = Integer.parseInt(hashMap.get("numberOfDownload"));
				        	
							hashMap = dataArrayList.get(1);
							String VenueId = hashMap.get("id");
							String VenueName = hashMap.get("name");
							String VenueLocation = hashMap.get("location");
							String VenueCity = hashMap.get("city");
							String VenueState = hashMap.get("state");
							String VenuePostalCode = hashMap.get("postal_code");
							String VenueCountry = hashMap.get("country");
							String VenueAddress = hashMap.get("address");
							String VenueLat = hashMap.get("lat");
							String VenueLong = hashMap.get("lon");
							
							String VenuePerformerName_ = "";
							String VenuePerformerId_ = "";
							for (int j = 2; j < dataArrayList.size(); j++) {
								hashMap = dataArrayList.get(j);
								String VenuePerformerName = hashMap.get("name");
								String VenuePerformerId = hashMap.get("id");
								if (VenuePerformerName_.equals("")) {
									VenuePerformerName_ = VenuePerformerName;
								}else{
									VenuePerformerName_ = VenuePerformerName_+"-"+VenuePerformerName;
								}
								if (VenuePerformerId_.equals("")) {
									VenuePerformerId_ = VenuePerformerId;
								}else{
									VenuePerformerId_ = VenuePerformerId_+"-"+VenuePerformerId;
								}
							}						
							
							 /*Log.d("ArtistTeamsFragment", "VenueId:"+VenueId);
							 Log.d("ArtistTeamsFragment", "VenueName:"+VenueName);
							 Log.d("ArtistTeamsFragment", "VenueLocation:"+VenueLocation);
							 Log.d("ArtistTeamsFragment", "VenueCity:"+VenueCity);
							 Log.d("ArtistTeamsFragment", "VenueState:"+VenueState);
							 Log.d("ArtistTeamsFragment", "VenuePostalCode:"+VenuePostalCode);
							 Log.i("ArtistTeamsFragment", "VenueCountry:"+VenueCountry);
							 Log.d("ArtistTeamsFragment", "VenueAddress:"+VenueAddress);
							 Log.d("ArtistTeamsFragment", "VenueLat:"+VenueLat);
							 Log.i("ArtistTeamsFragment", "VenueLong:"+VenueLong);*/
							 
							 venuesSortArraylist.add(new VenueData(VenueEventId,VenueEventTitle,VenueId, VenueName, VenueLocation,VenueCity, VenueState,
									 VenuePostalCode, VenueCountry,VenueAddress,VenueLat,VenueLong,VenuePerformerName_,VenuePerformerId_));
						}
				        numberOfDownload_per_page++;
					} while (numberOfDownload >= numberOfDownload_per_page);
	  		       
	  		        Log.d("AsyncTaskRunner", "The venuesSortArraylist.size is: " + venuesSortArraylist.size());
	  		        //ArrayList<Integer> idList = new ArrayList<Integer>();	
	  		       /* nameList.clear();
	  		        for (int i = 0; i < artistTeamsArrayList.size(); i++) {
	  					HashMap<String, String> hashMap = artistTeamsArrayList.get(i);
	  					String id = hashMap.get("id");
	  					String name = hashMap.get("name");
	  					//idList.add(Integer.parseInt(id));
	  					nameList.add(name);
	  				}*/	
	  		        //Collections.sort(idList);		       	       
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

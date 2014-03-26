package com.dds.fye.tickets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.dds.fye.tickets.adapter.LocationSearchAdapter;

import android.app.Activity;
import android.content.Context;
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
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class SearchLocation extends Activity{
	 private ArrayAdapter<String> adapter;
	 private AutoCompleteTextView autoCompleteTextView ;
	 private  ListView cityListView;
	// private ArrayAdapter<String> listAdapter;
	 private LocationSearchAdapter listAdapter;
	 private ArrayList<String> predictionsSymbolArr = new ArrayList<String>();
	 private ArrayList<String> cityListStore = new ArrayList<String>();
	 private SharedPreferences preferences ;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.location_search_city_zip);
		 preferences = PreferenceManager.getDefaultSharedPreferences(this);
		 LinearLayout cancel = (LinearLayout)findViewById(R.id.location_back_button);
		  autoCompleteTextView = (AutoCompleteTextView)
				  findViewById(R.id.city_zip_search_autoCompleteTextView1);
		  adapter = new ArrayAdapter<String>(this, R.layout.location_item_row);
		  autoCompleteTextView.setThreshold(1);
	      adapter.setNotifyOnChange(true);
	      autoCompleteTextView.setAdapter(adapter);
	      autoCompleteTextView.addTextChangedListener(textWatcher);
	      autoCompleteTextView.setOnItemClickListener(mMessageClickedHandler);
	     // makeArrayList
	      String cityList = preferences.getString("search_city", "Current Location");
	      makeArrayList(cityList);
	      cityListView =(ListView)findViewById(R.id.city_name_listView1); 
	      //listAdapter =  new ArrayAdapter<String>(getActivity(), R.layout.list_item);
	      listAdapter = new LocationSearchAdapter(this,cityListStore);
	      //listAdapter.setNotifyOnChange(true);
	      cityListView.setAdapter(listAdapter);
	      for (String string : cityListStore) {
	    	   //listAdapter.add(string);
		  }
	    
	      listAdapter.notifyDataSetChanged();
		  
		  cityListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(autoCompleteTextView.getWindowToken(), 0);
				String selectcity = "";
				if (arg2 == 0) {
					selectcity = preferences.getString("user_current_location", "Current Location");
				}else{
					selectcity = cityListStore.get(arg2);
				}
				Log.d("SearchLocation", "selectcity :" + selectcity);
				Log.d("SearchLocation", "selectcity :" + selectcity.length());
				SharedPreferences.Editor editor = preferences.edit();
	    		editor.putString("selectcity",selectcity);	        		
	    		editor.commit();	    		
	    	
	    		finish();
			}
		  });
		  
		  cancel.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(autoCompleteTextView.getWindowToken(), 0);
				finish();
			}
		  });
	}
	/*private void callParentClassMethod(){
		((MainActivity) this).callBackClass();
	}*/
	 /**
     * AutoCompleteTextView listener.
     */
    private OnItemClickListener mMessageClickedHandler = new 
            OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					// TODO Auto-generated method stub
					Log.d("RMFinanceActivity", "hello:"+arg2);
					Log.d("RMFinanceActivity", "predictionsSymbolArr.get(arg2):"+predictionsSymbolArr.get(arg2));
					//autoCompleteTextView.setText(predictionsSymbolArr.get(arg2));	
					//arg0.get
					autoCompleteTextView.removeTextChangedListener(textWatcher);
					autoCompleteTextView.dismissDropDown();
					 //update the adapter
					listAdapter = new LocationSearchAdapter(SearchLocation.this,cityListStore);
					//listAdapter.add(predictionsSymbolArr.get(arg2));
					cityListView.setAdapter(listAdapter);
					listAdapter.notifyDataSetChanged();
				    cityListStore.add(predictionsSymbolArr.get(arg2));
				    Log.d("RMFinanceActivity", "MyAlertDialogWIndow cityListStore:"+cityListStore);
				    String search_city = "";
				    for (String string : cityListStore) {
				    	if (search_city.equals("")) {
				    		search_city=string;
						}else{
							search_city=search_city+"-"+string;
						}
				    	
					}
										
					Log.d("RMFinanceActivity", "MyAlertDialogWIndow search_city:"+search_city);
					//search_city = search_city.replace("[", "");
					//search_city = search_city.replace("]", "");
					Log.d("RMFinanceActivity", "search_city:"+search_city);
					
					SharedPreferences.Editor editor = preferences.edit();
	        		editor.putString("search_city",search_city);	        		
	        		editor.commit();
				}    	
    };
	    /**
	     * TextWatcher handle input text.
	     */
	   private  TextWatcher textWatcher = new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				//Log.d("RMFinanceActivity", "count :"+s);
				// TODO Auto-generated method stub
				try {
					if (count%3 == 1) {
				      	  adapter.clear();
			              GetPlaces task = new GetPlaces();
			              //now pass the argument in the textview to the task
			              //task.execute(symbolText.getText().toString());
			              task.execute(s.toString());
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
		  * A subclass of AsyncTask that calls location detail in the
	  	  * background. The class definition has these generic types:
	      * Download data from api.
	      * Run on background.
		  * This class execute in background.
		  * Get the data for auto complete.
		  * 
		  */
		  class GetPlaces extends AsyncTask<String, Void, ArrayList<String>>{

		  @Override
		   // three dots is java for an array of strings
		  protected ArrayList<String> doInBackground(String... args) {

		  ArrayList<String> predictionsArr = new ArrayList<String>();
		 // http://maps.googleapis.com/maps/api/geocode/json?address=Atlanta&sensor=true
		  try {	        
			          //Log.d("RMFinanceActivity", "region :" + region);
			         // String baseurl = "http://maps.googleapis.com/maps/api/geocode/json?";
			  		  String baseurl = getResources().getString(R.string.search_google_api_);			        
			          String address = "address="+args[0];
			          String str = baseurl+address+"&sensor=true";	         
			          Log.d("urlString", "str :" + str);	   
			          //URL googlePlaces = new URL(str); 
			          URL googlePlaces = new URL(str);
			       
			          URLConnection tc = googlePlaces.openConnection();	         
		              BufferedReader in = new BufferedReader(new InputStreamReader(
		                      tc.getInputStream()));            

		              String line;
		              StringBuffer sb = new StringBuffer();
		              //take Google's legible JSON and turn it into one big string.
		              while ((line = in.readLine()) != null) {          	  
		                  sb.append(line);
		              }
		              
		              String jsonValue = sb.toString();
		             // Log.d("RMFinnaceActivity", "json string"+jsonValue);
		              int myIndex= 0;             
		              myIndex = jsonValue.indexOf("(");
		             // Log.d("RMFinanceActivity","indax no:"+myIndex);
		              String strOutput = jsonValue.replace(jsonValue.substring(0,myIndex+1),"");             
		              String jsonString = strOutput.replace(')', ' ');
		              Log.d("YourApp", "string sing:"+jsonString);              
		              //turn that string into a JSON object
		              JSONObject predictionsjsonObject = new JSONObject(jsonValue);
		              //now get the JSON array that's inside that object           
		              //JSONObject jMenuObject = predictionsjsonObject.getJSONObject("ResultSet");
		              JSONArray jArray = predictionsjsonObject.getJSONArray("results");
		              //Log.d("YourApp", " json array length:"+jArray.length());
		              predictionsSymbolArr.clear();
		              for (int i = 0; i < jArray.length(); i++) {
		                      JSONObject jo = (JSONObject) jArray.get(i);
		                                      //add each entry to our array
		                      String city = jo.getString("formatted_address");
		                      city = city.replace(" ", "");
		                      String[] citysplite = city.split("[,]");
		                      String cityNeames = "";
		                      for (int j = 0; j < citysplite.length-1; j++) {
								if (cityNeames.equals("")) {
									cityNeames = citysplite[j];
								}else{
									cityNeames = cityNeames+", "+citysplite[j];
								}
		                      }
 		                      predictionsArr.add(cityNeames);
		                      predictionsSymbolArr.add(cityNeames);
		                      Log.d("YourApp", "jo.getString(description):"+predictionsArr.get(0));
		              }
		  } catch (IOException e){
		     Log.e("YourApp", "GetPlaces 1: doInBackground", e);
		  } catch (JSONException e){
		     Log.e("YourApp", "GetPlaces 2: doInBackground:", e);
		  }
		  return predictionsArr;
		  }

		  //then our post 
		  /**
		   * Set symbol in autocomplete editText.
		   * symbolText autocomplete.
		   */
		  @Override
		  protected void onPostExecute(ArrayList<String> result){

		    Log.d("YourApp", "onPostExecute : " + result.size());
		    //update the adapter
		    adapter = new ArrayAdapter<String>(SearchLocation.this, R.layout.location_item_row);
		    adapter.setNotifyOnChange(true);
		    //attach the adapter to textview
		    autoCompleteTextView.setAdapter(adapter);
		    for (String string : result){
		      Log.d("YourApp", "onPostExecute : result = "+string);
		      adapter.add(string);
		      adapter.notifyDataSetChanged();
		    }    
		    Log.d("YourApp", "onPostExecute : autoCompleteAdapter:");
		   }
		  }
		  
		  private void makeArrayList(String dataString){
			  Log.d("YourApp", "onPostExecute : dataString:"+dataString);
			 cityListStore.clear();		
			 if (!dataString.equals("")) {
				 String[] dataStringSplit = dataString.split("[-]");
					for (String string : dataStringSplit) {
						cityListStore.add(string);
					}	
			}
		  }

}

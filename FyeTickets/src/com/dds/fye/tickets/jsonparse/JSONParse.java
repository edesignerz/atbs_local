package com.dds.fye.tickets.jsonparse;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class JSONParse {
  private static ArrayList<HashMap<String, String>> artistTeamsArrayList = 
		  new ArrayList<HashMap<String, String>>();	
  private static ArrayList<ArrayList<HashMap<String, String>>>    artistTeams_AlleventArrayList =
		  new ArrayList<ArrayList<HashMap<String,String>>>();
  private static ArrayList<HashMap<String, String>> ticketArrayList = 
		  new ArrayList<HashMap<String, String>>();	
 
  /**
   * parse artist and teams
   * @param jsonString
   * @return
   */
  public static ArrayList<HashMap<String, String>> artisTeamsJsonParse(String jsonString){
	  if (jsonString != null) {
		  try {

			  JSONObject jsonObject = new JSONObject(jsonString);
			  // Getting JSON Array node
              JSONArray  artistTeamsArray = jsonObject.getJSONArray("performers");
              artistTeamsArrayList.clear();
              // looping through All Contacts
              for (int i = 0; i < artistTeamsArray.length(); i++) {
            	  JSONObject art_team = artistTeamsArray.getJSONObject(i);
                  
                  int id = art_team.getInt("id");
                  String name = art_team.getString("name");
                  
                 // tmp hashmap for single contact
                  HashMap<String, String> contact = new HashMap<String, String>();

                  // adding each child node to HashMap key => value
                  contact.put("id", String.valueOf(id));
                  contact.put("name", name);
                 // Log.e("ServiceHandler", "contact"+contact);
                  // adding contact to contact list
                  artistTeamsArrayList.add(contact);
              }
			  } catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			  }			  
	  }else {
          Log.e("ServiceHandler", "Couldn't get any data from the url");
      }
	  Log.d("ServiceHandler", "jsonString.size:"+artistTeamsArrayList.size());			  
	  return artistTeamsArrayList;
  }
  /**
   * Parse event data
   * @param data
   * @return
   */
  public static ArrayList<ArrayList<HashMap<String, String>>> artistTeamsAllEvents(String data, int per_page_download){
	  if (data != null) {
		  try {
			  //Log.e("JSONParse", "artistTeamsAllEvents data:"+data);
			  JSONObject jsonObject = new JSONObject(data);
			  // Getting JSON Array node
              JSONArray  artistTeamsArray = jsonObject.getJSONArray("events");
              JSONObject metaObject = jsonObject.getJSONObject("meta");
              HashMap<String, String> metaDataMap =  metaDataValue(metaObject,per_page_download);
              // looping through All Contacts
              artistTeams_AlleventArrayList.clear();
              for (int i = 0; i < artistTeamsArray.length(); i++) {
            	  ArrayList<HashMap<String, String>> artistTeamsAlleventArrayList = 
            			  new ArrayList<HashMap<String, String>>();
            	  JSONObject art_team = artistTeamsArray.getJSONObject(i);
            	  //events
            	  String id = art_team.getString("id");
                  String title = art_team.getString("title");
                  String data_string = art_team.getString("date_string");
                  String datetime_local = art_team.getString("datetime_local");                
                  String datetime_tbd = art_team.getString("datetime_tbd");                 
                  String map_static = art_team.getString("map_static");
                  
                  JSONObject jsonObject2 = art_team.getJSONObject("venue");
                  
                  // tmp hashmap for single contact
                  HashMap<String, String> alleventHash_ = new HashMap<String, String>();
                  //Log.e("ServiceHandler", "title:"+title);
                  //events
                  alleventHash_.put("id", id);
                  alleventHash_.put("title", title);
                  alleventHash_.put("data_string", data_string);
                  alleventHash_.put("datetime_local", datetime_local);
                  alleventHash_.put("datetime_tbd", datetime_tbd);
                  alleventHash_.put("map_static", map_static);
                  alleventHash_.put("numberOfDownload", metaDataMap.get("numberOfDownload"));
                  artistTeamsAlleventArrayList.add(alleventHash_);
                  //Log.e("ServiceHandler", "alleventHash_:"+alleventHash_);
                  //Log.e("ServiceHandler", "artistTeamsAlleventArrayList:"+artistTeamsAlleventArrayList);
                  //venues
                  String id1 = jsonObject2.getString("id");
                  String name = jsonObject2.getString("name");
                  String location = jsonObject2.getString("location");
                  String city = jsonObject2.getString("city");                
                  String state = jsonObject2.getString("state");                 
                  String postal_code = jsonObject2.getString("postal_code");
                  String country = jsonObject2.getString("country");
                  String address = jsonObject2.getString("address");
                  String extended_address = jsonObject2.getString("extended_address");
                  String boxofficephone = jsonObject2.getString("boxofficephone");                
                  String capacity = jsonObject2.getString("capacity");                 
                  String child_rules = jsonObject2.getString("child_rules");                 
                  String directions = jsonObject2.getString("directions");
                  String notes = jsonObject2.getString("notes");
                  String parking = jsonObject2.getString("parking");
                  String public_transportation = jsonObject2.getString("public_transportation");                
                  String rules = jsonObject2.getString("rules");                 
                  String willcall = jsonObject2.getString("willcall");                 
                  JSONObject pinJsonObject = jsonObject2.getJSONObject("pin");
                  String lat = pinJsonObject.getString("lat");
                  String lon = pinJsonObject.getString("lon");
                  
                  HashMap<String, String> alleventHash = new HashMap<String, String>();
                                   
                  alleventHash.put("id", id1);
                  alleventHash.put("name", name);
                  alleventHash.put("location", location);
                  alleventHash.put("city", city);
                  alleventHash.put("state", state);
                  alleventHash.put("postal_code", postal_code);                
                  alleventHash.put("country", country);
                  alleventHash.put("address", address);
                  alleventHash.put("extended_address", extended_address);
                  alleventHash.put("boxofficephone", boxofficephone);
                  alleventHash.put("capacity", capacity);
                  alleventHash.put("child_rules", child_rules);  
                  alleventHash.put("directions", directions);
                  alleventHash.put("notes", notes);
                  alleventHash.put("parking", parking);
                  alleventHash.put("public_transportation", public_transportation);
                  alleventHash.put("rules", rules);
                  alleventHash.put("willcall", willcall);
                  alleventHash.put("lat", lat);
                  alleventHash.put("lon", lon);                 
                  artistTeamsAlleventArrayList.add(alleventHash);
                  
                  //performers
                  JSONArray jsonArrayPerforma = art_team.getJSONArray("performers");
                 
                  for (int j = 0; j < jsonArrayPerforma.length(); j++) {
                	  HashMap<String, String> alleventPerforma = new HashMap<String, String>();
                	  JSONObject jsonObject3 = jsonArrayPerforma.getJSONObject(j);
                	  String id2 = jsonObject3.getString("id");
                      String name1 = jsonObject3.getString("name");
                      String primary = jsonObject3.getString("primary");
                      String home = jsonObject3.getString("home"); 
                      
                      alleventPerforma.put("id", id2);
                      alleventPerforma.put("name", name1);
                      alleventPerforma.put("primary", primary);
                      alleventPerforma.put("home", home);
                      
                      artistTeamsAlleventArrayList.add(alleventPerforma);
				  }                   
                  artistTeams_AlleventArrayList.add(artistTeamsAlleventArrayList);
              }
		  } catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
		  }	
	  }
	  return artistTeams_AlleventArrayList;
  }
  /**
   * Parse venue data
   * @param data
   * @return
   */
  public static ArrayList<HashMap<String, String>> venuesParse(String data){
	    ArrayList<HashMap<String, String>> venuesArrayList = 
			  new ArrayList<HashMap<String, String>>();	
	  if (data != null) {
		  try {
			  JSONObject jsonObject = new JSONObject(data);
			  // Getting JSON Array node
              JSONArray  artistTeamsArray = jsonObject.getJSONArray("venues");
              // looping through All Contacts
              artistTeams_AlleventArrayList.clear();
              for (int i = 0; i < artistTeamsArray.length(); i++) {
            	  JSONObject art_team = artistTeamsArray.getJSONObject(i);
                  
                  //venues
                  String id1 = art_team.getString("id");
                  String name = art_team.getString("name");
                  String location = art_team.getString("location");
                  String city = art_team.getString("city");                
                  String state = art_team.getString("state");                 
                  String postal_code = art_team.getString("postal_code");
                  String country = art_team.getString("country");
                  String address = art_team.getString("address");
                  String extended_address = art_team.getString("extended_address");
                  String boxofficephone = art_team.getString("boxofficephone");                
                  String capacity = art_team.getString("capacity");                 
                  String child_rules = art_team.getString("child_rules");                 
                  String directions = art_team.getString("directions");
                  String notes = art_team.getString("notes");
                  String parking = art_team.getString("parking");
                  String public_transportation = art_team.getString("public_transportation");                
                  String rules = art_team.getString("rules");                 
                  String willcall = art_team.getString("willcall");                 
                  JSONObject pinJsonObject = art_team.getJSONObject("pin");
                  String lat = pinJsonObject.getString("lat");
                  String lon = pinJsonObject.getString("lon");
                  
                  HashMap<String, String> alleventHash = new HashMap<String, String>();
                                   
                  alleventHash.put("id", id1);
                  alleventHash.put("name", name);
                  alleventHash.put("location", location);
                  alleventHash.put("city", city);
                  alleventHash.put("state", state);
                  alleventHash.put("postal_code", postal_code);                
                  alleventHash.put("country", country);
                  alleventHash.put("address", address);
                  alleventHash.put("extended_address", extended_address);
                  alleventHash.put("boxofficephone", boxofficephone);
                  alleventHash.put("capacity", capacity);
                  alleventHash.put("child_rules", child_rules);  
                  alleventHash.put("directions", directions);
                  alleventHash.put("notes", notes);
                  alleventHash.put("parking", parking);
                  alleventHash.put("public_transportation", public_transportation);
                  alleventHash.put("rules", rules);
                  alleventHash.put("willcall", willcall);
                  alleventHash.put("lat", lat);
                  alleventHash.put("lon", lon);                 
                  venuesArrayList.add(alleventHash);
              }
		  } catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
		  }	
	  }
	  return venuesArrayList;
  }
  /**
   * Parse tickets
   * @param data
   * @return
   */
  public static ArrayList<HashMap<String, String>> ticketsDetailParse(String data){
	  if (data != null) {
		 try {
			JSONObject jsonObject = new JSONObject(data);
			JSONArray jsonArray = jsonObject.getJSONArray("tickets");
			String checkout_url = jsonObject.getString("checkout_url");
			String receipt_begins = jsonObject.getString("receipt_begins");
			
			ticketArrayList.clear();
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonObject2 = jsonArray.getJSONObject(i);
				String id = jsonObject2.getString("id");
				String title = jsonObject2.getString("title");
                String section = jsonObject2.getString("section");
                String row = jsonObject2.getString("row");
                String price = jsonObject2.getString("price");                
                String marked = jsonObject2.getString("marked");                 
                String description = jsonObject2.getString("description");
                String type = jsonObject2.getString("type");
                
                // temp hashmap for single contact
                HashMap<String, String> alleventTicketHash_ = new HashMap<String, String>();
                
                alleventTicketHash_.put("id", id);
                alleventTicketHash_.put("title", title);
                alleventTicketHash_.put("section", section);
                alleventTicketHash_.put("row", row);
                alleventTicketHash_.put("price", price);
                alleventTicketHash_.put("marked", marked);
                alleventTicketHash_.put("description", description);
                alleventTicketHash_.put("type", type);
                
                alleventTicketHash_.put("checkout_url", checkout_url);
                alleventTicketHash_.put("receipt_begins", receipt_begins);
                
                JSONArray jsonArraySplit = jsonObject2.getJSONArray("splits");
               // Log.d("AsyncTaskRunner", "The jsonArraySplit is: " + jsonArraySplit);
                String splite = jsonArraySplit.toString();
                splite = splite.replace("[", "");
                splite = splite.replace("]", "");           
                alleventTicketHash_.put("splite", splite);
                
                JSONArray jsonArraydeliveryMethods = jsonObject2.getJSONArray("delivery_methods");
                String jsonArraydeliveryMethods_ = jsonArraydeliveryMethods.toString();
                jsonArraydeliveryMethods_ = jsonArraydeliveryMethods_.replace("[", "");
                jsonArraydeliveryMethods_ = jsonArraydeliveryMethods_.replace("]", "");
                alleventTicketHash_.put("delivery_methods", jsonArraydeliveryMethods_);
                
                ticketArrayList.add(alleventTicketHash_);
			}
		 } catch (Exception e) {
			// TODO: handle exception
		}
	  }
	  return ticketArrayList;
  }
  
  public static HashMap<String, String> metaDataValue(JSONObject metaObject,int per_page_download){
	  HashMap<String, String> metadata = new HashMap<String, String>();
	  try {
		  int total  = metaObject.getInt("total");
		  int page =   metaObject.getInt("page");
		  String per_page = metaObject.getString("per_page");
		  
		 // float per_Page_int = per_page_download;
		  Log.e("ArtistTeamsFragment", "per_page_download:"+per_page_download);
		  float page_int = page;//Float.parseFloat(page);
		  float total_int = total;// Float.parseFloat(total);
		  
		  float numberOfDownload =  (1*total_int)/per_page_download;
		  int numberOfDownloadint = (int) numberOfDownload;
		  /*Log.i("ArtistTeamsFragment", "total_int:"+total_int);
		  Log.e("ArtistTeamsFragment", "page_int:"+page_int);
		  Log.e("ArtistTeamsFragment", "per_Page_int:"+per_page_download);
		  
		  Log.e("ArtistTeamsFragment", "numberOfDownload:"+numberOfDownload);
		  Log.e("ArtistTeamsFragment", "numberOfDownloadint:"+numberOfDownloadint);*/
		  if (numberOfDownloadint < numberOfDownload) {
			  numberOfDownloadint = numberOfDownloadint + 1;
		  }
		 // Log.e("ArtistTeamsFragment", "numberOfDownloadint:"+numberOfDownloadint);
		  metadata.put("total", String.valueOf(total));
		  metadata.put("page", String.valueOf(page));
		  metadata.put("per_page", per_page);
		  metadata.put("numberOfDownload", String.valueOf(numberOfDownloadint));
	} catch (JSONException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	return metadata;
	  
  }
}

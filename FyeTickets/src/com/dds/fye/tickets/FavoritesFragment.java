package com.dds.fye.tickets;

import java.util.ArrayList;

import com.dds.fye.database.DatabaseHandler;
import com.dds.fye.tickets.adapter.FavoritesExpandableListAdapter;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;

public class FavoritesFragment extends Fragment{
	 ExpandableListView favoritesExpandableListView;
	 private ArrayList<String> parentItems = new ArrayList<String>();
	 private ArrayList<ArrayList> childItems = new ArrayList<ArrayList>();
	 //private String[] filedType = {"events"};
	 FavoritesExpandableListAdapter adapter;
	 DatabaseHandler databaseHandler ; 
	
	  public FavoritesFragment(){}
	  
	  @Override
	  public View onCreateView(LayoutInflater inflater, ViewGroup container,
	            Bundle savedInstanceState) {	  
	        View rootView = inflater.inflate(R.layout.favorites_view, container, false); 
	        favoritesExpandableListView = (ExpandableListView)rootView.findViewById(R.id.favorites_expandableListView1);
	        databaseHandler = new DatabaseHandler(getActivity());
	        setGroupParents();
	        getAllData();
	        
	        favoritesExpandableListView.setOnChildClickListener(new OnChildClickListener() {
				
	        	
				@Override
				public boolean onChildClick(ExpandableListView parent, View v,
						int groupPosition, int childPosition, long id) {
					// TODO Auto-generated method stub
					 Log.d("FavoritesFragment", "groupPosition:"+groupPosition);
					 Log.d("FavoritesFragment", "childPosition:"+childPosition);
					if (groupPosition == 3) {
						//ticket
						TicketsData ticketsData = (TicketsData) adapter.getChild(groupPosition, childPosition);
						Intent intent = new Intent(getActivity(), ArtistTeamsEventList.class);
						intent.putExtra("ticketId", ticketsData.ticketId);
						intent.putExtra("ticketTitle", ticketsData.ticketTitle);
						intent.putExtra("ticketSection", ticketsData.ticketSection);
						intent.putExtra("ticketRow", ticketsData.ticketRow);
						intent.putExtra("ticketPrice", String.valueOf(ticketsData.ticketPrice));
						intent.putExtra("ticketSplits", ticketsData.ticketSplits);	
						
						intent.putExtra("ticketType", ticketsData.ticketType);
						intent.putExtra("ticketDelivery_methods", ticketsData.ticketDelivery_methods);
						intent.putExtra("ticketDescription", ticketsData.ticketDescription);
						intent.putExtra("ticketCheckout_url", ticketsData.ticketCheckout_url);
						intent.putExtra("ticketReceipt_begins", ticketsData.ticketReceipt_begins);
						
						intent.putExtra("call_class", "tickets");	
						
						Log.d("FavoritesFragment", "ticketsData.ticketTitle:"+ticketsData.ticketTitle);
				        startActivity(intent);
					}else{
						EventsData eventsData = (EventsData) adapter.getChild(groupPosition, childPosition);
						if (groupPosition == 0) {
							//event
							Intent intent = new Intent(getActivity(), ArtistTeamsEventList.class);
							intent.putExtra("eventId", eventsData.eventId);
							intent.putExtra("eventTitle", eventsData.eventTitle);
							intent.putExtra("eventDateString", eventsData.eventDateString);
							intent.putExtra("evantDateTimeLocal", eventsData.eventDateTimeLocal);
							intent.putExtra("eventVenuName", eventsData.eventVenueName);
							
							intent.putExtra("eventAddress", eventsData.localEventAddress);
							intent.putExtra("eventVenueLocation", eventsData.eventVenueLocation);
							intent.putExtra("eventVenuePostalCode", eventsData.eventVenueZipCode);
							intent.putExtra("eventVenueLat", eventsData.eventVenueLat);
							intent.putExtra("eventVenueLon", eventsData.eventVenueLon);
							
							intent.putExtra("map", eventsData.eventMapStatic);
							intent.putExtra("call_class", "events");
							intent.putExtra("listViewPosition", String.valueOf(childPosition));
					        startActivity(intent);
							
						}else if (groupPosition == 1) {
							//artist / teams 
							Intent intent = new Intent(getActivity(), ArtistTeamsEventList.class);
							intent.putExtra("name", eventsData.eventTitle);
							intent.putExtra("performer_name", eventsData.eventPerformerName);
							intent.putExtra("performer_id", eventsData.eventsPerformerId);
							intent.putExtra("call_class", "artist_teams");
							//intent.putExtra("listViewPosition", String.valueOf(arg2));							
					        startActivity(intent);
							
						}else if (groupPosition == 2) {
							// venue
							//Log.d("ArtistTeamsFragment", "item.text:"+item.text);
							Intent intent = new Intent(getActivity(), ArtistTeamsEventList.class);
							intent.putExtra("eventTitle", eventsData.eventTitle);
							intent.putExtra("venuName", eventsData.eventVenueName);
							intent.putExtra("venuLocation", eventsData.eventVenueLocation);
							intent.putExtra("call_class", "venues");
							//intent.putExtra("listViewPosition", String.valueOf(childPosition));
					        startActivity(intent);
						}
					}
					
					return false;
				}
			});
	        return rootView;
	    }
	   
	  // method to add parent Items
	  /**
	   * Create item header items list.
	   */
	  private void setGroupParents() {
	        parentItems.add("Events");
	        parentItems.add("Artist/Teams");
	        parentItems.add("Venues");
	        parentItems.add("Tickets");
	    }
	  
	  /**
	   * Create child item.
	   * Load to list view.
	   */
	  public void getAllData(){
		  
		  Log.d("FavoritesFragment", " fragment4 refres:");
		  childItems.clear();
		  ArrayList<EventsData> eventsArrayList = new ArrayList<EventsData>();
		  ArrayList<EventsData> artist_TeamsArrayList = new ArrayList<EventsData>();
		  ArrayList<EventsData> venuesArrayList = new ArrayList<EventsData>();
		  ArrayList<TicketsData> ticketsArrayList = new ArrayList<TicketsData>();	
		  
		  eventsArrayList =  databaseHandler.getAllFavorites("events");
		  Log.d("FavoritesFragment", "eventsArrayList.size():"+eventsArrayList.size());
		  if (eventsArrayList.size()!=0) {
			  childItems.add(eventsArrayList);
		  }else{
			  eventsArrayList.add(new EventsData("", "", "","", "", "", "","", "", "", "","", "", ""));
			  childItems.add(eventsArrayList);
		  }
		  
		  
		  artist_TeamsArrayList =  databaseHandler.getAllFavorites("artist_teams");
		  Log.d("FavoritesFragment", "artist_TeamsArrayList.size():"+artist_TeamsArrayList.size());
		  if (artist_TeamsArrayList.size()!=0) {
			  childItems.add(artist_TeamsArrayList);
		  }else{
			  artist_TeamsArrayList.add(new EventsData("", "", "","", "", "", "","", "", "", "","", "", ""));
			  childItems.add(artist_TeamsArrayList);
		  }
		  
		  
		  venuesArrayList =  databaseHandler.getAllFavorites("venues");
		  Log.d("FavoritesFragment", "venuesArrayList.size():"+venuesArrayList.size());
		  if (venuesArrayList.size()!=0) {
			  childItems.add(venuesArrayList);
		  }else{
			  venuesArrayList.add(new EventsData("", "", "","", "", "", "","", "", "", "","", "", ""));
			  childItems.add(venuesArrayList);
		  }
		 
		  
		  ticketsArrayList =  databaseHandler.getTickets();
		  Log.d("FavoritesFragment", "ticketsArrayList.size():"+ticketsArrayList.size());
		  if (ticketsArrayList.size()!=0) {
			  childItems.add(ticketsArrayList);
		  }else{
			  ticketsArrayList.add(new TicketsData("", "", "", "", 0, "", "", "", "", "", "", ""));
			  childItems.add(ticketsArrayList);
		  }
		  
		  Log.d("FavoritesFragment", "childItems.size():"+childItems.size());
		  
		  adapter = new FavoritesExpandableListAdapter(getActivity(), FavoritesFragment.this, parentItems, childItems);
		  favoritesExpandableListView.setAdapter(adapter);
		  adapter.notifyDataSetChanged();
		  favoritesExpandableListView.setSmoothScrollbarEnabled(true);
		  favoritesExpandableListView.setFastScrollEnabled(true);
		  favoritesExpandableListView.setFastScrollAlwaysVisible(true);
		  
		  favoritesExpandableListView.setGroupIndicator(null);
		  favoritesExpandableListView.expandGroup(0);
		  favoritesExpandableListView.expandGroup(1);
		  favoritesExpandableListView.expandGroup(2);
		  favoritesExpandableListView.expandGroup(3);

	  }
}

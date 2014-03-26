package com.dds.fye.tickets.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import com.dds.fye.database.DatabaseHandler;
import com.dds.fye.tickets.ArtistTeamsFragment;
import com.dds.fye.tickets.EventsData;
import com.dds.fye.tickets.EventsFragment;
import com.dds.fye.tickets.FavoritesFragment;
import com.dds.fye.tickets.R;
import com.dds.fye.tickets.TicketsData;
import com.dds.fye.tickets.VenueData;
import com.dds.fye.tickets.VenuesFragment;
import com.dds.fye.tickets.adapter.VenueAlphabetListAdapter.Item;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

public class FavoritesExpandableListAdapter extends BaseExpandableListAdapter{
	
	 private ArrayList<String> _listDataHeader; // header titles
	  // child data in format of header title, child title
	 private ArrayList<ArrayList> _listDataChild;

	private Context context;
	FavoritesFragment fragment;
	
	 DatabaseHandler db;
	public FavoritesExpandableListAdapter(Context context,FavoritesFragment fragment,ArrayList<String> _listDataHeader,
			ArrayList<ArrayList> _listDataChild){
		this.context = context;
		this.fragment = fragment;
		this._listDataHeader = _listDataHeader;
		this._listDataChild = _listDataChild;
		
		db = new DatabaseHandler(context);
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		//return this._listDataChild.get(this._listDataHeader.get(groupPosition)).get(childPosition);
		if (groupPosition == 3) {
			TicketsData ticketsData = (TicketsData)_listDataChild.get(groupPosition).get(childPosition);
			return ticketsData;
		}else{
			EventsData eventsDatas = (EventsData)_listDataChild.get(groupPosition).get(childPosition);
			return eventsDatas;
		}				
	}

	@SuppressWarnings("unchecked")
	public void removeDataFromList(int groupPosition, int childPosition){
		if (groupPosition == 3) {
			//TicketsData ticketsData = (TicketsData)_listDataChild.get(groupPosition).get(childPosition);
			_listDataChild.get(groupPosition).remove(childPosition);
		}else{
			//EventsData eventsDatas = (EventsData)_listDataChild.get(groupPosition).get(childPosition);
			int index = _listDataChild.get(groupPosition).size();
			 Log.d("FavoritesFragment", "index:"+index);
			 if (index != 0) {
				 _listDataChild.get(groupPosition).remove(childPosition);
			 }
			 index = _listDataChild.get(groupPosition).size();
			 Log.d("FavoritesFragment", "index:"+index);
			/* if (index == 0) {
				 ArrayList<EventsData> eventsArrayList = new ArrayList<EventsData>();
				 eventsArrayList.add(new EventsData("", "", "","", "", "", "","", "", "", "","", "", ""));
				 _listDataChild.get(groupPosition).add(eventsArrayList);
			 }
			 index = _listDataChild.get(groupPosition).size();
			 Log.d("FavoritesFragment", "index:"+index);*/
		}
	}
	
	@Override
	public long getChildId(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return childPosition;
	}

	@Override
	public View getChildView(final int groupPosition, final int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		  //final String childText = (String) getChild(groupPosition, childPosition);
			String headerTitle = (String) getGroup(groupPosition);
			//Log.d("FavoritesFragment", "getChildView headerTitle:"+headerTitle);          
			if (headerTitle.equals("Events")) {
				
			       // if (convertView == null) {
			            LayoutInflater infalInflater = (LayoutInflater) this.context
			                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			            convertView = infalInflater.inflate(R.layout.row_item, null);
			       // }
			 
			            TextView weekendsDaystextView = (TextView) convertView.findViewById(R.id.weekends_day_textview);
			            TextView monthtextView = (TextView) convertView.findViewById(R.id.month_textView1);
			            TextView daytextView = (TextView) convertView.findViewById(R.id.day_textView2);
			            TextView textView = (TextView) convertView.findViewById(R.id.textView1);
			            TextView addresstextView = (TextView) convertView.findViewById(R.id.address_textView2);
			            TextView dateTimeInfotextView = (TextView) convertView.findViewById(R.id.date_time_info_textView2);
			           // ImageView favoriteArtist = (ImageView)convertView.findViewById(R.id.favorite_imageView1);
			            final ToggleButton favoriteArtistToggleButton = (ToggleButton)convertView.findViewById(R.id.favorite_ToggleButton);
			            //favoriteArtistToggleButton.setVisibility(View.GONE);
			            View view = (View)convertView.findViewById(R.id.favorite_seperater_view1);
			           
			 
			            final EventsData hashMapData = (EventsData) getChild(groupPosition, childPosition);
			            String event_id = hashMapData.eventId;
						String event_title = hashMapData.eventTitle;
						String event_date_string = hashMapData.eventDateString;
						String event_data_time_local = hashMapData.eventDateTimeLocal;
						String event_map_static = hashMapData.eventMapStatic;
						String event_venue_name = hashMapData.eventVenueName;
						String event_venue_location = hashMapData.eventVenueLocation;
						//Log.d("FavoritesFragment", "getChildView event_title:"+event_title);
						if (!event_id.equals("")) {
				            
							String[] dateAndTime = event_date_string.split("[@]");
							String[] data = dateAndTime[0].split("[ ]");
							String day = data[0].replace(" ", "");			
							       day = day.toUpperCase();
							String month = data[1].replace(" ", ""); 
								   month = month.toUpperCase();
						    String day_no_ = data[2].replace(" ", "");
						    day_no_ = data[2].replace(",", "");	
						    if (day_no_.length() == 1) {
						    	day_no_ = "0"+day_no_;
							}
						    String time = 	dateAndTime[1].replace(" ", "");
						    String[] date_local = event_data_time_local.split("[T]");
						    String[] dateNumber = date_local[0].split("[-]");
						    
						    String yyyy =  dateNumber[0]; 
						    int mm =  Integer.parseInt(dateNumber[1]);  
						   // int dd =  Integer.parseInt(dateNumber[2]); 
						    String day_day = "";
						    int weekendFlag = 0;
						    if (day.equals("MON")) {
						    	day_day = parent.getContext().getResources().getString(R.string.MON);
						    	weekendFlag = 0;
							}else if (day.equals("TUE")) {
								day_day = parent.getContext().getResources().getString(R.string.TUE);
								weekendFlag = 0;
							}else if (day.equals("WED")) {
								day_day = parent.getContext().getResources().getString(R.string.WED);
								weekendFlag = 0;
							}else if (day.equals("THU")) {
								day_day = parent.getContext().getResources().getString(R.string.THU);
								weekendFlag = 0;
							}else if (day.equals("FRI")) {
								day_day = parent.getContext().getResources().getString(R.string.FRI);
								weekendFlag = 1;
							}else if (day.equals("SAT")) {
								day_day = parent.getContext().getResources().getString(R.string.SAT);
								weekendFlag = 1;
							}else if (day.equals("SUN")) {
								day_day = parent.getContext().getResources().getString(R.string.SUN);
								weekendFlag = 1;
							}
						    		    		    
						    String[] monthArray = parent.getContext().getResources().getStringArray(R.array.months_array);
						    String month_month = monthArray[mm-1];		    
						    String completDateAndTime = day_day+" "+month_month+" "+day_no_+", "+yyyy+"-"+time;
						    
						    String address = "Next Show: "+event_venue_name+"-"+event_venue_location;
						    
						    weekendsDaystextView.setText(day);
						    monthtextView.setText(month);
						    daytextView.setText(day_no_);
						    textView.setText(event_title);
						    addresstextView.setText(address);
						    dateTimeInfotextView.setText(completDateAndTime);
						    //Log.d("event Alpha ", "completDateAndTime:"+completDateAndTime);
						    if (weekendFlag == 0) {
						    	weekendsDaystextView.setBackgroundResource(R.drawable.section_flag_standard);
							}
						    if (weekendFlag == 1) {
						    	weekendsDaystextView.setBackgroundResource(R.drawable.section_flag_weekend);
							}
						    
						    //Add and remove favorite.
						    final boolean fav_temp = db.getCurrentIdAvalibleInFavorites(hashMapData.eventId, "events");	
						    if (fav_temp){
						    	//favoriteArtist.setImageResource(R.drawable.favorite_hilighted);
						    	favoriteArtistToggleButton.setChecked(false);
							}else{
								//favoriteArtist.setImageResource(R.drawable.favorite_default);
								favoriteArtistToggleButton.setChecked(true);
							}
						    favoriteArtistToggleButton.setOnClickListener(new OnClickListener() {																

								
								@Override
								public void onClick(View v) {
									// TODO Auto-generated method stub
									if (favoriteArtistToggleButton.isChecked()) {
								  		Log.d("ArtistTeamsEventslist", "delet from data base");
								    	//delet from data base
								    	//dataBaseHandler.deleteFavorites(data.eventId,"events");
								  		favoriteArtistToggleButton.setChecked(true);
								    	db.deleteFavorites(hashMapData.eventId,"events");
								    	removeDataFromList(groupPosition,childPosition);
								    	fragment.getAllData();
								    	EventsFragment.upDateList();
								    	 //((FavoritesFragment)).getAllData();
								    	//notifyDataSetChanged();
									}/*else{
										Log.d("ArtistTeamsEventslist", " add to data base");
										// add to data base
										//dataBaseHandler.addContact(data,"events");	
										favoriteArtistToggleButton.setChecked(false);
										db.addContact(hashMapData,"events");
									}*/
								}
							});
						}else{
						    weekendsDaystextView.setVisibility(View.GONE);
						    monthtextView.setVisibility(View.GONE);
						    daytextView.setVisibility(View.GONE);
						    textView.setText("No Favorite Events");
						    textView.setGravity(Gravity.CENTER);
						    addresstextView.setVisibility(View.GONE);
						    dateTimeInfotextView.setVisibility(View.GONE);
						    view.setVisibility(View.GONE);
						    favoriteArtistToggleButton.setVisibility(View.GONE);
						}
			}
			if (headerTitle.equals("Artist/Teams")) {
				// Log.d("FavoritesFragment", "Artist/Teams getChildView groupPosition:"+groupPosition);
		        // Log.d("FavoritesFragment", "Artist/Teams getChildView childPosition:"+childPosition);
	            LayoutInflater infalInflater = (LayoutInflater) this.context
	                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	            convertView = infalInflater.inflate(R.layout.row_item, null);
	            
	            TextView weekendsDaystextView = (TextView) convertView.findViewById(R.id.weekends_day_textview);
	            TextView monthtextView = (TextView) convertView.findViewById(R.id.month_textView1);
	            TextView daytextView = (TextView) convertView.findViewById(R.id.day_textView2);
	            TextView textView = (TextView) convertView.findViewById(R.id.textView1);
	            TextView addresstextView = (TextView) convertView.findViewById(R.id.address_textView2);
	            TextView dateTimeInfotextView = (TextView) convertView.findViewById(R.id.date_time_info_textView2);
	            //ImageView favoriteArtist = (ImageView)convertView.findViewById(R.id.favorite_imageView1);
	            final ToggleButton favoriteArtistToggleButton = (ToggleButton)convertView.findViewById(R.id.favorite_ToggleButton);
	            
	           // favoriteArtistToggleButton.setVisibility(View.GONE);
	            View view = (View)convertView.findViewById(R.id.favorite_seperater_view1);
	            	            	            
	            final EventsData hashMapData  = (EventsData) getChild(groupPosition, childPosition);
				String event_id = hashMapData.eventId;
				String event_title = hashMapData.eventTitle;
				String event_date_string = hashMapData.eventDateString;
				String event_data_time_local = hashMapData.eventDateTimeLocal;
				String event_map_static = hashMapData.eventMapStatic;
				String event_venue_name = hashMapData.eventVenueName;
				String event_venue_location = hashMapData.eventVenueLocation;
	            if (!event_id.equals("")) {
					
					String[] dateAndTime = event_date_string.split("[@]");
					String[] data = dateAndTime[0].split("[ ]");
					String day = data[0].replace(" ", "");			
					       day = day.toUpperCase();
					String month = data[1].replace(" ", ""); 
						   month = month.toUpperCase();
				    String day_no_ = data[2].replace(" ", "");	
				    day_no_ = data[2].replace(",", "");	
				    if (day_no_.length() == 1) {
				    	day_no_ = "0"+day_no_;
					}
				    String time = 	dateAndTime[1].replace(" ", "");
				    String[] date_local = event_data_time_local.split("[T]");
				    String[] dateNumber = date_local[0].split("[-]");
				    
				    String yyyy =  dateNumber[0]; 
				    int mm =  Integer.parseInt(dateNumber[1]);  
				   // int dd =  Integer.parseInt(dateNumber[2]); 
				    String day_day = "";
				    int weekendFlag = 0;
				    if (day.equals("MON")) {
				    	day_day = parent.getContext().getResources().getString(R.string.MON);
				    	weekendFlag = 0;
					}else if (day.equals("TUE")) {
						day_day = parent.getContext().getResources().getString(R.string.TUE);
						weekendFlag = 0;
					}else if (day.equals("WED")) {
						day_day = parent.getContext().getResources().getString(R.string.WED);
						weekendFlag = 0;
					}else if (day.equals("THU")) {
						day_day = parent.getContext().getResources().getString(R.string.THU);
						weekendFlag = 0;
					}else if (day.equals("FRI")) {
						day_day = parent.getContext().getResources().getString(R.string.FRI);
						weekendFlag = 1;
					}else if (day.equals("SAT")) {
						day_day = parent.getContext().getResources().getString(R.string.SAT);
						weekendFlag = 1;
					}else if (day.equals("SUN")) {
						day_day = parent.getContext().getResources().getString(R.string.SUN);
						weekendFlag = 1;
					}
				    		    		    
				    String[] monthArray = parent.getContext().getResources().getStringArray(R.array.months_array);
				    String month_month = monthArray[mm-1];		    
				    String completDateAndTime = day_day+" "+month_month+" "+day_no_+", "+yyyy+"-"+time;
				    
				    String address = "Next Show: "+event_venue_name+"-"+event_venue_location;
				    
				    weekendsDaystextView.setText(day);
				    monthtextView.setText(month);
				    daytextView.setText(day_no_);
				    textView.setText(event_title);
				    addresstextView.setText(address);
				    dateTimeInfotextView.setText(completDateAndTime);
				    if (weekendFlag == 0) {
				    	weekendsDaystextView.setBackgroundResource(R.drawable.section_flag_standard);
					}
				    if (weekendFlag == 1) {
				    	weekendsDaystextView.setBackgroundResource(R.drawable.section_flag_weekend);
					}
				    //Add and remove favorite.
				    final boolean fav_temp = db.getCurrentIdAvalibleInFavorites(hashMapData.eventId, "artist_teams");	
				    if (fav_temp){
				    	//favoriteArtist.setImageResource(R.drawable.favorite_hilighted);
				    	favoriteArtistToggleButton.setChecked(false);
					}else{
						//favoriteArtist.setImageResource(R.drawable.favorite_default);
						favoriteArtistToggleButton.setChecked(true);
					}
				    favoriteArtistToggleButton.setOnClickListener(new OnClickListener() {																

						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							if (favoriteArtistToggleButton.isChecked()) {
						  		Log.d("ArtistTeamsEventslist", "delet from data base");
						    	//delet from data base
						    	//dataBaseHandler.deleteFavorites(data.eventId,"events");
						  		favoriteArtistToggleButton.setChecked(true);
						    	db.deleteFavorites(hashMapData.eventId,"artist_teams");
						    	removeDataFromList(groupPosition,childPosition);
						    	fragment.getAllData();
						    	ArtistTeamsFragment.upDateList();
						    	 //((FavoritesFragment)).getAllData();
						    	//notifyDataSetChanged();
							}/*else{
								Log.d("ArtistTeamsEventslist", " add to data base");
								// add to data base
								//dataBaseHandler.addContact(data,"events");	
								favoriteArtistToggleButton.setChecked(false);
								db.addContact(hashMapData,"events");
							}*/
						}
					});
				}else{							            
		            weekendsDaystextView.setVisibility(View.GONE);
		            monthtextView.setVisibility(View.GONE);
		            daytextView.setVisibility(View.GONE);
		            addresstextView.setVisibility(View.GONE);
		            dateTimeInfotextView.setVisibility(View.GONE);
		            favoriteArtistToggleButton.setVisibility(View.GONE);
		            view.setVisibility(View.GONE);
		            textView.setText("No Favorite Artist/Teams");
		            textView.setGravity(Gravity.CENTER);
		            
				}
			}
			if (headerTitle.equals("Venues")) {
				LayoutInflater inflater = (LayoutInflater) parent.getContext().
						getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = (LinearLayout) inflater.inflate(R.layout.venue_row_list, parent, false);  
         
               
                TextView venueName = (TextView) convertView.findViewById(R.id.venue_name_textView1);
                TextView venueAddress = (TextView) convertView.findViewById(R.id.venue_address_textView2);
                TextView venueNextShow = (TextView) convertView.findViewById(R.id.venue_next_show_textView2);
                //ImageView venueFavoriteAdd = (ImageView) convertView.findViewById(R.id.venue_favorite_imageView1);
                final ToggleButton favoriteVenueToggleButton = (ToggleButton)convertView.findViewById(R.id.venue_favorite_ToggleButton);
                
                //venueFavoriteAdd.setVisibility(View.GONE);
	            View view = (View)convertView.findViewById(R.id.venue_favorites_separeter_view1);
	            
	            
                final EventsData hashMapData  = (EventsData) getChild(groupPosition, childPosition);
                String Venuename =  hashMapData.eventVenueName;
                String Venueaddress =  hashMapData.localEventAddress;
                String Venuelocation = hashMapData.eventVenueLocation;
                String VenueperformerName = hashMapData.eventPerformerName;
                if (!Venuename.equals("")) {
                    String address_ = Venueaddress+", "+Venuelocation;
                    String nextShow_ = "Next Show: "+VenueperformerName;
                    venueName.setText(Venuename);
                    venueAddress.setText(address_);
                    venueNextShow.setText(nextShow_);
                  //Add and remove favorite.
				    final boolean fav_temp = db.getCurrentIdAvalibleInFavorites(hashMapData.eventId, "venues");	
				    if (fav_temp){
				    	//favoriteArtist.setImageResource(R.drawable.favorite_hilighted);
				    	favoriteVenueToggleButton.setChecked(false);
					}else{
						//favoriteArtist.setImageResource(R.drawable.favorite_default);
						favoriteVenueToggleButton.setChecked(true);
					}
				    favoriteVenueToggleButton.setOnClickListener(new OnClickListener() {																

						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							if (favoriteVenueToggleButton.isChecked()) {
						  		Log.d("ArtistTeamsEventslist", "delet from data base");
						    	//delet from data base
						    	//dataBaseHandler.deleteFavorites(data.eventId,"events");
						  		favoriteVenueToggleButton.setChecked(true);
	    				  		db.deleteFavorites(hashMapData.eventId,"venues");
						    	removeDataFromList(groupPosition,childPosition);
						    	fragment.getAllData();
						    	VenuesFragment.upDateList();
						    	 //((FavoritesFragment)).getAllData();
						    	//notifyDataSetChanged();
							}/*else{
								Log.d("ArtistTeamsEventslist", " add to data base");
								// add to data base
								//dataBaseHandler.addContact(data,"events");	
								favoriteArtistToggleButton.setChecked(false);
								db.addContact(hashMapData,"events");
							}*/
						}
					});
				}else{
                    venueName.setText("No favorite Venues");
                    venueName.setGravity(Gravity.CENTER);
                    venueAddress.setVisibility(View.INVISIBLE);
                    venueNextShow.setVisibility(View.INVISIBLE);
                    favoriteVenueToggleButton.setVisibility(View.GONE);
                    view.setVisibility(View.GONE);
				}
			}
			if (headerTitle.equals("Tickets")) {
				LayoutInflater inflater = (LayoutInflater) parent.getContext().
						getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = (LinearLayout) inflater.inflate(R.layout.venue_row_list, parent, false);
				 	TextView titleText = (TextView) convertView.findViewById(R.id.venue_name_textView1);
	                TextView sectionText = (TextView) convertView.findViewById(R.id.venue_address_textView2);
	                TextView rowText = (TextView) convertView.findViewById(R.id.venue_next_show_textView2);
	               // ImageView venueFavoriteAdd = (ImageView) convertView.findViewById(R.id.venue_favorite_imageView1);
	                final ToggleButton favoriteVenueToggleButton = (ToggleButton)convertView.findViewById(R.id.venue_favorite_ToggleButton);
	                View view = (View)convertView.findViewById(R.id.venue_favorites_separeter_view1);
	               // venueFavoriteAdd.setVisibility(View.GONE);		            
		            
		            
		            final TicketsData hashMapData  = (TicketsData) getChild(groupPosition, childPosition);
		            String section =  hashMapData.ticketSection;
	                String row =  hashMapData.ticketRow;
	                String ticketid = hashMapData.ticketId;
	                if (!ticketid.equals("")) {
		                DatabaseHandler database = new DatabaseHandler(context);
		                String eventtitle = database.getTicketEvnetTitle(ticketid);
		                
		                titleText.setText(eventtitle);
		                sectionText.setText("Section:"+section);
		                rowText.setText("Row:"+row);
		                
		                final boolean fav_temp = db.getCurrentIdAvalibleInTicketFavorites(hashMapData.ticketId);	
					    if (fav_temp){
					    	//favoriteArtist.setImageResource(R.drawable.favorite_hilighted);
					    	favoriteVenueToggleButton.setChecked(false);
						}else{
							//favoriteArtist.setImageResource(R.drawable.favorite_default);
							favoriteVenueToggleButton.setChecked(true);
						}
					    favoriteVenueToggleButton.setOnClickListener(new OnClickListener() {																

							
							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								if (favoriteVenueToggleButton.isChecked()) {
							  		Log.d("ArtistTeamsEventslist", "delet from data base");
							    	//delet from data base
							    	//dataBaseHandler.deleteFavorites(data.eventId,"events");
							  		favoriteVenueToggleButton.setChecked(true);
		    				  		db.deleteTicketFavorites(hashMapData.ticketId);
							    	removeDataFromList(groupPosition,childPosition);
							    	fragment.getAllData();
							    	//VenuesFragment.upDateList();
								}
							}
						});
					}else{
						titleText.setText("No favorite Tickets");
						titleText.setGravity(Gravity.CENTER);
	                    sectionText.setVisibility(View.INVISIBLE);
	                    rowText.setVisibility(View.INVISIBLE);
	                    favoriteVenueToggleButton.setVisibility(View.GONE);
	                    view.setVisibility(View.GONE);
					}

			}

		return convertView;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		// TODO Auto-generated method stub
		return _listDataChild.get(groupPosition).size();    
	}

	@Override
	public Object getGroup(int groupPosition) {
		// TODO Auto-generated method stub
		return this._listDataHeader.get(groupPosition);
	    
		//return null;
	}

	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
		return this._listDataHeader.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		// TODO Auto-generated method stub
		return groupPosition;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		String headerTitle = (String) getGroup(groupPosition);
		//Log.d("FavoritesFragment", "headerTitle:"+headerTitle);
       // if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.row_section, null);
       // }
 
        TextView lblListHeader = (TextView) convertView.findViewById(R.id.section_textView1);
        //lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);
 
        return convertView;

	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return true;
	}

}

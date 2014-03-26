package com.dds.fye.tickets.adapter;

import java.util.List;

import com.dds.fye.database.DatabaseHandler;
import com.dds.fye.tickets.EventsData;
//import com.dds.fye.tickets.ArtistTeamsEventList;
import com.dds.fye.tickets.R;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SectionIndexer;
import android.widget.TextView;
import android.widget.ToggleButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class AlphabetListAdapter extends BaseAdapter{

    public static abstract class Row {}
    
    public static final class Section extends Row {
        public final String text;

        public Section(String text) {
            this.text = text;
        }
    }
    
    public static final class Item extends Row {
        public final String text;
        public final EventsData eventsData;
        public Item(String text, EventsData eventsData) {
            this.text = text;
            this.eventsData = eventsData;
        }
    }
    
    private List<Row> rows;
    
    public void setRows(List<Row> rows) {
        this.rows = rows;
    }

    @Override
    public int getCount() {
        return rows.size();
    }

    @Override
    public Row getItem(int position) {
        return rows.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    
    @Override
    public int getViewTypeCount() {
        return 2;
    }
    
    @Override
    public int getItemViewType(int position) {
        if (getItem(position) instanceof Section) {
            return 1;
        } else {
            return 0;
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        
        if (getItemViewType(position) == 0) { // Item
            //if (view == null) {
                LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = (LinearLayout) inflater.inflate(R.layout.row_item, parent, false);  
           // }
            
            Item item = (Item) getItem(position);
            TextView weekendsDaystextView = (TextView) view.findViewById(R.id.weekends_day_textview);
            TextView monthtextView = (TextView) view.findViewById(R.id.month_textView1);
            TextView daytextView = (TextView) view.findViewById(R.id.day_textView2);
            TextView textView = (TextView) view.findViewById(R.id.textView1);
            TextView addresstextView = (TextView) view.findViewById(R.id.address_textView2);
            TextView dateTimeInfotextView = (TextView) view.findViewById(R.id.date_time_info_textView2);
            //final ImageView favoriteArtist = (ImageView)view.findViewById(R.id.favorite_imageView1);
            final ToggleButton favoriteArtistToggleButton = (ToggleButton)view.findViewById(R.id.favorite_ToggleButton);
            final DatabaseHandler db = new DatabaseHandler(parent.getContext());
            
            final EventsData hashMapData = item.eventsData;
			String event_id = hashMapData.eventId;
			String event_title = hashMapData.eventTitle;
			String event_date_string = hashMapData.eventDateString;
			String event_data_time_local = hashMapData.eventDateTimeLocal;
			String event_map_static = hashMapData.eventMapStatic;
			String event_venue_name = hashMapData.eventVenueName;
			String event_venue_location = hashMapData.eventVenueLocation;

			
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
		    String day_no_day = day_no_;
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
		    daytextView.setText(day_no_day);
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
					}else{
						Log.d("ArtistTeamsEventslist", " add to data base");
						// add to data base
						//dataBaseHandler.addContact(data,"events");	
						favoriteArtistToggleButton.setChecked(false);
						db.addContact(hashMapData,"events");
					}
				}
			});
/*		    favoriteArtistToggleButton.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (favoriteArtistToggleButton.isChecked()) {
				  		Log.d("ArtistTeamsEventslist", "delet from data base");
				    	//delet from data base
				    	//dataBaseHandler.deleteFavorites(data.eventId,"events");
				  		favoriteArtistToggleButton.setChecked(true);
				    	db.deleteFavorites(hashMapData.eventId,"events");
					}else{
						Log.d("ArtistTeamsEventslist", " add to data base");
						// add to data base
						//dataBaseHandler.addContact(data,"events");	
						favoriteArtistToggleButton.setChecked(false);
						db.addContact(hashMapData,"events");
					}
				}
			});*/
/*		    favoriteArtistToggleButton.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					// TODO Auto-generated method stub
					Log.d("ArtistTeamsEventslist", "secondEventAddToFavoritesToggleButton isChecked:"+isChecked);
					//Log.d("ArtistTeamsEventslist", "secondEventAddToFavoritesToggleButton fav_temp:"+fav_temp);
					  	if (isChecked) {
					  		Log.d("ArtistTeamsEventslist", "delet from data base");
					    	//delet from data base
					    	//dataBaseHandler.deleteFavorites(data.eventId,"events");
					    	db.deleteFavorites(hashMapData.eventId,"events");
						}else{
							Log.d("ArtistTeamsEventslist", " add to data base");
							// add to data base
							//dataBaseHandler.addContact(data,"events");	
							db.addContact(hashMapData,"events");
						}
				}
			});*/
/*		    if (fav_temp) {
		    	favoriteArtist.setImageResource(R.drawable.favorite_hilighted);
			}else{
				favoriteArtist.setImageResource(R.drawable.favorite_default);
			}
            
            favoriteArtist.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					 Log.d("EventAlphabetListAdapter", "fav_temp:"+fav_temp);
					if (fav_temp) {
				    	favoriteArtist.setImageResource(R.drawable.favorite_default);
				    	//delet from data base
				    	db.deleteFavorites(hashMapData.eventId,"events");
					}else{
						favoriteArtist.setImageResource(R.drawable.favorite_hilighted);
						// add to data base
						db.addContact(hashMapData,"events");						
					}
				}
			});*/
		    //Friday March 7, 2014-8:000pm 		
           // textView.setText(item.text);
           // TextView textViewTitle = (TextView) view.findViewById(R.id.titletextView2);
            //Log.e("AsyncTaskRunner", "ArtistTeamsEventList.listview_position: " + ArtistTeamsEventList.listview_position);	
            /*if (ArtistTeamsEventList.listview_position == position ) {
            	//Log.i("AsyncTaskRunner", "ArtistTeamsEventList.listview_position: " + ArtistTeamsEventList.listview_position);
            	//Log.i("AsyncTaskRunner", "ArtistTeamsEventList.data_time_for_list: " + ArtistTeamsEventList.data_time_for_list);
            	textViewTitle.setVisibility(View.VISIBLE);
            	 if (!ArtistTeamsEventList.data_time_for_list.equals("")) {
            		textViewTitle.setText(ArtistTeamsEventList.data_time_for_list);          		
            		
     			 }
			}*/
                     
        } else { // Section
            //if (view == null) {
                LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = (LinearLayout) inflater.inflate(R.layout.row_section, parent, false);  
           // }
            
            Section section = (Section) getItem(position);
            TextView textView = (TextView) view.findViewById(R.id.section_textView1);
            textView.setText(section.text);
        }
        
        return view;
    }

}

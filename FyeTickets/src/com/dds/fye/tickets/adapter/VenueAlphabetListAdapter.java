package com.dds.fye.tickets.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.dds.fye.database.DatabaseHandler;
import com.dds.fye.tickets.ArtistTeamsEventList;
import com.dds.fye.tickets.EventsData;
import com.dds.fye.tickets.R;
import com.dds.fye.tickets.VenueData;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View.OnClickListener;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

public class VenueAlphabetListAdapter extends BaseAdapter implements Filterable{
    public static abstract class Row {}
    
    public static final class Section extends Row {
        public final String text;

        public Section(String text) {
            this.text = text;
        }
    }
    
    public static final class Item extends Row {      
        public final String textid;
        public final VenueData venueData;
     
        public Item(String text1,VenueData venueData) {

            this.textid = text1;
            this.venueData = venueData;
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
            if (view == null) {
                LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = (LinearLayout) inflater.inflate(R.layout.venue_row_list, parent, false);  
            }
                Item item = (Item) getItem(position);
                TextView venueName = (TextView) view.findViewById(R.id.venue_name_textView1);
                TextView venueAddress = (TextView) view.findViewById(R.id.venue_address_textView2);
                TextView venueNextShow = (TextView) view.findViewById(R.id.venue_next_show_textView2);
                //final ImageView venueFavoriteAdd = (ImageView)view.findViewById(R.id.venue_favorite_imageView1);
                final ToggleButton favoriteVenueToggleButton = (ToggleButton)view.findViewById(R.id.venue_favorite_ToggleButton);
                final DatabaseHandler db = new DatabaseHandler(parent.getContext()); 
                
                final VenueData hashMapData = item.venueData;
                String name =  hashMapData.VenueName;
                String address =  hashMapData.VenueAddress;
                String location = hashMapData.VenueLocation;
                String performerName = hashMapData.VenuePerformerName;
                String address_ = address+", "+location;
                String nextShow_ = "Next Show: "+performerName;
                venueName.setText(name);
                venueAddress.setText(address_);
                venueNextShow.setText(nextShow_);
                
                
              //Add and remove favorite.
    		    final boolean fav_temp = db.getCurrentIdAvalibleInFavorites(hashMapData.VenueEventId, "venues");		   
    		    //Log.i("ArtistTeamsEventslist", "fav_temp:"+fav_temp);
    		    //Log.w("ArtistTeamsEventslist", "favoriteVenueToggleButton.isChecked():"+favoriteVenueToggleButton.isChecked());
    		    if (fav_temp){
    		    	//favoriteArtist.setImageResource(R.drawable.favorite_hilighted);
    		    	favoriteVenueToggleButton.setChecked(false);
    			}else{
    				//favoriteArtist.setImageResource(R.drawable.favorite_default);
    				favoriteVenueToggleButton.setChecked(true);
    			}
    		    //Log.d("ArtistTeamsEventslist", "favoriteVenueToggleButton.isChecked():"+favoriteVenueToggleButton.isChecked());
    		    favoriteVenueToggleButton.setOnClickListener(new OnClickListener() {
    				
    				@Override
    				public void onClick(View v) {
    					// TODO Auto-generated method stub
    					//Log.e("Venuelist", "setOnClickListener favoriteVenueToggleButton.isChecked():"+favoriteVenueToggleButton.isChecked());
    					//boolean fav_temp = db.getCurrentIdAvalibleInFavorites(hashMapData.VenueEventId, "venues");
    					//Log.i("ArtistTeamsEventslist", "fav_temp:"+fav_temp);
    					if (favoriteVenueToggleButton.isChecked()) {
    				  		//Log.d("ArtistTeamsEventslist", "if delet from data base:"+favoriteVenueToggleButton.isChecked());
    				  		// true means: default - // false means: selected
    				    	//delet from data base
    				    	//dataBaseHandler.deleteFavorites(data.eventId,"events");
    				  		favoriteVenueToggleButton.setChecked(true);
    				  		db.deleteFavorites(hashMapData.VenueEventId,"venues");
    					}else{
    						//Log.d("Venuelist", "else add to data base:"+favoriteVenueToggleButton.isChecked());
    						// add to data base
    						//dataBaseHandler.addContact(data,"events");	
    						favoriteVenueToggleButton.setChecked(false);
    						db.addContactVaneu(hashMapData,"venues");	
    					}
    				}
    			});
/*    		    venueFavoriteAdd.setOnClickListener(new OnClickListener() {
    				
    				@Override
    				public void onClick(View v) {
    					// TODO Auto-generated method stub
    					 Log.d("EventAlphabetListAdapter", "fav_temp:"+fav_temp);
    					if (fav_temp) {
    						venueFavoriteAdd.setImageResource(R.drawable.favorite_default);
    				    	//delet from data base
    				    	db.addContactVaneu(hashMapData,"venues");
    					}else{
    						venueFavoriteAdd.setImageResource(R.drawable.favorite_hilighted);
    						// add to data base
    						db.addContactVaneu(hashMapData,"venues");						
    					}
    				}
    			});*/
            /*
            TextView textView = (TextView) view.findViewById(R.id.textView1);
            textView.setText(item.textname);
            TextView textViewTitle = (TextView) view.findViewById(R.id.titletextView2);
            String address_ = item.textaddress+", "+item.textlocation;
            textViewTitle.setText(address_);
            textViewTitle.setVisibility(View.VISIBLE);*/
            //Log.e("AsyncTaskRunner", "ArtistTeamsEventList.listview_position: " + ArtistTeamsEventList.listview_position);	                 
        } else { // Section
            if (view == null) {
                LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = (LinearLayout) inflater.inflate(R.layout.row_section, parent, false);  
            }
            
            Section section = (Section) getItem(position);
            TextView textView = (TextView) view.findViewById(R.id.section_textView1);
            //Log.d("VenueAlph", "textView:"+textView);
            textView.setText(section.text);
        }
        
        return view;
    }
    
    List<Object[]> alphabet = new ArrayList<Object[]>();
    HashMap<String, Integer> sections = new HashMap<String, Integer>();
    
    @Override
    public Filter getFilter() {

        Filter filter = new Filter() {

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

            	//setRows(List<Row> rows)
            	List<VenueData> arrayListNames = (List<VenueData>) results.values;
                if (arrayListNames.size() != 0) {
                	List<Row> rows = new ArrayList<Row>();
                    rows.clear();
                    int start = 0;
                    int end = 0;
                    String previousLetter = null;
                    Object[] tmpIndexItem = null;
                    Pattern numberPattern = Pattern.compile("[0-9]");
                    for (VenueData country : arrayListNames) {        	 
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
                    //Log.d("ArtistTeamsFragment", "sections.size():"+sections.size());
                    //Log.d("ArtistTeamsFragment", "rows.size():"+rows.size());
                    setRows(rows);
                    notifyDataSetChanged();
				}
                
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                FilterResults results = new FilterResults();
                ArrayList<VenueData> FilteredArrayNames = new ArrayList<VenueData>();

                // perform your search here using the searchConstraint String.

                constraint = constraint.toString().toLowerCase();
               // Log.e("VALUES", "getCount():"+getCount());
                for (int i = 0; i < getCount(); i++) {
                	try {
                		Item item = (Item) getItem(i);
                		VenueData hashMapData = item.venueData;
                		String dataNames = hashMapData.VenueName;
                		if (dataNames.toLowerCase().startsWith(constraint.toString()))  {
                         FilteredArrayNames.add(hashMapData);
                     }
					} catch (Exception e) {
						// TODO: handle exception
						
					}
                	 

                }

                results.count = FilteredArrayNames.size();
                results.values = FilteredArrayNames;
               // Log.e("VALUES", results.values.toString());

                return results;
            }
        };

        return filter;
    }     
}

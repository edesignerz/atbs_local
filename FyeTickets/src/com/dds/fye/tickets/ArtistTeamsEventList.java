package com.dds.fye.tickets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.dds.fye.animation.AnimationHelper;
import com.dds.fye.database.DatabaseHandler;
import com.dds.fye.tickets.RangeSeekBar.OnRangeSeekBarChangeListener;
import com.dds.fye.tickets.adapter.AlphabetListAdapter;
import com.dds.fye.tickets.adapter.AlphabetListAdapter.Item;
import com.dds.fye.tickets.adapter.AlphabetListAdapter.Row;
import com.dds.fye.tickets.adapter.AlphabetListAdapter.Section;
import com.dds.fye.tickets.adapter.TicketListAdapter;
import com.dds.fye.tickets.adapter.VenuAllEventsAlphabetListAdapter;
import com.dds.fye.tickets.adapter.VenuAllEventsAlphabetListAdapter.Item_S;
import com.dds.fye.tickets.adapter.VenuAllEventsAlphabetListAdapter.Row_S;
import com.dds.fye.tickets.adapter.VenuAllEventsAlphabetListAdapter.Section_S;
import com.dds.fye.tickets.jsonparse.JSONParse;
import com.dds.fye.tickets.share.FacebookShareActivity;
import com.dds.fye.tickets.share.TwitterShareActivity;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.SipAddress;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.RawContacts;

import android.support.v4.app.FragmentActivity;
import android.text.SpannableStringBuilder;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnGenericMotionListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.widget.ViewFlipper;

public class ArtistTeamsEventList  extends FragmentActivity implements NumberPicker.OnValueChangeListener{
	//Button change;		
	private LinearLayout customSpinnerLayout;
	private ImageView customSpinnerLayoutImageView;
	//Frist view variable
	private LinearLayout backButton_linearLayout;	
	private ViewFlipper viewFlipper;	
	private TextView titleTextView,subtitleTextView;
	private ImageView imageViewEventInfoFilter,imageViewEventShare;
	private ListView listView;
	
	//Second view variable
	private LinearLayout price_LinearLayout,e_ticket_linearLayout, section_LinearLayout,row_LinearLayout,directionViewPopUp;
	private ImageView price_ImageView,e_ticket_ImageView, section_ImageView,row_ImageView;
	private ToggleButton secondEventAddToFavoritesToggleButton;
	private ImageView setingRowImageView;
	private ListView ticketlistView;
	//More performer Info close variable in tickets page
	private ImageView moreInfocloseView;
	private GoogleMap mGoogleMap;
	private ScrollView directionEventInfoScrollView;
	//Third
	private Button changeQtyButton;
	//Note variable
	private LinearLayout ticketNoteLayout;
	private ImageView ticketNoteCloseView;
	private TextView ticketNoteTextView;
	
	private ImageView sittingRowImageView_second;
	private TextView event_date_textView;
	private TextView section_textView;
	private TextView row_textView;
	private TextView quantity_textView;
	private TextView subtotal_textView;
	private TextView price_ea;
	private TextView filterTicketsListNameTextView;
	private ImageView secondtest1 ;
	private ImageView secondHote2 ;
	private ImageView secondinstant3;
	private ImageView secondemai4;
	private ToggleButton thirdTicketAddToFavoritesToggleButton ;
	private Button purchesButton;
	private List<EventsData> eventsSortArraylist = new ArrayList<EventsData>();
	private List<TicketsData> ticketsdetailsArrayList = new ArrayList<TicketsData>();
	private List<TicketsData> ticketsFilterdetailsArrayList = new ArrayList<TicketsData>();
	
	private String date_time = "";
	int listposition = 0;
	public static String data_time_for_list = "";
	//public static int listview_position = -1;
	private ConnectionDetector networkInfo;
	private List<Object[]> alphabet = new ArrayList<Object[]>();
	private HashMap<String, Integer> sections = new HashMap<String, Integer>();
	private AlphabetListAdapter adapter = new AlphabetListAdapter();	
	private SharedPreferences preferences;	
	private VenuAllEventsAlphabetListAdapter venueAllEventsAdapter = new VenuAllEventsAlphabetListAdapter();
	private ArrayList<Integer> allTicketsPriceList = new ArrayList<Integer>();
	private String event_id_query_code="";
	private String ticketId="";
	
	private String ticketCheckOut="";
	private String ticketReceipt_begins="";
	
	private String event_performerEventIdMoreInfo = "";
	private String events_performerName = "";
	private String event_performerVenueTitleMoreInfo = "";
	private String event_performerVenueAddressMoreInfo = "";
	private String event_performerVenueLocationMoreInfo = "";
	private String event_performerZipCodeMoreInfo = "";
	private String event_performerEventDate="";
	private String event_performerEventTime="";
	private String event_performerVenueLat="";
	private String event_performerVenueLon = "";
	
	DatabaseHandler dataBaseHandler ;
	
	String imageUrl;
	int no_ticket = 0;
	
	ProgressDialog progress;
	
	//Handle  Resume function execute.
	boolean controlResumeLoading = true;
	//Handle event resume reload data.
	boolean controlResumeEventView = true;
	
	//Handle artist and venue resume reload data.
	boolean controlResumeArtistVenueView = true;
	
	private static final int REQUEST_CODE = 1001;
	
	TicketListAdapter ticketadapter;// = new TicketListAdapter(this,ticketsFilterdetailsArrayList);
			
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.artist_teams_all_events);
		progress = new ProgressDialog(this);
		networkInfo = new ConnectionDetector(getApplicationContext());
		preferences = PreferenceManager.getDefaultSharedPreferences(this);
		dataBaseHandler = new DatabaseHandler(this);
		
		//Ticket note view
		ticketNoteLayout = (LinearLayout)findViewById(R.id.ticket_not_layout_view);
		ticketNoteCloseView = (ImageView)findViewById(R.id.ticket_note_close_textView2);
		ticketNoteTextView = (TextView)findViewById(R.id.nots_of_tickets_textView7);
		ticketNoteLayout.setVisibility(View.INVISIBLE);
		ticketNoteCloseView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ticketNoteLayout.setVisibility(View.INVISIBLE);
			}
		});
		customSpinnerLayout =(LinearLayout)findViewById(R.id.custom_spinner_layout);
		customSpinnerLayoutImageView = (ImageView)findViewById(R.id.custom_spinner_ImageView2);
		//customSpinnerLayoutImageView.
		//First view
		backButton_linearLayout = (LinearLayout)findViewById(R.id.back_linearlayout);
		titleTextView = (TextView)findViewById(R.id.spinner_title_textView2);
		subtitleTextView = (TextView)findViewById(R.id.sub_title_textView2);
		imageViewEventInfoFilter = (ImageView)findViewById(R.id.event_filter_and_event_info_imageView3_);
		viewFlipper = (ViewFlipper)findViewById(R.id.events_view_flipper);
		listView = (ListView)findViewById(R.id.artist_teams_events_listView1);
		imageViewEventShare = (ImageView)findViewById(R.id.event_share_buton_imageView2_);
		imageViewEventInfoFilter.setVisibility(View.GONE);
				
		//second view 
		price_LinearLayout = (LinearLayout)findViewById(R.id.price_layout);
		e_ticket_linearLayout = (LinearLayout)findViewById(R.id.e_ticket_layout);
		section_LinearLayout = (LinearLayout)findViewById(R.id.section_layout);
		row_LinearLayout = (LinearLayout)findViewById(R.id.row_layout);	
		filterTicketsListNameTextView =(TextView)findViewById(R.id.filter_tickets_list_name_textView_);
		price_ImageView = (ImageView)findViewById(R.id.price_imageView1);
		e_ticket_ImageView = (ImageView)findViewById(R.id.e_ticket_imageView1);
		section_ImageView = (ImageView)findViewById(R.id.section_imageView1); 
		row_ImageView = (ImageView)findViewById(R.id.row_imageView1);		
		setingRowImageView = (ImageView)findViewById(R.id.seting_row_imageView1);
		secondEventAddToFavoritesToggleButton = (ToggleButton)findViewById(R.id.second_add_to_favorites_ToggleButton);
		ticketlistView = (ListView)findViewById(R.id.artist_teams_events_tickets_listView1);
		//More performer Info close variable in tickets page
		moreInfocloseView = (ImageView)findViewById(R.id.event_direction_info_close_textView2);
		mGoogleMap = ((MapFragment)getFragmentManager().findFragmentById(R.id.map)).getMap();
		directionViewPopUp = (LinearLayout)findViewById(R.id.direction_view_pop_up);
		directionViewPopUp.setVisibility(View.INVISIBLE);
		directionEventInfoScrollView = (ScrollView)findViewById(R.id.direction_event_info_scroll_view);
		//directionEventInfoScrollView
		//onBackPressed() 
        //Create ticket page sorting view listener.
		ticketShortInSecondView();
		
		String ticket_available = preferences.getString("ticket_available", "12");
		  int avliableTickests = Integer.parseInt(ticket_available);
		  if (avliableTickests < 12) {
			  avliableTickests = avliableTickests + 1;
			  filterTicketsListNameTextView.setText("Showing Tickets: "+avliableTickests+" or more tickets");
		  }else{
			  filterTicketsListNameTextView.setText("Showing Tickets: All Tickets"); 
		  }
		//Third
		 changeQtyButton = (Button) findViewById(R.id.change_qty_button1);
		 price_ea = (TextView)findViewById(R.id.price_ea_textView1);
		 sittingRowImageView_second = (ImageView)findViewById(R.id.sitting_row_imageView2_view);
		 event_date_textView = (TextView)findViewById(R.id.event_date_textView7);
		 section_textView = (TextView)findViewById(R.id.event_section_textView10);
		 row_textView = (TextView)findViewById(R.id.event_row_textView11);
		 quantity_textView = (TextView)findViewById(R.id.quantity_ticket_textView10);
		 subtotal_textView = (TextView)findViewById(R.id.subtotal_price_textView11);
		 
		 secondtest1 = (ImageView) findViewById(R.id.third_test1_imageView1);
		 secondHote2 = (ImageView)findViewById(R.id.third_hotel_imageView2);		 	 
		 secondinstant3 = (ImageView)findViewById(R.id.third_instant_imageView3);
		 secondemai4 = (ImageView) findViewById(R.id.third_email_imageView4);

		 thirdTicketAddToFavoritesToggleButton = (ToggleButton) findViewById(R.id.third_ticket_add_favorites_ToggleButton);
		 purchesButton = (Button) findViewById(R.id.purches_button_textView16);
		 
		     
        listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Log.d("ArtistTeamsFragment", "arg2:"+arg2);
				String callClassName = getIntent().getStringExtra("call_class");
				Log.d("ArtistTeamsFragment", "callClassName:"+callClassName);
				  max_price_rang_ = 0;
				  min_price_rang_ = 0;
				  isChecked_ = false;
				  availableTicketsPos = 13;
				
				try {
				  if (callClassName.equals("artist_teams")) {
						//Log.d("ArtistTeamsFragment", "arg2:"+rows.get);			
					Item item = (Item) adapter.getItem(arg2);	
					
					//Log.d("ArtistTeamsFragment", "item.textid:"+item.textid);	
					//Log.d("ArtistTeamsFragment", "item.textmap:"+item.textmap);	
					//Log.d("ArtistTeamsFragment", "item.text1:"+item.text1);
					EventsData data = item.eventsData;;
					String eventid = data.eventId;
					Log.d("ArtistTeamsFragment", "eventid:"+eventid);	
					//Log.d("ArtistTeamsFragment", "item.textmap:"+item.textmap);	
					//Log.d("ArtistTeamsFragment", "item.text1:"+item.text1);
					if (!eventid.equals("")) {
						listposition = arg2;
						date_time = data.eventDateString;
						events_performerName = data.eventPerformerName;
						date_time = date_time.replace("@", "");
						String date_time_local = data.eventDateTimeLocal;
						String venue_name = data.eventVenueName;
						String selectedCity = preferences.getString("selectcity", "");
						String dateStr = date_time+"-"+venue_name+"-"+selectedCity;//"Denver, Co";
						subtitleTextView.setText(dateStr);
						String event_ = data.eventTitle;
						titleTextView.setText(event_);
						
						event_performerEventIdMoreInfo = data.eventId;
						event_performerVenueTitleMoreInfo = data.eventVenueName;
						event_performerVenueAddressMoreInfo = data.localEventAddress;
						event_performerVenueLocationMoreInfo = data.eventVenueLocation;
						event_performerZipCodeMoreInfo = data.eventVenueZipCode;
						event_performerEventDate = data.eventDateString;
						event_performerEventTime = data.eventDateTimeLocal;
						event_performerVenueLat=data.eventVenueLat;
						event_performerVenueLon = data.eventVenueLon;
						
						imageViewEventInfoFilter.setVisibility(View.VISIBLE);
						imageViewEventInfoFilter.setImageResource(R.drawable.filter_button);
						date_time = data.eventDateString;
						Log.d("ArtistTeamsFragment", "date_time:"+date_time);
						viewFlipper.setInAnimation(AnimationHelper
			                    .inFromRightAnimation());
						viewFlipper.setOutAnimation(AnimationHelper
			                    .outToLeftAnimation());
						viewFlipper.showNext();
						

						//Add event to favorite  
						favoriteEventFromTicketPage(data);
						
						Boolean isInternetPresent = networkInfo.isConnectingToInternet();
						if (isInternetPresent) {
							//AsyncTaskRunner myWebFetch = new AsyncTaskRunner();
						    // myWebFetch.execute(api_url,"tickets");
							 loadTicketData();
						      imageUrl = data.eventMapStatic;
						     //Image load						       
						     new ImageDownloader().execute(imageUrl);
						}				
									
					}
				  }else if (callClassName.equals("venues")) {
						//Log.d("ArtistTeamEvents", "venues");
						Item_S item_venue = (Item_S) venueAllEventsAdapter.getItem(arg2);	
						EventsData data_venue = item_venue.eventsData;
						String venue_eventid = data_venue.eventId;
						events_performerName = data_venue.eventPerformerName;
						Log.d("ArtistTeamsFragment", "venue_eventid:"+venue_eventid);	
						
						listposition = arg2;
						date_time = data_venue.eventDateString;
						date_time = date_time.replace("@", "");
						String date_time_local = data_venue.eventDateTimeLocal;
						String venue_name = data_venue.eventVenueName;
						String selectedCity = preferences.getString("selectcity", "");
						String dateStr = date_time+"-"+venue_name+"-"+selectedCity;//"Denver, Co";
						subtitleTextView.setText(dateStr);
						String event_ = data_venue.eventTitle;
						titleTextView.setText(event_);
						
						event_performerEventIdMoreInfo = data_venue.eventId;
						event_performerVenueTitleMoreInfo = data_venue.eventVenueName;
						event_performerVenueAddressMoreInfo = data_venue.localEventAddress;
						event_performerVenueLocationMoreInfo = data_venue.eventVenueLocation;
						event_performerZipCodeMoreInfo = data_venue.eventVenueZipCode;
						event_performerEventDate = data_venue.eventDateString;
						event_performerEventTime = data_venue.eventDateTimeLocal;
						event_performerVenueLat=data_venue.eventVenueLat;
						event_performerVenueLon = data_venue.eventVenueLon;
						
						imageViewEventInfoFilter.setVisibility(View.VISIBLE);
						imageViewEventInfoFilter.setImageResource(R.drawable.filter_button);
						date_time = data_venue.eventDateString;
						Log.d("ArtistTeamsFragment", "date_time:"+date_time);
						viewFlipper.setInAnimation(AnimationHelper
			                    .inFromRightAnimation());
						viewFlipper.setOutAnimation(AnimationHelper
			                    .outToLeftAnimation());
						viewFlipper.showNext();
						
						//Add event to favorite  
						favoriteEventFromTicketPage(data_venue);
						
						Boolean isInternetPresent = networkInfo.isConnectingToInternet();
						if (isInternetPresent) {
							//AsyncTaskRunner myWebFetch = new AsyncTaskRunner();
						    // myWebFetch.execute(api_url,"tickets");
							 Log.d("ArtistTeamsFragment", "imageUrl");
							 loadTicketData();
						     imageUrl = data_venue.eventMapStatic;
						      Log.d("ArtistTeamsFragment", "imageUrl:"+imageUrl);
						     //Image load						       
						     new ImageDownloader().execute(imageUrl);
						}				
				 }
			     customSpinnerLayoutImageView.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View arg0) {
							// TODO Auto-generated method stub
							
				              if (popup != null) {
									popup.dismiss();
							  }
							  if (p != null){
								   showMoreInfoOfCurrentVenue(ArtistTeamsEventList.this, p);
							  }							
						}
				 });
			        //-----------------------below---------------------------//
			        customSpinnerLayout.setOnClickListener(ticketSpinnerListener);
			        
				} catch (Exception e) {
					// TODO: handle exception
					Log.e("ArtistTeamsFragment", "e:"+e);	
					Log.e("ArtistTeamsFragment", "callClassName:"+callClassName);	
				}
			}
				
		});
        backButton_linearLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				backToPreviouView();
			}
		});
        imageViewEventInfoFilter.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (viewFlipper.getDisplayedChild() == 1) {
					Bundle args = new Bundle();
					args.putString("message","ticketfilter");
					/*FragmentManager fm = getFragmentManager();
					TicketFilterDialogWindow alert = new TicketFilterDialogWindow();
					alert.setArguments(args);
		      		alert.show(fm, "Alert_Dialog");*/
					 //LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		              //View  view = (ViewFlipper) inflater.inflate(R.layout.tickets_filter_dialog, null);
		              if (popup != null) {
							popup.dismiss();
					  }
					       if (p != null)
					       showPopup(ArtistTeamsEventList.this, p);
				}else if(viewFlipper.getDisplayedChild() == 2){
					ticketNoteLayout.setVisibility(View.VISIBLE);
				}
			}
		});
       imageViewEventShare.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				onShareSocialNetwork();
			}
		});
        price_LinearLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub				
				 price_ImageView.setBackgroundColor(getResources().getColor(R.color.selected_bar_tab_orange_tab));
				 e_ticket_ImageView.setBackgroundColor(Color.TRANSPARENT);
				 section_ImageView.setBackgroundColor(Color.TRANSPARENT);
				 row_ImageView.setBackgroundColor(Color.TRANSPARENT);
				 SharedPreferences.Editor editor = preferences.edit();
		    	 editor.putString("t_Sorting","price");	        		
		    	 editor.commit();
		    	 secondViewAllTickets();
			}
		});
		e_ticket_linearLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
			
				price_ImageView.setBackgroundColor(Color.TRANSPARENT);
				e_ticket_ImageView.setBackgroundColor(getResources().getColor(R.color.selected_bar_tab_orange_tab));
				section_ImageView.setBackgroundColor(Color.TRANSPARENT);
				row_ImageView.setBackgroundColor(Color.TRANSPARENT);
				SharedPreferences.Editor editor = preferences.edit();
		    	editor.putString("t_Sorting","e_ticket");	        		
		    	editor.commit();
		    	secondViewAllTickets();
			}
		});
		section_LinearLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
				price_ImageView.setBackgroundColor(Color.TRANSPARENT);
				e_ticket_ImageView.setBackgroundColor(Color.TRANSPARENT);
				section_ImageView.setBackgroundColor(getResources().getColor(R.color.selected_bar_tab_orange_tab));
				row_ImageView.setBackgroundColor(Color.TRANSPARENT);	
				SharedPreferences.Editor editor = preferences.edit();
		    	editor.putString("t_Sorting","section");	        		
		    	editor.commit();
		    	secondViewAllTickets();
			}
		});
		row_LinearLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				price_ImageView.setBackgroundColor(Color.TRANSPARENT);
				e_ticket_ImageView.setBackgroundColor(Color.TRANSPARENT);
				section_ImageView.setBackgroundColor(Color.TRANSPARENT);
				row_ImageView.setBackgroundColor(getResources().getColor(R.color.selected_bar_tab_orange_tab));	
				SharedPreferences.Editor editor = preferences.edit();
		    	editor.putString("t_Sorting","row");	        		
		    	editor.commit();
		    	secondViewAllTickets();
			}
		});
		purchesButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				//event_id_query_code
				//no_ticket
				//ticketId
				//ticketCheckOut= ticketArrayHashMap.ticketCheckout_url;;
				// ticketReceipt_begins=ticketArrayHashMap.ticketReceipt_begins;
				Log.d("ArtistTeamsFragment", "event_id_query_code:"+event_id_query_code);
				Log.d("ArtistTeamsFragment", "ticketId:"+ticketId);		
				Log.d("ArtistTeamsFragment", "no_ticket:"+no_ticket);
				Log.d("ArtistTeamsFragment", "ticketCheckOut:"+ticketCheckOut);
				Log.d("ArtistTeamsFragment", "ticketReceipt_begins:"+ticketReceipt_begins);
				
				Intent intent = new Intent(ArtistTeamsEventList.this, WebViewFragmentActivity.class);
				intent.putExtra("event_id_query_code", event_id_query_code);
				intent.putExtra("ticketId", ticketId);
				intent.putExtra("no_ticket", String.valueOf(no_ticket));
				intent.putExtra("ticketCheckOut", ticketCheckOut);
				intent.putExtra("ticketReceipt_begins", ticketReceipt_begins);
		        startActivity(intent);
			}
		});
	}
	 /**
	  * Tickets spinner menu list.
	  */
	OnClickListener ticketSpinnerListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub

            if (popup != null) {
					popup.dismiss();
			  }
			  if (p != null)
				   showMoreInfoOfCurrentVenue(ArtistTeamsEventList.this, p);
		}
	};
	/**
	 * Go back to previous page.
	 */
	private void backToPreviouView(){
		Log.d("ArtistTeamsFragment", "viewFlipper.getDisplayedChild():"+viewFlipper.getDisplayedChild());
		
		if (viewFlipper.getDisplayedChild() == 0) {
			finish();
		}else{
			if (viewFlipper.getDisplayedChild() == 1) {
				controlResumeArtistVenueView = true;
				String callClassName = getIntent().getStringExtra("call_class");
				if (callClassName.equals("events")) {
					finish();
				}else if(callClassName.equals("venues")){
					String venuName = getIntent().getStringExtra("venuName");
					String venuLocation = getIntent().getStringExtra("venuLocation");
					
					titleTextView.setText(venuName);							
					subtitleTextView.setText(venuLocation);	
				}else{
					String name = getIntent().getStringExtra("name");
					titleTextView.setText(name);
					String selectedCity = preferences.getString("selectcity", "");
					subtitleTextView.setText(selectedCity);													
				}
				imageViewEventInfoFilter.setVisibility(View.INVISIBLE);
				
				customSpinnerLayoutImageView.setOnClickListener(null);
				customSpinnerLayout.setOnClickListener(null);
				
				//customSpinnerLayoutImageView.setVisibility(View.VISIBLE);
				
				viewFlipper.setInAnimation(AnimationHelper
	                    .inFromLeftAnimation());
				viewFlipper.setOutAnimation(AnimationHelper
	                    .outToRightAnimation());
				viewFlipper.showPrevious();
			}
			if (viewFlipper.getDisplayedChild() == 2) {
				controlResumeEventView = true;
				String callClassName = getIntent().getStringExtra("call_class");
				if (callClassName.equals("tickets")){
					finish();
				}else{
					imageViewEventInfoFilter.setVisibility(View.VISIBLE);
					imageViewEventInfoFilter.setImageResource(R.drawable.filter_button);
					
					customSpinnerLayoutImageView.setVisibility(View.VISIBLE);
					customSpinnerLayout.setOnClickListener(ticketSpinnerListener);
					
					viewFlipper.setInAnimation(AnimationHelper
		                    .inFromLeftAnimation());
					viewFlipper.setOutAnimation(AnimationHelper
		                    .outToRightAnimation());
					viewFlipper.showPrevious();	
				}
			}					
		}
	}
	/**
	 * Add events to favorite  from ticket page
	 */
	private void favoriteEventFromTicketPage(final EventsData data){
	    //Add and remove favorite.
		Log.d("ArtistTeamsEventslist", "secondEventAddToFavoritesToggleButton isChecked:"+secondEventAddToFavoritesToggleButton.isChecked());
	    final boolean fav_temp = dataBaseHandler.getCurrentIdAvalibleInFavorites(data.eventId, "events");	
	    Log.d("ArtistTeamsEventslist", "secondEventAddToFavoritesToggleButton fav_temp:"+fav_temp);
	    if (fav_temp) {
	    	//secondEventAddToFavoritesImageView1.setImageResource(R.drawable.favorite_hilighted);
	    	 secondEventAddToFavoritesToggleButton.setChecked(false);
		}else{
			//secondEventAddToFavoritesImageView1.setImageResource(R.drawable.favorite_default);
			 secondEventAddToFavoritesToggleButton.setChecked(true);
		}
	    Log.d("ArtistTeamsEventslist", "secondEventAddToFavoritesToggleButton isChecked:"+secondEventAddToFavoritesToggleButton.isChecked());
	    secondEventAddToFavoritesToggleButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			  	if (secondEventAddToFavoritesToggleButton.isChecked()) {
			  		Log.d("ArtistTeamsEventslist", "delet from data base");
					//secondEventAddToFavoritesImageView1.setImageResource(R.drawable.favorite_default);
					//secondEventAddToFavoritesToggleButton.setChecked(false);
			    	//delet from data base
			    	dataBaseHandler.deleteFavorites(data.eventId,"events");
				}else{
					Log.d("ArtistTeamsEventslist", " add to data base");
					//secondEventAddToFavoritesImageView1.setImageResource(R.drawable.favorite_hilighted);
					//secondEventAddToFavoritesToggleButton.setChecked(true);
					// add to data base
					dataBaseHandler.addContact(data,"events");						
				}
			}
		});
	    	  	    
	    String callClassName = getIntent().getStringExtra("call_class");
	    if (callClassName.equals("artist_teams")) {
	    	adapter.notifyDataSetChanged();
		}else if (callClassName.equals("venues")){
			venueAllEventsAdapter.notifyDataSetChanged();
		}else if (callClassName.equals("events")){
			EventsFragment.upDateList();
		}
        
	}
	/**
	 * Load ticket detail.
	 */
	private void loadTicketData(){

		String callClassName = getIntent().getStringExtra("call_class");
		
		if (callClassName.equals("artist_teams")) {
			Item item = (Item) adapter.getItem(listposition);
			EventsData data = item.eventsData;
			event_id_query_code = event_performerEventIdMoreInfo;	
		}else if (callClassName.equals("events")) {
			event_id_query_code =  event_performerEventIdMoreInfo;
		}else if(callClassName.equals("venues")){
			Item_S item_venue = (Item_S) venueAllEventsAdapter.getItem(listposition);	
			EventsData data_venue = item_venue.eventsData;;
			event_id_query_code = event_performerEventIdMoreInfo;
		}
	//"&sort="+"tickets.price"+asc
		String api_url="";
		/*if (!maxpriceRange.equals("")) {
			if (!eTicketsOnly.equals("")) {// ticket.price.gte = price, check only email in array delivery method
				api_url = getResources().getString(R.string.events_tickets)+"event.id="+query_code+
						"&ticket.price.gte="+minpriceRange+"&ticket.price.lte="+maxpriceRange+"&delivery_methods="+eTicketsOnly;
				
			}else{
				api_url = getResources().getString(R.string.events_tickets)+"event.id="+query_code+"&ticket.price.gte="+minpriceRange+
						"&ticket.price.lte="+maxpriceRange;
			}
		}else{
			if (!eTicketsOnly.equals("")) {// ticket.price.gte = price, check only email in array delivery method
				api_url = getResources().getString(R.string.events_tickets)+"event.id="+query_code+
						"&delivery_methods="+eTicketsOnly;
			}else{
				api_url = getResources().getString(R.string.events_tickets)+"event.id="+query_code;				
			}
		}*/
		api_url = getResources().getString(R.string.events_tickets)+"event.id="+event_id_query_code;	
				
		Log.d("ArtistTeamsFragment", "api_url:"+api_url);	
		Boolean isInternetPresent = networkInfo.isConnectingToInternet();
		if (isInternetPresent) {
			AsyncTaskRunner myWebFetch = new AsyncTaskRunner();
		     myWebFetch.execute(api_url,"tickets");
		}		
	}
	
	@Override
	public void onResume(){
		super.onResume();
		//imageViewInfo.setVisibility(View.VISIBLE);				
		//String position = getIntent().getStringExtra("listViewPosition");
	 Log.d("ArtistTeamsFragment", "onResume After performe class back handler:"+controlResumeLoading);	
	 if (controlResumeLoading) {					
		String callClassName = getIntent().getStringExtra("call_class");
		if (callClassName.equals("artist_teams")) {
			String name = getIntent().getStringExtra("name");
			//listview_position = Integer.parseInt(position);
			//Log.d("ArtistTeamsFragment", "position:"+position);
			//Log.d("ArtistTeamsFragment", "listview_position:"+listview_position);
			String selectedCity = preferences.getString("selectcity", "");
			if (controlResumeArtistVenueView) {
				controlResumeArtistVenueView = false;
				subtitleTextView.setText(selectedCity);
				titleTextView.setText(name);	
			}

			String[] split_name = name.split("[ ]");
			Log.d("ArtistTeamsFragment", "split_name[0]:"+split_name[0]);		
			String title_ ="";
			for (int i = 0; i < split_name.length; i++) {
				if(title_.equals("")){
					title_ = split_name[i];
				}else{
					title_ = title_ +"+"+ split_name[i];
				}
			}
			Log.d("ArtistTeamsFragment", "title_:"+title_);
			String query_code = "performer.name="+title_+"&performer.home=0";		
		    String api_url = getResources().getString(R.string.artist_teams_events)+query_code;
		    Log.d("ArtistTeamsFragment", "api_url:"+api_url);
		    Boolean isInternetPresent = networkInfo.isConnectingToInternet();
			if (isInternetPresent) {
				AsyncTaskRunner myWebFetch = new AsyncTaskRunner();
		        myWebFetch.execute(api_url,"events");
			}
		    
		}else if (callClassName.equals("venues")) {
			Log.d("ArtistTeamEvents", "venues");
			String eventTitle = getIntent().getStringExtra("eventTitle");		
			String venuName = getIntent().getStringExtra("venuName");
			String venuLocation = getIntent().getStringExtra("venuLocation");		
			if (controlResumeArtistVenueView) {
				controlResumeArtistVenueView = false;
				titleTextView.setText(venuName);
				subtitleTextView.setText(venuLocation);
			}

			String[] split_name = venuName.split("[ ]");
			Log.d("ArtistTeamsFragment", "split_name[0]:"+split_name[0]);		
			String title_ ="";
			title_ = venuName.replace(" ", "+");
			/*for (int i = 0; i < split_name.length; i++) {
				if(title_.equals("")){
					title_ = split_name[i];
				}else{
					title_ = title_ +"+"+ split_name[i];
				}
			}*/
			String query_code = "venue.name="+title_;//venue.name//venue.name=turner+field
			
			String selectedCity = preferences.getString("selectcity", "");
			String[] citySplite = selectedCity.split("[,]");				
			String[] spaceFreeCity = citySplite[0].split("[ ]");
			String selectCityName = "";
			selectCityName = selectedCity.replace(" ", "+");
			/*for (String string : spaceFreeCity) {
				if (selectCityName.equals("")) {
					selectCityName = string;
				}else{
					selectCityName = selectCityName+"+"+string;
				}					
			}*/
			//String id = getIntent().getStringExtra("id");
			//http://mobapi.atbss.com/events?venue.id=100
			 String api_url = getResources().getString(R.string.artist_teams_events)+query_code+"&venue.city="+selectCityName;
			 Boolean isInternetPresent = networkInfo.isConnectingToInternet();
				if (isInternetPresent) {
					AsyncTaskRunner myWebFetch = new AsyncTaskRunner();
			        myWebFetch.execute(api_url,"events");
				}

		} else if (callClassName.equals("events")) {
			Log.d("ArtistTeamEvents", "events");
			/*String id = getIntent().getStringExtra("id");		
			String map = getIntent().getStringExtra("map");
			String date_string = getIntent().getStringExtra("date_string");*/
			
			String eventId =  getIntent().getStringExtra("eventId");
			String eventTitle =  getIntent().getStringExtra("eventTitle");	
			String eventDateString =  getIntent().getStringExtra("eventDateString");	
			String evantDateTimeLocal =  getIntent().getStringExtra("evantDateTimeLocal");	
			String eventVenuName =  getIntent().getStringExtra("eventVenuName");	
			String eventMapStatic =  getIntent().getStringExtra("map");	
			String listViewPosition = getIntent().getStringExtra("listViewPosition");
			
			String eventPerformerName =  getIntent().getStringExtra("eventPerformerName");
			
			
			Log.d("MainActivity", "showMoreInfoOfCurrentVenue events_performerName:"+events_performerName);
			 event_performerEventIdMoreInfo = getIntent().getStringExtra("eventId");
			 events_performerName = getIntent().getStringExtra("eventPerformerName");	
			 event_performerVenueTitleMoreInfo = getIntent().getStringExtra("eventVenuName");
			 event_performerVenueAddressMoreInfo = getIntent().getStringExtra("eventAddress");
			 event_performerVenueLocationMoreInfo = getIntent().getStringExtra("eventVenueLocation");
			 event_performerZipCodeMoreInfo = getIntent().getStringExtra("eventVenuePostalCode");
			 event_performerEventDate = getIntent().getStringExtra("eventDateString");
			 event_performerEventTime = getIntent().getStringExtra("evantDateTimeLocal");
			 event_performerVenueLat = getIntent().getStringExtra("eventVenueLat");
			 event_performerVenueLon = getIntent().getStringExtra("eventVenueLon");
			 
			 EventsData eventsData = new EventsData(eventId, eventTitle, eventDateString, evantDateTimeLocal, eventMapStatic, 
						eventVenuName, event_performerVenueLocationMoreInfo, event_performerVenueAddressMoreInfo, "", eventPerformerName, 
						"", event_performerZipCodeMoreInfo, event_performerVenueLon, event_performerVenueLat);
			 //dfgdfg
			
			Log.d("ArtistTeamsFragment", "date_string :"+eventTitle);
			Log.d("ArtistTeamsFragment", "eventDateString:"+eventDateString);	
			Log.d("ArtistTeamsFragment", "evantDateTimeLocal:"+evantDateTimeLocal);	
			Log.d("ArtistTeamsFragment", "eventVenuName :"+eventVenuName);	
			Log.d("ArtistTeamsFragment", "eventMapStatic :"+eventMapStatic);
			
			if (!eventTitle.equals("")) {

					listposition = Integer.parseInt(listViewPosition);
					date_time = eventDateString;
					date_time = date_time.replace("@", "");
					String date_time_local = evantDateTimeLocal;
					String venue_name = eventVenuName;
					String dateStr = date_time+"-"+venue_name;//+"-"+"Denver, Co";
					subtitleTextView.setText(dateStr);
					String event_ = eventTitle;
					titleTextView.setText(event_);

					date_time = eventDateString;
					Log.d("ArtistTeamsFragment", "date_time:"+date_time);
					Log.e("ArtistTeamsFragment", "if controlResumeEventView:"+controlResumeEventView);
					if (controlResumeEventView) {
						Log.i("ArtistTeamsFragment", "if controlResumeEventView:"+controlResumeEventView);
						controlResumeEventView = false;
						
						imageViewEventInfoFilter.setVisibility(View.VISIBLE);
						imageViewEventInfoFilter.setImageResource(R.drawable.filter_button);
						
						viewFlipper.setInAnimation(AnimationHelper
			                    .inFromRightAnimation());
						viewFlipper.setOutAnimation(AnimationHelper
			                    .outToLeftAnimation());
						//viewFlipper.showNext();
						viewFlipper.setDisplayedChild(1);
				        customSpinnerLayoutImageView.setOnClickListener(new OnClickListener() {
							
							@Override
							public void onClick(View arg0) {
								// TODO Auto-generated method stub
								
					              if (popup != null) {
										popup.dismiss();
								  }
								  if (p != null){
									   showMoreInfoOfCurrentVenue(ArtistTeamsEventList.this, p);
								  }
								
							}
						});
				        //-------------------------below----------------------------//
				        customSpinnerLayout.setOnClickListener(ticketSpinnerListener);
					}
				
					//Add event to favorite  
					favoriteEventFromTicketPage(eventsData);
					
					Boolean isInternetPresent = networkInfo.isConnectingToInternet();
					if (isInternetPresent) {
						//AsyncTaskRunner myWebFetch = new AsyncTaskRunner();
					    // myWebFetch.execute(api_url,"tickets");
						 loadTicketData();
					      imageUrl = eventMapStatic;
					      Log.d("ArtistTeamsFragment", "event imageUrl:"+imageUrl);
					     //Image load						       
					     new ImageDownloader().execute(imageUrl);
					}				
			}
		}else if (callClassName.equals("tickets")) {
			
			viewFlipper.setDisplayedChild(2);
			imageViewEventInfoFilter.setImageResource(R.drawable.ticket_notes_button);
			imageViewEventInfoFilter.setVisibility(View.VISIBLE);
			no_ticket = 0;
									
			 secondtest1.setVisibility(View.INVISIBLE);
			 secondHote2.setVisibility(View.INVISIBLE);		 	 
			 secondinstant3.setVisibility(View.INVISIBLE);
			 secondemai4.setVisibility(View.INVISIBLE);
			   			
			String ticketId = getIntent().getStringExtra("ticketId");
			String ticketTitle= getIntent().getStringExtra("ticketTitle");
			String ticketSection= getIntent().getStringExtra("ticketSection"); 
			String ticketRow= getIntent().getStringExtra("ticketRow");
			int ticketPrice= Integer.parseInt(getIntent().getStringExtra("ticketPrice"));
			String ticketSplits= getIntent().getStringExtra("ticketSplits");	
			
			String ticketType= getIntent().getStringExtra("ticketType");
			String ticketDelivery_methods= getIntent().getStringExtra("ticketDelivery_methods");
			String ticketDescription= getIntent().getStringExtra("ticketDescription");
			String ticketCheckout_url= getIntent().getStringExtra("ticketCheckout_url");
			String ticketReceipt_begins= getIntent().getStringExtra("ticketReceipt_begins");
			TicketsData ticketsData = new TicketsData(ticketId, ticketTitle, ticketSection, ticketRow, 
					ticketPrice, "", ticketDescription, ticketSplits, ticketDelivery_methods, 
					ticketType, ticketCheckout_url, ticketReceipt_begins);
			imageUrl = dataBaseHandler.getTicketEvnetMapUrl(ticketId);	
			Log.d("ArtistTeamsFragment", "imageUrl:"+imageUrl);
		     //Image load						       
		     new ImageDownloader().execute(imageUrl);
			thirdViewTicketDetail(ticketsData);
		}
	  }else{
		controlResumeLoading = true;
	  }
	}
	 /**
	  * Activity call back result.
	  */
	  @Override
	  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
	      if (data.hasExtra("returnkey")) {
	        String result = data.getExtras().getString("returnkey");
	        if (result != null && result.length() > 0) {
	          Log.d("ArtistTeamsFragment", "onActivityResult After performe class back handler:");
	          //Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
	          String callClassName = getIntent().getStringExtra("call_class");
	          if (callClassName.equals("events")) {
	  			Log.d("ArtistTeamEvents", "events");
	  			
	  			String eventId =  data.getExtras().getString("eventId");
	  			String eventTitle =  data.getExtras().getString("eventTitle");	
	  			String eventDateString = data.getExtras().getString("eventDateString");	
	  			String evantDateTimeLocal =  data.getExtras().getString("evantDateTimeLocal");	
	  			String eventVenuName =  data.getExtras().getString("eventVenuName");	
	  			String eventMapStatic =  data.getExtras().getString("map");	
	  			String listViewPosition = data.getExtras().getString("listViewPosition");
	  			
	  			String eventPerformerName =  data.getExtras().getString("eventPerformerName");
	  			
	  			Log.d("MainActivity", "showMoreInfoOfCurrentVenue events_performerName:"+events_performerName);
	  			 event_performerEventIdMoreInfo = data.getExtras().getString("eventId");
	  			 events_performerName = data.getExtras().getString("eventPerformerName");	
	  			 event_performerVenueTitleMoreInfo = data.getExtras().getString("eventVenuName");
	  			 event_performerVenueAddressMoreInfo = data.getExtras().getString("eventAddress");
	  			 event_performerVenueLocationMoreInfo = data.getExtras().getString("eventVenueLocation");
	  			 event_performerZipCodeMoreInfo = data.getExtras().getString("eventVenuePostalCode");
	  			 event_performerEventDate = data.getExtras().getString("eventDateString");
	  			 event_performerEventTime = data.getExtras().getString("evantDateTimeLocal");
	  			 event_performerVenueLat = data.getExtras().getString("eventVenueLat");
	  			 event_performerVenueLon = data.getExtras().getString("eventVenueLon");
	  			 
	  			 EventsData eventsData = new EventsData(eventId, eventTitle, eventDateString, evantDateTimeLocal, eventMapStatic, 
	  						eventVenuName, event_performerVenueLocationMoreInfo, event_performerVenueAddressMoreInfo, "", eventPerformerName, 
	  						"", event_performerZipCodeMoreInfo, event_performerVenueLon, event_performerVenueLat);
	  			 //dfgdfg
	  			
	  			Log.d("ArtistTeamsFragment", "date_string :"+eventTitle);
	  			Log.d("ArtistTeamsFragment", "eventDateString:"+eventDateString);	
	  			Log.d("ArtistTeamsFragment", "evantDateTimeLocal:"+evantDateTimeLocal);	
	  			Log.d("ArtistTeamsFragment", "eventVenuName :"+eventVenuName);	
	  			Log.d("ArtistTeamsFragment", "eventMapStatic :"+eventMapStatic);
	  			if (!eventTitle.equals("")) {

	  					listposition = Integer.parseInt(listViewPosition);
	  					date_time = eventDateString;
	  					date_time = date_time.replace("@", "");
	  					String date_time_local = evantDateTimeLocal;
	  					String venue_name = eventVenuName;
	  					String dateStr = date_time+"-"+venue_name;//+"-"+"Denver, Co";
	  					subtitleTextView.setText(dateStr);
	  					String event_ = eventTitle;
	  					titleTextView.setText(event_);
	  					
	  					date_time = eventDateString;
	  					
	  					//Add event to favorite  
	  					favoriteEventFromTicketPage(eventsData);
	  					
	  					Boolean isInternetPresent = networkInfo.isConnectingToInternet();
	  					if (isInternetPresent) {
	  						//AsyncTaskRunner myWebFetch = new AsyncTaskRunner();
	  					    // myWebFetch.execute(api_url,"tickets");
	  						 loadTicketData();
	  					      imageUrl = eventMapStatic;
	  					      Log.d("ArtistTeamsFragment", "event imageUrl:"+imageUrl);
	  					     //Image load						       
	  					     new ImageDownloader().execute(imageUrl);
	  					}				
	  			}
	  		  }else if (callClassName.equals("venues")){
					//Item_S item_venue = (Item_S) venueAllEventsAdapter.getItem(arg2);
					String eventId =  data.getExtras().getString("eventId");
		  			String eventTitle =  data.getExtras().getString("eventTitle");	
		  			String eventDateString = data.getExtras().getString("eventDateString");	
		  			String evantDateTimeLocal =  data.getExtras().getString("evantDateTimeLocal");	
		  			String eventVenuName =  data.getExtras().getString("eventVenuName");	
		  			String eventMapStatic =  data.getExtras().getString("map");	
		  			String listViewPosition = data.getExtras().getString("listViewPosition");		  			
		  			String eventPerformerName =  data.getExtras().getString("eventPerformerName");
					
					//EventsData data_venue = item_venue.eventsData;
					//String venue_eventid = data_venue.eventId;
					events_performerName = data.getExtras().getString("eventPerformerName");
					//Log.d("ArtistTeamsFragment", "venue_eventid:"+venue_eventid);	
					
					//listposition = arg2;
					date_time = data.getExtras().getString("eventDateString");
					date_time = date_time.replace("@", "");
					String date_time_local = data.getExtras().getString("evantDateTimeLocal");;
					String venue_name = data.getExtras().getString("eventVenuName");;
					String dateStr = date_time+"-"+venue_name;//+"-"+"Denver, Co";
					subtitleTextView.setText(dateStr);
					String event_ = data.getExtras().getString("eventTitle");
					titleTextView.setText(event_);
					
					 event_performerEventIdMoreInfo = data.getExtras().getString("eventId");
					 event_performerVenueTitleMoreInfo = data.getExtras().getString("eventVenuName");
					 event_performerVenueAddressMoreInfo = data.getExtras().getString("eventAddress");
					 event_performerVenueLocationMoreInfo = data.getExtras().getString("eventVenueLocation");
					 event_performerZipCodeMoreInfo = data.getExtras().getString("eventVenuePostalCode");
					 event_performerEventDate = data.getExtras().getString("eventDateString");	
					 event_performerEventTime = data.getExtras().getString("evantDateTimeLocal");
					 event_performerVenueLat = data.getExtras().getString("eventVenueLat");
					 event_performerVenueLon = data.getExtras().getString("eventVenueLon");
					
					imageViewEventInfoFilter.setVisibility(View.VISIBLE);
					imageViewEventInfoFilter.setImageResource(R.drawable.filter_button);
					date_time = data.getExtras().getString("eventDateString");
					Log.d("ArtistTeamsFragment", "date_time:"+date_time);
					 EventsData eventsData = new EventsData(eventId, eventTitle, eventDateString, evantDateTimeLocal, eventMapStatic, 
		  						eventVenuName, event_performerVenueLocationMoreInfo, event_performerVenueAddressMoreInfo, "", eventPerformerName, 
		  						"", event_performerZipCodeMoreInfo, event_performerVenueLon, event_performerVenueLat);
					//Add event to favorite  
					favoriteEventFromTicketPage(eventsData);
					
					Boolean isInternetPresent = networkInfo.isConnectingToInternet();
					if (isInternetPresent) {
						//AsyncTaskRunner myWebFetch = new AsyncTaskRunner();
					    // myWebFetch.execute(api_url,"tickets");
						 Log.d("ArtistTeamsFragment", "imageUrl");
						 loadTicketData();
					     imageUrl = eventMapStatic;
					      Log.d("ArtistTeamsFragment", "imageUrl:"+imageUrl);
					     //Image load						       
					     new ImageDownloader().execute(imageUrl);
					}
	  		  }else if (callClassName.equals("artist_teams")){
	  			String eventId =  data.getExtras().getString("eventId");
	  			String eventTitle =  data.getExtras().getString("eventTitle");	
	  			String eventDateString = data.getExtras().getString("eventDateString");	
	  			String evantDateTimeLocal =  data.getExtras().getString("evantDateTimeLocal");	
	  			String eventVenuName =  data.getExtras().getString("eventVenuName");	
	  			String eventMapStatic =  data.getExtras().getString("map");	
	  			String listViewPosition = data.getExtras().getString("listViewPosition");		  			
	  			String eventPerformerName =  data.getExtras().getString("eventPerformerName");
	  			
	  			Log.d("MainActivity", "showMoreInfoOfCurrentVenue events_performerName:"+events_performerName);
	  			
	  			 event_performerEventIdMoreInfo = data.getExtras().getString("eventId");
	  			 events_performerName = data.getExtras().getString("eventPerformerName");	
	  			 event_performerVenueTitleMoreInfo = data.getExtras().getString("eventVenuName");
	  			 event_performerVenueAddressMoreInfo = data.getExtras().getString("eventAddress");
	  			 event_performerVenueLocationMoreInfo = data.getExtras().getString("eventVenueLocation");
	  			 event_performerZipCodeMoreInfo = data.getExtras().getString("eventVenuePostalCode");
	  			 event_performerEventDate = data.getExtras().getString("eventDateString");
	  			 event_performerEventTime = data.getExtras().getString("evantDateTimeLocal");
	  			 event_performerVenueLat = data.getExtras().getString("eventVenueLat");
	  			 event_performerVenueLon = data.getExtras().getString("eventVenueLon");
	  			 
	  			 EventsData eventsData = new EventsData(eventId, eventTitle, eventDateString, evantDateTimeLocal, eventMapStatic, 
	  						eventVenuName, event_performerVenueLocationMoreInfo, event_performerVenueAddressMoreInfo, "", eventPerformerName, 
	  						"", event_performerZipCodeMoreInfo, event_performerVenueLon, event_performerVenueLat);
	  			 
				if (!eventTitle.equals("")) {
					
  					//listposition = Integer.parseInt(listViewPosition);
  					date_time = eventDateString;
  					date_time = date_time.replace("@", "");
  					String date_time_local = evantDateTimeLocal;
  					String venue_name = eventVenuName;
  					String dateStr = date_time+"-"+venue_name;//+"-"+"Denver, Co";
  					subtitleTextView.setText(dateStr);
  					String event_ = eventTitle;
  					titleTextView.setText(event_);
  					
  					date_time = eventDateString;
  					
  					//Add event to favorite  
  					favoriteEventFromTicketPage(eventsData);
					
					imageViewEventInfoFilter.setVisibility(View.VISIBLE);
					imageViewEventInfoFilter.setImageResource(R.drawable.filter_button);
					
					Boolean isInternetPresent = networkInfo.isConnectingToInternet();
					if (isInternetPresent) {
						//AsyncTaskRunner myWebFetch = new AsyncTaskRunner();
					    // myWebFetch.execute(api_url,"tickets");
						 loadTicketData();
					      imageUrl = eventMapStatic;
					     //Image load						       
					     new ImageDownloader().execute(imageUrl);
					}											
				}
	  		  }
	        }
	      }else{
	    	  String callClassName = getIntent().getStringExtra("call_class");
	          if (callClassName.equals("events")) {
	  			Log.d("ArtistTeamEvents", "events");
	        	String eventId =  getIntent().getStringExtra("eventId");
				String eventTitle =  getIntent().getStringExtra("eventTitle");	
				String eventDateString =  getIntent().getStringExtra("eventDateString");	
				String evantDateTimeLocal =  getIntent().getStringExtra("evantDateTimeLocal");	
				String eventVenuName =  getIntent().getStringExtra("eventVenuName");	
				String eventMapStatic =  getIntent().getStringExtra("map");	
				String listViewPosition = getIntent().getStringExtra("listViewPosition");				
				String eventPerformerName =  getIntent().getStringExtra("eventPerformerName");	
				
				Log.d("MainActivity", "showMoreInfoOfCurrentVenue events_performerName:"+events_performerName);	
				
				EventsData eventsData = new EventsData(eventId, eventTitle, eventDateString, evantDateTimeLocal, eventMapStatic, 
							eventVenuName, event_performerVenueLocationMoreInfo, event_performerVenueAddressMoreInfo, "", eventPerformerName, 
							"", event_performerZipCodeMoreInfo, event_performerVenueLon, event_performerVenueLat);
	        	favoriteEventFromTicketPage(eventsData);
	        	
	          }else if (callClassName.equals("venues")){
		  			Log.d("ArtistTeamEvents", "venues");
		        	String eventId =  getIntent().getStringExtra("eventId");
					String eventTitle =  getIntent().getStringExtra("eventTitle");	
					String eventDateString =  getIntent().getStringExtra("eventDateString");	
					String evantDateTimeLocal =  getIntent().getStringExtra("evantDateTimeLocal");	
					String eventVenuName =  getIntent().getStringExtra("eventVenuName");	
					String eventMapStatic =  getIntent().getStringExtra("map");	
					String listViewPosition = getIntent().getStringExtra("listViewPosition");				
					String eventPerformerName =  getIntent().getStringExtra("eventPerformerName");	
					
					Log.d("MainActivity", "showMoreInfoOfCurrentVenue events_performerName:"+events_performerName);					 
					EventsData eventsData = new EventsData(eventId, eventTitle, eventDateString, evantDateTimeLocal, eventMapStatic, 
								eventVenuName, event_performerVenueLocationMoreInfo, event_performerVenueAddressMoreInfo, "", eventPerformerName, 
								"", event_performerZipCodeMoreInfo, event_performerVenueLon, event_performerVenueLat);
		        	favoriteEventFromTicketPage(eventsData);
	          }else if (callClassName.equals("artist_teams")){
	           	     String eventId =  getIntent().getStringExtra("eventId");
	      			 String eventTitle =  getIntent().getStringExtra("eventTitle");	
	      			 String eventDateString =  getIntent().getStringExtra("eventDateString");	
	      			 String evantDateTimeLocal =  getIntent().getStringExtra("evantDateTimeLocal");	
	      			 String eventVenuName =  getIntent().getStringExtra("eventVenuName");	
	      			 String eventMapStatic =  getIntent().getStringExtra("map");	
	      			 String listViewPosition = getIntent().getStringExtra("listViewPosition");
	      			 String eventPerformerName =  getIntent().getStringExtra("eventPerformerName");
	      			 
					 EventsData eventsData = new EventsData(eventId, eventTitle, eventDateString, evantDateTimeLocal, eventMapStatic, 
								eventVenuName, event_performerVenueLocationMoreInfo, event_performerVenueAddressMoreInfo, "", eventPerformerName, 
								"", event_performerZipCodeMoreInfo, event_performerVenueLon, event_performerVenueLat);
		        	favoriteEventFromTicketPage(eventsData); 
	          }
	        }
	    }
	  }
	  
	/**
	 * Create artist and teams list view.
	 * Integrate events data with event list view.
	 */
	//Artist and teams list create
	private void onDataList(){

        List<Row> rows = new ArrayList<Row>();
        int start = 0;
        int end = 0;
        String previousLetter = null;
        Object[] tmpIndexItem = null;
        //Pattern numberPattern = Pattern.compile("[0-9]");
       // for (int i = 0; i < itemAvalible.size(); i++) {
        for (EventsData country : eventsSortArraylist) {
        	 String allOrLocalEvents = country.localEventOrAllEvent;
        	
        	String firstLetter = null;
        	if (allOrLocalEvents.equals("allevents")) {
        		 firstLetter = "All Events";
			}else{
				 firstLetter = "Local Events";
			}
        	 

            // Group numbers together in the scroller
            /*if (numberPattern.matcher(firstLetter).matches()) {
                firstLetter = "#";
            }*/

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
           // Log.d("AsyncTaskRunner", "The date_timeArray_ is: " + date_timeArray_);	
            // Add the events to the list            
            rows.add(new Item(country.eventTitle, country));
            previousLetter = firstLetter;
        }

       /* if (previousLetter != null) {
            // Save the last letter
            tmpIndexItem = new Object[3];
            tmpIndexItem[0] = previousLetter.toUpperCase(Locale.UK);
            tmpIndexItem[1] = start;
            tmpIndexItem[2] = rows.size() - 1;
            alphabet.add(tmpIndexItem);
        }*/
        adapter.setRows(rows);
        listView.setAdapter(adapter);
	}
	/**
	 * Venue list view create.
	 * Integrate venues data with venue list view.
	 */
	//Venues list view create
	private void onVenuesDataList(){
			
		  List<Row_S> rows = new ArrayList<Row_S>();
	        int start = 0;
	        int end = 0;
	        String previousLetter = null;
	        Object[] tmpIndexItem = null;
	        //Pattern numberPattern = Pattern.compile("[0-9]");
	       // for (int i = 0; i < itemAvalible.size(); i++) {
	        for (EventsData country : eventsSortArraylist) {
	        	 String allOrLocalEvents = country.localEventOrAllEvent;
	        	
	        	String firstLetter = null;
	        	if (allOrLocalEvents.equals("allevents")) {
	        		 firstLetter = "All Events";
				}else{
					 firstLetter = "Local Events";
				}
	        	 

	            // Group numbers together in the scroller
	            /*if (numberPattern.matcher(firstLetter).matches()) {
	                firstLetter = "#";
	            }*/

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
	                rows.add(new Section_S(firstLetter));
	                sections.put(firstLetter, start);
	            }
	           // Log.d("AsyncTaskRunner", "The date_timeArray_ is: " + date_timeArray_);	
	            // Add the events to the list            
	            rows.add(new Item_S(country.eventTitle, country));
	            previousLetter = firstLetter;
	        }

	       /* if (previousLetter != null) {
	            // Save the last letter
	            tmpIndexItem = new Object[3];
	            tmpIndexItem[0] = previousLetter.toUpperCase(Locale.UK);
	            tmpIndexItem[1] = start;
	            tmpIndexItem[2] = rows.size() - 1;
	            alphabet.add(tmpIndexItem);
	        }*/
	        venueAllEventsAdapter.setRows(rows);
	        listView.setAdapter(venueAllEventsAdapter);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem menuItem){       	    	
	    return true;
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if (keyCode == KeyEvent.KEYCODE_BACK) {
	        // your code
	    	Log.d("AsyncTaskRunner", "The viewFlipper.getChildCount() is: " + viewFlipper.getChildCount());	
	    	//Log.d("AsyncTaskRunner", "The viewFlipper.getChildCount() is: " + viewFlipper.getDisplayedChild());
	    	if (viewFlipper.getDisplayedChild() == 0) {
				finish();
			}else{
				if (viewFlipper.getDisplayedChild() == 1) {
					controlResumeArtistVenueView = true;
					String callClassName = getIntent().getStringExtra("call_class");
					if (callClassName.equals("events")) {
						finish();
					}else if(callClassName.equals("venues")){
						String venuName = getIntent().getStringExtra("venuName");
						String venuLocation = getIntent().getStringExtra("venuLocation");
						
						titleTextView.setText(venuName);							
						subtitleTextView.setText(venuLocation);	
					}else{
						String name = getIntent().getStringExtra("name");
						titleTextView.setText(name);
						String selectedCity = preferences.getString("selectcity", "");
						subtitleTextView.setText(selectedCity);						
					}	
					imageViewEventInfoFilter.setVisibility(View.INVISIBLE);
					
					customSpinnerLayoutImageView.setOnClickListener(null);
					customSpinnerLayout.setOnClickListener(null);
					//customSpinnerLayoutImageView.setVisibility(View.VISIBLE);
					
					viewFlipper.setInAnimation(AnimationHelper
		                    .inFromLeftAnimation());
					viewFlipper.setOutAnimation(AnimationHelper
		                    .outToRightAnimation());
					viewFlipper.showPrevious();
				}
				if (viewFlipper.getDisplayedChild() == 2) {

					String callClassName = getIntent().getStringExtra("call_class");
					if (callClassName.equals("tickets")){
						finish();
					}else{
						imageViewEventInfoFilter.setVisibility(View.VISIBLE);
						imageViewEventInfoFilter.setImageResource(R.drawable.filter_button);
						
						customSpinnerLayoutImageView.setVisibility(View.VISIBLE);
						customSpinnerLayout.setOnClickListener(ticketSpinnerListener);
						
						viewFlipper.setInAnimation(AnimationHelper
			                    .inFromLeftAnimation());
						viewFlipper.setOutAnimation(AnimationHelper
			                    .outToRightAnimation());
						viewFlipper.showPrevious();
					}
				}

			}	    	
	        return true;
	    }
	    return super.onKeyDown(keyCode, event);
	}
	
	@Override
	public void onBackPressed() {
		 super.onBackPressed();   
	}
	/**
	 * Set the site image.
	 * Create the all tickets list view.
	 * Tickets sorting.
	 */
	//Second view
	private void secondViewAllTickets(){
		//Log.d("AsyncTaskRunner", "The secondViewAllTickets  ticketArrayList is: " + ticketsdetailsArrayList);
		//Tickets filter
		 ticketFilter();
		 String sortCode = preferences.getString("t_Sorting", "price");		 
		 //tickets sorting
		 Log.d("ArtistTeamsFragment", "sortCode:"+sortCode);
		 if (sortCode.equals("price")) {
				Collections.sort(ticketsFilterdetailsArrayList, new Comparator<TicketsData>(){
				      public int compare(TicketsData obj1, TicketsData obj2)
				      {
				            // TODO Auto-generated method stub
				    	   return Integer.compare(obj1.ticketPrice, obj2.ticketPrice);
				           // return obj1.ticketPrice.compareToIgnoreCase(obj2.ticketPrice);
				      }
				});
		 } else if(sortCode.equals("e_ticket")) {
			 ArrayList<TicketsData> tempSortedItem = new ArrayList<TicketsData>();
			 ArrayList<TicketsData> tempUnsortedItem = new ArrayList<TicketsData>();
			 
			 	for (int i = 0; i < ticketsFilterdetailsArrayList.size(); i++) {
			 		TicketsData ticketData = ticketsFilterdetailsArrayList.get(i);
			 		String deliveryMethode1 = ticketData.ticketDelivery_methods;
			 		String[] methode1 = deliveryMethode1.split("[,]");			 		
			 		for (String string : methode1) {
						Log.i("AsyncTaskRunner", "The secondViewAllTickets  string is: " + string);
						if (string.equals("\"email\"")) {
							tempSortedItem.add(ticketData);
						}else{
							tempUnsortedItem.add(ticketData);
						}
					}
			 	}
			 	ticketsFilterdetailsArrayList.clear();
			 	for (TicketsData ticketsData : tempSortedItem) {
			 		ticketsFilterdetailsArrayList.add(ticketsData);
				}
			 	for (TicketsData ticketsData : tempUnsortedItem) {
			 		ticketsFilterdetailsArrayList.add(ticketsData);
				}

/*				Collections.sort(ticketsdetailsArrayList, new Comparator<TicketsData>(){
				      public int compare(TicketsData obj1, TicketsData obj2)
				      {
				            // TODO Auto-generated method stub
				    	  String deliveryMethode1 = obj1.ticketDelivery_methods;
				    	  String deliveryMethode2 = obj2.ticketDelivery_methods;
							String[] methode1 = deliveryMethode1.split("[,]");
							String[] methode2 = deliveryMethode2.split("[,]");
							String fisrtEmail = "\"email\"";
							String secondEmail = "\"email\"";
							String fisrtEmail = "\"email\"";
							String secondEmail = "";
							Log.w("AsyncTaskRunner", "The secondViewAllTickets  fisrtEmail is: " + fisrtEmail);
							Log.w("AsyncTaskRunner", "The secondViewAllTickets  secondEmail is: " + secondEmail);
							for (String string : methode1) {
								Log.e("AsyncTaskRunner", "The secondViewAllTickets  string is: " + string);
								if (string.equals("\"email\"")) {	
									fisrtEmail = string;
								}
							}
							for (String string : methode2) {
								Log.i("AsyncTaskRunner", "The secondViewAllTickets  string is: " + string);
								if (string.equals("\"email\"")) {
									secondEmail = string;
								}
							}
							Log.d("AsyncTaskRunner", "The secondViewAllTickets  fisrtEmail is: " + fisrtEmail);
							Log.d("AsyncTaskRunner", "The secondViewAllTickets  secondEmail is: " + secondEmail);
							int te = fisrtEmail.compareToIgnoreCase(secondEmail);
							Log.d("AsyncTaskRunner", "The secondViewAllTickets  te is: " + te);
				            return fisrtEmail.compareToIgnoreCase(secondEmail);
				      }
				});*/
			 
		 }else if(sortCode.equals("section")) {
				Collections.sort(ticketsFilterdetailsArrayList, new Comparator<TicketsData>(){
				      public int compare(TicketsData obj1, TicketsData obj2)
				      {
				            // TODO Auto-generated method stub
				            return obj1.ticketSection.compareToIgnoreCase(obj2.ticketSection);
				      }
				});
		 }else if(sortCode.equals("row")) {
				Collections.sort(ticketsFilterdetailsArrayList, new Comparator<TicketsData>(){
				      public int compare(TicketsData obj1, TicketsData obj2)
				      {
				            // TODO Auto-generated method stub
				            return obj1.ticketRow.compareToIgnoreCase(obj2.ticketRow);
				      }
				});	
		 }
		 
		Log.d("ArtistTeamsFragment", "ticketsFilterdetailsArrayList.size():"+ticketsFilterdetailsArrayList.size());
		//Log.d("ArtistTeamsFragment", "ticketsFilterdetailsArrayList:"+ticketsFilterdetailsArrayList);
		sortSettingTickets();
		ticketadapter = new TicketListAdapter(this,ticketsFilterdetailsArrayList);
		ticketlistView.setAdapter(ticketadapter);	
		ticketlistView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub	
				viewFlipper.setInAnimation(AnimationHelper
	                    .inFromRightAnimation());
				viewFlipper.setOutAnimation(AnimationHelper
	                    .outToLeftAnimation());				
				viewFlipper.showNext();
				imageViewEventInfoFilter.setImageResource(R.drawable.ticket_notes_button);
				imageViewEventInfoFilter.setVisibility(View.VISIBLE);
				no_ticket = 0;
				
				TicketsData ticketArrayHashMap = ticketsFilterdetailsArrayList.get(arg2);				
				secondtest1.setVisibility(View.INVISIBLE);
				secondHote2.setVisibility(View.INVISIBLE);		 	 
				secondinstant3.setVisibility(View.INVISIBLE);
				secondemai4.setVisibility(View.INVISIBLE);
				thirdViewTicketDetail(ticketArrayHashMap);
			}			
		});
	}
	/**
	 * Filter tickets.
	 */
	private void ticketFilter(){
		//ticketsdetailsArrayList
		//Log.d("AsyncTaskRunner", "The ticketsdetailsArrayList.size() is: " + ticketsdetailsArrayList.size());
		//Log.d("MainActivity", "AllTicketsPriceList:"+allTicketsPriceList);
		  Collections.sort(allTicketsPriceList);
		  //Log.d("MainActivity", "AllTicketsPriceList:"+allTicketsPriceList);
		   int min_price = allTicketsPriceList.get(0);
		   int max_price = allTicketsPriceList.get(allTicketsPriceList.size()-1);
		  //Log.d("MainActivity", "min_price:"+min_price);
		 // Log.d("MainActivity", "max_price:"+max_price);
		  if (min_price < max_price) {
			
		  }else{
			 int temp = min_price;
			  min_price = max_price;
			  max_price = temp;
		  }
		  
		  if (max_price_rang_ == 0) {
			  max_price_rang_ = max_price;
		  }/*else{
			  max_price = max_price_rang_;
		  }*/
		  if (min_price_rang_ == 0) {
			  min_price_rang_ = min_price;
		  }/*else{
			  min_price = min_price_rang_;
		  }*/
		//boolean e_ticket_only = preferences.getBoolean("e_ticket_check", false); 
		//int maxpriceRange = preferences.getInt("maxPriceRange", max_price);	
		//int minpriceRange = preferences.getInt("minPriceRange", min_price);
		//String ticket_available = preferences.getString("ticket_available", "12");
		  //int max_price_rang_ = 0;
		  //int min_price_rang_ = 0;
		Log.d("TicketPage", "if e_ticket_only:"+isChecked_);
		Log.d("TicketPage", "if max_price_rang_:"+max_price_rang_);
		Log.d("TicketPage", "if min_price_rang_:"+min_price_rang_);
		Log.d("TicketPage", "if ticket_available:"+availableTicketsPos);
		//List<TicketsData> ticketsFilterdetailsArrayList = new ArrayList<TicketsData>();
		//List<TicketsData> ticketsPriceFilterdetailsArrayList = new ArrayList<TicketsData>();
		
		ticketsFilterdetailsArrayList.clear();
		for (TicketsData ticketData : ticketsdetailsArrayList) {
			int price = ticketData.ticketPrice;
			//Log.d("TicketPage", "price:"+price);
			if (max_price_rang_ >= price && 
					price >= min_price_rang_) {
				
				//Log.i("TicketPage", "if price:"+price);
				if (availableTicketsPos < 12) {
					String split = ticketData.ticketSplits;
					Log.d("TicketPage", "if splite:"+split);	
					String[] splits = split.split("[,]");
					boolean temp = false;
					for (String string : splits) {
						if (availableTicketsPos <= Integer.parseInt(string)) {
							temp = true;
						}
					 }					
					if (temp == true) {							
						if (isChecked_) {
							String deliveryMethode = ticketData.ticketDelivery_methods;
							String[] methode = deliveryMethode.split("[,]");
							//int pos = methode.
							for (String string : methode) {
								if (string.equals("\"email\"")) {
									ticketsFilterdetailsArrayList.add(ticketData);
								}
							}																				
						}else{
							ticketsFilterdetailsArrayList.add(ticketData);
						}
					}
				 }else{
						if (isChecked_) {
							String deliveryMethode = ticketData.ticketDelivery_methods;
							String[] methode = deliveryMethode.split("[,]");
							//int pos = methode.
							for (String string : methode) {
								if (string.equals("\"email\"")) {
									ticketsFilterdetailsArrayList.add(ticketData);
								}
							}																				
						}else{
							ticketsFilterdetailsArrayList.add(ticketData);
						} 
				 }
			}else{
				
			}
		}
	}
	/**
	 * Get number of tickets .
	 */
	public static int noOfTickets = 0;
	private void sortSettingTickets(){
		//String splits = ticketArrayList.get("splite");
		noOfTickets = 0;
		
		for (TicketsData ticketArrayHashMap : ticketsFilterdetailsArrayList) {
			String splits = ticketArrayHashMap.ticketSplits;//get("splite");
			String[] quantity = splits.split("[,]");
			noOfTickets = noOfTickets+quantity.length;
		}		
		Log.d("AsyncTaskRunner", "The noOfTickets  sortSettingTickets is: " + noOfTickets);
	}
	/**
	 * Get information of each ticket.
	 * @param ticketArrayHashMap
	 */
	//Third view
	private void thirdViewTicketDetail(final TicketsData ticketArrayHashMap){
		//-------------------------below-----------------------------//
		 customSpinnerLayout.setOnClickListener(null);//setOnClickListener(ticketSpinnerListener);
		//Add and remove favorite.
	    final boolean fav_temp = dataBaseHandler.getCurrentIdAvalibleInTicketFavorites(ticketArrayHashMap.ticketId);			   
	    if (fav_temp) {
	    	//thirdTicketAddToFavoritesImageView.setImageResource(R.drawable.favorite_hilighted);
	    	thirdTicketAddToFavoritesToggleButton.setChecked(false);
		}else{
			//thirdTicketAddToFavoritesImageView.setImageResource(R.drawable.favorite_default);
			thirdTicketAddToFavoritesToggleButton.setChecked(true);
		}
		Log.d("AsyncTaskRunner", "The ticketArrayHashMap is: " + ticketArrayHashMap);
		customSpinnerLayoutImageView.setVisibility(View.GONE);
		//TextView subtotal_textView;
		ticketId = ticketArrayHashMap.ticketId;
		String section = ticketArrayHashMap.ticketSection;//get("section");
		String row = ticketArrayHashMap.ticketRow;//get("row");
		final int price = ticketArrayHashMap.ticketPrice;//get("price");
		String splits = ticketArrayHashMap.ticketSplits;//get("splite");
		String type = ticketArrayHashMap.ticketType;//get("type");
		String delivery_methods = ticketArrayHashMap.ticketDelivery_methods;//get("delivery_methods");
		String description = ticketArrayHashMap.ticketDescription;//get("description");
		
		 ticketCheckOut= ticketArrayHashMap.ticketCheckout_url;;
		 ticketReceipt_begins=ticketArrayHashMap.ticketReceipt_begins;
		 
		Log.d("AsyncTaskRunner", "The section is: " + section);
		Log.d("AsyncTaskRunner", "The row is: " + row);
		Log.d("AsyncTaskRunner", "The price is: " + price);
		Log.d("AsyncTaskRunner", "The splits is: " + splits);
		Log.d("AsyncTaskRunner", "The delivery_methods is: " + delivery_methods);
		Log.d("AsyncTaskRunner", "The description is: " + description);
		
        String[] quantity = splits.split("[,]");
              
        String price_ea_ = "Price:$"+price+"/ea";
        String callClassName = getIntent().getStringExtra("call_class");
        String dateTime = "";
        if (callClassName.equals("tickets")) {
        	String ticketTitle = dataBaseHandler.getTicketEvnetTitle(ticketArrayHashMap.ticketId);
        	String ticketSubTitle  = dataBaseHandler.getTicketEvnetDateTime(ticketArrayHashMap.ticketId);
        	titleTextView.setText(ticketTitle);
			subtitleTextView.setText(ticketSubTitle);
			Log.d("AsyncTaskRunner", "tickets The ticketSubTitle is: " + ticketSubTitle);
			String[] date_time_splite_array = ticketSubTitle.split("[-]");
			Log.d("AsyncTaskRunner", "The date_time_splite_array[0] is: " + date_time_splite_array[0]);
			String[] date_time_array = date_time_splite_array[0].split("[ ]");
			String data_ = date_time_array[2];
			if (date_time_array[2].length() < 2) {
				data_ = "0"+date_time_array[2];
			}
			Log.d("AsyncTaskRunner", "The date_time_array.length is: " + date_time_array.length);
			Log.d("AsyncTaskRunner", "The date_time_array[4] is: " + date_time_array[4]);
			dateTime = date_time_array[1]+" "+data_+", "+date_time_array[4];
			
		}else{
	        Log.d("AsyncTaskRunner", "The date_time is: " + date_time);
			String[] date = date_time.split("[ ]");
			Log.d("AsyncTaskRunner", "The date[2] is: " + date[2]);
			int size = date[2].length();
			Log.d("AsyncTaskRunner", "The size is: " + size);
			String data_ = date[2];
			if (size < 2) {
				data_ = "0"+date[2];
			}
			Log.d("AsyncTaskRunner", "The data_ is: " + data_);
			Log.d("AsyncTaskRunner", "The date[4] is: " + date[4]);
			int position = date[4].indexOf("C");
			
			if (position != -1) {
				String[] time_split = date[4].split("[:]");
				size = time_split[0].length();
				Log.d("AsyncTaskRunner", "The size is: " + size);
				Log.d("AsyncTaskRunner", "The time_split[0] is: " + time_split[0]);
				String time_ = time_split[0];
				if (size < 2) {
					time_ = "0"+time_split[0];
				}
				dateTime = date[1]+" "+ data_+", "+time_+":"+time_split[1];
			}else{
				dateTime = date[1]+" "+ data_+", "+date[4];
			}
		}

					
		Log.i("AsyncTaskRunner", "The dateTime is: " + dateTime);
		Log.i("AsyncTaskRunner", "The no_ticket is: " + no_ticket);
		Log.i("AsyncTaskRunner", "The quantity[0] is: " + quantity[0]);
		price_ea.setText(price_ea_);
		event_date_textView.setText(dateTime);
		section_textView.setText(section);
		row_textView.setText(row);
		if (no_ticket == 0) {
			//String quant = quantity[0];
			no_ticket = Integer.parseInt(quantity[0]);
			Log.d("AsyncTaskRunner", "The no_ticket is: " + no_ticket);
			
			quantity_textView.setText(quantity[0]);
			 int subtotal = Integer.parseInt(quantity[0])*price;
			 subtotal_textView.setText("$"+subtotal);
			 String purchesString = "Purchase Your "+quantity[0]+" Tickets";
			 purchesButton.setText(purchesString);
		}else{
			quantity_textView.setText(String.valueOf(no_ticket));
			 int subtotal = no_ticket*price;
			 subtotal_textView.setText("$"+subtotal);
			 String purchesString = "Purchase Your "+no_ticket+" Tickets";
			 purchesButton.setText(purchesString);
		}
		
		
		String purchesString = "Purchase Your "+quantity[0]+" Tickets";
		purchesButton.setText(purchesString);
				
		if (!type.equals("ticket")) {
			ticketNoteTextView.setText(description);
			ticketNoteLayout.setVisibility(View.VISIBLE);
		}else{
			ticketNoteTextView.setText("No tickets notes.");
			ticketNoteLayout.setVisibility(View.INVISIBLE);
		}
		/*if (type.equals("parking")) {
			secondparking.setVisibility(View.VISIBLE);
		}else{
			secondparking.setVisibility(View.INVISIBLE);
		}*/
		String[] delivery_methods_ = delivery_methods.split("[,]");
		 ArrayList<ImageView> delivery_methodsImageViewArrayList = new ArrayList<ImageView>();
			delivery_methodsImageViewArrayList.add(secondemai4);
			delivery_methodsImageViewArrayList.add(secondinstant3);
			delivery_methodsImageViewArrayList.add(secondHote2);
			delivery_methodsImageViewArrayList.add(secondtest1);
		for (int i = 0; i < delivery_methods_.length; i++) {
			String method = delivery_methods_[i];
			Log.d("AsyncTaskRunner", "The method is: " + method);
			if (i < 4 ) {
				ImageView imageView = delivery_methodsImageViewArrayList.get(i);
				//Log.d("AsyncTaskRunner", "The method is: " + method);
				if (method.equals("\"email\"")) {
					imageView.setImageResource(R.drawable.e_ticket_icon);
					imageView.setVisibility(View.VISIBLE);
				}
				if (method.equals("\"instant\"")) {
					imageView.setImageResource(R.drawable.instant_ticket_icon);
					imageView.setVisibility(View.VISIBLE);
				}
				if (method.equals("\"localpickup\"")) {
					imageView.setImageResource(R.drawable.parking_pass_included_icon);
					imageView.setVisibility(View.VISIBLE);
				}
				if (method.equals("\"hotel\"")) {
					imageView.setImageResource(R.drawable.hotel_icon);
					imageView.setVisibility(View.VISIBLE);
				}
				if (method.equals("\"package\"")) {
					imageView.setImageResource(R.drawable.package_icon);
					imageView.setVisibility(View.VISIBLE);
				}
			}	
		}
		 Log.d("AsyncTaskRunner", "The quantity.length is: " + quantity.length);
		 //List<String> list = new ArrayList<String>();
		 final String[] lStrings = new String[quantity.length];
		 for (int i = 0; i < quantity.length; i++) {
			 String stcString = quantity[i];
			 String str = stcString+" tickets";
			 lStrings[i] = str;
		 }
		 changeQtyButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				 no_ticket = ticketPicker(lStrings,price);
			}
		});

		 thirdTicketAddToFavoritesToggleButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// TODO Auto-generated method stub
				if (thirdTicketAddToFavoritesToggleButton.isChecked()) {
					//thirdTicketAddToFavoritesImageView.setImageResource(R.drawable.favorite_default);
			    	//delet from data base
			    	dataBaseHandler.deleteTicketFavorites(ticketArrayHashMap.ticketId);			    			    	
				}else{
					//thirdTicketAddToFavoritesImageView.setImageResource(R.drawable.favorite_hilighted);
					// add to data base
					String eventTitle = titleTextView.getText().toString();
					String eventSubTitle = subtitleTextView.getText().toString();
					dataBaseHandler.addTickets(ticketArrayHashMap,eventTitle,eventSubTitle,imageUrl);					
				}
			}
		});
		 /*thirdTicketAddToFavoritesToggleButton.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if (isChecked) {
					//thirdTicketAddToFavoritesImageView.setImageResource(R.drawable.favorite_default);
			    	//delet from data base
			    	dataBaseHandler.deleteTicketFavorites(ticketArrayHashMap.ticketId);			    			    	
				}else{
					//thirdTicketAddToFavoritesImageView.setImageResource(R.drawable.favorite_hilighted);
					// add to data base
					String eventTitle = titleTextView.getText().toString();
					String eventSubTitle = subtitleTextView.getText().toString();
					dataBaseHandler.addTickets(ticketArrayHashMap,eventTitle,eventSubTitle,imageUrl);					
				}
			}
		});*/

		/* for (String quant_ : quantity) {
			 list.add(quant_);			 
		 }*/
		// Create an ArrayAdapter using the string array and a default spinner layout
		//ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
		        //R.array.planets_array, android.R.layout.simple_spinner_item);
		 /*ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
			        android.R.layout.simple_spinner_item, list);
		// Specify the layout to use when the list of choices appears
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		spinner.setAdapter(adapter);*/
		
	}
	/**
	 * Show the tickets picker dialog.
	 * @param list
	 * @param price
	 * @return
	 */
	private int ticketPicker(final String[] list, final int price){
		
		final Dialog dialog = new Dialog(ArtistTeamsEventList.this);
		dialog.setTitle("Ticket Count");
		dialog.setContentView(R.layout.number_picker_view);
        Button b1_done = (Button) dialog.findViewById(R.id.number_picker_done_button);
        Button b2_back = (Button) dialog.findViewById(R.id.number_picker_back_button);
        final NumberPicker np = (NumberPicker) dialog.findViewById(R.id.number_picker);
       // String ticket_available = preferences.getString("ticket_available", "12");
		 // int avliableTickests = Integer.parseInt(ticket_available);
       // Log.d("ArtistTeamsFragment", "availableTicketsPos:"+availableTicketsPos);
        //availableTicketsPos = availableTicketsPos - 1;
        Log.d("ArtistTeamsFragment", "list.length:"+list.length);
        np.setMaxValue(list.length-1); // max value 12
        np.setMinValue(0);   // min value 0
        //np.setValue(availableTicketsPos-1);   // min value 0
        np.setDescendantFocusability(TimePicker.FOCUS_BLOCK_DESCENDANTS);
        np.setWrapSelectorWheel(false);
        np.setDisplayedValues(list);
        np.setOnValueChangedListener(this);

        b1_done.setOnClickListener(new OnClickListener()
        {
         @Override
         public void onClick(View v) {
            // tv.setText(String.valueOf(np.getValue())); //set the value to textview
       	  Log.d("ArtistTeamsFragment", " String.valueOf(np.getValue()):"+String.valueOf(np.getValue()));
       	  String data =  list[np.getValue()];
       	  String[] ticketQt = data.split("[ ]");
       	   no_ticket  = Integer.valueOf(ticketQt[0]); 
       	   quantity_textView.setText(String.valueOf(no_ticket));
       	   int subtotal = no_ticket*price;
       	   subtotal_textView.setText("$"+subtotal);
       	   String purchesString = "Purchase Your "+no_ticket+" Tickets";
       	   purchesButton.setText(purchesString);
       	   dialog.dismiss();
          }    
         });
        b2_back.setOnClickListener(new OnClickListener()
        {
         @Override
         public void onClick(View v) {
        	 dialog.dismiss(); // dismiss the dialog
          }    
         });
        dialog.show();
		return no_ticket;
	}
	
	 /**
     * Progress Bar
     */
    public void openProgress(){
        progress.setMessage("Wait...");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);	      
        progress.setIndeterminate(true);
        progress.setCanceledOnTouchOutside(false);	        
        progress.show();
    }
    
    /**
     * Pull data by api.
     * Get Events, Venues and tickets data. 
     * Run background thread.
     * @author USER
     *
     */
	private class AsyncTaskRunner extends AsyncTask<String, String, String>{
		
		String AsyncTaskRunnerperformCondition="";
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
			    AsyncTaskRunnerperformCondition = arg0[1];
			    
			    try {
			    	int numberOfDownload_per_page = 1;
	  		    	String contentAsString = "";
			    	int numberOfDownload = 0;
			    	int per_page_download = 1000;
			    	do {
			    		String api_url = arg0[0]+"&per_page="+per_page_download+"&page="+numberOfDownload_per_page;
				        //URL url = new URL("http://mobapi.atbss.com/performers?sort=performer.name.desc");	
			    		Log.d("AsyncTaskRunner", "The api_url is: " + api_url);
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
				        if (AsyncTaskRunnerperformCondition.equals("events")) {	
				        	ArrayList<ArrayList<HashMap<String, String>>>    artistTeams_AlleventArrayList =
				      			  new ArrayList<ArrayList<HashMap<String,String>>>();
				        	artistTeams_AlleventArrayList.clear();
					        artistTeams_AlleventArrayList = JSONParse.artistTeamsAllEvents(contentAsString, per_page_download);
					        //Log.d("AsyncTaskRunner", "The artistTeams_AlleventArrayList is: " + artistTeams_AlleventArrayList);
					       // Log.d("AsyncTaskRunner", "The artistTeams_AlleventArrayList.size is: " + artistTeams_AlleventArrayList.size());
					        eventsSortArraylist.clear();
					        for (int i = 0; i < artistTeams_AlleventArrayList.size(); i++) {
					        	ArrayList<HashMap<String, String>> dataArrayList = artistTeams_AlleventArrayList.get(i);
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
								
								// Log.d("ArtistTeamsFragment", "performerId:"+performerId);
								 //Log.d("ArtistTeamsFragment", "performerName:"+performerName);
								//idList.add(Integer.parseInt(id));
								 /*Log.d("ArtistTeamsFragment", "event_id:"+event_id);
								 Log.d("ArtistTeamsFragment", "event_title:"+event_title);
								 Log.d("ArtistTeamsFragment", "event_data_string:"+event_data_string);
								 Log.d("ArtistTeamsFragment", "event_datetime_local:"+event_datetime_local);
								 Log.d("ArtistTeamsFragment", "event_map_static:"+event_map_static);
								 Log.d("ArtistTeamsFragment", "event_venue_name:"+event_venue_name);
								 Log.i("ArtistTeamsFragment", "event_venue_location:"+event_venue_location);*/
								 
								eventsSortArraylist.add(new EventsData(event_id, event_title, event_data_string, 
										event_datetime_local, event_map_static, event_venue_name, event_venue_location,
										event_venue_address,"allevents",performerName,performerId,
										event_venue_postal_code,event_venue_lon,event_venue_lat));
							}
				        
				        }else if(AsyncTaskRunnerperformCondition.equals("tickets")){
				        	//Log.d("TicketPage", "contentAsString:"+contentAsString);
				        	ArrayList<HashMap<String, String>> ticketArrayList = 
				      			  new ArrayList<HashMap<String, String>>();
							ticketArrayList.clear();
							ticketArrayList = JSONParse.ticketsDetailParse(contentAsString);
							//ticketsdetailsArrayList
							Log.d("TicketPage", "ticketArrayList:"+ticketArrayList);
							ticketsdetailsArrayList.clear();
							allTicketsPriceList.clear();	
							//Log.d("TicketPage", "ticketArrayList.size():"+ticketArrayList.size());
							for (int i = 0; i < ticketArrayList.size(); i++) {
								HashMap<String, String> hashMap = ticketArrayList.get(i);
								
								String id = hashMap.get("id");
								String title = hashMap.get("title");
								String section = hashMap.get("section");
								String row = hashMap.get("row");
								int price = Integer.parseInt(hashMap.get("price"));
								String marked = hashMap.get("marked");
								String description = hashMap.get("description");
								String type = hashMap.get("type");
								String splite = hashMap.get("splite");
								String delivery_methods = hashMap.get("delivery_methods");	
								
								String checkout_url = hashMap.get("checkout_url");
								String receipt_begins = hashMap.get("receipt_begins");
								//Log.d("TicketPage", "delivery_methods:"+delivery_methods);
								
								ticketsdetailsArrayList.add(new TicketsData(id, title, section, row, price,
										marked, description , splite, delivery_methods, type,checkout_url,receipt_begins));
								allTicketsPriceList.add(price);
							}
							//Log.d("TicketPage", "ticketsdetailsArrayList.size():"+ticketsdetailsArrayList.size());
						}
			    		  numberOfDownload_per_page++;
					} while (numberOfDownload >= numberOfDownload_per_page);

			      

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
			  if (AsyncTaskRunnerperformCondition.equals("events")) {	
				  String callClassName = getIntent().getStringExtra("call_class");
				  if (callClassName.equals("artist_teams")) {
					  onDataList();
					  //dataParseForPriviousEventList();
				  }else if (callClassName.equals("venues")) {
					  onVenuesDataList();
				  }
			  }else if(AsyncTaskRunnerperformCondition.equals("tickets")){
				  //sortSettingTickets();
				  if (allTicketsPriceList.size() != 0) {
					  secondViewAllTickets();
				  }else{
					  ticketsFilterdetailsArrayList.clear();
					  ticketadapter = new TicketListAdapter(ArtistTeamsEventList.this,ticketsFilterdetailsArrayList);
					  ticketadapter.notifyDataSetChanged();					 
				  }
			 }			  
		  }
	   }
	

	/**
	 * Download image.
	 * Run background thread.
	 * @author USER
	 *
	 */
	
	  private class ImageDownloader extends AsyncTask<String, String , String> {
		  	Bitmap bitmap = null;
			@Override
			protected String doInBackground(String... params) {
				// TODO Auto-generated method stub
				bitmap = downloadBitmap(params[0]);
				return null;
			}
 
	        @Override
	        protected void onPreExecute() {
	            Log.i("Async-Example", "onPreExecute Called");
	            //simpleWaitDialog = ProgressDialog.show(ImageDownladerActivity.this,
	                   // "Wait", "Downloading Image");	 
	        }
	        @Override
	        protected void onPostExecute(String result) {
	            Log.i("Async-Example", "onPostExecute Called");
	            //downloadedImg.setImageBitmap(result);	 
	            //setingRowImageView; sittingRowImageView_second;
			    if (bitmap!=null) {
			    	setingRowImageView.setImageBitmap(bitmap);
			    	sittingRowImageView_second.setImageBitmap(bitmap);
				}else{
					setingRowImageView.setBackgroundResource(R.drawable.ic_launcher);
					sittingRowImageView_second.setBackgroundResource(R.drawable.ic_launcher);
				}
	        }
	        private Bitmap downloadBitmap(String url) {
	            // initilize the default HTTP client object	            	 
	            //forming a HttoGet request
	        	 Log.i("ImageDownloader", "url:" +url );
	        	 url = url.replace(" ", "%20");
	            try {
	            	DefaultHttpClient client = new DefaultHttpClient();
	            	HttpGet getRequest = new HttpGet(url);
	                HttpResponse response = client.execute(getRequest);
	 
	                //check 200 OK for success
	                final int statusCode = response.getStatusLine().getStatusCode();
	 
	                if (statusCode != HttpStatus.SC_OK) {
	                    Log.w("ImageDownloader", "Error :" + statusCode +
	                            " while retrieving bitmap from " + url);
	                    return null;
	 
	                }
	 
	                final HttpEntity entity = response.getEntity();
	                if (entity != null) {
	                    InputStream inputStream = null;
	                    try {
	                        // getting contents from the stream
	                        inputStream = entity.getContent();
	 
	                        // decoding stream data back into image Bitmap that android understands
	                        final Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
	 
	                        return bitmap;
	                    } finally {
	                        if (inputStream != null) {
	                            inputStream.close();
	                        }
	                        entity.consumeContent();
	                    }
	                }
	            } catch (Exception e) {
	                // You Could provide a more explicit error message for IOException
	                //getRequest.abort();
	                Log.e("ImageDownloader", "Something went wrong while" +
	                        " retrieving bitmap from " + url + e.toString());
	            }	 
	            return null;
	        }
	    }
	  
	  /**
	   * PopupWindow variable.
	   */
	    Point p;
	    PopupWindow popup;
	  @Override
	    public void onWindowFocusChanged(boolean hasFocus) {
	     
	       int[] location = new int[2];
	       //Button button = (Button) findViewById(R.id.show_popup);
	     
	       // Get the x, y location and store it in the location[] array
	       // location[0] = x, location[1] = y.
	       imageViewEventInfoFilter.getLocationOnScreen(location);
	       customSpinnerLayoutImageView.getLocationOnScreen(location);
	       //Initialize the Point with x, and y positions
	       p = new Point();
	       p.x = location[0];
	       p.y = location[1];
	    }
	  
	  /**
	   * Show more information of events.
	   * Get direction, and list of performers.
	   * @param context
	   * @param p
	   */
	   boolean perdormerOpenTemp = false;
	   TextView seeTheDirection, eventInfoPagePop;
	   //boolean directonViewControl = false;
	   boolean eventInfoViewControl = false;
	   int numberOfPerformer = 0;
	   private void showMoreInfoOfCurrentVenue(final FragmentActivity context, Point p){
	    	
			RelativeLayout linearLauout = (RelativeLayout)findViewById(R.id.relativeLayout1arist_all_event);

	   		  Log.d("Log", "Height: " + "+linearLauout.getHeight:"+linearLauout.getHeight());
	   		// We need to get the instance of the LayoutInflater
	   		 Log.d("MainActivity", "Error in creating p:"+p);
	   		 LinearLayout viewGroup = (LinearLayout) context.findViewById(R.id.popup);
	   		 LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	   		 View layout = inflater.inflate(R.layout.ticket_listing_more_menu_pop, viewGroup);
	   		 popup = new PopupWindow(layout, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
	   		 popup.setBackgroundDrawable(new BitmapDrawable());
	   		 popup.showAtLocation(layout, Gravity.LEFT|Gravity.TOP, 0, linearLauout.getHeight()+15);
	   		 
	   		 eventInfoPagePop = (TextView)layout.findViewById(R.id.retun_to_event_page_textView1);
	   		 seeTheDirection = (TextView)layout.findViewById(R.id.see_the_direction_textView2);
	   		 TextView returnToVenuePage = (TextView)layout.findViewById(R.id.return_to_venue_page_textView3);
	   		 RelativeLayout performerNameList = (RelativeLayout)layout.findViewById(R.id.list_of_porformer_textView3);
	   		 final TextView morePerformerInfoTextView4 = (TextView)layout.findViewById(R.id.moreperformerinfo_textView4); 
	   		 ImageView morePerformerImageView = (ImageView)layout.findViewById(R.id.more_performer_imageView1);	   		 	   		 	   			   		
	   		 final ListView performerListView = (ListView)layout.findViewById(R.id.performer_listview_listView1);
	   		 	   					   			   		 
			   		seeTheDirection.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
						 // TODO Auto-generated method stub
							popup.dismiss();
							Log.d("ArtstTeams", "event_performerVenueLat:"+event_performerVenueLat);
							Log.d("ArtstTeams", "event_performerVenueLon:"+event_performerVenueLon);
							//current_latitude,current_longitude
							double current_latitude = 0;
							double current_longitude = 0;
							 GPSTracker  gps = new GPSTracker(ArtistTeamsEventList.this);

					         // check if GPS enabled     
					         if(gps.canGetLocation()){			        	 
					        	 current_latitude = gps.getLatitude();
					        	 current_longitude = gps.getLongitude();
					             // \n is for new line
					             //Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + current_latitude + "\nLong: " + current_longitude, Toast.LENGTH_LONG).show();    
					         }else{
					             // can't get location
					             // GPS or Network is not enabled
					             // Ask user to enable GPS/network in settings
					             gps.showSettingsAlert();
					         }
							if (current_latitude != 0 && current_longitude != 0) {
								String uri = "http://maps.google.com/maps?saddr="+current_latitude+","+current_longitude+
										"&daddr="+event_performerVenueLat+","+event_performerVenueLon;
								Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(uri));
								intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");		   			    
				   			    startActivity(intent);
							}else{
								String uri = "http://maps.google.com/maps?daddr="+event_performerVenueLat+","+event_performerVenueLon;//&daddr="+event_performerVenueLon+",45.345";
								Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(uri));
								intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");		   			    
				   			    startActivity(intent);
							}
						}
					 });
			   		
			   		returnToVenuePage.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
						 // TODO Auto-generated method stub							
							popup.dismiss();
							controlResumeLoading = false;
							Intent i = new Intent(ArtistTeamsEventList.this, MorePerformerDetailList.class);
							 i.putExtra("performer_name", event_performerVenueTitleMoreInfo);
							 i.putExtra("venu_name", "venue");
							 startActivityForResult(i, REQUEST_CODE);
						}
					 });
	   		 if (eventInfoViewControl) {

			 }else{
			   		
				 eventInfoPagePop.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
						 // TODO Auto-generated method stub
							getEventInfoView();
							popup.dismiss();
							eventInfoPagePop.setClickable(eventInfoViewControl);
							eventInfoViewControl = true;
						}
					 });
			 }
	   		 Log.d("MainActivity", "showMoreInfoOfCurrentVenue events_performerName:"+events_performerName);
	   		
	   		 if (!events_performerName.equals("")) {
				String[] performerArray = events_performerName.split("[,]");
				//String[] performerArray = events_performerName.split("[@]");
				//String[] performerArray = {"hari","tapan","avick","goolu"};
				Log.d("Log", "performerArray.length:"+performerArray.length);
				numberOfPerformer = performerArray.length;
				if (performerArray.length > 1) {					
					ArrayAdapter<String> performAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, 
							performerArray){
						 @Override
					        public View getView(int position, View convertView,
					                ViewGroup parent) {
					            View view =super.getView(position, convertView, parent);

					            TextView textView=(TextView) view.findViewById(android.R.id.text1);

					            /*YOUR CHOICE OF COLOR*/
					            textView.setTextColor(Color.WHITE);

					            return view;
					        }				
					};
					performerListView.setAdapter(performAdapter);
				}else{
					if (performerArray.length == 1) {
						morePerformerInfoTextView4.setText(performerArray[0]);
						morePerformerImageView.setVisibility(View.INVISIBLE);	
					}
				}
	   		 }	
	   		 
	   		perdormerOpenTemp = false;
	   		performerListView.setVisibility(View.GONE);
	   		performerNameList.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (numberOfPerformer > 1) {
						if (perdormerOpenTemp) {
							perdormerOpenTemp = false;
							performerListView.setVisibility(View.GONE);
						}else{
							perdormerOpenTemp = true;
							performerListView.setVisibility(View.VISIBLE);
						}
					}else if (numberOfPerformer == 1) {
						//go to performer page.
						popup.dismiss();
						
						String callClassName = getIntent().getStringExtra("call_class");
						if (callClassName.equals("events")){
							controlResumeLoading = false;
							String performerName = morePerformerInfoTextView4.getText().toString();
							Intent i = new Intent(ArtistTeamsEventList.this, MorePerformerDetailList.class);
						    i.putExtra("performer_name", performerName);
						    i.putExtra("venu_name", "");
						    startActivityForResult(i, REQUEST_CODE);	
						}else if (callClassName.equals("venues")){
							controlResumeLoading = false;			          
				        	String performerName = morePerformerInfoTextView4.getText().toString();
							Log.d("Log", "performerName:"+performerName);
							Intent i = new Intent(ArtistTeamsEventList.this, MorePerformerDetailList.class);
						    i.putExtra("performer_name", performerName);
						    i.putExtra("venu_name", "");
						    startActivityForResult(i, REQUEST_CODE);
						}else{
							backToPreviouView();	
						}									    
					}					
				}
			});
	   		
	   		performerListView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					// TODO Auto-generated method stub
					popup.dismiss();
					
					String callClassName = getIntent().getStringExtra("call_class");
					if (callClassName.equals("events")){
						controlResumeLoading = false;
			            TextView textView=(TextView) arg1.findViewById(android.R.id.text1);
						String performerName = textView.getText().toString();
						Log.d("Log", "performerName:"+performerName);
						Intent i = new Intent(ArtistTeamsEventList.this, MorePerformerDetailList.class);
					    i.putExtra("performer_name", performerName);
					    i.putExtra("venu_name", "");
					    startActivityForResult(i, REQUEST_CODE);
					}else if (callClassName.equals("venues")){
						controlResumeLoading = false;
			            TextView textView=(TextView) arg1.findViewById(android.R.id.text1);
						String performerName = textView.getText().toString();
						Log.d("Log", "performerName:"+performerName);
						Intent i = new Intent(ArtistTeamsEventList.this, MorePerformerDetailList.class);
					    i.putExtra("performer_name", performerName);
					    i.putExtra("venu_name", "");
					    startActivityForResult(i, REQUEST_CODE);
					}else{
						backToPreviouView();	
					}

				}
			});
	    }
	   
	  	// The method that displays the popup of filter page.
	    int max_price_rang_ = 0;
	    int min_price_rang_ = 0;
	    boolean isChecked_ =  false;
	    int min_price;
	    int max_price;
	    /**
	     * Tickets filter setting pop.
	     * @param context
	     * @param p
	     */
		private void showPopup(final FragmentActivity context, Point p) {
			RelativeLayout linearLauout = (RelativeLayout)findViewById(R.id.relativeLayout1arist_all_event);

	   		  Log.d("Log", "Height: " + "+linearLauout.getHeight:"+linearLauout.getHeight());
	   		// We need to get the instance of the LayoutInflater
	   		 Log.d("MainActivity", "Error in creating p:"+p);
	   		 LinearLayout viewGroup = (LinearLayout) context.findViewById(R.id.popup);
	   		 LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	   		 View layout = inflater.inflate(R.layout.tickets_filter_dialog, viewGroup);
	   		 popup = new PopupWindow(layout, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT-100, true);
	   		 popup.setBackgroundDrawable(new BitmapDrawable());
	   		 popup.showAtLocation(layout, Gravity.LEFT|Gravity.TOP, 0, linearLauout.getHeight()+15);
		   
		 	//  if (condition.equals("ticketfilter")) {
				 
				  int noT= ArtistTeamsEventList.noOfTickets;
				  final ViewFlipper viewFlipper = (ViewFlipper)layout.findViewById(R.id.ticketFilterViewFliper);
				  //First View
				  ImageView close_textview = (ImageView)layout.findViewById(R.id.event_info_close_textView2);	
				  LinearLayout ticketCountLinearLayout = (LinearLayout)layout.findViewById(R.id.select_ticket_count);
				  final ImageView email_setting = (ImageView) layout.findViewById(R.id.email_setting_imageView1);
				  email_setting.setImageResource(R.drawable.e_ticket_icon_selected);
				  final ImageView instant_setting = (ImageView) layout.findViewById(R.id.instant_setting_imageView2);
				  final ImageView hotal_setting = (ImageView) layout.findViewById(R.id.hotal_setting_imageView3);
				  final ImageView docter_setting = (ImageView) layout.findViewById(R.id.docter_setting_imageView4);
				  final ImageView parking_setting = (ImageView) layout.findViewById(R.id.parking_setting_imageView5);
				  final TextView messagetextview = (TextView)layout.findViewById(R.id.message_ticket_textView11);
				  ticketsNumberShowTextView1 = (TextView)layout.findViewById(R.id.tickets_number_show_textView1);
				  Button ticket_apply_filter_button =(Button)layout.findViewById(R.id.ticket_apply_filter_button);
				  
				 // String ticket_available = preferences.getString("ticket_available", "12");
				  //int avliableTickests = Integer.parseInt(ticket_available);
				  if (availableTicketsPos < 12) {
					  ticketsNumberShowTextView1.setText(String.valueOf(availableTicketsPos));
				  }else{
					  ticketsNumberShowTextView1.setText("Any"); 
				  }
							  
				  //Feature development 
				  //create RangeSeekBar as Integer range between min price  and max				 
				  //allTicketsPriceList		
				  Log.d("MainActivity", "AllTicketsPriceList:"+allTicketsPriceList);
				  Collections.sort(allTicketsPriceList);
				  Log.d("MainActivity", "AllTicketsPriceList:"+allTicketsPriceList);
				  min_price = allTicketsPriceList.get(0);
				  max_price = allTicketsPriceList.get(allTicketsPriceList.size()-1);
				  Log.d("MainActivity", "min_price:"+min_price);
				  Log.d("MainActivity", "max_price:"+max_price);
				  final TextView seekBarMaxPriceValue = (TextView)layout.findViewById(R.id.ticket_max_price_textView1);
				  final TextView seekBarMinPriceValue = (TextView)layout.findViewById(R.id.ticket_min_price_textView1);
				  if (min_price < max_price) {
					
				  }else{
					 int temp = min_price;
					  min_price = max_price;
					  max_price = temp;
				  }
				  
				  if (max_price_rang_ == 0) {
					  max_price_rang_ = max_price;
				  }
				  if (min_price_rang_ == 0) {
					  min_price_rang_ = min_price;
				  }
				  Log.i("ArtistteamsEventslist", "User selected new range values: max_price_rang_:="+max_price_rang_);
				  Log.i("ArtistteamsEventslist", "User selected new range values: min_price_rang_:="+min_price_rang_);
				  if (max_price_rang_ < max_price) {
					  //Log.d("ArtistteamsEventslist", "User selected new range values: max_price_rang_:="+max_price_rang_);
					  seekBarMaxPriceValue.setText("$"+String.valueOf(max_price_rang_));
				  }else{
					  seekBarMaxPriceValue.setText("Max"); 
				  }
				  if (min_price_rang_ > min_price) {
					  //Log.d("ArtistteamsEventslist", "User selected new range values: min_price_rang_:="+min_price_rang_);
					  seekBarMinPriceValue.setText("$"+String.valueOf(min_price_rang_));
				  }else{
					  seekBarMinPriceValue.setText("Min"); 
				  }
				  Log.i("ArtistteamsEventslist", "User selected new range values: MIN="+min_price);
				  Log.i("ArtistteamsEventslist", "User selected new range values: MAX="+max_price);
				  
				  
				  RangeSeekBar<Integer> seekBar = new RangeSeekBar<Integer>(min_price, max_price, context);
				  seekBar.setSelectedMaxValue(max_price_rang_);
				  seekBar.setSelectedMinValue(min_price_rang_);
				  seekBar.setOnRangeSeekBarChangeListener(new OnRangeSeekBarChangeListener<Integer>() {
				          @Override
				          public void onRangeSeekBarValuesChanged(RangeSeekBar<?> bar, Integer minValue, Integer maxValue) {
				                  // handle changed range values
				                  Log.i("ArtistteamsEventslist", "User selected new range values: MIN=" + minValue + ", " +
				                  		"MAX=" + maxValue);
				                  Log.d("ArtistteamsEventslist", "User selected new range values: min_price=" + min_price + ", " +
					                  		"max_price=" + max_price);
				                   if (maxValue >= min_price && maxValue < max_price) {
				                	    seekBarMaxPriceValue.setText("$"+String.valueOf(maxValue));
				                	    max_price_rang_ = maxValue;
									}else{
										max_price_rang_ = maxValue;
										seekBarMaxPriceValue.setText("Max"); 
									}
				                   if (minValue > min_price  && minValue <= max_price) {
				                	   seekBarMinPriceValue.setText("$"+String.valueOf(minValue)); 
				                	   min_price_rang_ = minValue;
									}else{
										min_price_rang_ = minValue;
										seekBarMinPriceValue.setText("Min"); 
									}
				          }
				  });

				  // add RangeSeekBar to pre-defined layout
				  ViewGroup layout_layout = (ViewGroup)layout.findViewById(R.id.ticket_price_range_linearLayout);
				  layout_layout.addView(seekBar);

				  final CheckBox e_ticket_check = (CheckBox)layout.findViewById(R.id.e_ticket_checkBox1);
				  //boolean e_ticket_check_ = preferences.getBoolean("e_ticket_check", false);
				  if (isChecked_) {
					  e_ticket_check.setChecked(isChecked_);
					  e_ticket_check.setButtonDrawable(R.drawable.checkbox_select_bg);
				  }else{
					  e_ticket_check.setButtonDrawable(R.drawable.checkbox_unselect_bg);  
				  }
				 
				  e_ticket_check.setOnCheckedChangeListener(new OnCheckedChangeListener() {
						
						@Override
						public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
							// TODO Auto-generated method stub
							Log.d("ArtistTeamsFragment", " conserts isChecked:"+isChecked);	
							if (isChecked){
				                // Checked
								e_ticket_check.setChecked(isChecked);
								e_ticket_check.setButtonDrawable(R.drawable.checkbox_select_bg);
							} else{
				                //Unchecked
								e_ticket_check.setChecked(isChecked);
								e_ticket_check.setButtonDrawable(R.drawable.checkbox_unselect_bg);
							}
							isChecked_ = isChecked;						
						}
					  });
				  
				  ticket_apply_filter_button.setOnClickListener(new View.OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							// String max_price_rang_ = "4";
	                	    //String min_price_rang_ = "4";
							//SharedPreferences.Editor editor = preferences.edit();
							//editor.putInt("maxPriceRange", max_price_rang_);
							//editor.putInt("minPriceRange", min_price_rang_);							
				    		//editor.putBoolean("e_ticket_check",isChecked_);				    		
				    		//editor.putString("ticket_available",String.valueOf(availableTicketsPos));
				    		//editor.commit();				    		
				    		popup.dismiss();
				    		
							 //String ticket_available = preferences.getString("ticket_available", "12");
							 // int avliableTickests = Integer.parseInt(ticket_available);
							  if (availableTicketsPos < 12) {								  
								  filterTicketsListNameTextView.setText("Showing Tickets: "+availableTicketsPos+" or more tickets");
							  }else{
								  filterTicketsListNameTextView.setText("Showing Tickets: All Tickets"); 
							  }
							  
							  secondViewAllTickets();						 		    		
						}
					   });
				 // final NumberPicker picker = new NumberPicker(this);
				  //final NumberPicker np;
				  ticketCountLinearLayout.setOnClickListener(new View.OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							numberPickerView();													
						}
					   });
				  
				  	  email_setting.setOnClickListener(new View.OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							messagetextview.setText(getResources().getString(R.string.emailticketMess));
							
							email_setting.setImageResource(R.drawable.e_ticket_icon_selected);
							instant_setting.setImageResource(R.drawable.instant_ticket_icon);
							hotal_setting.setImageResource(R.drawable.hotel_icon);
							docter_setting.setImageResource(R.drawable.package_icon);
							parking_setting.setImageResource(R.drawable.parking_pass_included_icon);
						}
					   });
					  instant_setting.setOnClickListener(new View.OnClickListener() {
							
							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								messagetextview.setText(getResources().getString(R.string.instantticketMess));
								
								email_setting.setImageResource(R.drawable.e_ticket_icon);
								instant_setting.setImageResource(R.drawable.instant_ticket_icon_selected);
								hotal_setting.setImageResource(R.drawable.hotel_icon);
								docter_setting.setImageResource(R.drawable.package_icon);
								parking_setting.setImageResource(R.drawable.parking_pass_included_icon);
							}
						});
					  hotal_setting.setOnClickListener(new View.OnClickListener() {
							
							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								messagetextview.setText(getResources().getString(R.string.validticketMess));
								
								email_setting.setImageResource(R.drawable.e_ticket_icon);
								instant_setting.setImageResource(R.drawable.instant_ticket_icon);
								hotal_setting.setImageResource(R.drawable.hotel_icon_selected);
								docter_setting.setImageResource(R.drawable.package_icon);
								parking_setting.setImageResource(R.drawable.parking_pass_included_icon);
							}
						});
					  docter_setting.setOnClickListener(new View.OnClickListener() {
							
							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								messagetextview.setText(getResources().getString(R.string.foodticketMess));
								
								email_setting.setImageResource(R.drawable.e_ticket_icon);
								instant_setting.setImageResource(R.drawable.instant_ticket_icon);
								hotal_setting.setImageResource(R.drawable.hotel_icon);
								docter_setting.setImageResource(R.drawable.package_icon_selected);
								parking_setting.setImageResource(R.drawable.parking_pass_included_icon);
							}
						});
					  parking_setting.setOnClickListener(new View.OnClickListener() {
							
							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								messagetextview.setText(getResources().getString(R.string.parkingticketMess));						
								email_setting.setImageResource(R.drawable.e_ticket_icon);
								instant_setting.setImageResource(R.drawable.instant_ticket_icon);
								hotal_setting.setImageResource(R.drawable.hotel_icon);
								docter_setting.setImageResource(R.drawable.package_icon);
								parking_setting.setImageResource(R.drawable.parking_pass_included_icon_selected);
							}
						});
					  close_textview.setOnClickListener(new View.OnClickListener() {
							
							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								popup.dismiss();
							}
						});
			  //}
		}
		/**
		 * Set the tickets current sorting tab.
		 * Tickets can sort by  price , e-ticket, section, and row.
		 */
		//Ticket sort by price , e-ticket, section, row in second view 
		private void ticketShortInSecondView(){
			  String sortCode = preferences.getString("t_Sorting", "price");	
			  Log.d("Log", "Height: " + "+sortCode:"+sortCode);
			  if (sortCode.equals("price")) {//R.color.orange_tab
				    price_ImageView.setBackgroundColor(getResources().getColor(R.color.selected_bar_tab_orange_tab));
					e_ticket_ImageView.setBackgroundColor(Color.TRANSPARENT);
					section_ImageView.setBackgroundColor(Color.TRANSPARENT);
					row_ImageView.setBackgroundColor(Color.TRANSPARENT);				
			  }
			
			  if (sortCode.equals("e_ticket")) {
				    price_ImageView.setBackgroundColor(Color.TRANSPARENT);
					e_ticket_ImageView.setBackgroundColor(getResources().getColor(R.color.selected_bar_tab_orange_tab));
					section_ImageView.setBackgroundColor(Color.TRANSPARENT);
					row_ImageView.setBackgroundColor(Color.TRANSPARENT);							
			  }
			 
			  if (sortCode.equals("section")) {
				    price_ImageView.setBackgroundColor(Color.TRANSPARENT);
					e_ticket_ImageView.setBackgroundColor(Color.TRANSPARENT);
					section_ImageView.setBackgroundColor(getResources().getColor(R.color.selected_bar_tab_orange_tab));
					row_ImageView.setBackgroundColor(Color.TRANSPARENT);						
			  }
			  if (sortCode.equals("row")) {
				  	price_ImageView.setBackgroundColor(Color.TRANSPARENT);
					e_ticket_ImageView.setBackgroundColor(Color.TRANSPARENT);
					section_ImageView.setBackgroundColor(Color.TRANSPARENT);
					row_ImageView.setBackgroundColor(getResources().getColor(R.color.selected_bar_tab_orange_tab));						
			  }
		}
		/**
		 * Tickets filter by  selected ticket and more.
		 * Ticket pickers dialog.
		 */
		//Number picker dialog
		static int availableTicketsPos = 13;
		TextView ticketsNumberShowTextView1 ;
		private void numberPickerView(){
			final Dialog d = new Dialog(ArtistTeamsEventList.this);
	         d.setTitle("Ticket Count");
	         d.setContentView(R.layout.number_picker_view);
	         Button b1_done = (Button) d.findViewById(R.id.number_picker_done_button);
	         Button b2_back = (Button) d.findViewById(R.id.number_picker_back_button);
	         final NumberPicker np = (NumberPicker) d.findViewById(R.id.number_picker);
	        // String ticket_available = preferences.getString("ticket_available", "12");
			 // int avliableTickests = Integer.parseInt(ticket_available);
	         Log.d("ArtistTeamsFragment", "availableTicketsPos:"+availableTicketsPos);
	         //availableTicketsPos = availableTicketsPos - 1;
	         Log.d("ArtistTeamsFragment", "availableTicketsPos:"+availableTicketsPos);
	         np.setMaxValue(12); // max value 12
	         np.setMinValue(0);   // min value 0
	         np.setValue(availableTicketsPos-1);   // min value 0
	         np.setDescendantFocusability(TimePicker.FOCUS_BLOCK_DESCENDANTS);
	         np.setWrapSelectorWheel(false);
	         np.setDisplayedValues( new String[] { "1 tickets", "2 tickets", "3 tickets", "4 tickets", "5 tickets", "6 tickets"
	        		 , "7 tickets", "8 tickets", "9 tickets", "10 tickets", "11 tickets", "12 tickets", "Any"});
	         np.setOnValueChangedListener(this);
	        /* np.setOnValueChangedListener(new OnValueChangeListener() {
				
				@Override
				public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
					// TODO Auto-generated method stub
					
				}
			});*///setOnValueChangedListener(this);
	         b1_done.setOnClickListener(new OnClickListener()
	         {
	          @Override
	          public void onClick(View v) {
	             // tv.setText(String.valueOf(np.getValue())); //set the value to textview
	        	  Log.d("ArtistTeamsFragment", " String.valueOf(np.getValue()):"+String.valueOf(np.getValue()));
	        	  availableTicketsPos = np.getValue();
	        	  if (availableTicketsPos<12) {
	        		  availableTicketsPos = availableTicketsPos + 1;
	        		  ticketsNumberShowTextView1.setText(String.valueOf(availableTicketsPos));
				  }else{
					  ticketsNumberShowTextView1.setText("Any");
				  }
	              d.dismiss();
	           }    
	          });
	         b2_back.setOnClickListener(new OnClickListener()
	         {
	          @Override
	          public void onClick(View v) {
	              d.dismiss(); // dismiss the dialog
	           }    
	          });
	       d.show();
		}

		@Override
		public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
			// TODO Auto-generated method stub
			
		}
		
		/**
		 * Show the direction view page.
		 * We can add to contact list and reminder list.
		 * We can see the venue position in map.
		 * And we can also move on map page to watch the direction(route).
		 */
		private void getEventInfoView(){
			 directionViewPopUp.setVisibility(View.VISIBLE);
			 
			 /*directionViewPopUp.removeAllViews();
	   		 LinearLayout viewGroup = (LinearLayout) this.findViewById(R.id.popup);
	   		 LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	   		 View layout = inflater.inflate(R.layout.direction_view, viewGroup);
	   		 directionViewPopUp.addView(layout);*/
	   		 TextView performer_name = (TextView)findViewById(R.id.performer_name_event_direction_info_textView1);
	   		
	   		 TextView performer_Address = (TextView)findViewById(R.id.performer_address_textView2);
	   		 TextView performer_Date_Time = (TextView)findViewById(R.id.performer_date_time_textView2);
	   		 
	   		 ImageView addToContacts = (ImageView)findViewById(R.id.add_to_contact_imageView2);
	   		 ImageView addToRemider = (ImageView)findViewById(R.id.add_to_remider_imageView2);
	   		
	   		 Button goToMapView = (Button)findViewById(R.id.get_direction_in_map_button1);
	   		

				String[] dateAndTime = event_performerEventDate.split("[@]");
				String[] data = dateAndTime[0].split("[ ]");
				String day = data[0].replace(" ", "");			
				       day = day.toUpperCase();
				String month = data[1].replace(" ", ""); 
					   month = month.toUpperCase();
			    String day_no_ = data[2].replace(" ", "");		   
			    String time = 	dateAndTime[1].replace(" ", "");
			    String[] date_local = event_performerEventTime.split("[T]");
			    String[] dateNumber = date_local[0].split("[-]");
			    
			    String yyyy =  dateNumber[0]; 
			    int mm =  Integer.parseInt(dateNumber[1]);  
			   // int dd =  Integer.parseInt(dateNumber[2]); 
			    String day_day = "";
			    int weekendFlag = 0;
			    if (day.equals("MON")) {
			    	day_day = getResources().getString(R.string.MON);
			    	weekendFlag = 0;
				}else if (day.equals("TUE")) {
					day_day = getResources().getString(R.string.TUE);
					weekendFlag = 0;
				}else if (day.equals("WED")) {
					day_day = getResources().getString(R.string.WED);
					weekendFlag = 0;
				}else if (day.equals("THU")) {
					day_day = getResources().getString(R.string.THU);
					weekendFlag = 0;
				}else if (day.equals("FRI")) {
					day_day = getResources().getString(R.string.FRI);
					weekendFlag = 1;
				}else if (day.equals("SAT")) {
					day_day = getResources().getString(R.string.SAT);
					weekendFlag = 1;
				}else if (day.equals("SUN")) {
					day_day = getResources().getString(R.string.SUN);
					weekendFlag = 1;
				}
			    		    		    
			    String[] monthArray = getResources().getStringArray(R.array.months_array);
			    String month_month = monthArray[mm-1];		    
			    final String completDateAndTime = day_day+" "+month_month+" "+day_no_+", "+yyyy+"-"+time;
			    
			    final String address = event_performerVenueTitleMoreInfo+" "+
			    event_performerVenueAddressMoreInfo+" "+event_performerVenueLocationMoreInfo+" "+event_performerZipCodeMoreInfo;
			    
			    performer_name.setText(events_performerName);
			    performer_Address.setText(address);
			    performer_Date_Time.setText(completDateAndTime);
			    
			   /*// WebView cropWebView = (WebView)layout.findViewById(R.id.crop_map_view_webView1);	   		
		   		cropWebView.getSettings().setLoadsImagesAutomatically(true);
		   		cropWebView.getSettings().setJavaScriptEnabled(true);
		   		cropWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);	
		   		//String mapUrl = "http://maps.google.com/?ll=event_performerVenueLat,event_performerVenueLon";//q={address}";
		   		//String mapUrl = "http://maps.google.com/maps/api/staticmap?center="+
		   		 //event_performerVenueLat+","+event_performerVenueLon + "&zoom=6&markers=color:blue%7Clabel:S%7C&size=200x200&maptype=roadmap&" +
		   				//"&sensor=false";
		   		String address_ = address.replace(" ", "+");
		   		//zoom 17		   		
		   		Log.d("ArtstTeams", "address_:"+address_);
		   		String mapUrl = "http://maps.google.com/maps/api/staticmap?center="+//address_+"&"+
		   				event_performerVenueLat+","+event_performerVenueLon+
		   				"&zoom=13&markers=color:red%7Clabel:s%7C"+event_performerVenueLat+
		   				","+event_performerVenueLon+"&size=200x100&maptype=roadmap&&sensor=true";  
		   		
		   		Log.d("ArtstTeams", "mapUrl:"+mapUrl);	   		
		   		cropWebView.loadUrl(mapUrl);*/
		   		
		   		
		   		int result = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
		        Log.e("Maps", "Result int value::" + result);
		        switch (result) {
		        case ConnectionResult.SUCCESS:
		            Log.e("Maps", "RESULT:: SUCCESS");          
		            break;

		        case ConnectionResult.DEVELOPER_ERROR:
		            Log.e("Maps", "RESULT:: DE");           
		            break;

		        case ConnectionResult.INTERNAL_ERROR:
		            Log.e("Maps", "RESULT:: IE");           
		            break;

		        case ConnectionResult.INVALID_ACCOUNT:
		            Log.e("Maps", "RESULT:: IA");           
		            break;

		        case ConnectionResult.NETWORK_ERROR:
		            Log.e("Maps", "RESULT:: NE");           
		            break;

		        case ConnectionResult.RESOLUTION_REQUIRED:
		            Log.e("Maps", "RESULT:: RR");           
		            break;

		        case ConnectionResult.SERVICE_DISABLED:
		            Log.e("Maps", "RESULT:: SD");           
		            break;

		        case ConnectionResult.SERVICE_INVALID:
		            Log.e("Maps", "RESULT:: SI");           
		            break;

		        case ConnectionResult.SERVICE_MISSING:
		            Log.e("Maps", "RESULT:: SM");           
		            break;
		        case ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED:
		            Log.e("Maps", "RESULT:: SVUR");         
		            break;
		        case ConnectionResult.SIGN_IN_REQUIRED:
		            Log.e("Maps", "RESULT:: SIR");          
		            break;      

		        default:
		            break;
		        }
		       
		        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);		       
		        // Enabling MyLocation in Google Map
	            mGoogleMap.setMyLocationEnabled(true);
	            mGoogleMap.getUiSettings().setZoomControlsEnabled(false);
	            mGoogleMap.getUiSettings().setCompassEnabled(false);
	            mGoogleMap.getUiSettings().setMyLocationButtonEnabled(false);
	            mGoogleMap.getUiSettings().setAllGesturesEnabled(true);
	           //mGoogleMap.setTrafficEnabled(true);
	            mGoogleMap.getUiSettings().setScrollGesturesEnabled(true);
	           // mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(12));
	            Double venueLat = Double.parseDouble(event_performerVenueLat);
	            Double venueLon = Double.parseDouble(event_performerVenueLon);
	            Log.e("Maps", "RESULT:: venueLat:"+venueLat); 
	            Log.e("Maps", "RESULT:: venueLon:"+venueLon); 
	           //mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(2), 2000, null);
	            LatLng toPosition = new LatLng(venueLat, venueLon);
	            // create marker
                final Marker storeMarker = mGoogleMap.addMarker(new MarkerOptions()
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))               
                .position(toPosition));
                
                //Set camera position
                CameraPosition cameraPosition = CameraPosition.builder()
                        .target(toPosition)
                        .zoom(9)                         
                        .tilt(20)    // Sets the tilt of the camera to 30 degrees
                        .build();
//      	        // Animate the change in camera view over 2 seconds
      	        mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition),
                        2000, null);
      	         	      
		   		//http://maps.googleapis.com/maps/api/staticmap?center=40.714728,-73.998672&zoom=12&size=400x400&sensor=false
		   		//http://maps.googleapis.com/maps/api/staticmap?center=40.714728,-73.998672&zoom=14&size=400x400&sensor=false
		   		//https://developers.google.com/maps/documentation/staticmaps/?csw=1
	       moreInfocloseView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					directionViewPopUp.setVisibility(View.INVISIBLE);
					eventInfoPagePop.setClickable(eventInfoViewControl);
					eventInfoViewControl = false;
					mGoogleMap.clear();	
					storeMarker.remove();
					
				}
			});
	   		addToRemider.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(ArtistTeamsEventList.this, ReminderActivity.class);
					intent.putExtra("address", address);
					intent.putExtra("date_time", completDateAndTime);
			        startActivity(intent);
				}
			});
	   		addToContacts.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					String contactPersonName = titleTextView.getText().toString();
					//addToContact(event_performerVenueTitleMoreInfo, address);
					addToContact(contactPersonName, address);
				}
			});
	   		goToMapView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					//String uri = String.format(Locale.ENGLISH, "geo:%f,%f", Float.valueOf(event_performerVenueLat), 
							//Float.valueOf(event_performerVenueLon));
					Log.d("ArtstTeams", "event_performerVenueLat:"+event_performerVenueLat);
					Log.d("ArtstTeams", "event_performerVenueLon:"+event_performerVenueLon);
					//current_latitude,current_longitude
					double current_latitude = 0;
					double current_longitude = 0;
					 GPSTracker  gps = new GPSTracker(ArtistTeamsEventList.this);

			         // check if GPS enabled     
			         if(gps.canGetLocation()){			        	 
			        	 current_latitude = gps.getLatitude();
			        	 current_longitude = gps.getLongitude();
			             // \n is for new line
			             //Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + current_latitude + "\nLong: " + current_longitude, Toast.LENGTH_LONG).show();    
			         }else{
			             // can't get location
			             // GPS or Network is not enabled
			             // Ask user to enable GPS/network in settings
			             gps.showSettingsAlert();
			         }
					if (current_latitude != 0 && current_longitude != 0) {
						String uri = "http://maps.google.com/maps?saddr="+current_latitude+","+current_longitude+
								"&daddr="+event_performerVenueLat+","+event_performerVenueLon;
						Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(uri));
						intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");		   			    
		   			    startActivity(intent);
					}else{
						String uri = "http://maps.google.com/maps?daddr="+event_performerVenueLat+","+event_performerVenueLon;//&daddr="+event_performerVenueLon+",45.345";
						Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(uri));
						intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");		   			    
		   			    startActivity(intent);
					}
					
					 //Uri.parse("http://maps.google.com/maps?saddr=20.344,34.34&daddr=20.5666,45.345"));
	   			     //"https://maps.google.com/maps?q=Salt+Lake+Stadium,+JB+Block,+Salt+Lake+City,+Kolkata,
	   			     //+West+Bengal,+India&hl=en&ll=22.569495,88.408978&spn=0.012067,
	   			     //0.021136&sll=37.0625,-95.677068&sspn=42.224734,86.572266&oq=saltlake+kolka&t=m&z=16"
	   			    
	   			    /* String geoCode = "geo:0,0?q=" + event_performerVenueLat + ","
	   	                    + event_performerVenueLon ;
	   	             Intent sendLocationToMap = new Intent(Intent.ACTION_VIEW,
	   	                  Uri.parse(geoCode));
	   	             startActivity(sendLocationToMap);*/	   			       			 
				}
			});
		}
		
		/**
		 * Add to contact list.
		 */
		private void addToContact(String eventname, String address){
			Log.d("ArtstTeams", "eventname:"+eventname);
			Log.d("ArtstTeams", "address:"+address);
			 ArrayList<ContentProviderOperation> op_list = new ArrayList<ContentProviderOperation>(); 
			    op_list.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI) 
			            .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null) 
			            .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null) 
			            .withValue(RawContacts.AGGREGATION_MODE, RawContacts.AGGREGATION_MODE_DEFAULT) 
			            .build());

			    // first and last names 
			    op_list.add(ContentProviderOperation.newInsert(Data.CONTENT_URI) 
			            .withValueBackReference(Data.RAW_CONTACT_ID, 0) 
			            .withValue(Data.MIMETYPE, StructuredName.CONTENT_ITEM_TYPE) 
			            .withValue(StructuredName.GIVEN_NAME, eventname) 
			            //.withValue(StructuredName.FAMILY_NAME, "First Name") 
			            .build()); 

			   /* op_list.add(ContentProviderOperation.newInsert(Data.CONTENT_URI) 
			            .withValueBackReference(Data.RAW_CONTACT_ID, 0) 
			            .withValue(ContactsContract.Data.MIMETYPE,ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
			            .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, "09876543210")
			            .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, Phone.TYPE_HOME)
			            .build());
			    op_list.add(ContentProviderOperation.newInsert(Data.CONTENT_URI) 
			            .withValueBackReference(Data.RAW_CONTACT_ID, 0)

			            .withValue(ContactsContract.Data.MIMETYPE,ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)
			            .withValue(ContactsContract.CommonDataKinds.Email.DATA, "abc@xyz.com")
			            .withValue(ContactsContract.CommonDataKinds.Email.TYPE, Email.TYPE_WORK)
			            .build());*/
			    
			    op_list.add(ContentProviderOperation.newInsert(Data.CONTENT_URI) 
			            .withValueBackReference(Data.RAW_CONTACT_ID, 0)
			            .withValue(ContactsContract.Data.MIMETYPE,ContactsContract.CommonDataKinds.SipAddress.CONTENT_ITEM_TYPE)//Email.CONTENT_ITEM_TYPE
			            .withValue(ContactsContract.CommonDataKinds.SipAddress.SIP_ADDRESS, address)
			            .withValue(ContactsContract.CommonDataKinds.SipAddress.TYPE, SipAddress.TYPE_WORK)
			            .build());

			    try{ 
			    	Log.d("ArtstTeams", "ContentProviderResult:");
			        ContentProviderResult[] results = getContentResolver().applyBatch(ContactsContract.AUTHORITY, op_list); 
			        if (results.length!=0) {
			        	Toast.makeText(ArtistTeamsEventList.this, "Add to contact.", Toast.LENGTH_LONG).show();
					}else{
						Toast.makeText(ArtistTeamsEventList.this, "Contact not add.", Toast.LENGTH_LONG).show();
					}

			        Log.d("ArtstTeams", "results.length:"+results.length);
			        Log.d("ArtstTeams", "results[0]:"+results[0]);
			        Log.d("ArtstTeams", "results[1]:"+results[1]);
			        //Log.d("ArtstTeams", "results[0]:"+results[0]);
			    }catch(Exception e){ 
			        e.printStackTrace(); 
			    }			 
		}
		
 		 /**
 		  * Share to social network site.
 		  */
 		 private void onShareSocialNetwork(){
 			RelativeLayout linearLauout = (RelativeLayout)findViewById(R.id.relativeLayout1arist_all_event);

	  		 Log.d("Log", "Height: " + "+linearLauout.getHeight:"+linearLauout.getHeight());
	  		// We need to get the instance of the LayoutInflater
	  		// Log.d("MainActivity", "Error in creating p:"+p);
	  		 LinearLayout viewGroup = (LinearLayout) this.findViewById(R.id.popup);
	  		 LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	  		 View layout = inflater.inflate(R.layout.share_view_layout, viewGroup);
	  		 popup = new PopupWindow(layout, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
	  		 popup.setBackgroundDrawable(new BitmapDrawable());
	  		 popup.showAtLocation(layout, Gravity.RIGHT|Gravity.TOP, 0, linearLauout.getHeight()+15);
	  		 
	  		 LinearLayout facebookShare = (LinearLayout)layout.findViewById(R.id.facebook_share_);
	  		 LinearLayout twitterShare = (LinearLayout)layout.findViewById(R.id.twitter_share_);
	  		 LinearLayout mailShare = (LinearLayout)layout.findViewById(R.id.mail_share_);
	  		 LinearLayout messageShare = (LinearLayout)layout.findViewById(R.id.message_share_);
	  		 ConnectionDetector  networkInfo = new ConnectionDetector(ArtistTeamsEventList.this);
	  	     final boolean isInternetPresent =networkInfo.isConnectingToInternet();
	  	    
	  	    //viewFlipper
	  		facebookShare.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (isInternetPresent) {
						 //Intent	intent = new Intent(MainActivity.this, ShareActivity.class);
						String shareMessage = getResources().getString(R.string.share_message);
						String playStoreLink = getResources().getString(R.string.play_store_link);
						String webSiteLink = getResources().getString(R.string.web_site_link);
						String webLogoShare = getResources().getString(R.string.web_logo_share);
						
						 Intent	intent = new Intent(ArtistTeamsEventList.this, FacebookShareActivity.class);
						 intent.putExtra("shareMessage", shareMessage);
						 intent.putExtra("playStoreLink", playStoreLink);
						 intent.putExtra("webSiteLink", webSiteLink);
						 intent.putExtra("webLogoShare", webLogoShare);
					     //Intent	intent = new Intent(MainActivity.this, MailActivity.class);
				         startActivity(intent);
					}else{
						Toast.makeText(ArtistTeamsEventList.this, getResources().getString(R.string.network_issue), Toast.LENGTH_LONG).show();
					}

					popup.dismiss();
				}
			});
	  		twitterShare.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
				
					if (isInternetPresent) {
						TwitterShareActivity  tweet = new TwitterShareActivity(ArtistTeamsEventList.this);
						String shareMessage = getResources().getString(R.string.share_message);
						String playStoreLink = getResources().getString(R.string.play_store_link);
						String webSiteLink = getResources().getString(R.string.web_site_link);
						String webLogoShare = getResources().getString(R.string.web_logo_share);
						 tweet.onTweet(shareMessage+"\n"+playStoreLink+"\n"+webSiteLink+"\n " +webLogoShare);
						 popup.dismiss();
					}else{
						Toast.makeText(ArtistTeamsEventList.this, getResources().getString(R.string.network_issue), Toast.LENGTH_LONG).show();
					}
										
				}
			});
	  		mailShare.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					String shareMessage = getResources().getString(R.string.share_message);
					String playStoreLink = getResources().getString(R.string.play_store_link);
					String webSiteLink = getResources().getString(R.string.web_site_link);
					String webLogoShare = getResources().getString(R.string.web_logo_share);
					
					SpannableStringBuilder builder = new SpannableStringBuilder();
				    builder.append(shareMessage);
				    builder.append("\n");
				    builder.append(playStoreLink);
				    builder.append("\n");
				    builder.append(webSiteLink);
				    builder.append("\n");
				    builder.append(webLogoShare);
				    
		            Intent emailIntent = new Intent( Intent.ACTION_VIEW);
		            emailIntent.setClassName("com.google.android.gm", "com.google.android.gm.ComposeActivityGmail");		            
		            emailIntent.putExtra(Intent.EXTRA_TEXT, builder);
				    emailIntent.putExtra(Intent.EXTRA_SUBJECT, "FYE Tickets");
				    emailIntent.setType("message/rfc822");
		            startActivity(emailIntent );
					 popup.dismiss();
				}
			});
	  		messageShare.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					String shareMessage = getResources().getString(R.string.share_message);
					String playStoreLink = getResources().getString(R.string.play_store_link);
					String webSiteLink = getResources().getString(R.string.web_site_link);
					StringBuilder message = new StringBuilder();
					message.append(shareMessage);
					message.append("\n");
					message.append(playStoreLink);
					message.append("\n");
					message.append(webSiteLink);
					
		            Intent intentsms = new Intent( Intent.ACTION_VIEW, Uri.parse( "sms:" + "" ) );
		            intentsms.setType("vnd.android-dir/mms-sms");
		            intentsms.putExtra( "sms_body", message.toString());
		            startActivity( intentsms );
		            popup.dismiss();
				}
			});	  		 
 		 }

}

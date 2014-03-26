package com.dds.fye.database;

import java.util.ArrayList;

import com.dds.fye.tickets.EventsData;
import com.dds.fye.tickets.TicketsData;
import com.dds.fye.tickets.VenueData;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHandler extends SQLiteOpenHelper{
	// All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;
 
    // Database Name
    private static final String DATABASE_NAME = "eventsFavoritesManager";
 
    // Favorites table name
    private static final String TABLE_EVENTS_DETAIL = "eventsFavoritesDetail";
    private static final String TABLE_TICKET_DETAIL = "ticketsFavoritesDetail";
    
    // Contacts Table Columns names
    private static final String KEY_EVENTS_ID = "eventId";
    private static final String KEY_EVENTS_TITALE = "eventTitle";
    private static final String KEY_EVENTS_DATE_STRING = "eventDateString";    
    private static final String KEY_EVENTS_DATE_TIME_LOCAL = "eventDateTimeLocal";
    private static final String KEY_EVENTS_MAP_STATIC = "eventMapStatic";
    private static final String KEY_EVENTS_VENUE_NAME = "eventVenueName";   
    private static final String KEY_EVENTS_VENUE_LOCATION = "eventVenueLocation";
    private static final String KEY_EVENTS_VENUE_ADDRESS = "localEventAddress";
    private static final String KEY_EVENTS_VENUE_ZIP_CODE = "eventVenueZipCode";
    private static final String KEY_EVENTS_VENUE_LONGITUDE = "eventVenueLon";
    private static final String KEY_EVENTS_VENUE_LATITUDE = "eventVenueLat";    
    private static final String KEY_EVENTS_PERFORMER_NAME = "eventPerformerName";
    private static final String KEY_EVENTS_PERFORMER_ID = "eventsPerformerId";    
    private static final String KEY_EVENTS_FILED_TYPE = "eventsFiledType";
    
    //Ticket Table Columns names
    private static final  String KEY_TICKET_EVENT_TITLE = "ticketEventTitle";
    private static final  String KEY_TICKET_EVENT_DATE_TIME = "ticketDateTime";
    private static final  String KEY_TICKET_EVENT_MAP_URL = "ticketMapUrl";
    
    private static final  String KEY_TICKET_ID = "ticketId";
    private static final String KEY_TICKET_TITLE = "ticketTitle";	        
    private static final String KEY_TICKET_SECTION = "ticketSection";
    private static final String KEY_TICKET_ROW = "ticketRow";
    private static final String KEY_TICKET_PRICE = "ticketPrice";
    private static final String KEY_TICKET_MARKED = "ticketMarked";
    private static final String KEY_TICKET_DESCRIPTION = "ticketDescription";
    private static final String KEY_TICKET_SPLITS = "ticketSplits";
    private static final String KEY_TICKET_DELIVERY_METHODS = "ticketDelivery_methods";
    private static final String KEY_TICKET_TYPE = "ticketType";
    private static final String KEY_TICKET_CHECKOUT_URL = "ticketCheckout_url";
    private static final String KEY_TICKET_RECEIPT_BEGINS = "ticketReceipt_begins";
    
    
	public DatabaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_EVENTS_DETAIL + "("+
                KEY_EVENTS_ID + " TEXT," +
                KEY_EVENTS_FILED_TYPE + " TEXT,"+
				KEY_EVENTS_TITALE + " TEXT,"+
                KEY_EVENTS_DATE_STRING + " TEXT," +
				KEY_EVENTS_DATE_TIME_LOCAL + " TEXT," + 
                KEY_EVENTS_MAP_STATIC + " TEXT," + 
				KEY_EVENTS_VENUE_NAME + " TEXT,"+ 
                KEY_EVENTS_VENUE_LOCATION + " TEXT," +
				KEY_EVENTS_VENUE_ADDRESS +  " TEXT,"+
                KEY_EVENTS_VENUE_ZIP_CODE + " TEXT,"+ 
				KEY_EVENTS_VENUE_LONGITUDE + " TEXT,"+ 
                KEY_EVENTS_VENUE_LATITUDE + " TEXT,"+ 
                KEY_EVENTS_PERFORMER_NAME + " TEXT,"+ 
                KEY_EVENTS_PERFORMER_ID + " TEXT"+")";
		
		String CREATE_TICKET_TABLE = "CREATE TABLE " + TABLE_TICKET_DETAIL + "("+
				KEY_TICKET_EVENT_TITLE+ " TEXT," +
				KEY_TICKET_EVENT_DATE_TIME+ " TEXT," +
				KEY_TICKET_EVENT_MAP_URL+ " TEXT," +
				KEY_TICKET_ID + " TEXT," +
				KEY_TICKET_TITLE + " TEXT,"+
				KEY_TICKET_SECTION + " TEXT,"+
				KEY_TICKET_ROW + " TEXT," +
				KEY_TICKET_PRICE + " INTEGER," + 
				KEY_TICKET_MARKED + " TEXT," + 
				KEY_TICKET_DESCRIPTION + " TEXT,"+ 
				KEY_TICKET_SPLITS + " TEXT," +
				KEY_TICKET_DELIVERY_METHODS +  " TEXT,"+
				KEY_TICKET_TYPE + " TEXT,"+ 
				KEY_TICKET_CHECKOUT_URL + " TEXT,"+
				KEY_TICKET_RECEIPT_BEGINS + " TEXT"+")";
        db.execSQL(CREATE_CONTACTS_TABLE);
        db.execSQL(CREATE_TICKET_TABLE);
	}

	// Upgrading database
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		// Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EVENTS_DETAIL);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TICKET_DETAIL);
        // Create tables again
        onCreate(db);
	}
   public void onUpgradDatabase(){
	// Drop older table if existed
	   SQLiteDatabase db = this.getWritableDatabase();
       db.execSQL("DROP TABLE IF EXISTS " + TABLE_EVENTS_DETAIL);
       db.execSQL("DROP TABLE IF EXISTS " + TABLE_TICKET_DETAIL);
       // Create tables again
       onCreate(db);
   }
    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */
 
     // Adding new event to favorites
	public void addContact(EventsData data, String eventType) {
    	
        SQLiteDatabase db = this.getWritableDatabase();
 
        ContentValues values = new ContentValues();
        
        values.put(KEY_EVENTS_ID, data.eventId); // Contact Name
        values.put(KEY_EVENTS_TITALE, data.eventTitle); // Contact Phone
        values.put(KEY_EVENTS_DATE_STRING, data.eventDateString); // Contact Name
        values.put(KEY_EVENTS_DATE_TIME_LOCAL, data.eventDateTimeLocal); // Contact Phone
        values.put(KEY_EVENTS_MAP_STATIC, data.eventMapStatic); // Contact Name
        values.put(KEY_EVENTS_VENUE_NAME, data.eventVenueName); // Contact Phone
        values.put(KEY_EVENTS_VENUE_LOCATION, data.eventVenueLocation); // Contact Name
        values.put(KEY_EVENTS_VENUE_ADDRESS, data.localEventAddress); // Contact Phone
        values.put(KEY_EVENTS_VENUE_ZIP_CODE, data.eventVenueZipCode); // Contact Name
        values.put(KEY_EVENTS_VENUE_LONGITUDE, data.eventVenueLon); // Contact Phone
        values.put(KEY_EVENTS_VENUE_LATITUDE, data.eventVenueLat); // Contact Name
        values.put(KEY_EVENTS_PERFORMER_NAME, data.eventPerformerName); // Contact Phone
        values.put(KEY_EVENTS_PERFORMER_ID, data.eventsPerformerId); // Contact Phone
        values.put(KEY_EVENTS_FILED_TYPE, eventType); // Contact Phone
        //Log.d("database", "values:"+values);
        // Inserting Row
        long key = db.insert(TABLE_EVENTS_DETAIL, null, values);
        Log.d("database", "key:"+key);
        db.close(); // Closing database connection
    }
	
    // Adding new venue to favorites
	public void addContactVaneu(VenueData data, String eventType) {
   	
       SQLiteDatabase db = this.getWritableDatabase();

       ContentValues values = new ContentValues();
      /* new VenueData(VenueEventId,VenueEventTitle,VenueId, VenueName, VenueLocation,VenueCity, VenueState,
				 VenuePostalCode, VenueCountry,VenueAddress,VenueLat,VenueLong,VenuePerformerName_,VenuePerformerId_)*/
       
       values.put(KEY_EVENTS_ID, data.VenueEventId); // Contact Name
       values.put(KEY_EVENTS_TITALE, data.VenueEventTitle); // Contact Phone
       values.put(KEY_EVENTS_VENUE_NAME, data.VenueName); // Contact Phone
       values.put(KEY_EVENTS_VENUE_LOCATION, data.VenueLocation); // Contact Name
       values.put(KEY_EVENTS_VENUE_ADDRESS, data.VenueAddress); // Contact Phone
       values.put(KEY_EVENTS_VENUE_ZIP_CODE, data.VenuePostalCode); // Contact Name
       values.put(KEY_EVENTS_VENUE_LONGITUDE, data.VenueLong); // Contact Phone
       values.put(KEY_EVENTS_VENUE_LATITUDE, data.VenueLat); // Contact Name
       values.put(KEY_EVENTS_PERFORMER_NAME, data.VenuePerformerName); // Contact Phone
       values.put(KEY_EVENTS_PERFORMER_ID, data.VenuePerformerId); // Contact Phone
       values.put(KEY_EVENTS_FILED_TYPE, eventType); // Contact Phone
       //Log.d("database", "values:"+values);
       // Inserting Row
       long key = db.insert(TABLE_EVENTS_DETAIL, null, values);
       Log.d("database", "key:"+key);
       db.close(); // Closing database connection
   }
	
	//Add new ticket to favorites
	public void addTickets(TicketsData data,String ticketEventTitle,String dateTime,String imageUrl) {
	   	
	       SQLiteDatabase db = this.getWritableDatabase();

	       ContentValues values = new ContentValues();
	       
	       values.put(KEY_TICKET_EVENT_TITLE, ticketEventTitle);
	       values.put(KEY_TICKET_EVENT_DATE_TIME, dateTime);
	       values.put(KEY_TICKET_EVENT_MAP_URL, imageUrl);
	       
	       values.put(KEY_TICKET_ID, data.ticketId);
	       values.put(KEY_TICKET_TITLE, data.ticketTitle); 
	       values.put(KEY_TICKET_SECTION, data.ticketSection); 
	       values.put(KEY_TICKET_ROW, data.ticketRow); 
	       values.put(KEY_TICKET_PRICE, data.ticketPrice); 
	       values.put(KEY_TICKET_MARKED, data.ticketMarked); 
	       values.put(KEY_TICKET_DESCRIPTION, data.ticketDescription); 
	       values.put(KEY_TICKET_SPLITS, data.ticketSplits); 
	       values.put(KEY_TICKET_DELIVERY_METHODS, data.ticketDelivery_methods); 
	       values.put(KEY_TICKET_TYPE, data.ticketType);	       
	       values.put(KEY_TICKET_CHECKOUT_URL, data.ticketCheckout_url);
	       values.put(KEY_TICKET_RECEIPT_BEGINS, data.ticketReceipt_begins);

	       // Inserting Row
	       long key = db.insert(TABLE_TICKET_DETAIL, null, values);
	       Log.d("database", "key:"+key);
	       db.close(); // Closing database connection
	   }
	
    //Available check in event table.
    public boolean getCurrentIdAvalibleInFavorites(String eventsid, String type){
    	boolean fav_avalible= false;
    	 String selectQuery = "SELECT  * FROM " + TABLE_EVENTS_DETAIL+
         		" WHERE "+ KEY_EVENTS_ID+"='"+eventsid+"' AND "+KEY_EVENTS_FILED_TYPE+"='"+type+"'";
    	 SQLiteDatabase db = this.getWritableDatabase();
         Cursor cursor = db.rawQuery(selectQuery, null);
         if (cursor != null ) {				
			 int size = cursor.getCount();
			 if (size > 0) {
				 fav_avalible = true;
			 }
			 
		 }
         db.close();
		return fav_avalible;   	
    }
    
    //Available check in ticket table.
    public boolean getCurrentIdAvalibleInTicketFavorites(String ticketid){
    	boolean fav_avalible= false;
    	 String selectQuery = "SELECT  * FROM " + TABLE_TICKET_DETAIL+
         		" WHERE "+ KEY_TICKET_ID+"='"+ticketid+"'";
    	 SQLiteDatabase db = this.getWritableDatabase();
         Cursor cursor = db.rawQuery(selectQuery, null);
         if (cursor != null ) {				
			 int size = cursor.getCount();
			 if (size > 0) {
				 fav_avalible = true;
			 }
			 
		 }
         db.close();
		return fav_avalible;   	
    }
    
	// Deleting single favorites
	public void deleteFavorites(String eventsid, String type) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_EVENTS_DETAIL, KEY_EVENTS_ID+"='"+eventsid+"' AND "+KEY_EVENTS_FILED_TYPE+"='"+ type +"'",null);
		
		//db.delete(TABLE_EVENTS_DETAIL, KEY_EVENTS_ID + " = ?",
				//new String[] { eventsid });
		db.close();
	}
	
    // Getting All events
    public ArrayList<EventsData> getAllFavorites(String type) {
    	ArrayList<EventsData> fevoritesList = new ArrayList<EventsData>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_EVENTS_DETAIL+
        		" WHERE "+KEY_EVENTS_FILED_TYPE+"='"+type+"'";
 
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
 
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
				String eventId = cursor.getString(cursor.getColumnIndex(KEY_EVENTS_ID));
				String eventTitle = cursor.getString(cursor.getColumnIndex(KEY_EVENTS_TITALE));
				String eventDateString = cursor.getString(cursor.getColumnIndex(KEY_EVENTS_DATE_STRING));
				String eventDateTimeLocal = cursor.getString(cursor.getColumnIndex(KEY_EVENTS_DATE_TIME_LOCAL));				
				String eventMapStatic = cursor.getString(cursor.getColumnIndex(KEY_EVENTS_MAP_STATIC));
				String eventVenueName = cursor.getString(cursor.getColumnIndex(KEY_EVENTS_VENUE_NAME));
				String eventVenueLocation = cursor.getString(cursor.getColumnIndex(KEY_EVENTS_VENUE_LOCATION));				
				String localEventAddress = cursor.getString(cursor.getColumnIndex(KEY_EVENTS_VENUE_ADDRESS));
				String eventVenuePostalCode = cursor.getString(cursor.getColumnIndex(KEY_EVENTS_VENUE_ZIP_CODE));

				String eventVenueLat = cursor.getString(cursor.getColumnIndex(KEY_EVENTS_VENUE_LATITUDE));
				String eventVenueLon = cursor.getString(cursor.getColumnIndex(KEY_EVENTS_VENUE_LONGITUDE));
				

				String eventVenuePerformerName = cursor.getString(cursor.getColumnIndex(KEY_EVENTS_PERFORMER_NAME));
				String eventVenuePerformerId = cursor.getString(cursor.getColumnIndex(KEY_EVENTS_PERFORMER_ID));

				fevoritesList.add(new EventsData(eventId, eventTitle, eventDateString, 
						eventDateTimeLocal, eventMapStatic, eventVenueName, eventVenueLocation,
						localEventAddress, "", eventVenuePerformerName, eventVenuePerformerId,
						eventVenuePostalCode, eventVenueLon, eventVenueLat));
				
            } while (cursor.moveToNext());
        }
        db.close();
        // return contact list
        return fevoritesList;
    }
    
 // Deleting single tickets favorites
 	public void deleteTicketFavorites(String ticketid) {
 		SQLiteDatabase db = this.getWritableDatabase();
 		db.delete(TABLE_TICKET_DETAIL, KEY_TICKET_ID+"='"+ticketid+"'",null);
 		db.close();
 	}
    //Getting all ticket
    public ArrayList<TicketsData> getTickets(){
    	ArrayList<TicketsData> fevoritesList = new ArrayList<TicketsData>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_TICKET_DETAIL;
 
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
            	String ticketEventTitle = cursor.getString(cursor.getColumnIndex(KEY_TICKET_EVENT_TITLE)); 
				String ticketId = cursor.getString(cursor.getColumnIndex(KEY_TICKET_ID));
				String ticketTitle = cursor.getString(cursor.getColumnIndex(KEY_TICKET_TITLE));
				String ticketSection = cursor.getString(cursor.getColumnIndex(KEY_TICKET_SECTION));
				String ticketRow = cursor.getString(cursor.getColumnIndex(KEY_TICKET_ROW));				
				int ticketPrice = cursor.getInt(cursor.getColumnIndex(KEY_TICKET_PRICE));
				String ticketMarked = cursor.getString(cursor.getColumnIndex(KEY_TICKET_MARKED));
				String ticketDescription = cursor.getString(cursor.getColumnIndex(KEY_TICKET_DESCRIPTION));				
				String ticketSplits = cursor.getString(cursor.getColumnIndex(KEY_TICKET_SPLITS));
				String ticketDelivery_methods = cursor.getString(cursor.getColumnIndex(KEY_TICKET_DELIVERY_METHODS));
				String ticketType = cursor.getString(cursor.getColumnIndex(KEY_TICKET_TYPE));
				String ticketCheckout_url = cursor.getString(cursor.getColumnIndex(KEY_TICKET_CHECKOUT_URL));			
				String ticketReceipt_begins = cursor.getString(cursor.getColumnIndex(KEY_TICKET_RECEIPT_BEGINS));

				fevoritesList.add(new TicketsData(ticketId, ticketTitle, ticketSection, ticketRow, ticketPrice,
						ticketMarked, ticketDescription, ticketSplits, ticketDelivery_methods, ticketType, 
						ticketCheckout_url,ticketReceipt_begins));		
            } while (cursor.moveToNext());
        }
        db.close();
		return fevoritesList;
    }
    
    public String getTicketEvnetTitle(String ticketId){
    	String ticketEventTitle= "";
        // Select All Query
        String selectQuery = "SELECT  "+KEY_TICKET_EVENT_TITLE+" FROM " + TABLE_TICKET_DETAIL+
        		" WHERE "+KEY_TICKET_ID+"='"+ticketId+"'";
 
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
            	 ticketEventTitle = cursor.getString(cursor.getColumnIndex(KEY_TICKET_EVENT_TITLE)); 		
            } while (cursor.moveToNext());
        }
        db.close();
		return ticketEventTitle;
    }
    
    public String getTicketEvnetDateTime(String ticketId){
    	String ticketEventDateTime= "";
        // Select All Query
        String selectQuery = "SELECT  "+KEY_TICKET_EVENT_DATE_TIME+" FROM " + TABLE_TICKET_DETAIL+
        		" WHERE "+KEY_TICKET_ID+"='"+ticketId+"'";
 
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
            	ticketEventDateTime = cursor.getString(cursor.getColumnIndex(KEY_TICKET_EVENT_DATE_TIME)); 		
            } while (cursor.moveToNext());
        }
        db.close();
		return ticketEventDateTime;
    }
    
    public String getTicketEvnetMapUrl(String ticketId){
    	String ticketEventMapUrl= "";
        // Select All Query
        String selectQuery = "SELECT  "+KEY_TICKET_EVENT_MAP_URL+" FROM " + TABLE_TICKET_DETAIL+
        		" WHERE "+KEY_TICKET_ID+"='"+ticketId+"'";
 
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
            	ticketEventMapUrl = cursor.getString(cursor.getColumnIndex(KEY_TICKET_EVENT_MAP_URL)); 		
            } while (cursor.moveToNext());
        }
        db.close();
		return ticketEventMapUrl;
    }
    
    
}

package com.dds.fye.tickets;

public class EventsData {
	public String eventId;
    public String eventTitle;	        
    public String eventDateString;
    public String eventDateTimeLocal;
    public String eventMapStatic;
    public String eventVenueName;
    public String eventVenueLocation;
    public String localEventAddress;
    public String localEventOrAllEvent;
    
    public String eventVenueZipCode;
    public String eventVenueLon;
    public String eventVenueLat;
    
    public String eventPerformerName;
    public String eventsPerformerId;
    
    /**
     *  Singleton class.
     * @param eventId
     * @param eventTitle
     * @param eventDateString
     * @param eventDateTimeLocal
     * @param eventMapStatic
     * @param eventVenueName
     * @param eventVenueLocation
     * @param localEventAddress
     * @param localEventOrAllEvent
     * @param eventPerformerName
     * @param eventsPerformerId
     * @param eventVenueZipCode
     * @param eventVenueLon
     * @param eventVenueLat
     */
    public EventsData(String eventId, String eventTitle, String eventDateString,
		String eventDateTimeLocal, String eventMapStatic,String eventVenueName,
		String eventVenueLocation,String localEventAddress,String localEventOrAllEvent,
		String eventPerformerName,String eventsPerformerId,String eventVenueZipCode,
		String eventVenueLon,String eventVenueLat) {
    	// TODO Auto-generated constructor stub
    	this.eventId = eventId;
    	this.eventTitle = eventTitle;
    	this.eventDateString = eventDateString;
    	this.eventDateTimeLocal = eventDateTimeLocal;
    	this.eventMapStatic = eventMapStatic;
    	this.eventVenueName = eventVenueName;
    	this.eventVenueLocation = eventVenueLocation;
    	this.localEventAddress = localEventAddress;
    	this.localEventOrAllEvent = localEventOrAllEvent;
    	
    	this.eventPerformerName = eventPerformerName;
    	this.eventsPerformerId = eventsPerformerId;
    	
    	this.eventVenueZipCode = eventVenueZipCode;
    	this.eventVenueLon = eventVenueLon;
    	this.eventVenueLat = eventVenueLat;
    }
}

package com.dds.fye.tickets;

public class VenueData {
	public String VenueEventTitle;
	public String VenueEventId;
	public String VenueId;
	public String VenueName; 
	public String VenueLocation;
	public String VenueCity;
	public String VenueState;
	public String VenuePostalCode;
	public String VenueCountry;
	public String VenueAddress;
	public String VenueLat;
	public String VenueLong;
	public String VenuePerformerName;
	public String VenuePerformerId;
	/**
	 *  Singleton class.
	 * @param VenueEventId
	 * @param VenueEventTitle
	 * @param VenueId
	 * @param VenueName
	 * @param VenueLocation
	 * @param VenueCity
	 * @param VenueState
	 * @param VenuePostalCode
	 * @param VenueCountry
	 * @param VenueAddress
	 * @param VenueLat
	 * @param VenueLong
	 * @param VenuePerformerName
	 * @param VenuePerformerId
	 */
	 public VenueData(String VenueEventId,String VenueEventTitle, String VenueId, String VenueName, String VenueLocation,String VenueCity, 
			 String VenueState,String VenuePostalCode,String VenueCountry,String VenueAddress,
			 String VenueLat,String VenueLong,String VenuePerformerName,String VenuePerformerId) {
		    	// TODO Auto-generated constructor stub
		 	    this.VenueEventTitle = VenueEventTitle;
				this.VenueEventId = VenueEventId;
		    	this.VenueId = VenueId;
		    	this.VenueName = VenueName;
		    	this.VenueLocation = VenueLocation;
		    	this.VenueCity = VenueCity;
		    	this.VenueState = VenueState;
		    	this.VenuePostalCode = VenuePostalCode;
		    	this.VenueCountry = VenueCountry;
		    	this.VenueAddress = VenueAddress;
		    	this.VenueLat = VenueLat;
		    	this.VenueLong = VenueLong;
		    	this.VenuePerformerName = VenuePerformerName;
		    	this.VenuePerformerId=VenuePerformerId;
		    }
}

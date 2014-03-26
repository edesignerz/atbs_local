package com.dds.fye.tickets;

public class TicketsData {
	public String ticketId;
    public String ticketTitle;	        
    public String ticketSection;
    public String ticketRow;
    public int ticketPrice;
    public String ticketMarked;
    public String ticketDescription;
    public String ticketSplits;
    public String ticketDelivery_methods;
    public String ticketType;
    
    public String ticketCheckout_url;
    public String ticketReceipt_begins;
    
    /**
     *  Singleton class.
     * @param ticketId
     * @param ticketTitle
     * @param ticketSection
     * @param ticketRow
     * @param ticketPrice
     * @param ticketMarked
     * @param ticketDescription
     * @param ticketSplits
     * @param ticketDelivery_methods
     * @param ticketType
     * @param ticketCheckout_url
     * @param ticketReceipt_begins
     */
    public TicketsData(String ticketId,String ticketTitle,String ticketSection,String ticketRow,int ticketPrice
    		,String ticketMarked,String ticketDescription,String ticketSplits,String ticketDelivery_methods,String ticketType
    		,String ticketCheckout_url,String ticketReceipt_begins){
    	this.ticketId = ticketId;
    	this.ticketTitle = ticketTitle;
    	this.ticketSection = ticketSection;
    	this.ticketRow = ticketRow;
    	this.ticketPrice = ticketPrice;
    	this.ticketMarked = ticketMarked;
    	this.ticketDescription = ticketDescription;
    	this.ticketSplits  = ticketSplits; 
    	this.ticketDelivery_methods = ticketDelivery_methods;
    	this.ticketType = ticketType; 
    	
    	this.ticketCheckout_url = ticketCheckout_url;
    	this.ticketReceipt_begins = ticketReceipt_begins;
    }
    
}

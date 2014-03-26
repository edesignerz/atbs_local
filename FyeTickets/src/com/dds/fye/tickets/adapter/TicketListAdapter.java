package com.dds.fye.tickets.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.dds.fye.tickets.R;
import com.dds.fye.tickets.TicketsData;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TicketListAdapter extends BaseAdapter{
	List<TicketsData> ticketArrayList = new ArrayList<TicketsData>();
	Activity context;
	public TicketListAdapter(Activity context, List<TicketsData> ticketArrayList){
		this.context = context;
		this.ticketArrayList = ticketArrayList;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return ticketArrayList.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		 View view = arg1;
		 LayoutInflater inflater = (LayoutInflater) arg2.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
         view = (LinearLayout) inflater.inflate(R.layout.ticket_row_view, arg2, false); 
		 //HashMap<String, String> map = new HashMap<String, String>();
		// map = ticketArrayList.get(arg0);
		 TicketsData ticketArrayHashMap = ticketArrayList.get(arg0);
		 //Log.d("AsyncTaskRunner", "The ticketArrayList is: " + ticketArrayList);
		 String ticketTitle_ =ticketArrayHashMap.ticketTitle;// map.get("title");
		 String noOfTicket_ = ticketArrayHashMap.ticketSplits;//map.get("splite");
		 String type =ticketArrayHashMap.ticketType;// map.get("type");
		 String delivery_methods = ticketArrayHashMap.ticketDelivery_methods;//map.get("delivery_methods");
		 //Log.d("AsyncTaskRunner", "The noOfTicket_ is: " + noOfTicket_);	
		 noOfTicket_ = noOfTicket_.replace(" ", "");
		 String[] noOfSite = noOfTicket_.split("[,]");		 
		 int ticketPrice_ = ticketArrayHashMap.ticketPrice;// map.get("price");
		 String row = ticketArrayHashMap.ticketRow;//map.get("row");
		 String section =ticketArrayHashMap.ticketSection;// map.get("section");
		 
		 ImageView spacialTickets = (ImageView)view.findViewById(R.id.spacial_tickets_imageView1);
		 LinearLayout normalTicketView = (LinearLayout)view.findViewById(R.id.normal_tickets_linearLayout);
		 TextView ticketsSectionTextView = (TextView) view.findViewById(R.id.tickets_section_textview);
		 TextView numberOfRow = (TextView) view.findViewById(R.id.number_of_row_textView2);
		 
	        TextView ticketTitle = (TextView) view.findViewById(R.id.ticket_title_textView1);
	        TextView noOfTicketView = (TextView) view.findViewById(R.id.nos_ticket_textView2);
			TextView totalticketPrice = (TextView) view.findViewById(R.id.total_price_textView2);
			
			LinearLayout deliveryMethodLinearlayout = (LinearLayout)view.findViewById(R.id.delivery_method_linearlayout);
			ImageView test1 = (ImageView) view.findViewById(R.id.second_list_row_imageView_test_test_);
			ImageView test2 = (ImageView) view.findViewById(R.id.second_list_row_imageView2);
			ImageView instant = (ImageView) view.findViewById(R.id.second_list__row_imageView3);
			ImageView email = (ImageView) view.findViewById(R.id.second_list_row_imageView4);
			ImageView parking = (ImageView) view.findViewById(R.id.second_list_row_imageView5);
			
			ArrayList<ImageView> delivery_methodsImageViewArrayList = new ArrayList<ImageView>();
			delivery_methodsImageViewArrayList.add(test1);
			delivery_methodsImageViewArrayList.add(test2);
			delivery_methodsImageViewArrayList.add(instant);
			delivery_methodsImageViewArrayList.add(email);
			delivery_methodsImageViewArrayList.add(parking);
						
			String sectionAndRow = "Section: "+section+" Row: "+row;
			ticketsSectionTextView.setText(section.toUpperCase());
			numberOfRow.setText(row);			
			ticketTitle.setText(sectionAndRow);
			
			String ticket_ = "ticket";
			if (Integer.parseInt(noOfSite[0]) > 1) {
				ticket_ = "tickets";
			}
			noOfTicketView.setText(noOfSite[0]+" "+ticket_);
			totalticketPrice.setText("$"+String.valueOf(ticketPrice_));
			//
			//Log.d("AsyncTaskRunner", "The delivery_methods is: " + delivery_methods);
			/*if (type.equals("ticket")) {
			
			}*/
			if (type.equals("parking")) {
				spacialTickets.setVisibility(View.VISIBLE);
				//spacialTickets.setImageResource(R.drawable.parking_ticket_icon);
				spacialTickets.setBackgroundResource(R.drawable.parking_ticket_icon);
				normalTicketView.setVisibility(View.GONE);
				
				/*test1.setVisibility(View.GONE);
				test2.setVisibility(View.GONE);
				instant.setVisibility(View.GONE);
				email.setVisibility(View.GONE);
				parking.setVisibility(View.GONE);*/
			}else if(type.equals("zone")){
				spacialTickets.setVisibility(View.VISIBLE);
				//spacialTickets.setImageResource(R.drawable.zone_ticket_icon);
				spacialTickets.setBackgroundResource(R.drawable.zone_ticket_icon);
				normalTicketView.setVisibility(View.GONE);
				
				/*test1.setVisibility(View.GONE);
				test2.setVisibility(View.GONE);
				instant.setVisibility(View.GONE);
				email.setVisibility(View.GONE);
				parking.setVisibility(View.GONE);*/
			}else if(type.equals("suite")){
				spacialTickets.setVisibility(View.VISIBLE);
				//spacialTickets.setImageResource(R.drawable.suite_ticket_icon);
				spacialTickets.setBackgroundResource(R.drawable.suite_ticket_icon);
				normalTicketView.setVisibility(View.GONE);
				
				/*test1.setVisibility(View.GONE);
				test2.setVisibility(View.GONE);
				instant.setVisibility(View.GONE);
				email.setVisibility(View.GONE);
				parking.setVisibility(View.GONE);*/
			}else if(type.equals("hotel")){
				spacialTickets.setVisibility(View.VISIBLE);
				//spacialTickets.setImageResource(R.drawable.hotel_ticket_icon);
				spacialTickets.setBackgroundResource(R.drawable.hotel_ticket_icon);
				normalTicketView.setVisibility(View.GONE);
				
				/*test1.setVisibility(View.GONE);
				test2.setVisibility(View.GONE);
				instant.setVisibility(View.GONE);
				email.setVisibility(View.GONE);
				parking.setVisibility(View.GONE);*/
			}else if(type.equals("package")){
				spacialTickets.setVisibility(View.VISIBLE);
				//spacialTickets.setImageResource(R.drawable.package_ticket_icon);
				spacialTickets.setBackgroundResource(R.drawable.package_ticket_icon);
				normalTicketView.setVisibility(View.GONE);
				
				/*test1.setVisibility(View.GONE);
				test2.setVisibility(View.GONE);
				instant.setVisibility(View.GONE);
				email.setVisibility(View.GONE);
				parking.setVisibility(View.GONE);*/
			}else{
				spacialTickets.setVisibility(View.GONE);
				normalTicketView.setVisibility(View.VISIBLE);
			}
			//Log.d("AsyncTaskRunner", "The delivery_methods is: " + delivery_methods);
			String[] delivery_methods_ = delivery_methods.split("[,]");
			for (int i = 0; i < delivery_methods_.length; i++) {
				String method = delivery_methods_[i];
				
				if (i <= 4 ) {					
					ImageView imageView = delivery_methodsImageViewArrayList.get(i);
					//Log.e("AsyncTaskRunner", "The method i is: " + i);
					if (method.equals("\"email\"")) {
						imageView.setImageResource(R.drawable.e_ticket_icon);
						imageView.setVisibility(View.VISIBLE);
						deliveryMethodLinearlayout.setVisibility(View.VISIBLE);
					}
					if (method.equals("\"instant\"")) {
						imageView.setImageResource(R.drawable.instant_ticket_icon);
						imageView.setVisibility(View.VISIBLE);
						deliveryMethodLinearlayout.setVisibility(View.VISIBLE);
					}
					if (method.equals("\"localpickup\"")) {
						imageView.setImageResource(R.drawable.parking_pass_included_icon);
						imageView.setVisibility(View.VISIBLE);
						deliveryMethodLinearlayout.setVisibility(View.VISIBLE);
					}
					if (method.equals("\"hotel\"")) {
						imageView.setImageResource(R.drawable.hotel_icon);
						imageView.setVisibility(View.VISIBLE);
						deliveryMethodLinearlayout.setVisibility(View.VISIBLE);
					}
					if (method.equals("\"package\"")) {
						imageView.setImageResource(R.drawable.package_icon);
						imageView.setVisibility(View.VISIBLE);
						deliveryMethodLinearlayout.setVisibility(View.VISIBLE);
					}					
				}		
			}
			
         return view;
	}

}

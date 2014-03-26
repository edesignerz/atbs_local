package com.dds.fye.tickets.adapter;

import java.util.ArrayList;

import com.dds.fye.tickets.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class LocationSearchAdapter extends BaseAdapter{
    Context context;
    ArrayList<String> data = new ArrayList<String>();
	public LocationSearchAdapter(Context context,ArrayList<String> data){
		this.context = context;
		this.data = data;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return data.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return data.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup parent) {
		// TODO Auto-generated method stub
		View view = arg1;
		 LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
         view = (LinearLayout) inflater.inflate(R.layout.list_item, parent, false);
         
         ImageView locationIcon = (ImageView)view.findViewById(R.id.location_icon_imageView1);
         TextView locationName = (TextView)view.findViewById(R.id.location_textView);
         locationName.setTextColor(parent.getContext().getResources().getColor(R.color.location_search_row_text_color));
         if (arg0 == 0) {
        	 locationIcon.setBackgroundResource(R.drawable.location_dropdown_current_location);
        	 String currentLoc = data.get(arg0);
        	 locationName.setText(currentLoc);
         }else{
        	 locationIcon.setBackgroundResource(R.drawable.location_dropdown_choose_location);
        	 String currentLoc = data.get(arg0);
        	 locationName.setText(currentLoc);
         }
         
		return view;
	}

}

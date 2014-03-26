package com.dds.fye.tickets.adapter;

import java.util.ArrayList;

import com.dds.fye.tickets.R;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CustomSpinnerAdapter extends BaseAdapter{
	private Context context;
	private String[] data_;
	public CustomSpinnerAdapter(Context context, String[] data){
		this.context = context;
		data_ = data;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return data_.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return data_[position];
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View view = convertView;
		 LayoutInflater layoutInflater = (LayoutInflater) context
	  		     .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	  		   view = layoutInflater.inflate(R.layout.titalbar_spinner, null);
	  		   	  		   
	  		   TextView title = (TextView)view.findViewById(R.id.title_spinner_textView1);
	  		   title.setText(data_[position]);	  		  
	  		   
		return view;
	}

}

package com.dds.fye.tickets;

import java.util.Calendar;

import android.R.string;
import android.app.Activity;
import android.app.DialogFragment;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract.Events;
import android.provider.CalendarContract.Reminders;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ReminderActivity extends Activity{
	
	LinearLayout backButtonLayout,chooseDateTimeLayout;
	Button donebutton;
	EditText reminder_editText;
	static String date_Time, mm, dd, yyyy, h, m ;
	static TextView selectedDateTimetextView1 ;	

	Calendar c = Calendar.getInstance();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.reminder_view);
		backButtonLayout = (LinearLayout)findViewById(R.id.back_linearlayout);
		donebutton = (Button)findViewById(R.id.reminder_done_button1);
		reminder_editText = (EditText)findViewById(R.id.reminder_editText1);
		chooseDateTimeLayout = (LinearLayout)findViewById(R.id.choose_date_time_linearLayout1);
		selectedDateTimetextView1 = (TextView)findViewById(R.id.selected_date_textView1);

		
		String address = getIntent().getStringExtra("address");
		String dateTime = getIntent().getStringExtra("date_time");	
		reminder_editText.setText(address);
		selectedDateTimetextView1.setText(dateTime);

		
		backButtonLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		donebutton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				addToReminder();	
				finish();
			}
		});
		chooseDateTimeLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
		        DialogFragment newFragment = new DateTimeDialogFragment(ReminderActivity.this,3);
				newFragment.show(getFragmentManager(), "timePicker");     
				
			}
		});
	}
	public static void setDateTime(String data,String mm_,String dd_,String yyyy_,String h_,String m_){
		date_Time = data;
		mm = mm_; 
		dd = dd_;
		yyyy = yyyy_; 
		h = h_; 
		m = m_;
		selectedDateTimetextView1.setText(data);
		Log.d("ArtstTeams", "mm:"+mm);
		Log.d("ArtstTeams", "dd:"+dd);
		Log.d("ArtstTeams", "yyyy:"+yyyy);
		Log.d("ArtstTeams", "h:"+h);
		Log.d("ArtstTeams", "m:"+m);
	}
	
	/**
	 * Add to reminder.
	 */
	private void addToReminder(){
		Log.d("ArtstTeams", "mm:"+mm);
		Log.d("ArtstTeams", "dd:"+dd);
		Log.d("ArtstTeams", "yyyy:"+yyyy);
		Log.d("ArtstTeams", "h:"+h);
		Log.d("ArtstTeams", "m:"+m);
		long calID = 3;
		long startMillis = 0; 
		long endMillis = 0;    
		
		Calendar beginTime = Calendar.getInstance();
		//beginTime.set(2014, 2, 2, 12, 55);
		//beginTime.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH), 2, 12, 55);
		
		startMillis = beginTime.getTimeInMillis();
		 /*Log.d("ArtstTeams", "beginTime:"+beginTime);
		 Log.d("ArtstTeams", "startMillis:"+startMillis);*/
		Calendar endTime = Calendar.getInstance();
		//endTime.set(2014, 2, 2, 1, 00);
		endTime.set(Integer.parseInt(yyyy), Integer.parseInt(mm), Integer.parseInt(dd),
				Integer.parseInt(h), Integer.parseInt(m));
		endMillis = endTime.getTimeInMillis();
		/* Log.d("ArtstTeams", "endTime:"+endTime);
		Log.d("ArtstTeams", "endMillis:"+endMillis);*/


		ContentResolver cr = getContentResolver();
		ContentValues values = new ContentValues();
		values.put(Events.DTSTART, startMillis);
		values.put(Events.DTEND, endMillis);
		values.put(Events.TITLE, "Jazzercise");
		values.put(Events.DESCRIPTION, "Group workout");
		values.put(Events.CALENDAR_ID, calID);
		values.put(Events.EVENT_TIMEZONE, "America/Los_Angeles");
		Uri uri = cr.insert(Events.CONTENT_URI, values);

		// get the event ID that is the last element in the Uri
		long eventID = Long.parseLong(uri.getLastPathSegment());
		//Log.d("ArtstTeams", "eventID:"+eventID);

		ContentResolver cr_ = getContentResolver();
		ContentValues values_ = new ContentValues();
		values_.put(Reminders.MINUTES, 15);
		values_.put(Reminders.EVENT_ID, eventID);
		values_.put(Reminders.METHOD, Reminders.METHOD_ALERT);
		Uri uri_ = cr_.insert(Reminders.CONTENT_URI, values_);
	}
}

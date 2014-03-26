package com.dds.fye.tickets;

import java.util.Calendar;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.DatePicker;

public class DatePickerFragment extends DialogFragment
		implements DatePickerDialog.OnDateSetListener {
	 private SharedPreferences preferences ;
	 /**
	  * Date picker dialog.
	  * Select date.
	  */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
    	
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        // Do something with the date chosen by the user
    	month = month + 1;
    	Log.d("ArtistTeamsFragment", " conserts year:"+year);	
    	Log.d("ArtistTeamsFragment", " conserts month:"+month);	
    	Log.d("ArtistTeamsFragment", " conserts day:"+day);	
    	 preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
    	SharedPreferences.Editor editor = preferences.edit();
		editor.putInt("event_filter_year",year);	
		editor.putInt("event_filter_month",month);	
		editor.putInt("event_filter_day",day);	
		editor.commit();
    }
}

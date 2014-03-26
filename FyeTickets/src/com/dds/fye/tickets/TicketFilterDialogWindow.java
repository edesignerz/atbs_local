package com.dds.fye.tickets;

import com.dds.fye.animation.AnimationHelper;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

public class TicketFilterDialogWindow  extends DialogFragment{
	@Override
	 public Dialog onCreateDialog(Bundle savedInstanceState) {
	   
	  OnClickListener positiveClick = new OnClickListener() {   
	   @Override
	   public void onClick(DialogInterface dialog, int which) {    
	    Toast.makeText(getActivity().getBaseContext(), "Application finishing ...", Toast.LENGTH_SHORT).show();
	    getActivity().finish();    
	   }
	  };
	   
	  OnClickListener negativeClick = new OnClickListener() {   
	   @Override
	   public void onClick(DialogInterface dialog, int which) {
	    Toast.makeText(getActivity().getBaseContext(), "No option selecting", Toast.LENGTH_SHORT).show();
	     
	   }
	  };
	  Bundle mArgs = getArguments();
	  String condition = mArgs.getString("message");
	  Log.d("ArtistTeamsFragment", "condition:"+condition);
	  
	   final Dialog dialog = new Dialog(getActivity(), R.style.DialogSlideAnimUpDown);
 	  dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
 	  dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
 	  
 	  if (condition.equals("ticketfilter")) {
		  dialog.setContentView(R.layout.tickets_filter_dialog);
		  int noT= ArtistTeamsEventList.noOfTickets;
		  final ViewFlipper viewFlipper = (ViewFlipper)dialog.findViewById(R.id.ticketFilterViewFliper);
		  //First View
		  ImageView close_textview = (ImageView)dialog.findViewById(R.id.event_info_close_textView2);	
		  LinearLayout ticketCountLinearLayout = (LinearLayout)dialog.findViewById(R.id.select_ticket_count);
		  final ImageView email_setting = (ImageView) dialog.findViewById(R.id.email_setting_imageView1);
		  final ImageView instant_setting = (ImageView) dialog.findViewById(R.id.instant_setting_imageView2);
		  final ImageView hotal_setting = (ImageView) dialog.findViewById(R.id.hotal_setting_imageView3);
		  final ImageView docter_setting = (ImageView) dialog.findViewById(R.id.docter_setting_imageView4);
		  final ImageView parking_setting = (ImageView) dialog.findViewById(R.id.parking_setting_imageView5);
		  final TextView messagetextview = (TextView)dialog.findViewById(R.id.message_ticket_textView11);

		  
		  //Feature development 
       /* final TextView seekBarRadiusValue = (TextView)dialog.findViewById(R.id.ticket_radiou_textView1);
		  SeekBar seekBar = (SeekBar)dialog.findViewById(R.id.ticket_radius_seekBar1);
		  
		  seekBar.setProgress(50);
		  seekBarRadiusValue.setText(String.valueOf(50)); 
		  seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				// TODO Auto-generated method stub
				if (progress != 0) {
					seekBarRadiusValue.setText(String.valueOf(progress)); 
					SharedPreferences.Editor editor = preferences.edit();
		    		editor.putInt("radius",progress);	        		
		    		editor.commit();
				}			 			
			}
		});*/
		  
		  ticketCountLinearLayout.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					viewFlipper.setInAnimation(AnimationHelper
		                    .inFromRightAnimation());
					viewFlipper.setOutAnimation(AnimationHelper
		                    .outToLeftAnimation());
					viewFlipper.showNext();
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
						dialog.dismiss();
					}
				});

	  }
 	  //dialog.getWindow().setGravity(Gravity.CENTER);	
	  dialog.show();
	  return dialog;	  	  
	}
}

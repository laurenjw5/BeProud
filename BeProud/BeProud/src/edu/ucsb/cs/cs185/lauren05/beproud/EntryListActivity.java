package edu.ucsb.cs.cs185.lauren05.beproud;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.content.DialogInterface;

public class EntryListActivity extends ListActivity {
	String todaysDate = null;
	String[] userInput = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Get Intent for this Activity and get Extras- user's input from main activity edit text field.
		Intent listIntent = getIntent();
		userInput = listIntent.getExtras().getStringArray("user's entry today");
		todaysDate = listIntent.getExtras().getString("today's date");
		setTitle(todaysDate);

	    // Binding user's accomplishment entries to ListAdapter
	    this.setListAdapter(new ArrayAdapter<String>(this, R.layout.activity_list, R.id.textView1, userInput));
	         
	}
	
	public void changeUserInput(String[] userInput)
	{
		 this.setListAdapter(new ArrayAdapter<String>(this, R.layout.activity_list, R.id.textView1, userInput));
	}
	
	@Override
	public void onBackPressed() {
		this.finish(); // should bring us back to MainActivity
	
	}
	
	@Override 
	public void onListItemClick(ListView l, View v, int position, long id) {
		//Toast.makeText(getApplicationContext(), "in clickEntry", Toast.LENGTH_SHORT).show();
		final String selectedAccomplishment = userInput[position]; // should be right 
		final CharSequence[] items = {"tweet","edit","delete"};
		
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		
		// set title
		alertDialogBuilder.setTitle("What would you like to do?");
		// set dialog message
					alertDialogBuilder
					.setItems(items, new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							
							Intent i = new Intent();
							
							switch(which){
							case 0: //Tweet
								Toast.makeText(getApplicationContext(), "Tweet successful!", Toast.LENGTH_SHORT).show(); break;
							case 1: //edit
								i.putExtra("user's entry to edit", selectedAccomplishment);
								i.putExtra("user's entry date", todaysDate);
								setResult(RESULT_OK, i); 
								onBackPressed();
								break;
							case 2: //delete
								String[] newStringArr = MainActivity.deleteAccomplishment(todaysDate, selectedAccomplishment);
								changeUserInput(newStringArr);
							    Toast.makeText(getApplicationContext(), "Accomplishment Successfully Deleted!", Toast.LENGTH_LONG).show();
								try {
									this.finalize();
								} catch (Throwable e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								break;
							default: break;
									
							}
							
						}
					})
					.setCancelable(true);


		// create alert dialog
		AlertDialog alertDialog = alertDialogBuilder.create();
		 
		// show it
		alertDialog.show();
	}
}

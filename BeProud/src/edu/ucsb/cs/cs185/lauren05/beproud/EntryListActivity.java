package edu.ucsb.cs.cs185.lauren05.beproud;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.actionbarsherlock.app.SherlockFragmentActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.content.DialogInterface;



public class EntryListActivity extends SherlockFragmentActivity {
	String todaysDate = null;
	String[] userInput = null;
	ListView listView = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list);
		
		listView = (ListView) findViewById(R.id.listview);
		// Get Intent for this Activity and get Extras- user's input from main activity edit text field.
		Intent listIntent = getIntent();
		userInput = listIntent.getExtras().getStringArray("user's entry today");
		todaysDate = listIntent.getExtras().getString("today's date");
		
		final ArrayList<String> list = new ArrayList<String>();
		for(int i=0; i< userInput.length; i++)
			list.add(userInput[i]);
		
		setTitle(todaysDate);

		final StableArrayAdapter adapter = new StableArrayAdapter(this,
		        android.R.layout.simple_list_item_1, list);
		    listView.setAdapter(adapter);
		
		final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		    
	    // Binding user's accomplishment entries to ListAdapter
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

		        @Override
		        public void onItemClick(AdapterView<?> parent, final View view,
		            int position, long id) {
		        	final String selectedAccomplishment = userInput[position]; // should be right 
		    		final CharSequence[] items = {"tweet","edit","delete"};
		    		
		    		
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
		    							    Toast.makeText(getApplicationContext(), "Delete successful!", Toast.LENGTH_LONG).show();
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

		      });
	         
	}
	
	public void changeUserInput(String[] userInput)
	{
		final ArrayList<String> list = new ArrayList<String>();
		for(int i=0; i< userInput.length; i++)
			list.add(userInput[i]);

		final StableArrayAdapter adapter = new StableArrayAdapter(this,
		        android.R.layout.simple_list_item_1, list);
		    listView.setAdapter(adapter);
	}
	
	@Override
	public void onBackPressed() {
		this.finish(); // should bring us back to MainActivity
	}
	
	/*
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
							    Toast.makeText(getApplicationContext(), "Delete successful!", Toast.LENGTH_LONG).show();
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
	*/
	
	private class StableArrayAdapter extends ArrayAdapter<String> {

	    HashMap<String, Integer> mIdMap = new HashMap<String, Integer>();

	    public StableArrayAdapter(Context context, int textViewResourceId,
	        List<String> objects) {
	      super(context, textViewResourceId, objects);
	      for (int i = 0; i < objects.size(); ++i) {
	        mIdMap.put(objects.get(i), i);
	      }
	    }

	    @Override
	    public long getItemId(int position) {
	      String item = getItem(position);
	      return mIdMap.get(item);
	    }

	    @Override
	    public boolean hasStableIds() {
	      return true;
	    }

	  }
	
	
}

package edu.ucsb.cs.cs185.lauren05.beproud;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
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
import com.actionbarsherlock.app.SherlockFragmentActivity;

public class EntryListActivity extends SherlockFragmentActivity {
	String todaysDate = null;
	String[] userInput = null;
	ListView listView = null;
	ArrayList<String> list;
	StableArrayAdapter adapter;
	String timeStamp;
	static final int ADD_ENTRY = 3;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list);
		timeStamp = new SimpleDateFormat("MM/dd/yyyy").format(Calendar.getInstance().getTime());
		
		listView = (ListView) findViewById(R.id.listview);
		Toast.makeText(getBaseContext(), "made it here EntryListActivity",
				Toast.LENGTH_SHORT).show();

		if (getIntent() != null) {
			// Get Intent for this Activity and get Extras- user's input from
			// main activity edit text field.
			Intent listIntent = getIntent();

			if (listIntent.getExtras() != null) {
				userInput = listIntent.getExtras().getStringArray(
						"user's entry today");
				todaysDate = listIntent.getExtras().getString("today's date");

				this.setTitle(todaysDate);

				final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
						this);

				list = new ArrayList<String>();

				for (int i = 0; i < userInput.length; i++)
					list.add(userInput[i]);

				adapter = new StableArrayAdapter(this,
						android.R.layout.simple_list_item_1, list);
				listView.setAdapter(adapter);

				// Binding user's accomplishment entries to ListAdapter
				listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
						final String selectedAccomplishment = userInput[position]; // should
																					// be
																					// right
						final CharSequence[] items = { "tweet", "edit", "delete" };

						// set title
						alertDialogBuilder
								.setTitle("What would you like to do?");
						// set dialog message
						alertDialogBuilder.setItems(items,
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {

										Intent i = new Intent();

										switch (which) {
										case 0: // Tweet
											Toast.makeText(
													getApplicationContext(),
													"Tweet successful!",
													Toast.LENGTH_SHORT).show();
											break;
										case 1: // edit
											i.putExtra("user's entry to edit",
													selectedAccomplishment);
											i.putExtra("user's entry date",
													todaysDate);
											setResult(RESULT_OK, i);
											onBackPressed();
											break;
										case 2: // delete
											String[] newStringArr = MainActivity
													.deleteAccomplishment(
															todaysDate,
															selectedAccomplishment);
											changeUserInput(newStringArr);
											Toast.makeText(
													getApplicationContext(),
													"Delete successful!",
													Toast.LENGTH_LONG).show();
											try {
												this.finalize();
											} catch (Throwable e) {
												// TODO Auto-generated catch
												// block
												e.printStackTrace();
											}
											break;
										default:
											break;

										}

									}
								}).setCancelable(true);

						// create alert dialog
						AlertDialog alertDialog = alertDialogBuilder.create();

						// show it
						alertDialog.show();
					}

				});
			}
		} else
			Toast.makeText(this.getApplicationContext(), "userInput is null",
					Toast.LENGTH_SHORT).show();
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data); 
		
		if (requestCode == ADD_ENTRY) {
			if (resultCode == RESULT_OK) {          	 
		    	 String editAccomplishment = data.getExtras().getString("user's entry to edit"); 
//			     timeStamp = data.getStringExtra("user's entry date"); 		       
			     
			     list.add(editAccomplishment);
			     adapter.notifyDataSetChanged();		         
		     }
		     
		     if (resultCode == RESULT_CANCELED) {    
		         //Write your code if there's no result
		     }
		  }
	} //onActivityResult
	
	
	@Override
	public boolean onCreateOptionsMenu(com.actionbarsherlock.view.Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		super.getSupportMenuInflater().inflate(R.menu.menu_list, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(
			com.actionbarsherlock.view.MenuItem item) {

		switch (item.getItemId()) {
		case R.id.add:
			Toast.makeText(getBaseContext(), "add", Toast.LENGTH_SHORT).show();
			Intent intent = new Intent(EntryListActivity.this, MainActivity.class);
    		intent.putExtra("date", timeStamp);
    		startActivityForResult(intent, ADD_ENTRY);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public void changeUserInput(String[] userInput) {
		final ArrayList<String> list = new ArrayList<String>();
		for (int i = 0; i < userInput.length; i++)
			list.add(userInput[i]);

		final StableArrayAdapter adapter = new StableArrayAdapter(this,
				android.R.layout.simple_list_item_1, list);
		listView.setAdapter(adapter);
	}

	@Override
	public void onBackPressed() {
		this.finish(); // should bring us back to MainActivity
	}

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

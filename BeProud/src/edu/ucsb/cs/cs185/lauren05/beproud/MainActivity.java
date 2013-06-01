package edu.ucsb.cs.cs185.lauren05.beproud;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Map;

//import android.app.Activity;
//import android.app.AlertDialog;
//import android.content.DialogInterface;
//import android.text.format.DateFormat;
//import android.view.Menu;
//import java.sql.Date;

import com.actionbarsherlock.app.SherlockFragmentActivity;

public class MainActivity extends SherlockFragmentActivity {

	
	String timeStamp;
	static final int PICK_DATE_REQUEST = 2;
	static Map<String,ArrayList<String> > entryObject = null;
	
	@SuppressLint("SimpleDateFormat")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		timeStamp = new SimpleDateFormat("MM/dd/yyyy").format(Calendar.getInstance().getTime());
		// initialize variables
		entryObject=new HashMap<String, ArrayList<String> >();
	}

	@Override
	public boolean onCreateOptionsMenu(com.actionbarsherlock.view.Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		super.getSupportMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(com.actionbarsherlock.view.MenuItem item) {
		
		switch (item.getItemId()) {
		case R.id.list:
			Toast.makeText(getBaseContext(), "clicked list", Toast.LENGTH_SHORT).show();
			saveEntry(this.findViewById(R.layout.activity_main));
			return true;
		case R.id.month:
			Intent intent = new Intent(MainActivity.this, CalendarView.class);
    		intent.putExtra("date", timeStamp);
    		startActivityForResult(intent, PICK_DATE_REQUEST);				
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	public void saveEntry(View v){
		
		String newEntry = null;
		EditText e = (EditText) findViewById(R.id.body);

		//use regexes for error handling -- we don't want whitespace as an Accomplishment Entry
		String pattern = "(\\s+)";

		if(!e.getText().toString().matches(pattern) && e.getText() != null)
		{
			if (!(entryObject.containsKey(timeStamp)))
					entryObject.put(timeStamp, new ArrayList<String>());
			newEntry =  e.getText().toString();
			if ( !(entryObject.get(timeStamp).contains(newEntry)))
				entryObject.get(timeStamp).add(newEntry);
			else
				Toast.makeText(this.getApplicationContext(), "Sorry, you have already entered this accomplishment.", Toast.LENGTH_LONG).show();
			Intent request = new Intent(MainActivity.this, EntryListActivity.class);
			String[] passUserInput = entryObject.get(timeStamp).toArray(new String[entryObject.size()]);
			request.putExtra("user's entry today", passUserInput );
			request.putExtra("today's date", timeStamp);
			startActivityForResult(request, 1); 
		}
		else
			Toast.makeText(this.getApplicationContext(), "Oops! It seems you have not entered anything. Please enter an Accomplishment.", Toast.LENGTH_LONG).show();
		
	} // end saveEntry
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		  if (requestCode == 1) {

		     if(resultCode == RESULT_OK){      
		    	 
		    	 String editAccomplishment = data.getStringExtra("user's entry to edit"); 
			     timeStamp = data.getStringExtra("user's entry date"); 		       
		         TextView t = (TextView) findViewById(R.id.body);
		         t.setText(editAccomplishment);
		         // delete this older Accomplishment for this date...they are going to change it anyways..:)
		         entryObject.get(timeStamp).remove(editAccomplishment);
		     }
		     if (resultCode == RESULT_CANCELED) {    
		         //Write your code if there's no result
		     }
		  }
		  else if(requestCode == PICK_DATE_REQUEST)
		  {
			  if(resultCode == RESULT_OK)
				  Toast.makeText(getApplicationContext(), data.getStringExtra("date"), Toast.LENGTH_SHORT).show();
		  }
		}//onActivityResult
	
	 public static String[] deleteAccomplishment(String todaysDate, String accomplishment) {
		entryObject.get(todaysDate).remove(accomplishment);
		String[] newStringArr = entryObject.get(todaysDate).toArray(new String[entryObject.size()]); 
		return newStringArr;
	}

	public static class LineEditText extends EditText{
  // we need this constructor for LayoutInflater
    	
   public LineEditText(Context context, AttributeSet attrs) {
   super(context, attrs);
    mRect = new Rect();
    mPaint = new Paint();
    mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
    mPaint.setColor(Color.BLUE);
    //this.setPadding(100, this.getPaddingTop(), this.getPaddingRight(), this.getPaddingBottom());
    
  }
 
  	private Rect mRect;
    private Paint mPaint; 
    private Paint redPaint;
     
     @Override
     protected void onDraw(Canvas canvas) {
   
         int height = getHeight();
         int line_height = getLineHeight();

         int count = height / line_height;

         if (getLineCount() > count)
             count = getLineCount();

         Rect r = mRect;
         Paint paint = mPaint;
         int baseline = getLineBounds(0, r);

         for (int i = 0; i < count; i++) {

             canvas.drawLine(r.left, baseline + 1, r.right, baseline + 1, paint);
             baseline += getLineHeight();

         super.onDraw(canvas);
     }

         
 }
}

}

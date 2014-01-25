package com.muddaflappers.hacktech;
 
import java.text.Format;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

import java.text.Format;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;

import android.app.Activity;
import android.database.Cursor;
import android.provider.CalendarContract;
import android.text.format.DateFormat;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
 
public class MainActivity extends Activity implements OnClickListener{
   
   //extra variables
   private EditText dateText;
   //create cursor for calendar
   private Cursor mCursor = null;
   private static final String[] COLS = new String[]
         {CalendarContract.Events.TITLE, CalendarContract.Events.DTSTART, CalendarContract.Events.DTEND, CalendarContract.Events.ALL_DAY};
 
   @Override
   protected void onCreate(Bundle savedInstanceState)
   {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_main);
     
      //set up calendar cursor
      mCursor = getContentResolver().query(CalendarContract.Events.CONTENT_URI, COLS, null, null, null);
      mCursor.moveToFirst();
               
      //set up button event
      dateText = (EditText) findViewById(R.id.dateBox);
      Button btnGetCalendar = (Button) findViewById(R.id.btnGetCalendar);
      btnGetCalendar.setOnClickListener(this);
      onClick(findViewById(R.id.btnGetCalendar));
   }
   
   public void onClick(View v){
                GregorianCalendar start = new GregorianCalendar();
                GregorianCalendar end = new GregorianCalendar();
                GregorianCalendar dayOf = Functions.toDate(dateText); //day user wants to find
                boolean allDay; //if event is all day
                ArrayList<Event> eventList = new ArrayList<Event>();
               
                //loop through all events and add to array list
                switch(v.getId()){
                case R.id.btnGetCalendar:
					while(!mCursor.isLast()){
						start.setTimeInMillis(mCursor.getLong(1));
						end.setTimeInMillis(mCursor.getLong(2)); // end date obj
						allDay = !mCursor.getString(3).equals("0"); //gets boolean if all day
						Event event = new Event(mCursor.getString(0), start, end, allDay);
						if(Functions.checkIfDateMatch(event, dayOf))
							eventList.add(event);
						mCursor.moveToNext(); //moves to next event
                    }
					Collections.sort(eventList);
                break;
                }
                 Toast t = Toast.makeText(getApplicationContext(), "All Events Added", Toast.LENGTH_LONG);
                 t.show();
        }
 
   @Override
   public boolean onCreateOptionsMenu(Menu menu)
   {
      // Inflate the menu; this adds items to the action bar if it is present.
      getMenuInflater().inflate(R.menu.main, menu);
      return true;
   }
 
}
package com.muddaflappers.hacktech;
 
import java.text.*;
import java.util.*;

import android.util.*;
import android.view.*;
import android.widget.*;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.app.AlertDialog;

import java.io.FileWriter;
import java.io.IOException;

import android.app.Activity;
import android.bluetooth.*;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Events;

import java.io.File;
import java.util.List;

import android.net.Uri;
import android.bluetooth.BluetoothAdapter;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.view.View.OnClickListener;

public class MainActivity extends Activity{
   
   //extra variables
   private EditText dateText;
   //create cursor for calendar
   private Cursor mCursor = null;
   private static final String[] COLS = new String[]
         {CalendarContract.Events.TITLE, CalendarContract.Events.DTSTART, CalendarContract.Events.DTEND, CalendarContract.Events.ALL_DAY,
	   			CalendarContract.Events.DELETED};
   Intent intent;
   ArrayList<Event> eventList;
   boolean eventListCreated;
   
   @Override
   protected void onCreate(Bundle savedInstanceState)
   {
	  intent = new Intent();
      intent.setAction(Intent.ACTION_SEND);
      intent.setType("text/plain");
      intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(Environment.getExternalStorageDirectory() + "/schedule.txt")));
	   
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_main);
     
      //set up calendar cursor
      mCursor = getContentResolver().query(CalendarContract.Events.CONTENT_URI, COLS, null, null, null);
      eventListCreated = false;
      ArrayList<String> stringList = Functions.refresh();
      ArrayAdapter<String> adapter;
      adapter=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, stringList);
   }
   
   // duration that the device is discoverable
   private static final int DISCOVER_DURATION = 300;
   // our request code (must be greater than zero)
   private static final int REQUEST_BLU = 1;
   
   public void enableBlu(){
	      // enable device discovery - this will automatically enable Bluetooth
	      Intent discoveryIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
	      discoveryIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, DISCOVER_DURATION );
	      startActivityForResult(discoveryIntent, REQUEST_BLU);
	   }
 
   protected void onActivityResult (int requestCode, int resultCode, Intent data) {
	      if (resultCode == DISCOVER_DURATION && requestCode == REQUEST_BLU) {
	         PackageManager pm = getPackageManager();
	         List<ResolveInfo> appsList = pm.queryIntentActivities(intent, 0);        
	         
	         if(appsList.size() > 0){
	            String packageName = null;
	            String className = null;
	            boolean found = false;
	             for(ResolveInfo info: appsList){
	                packageName = info.activityInfo.packageName;;
	              if( packageName.equals("com.android.bluetooth")){
	                 className = info.activityInfo.name;
	                 found = true;
	                 break;// found
	              }
	            }
	            if(!found){
	              Toast.makeText(this, "Bluetooth not found", Toast.LENGTH_SHORT).show();
	              // exit
	            }
	            intent.setClassName(packageName, className);
	            startActivity(intent);
	         }
	      }else{ // cancelled or error
	         Toast.makeText(this,"Bluetooth Canceled", Toast.LENGTH_SHORT).show();
	      }
	}
   
   @Override
   public boolean onCreateOptionsMenu(Menu menu)
   {
      // Inflate the menu; this adds items to the action bar if it is present.
      getMenuInflater().inflate(R.menu.more_menu, menu);
      return true;
   }
   
   @Override
   public boolean onOptionsItemSelected(MenuItem item) {
      final Context context = this;
       switch(item.getItemId())
       {
       case R.id.bluetooth:
          BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
          //if bluetooth not supported
          if (btAdapter == null) {
             Toast.makeText(this, "Bluetooth not supported", Toast.LENGTH_LONG).show();
          }else{
          eventList = new ArrayList<Event>();
          
          LayoutInflater li = LayoutInflater.from(context);
          View promptView = li.inflate(R.layout.prompt, null);
          AlertDialog.Builder adb = new AlertDialog.Builder(context);
          adb.setView(promptView);
          final EditText dateInput = (EditText) promptView.findViewById(R.id.dateInput);
          adb
             .setCancelable(false)
             .setPositiveButton("OK", new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface d, int id){
                   dateText.setText(dateInput.getText());
                }
             })
             .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface d, int id) {
                   d.cancel();
                }
             });
          AlertDialog ad = adb.create();
          ad.show();
          
          GregorianCalendar dayOf = Functions.toDate(dateText); //day user wants to find
          if(dayOf == null){//error in date
             Toast.makeText(getApplicationContext(), "Invalid Date", Toast.LENGTH_LONG).show();
             System.exit(1);
          }
          mCursor.moveToFirst();
             
          //checks through all events
          while(!mCursor.isAfterLast()){
             if(!mCursor.getString(4).equals("1")){
                GregorianCalendar start = new GregorianCalendar();
                GregorianCalendar end = new GregorianCalendar();
                start.setTimeInMillis(mCursor.getLong(1));
                end.setTimeInMillis(mCursor.getLong(2)); // end date obj
                boolean allDay = !mCursor.getString(3).equals("0"); //gets boolean if all day
                Event event = new Event(mCursor.getString(0), start, end, allDay);
                Log.d("MainActivity", "Event = " + (start.get(Calendar.MONTH)+1) + "/" + start.get(Calendar.DATE) + "/" + start.get(Calendar.YEAR));
                if(Functions.checkIfDateMatch(event, dayOf)){
                   Log.d("MainActivity", "Confirmed date");
                   eventList.add(event);
                }
             }
             mCursor.moveToNext(); //moves to next event
          }
          Collections.sort(eventList);
          eventListCreated = true;
     
          //writes file
          try {
             Functions.writeToText(eventList);
          } catch (IOException e) {
             Toast.makeText(getApplicationContext(), "Error Writing File", Toast.LENGTH_LONG).show();
          }
             Toast.makeText(getApplicationContext(), "All Events Added", Toast.LENGTH_LONG).show();
             enableBlu();
          }
          return true;
          
          // find option
       case R.id.find:
          if(eventListCreated)
          {
             ArrayList<Event> otherList = Functions.readFile();
             boolean[] freeTime = Functions.freeTimeCalc(eventList, otherList);
             
             /*
              * DEBUGGER DATA BELOW
              */
             String otherData = "";
             Log.d("Other List Size", Integer.toString(otherList.size()));
             for(int i = 0; i < otherList.size(); i++)
             {
                otherData += "Start: " + otherList.get(i).startDate.get(Calendar.HOUR_OF_DAY) + ":" + otherList.get(i).startDate.get(Calendar.MINUTE);
                otherData += "  End:" + otherList.get(i).endDate.get(Calendar.HOUR_OF_DAY) + ":" + otherList.get(i).endDate.get(Calendar.MINUTE);
                otherData += "\n";
             }
             Log.d("Other List", otherData);
             
             String eventData = "";
             Log.d("Event List Size", Integer.toString(eventList.size()));
             for(int i = 0; i < eventList.size(); i++)
             {
                eventData += "Start: " + eventList.get(i).startDate.get(Calendar.HOUR_OF_DAY) + ":" + eventList.get(i).startDate.get(Calendar.MINUTE);
                eventData += "  End:" + eventList.get(i).endDate.get(Calendar.HOUR_OF_DAY) + ":" + eventList.get(i).endDate.get(Calendar.MINUTE);
                eventData += "\n";
             }
             Log.d("Event List", eventData);
             
             int counter;
             int startHours = 0;
             int endHours = 0;
             int startMinutes = 0;
             int endMinutes = 0;
             int streak = 0;
             String freeTimeString = "";
             for(counter = 0; counter < freeTime.length; counter++)
             {
                
                if(freeTime[counter])
                   streak++;
                else
                {
                   if(streak == 0)
                      continue;
                   startHours = (counter-streak) / 60;
                   startMinutes = (counter-streak) % 60;
                   endHours = (counter) / 60;
                   endMinutes = (counter) % 60;
                   freeTimeString += "Start: " + startHours + ":" + startMinutes + "  End: " + endHours + ":" + endMinutes + "\n";
                   streak = 0;
                }
             }
             if(streak != 0)
             {
                startHours = (counter-streak) / 60;
                startMinutes = (counter-streak) % 60;
                endHours = (counter) / 60;
                endMinutes = (counter) % 60;
                freeTimeString += "Start: " + startHours + ":" + startMinutes + "  End: " + endHours + ":" + endMinutes + "\n";
             }
             Log.d("WRITETEST", freeTimeString);
             FileWriter fWriter;
             File sdCardFile = new File(Environment.getExternalStorageDirectory() + "/freeTime.txt");
             Log.d("TAG", sdCardFile.getPath()); //<-- check the log to make sure the path is correct.
             try{
                  fWriter = new FileWriter(sdCardFile, false);
                  fWriter.write(freeTimeString);
                  fWriter.flush();
                  fWriter.close();
              }catch(Exception e){
                       e.printStackTrace();
              }
             
             intent = new Intent();
             intent.setAction(Intent.ACTION_SEND);
             intent.setType("text/plain");
              intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(Environment.getExternalStorageDirectory() + "/freeTime.txt")));
              BluetoothAdapter blueAdapter = BluetoothAdapter.getDefaultAdapter();
                   enableBlu();
          }
          else
             Toast.makeText(this, "Connect Devices First!", Toast.LENGTH_LONG).show();
           return true;
       case R.id.refresh:
          ArrayList<String> stringList = Functions.refresh();
          ArrayAdapter<String> adapter;
          adapter=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, stringList);
          adapter.notifyDataSetChanged();
          Toast.makeText(this, "List Refreshed!", Toast.LENGTH_LONG).show();
           return true;
       default:
          break;
       }
       return super.onOptionsItemSelected(item);
   } 
}
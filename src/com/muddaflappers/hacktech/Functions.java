package com.muddaflappers.hacktech;

import java.io.*;
import java.text.*;
import java.util.*;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

public class Functions {
	
	public static GregorianCalendar toDate(EditText textInput){
	      GregorianCalendar cal = new GregorianCalendar();
	      String text = textInput.getText().toString();
	      String[] tokens = text.split("/");
	      cal.set(Integer.parseInt(tokens[2]), Integer.parseInt(tokens[0])-1, Integer.parseInt(tokens[1]));
	      return cal;
	}
	
	public static ArrayList<Event> readFile(){
		ArrayList<String> stringList = new ArrayList<String>();
		ArrayList<Event> eventList;
		try {
			Log.d("TAG", Environment.getExternalStorageDirectory() + "/bluetooth/schedule.txt");
			InputStream inputStream = new FileInputStream(Environment.getExternalStorageDirectory() + "/bluetooth/schedule.txt");
			if (inputStream != null) {
	            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
	            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
	            String lineInput = "";

	            while ((lineInput = bufferedReader.readLine()) != null) {
	                 stringList.add(lineInput);
	            }
	            inputStream.close();
	        }
			
		} catch (FileNotFoundException e) {
			Log.d("readFile", "File Not Found");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Log.d("readFile", "Corrupted File");
		}
		eventList = new ArrayList<Event>();
		eventList = parseStringToEvent(stringList);
		return eventList;
	}
	
	private static ArrayList<Event> parseStringToEvent(ArrayList<String> stringList){
		ArrayList<Event> eventList = new ArrayList<Event>();
		//string index order: title, hourStart, minuteStart, hourEnd, minuteEnd
		for(int i = 0; i < stringList.size(); i++){
			String[] temp = stringList.get(i).split(", ");
			GregorianCalendar tempCal1 = new GregorianCalendar(0,0,0,Integer.parseInt(temp[1]), Integer.parseInt(temp[2]), 0);
			GregorianCalendar tempCal2 = new GregorianCalendar(0,0,0,Integer.parseInt(temp[3]), Integer.parseInt(temp[4]), 0);
			eventList.add(new Event(temp[0], tempCal1, tempCal2, false));
		}
		
		return eventList;
	}
	
	public static boolean checkIfDateMatch(Event event, Calendar current) // TODO: NEED TO FIX
	{
		Calendar eventStart = event.getStartDate();
		Calendar eventEnd = event.getEndDate();
		if(eventStart.get(Calendar.YEAR) <= current.get(Calendar.YEAR) && eventEnd.get(Calendar.YEAR) >= current.get(Calendar.YEAR))
			if(eventStart.get(Calendar.MONTH) <= current.get(Calendar.MONTH) && eventEnd.get(Calendar.MONTH) >= current.get(Calendar.MONTH))
					if(eventStart.get(Calendar.DATE) <= current.get(Calendar.DATE) && eventEnd.get(Calendar.DATE) >= current.get(Calendar.DATE))
						return true;
		return false;
	}
	
	public static void writeToText(ArrayList<Event> eventArrayList) throws IOException
	{
		String writeString = "";
		for (int i = 0; i < eventArrayList.size(); i++)
		{
			writeString += eventArrayList.get(i).title;
			writeString += ", ";
			writeString += eventArrayList.get(i).startDate.get(Calendar.HOUR_OF_DAY);
			writeString += ", ";
			writeString += eventArrayList.get(i).startDate.get(Calendar.MINUTE);
			writeString += ", ";
			writeString += eventArrayList.get(i).endDate.get(Calendar.HOUR_OF_DAY);
			writeString += ", ";
			writeString += eventArrayList.get(i).endDate.get(Calendar.MINUTE);
			writeString += ", ";
			writeString += "\n";
		}
		
		Log.d("WRITETEST", writeString);
		FileWriter fWriter;
		File sdCardFile = new File(Environment.getExternalStorageDirectory() + "/schedule.txt");
		Log.d("TAG", sdCardFile.getPath()); //<-- check the log to make sure the path is correct.
		try{
		     fWriter = new FileWriter(sdCardFile, false);
		     fWriter.write(writeString);
		     fWriter.flush();
		     fWriter.close();
		 }catch(Exception e){
		          e.printStackTrace();
		 }
	}
	
	public static boolean[] freeTimeCalc(ArrayList<Event> event1, ArrayList<Event> event2)
	{
		final int MAX_MINUTES = 1440;
		boolean[] freeTime = new boolean[MAX_MINUTES];
		for(int i = 0; i < freeTime.length; i++)
			freeTime[i] = true;
		
		int startIndex;
		int endIndex;
		for(int i = 0; i < event1.size(); i++)
		{	
			startIndex = event1.get(i).startDate.get(Calendar.HOUR_OF_DAY) * 60 + event1.get(i).startDate.get(Calendar.MINUTE);
			endIndex = event1.get(i).endDate.get(Calendar.HOUR_OF_DAY) * 60 + event1.get(i).endDate.get(Calendar.MINUTE);
			for(int j = startIndex; j < endIndex; j++)
				freeTime[j] = false;
		}
		for(int i = 0; i < event2.size(); i++)
		{
			startIndex = event2.get(i).startDate.get(Calendar.HOUR_OF_DAY) * 60 + event2.get(i).startDate.get(Calendar.MINUTE);
			endIndex = event2.get(i).endDate.get(Calendar.HOUR_OF_DAY) * 60 + event2.get(i).endDate.get(Calendar.MINUTE);
			for(int j = startIndex; j < endIndex; j++)
				freeTime[j] = false;
		}
		return freeTime;
	}
}
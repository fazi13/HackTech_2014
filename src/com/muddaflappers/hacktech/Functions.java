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
			InputStream inputStream = new FileInputStream(Environment.getExternalStorageDirectory() + "/schedule.txt");
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
	
	public static ArrayList<Event> freeTimeCalc(ArrayList<Event> event1, ArrayList<Event> event2)
	{
		ArrayList<Event> freeTimeAL = new ArrayList<Event>(0);
		ArrayList<Event> free1AL;
		ArrayList<Event> free2AL;
		
		free1AL = convertFT(event1);
		free2AL = convertFT(event2);
		
		for (int i = 0; i < free1AL.size(); ++i)
		{
			for (int j = 0; j < free2AL.size(); ++j)
			{
				if (free1AL.get(i).startDate.get(Calendar.HOUR_OF_DAY) < free2AL.get(j).startDate.get(Calendar.HOUR_OF_DAY))
					{
					if (free1AL.get(i).endDate.get(Calendar.HOUR_OF_DAY) < free2AL.get(j).endDate.get(Calendar.HOUR_OF_DAY) || 
							(free1AL.get(i).endDate.get(Calendar.HOUR_OF_DAY) == free2AL.get(j).endDate.get(Calendar.HOUR_OF_DAY) && 
							 free1AL.get(i).endDate.get(Calendar.MINUTE) < free2AL.get(j).endDate.get(Calendar.MINUTE)) ||
							 (free1AL.get(i).endDate.get(Calendar.HOUR_OF_DAY) == free2AL.get(j).endDate.get(Calendar.HOUR_OF_DAY) &&
							  free1AL.get(i).endDate.get(Calendar.MINUTE) == free2AL.get(j).endDate.get(Calendar.MINUTE)))
					{
						 GregorianCalendar temp1 = new GregorianCalendar (0,0,0, free2AL.get(j).startDate.get(Calendar.HOUR_OF_DAY), free2AL.get(j).startDate.get(Calendar.MINUTE));
						 GregorianCalendar temp2 = new GregorianCalendar (0,0,0, free1AL.get(i).endDate.get(Calendar.HOUR_OF_DAY), free1AL.get(i).endDate.get(Calendar.MINUTE));
						 freeTimeAL.add(new Event("Free Time", temp1, temp2, false ));
					}
					
					else if (free1AL.get(i).endDate.get(Calendar.HOUR_OF_DAY) > free2AL.get(j).endDate.get(Calendar.HOUR_OF_DAY) ||
								(free1AL.get(i).endDate.get(Calendar.HOUR_OF_DAY) == free2AL.get(j).endDate.get(Calendar.HOUR_OF_DAY) &&
								 free1AL.get(i).endDate.get(Calendar.MINUTE) > free2AL.get(j).endDate.get(Calendar.MINUTE)))
								 {
									 GregorianCalendar temp3 = new GregorianCalendar (0,0,0, free2AL.get(j).startDate.get(Calendar.HOUR_OF_DAY), free2AL.get(j).startDate.get(Calendar.MINUTE));
									 GregorianCalendar temp4 = new GregorianCalendar (0,0,0, free2AL.get(j).endDate.get(Calendar.HOUR_OF_DAY), free2AL.get(j).endDate.get(Calendar.MINUTE));
									 freeTimeAL.add(new Event("Free Time", temp3, temp4, false ));
								 }
					}
				else if (free1AL.get(i).startDate.get(Calendar.MINUTE) <= free2AL.get(j).startDate.get(Calendar.MINUTE) &&
						 free1AL.get(i).startDate.get(Calendar.HOUR_OF_DAY) == free2AL.get(j).startDate.get(Calendar.HOUR_OF_DAY))
					{
						 GregorianCalendar temp1 = new GregorianCalendar (0,0,0, free2AL.get(j).startDate.get(Calendar.HOUR_OF_DAY), free2AL.get(j).startDate.get(Calendar.MINUTE));
						 GregorianCalendar temp2 = new GregorianCalendar (0,0,0, free2AL.get(j).endDate.get(Calendar.HOUR_OF_DAY), free2AL.get(j).endDate.get(Calendar.MINUTE));
						 freeTimeAL.add(new Event("Free Time", temp1, temp2, false ));
					}
					
			}
		}
		
		for (int i = 0; i < free1AL.size(); ++i)
		{
			for (int j = 0; j < free2AL.size(); ++j)
			{
				if (free1AL.get(i).endDate.get(Calendar.HOUR_OF_DAY) > free2AL.get(j).endDate.get(Calendar.HOUR_OF_DAY) || 
						(free1AL.get(i).endDate.get(Calendar.HOUR_OF_DAY) == free2AL.get(j).endDate.get(Calendar.HOUR_OF_DAY)
						&& free1AL.get(i).endDate.get(Calendar.MINUTE) > free2AL.get(j).endDate.get(Calendar.MINUTE)))
				{
					if (free1AL.get(i).startDate.get(Calendar.HOUR_OF_DAY) > free2AL.get(j).startDate.get(Calendar.HOUR_OF_DAY) ||
							(free1AL.get(i).startDate.get(Calendar.HOUR_OF_DAY) == free2AL.get(j).startDate.get(Calendar.HOUR_OF_DAY) &&
							 free1AL.get(i).startDate.get(Calendar.MINUTE) > free2AL.get(j).startDate.get(Calendar.MINUTE)) ||
							 (free1AL.get(i).startDate.get(Calendar.HOUR_OF_DAY) == free2AL.get(j).startDate.get(Calendar.HOUR_OF_DAY) &&
							 free1AL.get(i).startDate.get(Calendar.MINUTE) == free2AL.get(j).startDate.get(Calendar.MINUTE)))
					{
						GregorianCalendar temp5 = new GregorianCalendar (0,0,0, free1AL.get(i).startDate.get(Calendar.HOUR_OF_DAY), free1AL.get(i).startDate.get(Calendar.MINUTE));
						GregorianCalendar temp6 = new GregorianCalendar (0,0,0, free2AL.get(j).endDate.get(Calendar.HOUR_OF_DAY), free2AL.get(j).endDate.get(Calendar.MINUTE));
						freeTimeAL.add(new Event("Free Time", temp5, temp6, false ));	
					}
				}
			}
		}
		
		return freeTimeAL;
	}
	
	private static ArrayList<Event> convertFT(ArrayList<Event> event)
	{
		ArrayList<Event> freeAL = new ArrayList<Event>();
		int i = 0;
		
		if(event.size() == 0)
		{
			GregorianCalendar cal1 = new GregorianCalendar(0,0,0,0,0);
			GregorianCalendar cal2 = new GregorianCalendar(0,0,0, 23, 59);
			freeAL.add(new Event("Free Time", cal1, cal2, false));
			return freeAL;
		}
		
		if (!(event.get(0).startDate.get(Calendar.HOUR_OF_DAY) == 0 && event.get(0).startDate.get(Calendar.MINUTE) == 0))
		{
			GregorianCalendar cal1 = new GregorianCalendar(0,0,0,0,0);
			GregorianCalendar cal2 = new GregorianCalendar(0,0,0,event.get(0).startDate.get(Calendar.HOUR_OF_DAY), event.get(0).startDate.get(Calendar.MINUTE));
			freeAL.add(new Event("Free Time", cal1, cal2, false));
		}
		
		while (i < event.size())
		{
			GregorianCalendar cal3 = new GregorianCalendar(0,0,0,event.get(i).endDate.get(Calendar.HOUR_OF_DAY), event.get(i).endDate.get(Calendar.MINUTE));
			++i;
			if (i > event.size() - 1)
			{
				GregorianCalendar cal4 = new GregorianCalendar(0,0,0,23,59);
				freeAL.add(new Event("Free Time", cal3, cal4, false));
				break;
			}
			GregorianCalendar cal4 = new GregorianCalendar(0,0,0,event.get(i).startDate.get(Calendar.HOUR_OF_DAY), event.get(i).startDate.get(Calendar.MINUTE));
			freeAL.add(new Event("Free Time", cal3, cal4, false));
		}
		
		return freeAL;
	}
}
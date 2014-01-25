package com.muddaflappers.hacktech;

import java.text.*;
import java.util.*;

import android.util.Log;
import android.widget.EditText;

public class Functions {
	
	public static GregorianCalendar toDate(EditText textInput){
	      GregorianCalendar cal = new GregorianCalendar();
	      String text = textInput.getText().toString();
	      String[] tokens = text.split("/");
	      cal.set(Integer.parseInt(tokens[2]), Integer.parseInt(tokens[0])-1, Integer.parseInt(tokens[1]));
	      return cal;
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
	
	public static void writeToText(ArrayList<Event> eventArrayList)
	{
		String writeString = "";
		for (int i = 0; i < eventArrayList.size(); i++)
		{
			writeString += eventArrayList.get(i).title;
			writeString += ", ";
			writeString += eventArrayList.get(i).startDate.get(Calendar.HOUR_OF_DAY);
			writeString += ":";
			writeString += eventArrayList.get(i).startDate.get(Calendar.MINUTE);
			writeString += ", ";
			writeString += eventArrayList.get(i).endDate.get(Calendar.HOUR_OF_DAY);
			writeString += ":";
			writeString += eventArrayList.get(i).endDate.get(Calendar.MINUTE);
			writeString += ", ";
		}
		Log.d("TEST", writeString);
	}
}
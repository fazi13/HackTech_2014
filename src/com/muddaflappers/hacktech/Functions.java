package com.muddaflappers.hacktech;

import java.text.*;
import java.util.*;

import android.widget.EditText;

public class Functions {
	
	public static GregorianCalendar toDate(EditText textInput){
	      DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
	      Date dateObject;
	      Calendar cal;
	      try {
	         dateObject = df.parse(textInput.getText().toString());
	         cal = Calendar.getInstance();
	         cal.setTime(dateObject);
	      } catch (ParseException e) {
	         // TODO Auto-generated catch block
	         e.printStackTrace();
	         cal = new GregorianCalendar();
	      }
	      return (GregorianCalendar) cal;
	}
	
	public static boolean checkIfDateMatch(Event event, Calendar current)
	{
		Calendar eventStart = event.getStartDate();
		Calendar eventEnd = event.getEndDate();
		if(eventStart.get(Calendar.DATE) <= current.get(Calendar.DATE) && eventEnd.get(Calendar.DATE) >= current.get(Calendar.DATE))
			return true;
		else
			return false;
	}

	public static void sortArrayList(ArrayList<Event> events)
	{
		for(int i = 0; i < events.size(); i++)
		{
			// IMPLEMENT FINDING EARLIER DATE
		}
	}
}
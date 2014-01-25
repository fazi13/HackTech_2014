package com.muddaflappers.hacktech;

import java.util.Calendar;

public class Functions {
	
	public boolean checkIfDateMatch(Event event, Calendar current)
	{
		Calendar eventStart = event.getStartDate();
		Calendar eventEnd = event.getEndDate();
		if(eventStart.get(Calendar.DATE) <= current.get(Calendar.DATE) && eventEnd.get(Calendar.DATE) >= current.get(Calendar.DATE))
			return true;
		else
			return false;
	}
}
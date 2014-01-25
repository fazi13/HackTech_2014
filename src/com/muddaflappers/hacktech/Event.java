package com.muddaflappers.hacktech;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class Event implements Comparable<Event>{
	String title;
	GregorianCalendar startDate;
	GregorianCalendar endDate;
	boolean allDay;
	
	public Event (String title, GregorianCalendar startDate, GregorianCalendar endDate, boolean allDay)
	{
		this.title = title;
		this.startDate = startDate;
		this.endDate = endDate;
		this.allDay = allDay;
	}
	
	public void setTitle(String t){
	title = t;	
	}
	
	public String getTitle(){
		return title;
	}
	
	public void setStartDate(GregorianCalendar sd){
		startDate = sd;
	}
	
	public GregorianCalendar getStartDate(){
		return startDate;
	}
	
	public void setEndDate(GregorianCalendar ed){
		endDate = ed;
	}
	
	public GregorianCalendar getEndDate(){
		return endDate;
	}

	public int compareTo(Event other){
		if(this.startDate.before(other.startDate))
			return -1;
		else if(this.startDate.after(other.startDate))
			return 1;
		else
			return 0;
	}
}

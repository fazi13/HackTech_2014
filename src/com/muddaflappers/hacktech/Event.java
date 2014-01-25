package com.muddaflappers.hacktech;

import java.util.GregorianCalendar;

public class Event {
	String title;
	GregorianCalendar startDate;
	GregorianCalendar endDate;
	
	public Event (String title, GregorianCalendar startDate, GregorianCalendar endDate)
	{
		this.title = title;
		this.startDate = startDate;
		this.endDate = endDate;
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

}

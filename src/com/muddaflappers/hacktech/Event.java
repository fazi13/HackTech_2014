package com.muddaflappers.hacktech;

import java.util.GregorianCalendar;

import android.text.format.Time;

public class Event {
	String title;
	GregorianCalendar startDate;
	Time startTime;
	GregorianCalendar endDate;
	Time endTime;
	
	public Event (String title, GregorianCalendar startDate, GregorianCalendar endDate, Time startTime, Time endTime)
	{
		this.title = title;
		this.startDate = startDate;
		this.endDate = endDate;
		this.startTime = startTime;
		this.endTime = endTime;
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
	
	public void setStartTime(Time st){
		startTime = st;
	}
	
	public Time getStartTime(){
		return startTime;
	}
	
	public void setEndTime(Time et){
		endTime = et;
	}
	
	public Time getEndTime(){
		return endTime;
	}

}

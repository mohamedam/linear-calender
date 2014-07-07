package de.uni_hamburg.informatik.mci.linearcalender.model;

import java.util.Date;

import android.text.format.DateUtils;

public class Event {
	
	private long id; 
	private String titleEvent; 
	private String ort; 
	private String startEvent; 
	private String startTime;
	private String EndEvent; 
	private String endTime;
	private String description;
	
	
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getTitleEvent() {
		return titleEvent;
	}
	public void setTitleEvent(String titleEvent) {
		this.titleEvent = titleEvent;
	}
	public String getOrt() {
		return ort;
	}
	public void setOrt(String ort) {
		this.ort = ort;
	}
	public String getStartEvent() {
		return startEvent;
	}
	public void setStartEvent(String startEvent) {
		this.startEvent = startEvent;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndEvent() {
		return EndEvent;
	}
	public void setEndEvent(String endEvent) {
		EndEvent = endEvent;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	} 
	
	

}

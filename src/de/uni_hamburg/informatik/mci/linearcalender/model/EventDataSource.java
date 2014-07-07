package de.uni_hamburg.informatik.mci.linearcalender.model;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class EventDataSource {

	private SQLiteDatabase dataBase;
	private EventDbHelper helper;
	private String[] allColumns = { EventDbHelper.ID_EVENT,
			EventDbHelper.TITLE_EVENT, EventDbHelper.LOCATION_EVENT,
			EventDbHelper.START_EVENT, EventDbHelper.END_EVENT,
			EventDbHelper.DESCRIPTION_EVENT };

	public EventDataSource(Context context) {
		helper = new EventDbHelper(context);
	}

	public void open() {
		dataBase = helper.getWritableDatabase();
	}

	public void close() {
		helper.close();
	}

	public Event createEvent(String event) {
		ContentValues values = new ContentValues();
		values.put(EventDbHelper.ID_EVENT, event);
		long insertId = dataBase.insert(EventDbHelper.EVENT_TABLE_NAME, null,
				values);
		Cursor cursor = dataBase.query(EventDbHelper.EVENT_TABLE_NAME,
				allColumns, EventDbHelper.ID_EVENT + "=" + insertId, null,
				null, null, null);
		cursor.moveToFirst(); 
		Event newEvent = cursorToEvent(cursor); 
		cursor.close(); 
		return newEvent; 
		
	}
	
	public List<Event> getAllEvent(){
		List<Event>events = new ArrayList<Event>();
		Cursor cursor = dataBase.query(EventDbHelper.EVENT_TABLE_NAME,
				allColumns, null, null,null, null, null);
		 cursor.moveToFirst();
		 while (!cursor.isAfterLast()) {
			 Event event =cursorToEvent(cursor);  
			 events.add(event); 
			 cursor.moveToNext(); 
		 }
		 cursor.close(); 
		 return events; 
	}
	
//	  public void deleteEvent(Event event) {
//		    long id = event.getId();
//		    System.out.println("Comment deleted with id: " + id);
//		    helper.delete(EventDbHelper.EVENT_TABLE_NAME, EventDbHelper.ID_EVENT + " = " + id, null);
//		  }
	
	
	private Event cursorToEvent(Cursor cursor){
		Event event = new Event(); 
		event.setId(cursor.getLong(0)); 
		event.setTitleEvent(cursor.getString(1)); 
		event.setDescription(cursor.getString(2)); 
		event.setOrt(cursor.getString(3)); 
		event.setStartEvent(cursor.getString(4)); 
		event.setEndEvent(cursor.getString(5)); 
		event.setStartTime(cursor.getString(6)); 
		event.setEndTime(cursor.getString(7)); 
		return event; 
	}

}

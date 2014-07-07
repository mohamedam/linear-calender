package de.uni_hamburg.informatik.mci.linearcalender.model;

import java.util.Date;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public class EventDbHelper extends SQLiteOpenHelper{
	
	public static final String EVENT_TABLE_NAME = "Event"; 
	public static final String DATABASE_NAME ="Event" + ".db"; 
	public static final int VERSION = 1; 
	public static final String ID_EVENT = "id_event"; 
	public static final String TITLE_EVENT = "title_event"; 
	public static final String LOCATION_EVENT = "location_event"; 
	public static final String DESCRIPTION_EVENT = "Description_event"; 
	public static final String START_EVENT = "start_date"; 
	public static final String END_EVENT = "end_event";

	public EventDbHelper(Context context) {
		super(context, DATABASE_NAME, null, VERSION);
		
	}
	
	
	private void createTable(SQLiteDatabase sqLiteDatabase){
		 String createEvent = "CREATE TABLE " + EVENT_TABLE_NAME+ " (" +
                   BaseColumns._ID +
                   " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                   TITLE_EVENT + " TEXT, " +
                   LOCATION_EVENT + " TEXT, " +
                   DESCRIPTION_EVENT + " TEXT," +
                   START_EVENT + "TEXT," +
                   END_EVENT + "TEXT" + ");";
           sqLiteDatabase.execSQL(createEvent);
	}
	
	

	@Override
	public void onCreate(SQLiteDatabase db) {
		createTable(db); 
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		 db.execSQL("DROP TABLE IF EXISTS " + EVENT_TABLE_NAME);
		    onCreate(db);
		
	}

}

package de.uni_hamburg.informatik.mci.lineracalendar;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import de.uni_hamburg.informatik.mci.linearcalender.model.Event;
import de.uni_hamburg.informatik.mci.linearcalender.model.EventDataSource;

public class EditEvent extends Activity {
	
	private Button saveButton; 
	private Button cancelButton; 
	private EditText title; 
	private EditText ort; 
	private EditText description; 
	private EventDataSource dataSource; 
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		dataSource = new EventDataSource(this); 
		dataSource.open(); 	
//		List<Event>values = dataSource.getAllEvent(); 
//		
		clickCancelButton(); 
		
	}
	
	private void clickCancelButton(){

		cancelButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				return; 
				
			}
		});
		
	}
	private void clickSaveButton(){
		final Event event = null; 
	 
		saveButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
//				event= dataSource.createEvent(event)
			}
		}); 
	}
	
	

}

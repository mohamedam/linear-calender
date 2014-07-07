package de.uni_hamburg.informatik.mci.lineracalendar;

import java.util.Calendar;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.Time;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import de.uni_hamburg.informatik.mci.lineracalendar.view.ViewCalender;

public class Zooming extends Activity {

	private ViewCalender viewCalender;

	private ActionBar mActionBar;
	private TextView viewYear;
	private TextView viewmonth;
	private TextView viewDay;
	private TextView viewWeekDay;
	private Time mCurrentDate = new Time();
	private int test;
	private Intent newEvent; 
	private Intent preferenceActivity; 

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		viewCalender = new ViewCalender(getApplicationContext());

		
		setContentView(R.layout.day_view);
		mActionBar = getActionBar();
		mActionBar.setDisplayShowTitleEnabled(false);
		mActionBar.setDisplayShowHomeEnabled(false);

		mActionBar.setDisplayUseLogoEnabled(false);
		mActionBar.setDisplayHomeAsUpEnabled(false);
		mActionBar.setCustomView(R.layout.date_action_bar);
		configureActionBar();
		Calendar cal = Calendar.getInstance();
		

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.zooming, menu);

		return super.onCreateOptionsMenu(menu);
	}



	@SuppressLint("InlinedApi")
	private void configureActionBar() {
		mActionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		viewYear = (TextView) mActionBar.getCustomView().findViewById(
				R.id.actionBar_year);
		viewmonth = (TextView) mActionBar.getCustomView().findViewById(
				R.id.actionBar_month);
		viewDay = (TextView) mActionBar.getCustomView().findViewById(
				R.id.actionBar_day);
		viewWeekDay = (TextView) mActionBar.getCustomView().findViewById(
				R.id.actionBar_week_day);

	}

}

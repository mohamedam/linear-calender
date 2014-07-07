package de.uni_hamburg.informatik.mci.lineracalendar.view;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.v4.view.GestureDetectorCompat;
import android.text.format.Time;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.OverScroller;
import de.uni_hamburg.informatik.mci.lineracalendar.R;
import de.uni_hamburg.informatik.mci.lineracalendar.utilities.CalendarData;
import de.uni_hamburg.informatik.mci.lineracalendar.utilities.ModuleConstants;
import de.uni_hamburg.informatik.mci.lineracalendar.utilities.Utility;

public class ViewCalender extends View {

	private final static String TAG = "DaysListView";
	private final int NUMBER_OF_MONTH = 12;
	private final int NUMBER_OF_HOURS = 24;
	private final int VALUE_OF_VISIBITY_WEEK = 13;
	private final int VALUE_OF_VISIBITY_DAY = 30;
	private final int VALUE_OF_VISIBITY_HOURS = 192;
	private final int VALUE_OF_VISIBITY_LABEL_HOURS = 880;
	private int mYearLength;
	public String mYear;
	private Rect mYearRect = new Rect();
	private float[] mDayLinies;
	private static int DEFAULT_CELL_HEIGHT = 8;
	private static float mScale = 0;
	
	private Handler mHandler;
	private WeiterScroll mWeiterScroll = new WeiterScroll();
	
	private Paint bgpaint = new Paint();
	private int mCellHeightmin = 8;
	public static int mcellHeight = 0;
	private int mcellHeightmax = 6600;
	private static int mcellHeightHours = 17;
	private int mViewWidth;
	private int mViewHeight;
	private float mStartingSpanY = 0;
	private ScaleGestureDetector mScaleGestureDetector;
	private GestureDetectorCompat mGestureDetector;
	private OverScroller mScroller;
	private Resources resources;
	private static int COLOR_LABEL;
	private static int MONTH_TEXT_SIZE;
	private static int WEEK_TEXT_SIZE;
	public static int DAY_TEXT_SIZE;
	private static int HOURS_TEXT_SIZE; 
	private static int M_LABEL_BACKGROUNG;
	private static int D_LABEL_BACKGROUND;
	private static int SATURDAY_SUNDAY_COLOR;
	private static int M_HORIZONTAL_INNERE_LINIE;
	private static final float INNERE_LINIE_WIDTH = 2;
	private int TextHeight;
	private static final int MONTH_GAP = 1;
	private static final int DAY_GAP = 5;
	private static final int HOUR_GAP = 1;
	private String[] mDay2Letter;
	private String[] mMonthLabelAbrev;
	private int mFirstVisibleDate;
	private boolean mRemeasure = true;
	protected final Resources mResources;
	private int mSelectionDay;
	private int mCellHeightBeforeScaleGesture;
	private boolean mStartingScroll = false;
	private float mInitialScrollY;
	private boolean mCenter = false;
	private float mGestureCenterMonth = 0;
	private float mCenterOfView = 0;
	public static int mViewStartY;
	private int mScrollStartY;
	private int mMaxViewStartY;
	private int mFirstDay= -1;
	private int mFirstDayOffset;
	private boolean mScrolling = false;
	private static final int SELECTION_HIDDEN = 0;
	private boolean mOnFlingCalled;
	private Time mCurrentTime = new Time();;
	private int mFirstJulianDay;
	private int mFirstDayOfWeek;
	private Rect mPrevRect = new Rect();
	private int mBackgroundselectedHours;
	private int newEventColor;
	private float SIZE_TEXT_NEW_EVENT = 12;
	private int numberShowDays;
	public static String PARAMS_MONTH_VIEW;



	@SuppressLint("NewApi")
	public ViewCalender(Context context) {
		super(context);
		mResources = context.getResources();
		mScroller = new OverScroller(context);
	

		init(context);

	}

	public ViewCalender(Context context, AttributeSet attrs) {
		super(context, attrs);
		// mBaseTime = new Time(Utility.getTimeZone(context, mTZUpdater));
		// mcurrentTime = new Time(Utility.getTimeZone(context, mTZUpdater));
		// calculateDay();
		

		mResources = context.getResources();
		if (mScale == 0) {

			mScale = mResources.getDisplayMetrics().density;
		}
		if (mScale != 1) {
			DEFAULT_CELL_HEIGHT *= mScale;
		}

		init(context);

	}

	private void init(Context context) {
		Calendar calendar = Calendar.getInstance();
		// mCurrentTime.set(1, 0, calendar.get(Calendar.YEAR));

		mFirstDayOfWeek = Utility.getFirstDayOfWeek();


		// context = mContext;
		mScroller = new OverScroller(context);
		mHandler = new Handler();
		mScaleGestureDetector = new ScaleGestureDetector(context,
				new ScaleGesture());
		mGestureDetector = new GestureDetectorCompat(context,
				new DetectorGesture());
		resources = context.getResources();

		COLOR_LABEL = resources.getColor(R.color.calender_month_label);
		MONTH_TEXT_SIZE = (int) resources.getDimension(R.dimen.month_text_size);
		WEEK_TEXT_SIZE = (int) resources.getDimension(R.dimen.week_text_size);
		DAY_TEXT_SIZE = (int) resources.getDimension(R.dimen.day_text_size);
		HOURS_TEXT_SIZE = (int) resources.getDimension(R.dimen.hour_text_size); 
		M_LABEL_BACKGROUNG = resources
				.getColor(R.color.calendar_month_background);
		D_LABEL_BACKGROUND = resources
				.getColor(R.color.calender_day_background);
		M_HORIZONTAL_INNERE_LINIE = resources
				.getColor(R.color.line_inner_horizontal_color);
		SATURDAY_SUNDAY_COLOR = resources
				.getColor(R.color.saturday_sunday_color);
		mBackgroundselectedHours = resources
				.getColor(R.color.calender_background_selected_hour);
		newEventColor = resources.getColor(R.color.new_event_text_color);

		if (mcellHeight == 0) {
			mcellHeight = DEFAULT_CELL_HEIGHT;
		}
		mYearLength = mCurrentTime.getActualMaximum(Time.YEAR_DAY) + 1;
		mDayLinies = new float[mYearLength * NUMBER_OF_HOURS * 4];
		mDay2Letter = new String[mYearLength];
		mMonthLabelAbrev = new String[12];
		SimpleDateFormat sdf = new SimpleDateFormat("E", Locale.GERMANY);

		int dayIndex = 0;

		while (dayIndex + 1 < mYearLength) {
			for (int i = Calendar.SUNDAY; i <= Calendar.SATURDAY; i++) {
				int index = i - Calendar.SUNDAY;
				calendar.set(Calendar.DAY_OF_WEEK, index);
				sdf.setCalendar(calendar);
				mDay2Letter[index] = sdf.format(calendar.getTime());

				// mDay2Letter[index] =
				// CalendarData.dayLabel[index].substring(0, 1);
				mDay2Letter[dayIndex + index] = mDay2Letter[index];
			}
			dayIndex = dayIndex + Calendar.SATURDAY;

		}

		for (int month = 0; month < 12; month++) {
			mMonthLabelAbrev[month] = CalendarData.monthLabelG[month]
					.substring(0, 3);
			if (CalendarData.monthLabelG[month].length() <= 4) {
				mMonthLabelAbrev[month] = CalendarData.monthLabelG[month];
			}
		}
	
		

	}

	@SuppressLint("NewApi")
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		mViewHeight = h;
		mViewWidth = w;
	
		Paint p = new Paint();
		p.setTextSize(MONTH_TEXT_SIZE);
		TextHeight = (int) Math.abs(p.ascent());

		messen(w, h);

	}

	private void messen(int width, int height) {

		mMaxViewStartY = (mcellHeight * mYearLength) - height;
		// mColumn = mMaxViewStartY / mYearLength ;
		if (mViewStartY > mMaxViewStartY) {
			mViewStartY = mMaxViewStartY;
			positionTheFirstDay();
		}
		if (mFirstDay == -1) {

			mFirstDayOffset = 0;
		}
		Calendar calendar = Calendar.getInstance();
		if (mcellHeight > 50) {
			mYear = Integer.toString(calendar.get(Calendar.YEAR));
		}

		// mViewStartY = mFirstMonth * mcellHeight - mFirstMonthOffset;
		// mViewStartY = 0;
		System.out
				.println("mViewStartY is ------------------------------------------------+++++++++++++++++-:"
						+ mViewStartY);
		System.out.println("mFirstMonth is ---------------------:   "
				+ mFirstDay);

		System.out.println("the mYear is --------------------------------:::::::::::::::"
						+ mYear);

	}

	// public static int updateTitle(){
	// Time start = new Time(mCurrentTime);
	// start.normalize(true);
	// Time end = new Time(start);
	// String formatDate = null;
	// String year = Integer.toString(mCurrentTime.year);
	// // String month = Integer.toString(mCurrentTime.)
	// if(mcellHeight < 38){
	// formatDate = year;
	// }if(mcellHeight > 38 && mcellHeight < 880){
	// formatDate = Integer.toString(mCurrentTime.year) +
	// Integer.toString(mCurrentTime.month);
	// }

	// }

	
	

	public int numberOfDayinMonth(int month) {
		int numberOfDay = Utility.getNumberofDaysOfMonth(mCurrentTime, month);
		return numberOfDay * mViewHeight;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {

		int action = event.getAction();

		if ((event.getActionMasked() == MotionEvent.ACTION_DOWN)
				|| (event.getActionMasked() == MotionEvent.ACTION_UP)
				|| (event.getActionMasked() == MotionEvent.ACTION_POINTER_UP)
				|| (event.getActionMasked() == MotionEvent.ACTION_POINTER_DOWN)) {

			mCenter = true;
		}

		mScaleGestureDetector.onTouchEvent(event);

		switch (action) {
		case MotionEvent.ACTION_DOWN:

			mGestureDetector.onTouchEvent(event);
			return true;
		case MotionEvent.ACTION_POINTER_DOWN:

			

			return true;
		case MotionEvent.ACTION_MOVE:

			mGestureDetector.onTouchEvent(event);

			return true;
		case MotionEvent.ACTION_UP:
			
			mStartingScroll = false;
			mGestureDetector.onTouchEvent(event);



			return true;
		case MotionEvent.ACTION_CANCEL:
			mGestureDetector.onTouchEvent(event);
		
			return true;
		default:
			if (mGestureDetector.onTouchEvent(event)) {
				return true;
			}
			return super.onTouchEvent(event);
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {

		if (mRemeasure) {
			messen(getWidth(), getHeight());
			mRemeasure = false;
		} 
		canvas.save();

		float translateY = -mViewStartY;
		canvas.translate(0, translateY);
		Rect rect = mYearRect;
		rect.top = (int) translateY;
		rect.bottom = (int) (mViewHeight - translateY);
		rect.left = ModuleConstants.M_MARGIN_LEFT;
		rect.right = mViewWidth;
		canvas.save();
		canvas.clipRect(rect);
		Paint p = bgpaint;
		Rect r = mYearRect;

		doDrawBgColorLabel(r, canvas, p);
		doDrawBgColorLabelForDay(r, canvas, p);
		doDrawLabelOfMonth(r, canvas, p);

		if (mcellHeight > VALUE_OF_VISIBITY_WEEK
				&& mcellHeight < VALUE_OF_VISIBITY_DAY) {

			doDrawFirstDayOfWeek(r, canvas, p);

		}

		if (mcellHeight >= VALUE_OF_VISIBITY_DAY) {

			doDrawLabelDayForMonthAndWeek(r, canvas, p);
//			doDrawSelectedDay(r, canvas, p);

		}

		if (mcellHeight > VALUE_OF_VISIBITY_HOURS) {
			doDrawHours(r, canvas, p);

		}

		if (mcellHeight > VALUE_OF_VISIBITY_LABEL_HOURS) {

			for (int day = 0; day < 3; day++) {
				int viewingDay = (mViewStartY / mcellHeight) + (day - 1);
				doDrawLabelHours(r, canvas, p, viewingDay);
			}

		}

		doDrawDay(r, canvas, p);
		canvas.restore();

	}

	

	private void doDrawSelectedDay(Rect r, Canvas canvas, Paint p) {
		// mSelectionDay = (mViewStartY/mcellHeight)* 4;
		
		r.top = mSelectionDay * (mcellHeight + HOUR_GAP);
		r.bottom = r.top + mcellHeight + HOUR_GAP;
		r.left = ModuleConstants.M_MARGIN_RIGHT_FOR_DAY_LEVEL_2;
		r.right = mViewWidth;
		saveSelectionPosition(r.left, r.right, r.top, r.bottom);
		p.setColor(mBackgroundselectedHours);
		r.top += HOUR_GAP;
		r.right -= DAY_GAP;
		p.setAntiAlias(false);
		p.setColor(Color.BLUE);
		canvas.drawRect(r, p);

		p.setColor(newEventColor);
		p.setStyle(Paint.Style.FILL);
		p.setTextSize(SIZE_TEXT_NEW_EVENT);
		p.setTextAlign(Paint.Align.LEFT);
		p.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
		canvas.drawText("+", r.left + 6,
				r.top + Math.abs(p.getFontMetrics().ascent) + 2, p);

	}

	private void saveSelectionPosition(float left, float right, float top,
			float bottom) {
		mPrevRect.left = (int) left;
		mPrevRect.bottom = (int) bottom;
		mPrevRect.right = (int) right;
		mPrevRect.top = (int) top;
	}

	private void setSelectionDay(float day) {
		mSelectionDay = (int) day;
	}

	private void setSelectionFromPosition(int x, float y){
		
		  

	          
	      if(x < ModuleConstants.M_MARGIN_LEFT_FOR_DAY_LEVEL_2){
	        	x= ModuleConstants.M_MARGIN_LEFT_FOR_DAY_LEVEL_2; 
	        }else {
	        	x = mViewWidth; 	
	        }
	      float ajustedY = y; 
//	      if (ajustedY < mFirstDayOffset){
//	    	  setSelectionDay(mSelectionDay + mFirstDay- 1);
//	      }else{
	     
	    	  setSelectionDay(ajustedY  / (mcellHeight -HOUR_GAP) );
		
	}

	

	/**
	 * zeichen alle Line der Tages
	 * 
	 * @param rect
	 *            : rechteck, die der Tages Linie enthält
	 * @param canvas
	 *            :
	 * @param p
	 */

	private void doDrawDay(Rect rect, Canvas canvas, Paint p) {
		int index = 0;

		// die Eigenschaften der Aussehen von days
		p.setColor(M_HORIZONTAL_INNERE_LINIE);
		p.setStrokeWidth(INNERE_LINIE_WIDTH);
		p.setAntiAlias(false);
		int mYLinie = 0;
		// day Linie zeichen.
		for (int day = 0; day < (mYearLength * NUMBER_OF_HOURS); day++) {

			if (day % NUMBER_OF_HOURS == 0) {
				// Linie Koordonate auf dem Bildschirm des Tages x1, y1, x2, y2
				// x1(begin der linie auf x-Axis), y1(begin der linie auf
				// y-Axis), x2(Ende der linie auf -Axis),
				// y2(Ende der linie auf y-Axis).
				mDayLinies[index++] = ModuleConstants.M_MARGIN_LEFT_FOR_DAY_LEVEL_2;
				mDayLinies[index++] = mYLinie;
				mDayLinies[index++] = ModuleConstants.M_MARGIN_RIGHT_FOR_DAY_LEVEL_2;
				mDayLinies[index++] = mYLinie;
				mYLinie += mcellHeight;
				if (mcellHeight > 80) {
					mDayLinies[index++] = ModuleConstants.M_MARGIN_LEFT_FOR_DAY_LEVEL_2;
					mDayLinies[index++] = mYLinie;
					mDayLinies[index++] = mViewWidth;
					mDayLinies[index++] = mYLinie;
					// p.setColor(Color.RED);
				}
			}
		}
		// zeichen alle Linies         
		canvas.drawLines(mDayLinies, 0,index, p);
                                       
	}

	private void doDrawHours(Rect rect, Canvas canvas, Paint p) {
		int linieIndex = 0;
		p.setColor(M_HORIZONTAL_INNERE_LINIE);
		p.setStrokeWidth(INNERE_LINIE_WIDTH);
		p.setAntiAlias(false);
		int mYLinie = 0;

		for (int day = 0; day < (mYearLength * NUMBER_OF_HOURS); day++) {

			mDayLinies[linieIndex++] = ModuleConstants.M_MARGIN_LEFT_FOR_DAY_LEVEL_2;
			mDayLinies[linieIndex++] = mYLinie / (NUMBER_OF_HOURS);
			if (mcellHeight < 2000) {
				mDayLinies[linieIndex++] = ModuleConstants.M_MARGIN_RIGHT_FOR_DAY_LEVEL_2;
			} else {
				mDayLinies[linieIndex++] = mViewWidth;
			}
			mDayLinies[linieIndex++] = mYLinie / (NUMBER_OF_HOURS);
			mYLinie += mcellHeight;

		}
		canvas.drawLines(mDayLinies, 0, linieIndex, p);
	}

	private void doDrawBgColorLabel(Rect r, Canvas canvas, Paint p) {

		r.left = ModuleConstants.M_MARGIN_LEFT;
		r.right = ModuleConstants.M_MONTH_MARGIN_RIGHT;
		r.bottom = mYearRect.bottom;
		r.top = mYearRect.top;
		p.setColor(M_LABEL_BACKGROUNG);
		p.setStyle(Style.FILL);

		p.setAntiAlias(true);

		canvas.drawRect(r, p);

	}

	private void doDrawBgColorLabelForDay(Rect r, Canvas canvas, Paint p) {
		int color = D_LABEL_BACKGROUND;
		r.left = ModuleConstants.M_MAGRIN_LEFT_FOR_BG_DAY;
		r.bottom = mYearRect.bottom;
		r.top = mYearRect.top;
		p.setStyle(Style.FILL);
		p.setAntiAlias(true);
		p.setColor(color);
		canvas.drawRect(r, p);
		int x = 0;
		int numberOfDays = 1;
		int dayOfWeek = 0;
		for (int month = 0; month < NUMBER_OF_MONTH; month++) {
			for (int day = 1; day <= Utility.getNumberofDaysOfMonth(
					mCurrentTime, month); day++) {
				if (numberOfDays < 7) {
					dayOfWeek = numberOfDays;
					numberOfDays++;
				} else {
					numberOfDays = 0;
					dayOfWeek = numberOfDays;
					numberOfDays++;
				}
				//TODO mcellmax + 10 for the color 
				if (mcellHeight < mcellHeightmax + 10) {
					if (dayOfWeek == Time.SATURDAY + 1) {
						color = SATURDAY_SUNDAY_COLOR;
						r.left = ModuleConstants.M_MAGRIN_LEFT_FOR_BG_DAY;
						p.setStyle(Style.FILL);
						p.setAntiAlias(true);
						p.setColor(color);
						r.top = x;
						r.bottom = x + mcellHeight;
						canvas.drawRect(r, p);

					}
					if (dayOfWeek % 7 == Time.SUNDAY + 1) {
						color = SATURDAY_SUNDAY_COLOR;
						r.left = ModuleConstants.M_MAGRIN_LEFT_FOR_BG_DAY;
						r.top = x;
						r.bottom = x + mcellHeight;
						p.setStyle(Style.FILL);
						p.setAntiAlias(true);
						p.setColor(color);
						canvas.drawRect(r, p);

					}
					x += mcellHeight;

				}
			}
		}

	}


	private String testmethode(int allDay) {
		int numbers = 0;
		for (int month = 0; month < NUMBER_OF_MONTH - 1; month++) {
			int numberOfDayCurrentMonth = Utility.getNumberofDaysOfMonth(
					mCurrentTime, month);
			int numberOfDayNextMonth = Utility.getNumberofDaysOfMonth(
					mCurrentTime, month + 1);
			numbers += numberOfDayCurrentMonth;
			if (numbers < allDay && allDay < numbers + numberOfDayNextMonth) {
				Log.e(TAG,
						"the viewing month is ------------------------------------"
								+ CalendarData.monthLabelG[month + 1]);
				return CalendarData.monthLabelG[month + 1];
			}

		}
		return null;

	}
	
	

	private void doDrawLabelOfMonth(Rect r, Canvas canvas, Paint p) {

		p.setColor(COLOR_LABEL);
		p.setTextSize(MONTH_TEXT_SIZE);
		p.setTypeface(Typeface.DEFAULT);
		p.setTextAlign(Paint.Align.LEFT);
		p.setAntiAlias(true);

		int y = MONTH_GAP + TextHeight + ModuleConstants.M_MARGIN_LEFT;

		String monthLabel;
		for (int i = 0; i < NUMBER_OF_MONTH; i++) {
			monthLabel = mMonthLabelAbrev[i];
			if (monthLabel.length() < 4 && monthLabel != mMonthLabelAbrev[4]) {
				monthLabel = monthLabel + ".";
			}
			canvas.drawText(monthLabel,
					ModuleConstants.MONTH_MARGIN_LABEL_LEFT, y, p);
			y += mcellHeight * Utility.getNumberofDaysOfMonth(mCurrentTime, i)
					+ MONTH_GAP;
		}

	}

	private void doDrawLabelDay(String dayStr, int day, int countOfDay,
			int cell, Canvas canvas, Paint p) {
		// draw Label of Month day
		int dateNum = mFirstVisibleDate + day;
		int x;

		// if (dateNum > mMont) {
		// dateNum -= mMonthLength;
		// }

		String dateNumStr = String.valueOf(dateNum);

		p.setAntiAlias(true);
		// draw label of Month

		p.setTextSize(WEEK_TEXT_SIZE);
		p.setTypeface(Typeface.DEFAULT);
		p.setTextAlign(Paint.Align.CENTER);
		p.setAntiAlias(true);
		x = ModuleConstants.DAY_MARGIN_LABEL_LEFT;
		int y = compteYPosition(day) + DAY_GAP + TextHeight
				+ (countOfDay * mcellHeight) + ModuleConstants.D_MARGIN_LEFT;
		canvas.drawText(dateNumStr, x, y, p);

		// Draw Day of week
		x = ModuleConstants.WEEK_DAY_LABEL_MARGIN_LEFT;
		if (dateNumStr != String.valueOf(1)) {
			canvas.drawText(dayStr, x, y, p);
		}

	}

	private int compteYPosition(int day) {

		return ((day - 1) * mcellHeight);

	}

	private void doDrawLabelDayForMonthAndWeek(Rect r, Canvas canvas, Paint p) {
		p.setTextAlign(Paint.Align.CENTER);
		// mCurrentTime.setToNow();

		int cell = mFirstJulianDay;

		int countOfDay = 0;
		int numberOfDays = 1;
		int dayOfWeek = 0;
		// Rect blockDate ;
		for (int month = 0; month < NUMBER_OF_MONTH; month++, cell++) {
			for (int day = 1; day <= Utility.getNumberofDaysOfMonth(
					mCurrentTime, month); day++) {
				int color = COLOR_LABEL;

				if (numberOfDays < 7) {
					dayOfWeek = numberOfDays ;
					numberOfDays++;
				} else {
					numberOfDays = 0;
					dayOfWeek = numberOfDays; 
					numberOfDays++;
				}
				if (dayOfWeek == Time.SATURDAY + 1) {
					color = SATURDAY_SUNDAY_COLOR;

				}
				if (dayOfWeek % 7 == 1) {
					color = SATURDAY_SUNDAY_COLOR;

				}

				final int column = day % 7;
				if (Utility.isSaturday(column, mFirstDayOfWeek)) {
					color = SATURDAY_SUNDAY_COLOR;
				} else if (Utility.isSunday(column, mFirstDayOfWeek)) {
					color = SATURDAY_SUNDAY_COLOR;
				}
				p.setColor(color);
				doDrawLabelDay(mDay2Letter[dayOfWeek], day, countOfDay, cell,
						canvas, p);

			}
			countOfDay += Utility.getNumberofDaysOfMonth(mCurrentTime, month);

		}
	}

	private void doDrawLabelHours(Rect r, Canvas canvas, Paint p, int dayView) {
		p.setTextAlign(Paint.Align.CENTER);
		p.setColor(COLOR_LABEL);
		p.setTextSize(HOURS_TEXT_SIZE); 
		int x = ModuleConstants.HOUR_MARGIN_LABEL_LEFT;
		int k = (int) ((HOUR_GAP + TextHeight + ModuleConstants.D_MARGIN_LEFT));
		int y = 0;
		// int place = getWeek(week) * mcellHeight;
		// for (int day = 0; day < 7; day++) {
		for (int hours = 0; hours < NUMBER_OF_HOURS; hours++) {
			canvas.drawText(CalendarData.hourLabel[hours], x,
					(dayView * mcellHeight) + k + (y / NUMBER_OF_HOURS), p);
			y += mcellHeight;
		}
		// }
	}

	private void doDrawFirstDayOfWeek(Rect r, Canvas canvas, Paint p) {
		p.setColor(COLOR_LABEL);
		p.setTextSize(WEEK_TEXT_SIZE);
		p.setTypeface(Typeface.DEFAULT);
		p.setTextAlign(Paint.Align.LEFT);
		p.setAntiAlias(true);
		int numberOfDays = 1;
		int dayOfWeek = 0;

		int y = DAY_GAP + TextHeight + ModuleConstants.D_MARGIN_LEFT - 1;
		int x = ModuleConstants.DAY_MARGIN_LABEL_LEFT - 8;

		for (int month = 0; month < NUMBER_OF_MONTH; month++) {
			for (int day = 1; day <= Utility.getNumberofDaysOfMonth(
					mCurrentTime, month); day++) {

				if (numberOfDays < 7) {
					dayOfWeek = numberOfDays;
					numberOfDays++;
				} else {
					numberOfDays = 0;
					dayOfWeek = numberOfDays;
					numberOfDays++;
				}
 
				if (dayOfWeek % 7 == 2) {
					canvas.drawText(Integer.toString(day), x, y, p);

				}

				y += mcellHeight;
			}
		}
	}
	
	
	

	private class ScaleGesture extends ScaleGestureDetector.SimpleOnScaleGestureListener {

		

		@SuppressLint("NewApi")
		@Override
		public boolean onScaleBegin(ScaleGestureDetector detector) {

			float gesturePixelInCenter = detector.getFocusY();
			// mCenterOfdetector = (int) (gesturePixelInCenter * mcellHeight);

			mGestureCenterMonth = (mViewStartY + gesturePixelInCenter)/ mcellHeight;
			System.out.println("the mView Start y for the on scale Begin is "+ mGestureCenterMonth);
			mStartingSpanY = Math.max(mCellHeightmin,
					Math.abs(detector.getCurrentSpanY()));
			mCellHeightBeforeScaleGesture = mcellHeight;

			return true;
		}

		@Override
		public boolean onScale(ScaleGestureDetector detector) {
			// Log.e(VIEW_LOG_TAG, "test on Scale");

			//Gibt der Maximumzwischen der mcellheightmin und die durchschnittliche Y Abstand zwischen zwei finger, 
			// die Geste durch den Brennpunkt bilden. 
			float spanY = Math.max(mCellHeightmin, detector.getCurrentSpanY());
			// es wird der height des tages gerechnet durch der Skalierung  auf der Y axis
			mcellHeight = (int) (mCellHeightBeforeScaleGesture * spanY / mStartingSpanY);

			if (mcellHeight < mCellHeightmin) {
				mStartingSpanY = spanY;
				mcellHeight = mCellHeightmin;
				mCellHeightBeforeScaleGesture = mCellHeightmin;
			}
			float gesturePixelInCenter = detector.getFocusY();

			mViewStartY = (int) ((mGestureCenterMonth * mcellHeight) - gesturePixelInCenter);
			mMaxViewStartY = (mYearLength) * mcellHeight;
			// mGestureCenterMonth = gesturePixelInCenter / mcellHeight;

			if (mViewStartY < 0) {
				mViewStartY = 0;
				mGestureCenterMonth = (mViewStartY + gesturePixelInCenter)
						/ (float) (mcellHeight);
			} else if (mViewStartY > mMaxViewStartY) {
				mViewStartY = mMaxViewStartY;
				mGestureCenterMonth = (mViewStartY + gesturePixelInCenter)
						/ (float) (mcellHeight);
			}

			System.out.println("the mGestureCenterMonth ist --------------------------------------------o"
							+ mGestureCenterMonth);

			System.out.println("the small mcellHeightHours is ....................:          "
							+ mcellHeightHours);

			System.out.println("the height of cell is -------:  " + mcellHeight);
			System.out.println("the maximal view is ::::::::::::::::::::::::o:::::::::::::::"
							+ mMaxViewStartY);
			numberShowDays = (mViewStartY / mcellHeight) + 1;

			Calendar calendar = Calendar.getInstance();
			if (mcellHeight > 50) {
				mYear = Integer.toString(calendar.get(Calendar.YEAR));
			}

			positionTheFirstDay();

			mRemeasure = true;
			// mcellHeight *= detector.getScaleFactor();

			if (mcellHeight > mcellHeightmax) {
				mcellHeight = mcellHeightmax;
			}

			invalidate();
			return true;
		}

		@Override
		public void onScaleEnd(ScaleGestureDetector detector) {
			mStartingSpanY = 0;

		}
	}

	private void positionTheFirstDay() {
	
		mFirstDay = (mViewStartY + mcellHeight - 1) / mcellHeight;
		
		mFirstDayOffset = mFirstDay * (mcellHeight) - mViewStartY;

	}

	private class DetectorGesture extends
			GestureDetector.SimpleOnGestureListener {

	
		
		@Override
		public void onLongPress(MotionEvent e) {
		
			super.onLongPress(e);
			int x = (int) e.getX(); 
			int y = (int) e.getY(); 
			setSelectionFromPosition(x, y); 
		}

		@Override
		public boolean onDown(MotionEvent e) {

			// mTouchMode = TOUCH_MODE_DOWN;
			mOnFlingCalled = false;
			mHandler.removeCallbacks(mWeiterScroll);
			System.out.println(" the mcontinuescroll ist -------------------: "
					+ mWeiterScroll.toString());
			
//			setSelectionFromPosition(x, y); 
//			System.out.println("the Month of the rectangle is ----------------------::::::::::::::::::::::"+mSelectionDay);
//			System.out.println("the y coordinate is ++++++++++++++++++++++++++++++++  ++++" + y);
//
//			mDownTimeTouching = System.currentTimeMillis(); 
////			postDelayed(mSetClick, mDownDayTime);

			invalidate();
			return true;
		}

		@SuppressLint("NewApi")
		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2,
				float distanceX, float distanceY) {
			if (mStartingScroll) {
				mInitialScrollY = 0;
				mStartingScroll = false;
			}
			mInitialScrollY += distanceY;

			float focusY = getAverageY(e2);
			mCenterOfView = getAverageY(e2);

			if (mCenter) {
				mGestureCenterMonth = (mViewStartY + focusY) / mcellHeight;
				mCenter = false;
			}
			// if (mTouchMode == TOUCH_MODE_DOWN) {
			// int absDistance = Math.abs(distance);
			mScrollStartY = mViewStartY;
			// }

			mViewStartY = (int) ((mGestureCenterMonth * mcellHeight) - focusY);

			if (mViewStartY < 0) {
				mViewStartY = 0;
				mCenter = true;
			} else if (mViewStartY > mMaxViewStartY) {
				mViewStartY = mMaxViewStartY;
				mCenter = true;
			}
			if (mCenter) {
				// Calculate the hour that correspond to the average of the Y
				// touch points
				mGestureCenterMonth = (mViewStartY + focusY) / mcellHeight;
				System.out
						.println("mGestureCenterMonth beim Scrollen ist --------: "
								+ mGestureCenterMonth);

				mCenter = false;
			}
			positionTheFirstDay();
			// }
			mScrolling = true;
			numberShowDays = (mViewStartY / mcellHeight) + 1;
			PARAMS_MONTH_VIEW = testmethode(numberShowDays);

			System.out
					.println("the mscorll for the Month is ......................XXXXXXXXXXXXXXXXXXXXXXXXXXX"
							+ PARAMS_MONTH_VIEW);

			invalidate();
			// mYearRect.top = (int) (mYearRect.top - distanceY);
			return true;

		}

		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			mOnFlingCalled = true;
			mScrolling = true;
			mScroller.fling(0, mViewStartY, 0, (int) -velocityY, 0, 0, 0,
					mMaxViewStartY);
			
			

			mHandler.post(mWeiterScroll);
			return true;
		}

		

		
		private float getAverageY(MotionEvent me) {
			int count = me.getPointerCount();
			float focusY = 0;
			for (int i = 0; i < count; i++) {
				focusY += me.getY(i);
			}
			focusY /= count;
			return focusY;
		}
	}



	private class WeiterScroll implements Runnable {

		public void run() {
			mScrolling = mScrolling && mScroller.computeScrollOffset();

			mViewStartY = mScroller.getCurrY();

		

			if (mScrollStartY == 0 || mScrollStartY == mMaxViewStartY) {
				// Allow overscroll/springback only on a fling,
				// not a pull/fling from the end
				if (mViewStartY < 0) {
					mViewStartY = 0;
				} else if (mViewStartY > mMaxViewStartY) {
					mViewStartY = mMaxViewStartY;
				}
			}

			positionTheFirstDay();
			mHandler.post(this);
			invalidate();
		}
	}

}

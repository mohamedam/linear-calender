package de.uni_hamburg.informatik.mci.linearcalender.model;



public class EventForm {

	private int mCellMargin = 0;
	private float mMinuteHeight;
	private float mHourGap;
	private float mMinEventHeight;
	
	
	public void setmCellMargin(int mCellMargin) {
		this.mCellMargin = mCellMargin;
	}
	public void setHourHeight(float height) {
		this.mMinuteHeight = height / 60.0f;
	}
	public void setmHourGap(float mHourGap) {
		this.mHourGap = mHourGap;
	}
	public void setmMinEventHeight(float mMinEventHeight) {
		this.mMinEventHeight = mMinEventHeight;
	}
	
	
	public boolean compteEventRect(int date, int left, int top, int cellWidth, Event event){
		return true; 
		
	}
	
	

}

package de.uni_hamburg.informatik.mci.lineracalendar.utilities;

public class Interval {

	private float lowBound;
	private float upperBound;
	
	public Interval(){
		
	}

	public Interval(float lowBound, float upperBound) {
		this.lowBound = lowBound;
		this.upperBound = upperBound;
	}

	public Interval(final Interval i) {
		this(i.getLowBound(), i.getUpperBound());
	}

	public void setLowBound(float lowBound) {
		this.lowBound = lowBound;
	}

	public void setUpperBound(float upperBound) {
		this.upperBound = upperBound;
	}

	public float getLowBound() {
		return lowBound;
	}

	public float getUpperBound() {
		return upperBound;
	}

	public boolean contains(final float i) {

		return i >= getLowBound() && i <= getUpperBound();
	}

}

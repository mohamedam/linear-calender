package de.uni_hamburg.informatik.mci.lineracalendar.utilities;



import android.annotation.TargetApi;
import android.os.Build;
import android.view.ScaleGestureDetector;


public class ScaleGestureDetectorCompat {
   
	
	/**
	 * diese klasse wird beim Zooming einen View benutzt, um die komptabilität zu gewährleisten  
	 */
    private ScaleGestureDetectorCompat() {
    }

    
    public static float getCurrentSpanX(ScaleGestureDetector scaleGestureDetector) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            return scaleGestureDetector.getCurrentSpanX();
        } else {
            return scaleGestureDetector.getCurrentSpan();
        }
    }

   
    public static float getCurrentSpanY(ScaleGestureDetector scaleGestureDetector) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            return scaleGestureDetector.getCurrentSpanY();
        } else {
            return scaleGestureDetector.getCurrentSpan();
        }
    }
}

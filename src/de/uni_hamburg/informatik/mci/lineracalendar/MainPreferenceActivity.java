package de.uni_hamburg.informatik.mci.lineracalendar;



import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;




public class MainPreferenceActivity extends PreferenceActivity implements Preference.OnPreferenceChangeListener{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Display the fragment as the main content.
		getFragmentManager().beginTransaction()
				.replace(android.R.id.content, new PrefFragment()).commit();
		
		
	}
	
	


	public static class PrefFragment extends PreferenceFragment {
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			// use deprecated way to work with preferences for older android
			// versions
			if (Build.VERSION.SDK_INT < 11) {
				addPreferencesFromResource(R.xml.preferences);
			}
			
			
			PreferenceManager.setDefaultValues(getActivity(),
					R.xml.preferences, false);

			
			addPreferencesFromResource(R.xml.preferences);
		}
	}




	@Override
	public boolean onPreferenceChange(Preference arg0, Object arg1) {
		return false;
	}
}
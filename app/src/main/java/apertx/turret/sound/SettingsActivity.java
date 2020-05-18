package apertx.turret.sound;

import android.os.*;
import android.preference.*;

public class SettingsActivity extends PreferenceActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		if (PreferenceManager.getDefaultSharedPreferences(this).getBoolean("light", true) == false)
			setTheme(android.R.style.Theme_Material);
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.layout.settings);
	}
}

package com.ue.uebook;

import android.content.Intent;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

import com.ue.uebook.HomeActivity.HomeScreen;

public class SettingsActivity  extends PreferenceActivity implements Preference.OnPreferenceChangeListener{

    @SuppressWarnings("deprecation")
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Add preferences
        addPreferencesFromResource(R.xml.settings_preferences);

        // Bind the summaries of EditText/List/Dialog/Ringtone preferences to
        // their values. When their values change, their summaries are updated
        // to reflect the new value, per the Android Design guidelines.
        bindPreferenceSummaryToValue(findPreference("theme"));
        bindPreferenceSummaryToValue(findPreference("font_size"));
//        bindPreferenceSummaryToValue(findPreference("sort_by"));

//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            addPreferencesFromResource(R.xml.settings_preferences_md);
//            SharedPreferences pref = getSharedPreferences(getPackageName() + "_preferences", Context.MODE_PRIVATE);
//            findPreference("direct_edit").setOnPreferenceChangeListener(this);
//            findPreference("direct_edit").setEnabled(!pref.getBoolean("markdown", false));
//
//            findPreference("markdown").setOnPreferenceChangeListener(this);
//            findPreference("markdown").setEnabled(!pref.getBoolean("direct_edit", false));
//        }
    }

    /**
     * A preference value change listener that updates the preference's summary
     * to reflect its new value.
     */
    private static Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object value) {
            String stringValue = value.toString();

            if (preference instanceof ListPreference) {
                // For list preferences, look up the correct display value in
                // the preference's 'entries' list.
                ListPreference listPreference = (ListPreference) preference;
                int index = listPreference.findIndexOfValue(stringValue);


                // Set the summary to reflect the new value.
                preference.setSummary(index >= 0 ? listPreference.getEntries()[index] : null);
            } else {
                // For all other preferences, set the summary to the value's
                // simple string representation.
                preference.setSummary(stringValue);
            }

            return true;
        }
    };

    /**
     * Binds a preference's summary to its value. More specifically, when the
     * preference's value is changed, its summary (line of text below the
     * preference title) is updated to reflect the value. The summary is also
     * immediately updated upon calling this method. The exact display format is
     * dependent on the type of preference.
     *
     * @see #sBindPreferenceSummaryToValueListener
     */
    private static void bindPreferenceSummaryToValue(Preference preference) {
        // Set the listener to watch for value changes.
        preference
                .setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);

        // Trigger the listener immediately with the preference's
        // current value.
        sBindPreferenceSummaryToValueListener.onPreferenceChange(
                preference,
                PreferenceManager.getDefaultSharedPreferences(
                        preference.getContext()).getString(preference.getKey(),
                        "")

        );

    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean onPreferenceChange(Preference preference, Object value) {
        switch(preference.getKey()) {
            case "direct_edit":
                findPreference("markdown").setEnabled(!(Boolean) value);
                break;
            case "markdown":
                findPreference("direct_edit").setEnabled(!(Boolean) value);
                break;

        }

        return true;
    }
    private void gotoh(){
        Intent intent = new Intent(SettingsActivity.this, HomeScreen.class);
        startActivity(intent);
    }
}

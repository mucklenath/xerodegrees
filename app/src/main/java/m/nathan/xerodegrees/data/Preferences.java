package m.nathan.xerodegrees.data;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;
import m.nathan.xerodegrees.R;

public class Preferences {

    /**
     * Returns the location stored in preferences
     */

    public static String getPreferredWeatherLocation(Context context) {
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(context);
        String keyForLocation = context.getString(R.string.pref_location_key);
        String defaultLocation = context.getString(R.string.pref_location_default);
        return prefs.getString(keyForLocation, defaultLocation);
    }

    /**
     * Returns true if the user has selected metric in the preferences
     */
    public static boolean isMetric(Context context) {
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(context);
        String keyForUnits = context.getString(R.string.pref_units_key);
        String defaultUnits = context.getString(R.string.pref_units_metric);
        String preferredUnits = prefs.getString(keyForUnits, defaultUnits);
        String metric = context.getString(R.string.pref_units_metric);
        boolean userPrefersMetric;
        userPrefersMetric = metric.equals(preferredUnits);
        return userPrefersMetric;
    }
}
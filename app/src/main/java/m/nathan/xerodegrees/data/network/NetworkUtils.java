package m.nathan.xerodegrees.data.network;

import android.content.Context;
import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import m.nathan.xerodegrees.R;
import m.nathan.xerodegrees.data.Preferences;

public class NetworkUtils {
    private static final String TAG = NetworkUtils.class.getSimpleName();

    private static final String OPEN_WEATHER_URL =
            "http://api.openweathermap.org/data/2.5/forecast";

    private static final String format = "json";
    private static final String units = "metric";
    private static final String QUERY_PARAM = "q";
    private static final String APP_ID_PARAM = "appid";
    private static final String FORMAT_PARAM = "mode";
    private static final String UNITS_PARAM = "units";


    /**
     * @return URL for weather request based on current location
     */
    static URL getUrl(Context context) {
        String locationQuery = Preferences.getPreferredWeatherLocation(context);
        return buildUrlWithLocationQuery(locationQuery, context);
    }

    public static URL buildUrlWithLocationQuery(String locationQuery, Context context) {
        Uri weatherQueryUri = Uri.parse(OPEN_WEATHER_URL).buildUpon()
                .appendQueryParameter(QUERY_PARAM, locationQuery)
                .appendQueryParameter(APP_ID_PARAM, context.getString(R.string.open_weather_app_id))
                .appendQueryParameter(FORMAT_PARAM, format)
                .appendQueryParameter(UNITS_PARAM, units)
                .build();

        try {
            URL weatherQueryUrl = new URL(weatherQueryUri.toString());
            return weatherQueryUrl;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            String response = null;
            if (hasInput) {
                response = scanner.next();
            }
            scanner.close();
            return response;
        } finally {
            urlConnection.disconnect();
        }
    }
}

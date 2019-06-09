package m.nathan.xerodegrees.data.network;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.util.Date;

import androidx.annotation.Nullable;
import m.nathan.xerodegrees.data.database.WeatherEntry;

/**
 * Parser for OpenWeatherMap JSON data.
 */
final class OpenWeatherJsonParser {

    private static final String LIST = "list";
    private static final String DATETIME = "dt";

    private static final String MAIN = "main";
    private static final String PRESSURE = "pressure";
    private static final String HUMIDITY = "humidity";

    private static final String WIND = "wind";
    private static final String WINDSPEED = "speed";
    private static final String WIND_DIRECTION = "deg";

    //Temperatures
    private static final String MAX = "temp_max";
    private static final String MIN = "temp_min";

    private static final String WEATHER = "weather";
    private static final String WEATHER_ID = "id";

    private static final String MESSAGE_CODE = "cod";

    private static boolean hasHttpError(JSONObject forecastJson) throws JSONException {
        if (forecastJson.has(MESSAGE_CODE)) {
            int errorCode = forecastJson.getInt(MESSAGE_CODE);

            switch (errorCode) {
                case HttpURLConnection.HTTP_OK:
                    return false;
                case HttpURLConnection.HTTP_NOT_FOUND:
                    // Location invalid
                default:
                    // Server probably down
                    return true;
            }
        }
        return false;
    }

    private static WeatherEntry[] fromJson(final JSONObject forecastJson) throws JSONException {
        JSONArray jsonWeatherArray = forecastJson.getJSONArray(LIST);

        WeatherEntry[] weatherEntries = new WeatherEntry[jsonWeatherArray.length()];

        for (int i = 0; i < jsonWeatherArray.length(); i++) {
            JSONObject dayForecast = jsonWeatherArray.getJSONObject(i);
            WeatherEntry weather = fromEntryJson(dayForecast);
            weatherEntries[i] = weather;
        }
        return weatherEntries;
    }

    private static WeatherEntry fromEntryJson(final JSONObject dayForecast) throws JSONException {
        long dateTimeMillis = dayForecast.getLong(DATETIME) * 1000;

        JSONObject mainObject = dayForecast.getJSONObject(MAIN);
        double pressure = mainObject.getDouble(PRESSURE);
        int humidity = mainObject.getInt(HUMIDITY);
        double max = mainObject.getDouble(MAX);
        double min = mainObject.getDouble(MIN);

        JSONObject windObject = dayForecast.getJSONObject(WIND);
        double windSpeed = windObject.getDouble(WINDSPEED);
        double windDirection = windObject.getDouble(WIND_DIRECTION);

        JSONObject weatherObject =
                dayForecast.getJSONArray(WEATHER).getJSONObject(0);
        int weatherId = weatherObject.getInt(WEATHER_ID);

        return new WeatherEntry(weatherId, new Date(dateTimeMillis), min, max,
                humidity, pressure, windSpeed, windDirection);
    }

    @Nullable
    WeatherResponse parse(final String forecastJsonStr) throws JSONException {
        JSONObject forecastJson = new JSONObject(forecastJsonStr);

        // Error?
        if (hasHttpError(forecastJson)) {
            return null;
        }

        WeatherEntry[] weatherForecast = fromJson(forecastJson);

        return new WeatherResponse(weatherForecast);
    }
}
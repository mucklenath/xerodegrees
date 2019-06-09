package m.nathan.xerodegrees.data.network;

import androidx.annotation.NonNull;
import m.nathan.xerodegrees.data.database.WeatherEntry;

/**
 * Weather response from owm. Forecast consists of an array of WeatherEntry's.
 */
class WeatherResponse {

    @NonNull
    private final WeatherEntry[] mWeatherForecast;

    public WeatherResponse(@NonNull final WeatherEntry[] weatherForecast) {
        mWeatherForecast = weatherForecast;
    }

    public WeatherEntry[] getWeatherForecast() {
        return mWeatherForecast;
    }
}
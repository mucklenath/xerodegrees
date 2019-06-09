package m.nathan.xerodegrees.data.network;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.net.URL;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import m.nathan.xerodegrees.AppExecutors;
import m.nathan.xerodegrees.data.database.WeatherEntry;

public class WeatherNetworkDataSource {
    private static final String LOG_TAG = WeatherNetworkDataSource.class.getSimpleName();

    private static WeatherNetworkDataSource sInstance;
    private final Context mContext;

    // LiveData storing the latest downloaded weather forecasts
    private final MutableLiveData<WeatherEntry[]> mDownloadedWeatherForecasts;
    private final AppExecutors mExecutors;

    private WeatherNetworkDataSource(Context context, AppExecutors executors) {
        mContext = context;
        mExecutors = executors;
        mDownloadedWeatherForecasts = new MutableLiveData<WeatherEntry[]>();
    }

    /**
     * Get the singleton for this class
     */
    public static WeatherNetworkDataSource getInstance(Context context, AppExecutors executors) {
        Log.d(LOG_TAG, "Getting the network data source");
        if (sInstance == null) {
            sInstance = new WeatherNetworkDataSource(context.getApplicationContext(), executors);
            Log.d(LOG_TAG, "Made new network data source");
        }
        return sInstance;
    }

    public LiveData<WeatherEntry[]> getCurrentWeatherForecasts() {
        return mDownloadedWeatherForecasts;
    }

    /**
     * Starts an intent service to fetch the weather.
     */
    public void startFetchWeatherService() {
        Intent intentToFetch = new Intent(mContext, WeatherSyncIntentService.class);
        mContext.startService(intentToFetch);
        Log.d(LOG_TAG, "Service created");
    }

    /**
     * Request OWM for latest weather
     */
    void fetchWeather() {
        Log.d(LOG_TAG, "Fetch weather started");
        mExecutors.networkIO().execute(() -> {
            try {

                URL weatherRequestUrl = NetworkUtils.getUrl(mContext);

                String jsonWeatherResponse = NetworkUtils.getResponseFromHttpUrl(weatherRequestUrl);
                WeatherResponse response = new OpenWeatherJsonParser().parse(jsonWeatherResponse);

                if (response != null && response.getWeatherForecast().length != 0) {
                    //Response was successfully parsed, update weather forecast
                    mDownloadedWeatherForecasts.postValue(response.getWeatherForecast());
                }
            } catch (Exception e) {
                //Error with request
                e.printStackTrace();
            }
        });
    }

}
package m.nathan.xerodegrees.data.network;

import android.app.IntentService;
import android.content.Intent;

import m.nathan.xerodegrees.utils.InjectorUtils;

/**
 * IntentService for requesting weather data off of the main thread
 */
public class WeatherSyncIntentService extends IntentService {

    public WeatherSyncIntentService() {
        super("WeatherSyncIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        WeatherNetworkDataSource networkDataSource =
                InjectorUtils.provideNetworkDataSource(this.getApplicationContext());
        networkDataSource.fetchWeather();
    }
}
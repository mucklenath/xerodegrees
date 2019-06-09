package m.nathan.xerodegrees.data;

import android.util.Log;

import java.util.Date;
import java.util.List;

import androidx.lifecycle.LiveData;
import m.nathan.xerodegrees.AppExecutors;
import m.nathan.xerodegrees.data.database.ListWeatherEntry;
import m.nathan.xerodegrees.data.database.WeatherDao;
import m.nathan.xerodegrees.data.database.WeatherEntry;
import m.nathan.xerodegrees.data.network.WeatherNetworkDataSource;
import m.nathan.xerodegrees.utils.DateUtils;

/**
 * Repository to handle interaction between WeatherNetworkDataSource and WeatherDao
 */
public class WeatherRepository {
    private static final String LOG_TAG = WeatherRepository.class.getSimpleName();

    private static WeatherRepository sInstance;
    private final WeatherDao mWeatherDao;
    private final WeatherNetworkDataSource mWeatherNetworkDataSource;
    private final AppExecutors mExecutors;
    private boolean mInitialised = false;

    private WeatherRepository(WeatherDao weatherDao,
                              WeatherNetworkDataSource weatherNetworkDataSource,
                              AppExecutors executors) {
        mWeatherDao = weatherDao;
        mWeatherNetworkDataSource = weatherNetworkDataSource;
        mExecutors = executors;

        // Observe LiveData and update database
        LiveData<WeatherEntry[]> networkData = mWeatherNetworkDataSource.getCurrentWeatherForecasts();
        networkData.observeForever(newForecastsFromNetwork -> {
            mExecutors.diskIO().execute(() -> {
                // Delete old  data
                deleteOldData();
                Log.d(LOG_TAG, "Old weather deleted");
                // Insert new data into the database
                mWeatherDao.bulkInsert(newForecastsFromNetwork);
                Log.d(LOG_TAG, "New values inserted");
            });
        });
    }

    public synchronized static WeatherRepository getInstance(
            WeatherDao weatherDao, WeatherNetworkDataSource weatherNetworkDataSource,
            AppExecutors executors) {
        if (sInstance == null) {
            sInstance = new WeatherRepository(weatherDao, weatherNetworkDataSource,
                    executors);
        }
        return sInstance;
    }

    /**
     * Initialise and check if a sync is required
     */
    private synchronized void initialiseData() {

        if (mInitialised) return;
        mInitialised = true;

        mExecutors.diskIO().execute(() -> {
            startFetchWeatherService();
        });
    }

    /**
     * Database related operations
     **/

    public LiveData<List<ListWeatherEntry>> getCurrentWeatherForecasts() {
        initialiseData();
        Date today = DateUtils.getNormalizedUtcDateForToday();
        return mWeatherDao.getCurrentWeatherForecasts(today);
    }

    public LiveData<WeatherEntry> getWeatherByDate(Date date) {
        initialiseData();
        return mWeatherDao.getWeatherByDate(date);
    }

    /**
     * Delete old weather data that won't be displayed
     */
    private void deleteOldData() {
        Date today = DateUtils.getNormalizedUtcDateForToday();
        mWeatherDao.deleteOldWeather(today);
    }


    /**
     * Network related operation
     */
    private void startFetchWeatherService() {
        mWeatherNetworkDataSource.startFetchWeatherService();
    }

}
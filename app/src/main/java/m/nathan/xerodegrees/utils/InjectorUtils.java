package m.nathan.xerodegrees.utils;

import android.content.Context;

import m.nathan.xerodegrees.AppExecutors;
import m.nathan.xerodegrees.ui.DetailViewModelFactory;
import m.nathan.xerodegrees.data.WeatherRepository;
import m.nathan.xerodegrees.data.database.WeatherDatabase;
import m.nathan.xerodegrees.data.network.WeatherNetworkDataSource;
import m.nathan.xerodegrees.ui.MainViewModelFactory;

import java.util.Date;

/**
 * Provides static methods to inject classes
 */
public class InjectorUtils {

    public static WeatherRepository provideRepository(Context context) {
        WeatherDatabase database = WeatherDatabase.getInstance(context.getApplicationContext());
        AppExecutors executors = AppExecutors.getInstance();
        WeatherNetworkDataSource networkDataSource =
                WeatherNetworkDataSource.getInstance(context.getApplicationContext(), executors);
        return WeatherRepository.getInstance(database.weatherDao(), networkDataSource, executors);
    }

    public static WeatherNetworkDataSource provideNetworkDataSource(Context context) {
        provideRepository(context.getApplicationContext());
        AppExecutors executors = AppExecutors.getInstance();
        return WeatherNetworkDataSource.getInstance(context.getApplicationContext(), executors);
    }

    public static DetailViewModelFactory provideDetailViewModelFactory(Context context, Date date) {
        WeatherRepository repository = provideRepository(context.getApplicationContext());
        return new DetailViewModelFactory(repository, date);
    }

    public static MainViewModelFactory provideMainActivityViewModelFactory(Context context) {
        WeatherRepository repository = provideRepository(context.getApplicationContext());
        return new MainViewModelFactory(repository);
    }

}
package m.nathan.xerodegrees.ui;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import m.nathan.xerodegrees.data.WeatherRepository;
import m.nathan.xerodegrees.data.database.ListWeatherEntry;

class MainActivityViewModel extends ViewModel {

    private final WeatherRepository mRepository;
    private final LiveData<List<ListWeatherEntry>> mForecast;

    public MainActivityViewModel(WeatherRepository repository) {
        mRepository = repository;
        mForecast = mRepository.getCurrentWeatherForecasts();
    }

    public LiveData<List<ListWeatherEntry>> getForecast() {
        return mForecast;
    }


}

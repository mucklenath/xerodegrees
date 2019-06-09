package m.nathan.xerodegrees.ui;

import java.util.Date;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import m.nathan.xerodegrees.data.WeatherRepository;
import m.nathan.xerodegrees.data.database.WeatherEntry;

class DetailActivityViewModel extends ViewModel {

    private final LiveData<WeatherEntry> mWeather;

    private final Date mDate;
    private final WeatherRepository mRepository;

    public DetailActivityViewModel(WeatherRepository repository, Date date) {
        mRepository = repository;
        mDate = date;
        mWeather = mRepository.getWeatherByDate(mDate);
    }

    public LiveData<WeatherEntry> getWeather() {
        return mWeather;
    }
}

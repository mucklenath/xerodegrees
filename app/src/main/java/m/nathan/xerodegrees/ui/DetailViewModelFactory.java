package m.nathan.xerodegrees.ui;

import java.util.Date;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import m.nathan.xerodegrees.data.WeatherRepository;
import m.nathan.xerodegrees.data.database.WeatherEntry;

public class DetailViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final WeatherRepository mRepository;
    private final Date mDate;

    public DetailViewModelFactory(WeatherRepository repository, Date date) {
        this.mRepository = repository;
        this.mDate = date;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        //noinspection unchecked
        return (T) new DetailActivityViewModel(mRepository, mDate);
    }
}
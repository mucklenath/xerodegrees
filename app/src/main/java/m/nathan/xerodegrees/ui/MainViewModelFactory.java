package m.nathan.xerodegrees.ui;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import m.nathan.xerodegrees.data.WeatherRepository;

public class MainViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final WeatherRepository mRepository;

    public MainViewModelFactory(WeatherRepository repository) {
        this.mRepository = repository;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        //noinspection unchecked
        return (T) new MainActivityViewModel(mRepository);
    }
}
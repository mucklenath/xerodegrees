package m.nathan.xerodegrees.ui;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProviders;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import m.nathan.xerodegrees.R;
import m.nathan.xerodegrees.data.database.WeatherEntry;
import m.nathan.xerodegrees.databinding.ActivityDetailBinding;
import m.nathan.xerodegrees.utils.InjectorUtils;
import m.nathan.xerodegrees.utils.DateUtils;
import m.nathan.xerodegrees.utils.WeatherUtils;

import java.util.Date;

/**
 * Displays single day's forecast
 */
public class DetailActivity extends AppCompatActivity implements LifecycleOwner {

    public static final String WEATHER_ID_EXTRA = "WEATHER_ID_EXTRA";

    private ActivityDetailBinding mDetailBinding;
    private DetailActivityViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDetailBinding = DataBindingUtil.setContentView(this, R.layout.activity_detail);
        long timestamp = getIntent().getLongExtra(WEATHER_ID_EXTRA, -1);
        Date date = new Date(timestamp);

        DetailViewModelFactory factory = InjectorUtils.provideDetailViewModelFactory(this.getApplicationContext(), date);
        mViewModel = ViewModelProviders.of(this, factory).get(DetailActivityViewModel.class);


        mViewModel.getWeather().observe(this, weatherEntry -> {
            if (weatherEntry != null) bindWeatherToUI(weatherEntry);
        });

    }

    private void bindWeatherToUI(WeatherEntry weatherEntry) {

        int weatherId = weatherEntry.getWeatherIconId();
        int weatherImageId = WeatherUtils.getLargeArtResourceIdForWeatherCondition(weatherId);
        mDetailBinding.primaryInfo.weatherIcon.setImageResource(weatherImageId);

        long localDateMidnightGmt = weatherEntry.getDate().getTime();
        String dateText = DateUtils.getFriendlyDateString(DetailActivity.this, localDateMidnightGmt, true);
        mDetailBinding.primaryInfo.date.setText(dateText);

        String description = WeatherUtils.getStringForWeatherCondition(DetailActivity.this, weatherId);
        mDetailBinding.primaryInfo.weatherDescription.setText(description);

        double maxInCelsius = weatherEntry.getMax();
        String highString = WeatherUtils.formatTemperature(DetailActivity.this, maxInCelsius);
        mDetailBinding.primaryInfo.highTemperature.setText(highString);

        double humidity = weatherEntry.getHumidity();
        String humidityString = getString(R.string.format_humidity, humidity);
        mDetailBinding.extraDetails.humidity.setText(humidityString);

        double windSpeed = weatherEntry.getWind();
        double windDirection = weatherEntry.getDegrees();
        String windString = WeatherUtils.getFormattedWind(DetailActivity.this, windSpeed, windDirection);
        mDetailBinding.extraDetails.windMeasurement.setText(windString);

        double pressure = weatherEntry.getPressure();
        String pressureString = getString(R.string.format_pressure, pressure);
        mDetailBinding.extraDetails.pressure.setText(pressureString);
    }
}
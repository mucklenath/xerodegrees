package m.nathan.xerodegrees.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;
import m.nathan.xerodegrees.R;
import m.nathan.xerodegrees.data.database.ListWeatherEntry;
import m.nathan.xerodegrees.utils.WeatherUtils;

class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.ForecastAdapterViewHolder> {

    private static final int VIEW_TYPE_TODAY = 0;
    private static final int VIEW_TYPE_FUTURE_DAY = 1;

    private final Context mContext;
    private final ForecastAdapterOnItemClickHandler mClickHandler;
    private List<ListWeatherEntry> mForecast;

    ForecastAdapter(@NonNull Context context, ForecastAdapterOnItemClickHandler clickHandler) {
        mContext = context;
        mClickHandler = clickHandler;
    }

    @Override
    public ForecastAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        int layoutId = getLayoutIdByType(viewType);
        View view = LayoutInflater.from(mContext).inflate(layoutId, viewGroup, false);
        view.setFocusable(true);
        return new ForecastAdapterViewHolder(view);
    }


    @Override
    public void onBindViewHolder(ForecastAdapterViewHolder forecastAdapterViewHolder, int position) {
        ListWeatherEntry currentWeather = mForecast.get(position);

        int weatherIconId = currentWeather.getWeatherIconId();
        int weatherImageResourceId = getImageResourceId(weatherIconId, position);
        forecastAdapterViewHolder.iconView.setImageResource(weatherImageResourceId);

        long dateInMillis = currentWeather.getDate().getTime();
        String dateString = new SimpleDateFormat("E h a").format(dateInMillis);
        forecastAdapterViewHolder.dateView.setText(dateString);

        String description = WeatherUtils.getStringForWeatherCondition(mContext, weatherIconId);
        forecastAdapterViewHolder.descriptionView.setText(description);

        double highInCelsius = currentWeather.getMax();
        String highString = WeatherUtils.formatTemperature(mContext, highInCelsius);
        forecastAdapterViewHolder.highTempView.setText(highString);
    }

    /**
     * Converts the weather icon id from Open Weather to the local image resource id. Returns the
     * correct image based on whether the forecast is for today(large image) or the future(small image).
     *
     * @param weatherIconId Open Weather icon id
     * @param position      Position in list
     * @return Drawable image resource id for weather
     */
    private int getImageResourceId(int weatherIconId, int position) {
        int viewType = getItemViewType(position);

        switch (viewType) {

            case VIEW_TYPE_TODAY:
                return WeatherUtils
                        .getLargeArtResourceIdForWeatherCondition(weatherIconId);

            case VIEW_TYPE_FUTURE_DAY:
                return WeatherUtils
                        .getSmallArtResourceIdForWeatherCondition(weatherIconId);

            default:
                throw new IllegalArgumentException("Invalid view type, value of " + viewType);
        }
    }

    @Override
    public int getItemCount() {
        if (mForecast == null) return 0;
        return mForecast.size();
    }


    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return VIEW_TYPE_TODAY;
        } else {
            return VIEW_TYPE_FUTURE_DAY;
        }
    }

    /**
     * Swaps the list used by the ForecastAdapter for its weather data. This method is called by
     * {@link MainActivity} after a load has finished. When this method is called, we assume we have
     * a new set of data, so we call notifyDataSetChanged to tell the RecyclerView to update.
     *
     * @param newForecast the new list of forecasts to use as ForecastAdapter's data source
     */
    void swapForecast(final List<ListWeatherEntry> newForecast) {
        // If there was no forecast data, then recreate all of the list
        if (mForecast == null) {
            mForecast = newForecast;
            notifyDataSetChanged();
        } else {
            /*
             * Otherwise we use DiffUtil to calculate the changes and update accordingly. This
             * shows the four methods you need to override to return a DiffUtil callback. The
             * old list is the current list stored in mForecast, where the new list is the new
             * values passed in from the observing the database.
             */

            DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return mForecast.size();
                }

                @Override
                public int getNewListSize() {
                    return newForecast.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    return mForecast.get(oldItemPosition).getId() ==
                            newForecast.get(newItemPosition).getId();
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    ListWeatherEntry newWeather = newForecast.get(newItemPosition);
                    ListWeatherEntry oldWeather = mForecast.get(oldItemPosition);
                    return newWeather.getId() == oldWeather.getId()
                            && newWeather.getDate().equals(oldWeather.getDate());
                }
            });
            mForecast = newForecast;
            result.dispatchUpdatesTo(this);
        }
    }

    /**
     * Returns the layout id to use based on the type (today or future list)
     */
    private int getLayoutIdByType(int viewType) {
        switch (viewType) {

            case VIEW_TYPE_TODAY: {
                return R.layout.list_item_forecast_today;
            }

            case VIEW_TYPE_FUTURE_DAY: {
                return R.layout.forecast_list_item;
            }

            default:
                throw new IllegalArgumentException("Invalid view type, value of " + viewType);
        }
    }

    /**
     * The interface that receives onItemClick messages.
     */
    public interface ForecastAdapterOnItemClickHandler {
        void onItemClick(Date date);
    }

    /**
     * Viewholder for Forecast entries in the recyclerview
     */
    class ForecastAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final ImageView iconView;

        final TextView dateView;
        final TextView descriptionView;
        final TextView highTempView;

        ForecastAdapterViewHolder(View view) {
            super(view);

            iconView = view.findViewById(R.id.weather_icon);
            dateView = view.findViewById(R.id.date);
            descriptionView = view.findViewById(R.id.weather_description);
            highTempView = view.findViewById(R.id.high_temperature);

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            Date date = mForecast.get(adapterPosition).getDate();
            mClickHandler.onItemClick(date);
        }
    }
}
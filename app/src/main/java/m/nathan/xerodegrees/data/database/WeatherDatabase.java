package m.nathan.xerodegrees.data.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

/**
 * Room database containing a table of WeatherEntry's
 */

@Database(entities = {WeatherEntry.class}, version = 1, exportSchema = false)
@TypeConverters(DateConverter.class)
public abstract class WeatherDatabase extends RoomDatabase {

    private static final String DATABASE_NAME = "weather";
    private static WeatherDatabase sInstance;

    public static WeatherDatabase getInstance(Context context) {
        if (sInstance == null) {
            sInstance = Room.databaseBuilder(context.getApplicationContext(),
                    WeatherDatabase.class, WeatherDatabase.DATABASE_NAME).build();
        }
        return sInstance;
    }

    public abstract WeatherDao weatherDao();
}

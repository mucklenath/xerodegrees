package m.nathan.xerodegrees.data.database;

import java.util.Date;

import androidx.room.TypeConverter;

/**
 * Room type converter for date to long
 */
class DateConverter {
    @TypeConverter
    public static Date toDate(Long timestamp) {
        return timestamp == null ? null : new Date(timestamp);
    }

    @TypeConverter
    public static Long toTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }
}
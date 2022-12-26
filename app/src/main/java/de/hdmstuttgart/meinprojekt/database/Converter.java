package de.hdmstuttgart.meinprojekt.database;

import androidx.room.TypeConverter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Converter {

    static DateFormat dateFormat = new SimpleDateFormat("yyyy.mm.dd");

    @TypeConverter
    public static Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public static String dateToTimestamp(Date date) {
        return date == null ? null : dateFormat.format(date);
    }
}

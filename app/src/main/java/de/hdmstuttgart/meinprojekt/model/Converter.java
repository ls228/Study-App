package de.hdmstuttgart.meinprojekt.model;

import androidx.room.TypeConverter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Converter {

    static DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");

    @TypeConverter
    public static String dateToTimestamp(Date date) {
        return date == null ? null : dateFormat.format(date);
    }
}

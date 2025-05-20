package com.example.quizdeculturegnrale.db;

import androidx.room.TypeConverter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Converters {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;
    private static final Gson gson = new Gson();

    // LocalDate converters
    @TypeConverter
    public static LocalDate fromString(String value) {
        return value == null ? null : LocalDate.parse(value, formatter);
    }

    @TypeConverter
    public static String fromLocalDate(LocalDate date) {
        return date == null ? null : date.format(formatter);
    }

    // String[] converters
    @TypeConverter
    public static String[] fromStringArray(String value) {
        if (value == null) {
            return null;
        }
        Type listType = new TypeToken<String[]>() {}.getType();
        return gson.fromJson(value, listType);
    }

    @TypeConverter
    public static String fromArray(String[] list) {
        if (list == null) {
            return null;
        }
        return gson.toJson(list);
    }
} 
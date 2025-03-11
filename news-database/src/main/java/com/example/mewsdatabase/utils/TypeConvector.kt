package com.example.mewsdatabase.utils

import androidx.room.TypeConverter
import java.text.DateFormat
import java.util.Date


internal class TypeConvector {
    @TypeConverter
    fun fromTimestamp(value: String?): Date? {
        return value?.let { DateFormat.getDateTimeInstance().parse(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): String? {
        return date?.time?.let { DateFormat.getDateTimeInstance().format(it) }
    }
}
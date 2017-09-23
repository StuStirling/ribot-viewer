package com.stustirling.ribotviewer.data.local.model.converters

import android.arch.persistence.room.TypeConverter
import java.util.*


/**
 * Created by Stu Stirling on 23/09/2017.
 */
class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long): Date {
        return Date(value)
    }

    @TypeConverter
    fun dateToTimestamp(date: Date): Long {
        return date.time
    }
}
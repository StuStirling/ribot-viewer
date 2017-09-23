package com.stustirling.ribotviewer.data.local

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import com.stustirling.ribotviewer.data.local.dao.RibotDao
import com.stustirling.ribotviewer.data.local.model.LocalRibot
import com.stustirling.ribotviewer.data.local.model.converters.Converters

/**
 * Created by Stu Stirling on 23/09/2017.
 */
@Database(entities = arrayOf(LocalRibot::class),version = 1)
@TypeConverters(Converters::class)
abstract class LocalDatabase : RoomDatabase() {
    abstract fun ribotDao() : RibotDao
}

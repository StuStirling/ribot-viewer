package com.stustirling.ribotviewer.data.local

import android.arch.persistence.room.Room
import android.content.Context
import com.stustirling.ribotviewer.data.local.dao.RibotDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by Stu Stirling on 23/09/2017.
 */
@Module
class LocalDataModule(val context: Context) {

    @Singleton
    @Provides
    fun providesLocalDatabase() : LocalDatabase {
        return Room.databaseBuilder(context,
                LocalDatabase::class.java,
                "ribots").build()
    }

    @Singleton
    @Provides
    fun providesRibotDao( localDatabase: LocalDatabase ) : RibotDao {
        return localDatabase.ribotDao()
    }

}
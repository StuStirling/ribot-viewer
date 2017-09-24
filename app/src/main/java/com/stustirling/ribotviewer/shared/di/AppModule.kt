package com.stustirling.ribotviewer.shared.di

import android.content.Context
import com.stustirling.ribotviewer.data.RibotRepositoryImpl
import com.stustirling.ribotviewer.data.api.ApiModule
import com.stustirling.ribotviewer.data.api.RibotService
import com.stustirling.ribotviewer.data.local.LocalDataModule
import com.stustirling.ribotviewer.data.local.dao.RibotDao
import com.stustirling.ribotviewer.shared.di.scopes.ForApplication
import com.stustirling.ribotviewer.domain.RibotRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by Stu Stirling on 23/09/2017.
 */
@Module( includes = arrayOf(
            ApiModule::class,
            LocalDataModule::class))
class AppModule(val context: Context) {

    @Singleton
    @Provides
    @ForApplication
    fun providesApplicationContext() : Context {
        return context
    }

    @Singleton
    @Provides
    fun providesRibotRepository(ribotService: RibotService,
                                ribotDao: RibotDao) : RibotRepository {
        return RibotRepositoryImpl(ribotService,ribotDao)
    }

}
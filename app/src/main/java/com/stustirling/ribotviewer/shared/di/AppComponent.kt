package com.stustirling.ribotviewer.shared.di

import android.content.Context
import com.stustirling.ribotviewer.data.api.ApiModule
import com.stustirling.ribotviewer.data.local.LocalDataModule
import com.stustirling.ribotviewer.shared.di.scopes.ForApplication
import com.stustirling.ribotviewer.domain.RibotRepository
import dagger.Component
import javax.inject.Singleton

/**
 * Created by Stu Stirling on 23/09/2017.
 */
@Singleton
@Component(modules = arrayOf(AppModule::class,
                             LocalDataModule::class,
                             ApiModule::class))
interface AppComponent {

    @ForApplication
    fun applicationContext() : Context
    fun ribotRepository() : RibotRepository

}
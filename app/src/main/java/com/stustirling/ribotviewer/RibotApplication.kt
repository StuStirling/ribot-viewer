package com.stustirling.ribotviewer

import android.app.Application
import com.stustirling.ribotviewer.data.api.ApiModule
import com.stustirling.ribotviewer.data.local.LocalDataModule
import com.stustirling.ribotviewer.shared.di.AppComponent
import com.stustirling.ribotviewer.shared.di.AppModule
import com.stustirling.ribotviewer.shared.di.DaggerAppComponent
import timber.log.Timber

/**
 * Created by Stu Stirling on 23/09/2017.
 */
class RibotApplication  : Application() {
    override fun onCreate() {
        super.onCreate()

        Timber.plant(Timber.DebugTree())
    }

    val appComponent : AppComponent
        get() { return DaggerAppComponent.builder()
                    .apiModule(ApiModule())
                    .appModule(AppModule(this))
                    .localDataModule(LocalDataModule(this))
                    .build() }
}
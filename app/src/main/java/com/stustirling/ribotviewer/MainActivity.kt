package com.stustirling.ribotviewer

import android.os.Bundle
import com.stustirling.ribotviewer.di.AppComponent
import com.stustirling.ribotviewer.di.scopes.ActivityScope
import com.stustirling.ribotviewer.domain.RibotRepository
import javax.inject.Inject

class MainActivity : BaseActivity() {

    override fun layoutRes(): Int = R.layout.activity_main

    @Inject lateinit var ribotRepo : RibotRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun inject(appComponent: AppComponent) {
        DaggerMainActivity_Component.builder()
                .appComponent(appComponent)
                .build()
                .inject(this)
    }

    @dagger.Component(
            dependencies= arrayOf(AppComponent::class)
    )
    @ActivityScope
    interface Component {
        fun inject(activity:MainActivity)
    }
}

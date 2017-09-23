package com.stustirling.ribotviewer

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.stustirling.ribotviewer.di.AppComponent

/**
 * Created by Stu Stirling on 23/09/2017.
 */
abstract class BaseActivity : AppCompatActivity() {

    abstract fun layoutRes() : Int
    abstract fun inject( appComponent: AppComponent)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        inject((application as RibotApplication).appComponent)
        setContentView(layoutRes())
    }
}
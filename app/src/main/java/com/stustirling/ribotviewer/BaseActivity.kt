package com.stustirling.ribotviewer

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.stustirling.ribotviewer.shared.di.AppComponent
import io.reactivex.disposables.CompositeDisposable

/**
 * Created by Stu Stirling on 23/09/2017.
 */
abstract class BaseActivity : AppCompatActivity() {

    abstract fun layoutRes() : Int
    abstract fun inject( appComponent: AppComponent)

    private val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        inject((application as RibotApplication).appComponent)
        setContentView(layoutRes())
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }
}
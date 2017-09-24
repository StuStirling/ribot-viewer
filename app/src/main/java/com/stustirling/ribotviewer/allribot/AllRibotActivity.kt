package com.stustirling.ribotviewer.allribot

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import com.stustirling.ribotviewer.BaseActivity
import com.stustirling.ribotviewer.R
import com.stustirling.ribotviewer.domain.RibotRepository
import com.stustirling.ribotviewer.shared.di.AppComponent
import com.stustirling.ribotviewer.shared.di.scopes.ActivityScope
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_allribot.*
import timber.log.Timber
import javax.inject.Inject

class AllRibotActivity : BaseActivity() {

    override fun layoutRes(): Int = R.layout.activity_allribot

    @Inject lateinit var ribotRepo : RibotRepository
    @Inject lateinit var viewModelFactory: AllRibotViewModel.Factory

    private lateinit var viewModel: AllRibotViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProviders.of(this,viewModelFactory)
                .get(AllRibotViewModel::class.java)

        setup()
    }

    private lateinit var ribotAdapter: AllRibotAdapter

    fun setup() {
        rv_ara_ribots.layoutManager = GridLayoutManager(
                this,2,GridLayoutManager.VERTICAL,false)
        ribotAdapter = AllRibotAdapter()
        rv_ara_ribots.adapter = ribotAdapter

        srl_ara_refresh.isEnabled = false

        viewModel.apply {
            getRibotRetrievedSuccessfully()
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe { ribotAdapter.items = it.sortedBy { it.firstName } }
            getInitialRetrievalFailed()
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe { Timber.d("Initial Retrieval failed") }
        }
    }

    override fun inject(appComponent: AppComponent) {
        DaggerAllRibotActivity_Component.builder()
                .appComponent(appComponent)
                .build()
                .inject(this)
    }

    @dagger.Component(
            dependencies= arrayOf(AppComponent::class)
    )
    @ActivityScope
    interface Component {
        fun inject(activity: AllRibotActivity)
    }
}

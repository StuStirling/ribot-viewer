package com.stustirling.ribotviewer.ui.allribot

import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.view.ViewCompat
import android.support.v7.widget.GridLayoutManager
import com.stustirling.ribotviewer.BaseActivity
import com.stustirling.ribotviewer.R
import com.stustirling.ribotviewer.domain.RibotRepository
import com.stustirling.ribotviewer.model.RibotModel
import com.stustirling.ribotviewer.shared.di.AppComponent
import com.stustirling.ribotviewer.shared.di.scopes.ActivityScope
import com.stustirling.ribotviewer.ui.ribot.RibotActivity
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
        ribotAdapter = AllRibotAdapter { ribot,view ->
            val options = ActivityOptionsCompat
                    .makeSceneTransitionAnimation(this@AllRibotActivity,
                            view,
                            ViewCompat.getTransitionName(view))
            startActivity(Intent(this@AllRibotActivity,RibotActivity::class.java)
                    .apply { putExtra("ribot",ribot) },
                    options.toBundle())
         }
        ribotAdapter.hasStableIds()

        rv_ara_ribots.adapter = ribotAdapter

        srl_ara_refresh.isEnabled = false

        viewModel.apply {
            getRibotRetrievedSuccessfully()
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe { updateRibots(it) }
            getInitialRetrievalFailed()
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe { Timber.d("Initial Retrieval failed") }
        }
    }

    private fun updateRibots(ribots: List<RibotModel>) {
        ribotAdapter.items =
                ribots.sortedBy { it.firstName }
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

package com.stustirling.ribotviewer.ui.allribot

import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.view.ViewCompat
import android.support.v7.widget.GridLayoutManager
import android.widget.Toast
import com.stustirling.ribotviewer.BaseActivity
import com.stustirling.ribotviewer.R
import com.stustirling.ribotviewer.model.RibotModel
import com.stustirling.ribotviewer.shared.di.AppComponent
import com.stustirling.ribotviewer.shared.di.scopes.ActivityScope
import com.stustirling.ribotviewer.ui.ribot.RibotActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_allribot.*
import javax.inject.Inject

class AllRibotActivity : BaseActivity() {

    override fun layoutRes(): Int = R.layout.activity_allribot

    @Inject lateinit var viewModelFactory: AllRibotViewModel.Factory

    private lateinit var viewModel: AllRibotViewModel
    private lateinit var ribotAdapter: AllRibotAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProviders.of(this,viewModelFactory)
                .get(AllRibotViewModel::class.java)

        setup()
    }

    private fun setup() {
        setUpRecyclerView()
        setUpRefreshLayout()
        bindViewModel()
    }

    private fun setUpRecyclerView() {
        rv_ara_ribots.layoutManager = GridLayoutManager(
                this,2, GridLayoutManager.VERTICAL,false)
        ribotAdapter = AllRibotAdapter { ribot,view ->
            val options = ActivityOptionsCompat
                    .makeSceneTransitionAnimation(this@AllRibotActivity,
                            view,
                            ViewCompat.getTransitionName(view))
            startActivity(Intent(this@AllRibotActivity,RibotActivity::class.java)
                    .apply { putExtra(RibotActivity.RIBOT,ribot) },
                    options.toBundle())
        }
        ribotAdapter.hasStableIds()
        rv_ara_ribots.adapter = ribotAdapter
    }

    private fun setUpRefreshLayout() {
        srl_ara_refresh.isEnabled = false
        srl_ara_refresh.setOnRefreshListener { viewModel.triggerRefresh() }
    }

    private fun bindViewModel() {
        viewModel.apply {
            ribotsLoading
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe {
                        if( !srl_ara_refresh.isRefreshing ) srl_ara_refresh.isRefreshing = true }
            ribotsRetrievalSuccessful
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe { updateRibots(it)
                        srl_ara_refresh.isEnabled = true
                        srl_ara_refresh.isRefreshing = false }
            ribotsRetrievalFailed
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe {
                        srl_ara_refresh.isEnabled = true
                        Toast.makeText(this@AllRibotActivity,
                                "Something went wrong. Please try again.",
                                Toast.LENGTH_SHORT).show()
                        srl_ara_refresh.isRefreshing = false
                    }
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

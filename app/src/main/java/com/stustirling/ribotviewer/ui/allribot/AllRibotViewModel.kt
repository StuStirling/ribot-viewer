package com.stustirling.ribotviewer.ui.allribot

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.stustirling.ribotviewer.BaseViewModel
import com.stustirling.ribotviewer.domain.RefreshTrigger
import com.stustirling.ribotviewer.domain.RibotRepository
import com.stustirling.ribotviewer.model.RibotModel
import com.stustirling.ribotviewer.model.toRibotModel
import com.stustirling.ribotviewer.shared.extensions.onlyResourceError
import com.stustirling.ribotviewer.shared.extensions.onlyResourceLoading
import com.stustirling.ribotviewer.shared.extensions.onlyResourceSuccess
import com.stustirling.ribotviewer.shared.extensions.toUnit
import io.reactivex.Flowable
import io.reactivex.flowables.ConnectableFlowable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

/**
 * Created by Stu Stirling on 23/09/2017.
 */

class AllRibotViewModel(ribotRepository: RibotRepository) : BaseViewModel() {


    val ribotsRetrievalSuccessful: ConnectableFlowable<List<RibotModel>>
    val ribotsRetrievalFailed: ConnectableFlowable<Unit>
    private val networkRefresh = RefreshTrigger()
    val ribotsLoading: Flowable<Unit>

    init {
        val connectableRepoCall = ribotRepository.getRibots(networkRefresh)
                .subscribeOn(Schedulers.io())
                .publish()

        ribotsRetrievalSuccessful = connectableRepoCall
                .onlyResourceSuccess()
                .map { it.map { it.toRibotModel() } }
                .replay(1)

        ribotsRetrievalFailed = connectableRepoCall
                .onlyResourceError()
                .toUnit()
                .replay(1)

        ribotsLoading = connectableRepoCall
                .onlyResourceLoading()
                .toUnit()

        compositeDisposable.addAll(
                ribotsRetrievalFailed.connect(),
                ribotsRetrievalSuccessful.connect(),
                connectableRepoCall.connect()
            )
    }

    fun triggerRefresh() = networkRefresh.refresh()

    class Factory @Inject constructor(val ribotRepository: RibotRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>?): T {
            @Suppress("UNCHECKED_CAST")
            return AllRibotViewModel(ribotRepository) as T
        }
    }
}
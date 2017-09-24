package com.stustirling.ribotviewer.ui.allribot

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.stustirling.ribotviewer.BaseViewModel
import com.stustirling.ribotviewer.domain.RibotRepository
import com.stustirling.ribotviewer.model.RibotModel
import com.stustirling.ribotviewer.model.toRibotModel
import com.stustirling.ribotviewer.shared.extensions.onlyResourceError
import com.stustirling.ribotviewer.shared.extensions.onlyResourceSuccess
import com.stustirling.ribotviewer.shared.extensions.toUnit
import io.reactivex.Flowable
import io.reactivex.flowables.ConnectableFlowable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

/**
 * Created by Stu Stirling on 23/09/2017.
 */

class AllRibotViewModel(val ribotRepository: RibotRepository) : BaseViewModel() {


    private var ribotsRetrievalSuccessful: ConnectableFlowable<List<RibotModel>>

    private var ribotsRetrievalFailed: ConnectableFlowable<Unit>

    init {
        val initialConnectableRepoCall = ribotRepository.getRibots()
                .subscribeOn(Schedulers.io())
                .publish()

        ribotsRetrievalSuccessful = initialConnectableRepoCall
                .onlyResourceSuccess()
                .map { it.map { it.toRibotModel() } }
                .replay(1)

        ribotsRetrievalFailed = initialConnectableRepoCall
                .onlyResourceError()
                .toUnit()
                .replay(1)

        compositeDisposable.addAll(
                ribotsRetrievalFailed.connect(),
                ribotsRetrievalSuccessful.connect(),
                initialConnectableRepoCall.connect()
            )
    }

    fun getRibotRetrievedSuccessfully() : Flowable<List<RibotModel>> {
        return ribotsRetrievalSuccessful
    }

    fun getInitialRetrievalFailed() : Flowable<Unit> = ribotsRetrievalFailed

    class Factory @Inject constructor(val ribotRepository: RibotRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>?): T {
            @Suppress("UNCHECKED_CAST")
            return AllRibotViewModel(ribotRepository) as T
        }
    }
}
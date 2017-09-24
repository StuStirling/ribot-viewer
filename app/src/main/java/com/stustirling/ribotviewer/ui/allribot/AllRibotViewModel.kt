package com.stustirling.ribotviewer.ui.allribot

import android.arch.lifecycle.MutableLiveData
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
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

/**
 * Created by Stu Stirling on 23/09/2017.
 */

class AllRibotViewModel(ribotRepository: RibotRepository) : BaseViewModel() {

    private val networkRefresh = RefreshTrigger()

    val ribots = MutableLiveData<List<RibotModel>>()
    val retrievalError = MutableLiveData<Throwable>()
    val loading = MutableLiveData<Boolean>().apply {value = false }

    init {
        val connectableRepoCall = ribotRepository.getRibots(networkRefresh)
                .subscribeOn(Schedulers.io())
                .publish()

        val success = connectableRepoCall
                .onlyResourceSuccess()
                .map { it.map { it.toRibotModel() } }
                .observeOn(AndroidSchedulers.mainThread())

        val error = connectableRepoCall
                .onlyResourceError()
                .map { it.second }
                .observeOn(AndroidSchedulers.mainThread())

        val loadingFlowable = connectableRepoCall
                .onlyResourceLoading()
                .observeOn(AndroidSchedulers.mainThread())

        compositeDisposable.addAll(
                error.subscribe {
                    retrievalError.value = it
                    loading.value = false
                },
                success.subscribe {
                    retrievalError.value = null
                    ribots.value = it
                    loading.value = false},
                loadingFlowable.subscribe { loading.value = true },
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
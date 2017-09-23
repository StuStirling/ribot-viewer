package com.stustirling.ribotviewer.data

import com.stustirling.ribotviewer.domain.Resource
import io.reactivex.Flowable
import io.reactivex.FlowableEmitter
import io.reactivex.Single
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Function
import io.reactivex.schedulers.Schedulers

/**
 * Created by Stu Stirling on 23/09/2017.
 */
abstract class NetworkBoundResource<LocalType, RemoteType, DomainType>(val emitter: FlowableEmitter<Resource<DomainType>>) {
    init {
        create()
    }

    private var firstRetrievalDisposable: Disposable? = null

    private fun create() {
        emitter.onNext(Resource.loading())

        firstRetrievalDisposable = getLocal()

        refreshRemote()
    }

    private fun refreshRemote() {
        createCall()
                .doOnSubscribe { emitter.onNext(Resource.loading()) }
                .subscribeOn(Schedulers.io())
                .map (mapToLocal())
                .subscribe({ localType ->
                    firstRetrievalDisposable?.dispose()
                    saveCallResult(localType)
                    getLocal()
                },{throwable ->
                    emitter.onNext(Resource.error(throwable))
                })
    }

    private fun getLocal() : Disposable {
        return loadFromDb()
                .subscribeOn(Schedulers.io())
                .map(mapToDomain())
                .subscribe { emitter.onNext(Resource.success(it)) }
    }


    // Called to save the result of the API response into the database
    protected abstract fun saveCallResult(item: LocalType)

    // Called to get the cached data from the database
    protected abstract fun loadFromDb(): Flowable<LocalType>

    // Called to create the API call.
    protected abstract fun createCall(): Single<RemoteType>

    protected abstract fun mapToLocal() : Function<RemoteType,LocalType>

    protected abstract fun mapToDomain() : Function<LocalType,DomainType>

}
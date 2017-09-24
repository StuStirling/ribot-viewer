package com.stustirling.ribotviewer.shared.extensions

import com.stustirling.ribotviewer.domain.Resource
import io.reactivex.Flowable

/**
 * Created by Stu Stirling on 23/09/2017.
 */
fun <T> Flowable<Resource<T>>.onlyResourceSuccess() : Flowable<T> {
    return filter { it.status == Resource.Status.SUCCESS }
            .map { it.data }
}

fun <T> Flowable<Resource<T>>.onlyResourceError() : Flowable<Pair<T?,Throwable?>> {
    return filter { it.status == Resource.Status.ERROR}
            .map { Pair(it.data,it.throwable) }
}

fun <T> Flowable<Resource<T>>.onlyResourceLoading() : Flowable<Unit> {
    return filter { it.status == Resource.Status.LOADING }
            .toUnit()
}

fun <T> Flowable<T>.toUnit() : Flowable<Unit> = map { Unit }


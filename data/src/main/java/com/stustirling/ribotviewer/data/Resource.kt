package com.stustirling.ribotviewer.data

import android.support.annotation.Nullable


/**
 * Created by Stu Stirling on 21/09/2017.
 */
//a generic class that describes a data with a status
class Resource<T> private constructor(val status: Status,val data: T?,  val message: String?) {
    companion object {

        fun <T> success(data: T): Resource<T> {
            return Resource(Status.SUCCESS, data, null)
        }

        fun <T> error(data: T? = null, msg: String? = null): Resource<T> {
            return Resource(Status.ERROR, data, msg)
        }

        fun <T> loading(@Nullable data: T): Resource<T> {
            return Resource(Status.LOADING, data, null)
        }
    }

    var throwable: Throwable? = null

    enum class Status {
        SUCCESS,ERROR,LOADING
    }
}

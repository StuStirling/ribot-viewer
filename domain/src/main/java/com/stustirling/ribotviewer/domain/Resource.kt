package com.stustirling.ribotviewer.domain


/**
 * Created by Stu Stirling on 21/09/2017.
 */
//a generic class that describes a data with a status
class Resource<out T> private constructor(val status: Status, val data: T?, val message: String?) {
    companion object {

        fun <T> success(data: T): Resource<T> {
            return Resource(Status.SUCCESS, data, null)
        }

        fun <T> error(data: T? = null, msg: String? = null): Resource<T> {
            return Resource(Status.ERROR, data, msg)
        }

        fun <T> error(throwable: Throwable, msg: String? = null) : Resource<T> {
            return Resource(Status.ERROR,null,msg).apply {
                this.throwable = throwable
            }
        }

        fun <T> loading(data: T? = null): Resource<T> {
            return Resource(Status.LOADING, data, null)
        }
    }

    var throwable: Throwable? = null

    enum class Status {
        SUCCESS,ERROR,LOADING
    }
}

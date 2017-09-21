package com.stustirling.ribotviewer.data.api

import com.stustirling.ribotviewer.data.api.model.ApiRibot
import io.reactivex.Single
import retrofit2.http.GET

/**
 * Created by Stu Stirling on 21/09/2017.
 */
interface RibotService {

    @GET("/ribots")
    fun getRibots() : Single<List<ApiRibot>>

}
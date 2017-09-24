package com.stustirling.ribotviewer.domain

import com.stustirling.ribotviewer.domain.model.Ribot
import io.reactivex.Flowable

/**
 * Created by Stu Stirling on 21/09/2017.
 */
open interface RibotRepository {
    fun getRibots() : Flowable<Resource<List<Ribot>>>
    fun insertRibots(ribots : List<Ribot>)
}
package com.stustirling.ribotviewer.data.local.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.stustirling.ribotviewer.data.local.model.LocalRibot
import io.reactivex.Flowable

/**
 * Created by Stu Stirling on 23/09/2017.
 */
@Dao
interface RibotDao {
    @Query("SELECT * FROM ribots")
    fun getRibots() : Flowable<List<LocalRibot>>

    @Query("SELECT * FROM ribots WHERE id = :id")
    fun getRibot(id:String) : LocalRibot

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(ribots: List<LocalRibot>)
}
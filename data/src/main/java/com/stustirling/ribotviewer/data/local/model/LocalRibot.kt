package com.stustirling.ribotviewer.data.local.model

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import java.util.*

/**
 * Created by Stu Stirling on 23/09/2017.
 */
@Entity(tableName = "ribots")
data class LocalRibot(
        @PrimaryKey var id: String,
        @ColumnInfo(name = "first_name") var firstName: String,
        @ColumnInfo(name = "last_name") var lastName: String,
        var email: String,
        @ColumnInfo(name = "dob") var dateOfBirth: Date,
        var color: String,
        var bio: String? = null,
        var avatar: String? = null,
        @ColumnInfo(name = "active") var isActive: Boolean)